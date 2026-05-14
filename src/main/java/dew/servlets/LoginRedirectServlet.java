package dew.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import dew.client.CentroEducativoClient;

public class LoginRedirectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String dni = request.getParameter("dni");
        String pass = request.getParameter("password");
        System.out.println("KEY = " + dni + pass);
        CentroEducativoClient cliente = new CentroEducativoClient();

        try {
            String key = cliente.login(dni, pass);

            if (key != null && !key.isEmpty() && !key.equals("-1")) {
                HttpSession session = request.getSession();
                session.setAttribute("key", key);
                session.setAttribute("dni", dni);

                // hardcoded for now
                response.sendRedirect("AsignaturasServlet");
            } else {
                // todo display some error
                response.sendRedirect("login.html");
            }
        } catch (Exception e) {
        }
    }
}