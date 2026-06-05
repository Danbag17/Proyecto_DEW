# mapa-navegacion.md

## Mapa de navegación – nol2526

### Flujo del alumnado (Hito 1)

```
index.html
    └─→ /login
            ├─→ (rolalu) /alumno/asignaturas
            │               └─→ /alumno/detalle
            │               └─→ /alumno/expediente
            │               └─→ /logout → index.html
            │
            └─→ (rolpro) /profesor/asignaturas  [fase posterior]
```

---

### Descripción de cada vista

#### 1. `index.html` — Página inicial

Portada de entrada de la aplicación. Presenta el sistema, identifica al grupo G14 y ofrece acceso al flujo de autenticación.

#### 2. `/login` — Autenticación

Pantalla de login común para ambos roles. La autenticación se apoya en Tomcat (`tomcat-users.xml`) y redirige al flujo correspondiente según el rol del usuario autenticado.

- `rolalu` → `/alumno/asignaturas`
- `rolpro` → `/profesor/asignaturas` *(fase posterior)*

#### 3. `/alumno/asignaturas` — Lista de asignaturas

Vista principal del alumno autenticado. Muestra las asignaturas en las que está matriculado, obtenidas mediante consulta a CentroEducativo.

#### 4. `/alumno/detalle` — Detalle de asignatura

Muestra la información detallada de una asignatura concreta y la calificación obtenida por el alumno.

#### 5. `/alumno/expediente` — Expediente académico

Vista de resumen académico del alumno. Se implementará si el flujo del alumnado queda estable dentro del Hito 1. Incluirá opción de generar certificado de notas para impresión.

#### 6. `/logout` — Cierre de sesión

Invalida la sesión HTTP del usuario y redirige a `index.html`.

---

### Flujo del profesorado (fase posterior al Hito 1)

| Ruta | Descripción |
|---|---|
| `/profesor/asignaturas` | Lista de asignaturas impartidas por el profesor |
| `/profesor/alumnos` | Lista de alumnos de una asignatura (cargada con AJAX) |
| `/profesor/modificar-nota` | Modificación de la nota de un alumno (con AJAX) |

---

### Roles y acceso

| Rol | Rutas accesibles |
|---|---|
| `rolalu` | `/alumno/*`, `/logout` |
| `rolpro` | `/profesor/*`, `/logout` |

El control de acceso por rutas se configura de forma declarativa en `web.xml`.

---

### Observaciones

- La navegación AJAX del profesorado se abordará tras estabilizar el flujo del alumnado.
- El certificado de notas se generará desde la vista de expediente y está pensado para impresión directa desde el navegador.

---

# Evolución tras el Hito 1

## Navegación completa de la versión final

Durante el Hito 1 la navegación se centraba principalmente en el flujo del alumnado, dejando la parte del profesorado definida únicamente a nivel estructural.

Tras completar la implementación, la aplicación dispone de dos flujos funcionales completos.

---

## Flujo definitivo del alumnado

```text
index.html
    ↓
login.html
    ↓
Tomcat (FORM Authentication)
    ↓
AuthFilter
    ↓
/alumno/asignaturas
    ↓
/alumno/detalle
    ↓
/alumno/expediente
    ↓
/logout
    ↓
index.html
```

---

## Flujo definitivo del profesorado

```text
index.html
    ↓
login.html
    ↓
Tomcat (FORM Authentication)
    ↓
AuthFilter
    ↓
/profesor/asignaturas
    ↓
/profesor/alumnos
    ↓
Ficha de alumno
    ↓
Modificar nota (AJAX)
    ↓
Actualizar vista
    ↓
/logout
    ↓
index.html
```

---

## Navegación AJAX

Una diferencia importante respecto al diseño inicial es la incorporación de navegación dinámica mediante AJAX.

Muchas operaciones ya no provocan una recarga completa de la página.

Flujo típico:

```text
Usuario
   ↓
Página HTML
   ↓
JavaScript (fetch)
   ↓
Servlet
   ↓
JSON
   ↓
Actualización parcial de la vista
```

Esto mejora la experiencia de uso y reduce el número de recargas completas.

---

## Flujo de consulta de asignaturas del alumno

```text
Alumno
   ↓
AlumnoAsignaturasServlet
   ↓
CentroEducativoClient
   ↓
CentroEducativo
   ↓
JSON
   ↓
Vista de asignaturas
```

Desde esta pantalla el alumno puede acceder al detalle de cada asignatura o consultar el expediente completo.

---

## Flujo de expediente académico

```text
Alumno
   ↓
AlumnoExpedienteServlet
   ↓
CentroEducativoClient
   ↓
CentroEducativo
   ↓
Datos académicos
   ↓
Expediente
```

La vista final incluye:

* información académica;
* asignaturas;
* calificaciones;
* fotografía;
* nota media;
* formato de impresión.

---

## Flujo de consulta de alumnado por asignatura

```text
Profesor
   ↓
ProfesorAsignaturasServlet
   ↓
AsignaturaAlumnosServlet
   ↓
CentroEducativo
   ↓
JSON
   ↓
Tabla de alumnos
```

Esta información se carga dinámicamente sin abandonar la página principal.

---

## Flujo de modificación de nota

```text
Profesor
   ↓
Formulario AJAX
   ↓
ModificarNotaServlet
   ↓
CentroEducativoClient
   ↓
CentroEducativo
   ↓
Respuesta
   ↓
Actualización de la tabla
```

La modificación se realiza sin recargar la página.

---

## Flujo de autenticación completo

La navegación real incorpora dos mecanismos de autenticación.

```text
Usuario
   ↓
Tomcat
   ↓
Roles
   ↓
AuthFilter
   ↓
CentroEducativo
   ↓
Obtención de key
   ↓
SessionsUtils
   ↓
Aplicación NOL
```

Este proceso es transparente para el usuario.

---

## Gestión de errores durante la navegación

Durante la implementación final se añadieron rutas y comportamientos específicos para errores.

Casos contemplados:

```text
401 → Usuario no autenticado
403 → Sin permisos
404 → Recurso inexistente
500 → Error interno
```

Estas situaciones redirigen a páginas de error personalizadas en lugar de mostrar páginas genéricas de Tomcat.

---

## Navegación tras logout

La versión final refuerza el cierre de sesión.

Flujo:

```text
Usuario
   ↓
LogoutServlet
   ↓
Invalidación de sesión
   ↓
Limpieza de key
   ↓
Redirección
   ↓
index.html
```

Además se incorporan medidas para minimizar problemas derivados de la caché del navegador.

---

## Integración de fotografías

La navegación final incorpora fichas de alumno enriquecidas con imágenes.

La carga de la fotografía se realiza automáticamente a partir del DNI:

```text
/fotos/<DNI>.png
```

permitiendo mostrar información visual sin almacenar imágenes en CentroEducativo.

---

## Situación final

La versión final del proyecto dispone de una navegación completa para alumnado y profesorado. La interfaz combina navegación tradicional con operaciones AJAX, integrando autenticación, consulta académica, modificación de notas, generación de expediente y gestión de fotografías dentro de un flujo coherente y alineado con los requisitos del enunciado.

