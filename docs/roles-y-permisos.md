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
