# ejecucion.md

## Ejecución del proyecto

## Requisitos del entorno
- Apache Tomcat v10.1
- Java Runtime Environment v25
- Eclipse IDE con soporte para proyectos web y Git

## Clonación del repositorio
El repositorio debe clonarse desde GitHub utilizando la URL HTTPS del repositorio completo.

Se recomienda trabajar desde una rama individual y no directamente sobre la rama principal.

## Importación del proyecto en Eclipse
1. Clonar el repositorio desde Eclipse o desde el entorno disponible.
2. Importar el proyecto existente en el workspace.
3. Verificar que el runtime objetivo es **Apache Tomcat v10.1**.
4. Verificar que el entorno Java asociado es **Java 25**.

## Estructura principal del proyecto
El proyecto debe contener, al menos, las carpetas:
- `src/main/java`
- `src/main/webapp`
- `src/main/webapp/WEB-INF`

## Despliegue
El despliegue se realiza sobre Tomcat desde Eclipse, mediante el servidor configurado en el entorno.


---

# Evolución tras el Hito 1

## Ejecución completa de la versión final

Tras la finalización del Hito 1 se incorporaron nuevas funcionalidades y dependencias que obligaron a ampliar el procedimiento de despliegue.

La versión final del proyecto requiere la ejecución coordinada de:

* CentroEducativo
* Script de población
* Apache Tomcat
* Aplicación NOL

---

## Arranque de CentroEducativo

Antes de desplegar NOL debe estar disponible el backend CentroEducativo.

Dependiendo del entorno utilizado, puede arrancarse mediante:

```bash
/home/dew/lanza-centroeducativo.sh
```

o mediante el ejecutable JAR suministrado:

```bash
cd /home/dew/opt/centroeducativo

/usr/lib/jvm/java-8-openjdk-amd64/bin/java \
-jar es.upv.etsinf.ti.centroeducativo-0.2.0.jar
```

Una vez arrancado, la API REST queda disponible en:

```text
http://localhost:9090/CentroEducativo
```

La documentación Swagger puede consultarse en:

```text
http://localhost:9090/CentroEducativo/swagger-ui.html
```

---

## Población de datos de prueba

CentroEducativo no conserva los datos tras reiniciarse.

Por este motivo debe ejecutarse el script de población incluido en el repositorio.

Ejemplo:

```bash
bash scripts/poblar-centroeducativo-completo.sh
```

o la versión corregida utilizada durante las pruebas:

```bash
bash scripts/poblar-centroeducativo-corregido-v2.sh
```

El script crea:

* profesorado;
* alumnado;
* asignaturas;
* matrículas;
* calificaciones.

---

## Configuración de la URL base

La comunicación con CentroEducativo se realiza mediante:

```java
CentroEducativoClient
```

La URL base utilizada durante el desarrollo es:

```text
http://localhost:9090/CentroEducativo
```

Si el backend se despliega en otra máquina deberá actualizarse la configuración correspondiente.

---

## Despliegue en Eclipse

Pasos utilizados durante las pruebas:

1. Importar el proyecto Maven.
2. Asociar Apache Tomcat 10.1.
3. Verificar el JDK configurado.
4. Limpiar el proyecto.
5. Publicar sobre Tomcat.
6. Arrancar el servidor.

En caso de incidencias:

```text
Project → Clean
```

y posteriormente:

```text
Tomcat → Clean Tomcat Work Directory
```

---

## Acceso a la aplicación

Una vez desplegada:

```text
http://localhost:8080/DEW/
```

o el contexto configurado para el proyecto.

La página inicial permite acceder a:

* zona de alumnado;
* zona de profesorado.

---

## Usuarios de prueba

Durante las pruebas se utilizaron cuentas de alumnado y profesorado incluidas en:

```text
tomcat-users.xml
```

y sincronizadas con los datos cargados en CentroEducativo.

Es importante utilizar usuarios que hayan sido creados por el script de población para garantizar que disponen de asignaturas y datos académicos asociados.

---

## Fotografías del alumnado

La versión final incorpora fotografías de alumnado.

Las imágenes deben ubicarse en:

```text
src/main/webapp/fotos/
```

y nombrarse utilizando el DNI del usuario:

```text
12345678A.png
33445566X.png
87654321B.png
```

El sistema carga automáticamente la fotografía correspondiente a partir del DNI recibido.

---

## Comprobaciones recomendadas

Antes de una demostración o auditoría se recomienda verificar:

* CentroEducativo arrancado.
* Script de población ejecutado.
* Tomcat operativo.
* Login funcional.
* Acceso a zona alumno.
* Acceso a zona profesor.
* Consulta de asignaturas.
* Consulta de expediente.
* Modificación de nota mediante AJAX.
* Visualización de fotografías.
* Generación de certificado imprimible.

---

## Problemas encontrados durante el despliegue

Durante el desarrollo aparecieron incidencias relacionadas con:

* diferencias entre versiones de Java;
* configuración de Tomcat;
* rutas relativas de recursos estáticos;
* sincronización entre Tomcat y CentroEducativo;
* despliegue en máquinas virtuales.

La solución adoptada consistió en unificar versiones, centralizar la configuración REST y validar el despliegue completo mediante pruebas de integración.

---

## Estado final

La versión final del proyecto dispone de un procedimiento de despliegue completamente funcional que permite arrancar el backend, poblar datos de prueba, desplegar NOL y probar tanto el flujo del alumnado como el del profesorado.


## Observación importante
El `web.xml` válido para el proyecto es el situado en:

```text
src/main/webapp/WEB-INF/web.xml
