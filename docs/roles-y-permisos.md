# roles-y-permisos.md

## Roles y permisos del sistema

### 1. Roles definidos

El proyecto define dos roles principales:

| Rol | Perfil |
|---|---|
| `rolalu` | Alumnado |
| `rolpro` | Profesorado |

Estos roles condicionan tanto las vistas accesibles como las operaciones que cada usuario puede realizar dentro de la aplicación.

---

### 2. Rol de alumnado (`rolalu`)

#### Permisos

| Operación | Permitida |
|---|---|
| Autenticarse en la aplicación | ✅ |
| Consultar sus asignaturas | ✅ |
| Consultar la nota de una asignatura | ✅ |
| Consultar su expediente académico | ✅ |
| Generar certificado de notas | ✅ |
| Cerrar sesión | ✅ |

#### Restricciones

- No puede modificar calificaciones
- No puede acceder a rutas del profesorado
- No puede consultar información de otros usuarios

---

### 3. Rol de profesorado (`rolpro`)

#### Permisos *(previstos para fases posteriores al Hito 1)*

| Operación | Permitida |
|---|---|
| Autenticarse en la aplicación | ✅ |
| Consultar asignaturas impartidas | ✅ |
| Consultar alumnos de una asignatura | ✅ |
| Consultar y modificar notas de alumnos | ✅ |
| Cerrar sesión | ✅ |

#### Restricciones

- No puede acceder a rutas del alumnado
- No puede modificar información fuera de su ámbito funcional

---

### 4. Aplicación de la seguridad

La seguridad se apoya en dos niveles complementarios:

#### 4.1. Seguridad declarativa (`web.xml`)

Configurada mediante:

- Definición de roles con `<security-role>`
- Restricciones de acceso por rutas con `<security-constraint>`
- Configuración del mecanismo de autenticación con `<login-config>`

#### 4.2. Seguridad programática (servlets)

Aplicada dentro de los servlets cuando sea necesario comprobar condiciones adicionales no cubiertas por la configuración declarativa. Por ejemplo: verificar que la sesión contiene una `key` válida antes de consultar CentroEducativo.

---

### 5. Rutas asociadas a cada rol

| Rol | Rutas |
|---|---|
| `rolalu` | `/alumno/asignaturas`, `/alumno/detalle`, `/alumno/expediente` |
| `rolpro` | `/profesor/asignaturas`, `/profesor/alumnos`, `/profesor/modificar-nota` |
| Común | `/logout` |

---

### 6. Datos de sesión previstos

Para la integración con CentroEducativo, se almacenan en sesión los siguientes atributos mediante `SessionsUtils`:

| Atributo | Descripción |
|---|---|
| `dni` | Identificador del usuario autenticado |
| `password` | Contraseña del usuario |
| `key` | Clave de sesión devuelta por CentroEducativo |

---

### 7. Criterio general

Cada usuario solo puede ver y operar sobre la información que le corresponda según su rol. Ninguna operación queda expuesta sin control de acceso previo.

---

### 8. Observación sobre el Hito 1

En el Hito 1 se prioriza el flujo funcional del alumnado. La parte del profesorado queda definida a nivel de diseño y configuración, pero su implementación funcional se abordará en fases posteriores.


---

# Evolución tras el Hito 1

## Implementación completa del rol de profesorado

Durante el Hito 1 el rol de profesorado se encontraba definido a nivel de diseño y configuración, pero gran parte de sus funcionalidades todavía no estaban implementadas.

Durante la fase final del proyecto se completó el flujo completo del profesorado.

Las funcionalidades finalmente implementadas son:

| Operación                                | Estado final |
| ---------------------------------------- | ------------ |
| Consultar asignaturas impartidas         | Implementada |
| Consultar alumnos matriculados           | Implementada |
| Consultar ficha de alumno                | Implementada |
| Visualizar fotografía asociada al alumno | Implementada |
| Modificar notas                          | Implementada |
| Consultar nota media de la asignatura    | Implementada |
| Logout                                   | Implementado |

