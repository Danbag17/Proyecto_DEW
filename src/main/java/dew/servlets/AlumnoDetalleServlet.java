
package dew.servlets;

import java.io.IOException;

import dew.client.CentroEducativoClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AlumnoDetalleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AlumnoDetalleServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("key") == null
                || session.getAttribute("dni") == null) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No hay sesión activa");
            return;
        }

      
        String key = (String) session.getAttribute("key");
        String dni = (String) session.getAttribute("dni");

    
        String asig = request.getParameter("asig");

        if (asig == null || asig.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro asig");
            return;
        }

        CentroEducativoClient cliente = new CentroEducativoClient();

        try {
    
            String detalleAsignatura = cliente.getDetalleAsignaturaAlumno(dni, asig, key);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(detalleAsignatura);

        } catch (Exception e) {
            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error obteniendo detalle de asignatura"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}