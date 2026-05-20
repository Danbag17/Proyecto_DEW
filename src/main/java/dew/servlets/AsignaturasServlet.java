package dew.servlets;

import java.io.IOException;

import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet genérico para listar todas las asignaturas.
 *
 * Útil para pruebas o vistas comunes. Para la navegación real por rol conviene usar:
 * - AlumnoAsignaturasServlet
 * - ProfesorAsignaturasServlet
 */
public class AsignaturasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionsUtils.isLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No hay sesión activa");
            return;
        }

        String key = SessionsUtils.getKey(request);

        try {
            String json = new CentroEducativoClient().getAsignaturas(key);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error obteniendo asignaturas: " + e.getMessage());
        }
    }
}
