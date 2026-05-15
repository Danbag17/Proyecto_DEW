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

## Observación importante
El `web.xml` válido para el proyecto es el situado en:

```text
src/main/webapp/WEB-INF/web.xml
