# Acta 01 – Constitución del Equipo de Trabajo

**Asignatura:** Desarrollo Web (DEW) – Curso 2025/2026  
**Grupo:** G14 – 3TI21  
**Reunión nº:** 1  
**Fecha:** 27/04/2026  
**Hora:** 17:00 h  
**Lugar:** Laboratorio DSIC 3, ETSINF (UPV)  
**Secretario:** Vanesa Castro Bello
---

## 1. Asistentes

| Nombre y apellidos | DNI/NIE | Correo UPV | Grupo |
|---|---|---|---|
| Vanesa Carolina Castro Bello | 73281209 | vccasbel@etsinf.upv.es | 3TI21 |
| Mikel Escudero Aramburu | 49467180 | mescara@etsinf.upv.es | 3TI21 |
| Carlos Moldes Peña | 35589876 | cmolpea@etsinf.upv.es | 3TI21 |
| Pau Oroval González | 26626414 | porogon@etsinf.upv.es | 3TI21 |
| Michal Pojnar | PDFH473676 | mpojnar@etsinf.upv.es | 3TI21 |
| Daniel Zanon Barney | 45914067 | dzanbar@etsinf.upv.es | 3TI21 |

---

## 2. Orden del día

1. Presentación de los integrantes del grupo
2. Elección del secretario/a
3. Establecimiento de canales de comunicación y repositorio
4. Reparto inicial de roles y tareas
5. Planificación de reuniones
6. Expectativas de cada miembro
7. Definición inicial del alcance funcional y técnico
8. Ruegos y preguntas

---

## 3. Desarrollo de la reunión

### 3.1. Presentación de los integrantes

Los seis miembros del grupo se presentaron y compartieron su información de contacto. Todos son estudiantes del grupo 3TI21 del Grado en Ingeniería Informática (ETSINF, UPV).

### 3.2. Elección del secretario/a

Por acuerdo unánime, se designa a **Carlos Moldes Peña** como secretario del equipo. Su responsabilidad será redactar y custodiar las actas de todas las reuniones, así como distribuirlas al resto de integrantes para su validación.

### 3.3. Canales de comunicación y repositorio

Se acuerda utilizar los siguientes medios:

