package dew.servlets;

import java.io.IOException;

import dew.client.CentroEducativoClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ProfesorAsignaturasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No hay sesión");
            return;
        }

        String key = (String) session.getAttribute("key");
        String dni = (String) session.getAttribute("dni");

        System.out.println("ProfesorAsignaturasServlet DNI = " + dni);
        System.out.println("ProfesorAsignaturasServlet KEY = " + key);

        if (key == null || dni == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Faltan datos de sesión");
            return;
        }

        CentroEducativoClient cliente = new CentroEducativoClient();

        try {
            String json = cliente.getAsignaturasProfesor(dni, key);

            System.out.println("Asignaturas profesor JSON = " + json);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error obteniendo asignaturas del profesor");
        }
    }
}
