package dew.servlets;

import java.io.IOException;

import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginRedirectServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dni = request.getParameter("dni");
        String password = request.getParameter("password");

        if (dni == null || dni.isBlank()
                || password == null || password.isBlank()) {

            response.sendRedirect("login.html?error=missing");
            return;
        }

        try {

            String key = new CentroEducativoClient().login(dni, password);

            if (key == null || key.isBlank() || "-1".equals(key.trim())) {
                response.sendRedirect("login.html?error=bad_credentials");
                return;
            }

            SessionsUtils.createUserSession(
                    request,
                    dni,
                    password,
                    key.trim()
            );

            if (request.isUserInRole("rolpro")) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/profesor-asignaturas.html"
                );

            } else if (request.isUserInRole("rolalu")) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/alumno-asignaturas.html"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                        + "/index.html"
                );
            }

        } catch (Exception e) {

            throw new ServletException(
                    "Error autenticando contra CentroEducativo",
                    e
            );
        }
    }
}
