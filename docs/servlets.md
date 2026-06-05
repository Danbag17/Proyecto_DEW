# servlets.md

## Documentación de servlets

### Paquete: `dew.servlets`

Todos los servlets extienden `HttpServlet` y se configuran exclusivamente mediante `web.xml`, sin anotaciones.

---

### LoginRedirectServlet

**Ruta:** `/login`

**Función:** Gestionar la redirección del usuario tras la autenticación con Tomcat. Realiza el login REST contra CentroEducativo, obtiene la `key` de sesión y redirige al flujo correspondiente según el rol autenticado.

| Método | Descripción |
|---|---|
| `doGet()` | Redirige al recurso protegido correspondiente al rol del usuario |
| `doPost()` | Recibe credenciales, llama a CentroEducativo, crea sesión y redirige |

**Dependencias:** `SessionsUtils`, `CentroEducativoClient`

---

### AlumnoAsignaturasServlet

**Ruta:** `/alumno/asignaturas`

**Rol requerido:** `rolalu`

**Función:** Mostrar la lista de asignaturas en las que está matriculado el alumno autenticado, consultando CentroEducativo con la `key` de sesión.

| Método | Descripción |
|---|---|
| `doGet()` | Valida sesión, recupera `dni` y `key`, consulta REST y genera HTML con la lista |

**Dependencias:** `SessionsUtils`, `CentroEducativoClient`

---

### AlumnoDetalleServlet

**Ruta:** `/alumno/detalle`

**Rol requerido:** `rolalu`

**Función:** Mostrar el detalle y la calificación de una asignatura concreta del alumno autenticado.

| Parámetro | Descripción |
|---|---|
| `codigo` | Código de la asignatura a consultar |

| Método | Descripción |
|---|---|
| `doGet()` | Recupera `key` de sesión, consulta REST el detalle de la asignatura y genera HTML |

**Dependencias:** `SessionsUtils`, `CentroEducativoClient`

---

### AlumnoExpedienteServlet

**Ruta:** `/alumno/expediente`

**Rol requerido:** `rolalu`

**Función:** Mostrar el expediente académico completo del alumno autenticado. Incluye opción de generar certificado de notas para impresión.

**Estado:** Parcialmente implementado en el Hito 1.

| Método | Descripción |
|---|---|
| `doGet()` | Consulta asignaturas y notas del alumno y genera vista de expediente |

**Dependencias:** `SessionsUtils`, `CentroEducativoClient`

---

### LogoutServlet

**Ruta:** `/logout`

**Función:** Cerrar la sesión HTTP del usuario y redirigir a la página inicial.

| Método | Descripción |
|---|---|
| `doGet()` | Invalida la sesión mediante `SessionsUtils` y redirige a `index.html` |

**Dependencias:** `SessionsUtils`

---

### ProfesorAsignaturasServlet

**Ruta:** `/profesor/asignaturas`

**Rol requerido:** `rolpro`

**Función:** Mostrar la lista de asignaturas impartidas por el profesor autenticado.

**Estado:** Preparado estructuralmente. Implementación completa prevista para fases posteriores al Hito 1.

| Método | Descripción |
|---|---|
| `doGet()` | Consulta asignaturas del profesor y genera HTML con la lista |

---

### AsignaturaAlumnos

**Ruta:** `/profesor/alumnos`

**Rol requerido:** `rolpro`

**Función:** Consultar y mostrar el alumnado asociado a una asignatura concreta. La carga de datos se realiza mediante AJAX.

| Parámetro | Descripción |
|---|---|
| `codigo` | Código de la asignatura |

| Método | Descripción |
|---|---|
| `doGet()` | Devuelve la lista de alumnos de la asignatura en formato JSON (AJAX) |

---

### ModificarNota

**Ruta:** `/profesor/modificar-nota`

**Rol requerido:** `rolpro`

**Función:** Actualizar la nota de un alumno en una asignatura concreta. La operación se realiza mediante AJAX sin recarga de página.

| Parámetro | Descripción |
|---|---|
| `dni` | DNI del alumno |
| `codigo` | Código de la asignatura |
| `nota` | Nueva calificación |

| Método | Descripción |
|---|---|
| `doPost()` | Valida rol, actualiza la nota via REST en CentroEducativo y devuelve confirmación |

**Dependencias:** `SessionsUtils`, `CentroEducativoClient`

---

### AsignaturasServlet

**Función:** Servlet auxiliar de soporte para la navegación académica general.

**Estado:** Pendiente de definición completa.


---

# Evolución tras el Hito 1

## Evolución general del backend

Durante el Hito 1 la documentación de servlets describía principalmente la estructura prevista de la aplicación.

Tras la finalización del proyecto se completó la implementación del backend, consolidando una arquitectura basada en:

```text
Servlet
    ↓
SessionsUtils
    ↓
CentroEducativoClient
    ↓
CentroEducativo REST
```

Los servlets dejaron de generar páginas completas y pasaron a utilizarse principalmente como endpoints de consulta y operación para el frontend basado en AJAX.

---

## Integración con AuthFilter

Una de las modificaciones más importantes realizadas tras el Hito 1 fue la incorporación de:

```text
dew.filters.AuthFilter
```

