# Acta 03 – Puesta en marcha operativa, reorganización técnica y seguimiento del Hito 1

**Asignatura:** Desarrollo Web (DEW) – Curso 2025/2026
**Grupo:** G14 – 3TI21
**Reunión nº:** 3
**Fecha:** 11/05/2026
**Hora:** 17:00 h
**Lugar:** Laboratorio DSIC 3, ETSINF (UPV)
**Secretaria:** Vanesa Carolina Castro Bello

---

# 1. Asistentes

| Nombre y apellidos           | DNI/NIE    | Correo UPV                                              | Grupo |
| ---------------------------- | ---------- | ------------------------------------------------------- | ----- |
| Vanesa Carolina Castro Bello | 73281209   | [vccasbel@etsinf.upv.es](mailto:vccasbel@etsinf.upv.es) | 3TI21 |
| Mikel Escudero Aramburu      | 49467180   | [mescara@etsinf.upv.es](mailto:mescara@etsinf.upv.es)   | 3TI21 |
| Carlos Moldes Peña           | 35589876   | [cmolpea@etsinf.upv.es](mailto:cmolpea@etsinf.upv.es)   | 3TI21 |
| Pau Oroval González          | 26626414   | [porogon@etsinf.upv.es](mailto:porogon@etsinf.upv.es)   | 3TI21 |
| Michal Pojnar                | PDFH473676 | [mpojnar@etsinf.upv.es](mailto:mpojnar@etsinf.upv.es)   | 3TI21 |
| Daniel Zanon Barney          | 45914067   | [dzanbar@etsinf.upv.es](mailto:dzanbar@etsinf.upv.es)   | 3TI21 |

---

# 2. Orden del día

1. Revisión del estado del repositorio compartido
2. Clonación e integración del proyecto en Eclipse
3. Revisión del flujo de trabajo con GitHub
4. Consolidación de la estructura del proyecto
5. Revisión del entorno técnico común
6. Actualización del reparto de tareas
7. Revisión del estado real del Hito 1
8. Dependencias externas y librerías
9. Próximos pasos de implementación
10. Ruegos y preguntas

---

# 3. Desarrollo de la reunión

## 3.1. Estado del repositorio y clonación del proyecto

Durante la sesión se comprobó el estado del repositorio compartido del grupo y se verificó que todos los integrantes disponían ya de acceso al repositorio GitHub acordado en sesiones anteriores.

Se realizaron pruebas de clonación e importación del proyecto desde Eclipse, detectándose inicialmente cierta confusión al utilizar URLs asociadas a ramas concretas en lugar de la URL HTTPS general del repositorio.

Tras revisar el procedimiento correcto, se consiguió:

* clonar correctamente el proyecto
* importar el proyecto en Eclipse
* vincular el proyecto con EGit
* dejar operativas las ramas individuales de trabajo

Asimismo, se recordó que el trabajo diario debe realizarse preferentemente desde ramas personales, evitando modificar directamente la rama `master` salvo durante integraciones acordadas por el grupo.

---

## 3.2. Incidencia técnica con GitHub y Eclipse

Durante la sesión surgieron incidencias al intentar subir cambios desde Eclipse al repositorio GitHub.

En particular, se detectó que:

* algunos archivos permanecían en `Unstaged Changes`
* ciertos cambios no aparecían posteriormente en GitHub
* existía confusión respecto a las carpetas vacías
* el autor del commit no estaba correctamente configurado
* surgieron dudas sobre el `web.xml` válido del proyecto

Tras revisar el problema, se concluyó que:

1. Git no versiona carpetas vacías si no contienen archivos.
2. Algunos cambios no habían sido añadidos correctamente al índice de Git.
3. El autor del commit debía configurarse con nombre y correo válidos.
4. El `web.xml` correcto del proyecto es el situado en:

```text id="m5af7l"
src/main/webapp/WEB-INF/web.xml
```

y no el perteneciente a la configuración interna del servidor en Eclipse.

Para resolver la incidencia se realizaron las siguientes acciones:

* revisión del uso de `Git Staging`
* uso de `Add to Index`
* repetición del proceso de commit y push
* configuración correcta del autor de Git
* comprobación manual del estado del repositorio remoto

