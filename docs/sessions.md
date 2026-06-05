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


---

# Evolución tras el Hito 1

## Papel de SessionsUtils en la versión final

Durante el Hito 1 se planteó `SessionsUtils` como una utilidad para almacenar y recuperar atributos de sesión.

Durante la implementación final pasó a convertirse en uno de los componentes centrales de la aplicación, actuando como punto único de acceso a la información del usuario autenticado.

La filosofía adoptada fue:

```text
Ningún servlet debe acceder directamente a los atributos
principales de sesión.
```

Todos los accesos a:

```text
dni
password
key
rol
```

deben realizarse mediante métodos de `SessionsUtils`.

---

## Integración con AuthFilter

La incorporación de `AuthFilter` modificó el papel de la sesión.

Antes:

```text
Login
   ↓
Servlet
   ↓
Sesión
```

Después:

```text
Login Tomcat
   ↓
AuthFilter
   ↓
Login REST CentroEducativo
   ↓
SessionsUtils.createUserSession(...)
   ↓
Sesión NOL
```

Esto convirtió la sesión en el mecanismo principal para compartir información entre filtros, servlets y cliente REST.

---

## Información almacenada en la versión final

La implementación final almacena información suficiente para operar contra CentroEducativo sin volver a autenticar al usuario en cada petición.

Información principal:

| Atributo | Uso                                    |
| -------- | -------------------------------------- |
| dni      | Identificación del usuario             |
| password | Login REST cuando es necesario         |
| key      | Autenticación contra CentroEducativo   |
| rol      | Determinar funcionalidades disponibles |

---

## Session Key de CentroEducativo

Uno de los cambios más importantes respecto al diseño inicial fue el uso continuado de la `key` devuelta por CentroEducativo.

Proceso completo:

```text
Usuario introduce credenciales
           ↓
Tomcat autentica
           ↓
AuthFilter realiza login REST
           ↓
CentroEducativo devuelve key
           ↓
SessionsUtils almacena key
           ↓
Servlets reutilizan key
```

Ventajas:

* menos peticiones de autenticación;
* navegación más rápida;
* separación clara entre sesión web y sesión académica.

---

## Uso desde los servlets

La versión final adopta un patrón común.

Ejemplo conceptual:

```java
if (!SessionsUtils.isLoggedIn(request)) {
    response.sendError(401);
    return;
}

String dni = SessionsUtils.getDni(request);
String key = SessionsUtils.getKey(request);
```

De esta forma todos los servlets utilizan el mismo mecanismo de validación.

---

## Sesiones y AJAX

Las peticiones AJAX utilizan exactamente la misma sesión HTTP que las páginas normales.

Esto permite que operaciones como:

* consulta de alumnos;
* modificación de notas;
* actualización de vistas;

puedan reutilizar la misma `key` sin necesidad de volver a autenticarse.

Desde el punto de vista del frontend no existe diferencia entre una petición tradicional y una petición AJAX.

---

## Control de sesiones inválidas

Durante las pruebas aparecieron situaciones en las que:

* la sesión HTTP había expirado;
* la key ya no era válida;
* el usuario intentaba acceder mediante una URL guardada.

Para estos casos se añadieron comprobaciones adicionales que permiten:

* detectar ausencia de sesión;
* detectar ausencia de key;
* redirigir al login;
* devolver códigos HTTP adecuados.

---

## Logout completo

Inicialmente el logout únicamente invalidaba la sesión HTTP.

La versión final refuerza este proceso.

El cierre de sesión realiza:

```text
1. Invalidación de sesión HTTP.
2. Eliminación de dni.
3. Eliminación de password.
4. Eliminación de key.
5. Limpieza de autenticación.
6. Redirección a portada.
```

Esto evita reutilizar información de un usuario anterior.

---

## Relación con la seguridad

SessionsUtils no sustituye a la seguridad declarativa de Tomcat.

La responsabilidad queda repartida:

| Componente    | Responsabilidad                     |
| ------------- | ----------------------------------- |
| Tomcat        | Autenticación y roles               |
| AuthFilter    | Obtención de sesión CentroEducativo |
| SessionsUtils | Gestión de atributos de sesión      |
| Servlets      | Lógica de negocio                   |

Esta separación simplifica el mantenimiento y facilita las pruebas.

---

## Problemas encontrados

Durante el desarrollo se detectaron problemas relacionados con:

* sesiones parcialmente creadas;
* key inexistente;
* logout incompleto;
* recarga de páginas protegidas;
* caché del navegador.

La centralización mediante SessionsUtils permitió corregir estos problemas sin modificar cada servlet individualmente.

---

## Situación final

En la versión final del proyecto, SessionsUtils actúa como la capa de gestión de sesión de NOL. Centraliza la información del usuario, mantiene la key de CentroEducativo, facilita la integración con AuthFilter y simplifica la implementación de servlets y operaciones AJAX.

