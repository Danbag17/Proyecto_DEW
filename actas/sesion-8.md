# Acta 08 – Consolidación funcional, documentación final y preparación de la entrega definitiva

**Asignatura:** Desarrollo Web (DEW) – Curso 2025/2026
**Grupo:** G14 – 3TI21
**Reunión nº:** 8
**Fecha:** 02/06/2026 y 05/06/2026
**Hora:** Coordinación continua mediante WhatsApp, GitHub y Eclipse
**Lugar:** Trabajo remoto colaborativo
**Secretaria:** Vanesa Carolina Castro Bello

## 1. Participantes

| Nombre y apellidos           | Grupo |
| ---------------------------- | ----- |
| Vanesa Carolina Castro Bello | 3TI21 |
| Mikel Escudero Aramburu      | 3TI21 |
| Carlos Moldes Peña           | 3TI21 |
| Pau Oroval González          | 3TI21 |
| Michal Pojnar                | 3TI21 |
| Daniel Zanon Barney          | 3TI21 |

---

## 2. Orden del día

* Revisión del estado global del proyecto.
* Documentación de las últimas intervenciones realizadas sobre el código.
* Validación de requisitos pendientes del enunciado.
* Corrección de la gestión de créditos académicos.
* Revisión del sistema de autenticación y sesiones.
* Integración de fotografías y mejoras visuales.
* Preparación de una versión funcional estable para la entrega final.

---

## 3. Desarrollo de la reunión

### 3.1. Revisión del estado general del proyecto

Al inicio de la sesión se realizó una revisión completa del estado de la aplicación.

Se confirmó que la aplicación disponía ya de:

* flujo completo de alumnado;
* flujo funcional de profesorado;
* autenticación FORM mediante Tomcat;
* integración REST con CentroEducativo;
* sistema de logs operativo;
* gestión de sesiones;
* cliente REST centralizado;
* documentación técnica avanzada.

Durante esta revisión se identificaron todavía varios aspectos pendientes de consolidar antes de considerar cerrada la versión final del proyecto.

Entre ellos:

* visualización correcta de créditos académicos;
* robustez del proceso de login tras reinicios;
* integración definitiva de fotografías;
* unificación del rediseño visual v2;
* documentación de las últimas tandas de cambios.

---

### 3.2. Consolidación de la documentación técnica

Se revisó y amplió la documentación interna generada durante las últimas semanas.

La documentación quedó organizada en varias tandas temáticas:

#### Tanda 5 – Login, logout, caché y usuarios

Se documentaron:

* autenticación mediante Tomcat;
* cierre de sesión;
* control de caché;
* configuración de usuarios;
* relación entre Tomcat y CentroEducativo.

#### Tanda 6 – Fotografías, nota media, fichas y logs

Se documentaron:

* integración de fotografías;
* cálculo de nota media;
* ficha individual del profesorado;
* ficha de grupo;
* activación y desactivación de logs mediante configuración.

#### Tanda 7 – Créditos, rediseño visual y robustez

Se documentaron:

* corrección de créditos académicos;
* rediseño visual v2;
* refuerzo del login;
* páginas de error;
* mejoras de estabilidad.

Además, se elaboraron documentos de apoyo para la defensa del proyecto relacionados con:

* JavaScript y AJAX;
* arquitectura del servidor;
* integración entre frontend y backend.

---

### 3.3. Corrección de la gestión de créditos académicos

Durante las pruebas funcionales se detectó que los créditos de las asignaturas aparecían vacíos tanto en la vista de asignaturas del alumnado como en el expediente académico.

Tras analizar las respuestas de CentroEducativo se comprobó que:

```text
/alumnos/{dni}/asignaturas
```

únicamente devuelve:

* acrónimo;
* nota.

Mientras que los datos relativos a:

* créditos;
* nombre;
* curso;
* cuatrimestre;

se encuentran en:

```text
/asignaturas
```

Para resolver esta situación se desarrolló la utilidad:

```text
dew.util.AsignaturasUtils
```

Esta utilidad realiza automáticamente el cruce entre matrícula y catálogo utilizando el acrónimo como identificador común.

La solución fue integrada en:

* AlumnoAsignaturasServlet
* AlumnoExpedienteServlet

permitiendo mostrar correctamente la información académica enriquecida.

Asimismo, se implementó un mecanismo de degradación controlada para evitar errores en caso de que el catálogo no estuviera disponible.

---

### 3.4. Mejora del sistema de autenticación y sesiones

