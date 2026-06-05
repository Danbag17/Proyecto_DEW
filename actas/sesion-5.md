# Acta 05 – Integración técnica, revisión funcional y estabilización del Hito 1

**Asignatura:** Desarrollo Web (DEW) – Curso 2025/2026
**Grupo:** G14 – 3TI21
**Reunión nº:** 5
**Fecha:** 18/05/2026
**Hora:** 18:00 h
**Lugar:** Coordinación remota mediante WhatsApp, GitHub y Eclipse
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

## 2. Orden del día

* Revisión del estado técnico del proyecto.
* Integración de los componentes desarrollados por los distintos miembros del grupo.
* Revisión de la comunicación con CentroEducativo.
* Análisis de incidencias detectadas durante las pruebas.
* Revisión del poblado de datos y consultas REST.
* Coordinación de ramas e integración en GitHub.
* Revisión de configuración y seguridad.
* Organización del trabajo pendiente antes de la entrega.

## 3. Desarrollo de la reunión

### 3.1. Revisión general del estado del proyecto

Durante la sesión se realizó una revisión exhaustiva del estado actual del proyecto con el objetivo de evaluar el progreso alcanzado desde la sesión anterior y determinar el trabajo restante necesario para completar el Hito 1.

Se constató que el proyecto ya dispone de una estructura funcional claramente definida y que la mayor parte del trabajo organizativo realizado durante las sesiones anteriores ha permitido comenzar una fase centrada principalmente en integración, pruebas y resolución de incidencias.

En particular, se verificó la existencia de:

* estructura definitiva de paquetes Java;
* servlets principales del proyecto;
* cliente REST para CentroEducativo;
* utilidades de sesión;
* documentación técnica inicial;
* configuración de autenticación;
* estructura web y recursos estáticos;
* repositorio GitHub operativo.

Se concluye que el proyecto ha superado la fase de preparación y entra en una etapa donde la prioridad pasa a ser la consolidación funcional de todos los componentes desarrollados.

### 3.2. Integración entre autenticación y CentroEducativo

Uno de los principales temas tratados durante esta reunión fue la integración entre el sistema de autenticación web y la API REST de CentroEducativo.

Durante las pruebas realizadas se observó que la autenticación del usuario y la recuperación de información académica debían coordinarse correctamente para garantizar que las consultas posteriores pudieran realizarse utilizando la información obtenida durante el login.

Se revisó especialmente:

* obtención de la session key;
* almacenamiento de la clave en sesión;
* reutilización de la clave en peticiones posteriores;
* coordinación entre autenticación Tomcat y autenticación REST.

El grupo confirmó que la arquitectura prevista seguía siendo válida:

1. El usuario se autentica mediante Tomcat.
2. Se realiza la autenticación contra CentroEducativo.
3. Se obtiene una session key.
4. La clave se almacena en sesión HTTP.
5. Los servlets utilizan posteriormente dicha clave para realizar consultas autenticadas.

Se acordó continuar trabajando sobre esta integración hasta estabilizar completamente el flujo.

### 3.3. Problemas detectados en la recuperación de notas

Durante la revisión funcional se detectó una incidencia especialmente relevante relacionada con la visualización de calificaciones.

Al realizar determinadas consultas, algunas notas no aparecían correctamente reflejadas en las respuestas obtenidas desde CentroEducativo, pese a que aparentemente se habían realizado operaciones de inserción o modificación sobre los datos.

Ante esta situación, Carlos Moldes Peña inició una revisión más profunda del comportamiento de la API y de los formatos utilizados en las peticiones REST.

Se consideraron diversas posibles causas:

* errores en el formato JSON enviado;
* diferencias entre la documentación y el comportamiento real de la API;
* problemas de persistencia;
* datos incompletos en el entorno de pruebas.

Se acuerda continuar contrastando los resultados obtenidos con la documentación Swagger y realizar pruebas adicionales mediante herramientas externas cuando sea necesario.

### 3.4. Revisión del poblado de datos

Como consecuencia de las incidencias detectadas durante las consultas REST, el grupo dedicó parte de la sesión a revisar el estado del poblado de datos disponible en CentroEducativo.

