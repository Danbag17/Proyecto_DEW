# memoria.md

## Memoria técnica – Hito 1

### 1. Introducción

Este documento resume el estado del proyecto **nol2526** correspondiente al Laboratorio 2 de la asignatura **Desarrollo Web (DEW)** del curso **2025/2026**.

El objetivo del proyecto es desarrollar una aplicación web de consulta y gestión académica basada en servlets Java, con autenticación, control de acceso por roles, registro de uso mediante logs e integración con un nivel de datos externo proporcionado a través de servicios REST.

---

### 2. Contexto del desarrollo

El proyecto se ha desarrollado en equipo dentro del grupo **G14 – 3TI21**, siguiendo una metodología de trabajo basada en reuniones periódicas, reparto de tareas y redacción de actas en formato Markdown.

Durante las primeras sesiones se priorizó:

- la constitución del equipo y la organización del repositorio
- la creación de la estructura base del proyecto
- la definición del alcance funcional del Hito 1
- el reparto y reajuste de responsabilidades entre integrantes

Posteriormente el trabajo se orientó a dejar preparada una base técnica común sobre la que poder implementar la parte funcional del proyecto.

---

### 3. Entorno técnico

| Componente | Versión |
|---|---|
| Servidor de aplicaciones | Apache Tomcat v10.1 |
| Entorno Java | Java Runtime Environment v25 |
| IDE principal | Eclipse |
| Repositorio compartido | GitHub — https://github.com/Danbag17/DEW |
| Paquete Java base | `dew` |

Se ha acordado trabajar mediante ramas individuales para facilitar el desarrollo paralelo, integrando progresivamente en `master`.

---

### 4. Estructura del proyecto

**Repositorio GitHub:**

```
actas/
docs/
scripts/
config/
entregas/
src/
```

**Proyecto Java web (`nol2526`):**

```
src/main/java/dew/
    client/
    filters/
    model/
    servlets/
    util/

src/main/webapp/
    css/
    js/
    img/
    META-INF/
    WEB-INF/
    WEB-INF/web.xml
    index.html
    css/app.css
```

---

### 5. Elementos preparados a fecha del Hito 1

**Clases Java:**

| Clase | Paquete | Responsable |
|---|---|---|
| `CentroEducativoClient.java` | `dew.client` | Carlos / Michal |
| `LogsFilter.java` | `dew.filters` | Mikel |
| `AlumnoAsignaturasServlet.java` | `dew.servlets` | Carlos |
| `AlumnoDetalleServlet.java` | `dew.servlets` | Carlos |
| `AlumnoExpedienteServlet.java` | `dew.servlets` | Carlos |
| `LoginRedirectServlet.java` | `dew.servlets` | Carlos |
| `LogoutServlet.java` | `dew.servlets` | Carlos |
| `SessionsUtils.java` | `dew.util` | Vanesa |

**Archivos web:**

| Archivo | Responsable |
|---|---|
| `index.html` | Pau |
| `css/app.css` | Pau |
| `WEB-INF/web.xml` | Daniel |

---

### 6. Reparto de tareas — Hito 1

| Integrante | Responsabilidad |
|---|---|
| Carlos Moldes Peña | Servlets del alumno + operaciones GET en `CentroEducativoClient` |
| Michal Pojnar | Servlets del profesor + operaciones POST/PUT en `CentroEducativoClient` |
| Pau Oroval González | Todas las vistas HTML/CSS con Bootstrap 5 |
| Mikel Escudero Aramburu | `LogsFilter` versión 2 completo y operativo |
| Daniel Zanon Barney | `web.xml` completo + `tomcat-users.xml` |
| Vanesa Carolina Castro Bello | `SessionsUtils.java` + redacción de actas |

---

### 7. Alcance funcional del Hito 1

Se ha priorizado el flujo del alumnado. Objetivo mínimo acordado:

- Autenticación del usuario con Tomcat
- Navegación del alumno: asignaturas → detalle → expediente
- Logout
- Sistema de logs (filtro v2)
- Integración mínima con CentroEducativo
- Documentación técnica y organizativa

La parte del profesorado queda prevista estructuralmente, pero no constituye la prioridad de esta entrega.

---

### 8. Seguridad y roles

Roles definidos: `rolalu` y `rolpro`.

La seguridad se apoya en:

- Autenticación mediante Tomcat
- Configuración declarativa en `web.xml`
- Comprobaciones programáticas en los servlets cuando sea necesario

En sesión se almacenan: `dni`, `password` y `key`, gestionados a través de `SessionsUtils`.

---

### 9. Sistema de logs

Se implementa mediante el filtro `LogsFilter` (versión 2). Cada entrada registra:

- Fecha y hora
- DNI del usuario
- IP del cliente
- Recurso accedido
- Método HTTP

El fichero de logs y su activación se configuran desde `web.xml`.

---

### 10. Integración con CentroEducativo

El proyecto se integra con el nivel de datos externo mediante servicios REST. La clase `CentroEducativoClient` centraliza todas las llamadas. Operaciones mínimas del Hito 1:

- Login y obtención de `key`
- Consulta de asignaturas del alumno
- Consulta de detalle o nota de una asignatura

---

### 11. Estado actual

A fecha de redacción de esta memoria, el proyecto dispone de:

- Repositorio operativo con estructura base creada
- Paquetes y clases iniciales preparados
- Entorno técnico común validado
- Documentación base iniciada
- Reparto de trabajo reajustado y cerrado

El trabajo pendiente se concentra en la implementación funcional:

- Completar `web.xml` y `tomcat-users.xml`
- Implementar autenticación
- Desarrollar `LogsFilter` v2
- Hacer funcional el flujo del alumnado
- Integrar operaciones REST con CentroEducativo
- Completar documentación y Javadocs

---

### 12. Conclusión

El grupo considera que, tras las sesiones iniciales de organización y puesta en marcha técnica, el proyecto dispone de una base suficiente para abordar el cierre del Hito 1. La prioridad a partir de este punto es la implementación funcional, las pruebas de integración y la consolidación de la documentación técnica.


La prioridad a partir de este punto debe centrarse en la implementación funcional, las pruebas de integración y la consolidación de la documentación técnica del trabajo realizado.
