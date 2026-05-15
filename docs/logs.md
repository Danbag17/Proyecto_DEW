# logs.md

## Sistema de logs del proyecto

### Objetivo

El objetivo del sistema de logs es registrar el uso de la aplicación web, dejando constancia de las peticiones realizadas y del contexto mínimo necesario para su seguimiento y auditoría.

---

### Implementación

El sistema de logs se implementa mediante un filtro Java denominado `LogsFilter`, ubicado en el paquete `dew.filters`.

Este filtro intercepta todas las peticiones que pasan por los servlets de la aplicación, registra la información relevante y delega la ejecución al siguiente elemento de la cadena.

---

### Versiones del filtro

| Versión | Descripción |
|---|---|
| **v0** | Servlet que devuelve la información de log directamente al navegador |
| **v1** | Servlet que escribe el log en un fichero con ruta fija |
| **v2**  Hito 1 | Filtro real que intercepta peticiones, escribe en fichero persistente con ruta configurable desde `web.xml` y mantiene el log ordenado cronológicamente |

---

### Información mínima a registrar por línea

Cada entrada de log debe incluir:

| Campo | Descripción |
|---|---|
| Fecha y hora | Timestamp en formato ISO 8601 |
| Usuario | DNI del usuario autenticado (si está disponible) |
| IP del cliente | Dirección IP de la petición |
| Recurso accedido | Nombre del servlet o URI del recurso |
| Método HTTP | `GET`, `POST`, etc. |

**Formato orientativo:**

```
2026-05-11T17:22:10 73281209 158.11.0.1 AlumnoAsignaturasServlet GET
```

---

### Configuración desde `web.xml`

La ruta del fichero de logs se configura como parámetro de inicialización del filtro en `web.xml`:

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

### Responsable

La implementación del filtro `LogsFilter` está asignada a **Mikel Escudero Aramburu**.

---

### Estado actual

Durante la sesión 3 se ha dejado creada la clase `LogsFilter.java`. Queda pendiente su implementación efectiva (versión 2) y su integración completa en `web.xml`.