Una vez realizados estos pasos, se consiguió subir correctamente los cambios pendientes al repositorio compartido.

---

## 3.3. Consolidación de la estructura del proyecto

Durante la sesión se consolidó la estructura principal del repositorio y del proyecto Java Web.

### Estructura general del repositorio

```text id="h75k3w"
actas/
docs/
scripts/
config/
entregas/
src/
```

### Estructura Java del proyecto

```text id="2rr9xw"
src/main/java/dew/
    client/
    filters/
    model/
    servlets/
    util/
```

### Estructura web

```text id="54gpyf"
src/main/webapp/
    css/
    js/
    img/
    META-INF/
    WEB-INF/
```

Asimismo, se dejaron preparados o creados los siguientes elementos iniciales:

## Parte Java

* `CentroEducativoClient.java`
* `LogsFilter.java`
* `AlumnoAsignaturasServlet.java`
* `AlumnoDetalleServlet.java`
* `AlumnoExpedienteServlet.java`
* `LoginRedirectServlet.java`
* `LogoutServlet.java`
* `SessionsUtils.java`

## Parte web

* `index.html`
* `app.css`
* `web.xml`

## Documentación

* actas
* archivos `.md` en `docs/`
* estructura base de scripts
* carpetas de configuración y entregas

---

## 3.4. Revisión del entorno técnico

Se revisó el entorno técnico común utilizado por el grupo, confirmándose como base definitiva:

| Componente               | Versión                      |
| ------------------------ | ---------------------------- |
| Servidor de aplicaciones | Apache Tomcat v10.1          |
| Entorno Java             | Java Runtime Environment v25 |

También se revisó la necesidad de librerías externas.

Se acordó:

* no añadir manualmente la API de servlets, ya proporcionada por Tomcat
* utilizar Bootstrap mediante CDN para el apoyo visual
* valorar el uso de Gson para el tratamiento de respuestas JSON si la integración con CentroEducativo lo requiere
* evitar dependencias innecesarias que compliquen el despliegue

---

## 3.5. Revisión y actualización del reparto de tareas

Durante la sesión se revisó nuevamente el reparto de tareas acordado anteriormente, adaptándolo al estado real del proyecto y al trabajo pendiente para el Hito 1.

Se tuvo en cuenta especialmente que:

* el script de poblado ya se encontraba bastante avanzado
* el trabajo pendiente se concentra ya principalmente en implementación funcional
* era necesario redistribuir responsabilidades de forma más concreta

Tras la revisión, se acuerda el siguiente reparto operativo:

| Integrante                   | Tareas asignadas                                                                                                                |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| Carlos Moldes Peña           | Desarrollo de servlets del flujo del alumnado y operaciones GET de `CentroEducativoClient`                                      |
| Michal Pojnar                | Desarrollo de operaciones de inserción, actualización y borrado relacionadas con el profesorado                                 |
| Pau Oroval González          | Desarrollo de HTML y CSS de las vistas del proyecto                                                                             |
| Mikel Escudero Aramburu      | Implementación del filtro `LogsFilter` versión 2                                                                                |
| Daniel Zanon Barney          | Configuración de `web.xml`, autenticación, roles y `tomcat-users.xml`                                                           |
| Vanesa Carolina Castro Bello | Redacción y mantenimiento de actas y documentación `.md`, implementación de `SessionsUtils.java` y apoyo general de integración |

Se acuerda además que este reparto podrá ajustarse ligeramente si aparecen bloqueos técnicos o desequilibrios importantes de carga.

---

## 3.6. Gestión de sesión y session key

Durante la sesión surgió un debate técnico sobre la integración con CentroEducativo y la gestión de la `session key` devuelta tras el login REST.

Se acuerda que:

* la aplicación deberá autenticarse contra CentroEducativo
* la operación de login devolverá una `key`
* dicha `key` deberá reutilizarse posteriormente en las consultas autenticadas

Para centralizar esta gestión, se acuerda implementar una utilidad común denominada:

```text id="c7sqth"
SessionsUtils.java
```

