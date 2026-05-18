# Acta 02 – Puesta en marcha técnica del proyecto y planificación del Hito 1

**Asignatura:** Desarrollo Web (DEW) – Curso 2025/2026  
**Grupo:** G14 – 3TI21  
**Reunión nº:** 2  
**Fecha:** 04/05/2026  
**Hora:** 17:00 h  
**Lugar:** Laboratorio DSIC 3, ETSINF (UPV)  
**Secretario:** Vanesa Castro Bello

---

## 1. Asistentes

| Nombre y apellidos | DNI/NIE | Correo UPV | Grupo |
|---|---|---|---|
| Vanesa Carolina Castro Bello | 73281209 | vccasbel@etsinf.upv.es | 3TI21 |
| Mikel Escudero Aramburu | 49467180 | mescara@etsinf.upv.es | 3TI21 |
| Carlos Moldes Peña | 35589876 | cmolpea@etsinf.upv.es | 3TI21 |
| Pau Oroval González | 26626414 | porogon@etsinf.upv.es | 3TI21 |
| Michal Pojnar | PDFH473676 | mpojnar@etsinf.upv.es | 3TI21 |
| Daniel Zanon Barney | 45914067 | dzanbar@etsinf.upv.es | 3TI21 |

---

## 3. Desarrollo de la reunión

## 2. Orden del día

1. Revisión de acuerdos pendientes de la sesión 1
2. Conexión del proyecto con GitHub
3. Creación/importación del proyecto base en Eclipse
4. Configuración del entorno de ejecución
5. Definición cerrada del alcance del Hito 1
6. Revisión del reparto de tareas
7. Documentación técnica y criterios de Javadoc
8. Ruegos y preguntas

---

### 3.1. Revisión de pendientes de la sesión 1

Se revisaron los acuerdos de la sesión 1, detectándose los siguientes asuntos pendientes:

- Puesta en marcha efectiva del repositorio compartido
- Creación o importación del proyecto común en Eclipse
- Definición más concreta del alcance del Hito 1
- Transformación del reparto inicial de responsabilidades en tareas ejecutables

Se acuerda que la sesión 2 debe servir para pasar de una fase organizativa a una fase operativa, dejando establecida la base técnica y documental del proyecto.

---

### 3.2. Conexión del proyecto con GitHub

Se acuerda utilizar de forma efectiva el repositorio GitHub decidido en la sesión 1:

> **Repositorio del grupo:** https://github.com/Danbag17/DEW

Se decide que **una sola persona** realizará la vinculación inicial del proyecto con GitHub desde Eclipse, mientras que el resto clonará el repositorio en sus entornos locales, con el fin de evitar historiales duplicados y commits iniciales incompatibles.

El repositorio se utilizará para almacenar:

- Código fuente del proyecto
- Actas de reuniones
- Documentación técnica
- Scripts de inicialización y pruebas
- Material de apoyo y configuración

#### 3.2.1. Incidencias y acuerdos relativos a GitHub

Se produjo una incidencia al realizar el primer `push`, debido a un conflicto con el estado remoto del repositorio. Se resolvió eliminando el push conflictivo y repitiendo la subida del contenido a la rama principal.

Se adoptan los siguientes acuerdos de trabajo con el repositorio:

- La rama principal pasa a denominarse **`master`** (en lugar de `main`), para unificar el flujo de trabajo y evitar confusiones entre configuraciones locales y remotas.
- Cada integrante trabajará en una **rama propia**.
- Los cambios no se realizarán directamente sobre `master` salvo en casos justificados.
- La integración en `master` se hará una vez revisados los cambios y comprobada su compatibilidad con el estado actual del proyecto.

---

### 3.3. Proyecto base en Eclipse

Se acuerda utilizar como punto de partida el proyecto de la sesión introductoria basado en `ServletPrueba`, por los siguientes motivos:

- Ya parte de un **Dynamic Web Project** funcional
- Permite aprovechar la estructura de carpetas del proyecto web
- Facilita la transición hacia el proyecto `nol2526`
- Permite reutilizar la configuración de Tomcat y del entorno de ejecución

Dicha base será adaptada progresivamente, sustituyendo todos los elementos de prueba por clases, nombres, servlets y mapeos propios del laboratorio.

> **Nombre del proyecto de trabajo:** `Proyecto_DEW`

---

### 3.4. Configuración del entorno técnico

Se acuerda que todo el grupo trabajará sobre una configuración homogénea:

| Componente | Versión |
|---|---|
| Servidor de aplicaciones | Apache Tomcat v10.1 |
| JDK | Temurin JDK 25 |