Durante las pruebas se detectaron problemas esporádicos relacionados con sesiones antiguas almacenadas por el navegador tras reiniciar Tomcat o CentroEducativo.

En determinadas circunstancias aparecían errores derivados de sesiones previas que ya no eran válidas.

Para corregir esta situación se desarrolló:

```text
dew.filters.LoginResetFilter
```

Este filtro fuerza la invalidación de sesiones anteriores y garantiza que el proceso de autenticación comience desde un estado limpio.

Asimismo se incorporó configuración adicional en:

```text
META-INF/context.xml
```

con el objetivo de mejorar el comportamiento del sistema FORM Authentication de Tomcat y evitar errores observados durante las pruebas.

Por último se reforzó también:

```text
LogoutServlet
```

añadiendo:

* invalidación explícita de sesión;
* limpieza de cookies;
* control de caché;
* eliminación de información residual.

Las pruebas realizadas posteriormente mostraron una mejora significativa en la estabilidad del sistema.

---

### 3.5. Integración de páginas de error

Durante esta fase se completó la incorporación de páginas de error personalizadas.

Se añadieron configuraciones específicas para:

* Error 401 (No autenticado)
* Error 403 (Acceso denegado)
* Error 404 (Recurso inexistente)
* Error 500 (Error interno)

Estas páginas quedaron registradas en:

```text
WEB-INF/web.xml
```

con el objetivo de mejorar la experiencia de usuario y facilitar la detección de incidencias.

---

### 3.6. Revisión del rediseño visual v2

Se revisó el nuevo conjunto de estilos y páginas incorporados durante las últimas semanas.

Entre los elementos integrados destacan:

```text
css/nol-v2.css
```

y las nuevas vistas:

```text
*-v2.html
```

Durante las pruebas se verificó:

* correcta integración con Bootstrap;
* compatibilidad con los servlets existentes;
* funcionamiento del flujo de alumnado;
* consistencia visual general.

Se acordó mantener temporalmente las páginas antiguas como mecanismo de respaldo mientras se completa la unificación definitiva.

---

### 3.7. Integración de fotografías

Se completó la integración de fotografías para alumnado y profesorado.

La solución adoptada consiste en asociar automáticamente las imágenes mediante el DNI del usuario.

Formato utilizado:

```text
fotos/<DNI>.png
```

Ejemplos:

```text
fotos/12345678A.png
fotos/22222222P.png
```

Las pruebas realizadas confirmaron la correcta carga de imágenes desde las distintas vistas de la aplicación.

---

### 3.8. Valoración final de la versión funcional

Como cierre de la sesión se realizó una evaluación global del proyecto.

El grupo concluyó que:

* los requisitos principales del enunciado se encuentran implementados;
* la integración con CentroEducativo es estable;
* la autenticación funciona correctamente;
* la documentación se encuentra prácticamente completada;
* la base funcional es suficientemente sólida para continuar con la preparación de la entrega final.

Las tareas restantes se centran principalmente en pruebas, revisión documental y pequeños ajustes de presentación.

---

## 4. Acuerdos adoptados

1. Dar por cerrado el requisito relacionado con la gestión de créditos académicos.
2. Centralizar la lógica de enriquecimiento de asignaturas en `AsignaturasUtils`.
3. Mantener `LoginResetFilter` como mecanismo de limpieza de sesiones.
4. Incorporar definitivamente las páginas de error personalizadas.
5. Mantener el rediseño visual v2 como base de la versión final.
6. Integrar las fotografías mediante el criterio `fotos/<DNI>.png`.
7. Preparar una versión estable para integración definitiva en la rama principal.

---

## 5. Próximos pasos

* completar pruebas funcionales;
* revisar navegación completa alumno/profesor;
* validar el sistema de fotografías;
* finalizar documentación técnica;
* revisar el script de población;
* unificar páginas antiguas y v2;
* preparar la entrega final.

---

## 6. Validación del acta

El acta ha sido revisada y aceptada por los integrantes participantes en la coordinación.

| Nombre                       | Firma | Fecha |
| ---------------------------- | ----- | ----- |
| Vanesa Carolina Castro Bello |       |       |
| Mikel Escudero Aramburu      |       |       |
| Carlos Moldes Peña           |       |       |
| Pau Oroval González          |       |       |
| Michal Pojnar                |       |       |
| Daniel Zanon Barney          |       |       |

**Acta redactada por Vanesa Carolina Castro Bello – Secretaria del Grupo G14**
