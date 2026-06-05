# webxml-config.md

## Configuración de `web.xml`

### Ubicación

```
src/main/webapp/WEB-INF/web.xml
```

> **Importante:** este es el único `web.xml` del proyecto. No debe confundirse con el `web.xml` interno que Eclipse muestra dentro de la configuración del servidor Tomcat en la vista `Servers`. Son archivos distintos con propósitos distintos.

---

### Objetivo

Centralizar en un único archivo la configuración de:

- Servlets y sus mapeos de URL
- Filtros y sus mapeos
- Roles de seguridad
- Restricciones de acceso por ruta
- Mecanismo de autenticación
- Página de bienvenida

No se utilizan anotaciones (`@WebServlet`, `@WebFilter`). Toda la configuración se realiza exclusivamente en este archivo.

---

### Welcome file

```xml
<welcome-file-list>
    <welcome-file>index.html</welcome-file>
</welcome-file-list>
```

---

### Servlets configurados

| Servlet | Clase | Ruta |
|---|---|---|
| `LoginRedirectServlet` | `dew.servlets.LoginRedirectServlet` | `/login` |
| `AlumnoAsignaturasServlet` | `dew.servlets.AlumnoAsignaturasServlet` | `/alumno/asignaturas` |
| `AlumnoDetalleServlet` | `dew.servlets.AlumnoDetalleServlet` | `/alumno/detalle` |
| `AlumnoExpedienteServlet` | `dew.servlets.AlumnoExpedienteServlet` | `/alumno/expediente` |
| `LogoutServlet` | `dew.servlets.LogoutServlet` | `/logout` |
| `ProfesorAsignaturasServlet` | `dew.servlets.ProfesorAsignaturasServlet` | `/profesor/asignaturas` |
| `AsignaturaAlumnos` | `dew.servlets.AsignaturaAlumnos` | `/profesor/alumnos` |
| `ModificarNota` | `dew.servlets.ModificarNota` | `/profesor/modificar-nota` |

**Ejemplo de declaración:**

```xml
<servlet>
    <servlet-name>AlumnoAsignaturasServlet</servlet-name>
    <servlet-class>dew.servlets.AlumnoAsignaturasServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>AlumnoAsignaturasServlet</servlet-name>
    <url-pattern>/alumno/asignaturas</url-pattern>
</servlet-mapping>
```

---

### Filtro configurado

| Filtro | Clase | Patrón |
|---|---|---|
| `LogsFilter` | `dew.filters.LogsFilter` | `/*` |

**Declaración completa:**

```xml
<filter>
    <filter-name>LogsFilter</filter-name>
    <filter-class>dew.filters.LogsFilter</filter-class>
    <init-param>
        <param-name>logPath</param-name>
        <param-value>/var/nol/logs.txt</param-value>
    </init-param>
    <init-param>
        <param-name>active</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>

<filter-mapping>
    <filter-name>LogsFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

El parámetro `active` permite activar o desactivar el filtro sin modificar el código.

---

### Roles de seguridad

```xml
<security-role>
    <role-name>rolalu</role-name>
</security-role>

<security-role>
    <role-name>rolpro</role-name>
