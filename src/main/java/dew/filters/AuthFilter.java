package dew.filters; 
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// Se activa para cualquier ruta dentro de /alumno o /profesor
public class AuthFilter implements Filter {

    // 8.2: Tabla de usuarios para mapear login de Tomcat con DNI de CentroEducativo
    private static final Map<String, String[]> users = new HashMap<>();

    static {

        // Alumnos (rolalu)
        users.put("pepe",    new String[]{"12345678w", "123456"});
        users.put("maria",   new String[]{"23456387R", "123456"});
        users.put("miguel",  new String[]{"34567891F", "123456"});
        users.put("laura",   new String[]{"93847525G", "123456"});
        users.put("minerva", new String[]{"37264096W", "123456"});
        
        // Profesores (rolpro)
        users.put("ramon",   new String[]{"23456733H", "123456"});
        users.put("pedro",   new String[]{"10293756L", "123456"});
        users.put("manoli",  new String[]{"06374291A", "123456"});
        users.put("joan",    new String[]{"65748923M", "123456"});


        // users.put("login_tomcat", new String[]{"DNI", "Password_API"});
        users.put("alumno1", new String[]{"12345678w", "123456"}); // Pepe García Sanchez
        users.put("profesor1", new String[]{"23456733H", "123456"}); // Ramón Garcia
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession sesion = req.getSession();
        
        // 01: Comprobar si ya tenemos la key en la sesión
        if (sesion.getAttribute("key") == null) {
            
            // 02: Obtener el usuario autenticado en Tomcat
            String login = req.getRemoteUser();
            
            if (login != null && users.containsKey(login)) {
                // 03-04: Obtener DNI y Pass de nuestra tabla
                String dni = users.get(login)[0];
                String pass = users.get(login)[1];
                
                sesion.setAttribute("dni", dni);
                sesion.setAttribute("pass", pass);
                
                // 05: Invocando /login por POST a la API REST
                String key = pedirKeyACentroEducativo(dni, pass);
                
                if (key != null) {
                    // 06: Guardar la key en la sesión
                    sesion.setAttribute("key", key);
                } else {
                    throw new ServletException("Error de autenticación en CentroEducativo API");
                }
            }
        }
        
        // 13: Continuar con el servlet correspondiente
        chain.doFilter(request, response);
    }

    private String pedirKeyACentroEducativo(String dni, String pass) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String json = "{\"dni\":\"" + dni + "\", \"password\":\"" + pass + "\"}"; 
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/CentroEducativo/login")) 
                .header("Content-Type", "application/json") 
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return response.body().trim(); // Devuelve la key alfanumérica
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
