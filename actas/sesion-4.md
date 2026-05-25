# Acta 04 – Seguimiento técnico, integración funcional y coordinación final del Hito 1

**Asignatura:** Desarrollo Web (DEW) – Curso 2025/2026
**Grupo:** G14 – 3TI21
**Reunión nº:** 4
**Fecha:** 14/05/2026
**Hora:** 19:00 h
**Lugar:** Coordinación remota mediante WhatsApp y GitHub
**Secretaria:** Vanesa Carolina Castro Bello

---

# 1. Participantes

| Nombre y apellidos           | Grupo |
| ---------------------------- | ----- |
| Vanesa Carolina Castro Bello | 3TI21 |
| Mikel Escudero Aramburu      | 3TI21 |
| Carlos Moldes Peña           | 3TI21 |
| Pau Oroval González          | 3TI21 |
| Michal Pojnar                | 3TI21 |
| Daniel Zanon Barney          | 3TI21 |

---

# 2. Orden del día

1. Seguimiento del estado funcional del Hito 1
2. Integración entre autenticación y CentroEducativo
3. Gestión de la `session key`
4. Coordinación de ramas y sincronización con GitHub
5. Revisión del alcance real del Hito 1
6. Avance de documentación y utilidades comunes
7. Problemas técnicos detectados
8. Próximos pasos antes de la entrega

---

# 3. Desarrollo de la reunión

## 3.1. Estado funcional del Hito 1

Durante la coordinación mantenida mediante WhatsApp y GitHub se revisó el estado real del Hito 1, concluyéndose que el proyecto dispone ya de una estructura suficientemente consolidada y que el trabajo pendiente se concentra principalmente en la integración funcional.

Se confirmó que:

* la estructura base del proyecto ya está preparada
* los servlets principales ya existen
* la documentación `.md` se encuentra bastante avanzada
* el repositorio GitHub ya se utiliza de forma estable
* el trabajo mediante ramas individuales está funcionando correctamente

Asimismo, se concluye que el principal trabajo pendiente se concentra ya en:

* autenticación
* integración REST
* gestión de sesiones
* configuración de seguridad
* pruebas funcionales finales

---

## 3.2. Integración con CentroEducativo y session key

Uno de los principales temas tratados durante esta coordinación fue la integración entre la aplicación web y CentroEducativo.

Carlos y Michal trabajaron especialmente sobre:

* la petición de login REST
* la obtención de la `session key`
* las consultas autenticadas posteriores

Durante este proceso surgieron dudas técnicas relativas a:

* cómo reutilizar la `key`
* cómo almacenarla correctamente
* cómo coordinarla con la sesión HTTP del usuario
* cómo diferenciar la autenticación de Tomcat de la autenticación del nivel de datos

Se acuerda finalmente que:

* la autenticación web seguirá apoyándose en Tomcat
* CentroEducativo devolverá una `session key`
* dicha `key` deberá almacenarse en sesión HTTP
* los servlets utilizarán posteriormente dicha `key` para realizar consultas autenticadas

Se considera que esta parte constituye uno de los bloques técnicos más complejos del Hito 1.

---

## 3.3. Gestión de sesión y `SessionsUtils.java`

Durante esta fase se consolida también el uso de la utilidad común:

```text id="e0nm6i"
SessionsUtils.java
```

Desarrollada por Vanesa Carolina Castro Bello, esta utilidad se utilizará para:

* crear sesiones
* almacenar atributos comunes
* recuperar atributos de sesión
* invalidar sesiones durante el logout

Como base común se mantienen los siguientes atributos:

| Atributo   | Descripción                    |
| ---------- | ------------------------------ |
| `dni`      | Identificador del usuario      |
| `password` | Contraseña si fuera necesaria  |
| `key`      | Session key de CentroEducativo |

Se acuerda que los servlets deberán utilizar esta utilidad preferentemente en lugar de gestionar directamente los atributos de sesión.

---

## 3.4. Coordinación con GitHub y ramas

Durante esta fase se detectó la necesidad de mejorar la coordinación entre ramas debido a que algunos integrantes estaban modificando archivos ya actualizados remotamente.

Por ello, se acuerda recordar como flujo básico de trabajo:

1. realizar `pull` antes de comenzar modificaciones
2. trabajar desde la rama individual correspondiente
3. realizar `commit`
4. realizar `push`
5. integrar posteriormente en `master`

Asimismo, se detectó nuevamente que Git no incorpora carpetas vacías si estas no contienen archivos.

---

## 3.5. Incidencia con subida de cambios al repositorio

