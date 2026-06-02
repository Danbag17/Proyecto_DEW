Acta 07 – Resolución de incidencias finales, estabilización del proyecto y preparación de la entrega definitiva

Asignatura: Desarrollo Web (DEW) – Curso 2025/2026
Grupo: G14 – 3TI21
Reunión nº: 7
Fecha: 30/05/2026 – 02/06/2026
Hora: Coordinación continua mediante WhatsApp, GitHub y Eclipse
Lugar: Trabajo remoto colaborativo
Secretaria: Vanesa Carolina Castro Bello

1. Participantes
Nombre y apellidos	Grupo
Vanesa Carolina Castro Bello	3TI21
Mikel Escudero Aramburu	3TI21
Carlos Moldes Peña	3TI21
Pau Oroval González	3TI21
Michal Pojnar	3TI21
Daniel Zanon Barney	3TI21
2. Orden del día
Resolución de incidencias detectadas durante las pruebas finales.
Problemas de autenticación y redirecciones.
Revisión de la configuración de CentroEducativo.
Corrección del sistema de notas.
Gestión de sesiones, cookies y logout.
Incorporación de páginas de error.
Revisión del script de poblado.
Estado final del proyecto y planificación del cierre.
3. Desarrollo de la reunión
3.1. Inicio de la fase de pruebas finales

Tras la integración de la mayor parte de los componentes desarrollados durante las semanas anteriores, el grupo inició una fase intensiva de pruebas funcionales con el objetivo de detectar errores antes de la entrega definitiva.

Durante estas pruebas comenzaron a aparecer incidencias que no habían sido detectadas previamente durante el desarrollo individual de los distintos módulos.

La mayoría de estos problemas estaban relacionados con:

autenticación
redirecciones
configuración de entornos
gestión de sesiones
comportamiento del navegador
integración con CentroEducativo

Se acordó dedicar esta fase exclusivamente a estabilizar el proyecto y corregir errores, evitando introducir nuevas funcionalidades que pudieran comprometer la estabilidad alcanzada.

3.2. Problemas detectados durante el login

Uno de los primeros errores comunicados por Michal Pojnar se produjo durante el proceso de autenticación.

Aunque el usuario conseguía autenticarse correctamente, tras introducir las credenciales la aplicación realizaba una redirección a rutas que no existían dentro del proyecto.

En concreto, algunos usuarios eran enviados a:

/alumno

en lugar de a las vistas académicas previstas.

Esto provocaba errores de navegación e impedía continuar utilizando la aplicación con normalidad.

Tras revisar la situación, el grupo identificó varias posibles causas:

configuración incorrecta de rutas
errores en web.xml
discrepancias entre servlets y páginas HTML
configuraciones locales diferentes entre los equipos de desarrollo

Se acordó revisar conjuntamente la configuración de autenticación y los mapeos de URL para garantizar una navegación consistente.

3.3. Revisión de CentroEducativoClient y configuración de entornos

Durante el análisis de los errores de autenticación se detectó también que algunos problemas estaban relacionados con la configuración de la URL utilizada para acceder a CentroEducativo.

Se observó que distintos integrantes estaban ejecutando el backend utilizando configuraciones diferentes.

Por este motivo se revisó la configuración del cliente REST:

CentroEducativoClient.java

y se recordó la necesidad de adaptar correctamente la dirección utilizada para acceder al backend según el entorno de cada desarrollador.

Se acordó documentar esta configuración para evitar futuras incidencias derivadas de diferencias entre equipos.

Asimismo, se comprobó que algunos errores desaparecían cuando la aplicación utilizaba la dirección correcta del servicio REST.

3.4. Revisión de usuarios y autenticación de Tomcat

Durante las pruebas también se revisó la configuración de:

tomcat-users.xml

ya que algunos usuarios seguían utilizando versiones antiguas del archivo o cuentas que ya no coincidían con las utilizadas por el resto del grupo.

Se acordó mantener sincronizada esta configuración y utilizar únicamente los usuarios de prueba consensuados durante las sesiones anteriores.

La correcta actualización de estos usuarios permitió eliminar varias incidencias relacionadas con accesos aparentemente erróneos.

3.5. Corrección del sistema de notas

Uno de los objetivos prioritarios de esta fase consistió en resolver definitivamente los problemas relacionados con las calificaciones académicas.

Carlos Moldes Peña comunicó que las modificaciones realizadas sobre la gestión de notas habían permitido estabilizar completamente esta parte de la aplicación.

Las pruebas realizadas mostraron resultados satisfactorios y las consultas académicas comenzaron a devolver la información esperada.

El grupo considera este avance especialmente importante, ya que las calificaciones constituyen uno de los elementos centrales del flujo funcional del alumnado.

3.6. Problemas relacionados con caché y sesiones

A medida que avanzaban las pruebas comenzaron a aparecer comportamientos aparentemente inconsistentes durante los cambios de usuario.

En determinadas circunstancias, tras cerrar sesión e intentar acceder con otro usuario, el navegador parecía conservar información perteneciente a la sesión anterior.