Se recuerda que el proyecto debe quedar correctamente asociado al runtime objetivo antes de ser lanzado, y que en caso de no aparecer en Eclipse puede importarse desde el sistema de archivos.

---

### 3.5. Alcance definitivo del Hito 1

Se acuerda centrar la primera entrega en los requisitos mínimos exigidos, priorizando una solución sólida frente a un desarrollo excesivamente ambicioso. El **Hito 1** incluirá:

1. Filtro Logs versión 2 operativo
2. Sistema de autenticación funcional
3. Prototipo de la secuencia de navegación del alumnado
4. Consultas desde servlets a CentroEducativo
5. Archivos de configuración de Tomcat utilizados
6. Script o secuencia de instrucciones para poblar CentroEducativo
7. Documentación técnica del código
8. Actas de reuniones en Markdown

---

### 3.6. Vistas mínimas para el Hito 1

El alcance visual se limita al flujo del alumnado. Las vistas acordadas son:

| # | Vista | Descripción |
|---|---|---|
| 1 | **Index** | Página de bienvenida con acceso al flujo de autenticación e identificación del grupo |
| 2 | **Login** | Pantalla de autenticación con validación mediante Tomcat y redirección según rol |
| 3 | **Lista de asignaturas (alumno)** | Muestra las asignaturas del alumno autenticado |
| 4 | **Detalle de asignatura / nota (alumno)** | Información detallada de una asignatura y su calificación |
| 5 | **Expediente (alumno)** | Recomendable para el Hito 1; se implementará si el flujo del alumno está estable |
| 6 | **Logout** | Finalización de sesión y retorno a la página inicial |

> La parte correspondiente al profesorado (navegación AJAX y edición dinámica de notas) se abordará tras estabilizar el flujo del alumnado.

---

### 3.7. Autenticación, roles y sesiones

Se confirman los roles del sistema:

- `rolalu` — alumnado
- `rolpro` — profesorado

Se decide implementar una **única pantalla de login**, delegando la autenticación al contenedor Tomcat y redirigiendo según el rol autenticado.

La seguridad del sistema se apoyará en dos niveles:

1. **Seguridad declarativa** mediante `web.xml`
2. **Seguridad programática** mediante comprobaciones dentro de los servlets cuando sea necesario

Los datos mínimos que se almacenarán en sesión para interactuar con CentroEducativo son:

| Atributo de sesión | Descripción |
|---|---|
| `dni` | Identificador del usuario autenticado |
| `password` | Contraseña para re-autenticación si fuera necesaria |
| `key` | Clave devuelta por CentroEducativo tras el login |

---

### 3.8. Sistema de logs

Se confirma que el grupo implementará directamente la **versión 2** del filtro de logs, al ser un requisito expreso del Hito 1.

Cada línea de log registrará, como mínimo:

- Fecha y hora
- Usuario (DNI)
- IP del cliente
- Servlet o recurso activado
- Método HTTP

**Formato orientativo aprobado:**

```
2026-05-04T17:22:10 cmolpea 158.xx.xx.xx AlumnoAsignaturasServlet GET
```

Acuerdos adicionales:

- La ruta del fichero persistente se configurará desde `web.xml`
- Las entradas se mantendrán ordenadas cronológicamente
- El funcionamiento del filtro se documentará en `docs/logs.md`

---

### 3.9. Operaciones mínimas con CentroEducativo

Se acuerda el conjunto mínimo de operaciones REST necesarias para el Hito 1:

**Operaciones prioritarias:**

- Login contra CentroEducativo y obtención de la `key`
- Consulta de asignaturas del alumno autenticado
- Consulta del detalle o nota de una asignatura concreta
- Recuperación de datos para la vista de expediente

**Operaciones auxiliares:**

- Pruebas manuales mediante `curl`
- Script de poblado para dejar CentroEducativo en un estado conocido

> Estas operaciones se documentarán en `docs/centroeducativo-operaciones.md`.

---

### 3.10. Estructura del repositorio y del proyecto

**Estructura del repositorio GitHub:**

```
actas/
docs/
scripts/
config/
entregas/
nol2526/
```

**Estructura lógica del proyecto Eclipse `nol2526`:**

```
src/main/java/es/upv/dew/nol2526/
    filters/
    servlets/
    client/
    model/
    util/

src/main/webapp/
    css/
    js/
    img/
    WEB-INF/
```

Esta organización facilita la separación de responsabilidades, la documentación mediante Javadoc y la escalabilidad hacia la entrega final.

---