Durante esta fase se detectó también una incidencia relacionada con la subida de cambios al repositorio por parte de Pau Oroval González.

Debido a problemas de conexión e integración con el repositorio GitHub, no fue posible realizar correctamente el `push` de su parte del trabajo dentro del tiempo disponible para la preparación del Hito 1.

Como solución provisional y para evitar bloquear la integración del proyecto, los archivos correspondientes a su trabajo fueron compartidos mediante un archivo comprimido (`zip`) a través de WhatsApp y posteriormente incorporados y subidos al repositorio por otro integrante del grupo.

Se acuerda que esta solución se adopta únicamente como medida temporal motivada por las limitaciones de tiempo previas a la entrega.

Asimismo, queda pendiente revisar posteriormente la configuración de conexión de Pau con el repositorio para normalizar nuevamente el flujo habitual de trabajo mediante ramas y GitHub.

---

## 3.6. Dependencias y librerías

Se revisó nuevamente el uso de librerías externas y se confirmó como criterio del grupo:

* mantener el proyecto lo más ligero posible
* utilizar Bootstrap por CDN
* incorporar Gson únicamente si resulta realmente necesario para el tratamiento de JSON
* evitar dependencias innecesarias

---

## 3.7. Revisión del alcance real del Hito 1

Durante la coordinación se revisó el alcance real exigido para el Hito 1, especialmente en relación con:

* `/alumno/detalle`
* `/alumno/expediente`

Se concluye que el objetivo principal del Hito 1 es disponer de un:

> prototipo funcional de la secuencia de navegación del alumnado

y no necesariamente una implementación académica completa y definitiva.

En consecuencia, el grupo considera prioritario garantizar:

* autenticación funcional
* obtención de `session key`
* navegación del alumnado
* al menos una consulta real a CentroEducativo
* integración entre servlets y cliente REST
* funcionamiento del filtro de logs
* configuración correcta de seguridad

Se acuerda además que:

* `/alumno/detalle` podrá implementarse inicialmente de forma simplificada
* `/alumno/expediente` podrá quedar parcial o preparado estructuralmente si el flujo principal ya funciona correctamente

---

## 3.8. Documentación técnica

Vanesa Carolina Castro Bello continúa coordinando la documentación técnica del proyecto, incluyendo:

* actas
* memoria
* decisiones iniciales
* roles y permisos
* logs
* mapa de navegación
* ejecución
* integración con CentroEducativo

Asimismo, se recuerda la necesidad de completar progresivamente los Javadocs de:

* servlets
* filtros
* cliente REST
* utilidades

---

## 3.9. Problemas y riesgos detectados

Durante esta fase se identifican los siguientes riesgos principales:

* integración incompleta entre Tomcat y CentroEducativo
* posibles conflictos entre ramas
* retrasos en la integración final
* complejidad de la gestión de `session key`
* dificultad de cerrar completamente algunas operaciones REST antes de la entrega

Se considera especialmente importante estabilizar cuanto antes:

* autenticación
* navegación del alumnado
* consultas GET
* filtro de logs

---

# 4. Acuerdos adoptados

1. Se mantiene GitHub como repositorio oficial del grupo.
2. Se continuará trabajando mediante ramas individuales.
3. Se acuerda realizar `pull` antes de comenzar modificaciones.
4. La autenticación web seguirá apoyándose en Tomcat.
5. CentroEducativo proporcionará una `session key` reutilizable.
6. La `session key` se almacenará en sesión HTTP.
7. `SessionsUtils.java` se consolida como utilidad común de gestión de sesión.
8. El objetivo prioritario del Hito 1 será un prototipo funcional del flujo del alumnado.
9. `/alumno/detalle` podrá implementarse inicialmente de forma simplificada.
10. `/alumno/expediente` podrá quedar parcial si el flujo principal queda estable.
11. Se prioriza la estabilidad funcional frente a funcionalidades secundarias.
12. La documentación técnica continuará centralizándose en `docs/`.
13. La incidencia de subida de cambios relacionada con Pau Oroval González se resolverá posteriormente, manteniéndose temporalmente la integración manual mediante archivos comprimidos.

---

# 5. Próximos pasos

* completar autenticación
* terminar integración REST
* estabilizar `session key`
* finalizar `web.xml`
* completar `LogsFilter`
* integrar consultas reales
* completar HTML y navegación
* finalizar documentación
* preparar integración final en `master`

---

# 6. Validación del acta

El acta ha sido revisada y aceptada por los integrantes participantes en la coordinación.

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
