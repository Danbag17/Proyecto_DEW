	package dew.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Utilidad para centralizar la gestión de la sesión HTTP de la aplicación.
 *
 * <p>
 * Esta clase permite almacenar y recuperar los atributos principales
 * asociados al usuario autenticado, especialmente aquellos necesarios
 * para la integración con CentroEducativo.
 * </p>
 *
 * <p>
 * Atributos de sesión previstos:
 * </p>
 * <ul>
 *   <li>{@code dni}: identificador del usuario autenticado</li>
 *   <li>{@code password}: contraseña del usuario, si se decide conservar temporalmente</li>
 *   <li>{@code key}: clave de sesión devuelta por CentroEducativo</li>
 * </ul>
 */
public final class SessionsUtils {

    /** Nombre del atributo de sesión para el DNI del usuario. */
    public static final String ATTR_DNI = "dni";

    /** Nombre del atributo de sesión para la contraseña del usuario. */
    public static final String ATTR_PASSWORD = "password";

    /** Nombre del atributo de sesión para la key devuelta por CentroEducativo. */
    public static final String ATTR_KEY = "key";

    /**
     * Constructor privado para evitar instanciación.
     */
    private SessionsUtils() {
        // Clase de utilidad: no debe instanciarse.
    }

    /**
     * Crea una nueva sesión si no existe y guarda en ella los atributos
     * principales del usuario autenticado.
     *
     * @param request petición HTTP actual
     * @param dni identificador del usuario
     * @param password contraseña del usuario
     * @param key clave de sesión devuelta por CentroEducativo
     */
    public static void createUserSession(HttpServletRequest request, String dni, String password, String key) {
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTR_DNI, dni);
        session.setAttribute(ATTR_PASSWORD, password);
        session.setAttribute(ATTR_KEY, key);
    }

    /**
     * Devuelve la sesión actual sin crear una nueva.
     *
     * @param request petición HTTP actual
     * @return sesión actual o {@code null} si no existe
     */
    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession(false);
    }

    /**
     * Indica si existe una sesión válida con los atributos mínimos esperados.
     *
     * @param request petición HTTP actual
     * @return {@code true} si existe sesión y contiene al menos DNI y key; {@code false} en caso contrario
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = getSession(request);
        return session != null
                && session.getAttribute(ATTR_DNI) != null
                && session.getAttribute(ATTR_KEY) != null;
    }

    /**
     * Devuelve el DNI almacenado en sesión.
     *
     * @param request petición HTTP actual
     * @return DNI del usuario o {@code null} si no existe
     */
    public static String getDni(HttpServletRequest request) {
        return getStringAttribute(request, ATTR_DNI);
    }

    /**
     * Devuelve la contraseña almacenada en sesión.
     *
     * @param request petición HTTP actual
     * @return contraseña o {@code null} si no existe
     */
    public static String getPassword(HttpServletRequest request) {
        return getStringAttribute(request, ATTR_PASSWORD);
    }

    /**
     * Devuelve la key de CentroEducativo almacenada en sesión.
     *
     * @param request petición HTTP actual
     * @return key o {@code null} si no existe
     */
    public static String getKey(HttpServletRequest request) {
        return getStringAttribute(request, ATTR_KEY);
    }

    /**
     * Actualiza la key almacenada en sesión.
     *
     * @param request petición HTTP actual
     * @param key nueva key
     */
    public static void setKey(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTR_KEY, key);
    }

    /**
     * Actualiza el DNI almacenado en sesión.
     *
     * @param request petición HTTP actual
     * @param dni nuevo DNI
     */
    public static void setDni(HttpServletRequest request, String dni) {
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTR_DNI, dni);
    }

    /**
     * Actualiza la contraseña almacenada en sesión.
     *
     * @param request petición HTTP actual
     * @param password nueva contraseña
     */
    public static void setPassword(HttpServletRequest request, String password) {
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTR_PASSWORD, password);
    }

    /**
     * Elimina la sesión actual si existe.
     *
     * @param request petición HTTP actual
     */
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = getSession(request);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Recupera un atributo de sesión como cadena.
     *
     * @param request petición HTTP actual
     * @param attributeName nombre del atributo
     * @return valor del atributo convertido a cadena, o {@code null} si no existe
     */
    private static String getStringAttribute(HttpServletRequest request, String attributeName) {
        HttpSession session = getSession(request);
        if (session == null) {
            return null;
        }

        Object value = session.getAttribute(attributeName);
        return value != null ? value.toString() : null;
    }
}