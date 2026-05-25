#!/usr/bin/env bash
set -euo pipefail

API_URL="${API_URL:-http://172.23.189.79:9090/CentroEducativo}"
COOKIE_ADMIN="${COOKIE_ADMIN:-/tmp/nol2526-admin-cookies.txt}"
COOKIE_PROF_AVA="${COOKIE_PROF_AVA:-/tmp/nol2526-prof-ava-cookies.txt}"
ADMIN_DNI="111111111"
ADMIN_PASS="654321"

LAST_RESPONSE="/tmp/nol2526-last-response.txt"

curl_json() {
  local method="$1"
  local url="$2"
  local cookie_file="$3"
  local data="${4:-}"

  if [[ -n "$data" ]]; then
    curl -sS -X "$method" \
      -H "content-type: application/json" \
      -H "accept: application/json, text/plain, */*" \
      --data "$data" \
      "$url" \
      -c "$cookie_file" -b "$cookie_file"
  else
    curl -sS -X "$method" \
      -H "accept: application/json, text/plain, */*" \
      "$url" \
      -c "$cookie_file" -b "$cookie_file"
  fi
}

login() {
  local dni="$1"
  local pass="$2"
  local cookie_file="$3"

  # IMPORTANTE:
  # /login devuelve texto plano con la key, no JSON.
  # Si mandamos solo Accept: application/json, Spring puede responder 406 Not Acceptable.
  curl -sS -X POST \
    -H "content-type: application/json" \
    -H "accept: text/plain, */*" \
    --data "{\"dni\":\"$dni\",\"password\":\"$pass\"}" \
    "$API_URL/login" \
    -c "$cookie_file" -b "$cookie_file"
}

require_key() {
  local key="$1"
  local label="$2"

  if [[ -z "$key" \
        || "$key" == *"error"* \
        || "$key" == *"Error"* \
        || "$key" == *"Exception"* \
        || "$key" == *"timestamp"* \
        || "$key" == *"status"* \
        || "$key" == *"Not Acceptable"* \
        || "$key" == *"User is not logged"* \
        || "$key" == *"<html"* ]]; then
    echo "ERROR: la respuesta de login para $label no parece una key válida:" >&2
    echo "$key" >&2
    exit 1
  fi
}

post_ignore() {
  local url="$1"
  local data="$2"
  local label="$3"
  local cookie_file="$4"

  echo " -> $label"
  if ! curl_json POST "$url" "$cookie_file" "$data" > "$LAST_RESPONSE"; then
    echo "    AVISO: fallo curl en $label. Respuesta guardada en $LAST_RESPONSE" >&2
    return 0
  fi

  if grep -qiE "User is not logged|Not Acceptable|Exception|error|timestamp" "$LAST_RESPONSE"; then
    echo "    AVISO: posible error en $label:" >&2
    sed 's/^/    /' "$LAST_RESPONSE" >&2
  fi
}

put_ignore() {
  local url="$1"
  local data="$2"
  local label="$3"
  local cookie_file="$4"

  echo " -> $label"
  if ! curl_json PUT "$url" "$cookie_file" "$data" > "$LAST_RESPONSE"; then
    echo "    AVISO: fallo curl en $label. Respuesta guardada en $LAST_RESPONSE" >&2
    return 0
  fi

  if grep -qiE "User is not logged|Not Acceptable|Exception|error|timestamp" "$LAST_RESPONSE"; then
    echo "    AVISO: posible error en $label:" >&2
    sed 's/^/    /' "$LAST_RESPONSE" >&2
  fi
}

echo "=== Poblado NOL 25/26 ==="
echo "API_URL: $API_URL"
echo "------------------------------------------------------------"

rm -f "$COOKIE_ADMIN" "$COOKIE_PROF_AVA" "$LAST_RESPONSE"

KEY_ADMIN="$(login "$ADMIN_DNI" "$ADMIN_PASS" "$COOKIE_ADMIN")"
require_key "$KEY_ADMIN" "admin"
echo "Admin autenticado. KEY_ADMIN=$KEY_ADMIN"

# Profesores extra. Requiere sesión/cookie de admin.
post_ignore "$API_URL/profesores?key=$KEY_ADMIN" \
  '{"dni":"22222222P","nombre":"Ava","apellidos":"Williams","password":"123456"}' \
  "Profesor Ava Williams" "$COOKIE_ADMIN"

post_ignore "$API_URL/profesores?key=$KEY_ADMIN" \
  '{"dni":"33333333P","nombre":"Luis","apellidos":"Navarro","password":"123456"}' \
  "Profesor Luis Navarro" "$COOKIE_ADMIN"

# Alumnos extra. Requiere sesión/cookie de admin.
post_ignore "$API_URL/alumnos?key=$KEY_ADMIN" \
  '{"dni":"33445566X","nombre":"John","apellidos":"Wick","password":"123456"}' \
  "Alumno John Wick" "$COOKIE_ADMIN"

post_ignore "$API_URL/alumnos?key=$KEY_ADMIN" \
  '{"dni":"12345678A","nombre":"Carlos","apellidos":"Martínez","password":"123456"}' \
  "Alumno Carlos Martínez" "$COOKIE_ADMIN"

