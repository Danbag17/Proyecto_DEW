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
