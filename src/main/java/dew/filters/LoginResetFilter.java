package dew.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession; // necesario solo si reactivas session.invalidate()

/**
 * Limpia la sesión cada vez que se llega a la página de login.
 *
 * Motivo: tras reiniciar CentroEducativo (que no persiste) o el propio Tomcat,
 * el navegador puede conservar un JSESSIONID que sigue "autenticado" en Tomcat
 * pero cuya key de CentroEducativo ya no vale. Al recibir un 401/403, el cliente
 * AJAX redirige a login.html; pero como la sesión de Tomcat sigue viva, el
 * AuthFilter no vuelve a loguearse contra CentroEducativo y se entra en bucle.
 *
 * Al pasar por login.html forzamos un arranque limpio: cerramos el principal de
 * Tomcat, invalidamos la HttpSession y caducamos la cookie en el navegador. Así
 * la siguiente autenticación crea una sesión nueva y una key fresca, sin que
 * nadie tenga que borrar la cookie a mano.
 *
 * Mapeado SOLO a REQUEST (ver web.xml): cuando Tomcat reenvía (FORWARD) a
 * login.html en un primer acceso no autenticado, este filtro no actúa, de modo
 * que no se rompe el "saved request" del login normal.
 */
public class LoginResetFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Sin inicialización necesaria.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // --- Limpieza extra en el servidor (DESACTIVADA por ahora) ---
        // Estas dos acciones liberan ya la sesión muerta en el servidor en vez
        // de dejarla huérfana hasta que caduque. NO son imprescindibles para
        // romper el bucle: basta con caducar la cookie (abajo). Descomenta si
        // quieres el comportamiento completo (= tu LogoutServlet).
        //
        // // Cierra el principal autenticado por Tomcat, si lo hubiera.
        // try {
        //     req.logout();
        // } catch (ServletException ignored) {
        //     // Si no había principal autenticado, no hay nada que cerrar.
        // }
        //
        // // Invalida la HttpSession para borrar los atributos propios (dni/key/...).
        // HttpSession sesion = req.getSession(false);
        // if (sesion != null) {
        //     sesion.invalidate();
        // }

        // Caduca el JSESSIONID en el navegador: el id viejo "puff" desaparece y
        // la próxima petición recibirá uno nuevo y limpio.
        Cookie cookie = new Cookie("JSESSIONID", "");
        String ctx = req.getContextPath();
        cookie.setPath(ctx == null || ctx.isEmpty() ? "/" : ctx);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        // Evita que login.html quede cacheado.
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