post_ignore "$API_URL/alumnos?key=$KEY_ADMIN" \
  '{"dni":"87654321B","nombre":"Eva","apellidos":"Fotografía Ruiz","password":"123456"}' \
  "Alumna Eva Fotografía Ruiz" "$COOKIE_ADMIN"

# Asignaturas extra. Requiere sesión/cookie de admin.
post_ignore "$API_URL/asignaturas?key=$KEY_ADMIN" \
  '{"acronimo":"GPR","nombre":"Gestión de Proyectos","curso":3,"cuatrimestre":"B","creditos":6}' \
  "Asignatura GPR" "$COOKIE_ADMIN"

post_ignore "$API_URL/asignaturas?key=$KEY_ADMIN" \
  '{"acronimo":"SEG","nombre":"Seguridad Web","curso":4,"cuatrimestre":"B","creditos":4.5}' \
  "Asignatura SEG" "$COOKIE_ADMIN"

# Login profesor en cookie separada. No pisa la sesión/cookie admin.
KEY_PROF_AVA="$(login "22222222P" "123456" "$COOKIE_PROF_AVA")"
require_key "$KEY_PROF_AVA" "profesor Ava"
echo "Profesor Ava autenticado. KEY_PROF_AVA=$KEY_PROF_AVA"

# Vinculaciones profesor-asignatura.
# Si alguna ruta falla, revisar endpoint exacto en Swagger.
post_ignore "$API_URL/asignaturas/DEW/profesores?key=$KEY_ADMIN" \
  '"22222222P"' \
  "Ava imparte DEW" "$COOKIE_ADMIN"

post_ignore "$API_URL/asignaturas/GPR/profesores?key=$KEY_ADMIN" \
  '"22222222P"' \
  "Ava imparte GPR" "$COOKIE_ADMIN"

post_ignore "$API_URL/asignaturas/SEG/profesores?key=$KEY_ADMIN" \
  '"22222222P"' \
  "Ava imparte SEG" "$COOKIE_ADMIN"

# Matrículas.
# Normalmente estas operaciones requieren que el usuario sea profesor de la asignatura.
post_ignore "$API_URL/asignaturas/DEW/alumnos?key=$KEY_PROF_AVA" \
  '"33445566X"' \
  "John matriculado en DEW" "$COOKIE_PROF_AVA"

post_ignore "$API_URL/asignaturas/DEW/alumnos?key=$KEY_PROF_AVA" \
  '"12345678A"' \
  "Carlos matriculado en DEW" "$COOKIE_PROF_AVA"

post_ignore "$API_URL/asignaturas/GPR/alumnos?key=$KEY_PROF_AVA" \
  '"33445566X"' \
  "John matriculado en GPR" "$COOKIE_PROF_AVA"

post_ignore "$API_URL/asignaturas/GPR/alumnos?key=$KEY_PROF_AVA" \
  '"87654321B"' \
  "Eva matriculada en GPR" "$COOKIE_PROF_AVA"

post_ignore "$API_URL/asignaturas/SEG/alumnos?key=$KEY_PROF_AVA" \
  '"12345678A"' \
  "Carlos matriculado en SEG" "$COOKIE_PROF_AVA"

post_ignore "$API_URL/asignaturas/SEG/alumnos?key=$KEY_PROF_AVA" \
  '"87654321B"' \
  "Eva matriculada en SEG" "$COOKIE_PROF_AVA"

# Notas.
# Endpoint usado según lo que habíamos asumido: /alumnos/{dni}/asignaturas/{acr}
# Si Swagger muestra otro formato de body, se cambia aquí.
put_ignore "$API_URL/alumnos/33445566X/asignaturas/DEW?key=$KEY_PROF_AVA" \
  '9.5' \
  "Nota John DEW" "$COOKIE_PROF_AVA"

put_ignore "$API_URL/alumnos/12345678A/asignaturas/DEW?key=$KEY_PROF_AVA" \
  '7.8' \
  "Nota Carlos DEW" "$COOKIE_PROF_AVA"

put_ignore "$API_URL/alumnos/33445566X/asignaturas/GPR?key=$KEY_PROF_AVA" \
  '8.7' \
  "Nota John GPR" "$COOKIE_PROF_AVA"

put_ignore "$API_URL/alumnos/87654321B/asignaturas/GPR?key=$KEY_PROF_AVA" \
  '9.2' \
  "Nota Eva GPR" "$COOKIE_PROF_AVA"

put_ignore "$API_URL/alumnos/12345678A/asignaturas/SEG?key=$KEY_PROF_AVA" \
  '8.1' \
  "Nota Carlos SEG" "$COOKIE_PROF_AVA"

put_ignore "$API_URL/alumnos/87654321B/asignaturas/SEG?key=$KEY_PROF_AVA" \
  '9.0' \
  "Nota Eva SEG" "$COOKIE_PROF_AVA"

echo "------------------------------------------------------------"
echo "=== Fin. Datos de prueba cargados o intentados. ==="
echo "Cookie admin: $COOKIE_ADMIN"
echo "Cookie profesor Ava: $COOKIE_PROF_AVA"
echo "Última respuesta: $LAST_RESPONSE"
