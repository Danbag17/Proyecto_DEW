package dew.servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

      HttpSession sesion = request.getSession(false);

      // Cierra el principal autenticado por Tomcat (j_security_check) y, además,
      // invalida la HttpSession para borrar los atributos propios (dni/key/...).
      request.logout();

      if (sesion != null) {
    	  sesion.invalidate();
      }

      // Borra la cookie de sesión en el navegador.
      Cookie cookie = new Cookie("JSESSIONID", "");
      cookie.setPath(request.getContextPath());
      cookie.setHttpOnly(true);
      cookie.setMaxAge(0);
      response.addCookie(cookie);

      // Impide que la respuesta (y la página a la que volvemos) quede cacheada,
      // de modo que tras el logout siempre se exija iniciar sesión de nuevo.
      response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
      response.setHeader("Pragma", "no-cache");
      response.setDateHeader("Expires", 0);

      response.sendRedirect(request.getContextPath() + "/index-v2.html?logout=1");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
