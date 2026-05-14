package dew.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import dew.client.CentroEducativoClient;

public class LogsFilter implements Filter {
	
	private static final long serialVersionUID = 1L;
	public LogsFilter() {
		super();
	// TODO Auto-generated constructor stub
	}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

       

        // 2. LOGS VERSIÓN 2 (Escritura persistente en archivo)
        registrarLog(req);

        // 3. CONTINUAR (Deja que la petición llegue al Servlet)
        chain.doFilter(request, response);
    }

    private void registrarLog(HttpServletRequest req) {
        // Recopilar datos
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String usuario = (req.getRemoteUser() != null) ? req.getRemoteUser() : "Anónimo";
        String ip = req.getRemoteAddr();
        String pathServlet = req.getServletPath();
        String metodo = req.getMethod();

        // Formatear línea: Fecha Hora usuario IP Servlet METODO
        String lineaLog = String.format("%s %s %s %s %s\n", fechaHora, usuario, ip, pathServlet, metodo);

        // Obtener ruta del archivo en la raíz del servidor
        String rutaArchivo = req.getServletContext().getRealPath("/") + "logs_proyecto.txt";

        // Escribir usando "append = true" (Versión 2)
        try (FileWriter fw = new FileWriter(rutaArchivo, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.print(lineaLog);
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el archivo de logs: " + e.getMessage());
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}