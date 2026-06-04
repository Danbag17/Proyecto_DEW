
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
    	// ===== Profesorado (rolpro) =====
    	// --- Por defecto ---
    	USERS.put("23456733H", new Credentials("23456733H", "123456")); // Ramón Garcia
    	USERS.put("10293756L", new Credentials("10293756L", "123456")); // Pedro Valderas
    	USERS.put("06374291A", new Credentials("06374291A", "123456")); // Manoli Albert
    	USERS.put("65748923M", new Credentials("65748923M", "123456")); // Joan Fons
    	// --- Creados por el script ---
    	USERS.put("22222222P", new Credentials("22222222P", "123456")); // Ava Williams
    	USERS.put("33333333P", new Credentials("33333333P", "123456")); // Luis Navarro
    	USERS.put("44444444R", new Credentials("44444444R", "123456")); // Marta Sánchez
    	USERS.put("55555555S", new Credentials("55555555S", "123456")); // Carlos Gómez
    	USERS.put("66666666T", new Credentials("66666666T", "123456")); // Lucía Fernández
    	USERS.put("77777777W", new Credentials("77777777W", "123456")); // David Ramírez

    	// ===== Alumnado (rolalu) =====
    	// --- Por defecto ---
    	USERS.put("12345678W", new Credentials("12345678W", "123456")); // Pepe Garcia Sanchez
    	USERS.put("23456387R", new Credentials("23456387R", "123456")); // Maria Fernandez Gómez
    	USERS.put("34567891F", new Credentials("34567891F", "123456")); // Miguel Hernandez Llopis
    	USERS.put("93847525G", new Credentials("93847525G", "123456")); // Laura Benitez Torres
    	USERS.put("37264096W", new Credentials("37264096W", "123456")); // Minerva Alonso Pérez
    	// --- Creados por el script ---
    	USERS.put("33445566X", new Credentials("33445566X", "123456")); // John Wick
    	USERS.put("12345678A", new Credentials("12345678A", "123456")); // Carlos Martínez
    	USERS.put("87654321B", new Credentials("87654321B", "123456")); // Eva Ruiz
    	USERS.put("11223344C", new Credentials("11223344C", "123456")); // Laura Gómez
    	USERS.put("22334455D", new Credentials("22334455D", "123456")); // Pedro Díaz
    	USERS.put("33445567E", new Credentials("33445567E", "123456")); // Sara López
    	USERS.put("44556677F", new Credentials("44556677F", "123456")); // Hugo Moreno
    	USERS.put("55667788G", new Credentials("55667788G", "123456")); // Marta Jiménez
    	USERS.put("66778899H", new Credentials("66778899H", "123456")); // Iván Torres
    	USERS.put("77889900J", new Credentials("77889900J", "123456")); // Nerea Castro
    	USERS.put("88990011K", new Credentials("88990011K", "123456")); // Adrián Vega
    	USERS.put("99001122L", new Credentials("99001122L", "123456")); // Paula Ortega
    	USERS.put("10111213M", new Credentials("10111213M", "123456")); // Diego Romero
    	USERS.put("12131415N", new Credentials("12131415N", "123456")); // Alba Gil
    	USERS.put("14151617P", new Credentials("14151617P", "123456")); // Mario Serrano
    	USERS.put("16171819Q", new Credentials("16171819Q", "123456")); // Claudia Núñez
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
        
        //Impedir que Firefox cachee las páginas protegidas
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

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
            	//CentroEducativoClient.clearCookieJar();
            	
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