</security-role>
```

---

### Restricciones de acceso

**Rutas del alumnado — solo `rolalu`:**

```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>Zona alumno</web-resource-name>
        <url-pattern>/alumno/*</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <role-name>rolalu</role-name>
    </auth-constraint>
</security-constraint>
```

**Rutas del profesorado — solo `rolpro`:**

```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>Zona profesor</web-resource-name>
        <url-pattern>/profesor/*</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <role-name>rolpro</role-name>
    </auth-constraint>
</security-constraint>
```

---

### Configuración de autenticación

Se utiliza autenticación de tipo `FORM`, delegando en Tomcat:

```xml
<login-config>
    <auth-method>FORM</auth-method>
    <realm-name>nol2526</realm-name>
    <form-login-config>
        <form-login-page>/login</form-login-page>
        <form-error-page>/login?error=true</form-error-page>
    </form-login-config>
</login-config>
```

---

### Responsable

La configuración de `web.xml` y `tomcat-users.xml` está asignada a **Daniel Zanon Barney**.


---

# Evolución tras el Hito 1

## Papel de web.xml en la versión final

Durante todo el proyecto se mantuvo una decisión fundamental exigida por el enunciado:

```text
Toda la configuración debe realizarse mediante web.xml.
```

Por este motivo no se utilizaron:

```java
@WebServlet
@WebFilter
```

Toda la estructura de la aplicación quedó centralizada en:

```text
src/main/webapp/WEB-INF/web.xml
```

Esto permitió disponer de un único punto de configuración para:

* servlets;
* filtros;
* autenticación;
* roles;
* restricciones;
* páginas de error;
* parámetros globales.

---

## Evolución de la configuración de seguridad

Durante el Hito 1 la configuración de seguridad se centró principalmente en la separación entre alumnado y profesorado.

Durante la implementación final se consolidó una estructura de seguridad basada en varias capas:

```text
Tomcat
   ↓
FORM Authentication
   ↓
Roles
   ↓
AuthFilter
   ↓
Sesión NOL
   ↓
Servlets
```

Esta arquitectura permitió combinar seguridad declarativa y seguridad programática.

---

## Configuración FORM Authentication

La autenticación definitiva se basa en:

```xml
<login-config>
    <auth-method>FORM</auth-method>
</login-config>
```

Tomcat intercepta automáticamente las peticiones dirigidas a:

```text
j_security_check
```

utilizando los campos:

```html
<input name="j_username">
<input name="j_password">
```

El resultado es una autenticación gestionada completamente por el contenedor.

Ventajas:

* menos código propio;
* mayor integración con Tomcat;
* aplicación automática de roles.

---

## Relación con tomcat-users.xml

El sistema utiliza usuarios definidos en:

```text
tomcat-users.xml
```

Cada usuario dispone de:

* nombre de usuario;
* contraseña;
* rol asociado.

Ejemplo conceptual:

```xml
<user username="12345678A"
      password="123456"
      roles="rolalu"/>
```

o

```xml
<user username="22222222P"
      password="123456"
      roles="rolpro"/>
```

La autenticación web depende directamente de esta configuración.

---

## Incorporación de AuthFilter

Una de las modificaciones más importantes realizadas tras el Hito 1 fue la incorporación de:

```text
dew.filters.AuthFilter
```

Su función es actuar como puente entre:

```text
Tomcat
```

y

```text
CentroEducativo
```

Flujo:

```text
Usuario autenticado
        ↓
AuthFilter
        ↓
Login REST
        ↓
Obtención de key
        ↓
Creación de sesión
        ↓
Acceso a servlets
```

Gracias a esta estrategia los servlets reciben una sesión ya preparada.

---

## Evolución de los filtros

Inicialmente la documentación solo contemplaba:

```text
LogsFilter
```

La versión final utiliza varios mecanismos complementarios:

### LogsFilter

Responsable de:

* registrar accesos;
* almacenar información de auditoría;
* generar trazabilidad.

### AuthFilter

Responsable de:

* autenticar contra CentroEducativo;
* crear sesión NOL;
* reutilizar la key;
* evitar duplicación de lógica.

---

## Protección de recursos AJAX

Durante las pruebas se detectó que no bastaba con proteger únicamente las páginas visibles.

Los endpoints AJAX también debían quedar protegidos.

Por este motivo:

```text
/profesor/*
```

y

```text
/alumno/*
```

incluyen tanto páginas como servlets AJAX.

Esto impide que un usuario invoque manualmente operaciones restringidas.

---

## Páginas de error

Durante la implementación final se añadieron páginas específicas para errores frecuentes.

Errores contemplados:

```text
401
403
404
500
```

Objetivos:

* mejorar experiencia de usuario;
* facilitar depuración;
* evitar páginas genéricas de Tomcat.

Estas páginas se configuran mediante:

```xml
<error-page>
```

dentro de web.xml.

---

## Parámetros globales

El uso de parámetros de contexto permitió evitar constantes embebidas en el código.

Ejemplos:

```xml
<context-param>
```

o

```xml
<init-param>
```

utilizados para:

* configuración de logs;
* activación de funcionalidades;
* rutas de almacenamiento.

Esta estrategia mejora la mantenibilidad de la aplicación.

---

## Evolución de LogsFilter

La configuración final del filtro incluye:

* activación/desactivación mediante parámetro;
* ruta configurable del fichero de log;
* aplicación global mediante:

```xml
<url-pattern>/*</url-pattern>
```

Esto garantiza que todas las peticiones relevantes queden registradas.

---

## Welcome file definitivo

La aplicación mantiene:

```xml
<welcome-file>
    index.html
</welcome-file>
```

como punto de entrada principal.

Desde esta página el usuario puede:

* consultar información del proyecto;
* acceder al login;
* navegar según su rol.

---

## Seguridad declarativa y programática

La versión final combina dos enfoques.

### Seguridad declarativa

Configurada mediante:

```xml
<security-constraint>
```

y

```xml
<security-role>
```

Ventajas:

* centralizada;
* visible;
* gestionada por Tomcat.

### Seguridad programática

Aplicada dentro de filtros y servlets para:

* validar sesiones;
* comprobar key;
* verificar parámetros;
* gestionar errores.

La combinación de ambos enfoques proporciona una solución más robusta.

---

## Problemas encontrados

Durante el desarrollo aparecieron incidencias relacionadas con:

* rutas de login;
* contexto de la aplicación;
* despliegue en Tomcat;
* sincronización con CentroEducativo;
* usuarios mal configurados;
* sesiones parcialmente creadas.

La centralización en web.xml facilitó enormemente la resolución de estos problemas.

---

## Situación final

En la versión final del proyecto, web.xml actúa como el núcleo de configuración de la aplicación. Centraliza servlets, filtros, autenticación, roles, errores y parámetros globales, cumpliendo además el requisito del enunciado de no utilizar anotaciones y permitiendo mantener una arquitectura clara y fácilmente auditable.

