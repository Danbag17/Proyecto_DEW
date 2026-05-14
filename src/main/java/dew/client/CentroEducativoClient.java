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
}