Se observó que algunos comportamientos anómalos podían estar relacionados no con errores de programación, sino con la ausencia de determinadas entidades académicas necesarias para realizar pruebas completas.

Se concluyó que era necesario revisar cuidadosamente:

* alumnos existentes;
* profesores registrados;
* asignaturas disponibles;
* matrículas;
* calificaciones asociadas.

Asimismo, se acordó verificar el funcionamiento de los scripts de poblado utilizados por el grupo para asegurar que el entorno de pruebas reflejase correctamente los escenarios previstos.

### 3.5. Integración de componentes desarrollados

Durante esta sesión se avanzó también en la integración de los distintos componentes desarrollados por los miembros del equipo.

Se revisaron especialmente:

* servlets del alumnado;
* utilidades de sesión;
* filtros;
* cliente REST;
* configuración de autenticación.

Se comprobó que varios componentes ya podían comenzar a trabajar conjuntamente, aunque todavía quedaban ajustes necesarios para garantizar una integración completamente estable.

El grupo coincidió en que la prioridad ya no debía centrarse en crear nuevas clases o estructuras, sino en asegurar que los elementos existentes funcionasen correctamente como un sistema único.

### 3.6. Problemas de integración y conflictos Git

A medida que aumentó el número de componentes desarrollados simultáneamente comenzaron a aparecer conflictos derivados de la integración de ramas.

Los principales problemas detectados estuvieron relacionados con:

* modificaciones simultáneas sobre archivos comunes;
* diferencias entre versiones locales y remotas;
* conflictos durante los merges;
* cambios concurrentes sobre configuración.

Entre todos los archivos afectados destacó especialmente:

```text
web.xml
```

que se convirtió en uno de los puntos más sensibles del proyecto debido a que centraliza:

* servlets;
* filtros;
* autenticación;
* restricciones de seguridad;
* mapeos de URL.

Por este motivo se acordó extremar las precauciones al modificar dicho archivo.

### 3.7. Revisión de configuración y seguridad

Se revisó igualmente el estado de la configuración general del proyecto.

Durante esta revisión se confirmó que la seguridad debía continuar apoyándose principalmente en:

* autenticación gestionada por Tomcat;
* control de acceso mediante roles;
* gestión de sesiones;
* restricciones declaradas en `web.xml`.

El grupo considera que esta aproximación permite mantener una arquitectura más sencilla y alineada con los requisitos establecidos para el proyecto.

### 3.8. Estado del Hito 1

Como cierre de la sesión se realizó una valoración general del progreso alcanzado.

Se considera que el proyecto ya dispone de una base sólida y suficientemente avanzada para afrontar la fase final del Hito 1.

No obstante, se identifican todavía varias áreas prioritarias:

* estabilizar autenticación;
* completar consultas REST;
* corregir incidencias relacionadas con notas;
* finalizar configuración de seguridad;
* completar pruebas funcionales;
* mantener actualizada la documentación.

Se acuerda que todas las tareas futuras deberán orientarse principalmente a consolidar el funcionamiento global del sistema.

## 4. Acuerdos adoptados

* Continuar verificando el comportamiento de la API REST de CentroEducativo.
* Revisar el tratamiento de las calificaciones obtenidas mediante consultas REST.
* Completar la revisión de los scripts de poblado.
* Priorizar la integración funcional frente al desarrollo de nuevas funcionalidades.
* Mantener la autenticación basada en Tomcat.
* Consolidar el uso de la session key almacenada en sesión HTTP.
* Extremar las precauciones al modificar `web.xml`.
* Mantener la documentación sincronizada con el estado real del proyecto.
* Continuar realizando pruebas de integración entre componentes.

## 5. Próximos pasos

* completar integración REST;
* estabilizar autenticación;
* revisar recuperación de notas;
* verificar poblado de datos;
* resolver conflictos de integración;
* finalizar configuración de seguridad;
* ampliar pruebas funcionales;
* continuar documentación técnica;
* preparar la integración final previa a la entrega.

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

