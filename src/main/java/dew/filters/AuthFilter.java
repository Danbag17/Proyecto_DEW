
package dew.filters; 


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dew.client.CentroEducativoClient;
import dew.util.SessionsUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class AuthFilter implements Filter {

    private static final Map<String, Credentials> USERS = new HashMap<>();

    static {
        /*
         * Ajustad estos login_tomcat a lo que pongáis en tomcat-users.xml/conf.
         * Los DNI/password son los de CentroEducativo.
         */
    	USERS.put("12345678W", new Credentials("12345678W", "123456"));
    	USERS.put("23456387R", new Credentials("23456387R", "123456"));
    	USERS.put("34567891F", new Credentials("34567891F", "123456"));
    	USERS.put("93847525G", new Credentials("93847525G", "123456"));
    	USERS.put("37264096W", new Credentials("37264096W", "123456"));

    	USERS.put("22222222P", new Credentials("22222222P", "123456"));
    	USERS.put("profesor2", new Credentials("23456733H", "123456"));
    	USERS.put("10293756L", new Credentials("10293756L", "123456"));
    	USERS.put("06374291A", new Credentials("06374291A", "123456"));
    	USERS.put("65748923M", new Credentials("65748923M", "123456"));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Sin inicialización necesaria.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

    	
    	
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;


    	String uri = req.getRequestURI();

    	if (uri.endsWith("/j_security_check")) {
    	    chain.doFilter(request, response);
    	    return;
    	}
    	
    	if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png")
    	        || uri.endsWith(".jpg") || uri.endsWith(".jpeg") || uri.endsWith(".ico")) {
    	    chain.doFilter(request, response);
    	    return;
    	}
        
        if (!SessionsUtils.isLoggedIn(req)) {
            String loginTomcat = req.getRemoteUser();

            if (loginTomcat == null) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autenticado en Tomcat");
                return;
            }

            Credentials credentials = USERS.get(loginTomcat);

            if (credentials == null) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Usuario Tomcat sin equivalencia definida en CentroEducativo: " + loginTomcat);
                return;
            }

            try {
                String key = new CentroEducativoClient().login(credentials.dni, credentials.password);

                if (key == null || key.isBlank() || "-1".equals(key.trim())) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            "CentroEducativo no ha devuelto una key válida");
                    return;
                }

                SessionsUtils.createUserSession(req, credentials.dni, credentials.password, key.trim());
            } catch (Exception e) {
                throw new ServletException("Error autenticando contra CentroEducativo", e);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private static final class Credentials {
        final String dni;
        final String password;

        Credentials(String dni, String password) {
            this.dni = dni;
            this.password = password;
        }
    }

}
