package dew.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import dew.client.CentroEducativoClient;

/**
 * Servlet implementation class AlumnoExpedienteServlet
 */

public class AlumnoExpedienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlumnoExpedienteServlet() {
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

	        if (key == null || dni == null) {
	            response.sendRedirect("login.html");
	            return;
	        }

	        CentroEducativoClient cliente = new CentroEducativoClient();
	        try {
	            // 1. Obtenemos los datos personales del alumno (Nombre, apellidos...)
	            String jsonAlumno = cliente.getAlumnoPorDNI(dni, key);
	            
	            // 2. Obtenemos las asignaturas con sus notas (La ruta /alumnos/{dni}/asignaturas que vimos en Swagger)
	            String jsonNotas = cliente.getExpediente(dni, key);

	            // 3. Construimos un JSON de respuesta único para el HTML
	            // Combinamos ambos en un solo objeto para que el JS lo maneje fácil
	            String resultadoFinal = "{\"datosPersonales\":" + jsonAlumno + ", \"calificaciones\":" + jsonNotas + "}";

	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(resultadoFinal);

	        } catch (Exception e) {
	            e.printStackTrace();
	            response.sendError(500, "Error al generar el expediente");
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
