package dew.servlets;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dew.client.CentroEducativoClient;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AlumnoDetalleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acronimo = request.getParameter("asig");

        if (acronimo == null || acronimo.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro asig");
            return;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No hay sesión activa");
            return;
        }

        String dni = (String) session.getAttribute("dni");
        String key = (String) session.getAttribute("key");

        if (dni == null || key == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Faltan datos de sesión");
            return;
        }

        CentroEducativoClient client = new CentroEducativoClient();

        try {
            JsonArray matriculas = JsonParser
                    .parseString(client.getAsignaturasAlumno(dni, key))
                    .getAsJsonArray();

            JsonArray asignaturas = JsonParser
                    .parseString(client.getAsignaturas(key))
                    .getAsJsonArray();

            JsonObject matriculaEncontrada = null;

            for (int i = 0; i < matriculas.size(); i++) {
                JsonObject obj = matriculas.get(i).getAsJsonObject();

                String acr = obj.has("asignatura")
                        ? obj.get("asignatura").getAsString()
                        : "";

                if (acronimo.equalsIgnoreCase(acr)) {
                    matriculaEncontrada = obj;
                    break;
                }
            }

            JsonObject asignaturaEncontrada = null;

            for (int i = 0; i < asignaturas.size(); i++) {
                JsonObject obj = asignaturas.get(i).getAsJsonObject();

                String acr = obj.has("acronimo")
                        ? obj.get("acronimo").getAsString()
                        : "";

                if (acronimo.equalsIgnoreCase(acr)) {
                    asignaturaEncontrada = obj;
                    break;
                }
            }

            JsonObject resultado = new JsonObject();

            if (matriculaEncontrada != null) {
                resultado.add("matricula", matriculaEncontrada);
            }

            if (asignaturaEncontrada != null) {
                resultado.add("asignatura", asignaturaEncontrada);
            }

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(resultado.toString());

        } catch (Exception e) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error obteniendo detalle de asignatura: " + e.getMessage()
            );
        }
    }
}