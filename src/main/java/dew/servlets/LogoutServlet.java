package dew.servlets;

import java.io.IOException;

import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Cierra el principal autenticado por Tomcat (j_security_check).
        try {
            request.logout();
        } catch (ServletException ignored) {
        }

        // Invalida la HttpSession (borra dni/key/... propios) y limpia el cookie
        // jar del cliente REST para que no se arrastre sesión de CentroEducativo
        // entre logins.
        SessionsUtils.invalidateSession(request);
        CentroEducativoClient.clearCookieJar();

        // Borra la cookie de sesión en el navegador.
        String cookiePath = request.getContextPath();
        if (cookiePath == null || cookiePath.isEmpty()) {
            cookiePath = "/";
        }
        Cookie sessionCookie = new Cookie("JSESSIONID", "");
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath(cookiePath);
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);

        // Impide que la respuesta (y la página a la que volvemos) quede cacheada,
        // de modo que tras el logout siempre se exija iniciar sesión de nuevo.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        response.sendRedirect(request.getContextPath() + "/index.html?logout=1");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
