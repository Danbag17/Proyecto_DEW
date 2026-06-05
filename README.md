# NOL 25/26 · Notas OnLine

Portal web académico para consultar y gestionar calificaciones, desarrollado para la asignatura **Desarrollo de Entornos Web (DEW)** de la Escuela Técnica Superior de Ingeniería Informática, curso 2025/2026.

La aplicación permite:

- Al **alumnado**, consultar sus asignaturas matriculadas, ver las notas disponibles y generar un expediente imprimible.
- Al **profesorado**, consultar sus asignaturas, ver el alumnado matriculado y modificar calificaciones.

El proyecto está formado por una aplicación web desplegada en **Tomcat**. La parte visible está hecha con **HTML, CSS y JavaScript**, mientras que la lógica intermedia se implementa mediante **servlets Java**. Estos servlets se comunican con el backend externo `CentroEducativo`, que expone los datos mediante un servicio REST.

```text
Navegador  ──►  Webapp NOL en Tomcat  ──►  Backend CentroEducativo
HTML/CSS/JS      Servlets + filtros          Servicio REST
```

---

## 1. Requisitos

| Componente | Versión / detalle |
|------------|-------------------|
| Apache Tomcat | v10.1 |
| JDK | 17 o superior |
| Eclipse IDE for Enterprise Java and Web Developers | Con soporte WTP |
| Backend `CentroEducativo` | Arrancado y accesible por red |
| bash + curl | Solo necesarios para ejecutar el script de carga de datos |

El proyecto usa **Tomcat 10.1** y, por tanto, el espacio de nombres `jakarta.*`, por ejemplo `jakarta.servlet`. No debe ejecutarse sobre Tomcat 9, ya que Tomcat 9 trabaja con `javax.*` y la aplicación no arrancaría correctamente.

Las librerías necesarias ya están incluidas dentro de:

```text
src/main/webapp/WEB-INF/lib/
```

Por tanto, no hace falta descargarlas aparte. Entre ellas se incluyen `gson`, `okhttp`, `okio`, `kotlin-stdlib` y `jakarta.ws.rs-api`.

---

## 2. Estructura del proyecto

```text
Proyecto_DEW/
├── README.md
├── conf/
│   └── tomcat-users.xml
├── scripts/
│   └── poblar-centroeducativo-completo.sh
├── src/main/
│   ├── java/dew/
│   │   ├── client/
│   │   │   └── CentroEducativoClient.java
│   │   ├── filters/
│   │   │   ├── AuthFilter.java
│   │   │   └── LogsFilter.java
│   │   ├── servlets/
│   │   │   └── ...
│   │   └── util/
│   │       ├── SessionsUtils.java
│   │       └── AsignaturasUtils.java
│   └── webapp/
│       ├── index.html
│       ├── login.html
│       ├── login-error.html
│       ├── alumno/
│       ├── profesor/
│       ├── css/
│       │   └── nol.css
│       ├── js/
│       │   └── api.js
│       ├── fotos/
│       ├── WEB-INF/
│       │   └── web.xml
│       └── META-INF/
│           └── context.xml
```

La aplicación está pensada para desplegarse con el contexto **`/DEW`**. Por ejemplo:

```text
http://localhost:8080/DEW/
```

El nombre del contexto es importante porque aparece en varias rutas de la aplicación, como formularios, redirecciones y configuración de autenticación. Por ese motivo, el proyecto debe desplegarse como **DEW**.

---

## 3. Configuración del backend

El backend `CentroEducativo` se ejecuta por separado. Por defecto se espera que esté disponible en el puerto **9090**.

Para indicar a la aplicación dónde está el backend, hay que modificar la constante `BASE_URL` en el archivo:

```text
src/main/java/dew/client/CentroEducativoClient.java
```

Ejemplo:

```java
private static final String BASE_URL =
        "http://localhost:9090/CentroEducativo";
```

Si cambia la IP o el puerto del backend, debe actualizarse esta constante, recompilar el proyecto y volver a desplegarlo en Tomcat.

Si el backend CentroEducativo se va a consumir desde otra máquina distinta a la que lo ejecuta, además de configurar correctamente la IP en BASE_URL, puede ser necesario abrir el puerto 9090 en el firewall del servidor. En Linux, si se usa ufw, se puede permitir el acceso con sudo ufw allow 9090/tcp y comprobar el estado con sudo ufw status. Si todo se ejecuta en la misma máquina, normalmente no hace falta abrir este puerto.

```bash
	sudo ufw allow 9090/tcp
	sudo ufw status
```


## 4. Configuración de Tomcat: usuarios y roles

La autenticación se realiza mediante **FORM authentication** usando el realm de Tomcat. Los usuarios válidos no están en el backend, sino en el archivo:

```text
tomcat-users.xml
```

El proyecto utiliza dos roles:

| Rol | Uso |
|-----|-----|
| `rolalu` | Acceso a la zona de alumnado |
| `rolpro` | Acceso a la zona de profesorado |

El repositorio incluye un archivo ya preparado en:

```text
conf/tomcat-users.xml
```


Si se usa Tomcat desde Eclipse, ya que este tiene su propia copia de este archivo, se copia e contenido de `tomcat-users.xml` en la ruta:

```text
Servers/Tomcat v10.1 Server at localhost-config/tomcat-users.xml
```

En ese archivo también debe estar el contenido correcto de usuarios y roles.

Los usuarios se identifican mediante su **DNI** y la contraseña. Las cuentas de prueba usan la contraseña:

```text
123456
```

Credenciales de ejemplo:

| Tipo | DNI | Contraseña |
|------|-----|------------|
| Alumno | `12345678A` | `123456` |
| Alumno | `12345678W` | `123456` |
| Profesor | `22222222P` | `123456` |
| Profesor | `23456733H` | `123456` |

