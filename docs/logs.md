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
---

# Evolución tras el Hito 1

## Consolidación de LogsFilter

Durante el Hito 1 el objetivo principal era disponer de una versión funcional del sistema de logs que permitiera registrar accesos a la aplicación.

En la versión final, `LogsFilter` pasó a convertirse en un componente transversal del sistema, registrando de forma automática las peticiones realizadas por usuarios autenticados y no autenticados.

Clase responsable:

```text
dew.filters.LogsFilter
```

---

## Papel del filtro dentro de la arquitectura

El filtro se ejecuta antes de que la petición llegue al servlet correspondiente.

Flujo simplificado:

```text
Petición HTTP
      ↓
LogsFilter
      ↓
AuthFilter
      ↓
Servlet
      ↓
Respuesta
```

Esto permite registrar la petición independientemente de la lógica concreta del servlet.

---

## Información registrada

La versión final mantiene el formato acordado durante el Hito 1:

| Campo        | Descripción                   |
| ------------ | ----------------------------- |
| Fecha y hora | Timestamp de la petición      |
| Usuario      | Usuario autenticado si existe |
| IP           | Dirección IP origen           |
| Recurso      | URI o servlet solicitado      |
| Método HTTP  | GET, POST, PUT, DELETE        |

Ejemplo:

```text
2026-05-11T17:22:10 12345678A 127.0.0.1 /alumno/asignaturas GET
```

Este formato facilita la revisión de incidencias y la auditoría del sistema.

---

## Aplicación global del filtro

La configuración final mantiene:

```xml
<url-pattern>/*</url-pattern>
```

Esto garantiza que el filtro pueda interceptar:

* páginas HTML;
* servlets;
* peticiones AJAX;
* recursos protegidos.

Gracias a ello se obtiene una visión completa del uso de la aplicación.

---

## Registro de peticiones AJAX

Una mejora relevante respecto al planteamiento inicial fue el registro de peticiones AJAX.

En la versión final muchas operaciones importantes utilizan:

```javascript
fetch(...)
```

Por ejemplo:

* carga de alumnos;
* modificación de notas;
* actualización de vistas.

Estas peticiones también atraviesan el filtro y quedan registradas.

Esto resulta especialmente útil para detectar errores difíciles de reproducir desde la interfaz gráfica.

---

## Configuración desde web.xml

La ruta del fichero de log continúa configurándose mediante parámetros de inicialización.

Ventajas:

* no es necesario recompilar;
* permite cambiar ubicación fácilmente;
* simplifica pruebas y despliegues.

Ejemplo conceptual:

```xml
<init-param>
    <param-name>logPath</param-name>
    <param-value>/var/nol/logs.txt</param-value>
</init-param>
```

---

## Activación y desactivación

La implementación contempla la posibilidad de activar o desactivar el registro mediante configuración.

Ejemplo:

```xml
<init-param>
    <param-name>active</param-name>
    <param-value>true</param-value>
</init-param>
```

Esto facilita:

* pruebas de rendimiento;
* depuración;
* demostraciones.

Sin necesidad de modificar el código Java.

---

## Escritura concurrente

Durante la implementación se tuvo en cuenta que Tomcat puede procesar varias peticiones simultáneamente.

Por este motivo se valoró proteger la sección de escritura en fichero para evitar:

```text
mezcla de líneas
registros incompletos
corrupción del log
```

La solución adoptada garantiza que cada entrada se escriba de forma consistente.

---

## Utilidad durante el desarrollo

Los logs resultaron especialmente útiles para detectar:

* accesos a rutas incorrectas;
* problemas de login;
* errores de despliegue;
* peticiones AJAX fallidas;
* llamadas inesperadas a recursos estáticos;
* problemas relacionados con favicon y recursos inexistentes.

---

## Relación con la auditoría

El sistema de logs constituye una evidencia objetiva del funcionamiento de la aplicación.

Permite demostrar:

* qué usuario realizó una acción;
* cuándo se produjo;
* desde qué dirección IP;
* sobre qué recurso.

Esto resulta especialmente útil para justificar el cumplimiento de los requisitos del enunciado.

---

## Limitaciones conocidas

El sistema implementado cumple los requisitos académicos del proyecto, aunque una aplicación de producción podría incorporar mejoras como:

* rotación automática de logs;
* niveles de severidad;
* almacenamiento en base de datos;
* exportación centralizada;
* integración con herramientas de monitorización.

---

## Situación final

La versión final del proyecto dispone de un sistema de trazabilidad basado en `LogsFilter` que registra automáticamente el uso de la aplicación, tanto en navegación tradicional como en operaciones AJAX. Su configuración permanece centralizada en `web.xml`, cumpliendo los requisitos del enunciado y facilitando tanto la depuración como la auditoría del sistema.


