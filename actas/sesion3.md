# Acta 03 – Puesta en marcha operativa, revisión técnica y cierre organizativo del Hito 1

**Asignatura:** Desarrollo Web (DEW) – Curso 2025/2026  
**Grupo:** G14 – 3TI21  
**Reunión nº:** 3  
**Fecha:** 11/05/2026  
**Hora:** 17:00 h  
**Lugar:** Laboratorio DSIC 3, ETSINF (UPV)  
**Secretario:** Vanesa Carolina Castro Bello  

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

1. Revisión del estado del repositorio compartido  
2. Clonación del proyecto en los entornos locales  
3. Revisión de ramas y flujo de trabajo con GitHub  
4. Creación y consolidación de la estructura del repositorio y del proyecto  
5. Revisión de la configuración de Eclipse, Tomcat y Java  
6. Revisión del archivo `web.xml` y de la estructura inicial del proyecto  
7. Revisión y actualización del reparto de tareas  
8. Estado actual del Hito 1  
9. Próximos pasos de implementación  
10. Ruegos y preguntas  

---

## 3. Desarrollo de la reunión

### 3.1. Estado del repositorio y clonación del proyecto

Durante la sesión se comprobó que el código del proyecto ya se encontraba en el repositorio compartido de GitHub y que este debía utilizarse como punto central de trabajo del grupo.

Se realizaron pruebas de clonación desde Eclipse, detectándose inicialmente confusión al intentar utilizar una URL web de una rama concreta en lugar de la URL HTTPS de clonación del repositorio completo. Una vez aclarado este punto, se consiguió clonar correctamente el proyecto y dejar operativa la copia local de trabajo.

Se confirmó también que el desarrollo debe realizarse desde ramas individuales, evitando trabajar directamente sobre la rama principal salvo en integraciones acordadas por el grupo.

---

### 3.2. Incidencia técnica detectada con GitHub y Eclipse

Durante la sesión se produjo una incidencia al intentar subir cambios al repositorio desde Eclipse. Aunque se habían creado correctamente nuevas carpetas, archivos de documentación y clases Java dentro del proyecto, parte de esos cambios no aparecían posteriormente en GitHub.

Tras revisar la situación, se identificaron varios factores que estaban provocando el problema:

1. Algunos cambios no habían sido incluidos en el `commit`, ya que permanecían en **Unstaged Changes** dentro de la vista **Git Staging** de Eclipse.
2. Existía confusión entre el hecho de crear carpetas en el proyecto y su aparición en GitHub, comprobándose que **Git no versiona carpetas vacías** si no contienen al menos un archivo.
3. La ventana de `commit` de Eclipse mostraba inicialmente un error de autor, ya que el campo **Author** no estaba definido con un formato válido de nombre y correo electrónico.
4. También surgieron dudas al diferenciar entre los archivos del proyecto y los archivos de configuración del servidor, especialmente en relación con `web.xml`.

Para resolver la incidencia, se adoptaron las siguientes medidas:

- se revisó el uso correcto de la vista **Git Staging**
- se añadieron explícitamente los archivos pendientes al índice mediante **Add to Index**
- se configuró correctamente el autor del `commit` con nombre y correo en formato válido
- se repitió el proceso de `commit` y posterior `push` sobre la rama personal correspondiente
- se aclaró que las carpetas vacías deben contener un archivo auxiliar, como `.gitkeep`, o un archivo real, para que Git las incluya en el repositorio

Una vez realizados estos pasos, se consiguió subir correctamente los archivos pendientes a la rama de trabajo del repositorio.

---

### 3.3. Estructura de carpetas y archivos preparada durante la sesión

Durante esta sesión se avanzó en la organización de la estructura base del repositorio y del proyecto Eclipse, con el objetivo de dejar preparada una base común para el desarrollo del Hito 1.

A nivel de repositorio, se consolidó la siguiente estructura principal:

