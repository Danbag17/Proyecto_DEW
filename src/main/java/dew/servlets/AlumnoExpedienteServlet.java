package dew.servlets;

import java.io.IOException;

import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AlumnoExpedienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionsUtils.isLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No hay sesión activa");
            return;
        }

        if (!request.isUserInRole("rolalu")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo alumnos");
            return;
        }

        String key = SessionsUtils.getKey(request);
        String dni = SessionsUtils.getDni(request);

        try {
            CentroEducativoClient cliente = new CentroEducativoClient();

            String jsonAlumno = cliente.getAlumnoPorDNI(dni, key);
            String jsonNotas = cliente.getExpediente(dni, key);

            String resultadoFinal = "{"
                    + "\"datosPersonales\":" + jsonAlumno + ","
                    + "\"calificaciones\":" + jsonNotas
                    + "}";

            writeJson(response, resultadoFinal);

        } catch (Exception e) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al generar el expediente: " + e.getMessage()
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void writeJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}