package dew.servlets;

import java.io.IOException;

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
            String json = new CentroEducativoClient().getAlumnosDeAsignatura(asig, key);
            writeJson(response, json);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error obteniendo alumnos de la asignatura: " + e.getMessage());
        }
    }

    private void writeJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}
