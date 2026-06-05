# centroeducativo-operaciones.md

## Integración con CentroEducativo

### Objetivo

La aplicación no se conecta directamente a una base de datos propia, sino que se integra con el nivel de datos proporcionado mediante servicios REST expuestos por CentroEducativo.

La documentación de la API está disponible en:

```
http://localhost:9090/CentroEducativo/swagger-ui.html
```

---

### Cliente Java previsto

Todas las llamadas REST se centralizarán en la clase:

- `CentroEducativoClient.java` — paquete `dew.client`

---

### Operaciones mínimas previstas para el Hito 1

#### 1. Autenticación

- Login contra CentroEducativo enviando `dni` y `password`
- Obtención de la `key` de sesión devuelta por la API
- Almacenamiento de `dni`, `password` y `key` en la sesión HTTP mediante `SessionsUtils`

#### 2. Consulta de asignaturas del alumno

- Obtención de las asignaturas en las que está matriculado el alumno autenticado
- Parámetros necesarios: `dni`, `key`

#### 3. Consulta de detalle o nota de una asignatura

- Obtención del detalle o calificación correspondiente a una asignatura concreta
- Parámetros necesarios: `dni`, `key`, identificador de asignatura

#### 4. Consulta de expediente

- Recuperación de la información necesaria para construir la vista de expediente del alumno
- Se implementará si el flujo del alumnado queda estable dentro del Hito 1

---

### Operaciones previstas para fases posteriores

Las siguientes operaciones quedan previstas para la parte del profesorado, que se abordará tras el Hito 1:

- Consulta de asignaturas del profesor
- Consulta de alumnos por asignatura
- Modificación de la nota de un alumno en una asignatura

---

### Operaciones auxiliares

- Pruebas manuales de las llamadas REST mediante `curl`
- Script de poblado de CentroEducativo con datos de prueba — **ya completado**

---
---

# Evolución tras el Hito 1

## Ampliación de la integración con CentroEducativo

Durante el Hito 1 la integración con CentroEducativo se centró principalmente en validar la comunicación REST y en obtener correctamente la `key` de sesión necesaria para acceder a los recursos protegidos.

Tras la implementación completa del proyecto, la integración pasó a cubrir la totalidad de operaciones necesarias para alumnado y profesorado.

---

## Incorporación de AuthFilter

Uno de los cambios más importantes respecto al diseño inicial fue la incorporación de:

```text
dew.filters.AuthFilter
```

Inicialmente se planteó que los servlets gestionaran directamente el login contra CentroEducativo.

Durante la implementación se detectó que esta lógica se repetía en varios puntos de la aplicación, por lo que se decidió centralizarla en un filtro.

El flujo final quedó:

```text
Usuario
   ↓
Tomcat
   ↓
AuthFilter
   ↓
CentroEducativo
   ↓
Obtención de key
   ↓
SessionsUtils
   ↓
Servlets
```

Gracias a esta decisión todos los servlets reciben una sesión ya preparada para trabajar con CentroEducativo.

---

## Evolución de CentroEducativoClient

Durante el Hito 1 el cliente REST se utilizaba principalmente para validar credenciales y realizar consultas básicas.

En la versión final se amplió para soportar:

### Funcionalidades de alumnado

* consulta de asignaturas matriculadas;
* consulta de detalle de asignatura;
* consulta de expediente académico.

### Funcionalidades de profesorado

* consulta de asignaturas impartidas;
* consulta de alumnado matriculado;
* modificación de notas;
* recuperación de información adicional para las vistas.

Todas las llamadas REST continúan centralizadas en:

```text
dew.client.CentroEducativoClient
```

evitando que los servlets construyan peticiones HTTP directamente.

---

## Gestión de la session key

La `key` devuelta por CentroEducativo pasó a convertirse en un elemento central de la arquitectura.

Proceso definitivo:

```text
Login Tomcat
      ↓
AuthFilter
      ↓
Login REST
      ↓
Obtención de key
      ↓
SessionsUtils
      ↓
Uso por servlets y AJAX
```

La reutilización de esta clave evita autenticaciones repetidas y reduce el número de peticiones realizadas al backend.

---

## Uso desde AJAX

Durante el Hito 1 las operaciones se realizaban principalmente mediante navegación tradicional.

La versión final incorpora AJAX para varias funcionalidades del profesorado.

Las peticiones siguen utilizando la misma sesión HTTP y la misma `key` almacenada en `SessionsUtils`, por lo que no es necesario realizar nuevas autenticaciones.

Esto permite:

* consultar alumnado matriculado;
* actualizar información dinámica;
* modificar calificaciones sin recargar la página.

---

## Integración con el expediente académico

La funcionalidad de expediente evolucionó considerablemente respecto al planteamiento inicial.

Para generar la vista final fue necesario combinar información procedente de varias operaciones REST:

```text
Datos personales
+
Asignaturas matriculadas
+
Calificaciones
+
Información ampliada de asignaturas
=
Expediente final
```

La información obtenida se utiliza además para generar el certificado imprimible solicitado por el enunciado.

---

## Integración de fotografías

La versión final incorpora fotografías asociadas a alumnado y profesorado.

Se adoptó el criterio:

```text
fotos/<DNI>.png
```

Ejemplos:

```text
fotos/12345678A.png
fotos/33445566X.png
fotos/22222222P.png
```

La fotografía se carga automáticamente utilizando el DNI recibido desde CentroEducativo.

Esta solución evita almacenar imágenes dentro de las respuestas JSON del backend.

---

## Script de población definitivo

Durante la fase final del proyecto se amplió el uso del script de población para facilitar las pruebas completas de la aplicación.

El script permite crear:

* usuarios;
* profesorado;
* alumnado;
* asignaturas;
* matrículas;
* calificaciones.

De esta forma es posible reconstruir rápidamente un entorno de pruebas funcional tras reiniciar CentroEducativo.

---

## Problemas encontrados durante la integración

Durante el desarrollo aparecieron incidencias relacionadas con:

* obtención de la key;
* sincronización entre Tomcat y CentroEducativo;
* usuarios presentes en Tomcat pero no en CentroEducativo;
* configuración de la URL base;
* despliegue en diferentes entornos.

La centralización de la lógica REST en `CentroEducativoClient` y `AuthFilter` permitió simplificar la resolución de estos problemas.

---

## Situación final

La versión final del proyecto utiliza CentroEducativo como único proveedor de datos académicos. La autenticación REST se encuentra integrada mediante `AuthFilter`, la gestión de sesión se centraliza en `SessionsUtils` y todas las operaciones se realizan a través de `CentroEducativoClient`, permitiendo soportar tanto el flujo completo de alumnado como el de profesorado.


### Observación sobre el Hito 1

En el Hito 1 se priorizan las operaciones de consulta del alumnado. Las operaciones de inserción, actualización o borrado pueden quedar iniciadas o preparadas estructuralmente, pero no constituyen el núcleo prioritario de esta entrega.


