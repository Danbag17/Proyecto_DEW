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