- **Comunicación rápida:** Grupo de WhatsApp creado durante la reunión con todos los integrantes.
- **Repositorio de código y documentación:** GitHub, en el repositorio [https://github.com/Danbag17/DEW](https://github.com/Danbag17/DEW). En él se almacenará el código fuente, la documentación del proyecto, las actas y otros materiales compartidos del trabajo.

### 3.4. Reparto inicial de roles y tareas

Con el objetivo de estructurar el trabajo de cara al **Hito 1 (entrega 15/05/2026)**, se acuerda el siguiente reparto orientativo. Los roles podrán ajustarse según el avance del proyecto:

| Área | Responsable(s) | Descripción |
|---|---|---|
| **Filtro de Logs** | Mikel Escudero Aramburu | Implementación del filtro en sus versiones 0, 1 y 2 |
| **Autenticación y sesiones** | Daniel Zanon Barney | Configuración de Tomcat, `web.xml`, archivo de usuarios de Tomcat y coordinación de sesiones con CentroEducativo |
| **Servlets y lógica de aplicación** | Vanesa Carolina Castro Bello / Pau Oroval González | Desarrollo de los servlets principales: login, lista de asignaturas y notas del alumno |
| **Script de poblado (curl)** | Michal Pojnar | Script de shell para inicializar CentroEducativo con alumnos, profesores, asignaturas y notas |
| **Integración y pruebas** | Carlos Moldes Peña | Integración de los módulos, pruebas end-to-end y coordinación técnica general |
| **Actas y documentación** | Carlos Moldes Peña | Redacción de actas, documentación de clases y métodos (Javadoc) |

> **Nota:** El reparto es flexible y se revisará en cada sesión práctica y según las necesidades del proyecto.

### 3.5. Planificación de reuniones

Se acuerda reunirse, como mínimo, en cada sesión práctica del lunes (3TI21) según el calendario oficial:

| Sesión | Fecha |
|---|---|
| Sesión 1 | 27/04/2026 |
| Sesión 2 | 04/05/2026 |
| Sesión 3 | 11/05/2026 |
| Sesión 4 | 18/05/2026 |
| Sesión 5 | 25/05/2026 |

Adicionalmente, se podrán convocar reuniones extraordinarias por WhatsApp cuando algún miembro lo considere necesario, con un preaviso mínimo de 24 horas.

En cada reunión se levantará acta en formato Markdown y se compartirá con todos los integrantes para su revisión y validación.

### 3.6. Expectativas de cada miembro

Los integrantes expresan su intención de implicarse de forma equitativa en el proyecto, con el objetivo de obtener la máxima calificación posible. Se acuerda que cualquier dificultad, ausencia o desequilibrio en la carga de trabajo deberá comunicarse al grupo cuanto antes para poder redistribuir tareas a tiempo.

### 3.7. Definición inicial del alcance funcional y técnico

Durante la reunión se realizó una primera aproximación a la estructura funcional de la aplicación y a los bloques técnicos principales que deberán implementarse en el proyecto.

#### 3.7.1. Vistas HTML identificadas inicialmente

Se identifican, de forma provisional, las siguientes vistas HTML a implementar:

1. **Index**  
   Página inicial de bienvenida de la aplicación.

2. **Login**  
   Vista de autenticación para el acceso de usuarios.

3. **Vista alumno – lista de asignaturas**  
   Página en la que el alumnado podrá consultar las asignaturas en las que está matriculado.

4. **Vista profesor – lista de asignaturas y lista de alumnos**  
   Página en la que el profesorado podrá consultar las asignaturas que imparte y el alumnado asociado a cada una de ellas.

5. **Vista ficha de alumno**  
   Página de detalle con la información individual de un alumno o alumna.

6. **Vista expediente de alumno**  
   Página destinada a mostrar el resumen completo del expediente o certificado de notas del alumno.

> Estas vistas se consideran una primera propuesta y podrán ajustarse conforme avance el desarrollo del proyecto.

#### 3.7.2. APIs y operaciones pendientes de definir

Se acuerda que será necesario concretar y documentar las APIs internas o puntos de acceso a datos necesarios para implementar, como mínimo, las siguientes operaciones:

- **Consultar, actualizar y persistir notas**
- **Consultar asignaturas**
- **Consultar alumnos**

Se deja pendiente concretar cómo se traducirán estas operaciones en servlets, llamadas REST a CentroEducativo y posibles endpoints auxiliares para interacciones AJAX.

#### 3.7.3. Autenticación, roles y control de acceso

Se considera prioritario implementar un sistema de autenticación que garantice que cada usuario únicamente pueda acceder a la información que le corresponde según su rol dentro de la aplicación.

En particular, se establecen las siguientes líneas de trabajo:

- Implementar autenticación para restringir el acceso a vistas y operaciones.
- Definir los **roles de usuario** en la configuración de Tomcat, mediante el archivo de usuarios correspondiente.
- Aplicar una seguridad básica basada en:
  - autenticación,
  - permisos según rol,
  - vistas ofrecidas a cada tipo de usuario,
  - y restricciones en las operaciones disponibles en las APIs.

Se acuerda que esta parte será clave para garantizar que:

- un alumno solo pueda ver sus propios datos,
- un profesor solo pueda acceder a la información de las asignaturas que imparte,
- y no se puedan consultar ni modificar datos fuera del ámbito autorizado.

#### 3.7.4. Seguridad básica de la aplicación

Se acuerda que la seguridad inicial del sistema se basará en la autenticación, los permisos, las vistas ofrecidas según rol y la configuración de las APIs para que solo permitan operaciones autorizadas.

#### 3.7.5. Sistema de logs del servidor

Se identifica como objetivo implementar un sistema de logs de uso del servidor que permita registrar, de forma persistente, el uso de cada servlet, indicando al menos:

- **quién** realiza la acción,
- **qué** servlet o recurso se activa,
- y **cuándo** ocurre.

Como estrategia incremental, se acuerda:

1. **Primera implementación provisional**  
   Implementar una versión inicial en la que la información de logs se consulte o visualice en pantalla dentro de las propias vistas, con el fin de comprobar el funcionamiento general del mecanismo.

2. **Definición detallada del contenido del log**  
   Antes de la migración a la versión final, habrá que concretar con detalle:
   - qué información exacta se registrará,
   - en qué formato,
   - y con qué estructura se almacenará.

3. **Migración posterior a filtro persistente**  
   Una vez validado el funcionamiento básico, se migrará el sistema a una estructura separada y persistente basada en un **filtro**.

---

## 4. Acuerdos adoptados

1. Carlos Moldes Peña asume el rol de **secretario**.
2. El canal de comunicación principal del grupo será **WhatsApp**.
3. El repositorio compartido del grupo será **GitHub** ([https://github.com/Danbag17/DEW](https://github.com/Danbag17/DEW)).
4. El repositorio se utilizará para almacenar código, documentación, actas y materiales de apoyo del proyecto.
5. Se adopta el reparto inicial de tareas descrito en el apartado 3.4, sujeto a revisión.
6. El grupo se reunirá obligatoriamente en cada sesión práctica del lunes y, si fuera necesario, en reuniones extraordinarias adicionales.
7. Todas las reuniones deberán quedar reflejadas en actas redactadas en **Markdown**.
8. Todas las actas deberán ser revisadas y validadas por todos los integrantes.
9. Se establece una primera propuesta de seis vistas HTML principales: index, login, vista de alumno, vista de profesor, ficha de alumno y expediente de alumno.
10. Se deja pendiente definir en detalle las APIs y operaciones necesarias para consultar alumnos, asignaturas y notas, así como actualizar y persistir calificaciones.
11. Se considera prioritario implementar autenticación y control de acceso por roles para que cada usuario solo pueda visualizar y operar sobre la información que le corresponde.
12. Los roles iniciales se configurarán en Tomcat mediante el archivo de usuarios correspondiente.
13. La seguridad básica de la aplicación se apoyará en autenticación, permisos, vistas según rol y restricciones en las APIs.
14. El sistema de logs se desarrollará de forma incremental: primero como mecanismo provisional visible en pantalla y posteriormente como filtro persistente separado.

---

## 5. Validación por los integrantes

El acta ha sido revisada y aceptada por todos los miembros del grupo.

| Nombre | Firma | Fecha |
|---|---|---|
| Vanesa Carolina Castro Bello | | |
| Mikel Escudero Aramburu | | |
| Carlos Moldes Peña | | |
| Pau Oroval González | | |
| Michal Pojnar | | |
| Daniel Zanon Barney | | |

---

*Acta redactada por Vanesa Castro Bello – Secretaria del Grupo G14*