La lista completa está en `conf/tomcat-users.xml`.

Los DNIs definidos en `tomcat-users.xml` deben coincidir con los alumnos y profesores existentes en el backend. Si se usa el script de carga de datos incluido en el proyecto, los datos quedan preparados para funcionar con esas credenciales.

---

## 5. Cargar datos de prueba en el backendd

Si el backend está vacío, se puede ejecutar el script incluido para crear profesores, alumnos, asignaturas, matrículas y notas de prueba.

```bash
chmod +x scripts/poblar-centroeducativo-completo.shh
./scripts/poblar-centroeducativo-completo.sh
```

Si el backend está en otra IP o puerto, se puede indicar mediante la variable `API_URL`:

```bash
API_URL="http://IP:9090/CentroEducativo" ./scripts/poblar-centroeducativo-completo.sh
```

El script intenta cargar todos los datos necesarios. Si algún dato ya existe, puede mostrar un aviso para esa operación concreta, pero continúa con el resto de la carga.

---

## 6. Compilación y despliegue

El proyecto está preparado como **Dynamic Web Project** de Eclipse. No usa Maven ni Gradle.

### Opción A: despliegue desde Eclipse

1. Importar el proyecto desde **File → Import → Existing Projects into Workspace**.
2. Seleccionar la carpeta del proyecto.
3. Comprobar que Eclipse tiene configurado un runtime de **Apache Tomcat 10.1**.
4. Configurar la URL del backend en `CentroEducativoClient.java`.
5. Copiar correctamente `tomcat-users.xml` en la configuración de Tomcat.
6. Pulsar botón derecho sobre el proyecto.
7. Seleccionar **Run As → Run on Server**.
8. Elegir Tomcat 10.1.
9. Abrir la aplicación en:

```text
http://localhost:8080/DEW/
```

### Opción B: despliegue mediante WAR
-

## 7. Uso de la aplicación

1. Abrir la portada:

```text
http://localhost:8080/DEW/
```

2. Entrar con un usuario definido en `tomcat-users.xml`.
3. Si el usuario tiene el rol `rolalu`, accederá a la zona de alumnado.
4. Si el usuario tiene el rol `rolpro`, accederá a la zona de profesorado.
5. Para salir, usar la opción **Cerrar sesión** o **Salir**.

El cierre de sesión invalida la sesión activa y devuelve al usuario a la portada.

---

## 8. Funcionamiento interno

### Autenticación

La autenticación está configurada en `web.xml` mediante **FORM authentication**. El formulario de `login.html` envía las credenciales a:

```text
/DEW/j_security_check
```

Los campos usados por Tomcat son:

```text
j_username
j_password
```

Tomcat valida esos datos contra `tomcat-users.xml` y asigna los roles correspondientes.

### Autorizaciónn

El acceso a las zonas privadas se controla mediante restricciones de seguridad en `web.xml`.

- Las rutas de alumnado requieren el rol `rolalu`.
- Las rutas de profesorado requieren el rol `rolpro`.
- Algunos endpoints comunes pueden estar disponibles para ambos roles autenticados.

### Servlets

Los servlets reciben las peticiones de la interfaz, llaman al backend mediante `CentroEducativoClient.java` y devuelven respuestas en formato JSON.

El archivo `js/api.js` se encarga de hacer las peticiones AJAX desde el navegador y de procesar las respuestas para pintar la información en las vistas.

### Erroress

Cuando ocurre un error interno, los servlets registran la información en los logs de Tomcat y devuelven al cliente un mensaje genérico, evitando mostrar detalles internos de la aplicación.

En la parte del cliente, `api.js` interpreta los códigos HTTP y muestra mensajes más claros para el usuario.

Los errores configurados en Tomcat, como 403, 404 o 500, se redirigen al manejador de errores correspondiente para mostrarse con el estilo del portal.

### Logs

El filtro `LogsFilter` registra accesos en el archivo configurado mediante el parámetro de contexto `logFilePath`. Por defecto, se utiliza:

```text
/tmp/nol2526-access.log
```

También puede activarse o desactivarse desde `web.xml` mediante el parámetro `logsHabilitado`.

---

## 9. Solución de problemas

| Problema | Causa probable | Solución |
|----------|----------------|----------|
| La aplicación no arranca | Se está usando Tomcat 9 | Usar Tomcat 10.1 |
| Aparecen errores relacionados con `javax.servlet` | Versión incorrecta de Tomcat | Usar Tomcat 10.1, que trabaja con `jakarta.*` |
| El login falla siempre | `tomcat-users.xml` no está bien copiado o el usuario no existe | Revisar usuarios, roles y ubicación del archivo |
| Las vistas aparecen vacías | Backend apagado o URL incorrecta | Comprobar `BASE_URL` y que el backend responda |
| Error 404 al abrir la aplicación | Contexto de despliegue incorrecto | Desplegar como `DEW` o generar `DEW.war` |
| Después del logout hay problemas al volver a entrar | Sesión anterior o petición guardada caducada | Revisar configuración de `context.xml` y reiniciar sesión |
| No aparecen datos | Backend sin datos | Ejecutar el script de carga de datos |
| Las imágenes no cargan | Ruta incorrecta o archivo inexistente | Revisar la carpeta `fotos/` y las rutas usadas en las vistas |

---

## 10. Grupo

**DEW 2025/2026 · G14 · 3TI21**

- Castro Bello, Vanesa Carolina
- Escudero Aramburu, Mikel
- Moldes Peña, Carlos
- Oroval González, Pau
- Pojnar, Michal
- Zanon Barney, Daniel
