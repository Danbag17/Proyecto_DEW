
---

```markdown
# mapa-navegacion.md

## Mapa de navegación del Hito 1

### Flujo principal previsto
- `index.html`
- `/login`
- `/alumno/asignaturas`
- `/alumno/detalle`
- `/alumno/expediente`
- `/logout`

## Descripción del flujo

### 1. Página inicial
La aplicación arranca en `index.html`, que actúa como portada de entrada del proyecto. En esta vista se presenta la aplicación, el grupo y una entrada al flujo de autenticación.

### 2. Login
La autenticación se realizará a través de la ruta `/login`, apoyándose en la configuración del servidor y en la gestión de sesión del proyecto.

### 3. Vista de asignaturas del alumno
Una vez autenticado, el alumno accede a `/alumno/asignaturas`, donde se muestran las asignaturas disponibles para su perfil.

### 4. Vista de detalle
Desde la lista de asignaturas, el alumno puede acceder a `/alumno/detalle`, donde se mostrará el detalle o calificación correspondiente.

### 5. Vista de expediente
Si se completa dentro del alcance del Hito 1, el alumno podrá consultar también `/alumno/expediente`.

### 6. Logout
La sesión podrá cerrarse mediante la ruta `/logout`.

## Observaciones
La parte del profesorado puede quedar preparada estructuralmente, pero no constituye la prioridad principal del Hito 1.
