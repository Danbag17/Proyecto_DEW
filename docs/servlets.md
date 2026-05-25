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