```text
actas/
docs/
scripts/
config/
entregas/
src/
Asimismo, se dejaron preparados o creados diversos archivos iniciales de documentación y organización, incluyendo:

actas de reuniones
documentación técnica y funcional en docs/
estructura base de scripts
carpeta de configuración
carpeta de entregas

A nivel del proyecto Java web, se dejó organizada la siguiente estructura lógica:

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

Dentro de esta estructura, se crearon o dejaron preparados los siguientes archivos y clases base:

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

Se dejó también constancia de que el archivo web.xml válido del proyecto es el situado en src/main/webapp/WEB-INF/web.xml, diferenciándolo del web.xml perteneciente a la configuración del servidor en Eclipse.

3.4. Revisión del entorno técnico

Se comprobó el entorno técnico común del grupo, confirmándose como base de trabajo:

Apache Tomcat v10.1
Java Runtime Environment v25

Con ello, se valida que el proyecto está siendo desarrollado sobre una base homogénea y compatible con los requisitos del laboratorio.

3.5. Revisión y actualización del reparto de tareas

Durante la sesión se revisó nuevamente el reparto de tareas acordado en reuniones anteriores, al comprobarse que algunas responsabilidades inicialmente previstas ya no requerían el mismo esfuerzo o habían cambiado de alcance.

En particular, se tuvo en cuenta que el script de poblado ya estaba resuelto, y que por tanto convenía redistribuir el trabajo restante para centrarse en los elementos realmente críticos del Hito 1: servlets, configuración, filtros, vistas y gestión de sesión.

Tras la revisión, se acuerda el siguiente reparto operativo actualizado:

Integrante	Tareas asignadas
Carlos Moldes Peña	Desarrollo de los servlets de consulta del alumnado y operaciones GET necesarias en CentroEducativoClient
Michal Pojnar	Desarrollo de operaciones de inserción, actualización o borrado, especialmente en la parte del profesorado
Pau Oroval González	Desarrollo de la parte visual del proyecto: HTML y CSS de las vistas
Mikel Escudero Aramburu	Implementación del filtro LogsFilter versión 2
Daniel Zanon Barney	Configuración de web.xml, definición de mapeos, seguridad, autenticación y archivo tomcat-users.xml
Vanesa Carolina Castro Bello	Redacción y mantenimiento de las actas, implementación de SessionsUtils.java y apoyo general en las tareas pendientes de integración

Se acuerda además que este reparto podrá ajustarse ligeramente si durante la implementación aparecen bloqueos técnicos, dependencias no previstas o desequilibrios de carga, aunque se considera suficientemente concreto para afrontar el cierre del Hito 1.

3.6. Conclusión sobre el reparto y forma de trabajo

Tras la revisión realizada durante la sesión, se considera cerrado el reparto de tareas del grupo para afrontar el Hito 1, quedando cada integrante asignado a un bloque de trabajo concreto y suficientemente delimitado.

Se acuerda que este reparto será la referencia operativa para los próximos días, de modo que cada miembro avanzará desde su rama personal sobre su parte correspondiente, manteniendo la coordinación necesaria en aquellos elementos compartidos del proyecto.

Se confirma además que el desarrollo se realizará preferentemente mediante ramas individuales, de forma que cada integrante pueda avanzar en su bloque de trabajo con la mayor independencia posible.

No obstante, se deja constancia de que esta independencia requiere haber fijado previamente una base común, especialmente en los siguientes aspectos:

nombres de clases
rutas de servlets
paquetes
atributos de sesión
responsabilidades de cada archivo

Con ello se pretende facilitar la integración posterior en la rama principal y reducir tanto conflictos de merge como inconsistencias funcionales entre los distintos bloques del proyecto.

3.7. Librerías externas y dependencias

Durante la sesión se revisó también la necesidad de incorporar librerías externas al proyecto. Se concluyó que, para el Hito 1, no es necesario añadir de forma manual la API de servlets, puesto que esta ya viene proporcionada por el entorno de Tomcat 10.1.

Se acordó además lo siguiente:

utilizar Bootstrap por CDN para la parte visual del proyecto, siempre que resulte necesario
considerar Gson como librería externa Java útil para el tratamiento de respuestas JSON en caso de que la integración con CentroEducativo lo requiera
evitar incorporar librerías innecesarias que compliquen el despliegue o la integración entre entornos

Se acuerda documentar cualquier dependencia externa finalmente utilizada dentro de la carpeta docs/, indicando su finalidad y la forma en que se haya incorporado al proyecto.

3.8. Estado actual del Hito 1: trabajo realizado y trabajo pendiente

Durante la sesión se revisó el estado actual del proyecto de cara al Hito 1, diferenciando entre los elementos ya preparados y aquellos que todavía requieren implementación o cierre técnico.

Aspectos ya realizados o encauzados

Se considera que, a fecha de esta sesión, ya se dispone de una base de trabajo suficiente en los siguientes aspectos:

repositorio GitHub operativo
trabajo organizado mediante ramas individuales
proyecto clonado e importado en Eclipse
estructura base del repositorio creada
estructura base del proyecto Java web creada
paquetes principales del proyecto preparados:
client
filters
model
servlets
util
carpetas web preparadas:
css
js
img
WEB-INF
archivos base ya creados:
index.html
app.css
web.xml
clases Java iniciales del filtro, cliente REST y servlets
actas y documentación base iniciadas
reparto de responsabilidades ya establecido
alcance funcional del Hito 1 ya bastante definido
Aspectos todavía pendientes

Se concluye que lo que resta para cerrar el Hito 1 ya no es principalmente organizativo, sino de implementación y puesta en funcionamiento. En particular, quedan pendientes los siguientes elementos:

Adaptación real de web.xml al proyecto
inclusión de los servlets reales
alta del filtro de logs
declaración de roles
restricciones de seguridad
configuración de autenticación
Sistema de autenticación
definición operativa de acceso con Tomcat
aplicación de roles rolalu y rolpro
redirección correcta en función del usuario autenticado
Filtro Logs versión 2
implementación efectiva de LogsFilter
escritura en fichero persistente
registro mínimo de fecha/hora, usuario, IP, recurso y método HTTP
vinculación del filtro en web.xml
Flujo funcional del alumnado
vista inicial
login
lista de asignaturas
detalle de asignatura o nota
logout
expediente, si da tiempo
Integración con CentroEducativo
login REST
obtención de la key
almacenamiento en sesión
consulta de asignaturas
consulta de detalle o nota
Script de poblado
secuencia funcional de instrucciones curl
o script sh suficientemente documentado
Documentación técnica
completar los archivos en docs/
mantener actualizadas las actas
Javadocs
incluir comentarios Javadoc en clases y métodos principales del filtro, servlets y cliente REST
Valoración general

Se acuerda que el grupo ya dispone de una base razonable para afrontar el cierre del Hito 1, pero que el trabajo pendiente más importante se concentra ya en la implementación funcional, las pruebas y la documentación técnica final.

En consecuencia, a partir de esta sesión se considera prioritario dejar de ampliar la estructura del proyecto y centrarse en:

completar web.xml
implementar autenticación
desarrollar LogsFilter
hacer funcional el flujo del alumno
integrar las operaciones mínimas con CentroEducativo
completar documentación y script de poblado
4. Acuerdos adoptados
Se confirma GitHub como único repositorio de trabajo del grupo.
Se valida el trabajo mediante ramas individuales.
Se consolida la estructura base del repositorio y del proyecto Eclipse.
Se aclara el procedimiento correcto de commit y push desde Eclipse.
Se actualiza el reparto de tareas para ajustarlo al trabajo real pendiente del Hito 1.
Se acuerda que el trabajo futuro debe centrarse en implementación y no en seguir ampliando la estructura.
Se fija el uso de Tomcat 10.1 y Java 25 como base técnica común.
Se acuerda emplear Bootstrap por CDN si se utiliza apoyo visual externo.
Se considera Gson como librería útil si la integración con CentroEducativo requiere tratamiento de JSON.
Se da por cerrada la fase de organización básica y se entra en una fase de desarrollo efectivo.
5. Próximos pasos
completar web.xml
implementar la autenticación
desarrollar LogsFilter
hacer operativo el flujo del alumnado
integrar las operaciones mínimas con CentroEducativo
completar la documentación técnica y los Javadocs
preparar la revisión final del Hito 1
6. Validación del acta

El acta ha sido revisada y aceptada por todos los integrantes del grupo.

Nombre	Firma	Fecha
Vanesa Carolina Castro Bello		
Mikel Escudero Aramburu		
Carlos Moldes Peña		
Pau Oroval González		
Michal Pojnar		
Daniel Zanon Barney		

Acta redactada por Vanesa Carolina Castro Bello – Grupo G14


---

```markdown
# decisiones-iniciales.md

