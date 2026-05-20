package dew.servlets;

<<<<<<< HEAD
=======
import java.io.IOException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;
>>>>>>> 083c613 (base)
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class AlumnoDetalleServlet
 */

public class AlumnoDetalleServlet extends HttpServlet {
<<<<<<< HEAD
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlumnoDetalleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
=======
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acronimo = request.getParameter("asig");

        if (acronimo == null || acronimo.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Falta parámetro asig");
            return;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String dni = (String) session.getAttribute("dni");
        String key = (String) session.getAttribute("key");

        CentroEducativoClient client = new CentroEducativoClient();

        try {

        	JsonArray matriculas = JsonParser.parseString(
        		    client.getAsignaturasAlumno(dni, key)
        		).getAsJsonArray();

        		JsonArray asignaturas = JsonParser.parseString(
        		    client.getAsignaturas(key)
        		).getAsJsonArray();

        		
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
            throw new ServletException(
                    "Error obteniendo detalle de asignatura", e);
        }
    }
}

>>>>>>> 083c613 (base)
