# NOL 25/26 · Notas OnLine

Portal académico web para la consulta y gestión de calificaciones, desarrollado para la
asignatura **Desarrollo de Entornos Web (DEW)** — Escuela Técnica Superior de Informática,
curso 2025/2026.

- El **alumnado** consulta sus asignaturas matriculadas, la nota de cada una y genera un
  expediente imprimible.
- El **profesorado** consulta las asignaturas que imparte, el alumnado matriculado y la nota
  media, y puede **modificar** calificaciones.

La aplicación es un *frontend* (HTML + JavaScript) que habla con **servlets Java** propios.
Esos servlets, a su vez, actúan como cliente de un **backend externo** llamado
`CentroEducativo` (servicio REST), que es donde residen realmente los datos.

```
Navegador  ──►  Webapp NOL (Tomcat)  ──►  Backend CentroEducativo (REST)
 (HTML/JS)        servlets + filtros          datos reales
```

---

## 1. Requisitos

| Componente | Versión |
|------------|---------|
| **Apache Tomcat** | **10.1.x** (Jakarta EE 6 / Servlet 6.0 — namespace `jakarta.*`) |
| **JDK** | 17 o superior |
| **Eclipse IDE for Enterprise Java and Web Developers** | con plugin WTP (Web Tools) |
| **Backend `CentroEducativo`** | en marcha y accesible por red |
| **bash + curl** | solo para poblar el backend con datos de prueba |

> ⚠️ Es **Tomcat 10.1**, no 9. El proyecto usa el espacio de nombres `jakarta.*`
> (`jakarta.servlet`). Sobre Tomcat 9 (que usa `javax.*`) **no arranca**.

Las librerías de terceros ya van incluidas en `src/main/webapp/WEB-INF/lib/`
(no hay que descargar nada): `gson`, `okhttp`, `okio`, `kotlin-stdlib`, `jakarta.ws.rs-api`.

---

## 2. Estructura del proyecto

```
Proyecto_DEW/
├── README.md                         ← este archivo
├── conf/
│   └── tomcat-users.xml              ← usuarios y roles (copiar a Tomcat, ver §4)
├── scripts/
│   └── poblar-centroeducativo-completo.sh   ← carga datos de prueba en el backend
├── src/main/
│   ├── java/dew/
│   │   ├── client/CentroEducativoClient.java   ← cliente del backend (⚠ BASE_URL aquí)
│   │   ├── filters/   (AuthFilter, LogsFilter)
│   │   ├── servlets/  (Alumno*, Profesor*, ModificarNota, Logout, ErrorHandler…)
│   │   └── util/      (AsignaturasUtils, SessionsUtils)
│   └── webapp/
│       ├── index.html  login.html  login-error.html
│       ├── alumno/     (vistas del alumnado)
│       ├── profesor/   (vistas del profesorado)
│       ├── css/nol.css  js/api.js  fotos/
│       ├── WEB-INF/web.xml           ← servlets, filtros, seguridad, páginas de error
│       └── META-INF/context.xml      ← Valve de autenticación (FormAuthenticator)
```

La aplicación se despliega con el **contexto `/DEW`** (la URL base es `http://HOST:8080/DEW/`).
Este nombre está fijado en varios sitios (`context.xml`, formularios de login, enlaces),
así que **debe desplegarse exactamente como `DEW`**.

---

## 3. Configurar la conexión con el backend

El backend `CentroEducativo` corre por separado (por defecto en el puerto **9090**).
Hay que indicarle a la webapp dónde encontrarlo.

Edita **`src/main/java/dew/client/CentroEducativoClient.java`** y cambia la constante
`BASE_URL` por la IP/host real del backend:

```java
private static final String BASE_URL =
        "http://172.23.189.79:9090/CentroEducativo";   // ← pon aquí tu IP:puerto
```

Tras cambiarla hay que **recompilar y volver a desplegar** (ver §6).

---

## 4. Configurar Tomcat (usuarios y roles)

La autenticación es **FORM contra el realm de Tomcat**: los usuarios válidos y sus roles
se definen en el fichero **`tomcat-users.xml`** de Tomcat (NO en la base de datos del
backend). Hay dos roles:

- `rolalu` → alumnado
- `rolpro` → profesorado

Este repositorio incluye un `conf/tomcat-users.xml` ya preparado con todos los usuarios de
prueba. **Cópialo a la carpeta `conf/` de tu instalación de Tomcat**, sustituyendo el que
trae por defecto:

```bash
cp conf/tomcat-users.xml $CATALINA_HOME/conf/tomcat-users.xml
```

