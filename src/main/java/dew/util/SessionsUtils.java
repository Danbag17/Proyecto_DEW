package dew.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Utilidad centralizada para leer y escribir los datos de sesión usados por NOL.
 *
 * Atributos principales:
 * - dni: DNI del usuario autenticado en CentroEducativo.
 * - password: contraseña usada para pedir la key a CentroEducativo.
 * - key: clave de sesión devuelta por CentroEducativo.
 */
public final class SessionsUtils {

    public static final String ATTR_DNI = "dni";
    public static final String ATTR_PASSWORD = "password";
    public static final String ATTR_KEY = "key";

    private SessionsUtils() {
    }

    public static void createUserSession(HttpServletRequest request, String dni, String password, String key) {
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTR_DNI, dni);
        session.setAttribute(ATTR_PASSWORD, password);
        session.setAttribute(ATTR_KEY, key);
    }

    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession(false);
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = getSession(request);
        return session != null
                && session.getAttribute(ATTR_DNI) != null
                && session.getAttribute(ATTR_KEY) != null;
    }

    public static String getDni(HttpServletRequest request) {
        return getStringAttribute(request, ATTR_DNI);
    }

    public static String getPassword(HttpServletRequest request) {
        return getStringAttribute(request, ATTR_PASSWORD);
    }

    public static String getKey(HttpServletRequest request) {
        return getStringAttribute(request, ATTR_KEY);
    }

    public static void setKey(HttpServletRequest request, String key) {
        request.getSession(true).setAttribute(ATTR_KEY, key);
    }

    public static void setDni(HttpServletRequest request, String dni) {
        request.getSession(true).setAttribute(ATTR_DNI, dni);
    }

    public static void setPassword(HttpServletRequest request, String password) {
        request.getSession(true).setAttribute(ATTR_PASSWORD, password);
    }

    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = getSession(request);
        if (session != null) {
            session.invalidate();
        }
    }

    private static String getStringAttribute(HttpServletRequest request, String attributeName) {
        HttpSession session = getSession(request);
        if (session == null) {
            return null;
        }

        Object value = session.getAttribute(attributeName);
        return value != null ? value.toString() : null;
    }
}
