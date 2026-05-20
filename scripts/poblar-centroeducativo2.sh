localhost#!/usr/bin/env bash
set -euo pipefail

API_URL="${API_URL:-http://172.23.189.79:9090/CentroEducativo}"
COOKIE_FILE="${COOKIE_FILE:-/tmp/nol2526-cookies.txt}"
ADMIN_DNI="111111111"
ADMIN_PASS="654321"

curl_json() {
  local method="$1" url="$2" data="${3:-}"
  if [[ -n "$data" ]]; then
    curl -sS -X "$method" -H "content-type: application/json" -H "accept: application/json" \
      --data "$data" "$url" -c "$COOKIE_FILE" -b "$COOKIE_FILE"
  else
    curl -sS -X "$method" -H "accept: application/json" "$url" -c "$COOKIE_FILE" -b "$COOKIE_FILE"
  fi
}

login() {
  local dni="$1" pass="$2"
  curl_json POST "$API_URL/login" "{\"dni\":\"$dni\",\"password\":\"$pass\"}"
}

require_key() {
  local key="$1"
  if [[ -z "$key" || "$key" == *"error"* || "$key" == *"Exception"* ]]; then
    echo "ERROR: no se pudo obtener key. ¿Está lanzado CentroEducativo en $API_URL?" >&2
    exit 1
  fi
}

post_ignore() {
  local url="$1" data="$2" label="$3"
  echo " -> $label"
  curl_json POST "$url" "$data" >/tmp/nol2526-last-response.txt || true
}

put_ignore() {
  local url="$1" data="$2" label="$3"
  echo " -> $label"
  curl_json PUT "$url" "$data" >/tmp/nol2526-last-response.txt || true
}

echo "=== Poblado NOL 25/26 ==="
rm -f "$COOKIE_FILE"
KEY_ADMIN="$(login "$ADMIN_DNI" "$ADMIN_PASS")"
require_key "$KEY_ADMIN"
echo "Admin autenticado."

# Profesores extra
post_ignore "$API_URL/profesores?key=$KEY_ADMIN" '{"dni":"22222222P","nombre":"Ava","apellidos":"Williams","password":"123456"}' "Profesor Ava Williams"
post_ignore "$API_URL/profesores?key=$KEY_ADMIN" '{"dni":"33333333P","nombre":"Luis","apellidos":"Navarro","password":"123456"}' "Profesor Luis Navarro"

# Alumnos extra
post_ignore "$API_URL/alumnos?key=$KEY_ADMIN" '{"dni":"33445566X","nombre":"John","apellidos":"Wick","password":"123456"}' "Alumno John Wick"
post_ignore "$API_URL/alumnos?key=$KEY_ADMIN" '{"dni":"12345678A","nombre":"Carlos","apellidos":"Martínez","password":"123456"}' "Alumno Carlos Martínez"
post_ignore "$API_URL/alumnos?key=$KEY_ADMIN" '{"dni":"87654321B","nombre":"Eva","apellidos":"Fotografía Ruiz","password":"123456"}' "Alumna Eva Fotografía Ruiz"

# Asignaturas extra
post_ignore "$API_URL/asignaturas?key=$KEY_ADMIN" '{"acronimo":"GPR","nombre":"Gestión de Proyectos","curso":3,"cuatrimestre":"B","creditos":6}' "Asignatura GPR"
post_ignore "$API_URL/asignaturas?key=$KEY_ADMIN" '{"acronimo":"SEG","nombre":"Seguridad Web","curso":4,"cuatrimestre":"B","creditos":4.5}' "Asignatura SEG"

# Vinculaciones profesor-asignatura. Para poner notas hay que usar sesión de profesor que imparta la asignatura.
KEY_PROF_AVA="$(login "22222222P" "123456")"
require_key "$KEY_PROF_AVA"
post_ignore "$API_URL/asignaturas/DEW/profesores?key=$KEY_PROF_AVA" '"22222222P"' "Ava imparte DEW"
post_ignore "$API_URL/asignaturas/GPR/profesores?key=$KEY_PROF_AVA" '"22222222P"' "Ava imparte GPR"
post_ignore "$API_URL/asignaturas/SEG/profesores?key=$KEY_PROF_AVA" '"22222222P"' "Ava imparte SEG"

# Matrículas
post_ignore "$API_URL/asignaturas/DEW/alumnos?key=$KEY_PROF_AVA" '"33445566X"' "John matriculado en DEW"
post_ignore "$API_URL/asignaturas/DEW/alumnos?key=$KEY_PROF_AVA" '"12345678A"' "Carlos matriculado en DEW"
post_ignore "$API_URL/asignaturas/GPR/alumnos?key=$KEY_PROF_AVA" '"33445566X"' "John matriculado en GPR"
post_ignore "$API_URL/asignaturas/GPR/alumnos?key=$KEY_PROF_AVA" '"87654321B"' "Eva matriculada en GPR"
post_ignore "$API_URL/asignaturas/SEG/alumnos?key=$KEY_PROF_AVA" '"12345678A"' "Carlos matriculado en SEG"
post_ignore "$API_URL/asignaturas/SEG/alumnos?key=$KEY_PROF_AVA" '"87654321B"' "Eva matriculada en SEG"

# Notas: endpoint documentado/observado en el enunciado: /alumnos/{dni}/asignaturas/{acr}
put_ignore "$API_URL/alumnos/33445566X/asignaturas/DEW?key=$KEY_PROF_AVA" '9.5' "Nota John DEW"
put_ignore "$API_URL/alumnos/12345678A/asignaturas/DEW?key=$KEY_PROF_AVA" '7.8' "Nota Carlos DEW"
put_ignore "$API_URL/alumnos/33445566X/asignaturas/GPR?key=$KEY_PROF_AVA" '8.7' "Nota John GPR"
put_ignore "$API_URL/alumnos/87654321B/asignaturas/GPR?key=$KEY_PROF_AVA" '9.2' "Nota Eva GPR"
put_ignore "$API_URL/alumnos/12345678A/asignaturas/SEG?key=$KEY_PROF_AVA" '8.1' "Nota Carlos SEG"
put_ignore "$API_URL/alumnos/87654321B/asignaturas/SEG?key=$KEY_PROF_AVA" '9.0' "Nota Eva SEG"

echo "=== Fin. Datos de prueba cargados. ==="
echo "Cookie file: $COOKIE_FILE"