> En Eclipse, si usas un servidor Tomcat gestionado por el propio Eclipse, la copia de
> `tomcat-users.xml` que se usa al arrancar es la de la carpeta **Servers/** del workspace
> (p. ej. `Servers/Tomcat v10.1 Server at localhost-config/tomcat-users.xml`). En ese caso,
> pega ahí el contenido de `conf/tomcat-users.xml`.

El usuario se identifica con su **DNI** (campo usuario) y la contraseña. Todas las cuentas
de prueba usan la contraseña `123456`.

**Credenciales de ejemplo:**

| Rol | DNI | Contraseña |
|-----|-----|------------|
| Alumno   | `12345678A` | `123456` |
| Alumno   | `12345678W` | `123456` |
| Profesor | `22222222P` | `123456` |
| Profesor | `23456733H` | `123456` |

(La lista completa está en `conf/tomcat-users.xml`.)

> Los DNIs que aparecen en `tomcat-users.xml` deben coincidir con los alumnos/profesores
> dados de alta en el backend. Si usas el script de población (§5), ya está todo alineado.

---

## 5. Poblar el backend con datos de prueba (opcional)

Si el backend está vacío, puedes cargarlo con profesores, alumnos, asignaturas, matrículas
y notas de ejemplo usando el script incluido:

```bash
chmod +x scripts/poblar-centroeducativo-completo.sh
./scripts/poblar-centroeducativo-completo.sh
```

Para apuntar a otra IP/puerto del backend, exporta `API_URL` antes de ejecutarlo:

```bash
API_URL="http://IP:9090/CentroEducativo" ./scripts/poblar-centroeducativo-completo.sh
```

El script es *idempotente best-effort*: si un dato ya existe, ese alta concreta puede fallar
y simplemente lo avisa, sin abortar el resto. Los usuarios que crea ya están reflejados en
`conf/tomcat-users.xml`.

---

## 6. Compilar y desplegar

El proyecto es un **Dynamic Web Project de Eclipse** (no usa Maven ni Gradle).

### Opción A — desde Eclipse (recomendada)

1. **File → Import → Existing Projects into Workspace** y selecciona esta carpeta.
2. Asegúrate de tener un **runtime de Apache Tomcat v10.1** registrado
   (*Window → Preferences → Server → Runtime Environments*).
3. Comprueba que el backend está configurado (§3) y `tomcat-users.xml` copiado (§4).
4. Botón derecho sobre el proyecto → **Run As → Run on Server** → elige el Tomcat 10.1.
5. Eclipse compila, despliega como contexto `/DEW` y abre el navegador.

### Opción B — desplegar un WAR manualmente

1. En Eclipse: **Export → WAR file** y nómbralo **`DEW.war`** (el nombre fija el contexto).
2. Copia `DEW.war` a `$CATALINA_HOME/webapps/`.
3. Arranca Tomcat:
   ```bash
   $CATALINA_HOME/bin/startup.sh      # Windows: bin\startup.bat
   ```

---

## 7. Usar la aplicación

1. Abre **`http://localhost:8080/DEW/`** (o la IP/puerto de tu Tomcat).
2. Verás la portada. Para entrar a las zonas privadas te redirige al **login**.
3. Identifícate con un **DNI + contraseña** de `tomcat-users.xml`:
   - Con un usuario `rolalu` accedes al **área de alumnado** (`/DEW/alumno/...`).
   - Con un usuario `rolpro` accedes al **área de profesorado** (`/DEW/profesor/...`).
4. Para salir, usa **Cerrar sesión / Salir** (invalida la sesión y vuelve a la portada).

---

## 8. Cómo funciona por dentro (resumen técnico)

- **Autenticación FORM (`web.xml` + Realm de Tomcat).** El formulario de `login.html` hace
  `POST` a `/DEW/j_security_check` con `j_username` (DNI) y `j_password`. Tomcat valida
  contra `tomcat-users.xml` y asigna el rol.
- **Autorización por zonas (`<security-constraint>` en `web.xml`):**
  `/alumno/*` y los endpoints de alumno exigen `rolalu`; `/profesor/*` exige `rolpro`;
  `/AsignaturasServlet` es común a ambos roles autenticados.
- **`context.xml`** declara explícitamente el `FormAuthenticator` con
  `landingPage="/DEW/index.html"` para que, si la sesión caduca al hacer login (petición sin
  *saved request*), Tomcat redirija a la portada en vez de devolver un HTTP 408.
- **Servlets** (`dew.servlets.*`): reciben las peticiones AJAX, llaman al backend a través de
  `CentroEducativoClient` y devuelven **JSON** (procesado con Gson). El JavaScript de
  `js/api.js` consume ese JSON y pinta las vistas.
- **Manejo de errores:** ante una excepción, los servlets registran la traza en el log de
  Tomcat (`printStackTrace`) y devuelven un mensaje genérico (no se filtran detalles internos
  al cliente). En `js/api.js`, `ajax()` traduce el código HTTP a un mensaje claro en
  castellano que se muestra en el recuadro de error. Los errores 403/404/500 de Tomcat se
  enrutan al servlet `ErrorHandler`, que muestra una página de error con el estilo del portal.
- **Logs de acceso:** el `LogsFilter` registra los accesos en el fichero indicado por el
  parámetro de contexto `logFilePath` (por defecto `/tmp/nol2526-access.log`). Se puede
  activar/desactivar con el parámetro `logsHabilitado` en `web.xml`, sin tocar código.

---

## 9. Solución de problemas

| Síntoma | Causa probable | Solución |
|---------|----------------|----------|
| La app no carga / errores `javax.servlet` | Tomcat 9 en vez de 10.1 | Usa **Tomcat 10.1** (Jakarta) |
| Login siempre falla | `tomcat-users.xml` no copiado o usuario inexistente | Copia `conf/tomcat-users.xml` a Tomcat (§4) y reinicia |
| Las vistas salen vacías o dan error de servidor | Backend caído o `BASE_URL` mal | Revisa §3 y que el backend responda en su IP:9090 |
| 404 al abrir la app | Contexto distinto de `/DEW` | Despliega como `DEW` (WAR `DEW.war`) |
| Tras logout, al loguear sale un 408 | Sesión/saved-request caducados | Ya cubierto por el `landingPage` de `context.xml` |
| No hay datos que mostrar | Backend vacío | Ejecuta el script de población (§5) |

---

## 10. Grupo · DEW 2025/2026 (G14 · 3TI21)

- Castro Bello, Vanesa Carolina
- Escudero Aramburu, Mikel
- Moldes Peña, Carlos
- Oroval González, Pau
- Pojnar, Michal
- Zanon Barney, Daniel
