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

        CentroEducativoClient cliente = new CentroEducativoClient();

        try {
            String jsonAlumno = cliente.getAlumnoPorDNI(dni, key);
            String jsonNotas = cliente.getExpediente(dni, key);

            String resultadoFinal = "{"
                    + "\"datosPersonales\":" + jsonAlumno + ","
                    + "\"calificaciones\":" + jsonNotas
                    + "}";

            writeJson(response, resultadoFinal);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al generar el expediente: " + e.getMessage());
        }
    }

<<<<<<< HEAD
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

=======
    private void writeJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
>>>>>>> 083c613 (base)
}
