#!/bin/bash

# Configuración básica
API_URL="http://localhost:9090/CentroEducativo"
COOKIE_FILE="cucu"

echo "=== Iniciando población de datos para Hito 1 ==="

# 1. Admin login
echo "Obteniendo llave de acceso..."
KEY=$(curl -s --data '{"dni":"111111111","password":"654321"}' \
-X POST -H "content-type: application/json" \
"$API_URL/login" -c $COOKIE_FILE -b $COOKIE_FILE)

echo "Llave obtenida con éxito: $KEY"
echo "-----------------------------------------------"

# 2. Insertar profesores
echo "Añadiendo profesores..."
curl -s -X POST -H "content-type: application/json" \
-d '{"dni":"22222222P", "nombre":"Ava", "apellidos":"Williams", "password":"123456"}' \
"$API_URL/profesores?key=$KEY" -c $COOKIE_FILE -b $COOKIE_FILE

# 3. Insertar alumnos
echo "Añadiendo alumnos..."
curl -s -X POST -H "content-type: application/json" \
-d '{"dni":"33445566X", "nombre":"John", "apellidos":"Wick", "password":"123456"}' \
"$API_URL/alumnos?key=$KEY" -c $COOKIE_FILE -b $COOKIE_FILE

curl -s -X POST -H "content-type: application/json" \
-d '{"dni":"12345678A", "nombre":"Carlos", "apellidos":"Martínez", "password":"123456"}' \
"$API_URL/alumnos?key=$KEY" -b $COOKIE_FILE

# 4. Insertar Asignatura
echo "Añadiendo asignaturas..."
curl -s -X POST -H "content-type: application/json" \
-d '{"acronimo":"GPR", "nombre":"Gestión de Proyectos", "curso":3, "cuatrimestre":"B", "creditos":6}' \
"$API_URL/asignaturas?key=$KEY" -b $COOKIE_FILE

# 5. Insertar notas
echo "Cambiando a sesión de PROFESOR para poner notas..."

KEY_PROFE=$(curl -s --data '{"dni":"22222222P","password":"123456"}' \
-X POST -H "content-type: application/json" \
"$API_URL/login" -c $COOKIE_FILE -b $COOKIE_FILE)

echo "Vinculando Profesor..."
curl -s -X POST -H "Content-Type: application/json" \
-d '"22222222P"' \
"$API_URL/asignaturas/DEW/profesores?key=$KEY_PROFE" -b $COOKIE_FILE

# 5. Vincular Alumno a Asignatura (Matricular)
# Nota: Si esta ruta falla, prueba cambiar el orden según tu API
echo "Matriculando Alumno..."
curl -s -X POST -H "Content-Type: application/json" \
-d '"33445566X"' \
"$API_URL/asignaturas/DEW/alumnos?key=$KEY_PROFE" -b $COOKIE_FILE

echo "Insertando calificación final..."
curl -s -X PUT -H "content-type: application/json" \
-d '9.5' \
"$API_URL/alumnos/33445566X/asignaturas/DEW?key=$KEY_PROFE" -b $COOKIE_FILE

echo "-----------------------------------------------"
echo "¡Proceso terminado! Base de datos poblada."