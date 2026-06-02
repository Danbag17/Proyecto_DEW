Acta 06 – Integración final, validación funcional y preparación de la entrega del Hito 1

Asignatura: Desarrollo Web (DEW) – Curso 2025/2026
Grupo: G14 – 3TI21
Reunión nº: 6
Fecha: 25/05/2026
Hora: 19:00 h
Lugar: Coordinación remota mediante WhatsApp, GitHub y Eclipse
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
Revisión del estado global del proyecto.
Integración final de los desarrollos realizados por cada integrante.
Validación de los requisitos mínimos exigidos para el Hito 1.
Revisión de documentación técnica y memoria.
Coordinación de ramas e integración en GitHub.
Detección de funcionalidades pendientes.
Planificación de la fase final previa a la entrega.
3. Desarrollo de la reunión
3.1. Revisión general del estado del proyecto

Durante esta sesión el grupo realizó una revisión completa del estado funcional del proyecto con el objetivo de determinar el nivel real de cumplimiento de los requisitos exigidos para el Hito 1.

Tras analizar las distintas ramas y los componentes desarrollados por cada integrante, se concluyó que el proyecto había alcanzado un grado de madurez considerable respecto a las primeras fases de desarrollo.

Se confirmó la disponibilidad de:

autenticación mediante Tomcat
estructura de navegación del alumnado
integración básica con CentroEducativo
gestión de sesiones
sistema de logs
cliente REST operativo
documentación técnica avanzada
estructura web definitiva

Asimismo, se constató que la mayor parte de los elementos arquitectónicos previstos inicialmente ya se encontraban implementados o preparados para su integración.

Por este motivo, el grupo acordó que la prioridad de esta fase debía centrarse en consolidar el funcionamiento global de la aplicación y no en ampliar el alcance funcional inicialmente previsto.

3.2. Integración de componentes desarrollados por los integrantes

Uno de los principales objetivos de la sesión fue coordinar la integración de los desarrollos realizados por los distintos miembros del equipo.

Se revisó el estado de:

servlets del alumnado
servlets relacionados con profesorado
utilidades de sesión
filtros
cliente REST
vistas HTML y CSS
documentación técnica

Durante esta revisión se verificó que los componentes comenzaban a trabajar de forma conjunta y que el proyecto empezaba a comportarse como una aplicación integrada en lugar de un conjunto de módulos independientes.

No obstante, se identificaron todavía pequeños problemas derivados de diferencias entre versiones locales, por lo que se acordó continuar realizando pruebas conjuntas antes de considerar cerrada la integración.

3.3. Integración del frontend y recursos visuales

Se revisó especialmente la situación del frontend desarrollado durante las semanas anteriores.

Debido a las dificultades que habían surgido anteriormente con la sincronización del repositorio, parte de los recursos desarrollados por Pau Oroval González habían sido distribuidos inicialmente mediante archivos comprimidos compartidos a través de WhatsApp.

Durante esta fase se verificó la correcta incorporación de dichos recursos al proyecto principal y se comprobó que las páginas HTML ya podían integrarse con los servlets y flujos de navegación existentes.

Asimismo, se revisó el uso de Bootstrap mediante CDN y la estructura de estilos CSS utilizada para mantener una apariencia homogénea en toda la aplicación.

El grupo considera que la parte visual dispone ya de una base suficiente para satisfacer los requisitos del Hito 1.

3.4. Revisión del alcance real del Hito 1

Uno de los aspectos más debatidos durante la sesión fue la interpretación práctica de los requisitos exigidos para la entrega.

Durante las pruebas y revisiones realizadas se observó que algunas funcionalidades avanzadas previstas inicialmente podrían requerir un esfuerzo considerable para alcanzar un nivel completamente estable antes de la fecha límite.

Por este motivo, se revisó nuevamente el alcance funcional considerado imprescindible para la primera entrega.

El grupo concluyó que los elementos prioritarios eran:

