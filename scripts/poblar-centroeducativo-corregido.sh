#!/usr/bin/env bash
set -u

# ============================================================
# Script de población para CentroEducativo - NOL 25/26
# ============================================================
# Uso normal:
#   chmod +x poblar-centroeducativo-corregido.sh
#   ./poblar-centroeducativo-corregido.sh
#
# Si necesitas cambiar la IP/URL:
#   API_URL="http://IP:9090/CentroEducativo" ./poblar-centroeducativo-corregido.sh
# ============================================================

API_URL="${API_URL:-http://172.23.189.79:9090/CentroEducativo}"

COOKIE_ADMIN="${COOKIE_ADMIN:-/tmp/nol2526-admin-cookies.txt}"
COOKIE_PROF_AVA="${COOKIE_PROF_AVA:-/tmp/nol2526-prof-ava-cookies.txt}"
COOKIE_PROF_RAMON="${COOKIE_PROF_RAMON:-/tmp/nol2526-prof-ramon-cookies.txt}"

ADMIN_DNI="111111111"
ADMIN_PASS="654321"

LAST_RESPONSE="/tmp/nol2526-last-response.txt"

# ------------------------------------------------------------
# Utilidades
# ------------------------------------------------------------

separator() {
  echo "------------------------------------------------------------"
}

curl_json_with_cookie() {
  local cookie_file="$1"
  local method="$2"
  local url="$3"
  local data="${4:-}"

  if [[ -n "$data" ]]; then
    curl -sS -X "$method" \
      -H "content-type: application/json" \
      -H "accept: application/json" \
      --data "$data" \
      "$url" \
      -c "$cookie_file" -b "$cookie_file"
  else
    curl -sS -X "$method" \
      -H "accept: application/json" \
      "$url" \
      -c "$cookie_file" -b "$cookie_file"
  fi
}

login_with_cookie() {
  local dni="$1"
  local pass="$2"
  local cookie_file="$3"

  rm -f "$cookie_file"

  curl_json_with_cookie \
    "$cookie_file" \
    POST \
    "$API_URL/login" \
    "{\"dni\":\"$dni\",\"password\":\"$pass\"}"
}

require_key() {
  local key="$1"
  local label="$2"

  if [[ -z "$key" ]]; then
    echo "ERROR: no se pudo obtener key para $label. Respuesta vacía." >&2
    exit 1
  fi

  if [[ "$key" == *"error"* || "$key" == *"Error"* || "$key" == *"Exception"* || "$key" == *"not logged"* || "$key" == *"Not logged"* || "$key" == *"<html"* ]]; then
    echo "ERROR: la respuesta de login para $label no parece una key válida:" >&2
    echo "$key" >&2
    exit 1
  fi
}

request_ignore_error() {
  local cookie_file="$1"
  local method="$2"
  local url="$3"
  local data="$4"
  local label="$5"

  echo " -> $label"

  local response
  response="$(curl_json_with_cookie "$cookie_file" "$method" "$url" "$data" 2>&1)"
  local status=$?

  echo "$response" > "$LAST_RESPONSE"

  if [[ $status -ne 0 ]]; then
    echo "    AVISO: curl falló en '$label'. Respuesta guardada en $LAST_RESPONSE" >&2
    return 0
  fi

  # No paramos el script porque algunos POST pueden fallar si el dato ya existe.
  # Pero sí avisamos si parece un fallo real.
  if [[ "$response" == *"not logged"* || "$response" == *"User is not logged"* || "$response" == *"error"* || "$response" == *"Exception"* ]]; then
    echo "    AVISO: posible error en '$label': $response" >&2
  fi

  return 0
}

post_ignore() {
  request_ignore_error "$1" POST "$2" "$3" "$4"
}

put_ignore() {
  request_ignore_error "$1" PUT "$2" "$3" "$4"
}

get_check() {
  local cookie_file="$1"
  local url="$2"
  local label="$3"

  echo " -> Comprobando: $label"
  local response
  response="$(curl_json_with_cookie "$cookie_file" GET "$url" "" 2>&1)"
  echo "$response" > "$LAST_RESPONSE"

  if [[ "$response" == *"not logged"* || "$response" == *"User is not logged"* ]]; then
    echo "ERROR: $label devuelve 'User is not logged'. La cookie/key no corresponden a la misma sesión." >&2
    echo "Respuesta: $response" >&2
    exit 1
  fi

  echo "$response"
}

# ------------------------------------------------------------
# Inicio
# ------------------------------------------------------------

echo "=== Poblado NOL 25/26 ==="
echo "API_URL: $API_URL"
separator

rm -f "$COOKIE_ADMIN" "$COOKIE_PROF_AVA" "$COOKIE_PROF_RAMON" "$LAST_RESPONSE"

# 1. Login administrador
KEY_ADMIN="$(login_with_cookie "$ADMIN_DNI" "$ADMIN_PASS" "$COOKIE_ADMIN")"
require_key "$KEY_ADMIN" "admin"
echo "Admin autenticado."

# Verificación rápida con la misma cookie del admin.
get_check "$COOKIE_ADMIN" "$API_URL/asignaturas?key=$KEY_ADMIN" "asignaturas como admin" >/dev/null

separator

echo "Creando datos base con sesión admin..."

# Profesores extra
post_ignore "$COOKIE_ADMIN" "$API_URL/profesores?key=$KEY_ADMIN" '{"dni":"22222222P","nombre":"Ava","apellidos":"Williams","password":"123456"}' "Profesor Ava Williams"
post_ignore "$COOKIE_ADMIN" "$API_URL/profesores?key=$KEY_ADMIN" '{"dni":"33333333P","nombre":"Luis","apellidos":"Navarro","password":"123456"}' "Profesor Luis Navarro"

