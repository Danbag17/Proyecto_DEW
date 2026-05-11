
# memoria.md

## Memoria técnica breve – Hito 1

### 1. Introducción

Este documento resume el estado del proyecto **Proyecto_DEW** correspondiente al laboratorio 2 de la asignatura **Desarrollo Web (DEW)** del curso **2025/2026**.

El objetivo del proyecto es desarrollar una aplicación web de consulta y gestión académica basada en servlets Java, con autenticación, control de acceso por roles, registro de uso mediante logs e integración con un nivel de datos externo proporcionado a través de servicios REST.

---

### 2. Contexto del desarrollo

El proyecto se ha desarrollado en equipo dentro del grupo **G14 – 3TI21**, siguiendo una metodología de trabajo basada en reuniones periódicas, reparto de tareas y redacción de actas en formato Markdown.

Durante las primeras sesiones se priorizó:

- la constitución del equipo
- la organización del repositorio compartido
- la creación de la estructura base del proyecto
- la definición del alcance funcional del Hito 1
- el reparto de responsabilidades entre integrantes

Posteriormente, el trabajo se orientó a dejar preparada una base técnica común sobre la que poder implementar la parte funcional del proyecto.

---

### 3. Entorno técnico

El proyecto se ha preparado para ejecutarse sobre el siguiente entorno:

- **Servidor de aplicaciones:** Apache Tomcat v10.1
- **Entorno Java:** Java Runtime Environment v25
- **IDE principal:** Eclipse
- **Paquete Java base:** `dew`

Se ha utilizado GitHub como repositorio compartido del grupo y se ha acordado trabajar mediante ramas individuales para facilitar el desarrollo paralelo.

---

### 4. Estructura del proyecto

La estructura general del repositorio se ha organizado del siguiente modo:

```text
actas/
docs/
scripts/
config/
entregas/
src/

La estructura interna del proyecto Java web se ha organizado en torno a:

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
5. Elementos preparados

A fecha del Hito 1, se han dejado creados o iniciados los siguientes elementos principales:

Parte Java
CentroEducativoClient.java
LogsFilter.java
AlumnoAsignaturasServlet.java
AlumnoDetalleServlet.java
AlumnoExpedienteServlet.java
LoginRedirectServlet.java
LogoutServlet.java
SessionsUtils.java
Parte web
index.html
css/app.css
WEB-INF/web.xml
Documentación
actas de las reuniones
documentación funcional y técnica en docs/
6. Alcance funcional del Hito 1

Para el Hito 1 se ha priorizado el flujo correspondiente al alumnado. En concreto, se ha definido como objetivo mínimo:

autenticación del usuario
navegación del alumno
consulta de asignaturas
consulta del detalle o nota de una asignatura
logout
sistema de logs
integración mínima con CentroEducativo
documentación técnica y organizativa

La parte correspondiente al profesorado queda prevista estructuralmente, pero no constituye la prioridad principal de esta primera entrega.

7. Seguridad y roles

Se han definido dos roles principales dentro del sistema:

rolalu
rolpro

La seguridad de la aplicación se apoyará en:

autenticación mediante Tomcat
configuración declarativa en web.xml
comprobaciones complementarias dentro de los servlets cuando sea necesario

Asimismo, se ha previsto el uso de sesión para almacenar la información mínima necesaria para la integración con el nivel de datos externo.

8. Logs

Se ha previsto la implementación de un filtro LogsFilter encargado de registrar el uso de la aplicación.

Cada entrada de log deberá incluir, como mínimo:

fecha y hora
usuario
IP del cliente
recurso o servlet accedido
método HTTP

Se ha acordado que este sistema de registro sea persistente y configurable desde web.xml.

9. Integración con CentroEducativo

El proyecto no se conecta directamente a una base de datos propia, sino que debe integrarse con el nivel de datos proporcionado mediante servicios REST.

Para ello se ha previsto una clase cliente específica, CentroEducativoClient.java, encargada de centralizar operaciones como:

login
obtención de la key
consulta de asignaturas
consulta del detalle académico
otras operaciones necesarias según avance el proyecto
10. Organización del trabajo

Durante la sesión 3 se revisó y concretó el reparto de tareas del grupo para afrontar la entrega del Hito 1, asignando bloques específicos de trabajo a cada integrante en función de las necesidades reales del proyecto.

También se acordó trabajar mediante ramas individuales, con integración posterior, fijando previamente una base común de nombres de clases, responsabilidades y elementos compartidos para evitar inconsistencias funcionales.

11. Estado actual

A fecha de redacción de esta memoria, el proyecto dispone ya de:

repositorio operativo
estructura base del proyecto creada
paquetes y clases iniciales preparados
entorno técnico común validado
documentación base iniciada
reparto de trabajo actualizado

El trabajo pendiente se concentra ya principalmente en:

completar web.xml
implementar autenticación
desarrollar el filtro de logs
hacer funcional el flujo del alumnado
integrar las operaciones mínimas con CentroEducativo
completar documentación y Javadocs
12. Conclusión

El grupo considera que, tras las sesiones iniciales de organización y puesta en marcha técnica, el proyecto dispone de una base suficiente para abordar el cierre del Hito 1.

La prioridad a partir de este punto debe centrarse en la implementación funcional, las pruebas de integración y la consolidación de la documentación técnica del trabajo realizado.