autenticación funcional
gestión correcta de sesiones
integración con CentroEducativo
consultas académicas básicas
navegación operativa
sistema de logs
documentación técnica

Se acordó que determinadas funcionalidades secundarias o ampliaciones futuras podrían quedar preparadas estructuralmente sin necesidad de encontrarse completamente desarrolladas siempre que el flujo principal funcionase correctamente.

Esta decisión permitió centrar los esfuerzos del equipo en garantizar la estabilidad de los elementos considerados críticos.

3.5. Revisión de la documentación técnica

Durante esta fase se revisó también el estado de la documentación almacenada en la carpeta:

docs/

Vanesa Carolina Castro Bello presentó el estado actualizado de los documentos técnicos desarrollados hasta el momento.

Entre ellos:

memoria
decisiones iniciales
roles y permisos
operaciones de CentroEducativo
logs
sesiones
configuración
actas de seguimiento

Asimismo, se acordó continuar ampliando la documentación para incluir información más detallada sobre:

servlets
filtros
métodos principales
estructura interna del proyecto
configuración de logs
integración REST

El grupo considera que una documentación completa facilitará tanto la evaluación académica como el mantenimiento futuro del proyecto.

3.6. Coordinación mediante GitHub

A medida que se acercaba la fecha de entrega aumentó el número de integraciones realizadas sobre la rama principal del proyecto.

Por este motivo se revisó nuevamente el flujo de trabajo recomendado para evitar conflictos y pérdidas de información.

Se recordó a todos los integrantes la necesidad de:

realizar pull antes de comenzar cualquier modificación
comprobar el estado de la rama local
trabajar sobre ramas individuales siempre que sea posible
realizar commit frecuentes
integrar los cambios de forma progresiva

Durante la sesión se analizaron varios conflictos de integración surgidos en días anteriores y se acordó mantener una coordinación más estrecha para evitar modificaciones simultáneas sobre archivos especialmente sensibles.

3.7. Funcionalidades pendientes y problemas detectados

Aunque el estado general del proyecto fue considerado positivo, se identificaron todavía varias áreas que requerían atención antes de la entrega.

Entre ellas:

validación definitiva de consultas REST
comprobación de datos de prueba
revisión del poblado de CentroEducativo
pruebas de navegación completas
integración final de algunos componentes
revisión de configuraciones específicas

Asimismo, se detectó que determinadas pruebas seguían dependiendo de la correcta disponibilidad de datos académicos dentro de CentroEducativo.

Por ello se acordó continuar verificando el entorno de pruebas y garantizar que todos los datos necesarios estuvieran disponibles para la demostración funcional.

3.8. Planificación de la fase final

Como cierre de la sesión se realizó una planificación específica para los últimos días previos a la entrega.

Se acordó dividir el trabajo restante en tres bloques principales:

Estabilización técnica
autenticación
consultas REST
navegación
sesiones
Validación
pruebas funcionales
pruebas de integración
revisión de errores
Documentación
actualización de memoria
revisión de actas
ampliación de documentación técnica

El grupo coincidió en que el proyecto se encontraba ya muy próximo a una versión entregable y que el esfuerzo restante debía centrarse principalmente en garantizar estabilidad y coherencia.

4. Acuerdos adoptados
Priorizar la estabilidad del proyecto frente a nuevas funcionalidades.
Mantener el flujo principal del alumnado como objetivo prioritario.
Continuar verificando la integración con CentroEducativo.
Revisar el poblado de datos antes de la entrega.
Mantener actualizada la documentación técnica.
Seguir utilizando GitHub como repositorio central del proyecto.
Reforzar la coordinación entre ramas para evitar conflictos.
Completar las pruebas funcionales pendientes.
Preparar una versión estable para la entrega definitiva.
5. Próximos pasos
finalizar integración completa
validar navegación alumno/profesor
revisar datos académicos
comprobar funcionamiento REST
completar documentación técnica
corregir incidencias detectadas
realizar pruebas finales
preparar entrega del Hito 1
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
