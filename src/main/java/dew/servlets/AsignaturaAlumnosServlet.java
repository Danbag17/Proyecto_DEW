package dew.servlets;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AsignaturaAlumnosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionsUtils.isLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No hay sesión activa");
            return;
        }

        if (!request.isUserInRole("rolpro")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo profesores");
            return;
        }

        String asig = request.getParameter("asig");
        if (asig == null || asig.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro asig");
            return;
        }

        String key = SessionsUtils.getKey(request);

        try {
            CentroEducativoClient cliente = new CentroEducativoClient();
            String json = cliente.getAlumnosDeAsignatura(asig, key);

            // El endpoint de alumnos por asignatura solo devuelve dni + nota.
            // Enriquecemos cada alumno con nombre/apellidos para la ficha.
            String enriquecido = enriquecerConNombres(json, key, cliente);

            writeJson(response, enriquecido);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error interno del servidor");
        }
    }

    /*
     * Para cada elemento {alumno, nota} añade nombre y apellidos consultando
     * /alumnos/{dni}. Es "best effort": si una consulta falla (p. ej. por
     * permisos), se deja el alumno tal cual y la vista mostrará el DNI.
     */
    private String enriquecerConNombres(String json, String key, CentroEducativoClient cliente) {
        try {
            JsonArray alumnos = JsonParser.parseString(json).getAsJsonArray();

            for (int i = 0; i < alumnos.size(); i++) {
                JsonObject obj = alumnos.get(i).getAsJsonObject();

                String dni = obj.has("alumno") ? obj.get("alumno").getAsString()
                        : (obj.has("dni") ? obj.get("dni").getAsString() : null);

                if (dni == null || dni.isBlank()) {
                    continue;
                }

                obj.addProperty("dni", dni);

                try {
                    JsonObject datos = JsonParser
                            .parseString(cliente.getAlumnoPorDNI(dni, key))
                            .getAsJsonObject();

                    if (datos.has("nombre")) {
                        obj.add("nombre", datos.get("nombre"));
                    }
                    if (datos.has("apellidos")) {
                        obj.add("apellidos", datos.get("apellidos"));
                    }
                } catch (Exception ignored) {
                    // Sin nombre: la ficha mostrará el DNI.
                }
            }

            return alumnos.toString();
        } catch (Exception e) {
            // Si el JSON no es el array esperado, lo devolvemos sin tocar.
            return json;
        }
    }

    private void writeJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}