## Decisiones iniciales del proyecto

### 1. Proyecto y contexto
- **Proyecto:** Proyecto_DEW
- **Asignatura:** Desarrollo Web (DEW)
- **Curso:** 2025/2026
- **Grupo:** G14 – 3TI21

### 2. Base técnica acordada
- **Servidor de aplicaciones:** Apache Tomcat v10.1
- **Entorno Java:** Java Runtime Environment v25
- **Paquete Java base:** `dew`

### 3. Organización del repositorio
El repositorio se estructura en las siguientes carpetas principales:

```text
actas/
docs/
scripts/
config/
entregas/
src/
4. Organización del proyecto

La estructura principal del proyecto queda organizada en:

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
5. Estrategia de trabajo

Se acuerda trabajar mediante ramas individuales en GitHub, evitando desarrollar directamente sobre la rama principal salvo integración acordada.

6. Prioridad funcional del Hito 1

Se prioriza el flujo del alumnado frente a otras partes del sistema. El Hito 1 debe centrarse en:

autenticación
navegación del alumno
logs
integración con CentroEducativo
documentación
configuración mínima del entorno
7. Dependencias y librerías

Se acuerda:

no añadir manualmente la API de servlets, al estar ya proporcionada por Tomcat
utilizar Bootstrap por CDN si se decide usar apoyo visual basado en framework
valorar el uso de Gson como librería externa para el tratamiento de JSON
8. Observación

A partir de la sesión 3 se considera cerrada la fase de estructuración básica y se prioriza el desarrollo funcional del Hito 1.