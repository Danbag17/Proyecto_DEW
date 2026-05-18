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
            throw new ServletException("No se ha definido el parámetro logFilePath en web.xml");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        registrarAcceso(httpRequest);

        chain.doFilter(request, response);
    }

    private void registrarAcceso(HttpServletRequest request) throws IOException {
        String recurso = request.getRequestURI();

        if (esRecursoEstatico(recurso)) {
            return;
        }

        String fecha = LocalDateTime.now().toString();

        String usuario = request.getRemoteUser();
        if (usuario == null) {
            usuario = "anonimo";
        }

        String ip = request.getRemoteAddr();
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
                || recurso.endsWith(".woff2");
    }

    private synchronized void escribirLinea(String linea) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(linea);
            writer.newLine();
        }
    }

    @Override
    public void destroy() {
        // No hay recursos abiertos permanentemente.
    }
}