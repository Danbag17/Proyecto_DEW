# centroeducativo-operaciones.md

## Integración con CentroEducativo

### Objetivo

La aplicación no se conecta directamente a una base de datos propia, sino que se integra con el nivel de datos proporcionado mediante servicios REST expuestos por CentroEducativo.

La documentación de la API está disponible en:

```
http://localhost:9090/CentroEducativo/swagger-ui.html
```

---

### Cliente Java previsto

Todas las llamadas REST se centralizarán en la clase:

- `CentroEducativoClient.java` — paquete `dew.client`

---

### Operaciones mínimas previstas para el Hito 1

#### 1. Autenticación

- Login contra CentroEducativo enviando `dni` y `password`
- Obtención de la `key` de sesión devuelta por la API
- Almacenamiento de `dni`, `password` y `key` en la sesión HTTP mediante `SessionsUtils`

#### 2. Consulta de asignaturas del alumno

- Obtención de las asignaturas en las que está matriculado el alumno autenticado
- Parámetros necesarios: `dni`, `key`

#### 3. Consulta de detalle o nota de una asignatura

- Obtención del detalle o calificación correspondiente a una asignatura concreta
- Parámetros necesarios: `dni`, `key`, identificador de asignatura

#### 4. Consulta de expediente

- Recuperación de la información necesaria para construir la vista de expediente del alumno
- Se implementará si el flujo del alumnado queda estable dentro del Hito 1

---

### Operaciones previstas para fases posteriores

Las siguientes operaciones quedan previstas para la parte del profesorado, que se abordará tras el Hito 1:

- Consulta de asignaturas del profesor
- Consulta de alumnos por asignatura
- Modificación de la nota de un alumno en una asignatura

---

### Operaciones auxiliares

- Pruebas manuales de las llamadas REST mediante `curl`
- Script de poblado de CentroEducativo con datos de prueba — **ya completado**

---

### Observación sobre el Hito 1

En el Hito 1 se priorizan las operaciones de consulta del alumnado. Las operaciones de inserción, actualización o borrado pueden quedar iniciadas o preparadas estructuralmente, pero no constituyen el núcleo prioritario de esta entrega.