Gracias a este filtro los servlets ya no necesitan realizar el login REST contra CentroEducativo.

Cuando una petición llega a un servlet:

```text
Usuario
   ↓
Tomcat
   ↓
AuthFilter
   ↓
SessionsUtils
   ↓
Servlet
```

el servlet recibe una sesión ya preparada con:

* dni;
* password;
* key;
* rol.

Esto simplifica considerablemente la lógica interna.

---

## Evolución de LoginRedirectServlet

Inicialmente se planteó como servlet responsable del login.

Durante la implementación final su papel quedó más orientado a:

* redirección según rol;
* navegación inicial;
* coordinación con la autenticación declarativa de Tomcat.

La autenticación real pasó a depender principalmente de:

```text
j_security_check
```

y de:

```text
AuthFilter
```

---

## Evolución de AlumnoAsignaturasServlet

La implementación final permite:

* recuperar las asignaturas matriculadas;
* devolver información preparada para el frontend;
* reutilizar la key almacenada en sesión.

Flujo:

```text
AlumnoAsignaturasServlet
          ↓
SessionsUtils
          ↓
CentroEducativoClient
          ↓
CentroEducativo
          ↓
JSON
```

---

## Evolución de AlumnoDetalleServlet

Además de mostrar la nota obtenida, la versión final cruza información procedente de varias consultas.

Esto permite mostrar:

* nombre de asignatura;
* acrónimo;
* créditos;
* calificación;
* información adicional necesaria para la vista.

La respuesta enviada al frontend está adaptada para facilitar la construcción de la interfaz.

---

## Evolución de AlumnoExpedienteServlet

Durante el Hito 1 figuraba como funcionalidad parcialmente implementada.

La versión final incorpora:

* datos académicos completos;
* expediente preparado para impresión;
* cálculo de nota media;
* integración de fotografía;
* enriquecimiento mediante información adicional de asignaturas.

Esta vista constituye una de las funcionalidades más completas del flujo de alumnado.

---

## Evolución de ProfesorAsignaturasServlet

La funcionalidad prevista inicialmente quedó implementada completamente.

Permite:

* identificar al profesor autenticado;
* recuperar asignaturas impartidas;
* devolver datos para construcción dinámica de la vista.

La carga de información se realiza utilizando AJAX.

---

## Evolución de AsignaturaAlumnosServlet

Esta funcionalidad pasó a convertirse en uno de los puntos centrales de la zona de profesorado.

Responsabilidades:

* recuperar alumnado matriculado;
* devolver JSON;
* facilitar carga dinámica mediante AJAX;
* permitir acceso a la ficha individual de cada alumno.

Flujo:

```text
Profesor
    ↓
AJAX
    ↓
AsignaturaAlumnosServlet
    ↓
CentroEducativoClient
    ↓
JSON
```

---

## Evolución de ModificarNotaServlet

Inicialmente se encontraba únicamente previsto.

La implementación final permite:

* validar sesión;
* validar rol;
* recibir la nueva nota;
* enviar la actualización a CentroEducativo;
* devolver confirmación al frontend.

La operación se realiza sin recargar la página.

Flujo completo:

```text
Profesor
   ↓
Formulario AJAX
   ↓
ModificarNotaServlet
   ↓
CentroEducativoClient.modificarNota()
   ↓
CentroEducativo
   ↓
Respuesta
   ↓
Actualización visual
```

---

## Servlets y AJAX

La versión final adopta una filosofía clara:

```text
HTML
   ↓
JavaScript
   ↓
Fetch
   ↓
Servlet
   ↓
JSON
```

Los servlets dejan de ser generadores de páginas completas y pasan a funcionar como capa de servicios para el frontend.

Ventajas:

* menos recargas;
* interfaz más fluida;
* separación entre presentación y lógica.

---

## Manejo de errores

Durante la implementación se incorporó tratamiento de errores en varias capas.

Situaciones contempladas:

* sesión inexistente;
* key inválida;
* parámetros incorrectos;
* errores REST;
* recursos inexistentes;
* errores internos.

Los servlets devuelven códigos HTTP coherentes para facilitar el trabajo del frontend.

---

## Protección por roles

Todos los servlets se encuentran protegidos mediante:

```text
web.xml
```

Las rutas:

```text
/alumno/*
```

requieren:

```text
rolalu
```

Las rutas:

```text
/profesor/*
```

requieren:

```text
rolpro
```

Además, algunos servlets realizan validaciones adicionales antes de ejecutar operaciones sensibles.

---

## Relación con CentroEducativoClient

La implementación final evita que los servlets construyan directamente peticiones HTTP.

Toda la comunicación queda centralizada en:

```text
dew.client.CentroEducativoClient
```

Ventajas:

* menos duplicación;
* mantenimiento más sencillo;
* tratamiento uniforme de errores;
* desacoplamiento entre lógica web y REST.

---

## Situación final

La versión final del proyecto dispone de un backend completo basado en servlets, filtros y AJAX. Los servlets actúan como intermediarios entre el frontend y CentroEducativo, reutilizando la sesión gestionada por AuthFilter y SessionsUtils y delegando toda la comunicación REST en CentroEducativoClient.