### 3.11. Documentación técnica y Javadoc

Se acuerda que la documentación técnica se realizará principalmente mediante **Javadoc**, con comentarios obligatorios en:

- Clases
- Métodos públicos principales
- Servlets
- Filtros
- Cliente de acceso a CentroEducativo
- Utilidades relevantes

Se complementará con documentación en Markdown dentro de `docs/`. Documentos mínimos aprobados:

| Fichero | Contenido |
|---|---|
| `decisiones-iniciales.md` | Decisiones técnicas y organizativas del grupo |
| `mapa-navegacion.md` | Diagrama y descripción del flujo de pantallas |
| `roles-y-permisos.md` | Descripción de roles y restricciones de acceso |
| `logs.md` | Funcionamiento del filtro de logs |
| `centroeducativo-operaciones.md` | Operaciones REST utilizadas |
| `ejecucion.md` | Instrucciones para arrancar el proyecto |

---

## 4. Acuerdos adoptados

1. El repositorio GitHub se utilizará de forma efectiva a partir de esta sesión.
2. Una sola persona realizará la vinculación inicial; el resto clonará el repositorio.
3. El proyecto base será el derivado de `ServletPrueba`, adaptado a `nol2526`.
4. El entorno de trabajo será Apache Tomcat v10.1 + Temurin JDK 25.
5. El alcance del Hito 1 se limita a los requisitos expresamente exigidos: logs v2, autenticación, flujo del alumnado, consultas a CentroEducativo, documentación, script de poblado y actas.
6. Las vistas mínimas del Hito 1 son: index, login, lista de asignaturas, detalle de asignatura, logout y (si es posible) expediente.
7. Los roles del sistema son `rolalu` y `rolpro`.
8. La autenticación se apoyará en Tomcat con mecanismos programáticos adicionales cuando sean necesarios.
9. En sesión se almacenarán `dni`, `password` y `key` para la integración con CentroEducativo.
10. El sistema de logs se implementará directamente como filtro persistente (versión 2).
11. La documentación técnica se realizará con Javadoc y se complementará con Markdown en `docs/`.
12. Se aprueba la estructura base del repositorio y del proyecto.
13. El primer `push` al repositorio fue resuelto repitiendo la subida tras eliminar el conflicto.
14. La rama principal del repositorio pasa a denominarse `master`.
15. Cada integrante trabajará en una rama propia para reducir conflictos de `merge`.

---

## 5. Tareas asignadas

| Integrante | Tarea asignada |
|---|---|
| Daniel Zanon Barney | Configuración de seguridad: `web.xml`, roles, autenticación y archivo de usuarios de Tomcat |
| Mikel Escudero Aramburu | Diseño e implementación inicial del filtro `LogsFilter` |
| Vanesa Carolina Castro Bello | Maquetación inicial de `index` y `login` con Bootstrap |
| Pau Oroval González | Diseño de las vistas del alumnado: lista de asignaturas y detalle |
| Michal Pojnar | Identificación y prueba de operaciones REST con `curl`; preparación del script de poblado |
| Carlos Moldes Peña | Integración general, estructura del repositorio, documentación base y redacción del acta |

---

## 6. Problemas y riesgos detectados

- Posibles diferencias de configuración entre máquinas virtuales del laboratorio.
- Necesidad de comprobar que todos los miembros pueden trabajar correctamente con Eclipse, EGit y Tomcat.
- Riesgo de dedicar demasiado tiempo a la parte visual del profesorado antes de estabilizar el flujo del alumnado.
- Necesidad de coordinar adecuadamente la autenticación web con la del nivel de datos (CentroEducativo).

---

## 7. Próximos pasos

- Dejar operativo el repositorio compartido con la estructura acordada.
- Importar o crear el proyecto `nol2526` en Eclipse.
- Configurar y comprobar el arranque en Tomcat.
- Crear la estructura base del proyecto y del repositorio.
- Iniciar el desarrollo de: autenticación, flujo del alumnado, filtro Logs v2 y cliente REST de CentroEducativo.
- Completar la documentación técnica básica y el script de poblado.

---

## 8. Validación del acta

El acta ha sido revisada y aceptada por todos los integrantes del grupo.

| Nombre | Firma | Fecha |
|---|---|---|
| Vanesa Carolina Castro Bello | | |
| Mikel Escudero Aramburu | | |
| Carlos Moldes Peña | | |
| Pau Oroval González | | |
| Michal Pojnar | | |
| Daniel Zanon Barney | | |

---

*Acta redactada por Vanesa Castro Bello – Secretaria del Grupo G14*