Inicialmente se sospechó de errores en la invalidación de sesiones, pero tras diversas pruebas se observó que parte del problema estaba relacionado con mecanismos de caché del navegador.

Durante esta fase se realizaron múltiples pruebas utilizando:

ventanas privadas
herramientas de desarrollador
eliminación de cookies
desactivación temporal de caché

Estas pruebas permitieron aislar progresivamente el origen del problema y facilitaron la búsqueda de una solución definitiva.

3.7. Corrección del proceso de logout

Como continuación del análisis anterior, Carlos Moldes Peña trabajó específicamente sobre el mecanismo de cierre de sesión.

Tras diversas pruebas se consiguió identificar el origen principal del problema y se implementaron modificaciones destinadas a:

invalidar correctamente la sesión
limpiar información residual
evitar reutilización accidental de sesiones anteriores
mejorar la experiencia de cambio de usuario

Hasta disponer de una solución completamente estable, el grupo documentó un procedimiento temporal que permitía utilizar la aplicación sin incidencias importantes durante las demostraciones y pruebas funcionales.

Posteriormente se confirmó que la corrección implementada resolvía satisfactoriamente gran parte de los problemas observados.

3.8. Incorporación de páginas de error

Durante esta fase también se integró el trabajo realizado por Michal Pojnar relacionado con el tratamiento de errores.

Se añadieron:

páginas de error personalizadas
configuración adicional en web.xml
mecanismos centralizados de gestión de errores

El objetivo de esta mejora fue proporcionar una experiencia más controlada cuando se produjesen incidencias dentro de la aplicación.

Asimismo, esta incorporación contribuyó a mejorar la calidad general del proyecto y facilitar futuras tareas de mantenimiento.

3.9. Integración en la rama principal

A medida que las correcciones iban siendo completadas, los cambios fueron integrándose progresivamente en la rama principal del repositorio.

Durante esta fase se insistió nuevamente en la importancia de:

realizar pull antes de modificar código
verificar el estado local de la rama
evitar sobrescribir cambios de otros integrantes
coordinar las integraciones importantes

La correcta aplicación de estas medidas permitió reducir significativamente los conflictos observados en sesiones anteriores.

3.10. Revisión del script de poblado

Durante la revisión final también se analizaron los scripts utilizados para poblar CentroEducativo.

Se detectó que algunas ejecuciones no estaban generando correctamente:

usuarios
asignaturas
relaciones académicas

Esto provocaba resultados inconsistentes durante las pruebas funcionales.

Por este motivo se acordó revisar:

endpoints utilizados
secuencia de ejecución
parámetros enviados
estado final de la base de datos tras el poblado

El grupo considera esta tarea necesaria para garantizar que las demostraciones y pruebas finales reflejen correctamente el funcionamiento esperado del sistema.

3.11. Funcionalidades adicionales detectadas

Durante el análisis de la API disponible se observó la aparición de nuevas operaciones relacionadas con:

eliminación de alumnos
eliminación de profesores
eliminación de relaciones académicas

Aunque estas funcionalidades resultaron interesantes para futuras ampliaciones del proyecto, el grupo concluyó que no debían convertirse en una prioridad inmediata debido a la cercanía de la fecha de entrega.

Se acordó centrar los esfuerzos restantes exclusivamente en garantizar la estabilidad y completitud de las funcionalidades principales.

3.12. Valoración general del estado del proyecto

Como cierre de esta fase, el grupo realizó una valoración global del estado alcanzado.

Se concluyó que:

la autenticación se encuentra funcional
la integración REST está ampliamente avanzada
las consultas académicas principales funcionan correctamente
los errores más críticos han sido identificados y corregidos
la documentación se encuentra muy avanzada
la estructura del proyecto es estable

Las tareas restantes se concentran principalmente en:

pruebas finales
validación funcional completa
revisión de scripts
pequeños ajustes de integración

El grupo considera que el proyecto se encuentra en una situación favorable para afrontar la entrega definitiva.

4. Acuerdos adoptados
Mantener como prioridad la corrección de errores frente al desarrollo de nuevas funcionalidades.
Revisar la configuración de CentroEducativo en todos los entornos de desarrollo.
Mantener actualizada la configuración de usuarios de Tomcat.
Consolidar la solución aplicada al sistema de logout.
Incorporar definitivamente las páginas de error al proyecto principal.
Revisar y validar los scripts de poblado.
Completar las pruebas funcionales restantes.
Continuar documentando las incidencias detectadas y sus soluciones.
Preparar una versión estable y validada para la entrega.
5. Próximos pasos
finalizar pruebas funcionales
validar navegación completa
revisar scripts de poblado
verificar usuarios y asignaturas creados
comprobar funcionamiento de sesiones
completar documentación técnica
preparar entrega definitiva
6. Validación del acta

El acta ha sido revisada y aceptada por los integrantes participantes en la coordinación.

Nombre	Firma	Fecha
Vanesa Carolina Castro Bello		
Mikel Escudero Aramburu		
Carlos Moldes Peña		
Pau Oroval González		
Michal Pojnar		
Daniel Zanon Barney		

Acta redactada por Vanesa Carolina Castro Bello – Secretaria del Grupo G14
