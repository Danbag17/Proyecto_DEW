package dew.client;

import okhttp3.*;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import com.google.gson.Gson;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentroEducativoClient {
    private final static OkHttpClient client = new OkHttpClient().newBuilder().cookieJar(new CookieJar() {
    	private static final Map<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<>();
        }
    })
    .build();
    private final Gson gson = new Gson();
    private final String BASE_URL = "http://localhost:9090/CentroEducativo"; // [cite: 111]

    public String login(String dni, String password) throws IOException {
        // Preparamos el JSON para el login [cite: 192]
        String json = "{\"dni\":\"" + dni + "\", \"password\":\"" + password + "\"}";
        
        RequestBody body = RequestBody.create(
            json, MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
            .url(BASE_URL + "/login") // [cite: 191]
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string(); // Aquí obtienes la 'key' [cite: 122]
            }
        }
        return null;
    }

    public String getAsignaturas(String key) throws IOException {
        Request request = new Request.Builder()
            .url(BASE_URL + "/asignaturas?key=" + key)
            .get()
            .build();
        System.out.println("KEY = " + BASE_URL + "/asignaturas?key=" + key );
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    public String getAsignaturasAlumno(String dni, String key) throws IOException {
        
        String url = BASE_URL + "/alumnos/" + dni + "/asignaturas?key=" + key;
        
        System.out.println("Pidiendo asignaturas del alumno: " + url);

        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Error inesperado: " + response);
            
            return response.body().string();
        }
    }
    
    public String getExpediente(String dni, String key) throws IOException {
        // Usamos la ruta exacta que aparece en tu Swagger UI
        String url = BASE_URL + "/alumnos/" + dni + "/asignaturas?key=" + key;
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Error API: " + response);
            return response.body().string(); 
        }
    }
    
    public String getAlumnoPorDNI(String dni, String key) throws IOException {
        // La URL para obtener los datos personales del alumno
        String url = BASE_URL + "/alumnos/" + dni + "?key=" + key;
        
        System.out.println("Solicitando datos del alumno: " + url);

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al obtener datos del alumno: " + response);
            }
            
            return response.body().string(); // Devuelve el JSON con nombre, apellidos, etc.
        }
    }
}