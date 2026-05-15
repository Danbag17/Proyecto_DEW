# sessions.md

## Gestión de sesión – `SessionsUtils`

### Clase

```
dew.util.SessionsUtils
```

---

### Objetivo

Centralizar toda la gestión de la sesión HTTP de la aplicación, evitando que cada servlet acceda directamente a `HttpSession` y repita la misma lógica.

**Problemas que resuelve:**

- Duplicación de código entre servlets
- Errores por nombres de atributos inconsistentes
- Acceso no controlado a `HttpSession`
- Dificultad para cambiar atributos de sesión sin tocar múltiples clases

---

### Atributos de sesión gestionados

| Constante | Atributo HTTP | Descripción |
|---|---|---|
| `ATTR_DNI` | `"dni"` | DNI del usuario autenticado |
| `ATTR_PASSWORD` | `"password"` | Contraseña del usuario |
| `ATTR_KEY` | `"key"` | Session key devuelta por CentroEducativo |

---

### Métodos

| Método | Firma | Descripción |
|---|---|---|
| `createUserSession()` | `(HttpServletRequest, String dni, String password, String key)` | Crea una nueva sesión y almacena los atributos principales |
| `getSession()` | `(HttpServletRequest)` → `HttpSession` | Devuelve la sesión actual sin crear una nueva |
| `isLoggedIn()` | `(HttpServletRequest)` → `boolean` | Comprueba si existe sesión válida con `dni` y `key` presentes |
| `getDni()` | `(HttpServletRequest)` → `String` | Devuelve el DNI almacenado en sesión |
| `getPassword()` | `(HttpServletRequest)` → `String` | Devuelve la contraseña almacenada en sesión |
| `getKey()` | `(HttpServletRequest)` → `String` | Devuelve la `key` de CentroEducativo almacenada en sesión |
| `setDni()` | `(HttpServletRequest, String)` | Actualiza el DNI en sesión |
| `setPassword()` | `(HttpServletRequest, String)` | Actualiza la contraseña en sesión |
| `setKey()` | `(HttpServletRequest, String)` | Actualiza la `key` en sesión |
| `invalidateSession()` | `(HttpServletRequest)` | Invalida la sesión si existe |

---

### Flujo de uso

#### 1. Login

```java
// Tras autenticarse con Tomcat y obtener la key de CentroEducativo:
SessionsUtils.createUserSession(request, dni, password, key);
response.sendRedirect(request.getContextPath() + "/alumno/asignaturas");
```

#### 2. Navegación en servlets

```java
if (!SessionsUtils.isLoggedIn(request)) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
String dni = SessionsUtils.getDni(request);
String key = SessionsUtils.getKey(request);
// ... llamada a CentroEducativoClient con dni y key
```

#### 3. Logout

```java
SessionsUtils.invalidateSession(request);
response.sendRedirect(request.getContextPath() + "/");
```

---

### Criterio general

Todo acceso a los atributos de sesión `dni`, `password` y `key` debe realizarse a través de `SessionsUtils`. No se accede directamente a `HttpSession.getAttribute()` desde los servlets para estos atributos.
