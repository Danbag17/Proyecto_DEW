package dew.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ModificarNotaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionsUtils.isLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No hay sesión activa");
            return;
        }

        if (!request.isUserInRole("rolpro")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo profesores");
            return;
        }

        NotaRequest notaRequest = leerNotaRequest(request);

        if (notaRequest.dniAlumno == null || notaRequest.dniAlumno.isBlank()
                || notaRequest.asig == null || notaRequest.asig.isBlank()
                || notaRequest.nota == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Se esperan dniAlumno, asig y nota");
            return;
        }

        if (notaRequest.nota < 0.0 || notaRequest.nota > 10.0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "La nota debe estar entre 0 y 10");
            return;
        }

        String key = SessionsUtils.getKey(request);

        try {
            String json = new CentroEducativoClient()
                    .modificarNota(notaRequest.dniAlumno, notaRequest.asig, notaRequest.nota, key);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error modificando nota: " + e.getMessage());
        }
    }

    /*
     * Lee dni/asig de la query string (los manda api.js) y la nota del body JSON
     * o del parámetro "nota". Sirve para AJAX y para formularios clásicos.
     */
    private NotaRequest leerNotaRequest(HttpServletRequest request) throws IOException {
        NotaRequest nr = new NotaRequest();

        nr.dniAlumno = primerNoVacio(
                request.getParameter("dniAlumno"),
                request.getParameter("dni"));
        nr.asig = request.getParameter("asig");

        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().contains("application/json")) {
            try (BufferedReader reader = request.getReader()) {
                NotaRequest bodyData = gson.fromJson(reader, NotaRequest.class);
                if (bodyData != null) {
                    if (nr.dniAlumno == null || nr.dniAlumno.isBlank()) nr.dniAlumno = bodyData.dniAlumno;
                    if (nr.asig == null || nr.asig.isBlank()) nr.asig = bodyData.asig;
                    nr.nota = bodyData.nota;
                }
            }
            return nr;
        }

        String notaParam = request.getParameter("nota");
        if (notaParam != null && !notaParam.isBlank()) {
            try {
                nr.nota = Double.parseDouble(notaParam);
            } catch (NumberFormatException ignored) {
                nr.nota = null;
            }
        }

        return nr;
    }

    private static String primerNoVacio(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        return b;
    }

    private static class NotaRequest {
        String dniAlumno;
        String asig;
        Double nota;
    }
}
