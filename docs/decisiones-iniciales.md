#decisiones-iniciales.md

## Decisiones iniciales del proyecto

### 1. Identificación del proyecto

- **Proyecto:** nol2526
- **Asignatura:** Desarrollo Web (DEW)
- **Curso:** 2025/2026
- **Grupo:** G14 – 3TI21

---

### 2. Base técnica acordada

El grupo acuerda desarrollar el proyecto sobre una base técnica común, con el fin de evitar diferencias de compilación, despliegue y ejecución entre entornos de trabajo.

Se fija como entorno técnico:

- **Servidor de aplicaciones:** Apache Tomcat v10.1
- **Entorno Java:** Java Runtime Environment v25
- **Paquete Java base:** `dew`
- **IDE principal de desarrollo:** Eclipse

---

### 3. Organización del repositorio

Se acuerda utilizar GitHub como repositorio compartido del grupo y mantener una estructura organizada que permita separar código, documentación y materiales auxiliares.

La estructura principal del repositorio queda definida como:

```
actas/
docs/
scripts/
config/
entregas/
src/
```

---

### 4. Organización interna del proyecto

La estructura base del proyecto Java web se organiza del siguiente modo:

```
src/main/java/dew/
    client/
    filters/
    model/
    servlets/
    util/

src/main/webapp/
    css/
    js/
    img/
    META-INF/
    WEB-INF/
```

Con ello se pretende separar claramente: acceso a servicios externos, filtros, servlets, utilidades y recursos web estáticos.

---

### 5. Método de trabajo con Git

Se acuerda trabajar mediante ramas individuales, evitando realizar cambios directamente sobre la rama principal salvo en integraciones acordadas por el grupo.

Se establece además que:

- cada integrante trabajará en su propia rama
- los cambios se integrarán posteriormente en `master`
- antes de integrar se fijará una base común de nombres, rutas y responsabilidades para reducir conflictos

---

### 6. Alcance funcional del Hito 1

Se acuerda priorizar para el Hito 1 la parte mínima funcional exigida por la asignatura, centrándose en el flujo del alumnado.

Se considera prioritario implementar:

- autenticación y control por roles
- navegación del alumnado
- sistema de logs
- integración mínima con CentroEducativo
- documentación técnica y organizativa

La parte del profesorado podrá quedar iniciada o estructurada, pero no constituye la prioridad principal de esta fase.

---

### 7. Nombres de clases principales ya fijados

| Categoría | Clase |
|---|---|
| Cliente REST | `CentroEducativoClient` |
| Filtro | `LogsFilter` |
| Servlet login | `LoginRedirectServlet` |
| Servlet alumno | `AlumnoAsignaturasServlet` |
| Servlet alumno | `AlumnoDetalleServlet` |
| Servlet alumno | `AlumnoExpedienteServlet` |
| Servlet logout | `LogoutServlet` |
| Utilidad de sesión | `SessionsUtils` |

---

### 8. Decisión sobre la gestión de sesión

Se acuerda centralizar la gestión de la sesión HTTP de la aplicación mediante la utilidad común `SessionsUtils.java`, con el fin de evitar que cada servlet gestione de forma independiente los atributos de sesión.

Los atributos de sesión acordados son:

| Atributo | Descripción |
|---|---|
| `dni` | Identificador del usuario autenticado |
| `password` | Contraseña del usuario |
| `key` | Clave de sesión devuelta por CentroEducativo |

La clase `SessionsUtils` se utilizará para crear la sesión, almacenar y recuperar atributos, comprobar si existe una sesión válida e invalidar la sesión en el logout.

Se establece como criterio general que cualquier acceso a estos atributos debe hacerse a través de esta utilidad para reducir duplicación de código e inconsistencias.

---

### 9. Decisión sobre la session key de CentroEducativo

La aplicación no se conecta directamente a una base de datos propia, sino que se integra con el nivel de datos proporcionado mediante servicios REST.

Tras el login contra CentroEducativo, el sistema obtiene una `key` de sesión necesaria para invocar las operaciones autenticadas posteriores. Dicha `key` se almacenará en la sesión HTTP junto con `dni` y `password`, de modo que pueda ser reutilizada por los servlets y por `CentroEducativoClient`.

---

### 10. Criterio sobre rutas y navegación

Se acuerda distinguir entre la estructura física del proyecto y las rutas públicas de acceso definidas en `web.xml`. Las rutas visibles de la aplicación no tienen que coincidir con los nombres físicos de las clases o carpetas.

Rutas de referencia acordadas:

```
/login
/alumno/asignaturas
/alumno/detalle
/alumno/expediente
/logout
```

---

### 11. Roles definidos

Se fijan como roles principales del sistema:

- `rolalu` — alumnado
- `rolpro` — profesorado

Estos roles se utilizarán para restringir vistas y operaciones según el tipo de usuario autenticado.

---

### 12. Dependencias y librerías

Se acuerda:

- no añadir manualmente la API de servlets, al estar ya proporcionada por Tomcat 10.1
- utilizar Bootstrap 5 por CDN para el apoyo visual
- valorar el uso de **Gson** si la integración con CentroEducativo requiere tratamiento cómodo de respuestas JSON
- evitar añadir librerías innecesarias que compliquen la integración

---

### 13. Cierre de la fase inicial

Se considera que, tras la creación de la estructura base y la revisión del reparto, queda cerrada la fase inicial del proyecto. A partir de este punto, el grupo debe centrarse prioritariamente en:

- completar `web.xml`
- implementar autenticación
- desarrollar `LogsFilter`
- hacer funcional el flujo del alumnado
- integrar las operaciones mínimas con CentroEducativo
- completar la documentación y los Javadocs
