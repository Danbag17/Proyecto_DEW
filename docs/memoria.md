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

---

# 13. Evolución tras el Hito 1

## 13.1 Objetivo de la segunda fase

Tras la validación del Hito 1, el objetivo principal pasó de disponer de una estructura funcional mínima a completar la totalidad de requisitos exigidos por el enunciado del proyecto.

Las prioridades establecidas fueron:

* completar el flujo del profesorado;
* consolidar la integración REST;
* implementar AJAX;
* reforzar la autenticación y gestión de sesiones;
* incorporar fotografías del alumnado;
* mejorar la experiencia visual;
* finalizar la documentación técnica.

---

## 13.2 Evolución de la arquitectura

La arquitectura prevista durante el Hito 1 se mantuvo, aunque se reforzó la separación entre capas.

Arquitectura final:

```text
Navegador
    ↓
HTML + CSS + JavaScript
    ↓
AJAX (fetch)
    ↓
Servlets
    ↓
CentroEducativoClient
    ↓
CentroEducativo REST
```

Esta separación permitió mantener desacopladas:

* presentación;
* lógica web;
* acceso a datos.

---

## 13.3 Integración completa con CentroEducativo

Durante el Hito 1 únicamente se habían priorizado operaciones de consulta para alumnado.

La versión final incorpora:

### Alumnado

* consulta de asignaturas;
* consulta de detalle;
* consulta de expediente.

### Profesorado

* consulta de asignaturas impartidas;
* consulta de alumnado matriculado;
* modificación de notas.

Toda la comunicación quedó centralizada en:

```text
dew.client.CentroEducativoClient
```

---

## 13.4 Sistema de autenticación definitivo

La autenticación final se apoya en dos mecanismos complementarios.

### Tomcat

Responsable de:

* validar credenciales;
* gestionar roles;
* proteger recursos.

### CentroEducativo

Responsable de:

* autenticar contra el sistema académico;
* generar la session key.

Para coordinar ambos sistemas se incorporó:

```text
dew.filters.AuthFilter
```

que realiza automáticamente la autenticación REST y crea la sesión interna de NOL.

---

## 13.5 Gestión de sesiones

La gestión de sesiones evolucionó significativamente respecto al diseño inicial.

La clase:

```text
dew.util.SessionsUtils
```

se convirtió en el punto único de acceso a:

* dni;
* password;
* key;
* rol.

Esto permitió reducir duplicación de código y facilitar el mantenimiento.

---

## 13.6 Desarrollo del flujo del profesorado

La funcionalidad de profesorado quedó completamente implementada.

Capacidades finales:

* consultar asignaturas impartidas;
* consultar alumnos matriculados;
* visualizar fichas académicas;
* modificar calificaciones;
* consultar información ampliada del alumnado.

La modificación de notas se realiza mediante AJAX sin necesidad de recargar páginas completas.

---

## 13.7 Incorporación de AJAX

Una de las principales mejoras respecto al Hito 1 fue la incorporación sistemática de AJAX.

La filosofía adoptada fue:

```text
Vista HTML
     ↓
JavaScript
     ↓
Fetch
     ↓
Servlet
     ↓
JSON
```

Ventajas obtenidas:

* menor número de recargas;
* mayor fluidez;
* mejor separación entre frontend y backend.

---

## 13.8 Evolución del frontend

La interfaz pasó de una estructura básica a una aplicación visualmente completa.

Tecnologías utilizadas:

* Bootstrap 5;
* CSS personalizado;
* JavaScript;
* AJAX.

Se añadieron:

* navegación por roles;
* tarjetas;
* tablas dinámicas;
* estilos de impresión;
* páginas de error;
* integración de fotografías.

---

## 13.9 Fotografías del alumnado

La versión final incorpora fotografías asociadas a cada alumno.

Se adoptó el criterio:

```text
fotos/<DNI>.png
```

Ejemplo:

```text
fotos/12345678A.png
```

La fotografía se carga automáticamente utilizando el DNI recibido desde CentroEducativo.

Esta solución evita almacenar imágenes en JSON o realizar peticiones adicionales al backend.

---

## 13.10 Expediente académico y certificado

La funcionalidad de expediente evolucionó hasta convertirse en una de las vistas más completas del proyecto.

Incluye:

* datos personales;
* asignaturas;
* notas;
* créditos;
* fotografía;
* nota media;
* formato de impresión.

Para obtener toda la información fue necesario combinar datos procedentes de varias consultas REST.

---

## 13.11 Sistema de logs

El filtro:

```text
dew.filters.LogsFilter
```

permite registrar:

* fecha;
* usuario;
* IP;
* recurso solicitado;
* método HTTP.

La configuración continúa realizándose mediante parámetros definidos en:

```text
WEB-INF/web.xml
```

---

## 13.12 Seguridad y control de acceso

La seguridad definitiva combina:

### Seguridad declarativa

Configurada mediante:

```xml
<security-constraint>
```

y

```xml
<security-role>
```

### Seguridad programática

Aplicada mediante:

* AuthFilter;
* SessionsUtils;
* validaciones en servlets.

Esta combinación protege tanto páginas como endpoints AJAX.

---

## 13.13 Problemas encontrados

Durante el desarrollo se resolvieron incidencias relacionadas con:

* autenticación Tomcat;
* integración REST;
* configuración de Tomcat;
* despliegue en distintos entornos;
* rutas relativas;
* sesiones;
* logout;
* caché del navegador;
* carga dinámica mediante AJAX.

La resolución de estos problemas permitió estabilizar la aplicación y mejorar la experiencia de usuario.

---

## 13.14 Estado final del proyecto

La aplicación desarrollada cumple los requisitos principales definidos en el enunciado:

### Alumno

* autenticación;
* consulta de asignaturas;
* consulta de detalle;
* consulta de expediente;
* certificado imprimible;
* visualización de fotografía.

### Profesor

* autenticación;
* consulta de asignaturas;
* consulta de alumnado;
* modificación de notas;
* visualización de fichas.

### Infraestructura

* autenticación FORM;
* control de roles;
* integración REST;
* gestión de sesiones;
* logs;
* AJAX;
* Bootstrap;
* documentación técnica.

---

## 13.15 Conclusión final

Tras la finalización de la segunda fase, el proyecto evolucionó desde una base funcional centrada en alumnado hasta una aplicación web completa que integra autenticación, roles, sesiones, comunicación REST, AJAX, gestión académica, fotografías y documentación técnica. La arquitectura adoptada permitió mantener una separación clara entre interfaz, lógica de aplicación y acceso a datos, facilitando tanto el desarrollo como el mantenimiento del sistema.

