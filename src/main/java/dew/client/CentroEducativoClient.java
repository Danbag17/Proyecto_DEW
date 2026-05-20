package dew.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentroEducativoClient {

    private static final String BASE_URL =
            "http://172.23.189.79:9090/CentroEducativo";

    private static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient client =
            new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {

                private final Map<String, List<Cookie>> cookieStore =
                        new HashMap<>();

                @Override
                public synchronized void saveFromResponse(
                        HttpUrl url,
                        List<Cookie> cookies) {

                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public synchronized List<Cookie> loadForRequest(
                        HttpUrl url) {

                    List<Cookie> cookies =
                            cookieStore.get(url.host());

                    return cookies != null
                            ? cookies
                            : new ArrayList<>();
                }
            })
            .build();

    public String login(String dni, String password)
            throws IOException {

        String json =
                "{\"dni\":\"" + dni
                + "\",\"password\":\"" + password + "\"}";

        RequestBody body = RequestBody.create(
                json,
                JSON
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/login")
                .post(body)
                .build();

        try (Response response =
                     client.newCall(request).execute()) {

            String responseBody =
                    response.body() != null
                    ? response.body().string().trim()
                    : "";

            if (!response.isSuccessful()) {

                throw new IOException(
                        "CentroEducativo HTTP "
                        + response.code()
                        + ": "
                        + responseBody
                );
            }

            return responseBody;
        }
    }

    public String getAsignaturas(String key)
            throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL
                        + "/asignaturas?key="
                        + enc(key))
                .get()
                .build();

        return executeJson(request);
    }

    public String getAsignaturasAlumno(
            String dni,
            String key)
            throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL
                        + "/alumnos/"
                        + enc(dni)
                        + "/asignaturas?key="
                        + enc(key))
                .get()
                .addHeader("accept", "application/json")
                .build();

        return executeJson(request);
    }

    public String getExpediente(
            String dni,
            String key)
            throws IOException {

        return getAsignaturasAlumno(dni, key);
    }

    public String getAlumnoPorDNI(
            String dni,
            String key)
            throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL
                        + "/alumnos/"
                        + enc(dni)
                        + "?key="
                        + enc(key))
                .get()
                .addHeader("accept", "application/json")
                .build();

        return executeJson(request);
    }

    public String getDetalleAsignaturaAlumno(
            String dni,
            String acronimo,
            String key)
            throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL
                        + "/alumnos/"
                        + enc(dni)
                        + "/asignaturas/"
                        + enc(acronimo)
                        + "?key="
                        + enc(key))
                .get()
                .addHeader("accept", "application/json")
                .build();

        return executeJson(request);
    }

    public String getAsignaturasProfesor(
            String dniProfesor,
            String key)
            throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL
                        + "/profesoresyasignaturas?key="
                        + enc(key))
                .get()
                .addHeader("accept", "application/json")
                .build();

        return executeJson(request);
    }

    public String getAlumnosDeAsignatura(
            String acronimo,
            String key)
            throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL
                        + "/asignaturas/"
                        + enc(acronimo)
                        + "/alumnos?key="
                        + enc(key))
                .get()
                .addHeader("accept", "application/json")
                .build();

        return executeJson(request);
    }

    public String modificarNota(
            String dniAlumno,
            String acronimo,
            double nota,
            String key)
            throws IOException {

        String json = "{\"nota\":" + nota + "}";

        Request request = new Request.Builder()
                .url(BASE_URL
                        + "/alumnos/"
                        + enc(dniAlumno)
                        + "/asignaturas/"
                        + enc(acronimo)
                        + "?key="
                        + enc(key))
                .put(RequestBody.create(json, JSON))
                .addHeader("accept", "application/json")
                .build();

        return executeAllowEmpty(request);
    }

    private String executeJson(Request request)
            throws IOException {

        try (Response response =
                     client.newCall(request).execute()) {

            String body =
                    response.body() != null
                    ? response.body().string()
                    : "";

            if (!response.isSuccessful()) {

                throw new IOException(
                        "CentroEducativo HTTP "
                        + response.code()
                        + ": "
                        + body
                );
            }

            return body;
        }
    }

    private String executeAllowEmpty(Request request)
            throws IOException {

        try (Response response =
                     client.newCall(request).execute()) {

            String body =
                    response.body() != null
                    ? response.body().string()
                    : "";

            if (!response.isSuccessful()) {

                throw new IOException(
                        "CentroEducativo HTTP "
                        + response.code()
                        + ": "
                        + body
                );
            }

            if (body == null || body.isBlank()) {
                return "{\"ok\":true}";
            }

            return body;
        }
    }

    private static String enc(String value) {

        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8
        );
    }
}