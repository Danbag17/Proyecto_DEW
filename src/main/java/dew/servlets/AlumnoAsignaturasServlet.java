package dew.servlets;

import java.io.IOException;

import dew.client.CentroEducativoClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AlumnoAsignaturasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AlumnoAsignaturasServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Importante:
         * getSession(false) NO crea una sesión nueva.
         * Si no existe sesión, significa que el usuario no ha iniciado sesión.
         */
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("key") == null
                || session.getAttribute("dni") == null) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No hay sesión activa");
            return;
        }

 
        String key = (String) session.getAttribute("key");
        String dni = (String) session.getAttribute("dni");

        CentroEducativoClient cliente = new CentroEducativoClient();

        try {
            
            String asignaturasAlumno = cliente.getAsignaturasAlumno(dni, key);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(asignaturasAlumno);

        } catch (Exception e) {
            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error obteniendo asignaturas del alumno"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}