---

## Separación real de permisos

La versión final distingue claramente entre operaciones de consulta y operaciones de modificación.

### Alumno

El alumnado únicamente dispone de permisos de lectura.

Puede:

* consultar asignaturas;
* consultar notas;
* consultar expediente;
* imprimir certificado;
* visualizar información propia.

No puede:

* modificar notas;
* consultar datos de otros alumnos;
* acceder a recursos del profesorado.

---

### Profesor

El profesorado dispone de permisos de consulta y modificación sobre los recursos académicos que gestiona.

Puede:

* consultar asignaturas impartidas;
* consultar alumnado matriculado;
* visualizar fichas académicas;
* modificar calificaciones;
* consultar estadísticas básicas de la asignatura.

No puede:

* acceder a funcionalidades exclusivas del alumnado;
* modificar recursos fuera de las asignaturas que gestiona.

---

## Protección declarativa mediante web.xml

Durante la implementación final se mantuvo la estrategia de seguridad declarativa exigida por el enunciado.

Las restricciones principales continúan siendo:

```text
/alumno/*
```

accesible únicamente por:

```text
rolalu
```

y

```text
/profesor/*
```

accesible únicamente por:

```text
rolpro
```

Esta separación impide el acceso directo a recursos mediante URL.

---

## Protección de endpoints AJAX

Una decisión importante durante el desarrollo fue proteger también los endpoints AJAX.

Aunque un usuario no vea un botón en pantalla, podría intentar acceder directamente a un servlet mediante una petición manual.

Por este motivo los servlets utilizados para:

* listado de alumnos;
* modificación de notas;
* consultas dinámicas;

se mantienen dentro de rutas protegidas por rol.

Esto garantiza que la seguridad no dependa únicamente de la interfaz gráfica.

---

## Integración con AuthFilter

La seguridad final no depende exclusivamente de Tomcat.

La incorporación de:

```text
dew.filters.AuthFilter
```

añade una segunda capa de validación.

El flujo real es:

```text
Usuario
   ↓
Tomcat valida credenciales
   ↓
Comprobación de rol
   ↓
AuthFilter
   ↓
Obtención de key de CentroEducativo
   ↓
Creación de sesión NOL
   ↓
Acceso a funcionalidades
```

Esto permite que todas las operaciones académicas se realicen utilizando una sesión válida de CentroEducativo.

---

## Seguridad programática

Además de las restricciones declarativas definidas en web.xml, varios servlets realizan comprobaciones adicionales.

Entre ellas:

* validación de sesión activa;
* comprobación de existencia de key;
* verificación de parámetros obligatorios;
* control de errores en llamadas REST.

Estas comprobaciones reducen la posibilidad de accesos inconsistentes o peticiones incompletas.

---

## Gestión de sesión y permisos

La sesión almacena:

```text
dni
password
key
rol
```

Estos datos permiten:

* identificar al usuario autenticado;
* determinar qué vistas puede utilizar;
* autenticar llamadas contra CentroEducativo.

La gestión se centraliza mediante:

```text
dew.util.SessionsUtils
```

para evitar inconsistencias entre servlets.

---

## Logout y revocación de permisos

Tras el cierre de sesión:

* se invalida la sesión HTTP;
* se eliminan los atributos de usuario;
* se elimina la key de CentroEducativo;
* se impide reutilizar recursos protegidos.

Además se incorporaron medidas para minimizar problemas derivados de la caché del navegador.

---

## Situación final

La versión final del proyecto dispone de un sistema de seguridad basado en varias capas:

1. autenticación FORM de Tomcat;
2. control de roles declarativo;
3. validación mediante AuthFilter;
4. sesión propia de NOL;
5. autenticación REST contra CentroEducativo;
6. comprobaciones programáticas adicionales.

Esta combinación permite separar correctamente alumnado y profesorado y proteger tanto las vistas tradicionales como las operaciones AJAX.