# Alumnos extra
post_ignore "$COOKIE_ADMIN" "$API_URL/alumnos?key=$KEY_ADMIN" '{"dni":"33445566X","nombre":"John","apellidos":"Wick","password":"123456"}' "Alumno John Wick"
post_ignore "$COOKIE_ADMIN" "$API_URL/alumnos?key=$KEY_ADMIN" '{"dni":"12345678A","nombre":"Carlos","apellidos":"Martínez","password":"123456"}' "Alumno Carlos Martínez"
post_ignore "$COOKIE_ADMIN" "$API_URL/alumnos?key=$KEY_ADMIN" '{"dni":"87654321B","nombre":"Eva","apellidos":"Fotografía Ruiz","password":"123456"}' "Alumna Eva Fotografía Ruiz"

# Asignaturas extra
post_ignore "$COOKIE_ADMIN" "$API_URL/asignaturas?key=$KEY_ADMIN" '{"acronimo":"GPR","nombre":"Gestión de Proyectos","curso":3,"cuatrimestre":"B","creditos":6}' "Asignatura GPR"
post_ignore "$COOKIE_ADMIN" "$API_URL/asignaturas?key=$KEY_ADMIN" '{"acronimo":"SEG","nombre":"Seguridad Web","curso":4,"cuatrimestre":"B","creditos":4.5}' "Asignatura SEG"

separator

echo "Creando sesiones de profesor separadas..."

# 2. Login profesor Ava con cookie propia.
KEY_PROF_AVA="$(login_with_cookie "22222222P" "123456" "$COOKIE_PROF_AVA")"
require_key "$KEY_PROF_AVA" "profesora Ava"
echo "Profesora Ava autenticada."

# Verificación rápida con la misma cookie de Ava.
get_check "$COOKIE_PROF_AVA" "$API_URL/asignaturas?key=$KEY_PROF_AVA" "asignaturas como Ava" >/dev/null

separator

echo "Vinculando profesor, matrículas y notas con sesión de profesor..."

# Vinculaciones profesor-asignatura.
# Nota: si la API exige admin para asignar profesores, estas tres líneas podrían requerir COOKIE_ADMIN/KEY_ADMIN.
# Si ves avisos aquí, revisa Swagger y cambia COOKIE_PROF_AVA/KEY_PROF_AVA por COOKIE_ADMIN/KEY_ADMIN solo en estas rutas.
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/DEW/profesores?key=$KEY_PROF_AVA" '"22222222P"' "Ava imparte DEW"
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/GPR/profesores?key=$KEY_PROF_AVA" '"22222222P"' "Ava imparte GPR"
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/SEG/profesores?key=$KEY_PROF_AVA" '"22222222P"' "Ava imparte SEG"

# Matrículas
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/DEW/alumnos?key=$KEY_PROF_AVA" '"33445566X"' "John matriculado en DEW"
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/DEW/alumnos?key=$KEY_PROF_AVA" '"12345678A"' "Carlos matriculado en DEW"
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/GPR/alumnos?key=$KEY_PROF_AVA" '"33445566X"' "John matriculado en GPR"
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/GPR/alumnos?key=$KEY_PROF_AVA" '"87654321B"' "Eva matriculada en GPR"
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/SEG/alumnos?key=$KEY_PROF_AVA" '"12345678A"' "Carlos matriculado en SEG"
post_ignore "$COOKIE_PROF_AVA" "$API_URL/asignaturas/SEG/alumnos?key=$KEY_PROF_AVA" '"87654321B"' "Eva matriculada en SEG"

# Notas
put_ignore "$COOKIE_PROF_AVA" "$API_URL/alumnos/33445566X/asignaturas/DEW?key=$KEY_PROF_AVA" '9.5' "Nota John DEW"
put_ignore "$COOKIE_PROF_AVA" "$API_URL/alumnos/12345678A/asignaturas/DEW?key=$KEY_PROF_AVA" '7.8' "Nota Carlos DEW"
put_ignore "$COOKIE_PROF_AVA" "$API_URL/alumnos/33445566X/asignaturas/GPR?key=$KEY_PROF_AVA" '8.7' "Nota John GPR"
put_ignore "$COOKIE_PROF_AVA" "$API_URL/alumnos/87654321B/asignaturas/GPR?key=$KEY_PROF_AVA" '9.2' "Nota Eva GPR"
put_ignore "$COOKIE_PROF_AVA" "$API_URL/alumnos/12345678A/asignaturas/SEG?key=$KEY_PROF_AVA" '8.1' "Nota Carlos SEG"
put_ignore "$COOKIE_PROF_AVA" "$API_URL/alumnos/87654321B/asignaturas/SEG?key=$KEY_PROF_AVA" '9.0' "Nota Eva SEG"

separator

echo "Comprobaciones finales:"
echo "Admin -> alumnos y asignaturas:"
get_check "$COOKIE_ADMIN" "$API_URL/alumnosyasignaturas?key=$KEY_ADMIN" "alumnosyasignaturas como admin" | head -c 1000
echo
echo

echo "Ava -> alumnos de DEW:"
get_check "$COOKIE_PROF_AVA" "$API_URL/asignaturas/DEW/alumnos?key=$KEY_PROF_AVA" "alumnos de DEW como Ava" | head -c 1000
echo

separator

echo "=== Fin. Datos de prueba cargados o ya existentes. ==="
echo "Cookie admin: $COOKIE_ADMIN"
echo "Cookie profesora Ava: $COOKIE_PROF_AVA"
echo "Última respuesta: $LAST_RESPONSE"
