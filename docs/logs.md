# logs.md

## Sistema de logs del proyecto

## Objetivo
El objetivo del sistema de logs es registrar el uso de la aplicación web, dejando constancia de las peticiones realizadas y del contexto mínimo necesario para su seguimiento.

## Implementación prevista
El sistema de logs se implementará mediante un filtro denominado:

- `LogsFilter`

Este filtro deberá activarse desde `web.xml` y aplicarse sobre las rutas necesarias de la aplicación.

## Información mínima a registrar
Cada entrada de log deberá incluir, como mínimo:

- fecha y hora
- usuario identificado o dato equivalente disponible
- IP del cliente
- recurso o servlet accedido
- método HTTP

## Ejemplo de línea de log
```text
2026-05-11T17:22:10 usuario 158.xx.xx.xx AlumnoAsignaturasServlet GET