Esta clase tendrá como responsabilidad:

* crear sesiones
* almacenar atributos relevantes
* recuperar atributos de sesión
* invalidar sesiones en logout

Como base común, se acuerda utilizar los siguientes atributos de sesión:

| Atributo   | Descripción                              |
| ---------- | ---------------------------------------- |
| `dni`      | Identificador del usuario                |
| `password` | Contraseña si fuera necesaria            |
| `key`      | Session key devuelta por CentroEducativo |

Se considera que esta solución facilitará la integración entre servlets y reducirá inconsistencias entre ramas.

---

## 3.7. Estado actual del Hito 1

Durante la sesión se revisó el estado real del proyecto de cara al Hito 1.

### Aspectos ya preparados o encauzados

Se considera ya resuelto o suficientemente avanzado:

* repositorio GitHub operativo
* trabajo mediante ramas individuales
* proyecto clonado e importado en Eclipse
* estructura base del proyecto creada
* paquetes principales preparados
* carpetas web creadas
* documentación inicial en Markdown
* servlets base preparados
* entorno técnico validado
* reparto de tareas actualizado

### Aspectos todavía pendientes

Se concluye que el trabajo pendiente ya es principalmente funcional.

En particular, quedan pendientes:

### `web.xml`

* mapeos reales de servlets
* filtro de logs
* roles
* restricciones de seguridad
* autenticación

### Autenticación

* integración con Tomcat
* aplicación de roles `rolalu` y `rolpro`
* redirección correcta tras login

### LogsFilter v2

* implementación efectiva
* persistencia en fichero
* registro mínimo de:

  * fecha/hora
  * usuario
  * IP
  * recurso
  * método HTTP

### Flujo del alumnado

* login funcional
* lista de asignaturas
* detalle de asignatura
* logout
* expediente, si el flujo principal queda estable

### Integración con CentroEducativo

* login REST
* obtención de la `key`
* almacenamiento en sesión
* consultas GET

### Documentación técnica

* completar documentación `.md`
* completar Javadocs

---

## 3.8. Valoración general

El grupo considera que la fase organizativa y estructural del proyecto puede darse prácticamente por cerrada.

A partir de este punto, el trabajo debe centrarse prioritariamente en:

* implementación funcional
* autenticación
* integración REST
* configuración de seguridad
* logs
* pruebas
* documentación técnica final

Asimismo, se acuerda evitar ampliar innecesariamente la estructura del proyecto y concentrar el esfuerzo restante en conseguir un prototipo funcional estable para el Hito 1.

---

# 4. Acuerdos adoptados

1. Se confirma GitHub como único repositorio oficial del grupo.
2. El trabajo continuará realizándose mediante ramas individuales.
3. Se consolida la estructura base definitiva del proyecto.
4. Se valida el uso de Tomcat 10.1 y Java 25.
5. Se acuerda utilizar Bootstrap por CDN si resulta necesario.
6. Se considera Gson como posible librería auxiliar para JSON.
7. Se redefine el reparto operativo de tareas.
8. Se acuerda centralizar la gestión de sesión mediante `SessionsUtils.java`.
9. Se da por cerrada la fase de organización básica.
10. El trabajo restante debe centrarse en funcionalidad e integración.

---

# 5. Próximos pasos

* completar `web.xml`
* implementar autenticación
* desarrollar `LogsFilter`
* implementar login REST
* integrar la `session key`
* hacer funcional el flujo del alumnado
* completar documentación técnica
* añadir Javadocs
* preparar pruebas finales del Hito 1

---

# 6. Validación del acta

El acta ha sido revisada y aceptada por todos los integrantes del grupo.

| Nombre                       | Firma | Fecha |
| ---------------------------- | ----- | ----- |
| Vanesa Carolina Castro Bello |       |       |
| Mikel Escudero Aramburu      |       |       |
| Carlos Moldes Peña           |       |       |
| Pau Oroval González          |       |       |
| Michal Pojnar                |       |       |
| Daniel Zanon Barney          |       |       |

---

*Acta redactada por Vanesa Carolina Castro Bello – Secretaria del Grupo G14*
