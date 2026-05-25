package dew.filters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class LogsFilter implements Filter {

    private String logFilePath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        logFilePath = context.getInitParameter("logFilePath");

        if (logFilePath == null || logFilePath.isBlank()) {
            throw new ServletException("No se ha definido logFilePath en web.xml");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!esRecursoEstatico(httpRequest.getRequestURI())) {
            registrarAcceso(httpRequest);
        }

        chain.doFilter(request, response);
    }

    private void registrarAcceso(HttpServletRequest request) throws IOException {
        String fecha = LocalDateTime.now().toString();

        String usuario = request.getRemoteUser();
        if (usuario == null || usuario.isBlank()) {
            usuario = "anonimo";
        }

        String ip = request.getRemoteAddr();

        /*
         * El enunciado pide que aparezca el servlet/recurso activado.
         * Usamos servletPath para que salga /AlumnoAsignaturasServlet en vez de toda la URI.
         */
        String recurso = request.getServletPath();
        if (recurso == null || recurso.isBlank()) {
            recurso = request.getRequestURI();
        }

        String metodo = request.getMethod();

        String linea = fecha + " " + usuario + " " + ip + " " + recurso + " " + metodo;
        escribirLinea(linea);
    }

    private boolean esRecursoEstatico(String recurso) {
        return recurso.endsWith(".css")
                || recurso.endsWith(".js")
                || recurso.endsWith(".png")
                || recurso.endsWith(".jpg")
                || recurso.endsWith(".jpeg")
                || recurso.endsWith(".gif")
                || recurso.endsWith(".ico")
                || recurso.endsWith(".svg")
                || recurso.endsWith(".woff")
                || recurso.endsWith(".woff2")
                || recurso.endsWith(".map");
    }

    private synchronized void escribirLinea(String linea) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(linea);
            writer.newLine();
        }
    }

    @Override
    public void destroy() {
    }
}
