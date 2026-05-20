package dew.servlets;

<<<<<<< HEAD
=======
import java.io.IOException;

import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;
>>>>>>> 083c613 (base)
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

<<<<<<< HEAD
import dew.client.CentroEducativoClient;

/**
 * Servlet implementation class AlumnoAsignaturasServlet
 */

public class AlumnoAsignaturasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlumnoAsignaturasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
        String key = (String) session.getAttribute("key");
        String dni = (String) session.getAttribute("dni");
        System.out.println("KEY = " + key);
        
        if (key != null) {
            CentroEducativoClient cliente = new CentroEducativoClient();
            try {
                String profesoresYAsignaturas = cliente.getAsignaturasAlumno(dni,key);
                //response.setContentType("application/json");
                //response.getWriter().write(profesoresYAsignaturas);  
                
                request.setAttribute("datos", profesoresYAsignaturas);              
                request.getRequestDispatcher("asignaturas.html").forward(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error: " + e.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No API key found in session");
=======
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
            String json = new CentroEducativoClient().getAsignaturasAlumno(dni, key);
            writeJson(response, json);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error obteniendo asignaturas del alumno: " + e.getMessage());
>>>>>>> 083c613 (base)
        }
	}

<<<<<<< HEAD
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

=======
    private void writeJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
>>>>>>> 083c613 (base)
}
