#!/usr/bin/env bash
set -u

# ============================================================
# Script de población COMPLETA para CentroEducativo - NOL 25/26
# ============================================================
# Llena el backend con un volumen amplio de datos de prueba:
#   - Profesores
#   - Alumnos
#   - Asignaturas (varios cursos y cuatrimestres)
#   - Vinculaciones profesor <-> asignatura (sesión admin)
#   - Matrículas alumno <-> asignatura (sesión del profesor que la imparte)
#   - Notas (sesión del profesor que la imparte)
#
# Uso:
#   chmod +x scripts/poblar-centroeducativo-completo.sh
#   ./scripts/poblar-centroeducativo-completo.sh
#
# Para cambiar la IP/URL:
#   API_URL="http://IP:9090/CentroEducativo" ./scripts/poblar-centroeducativo-completo.sh
#
# Notas de diseño:
#   - El script es idempotente "best effort": si un dato ya existe, el POST
#     puede fallar y simplemente se avisa, sin abortar.
#   - Para matricular y poner notas se usa SIEMPRE la sesión del profesor que
#     imparte la asignatura, porque el backend exige ese rol para esas operaciones.
#   - La asignatura "DEW" suele venir precreada en el backend; igualmente se
#     intenta crear (se ignora el error si ya existe).
# ============================================================

API_URL="${API_URL:-http://localhost:9090/CentroEducativo}"

ADMIN_DNI="111111111"
ADMIN_PASS="654321"

COOKIE_DIR="${COOKIE_DIR:-/tmp/nol2526-cookies}"
COOKIE_ADMIN="$COOKIE_DIR/admin.txt"
LAST_RESPONSE="$COOKIE_DIR/last-response.txt"

# Contraseña por defecto para profesores y alumnos creados.
DEFAULT_PASS="123456"

mkdir -p "$COOKIE_DIR"
rm -f "$COOKIE_DIR"/*.txt

# ------------------------------------------------------------
# Datos a poblar
# ------------------------------------------------------------

# Profesores: dni|nombre|apellidos
PROFESORES=(
  "22222222P|Ava|Williams"
  "33333333P|Luis|Navarro"
  "44444444R|Marta|Sánchez"
  "55555555S|Carlos|Gómez"
  "66666666T|Lucía|Fernández"
  "77777777W|David|Ramírez"
)

# Alumnos: dni|nombre|apellidos
ALUMNOS=(
  "33445566X|John|Wick"
  "12345678A|Carlos|Martínez"
  "87654321B|Eva|Ruiz"
  "11223344C|Laura|Gómez"
  "22334455D|Pedro|Díaz"
  "33445567E|Sara|López"
  "44556677F|Hugo|Moreno"
  "55667788G|Marta|Jiménez"
  "66778899H|Iván|Torres"
  "77889900J|Nerea|Castro"
  "88990011K|Adrián|Vega"
  "99001122L|Paula|Ortega"
  "10111213M|Diego|Romero"
  "12131415N|Alba|Gil"
  "14151617P|Mario|Serrano"
  "16171819Q|Claudia|Núñez"
)

# Asignaturas: acronimo|nombre|curso|cuatrimestre|creditos
ASIGNATURAS=(
  "DEW|Desarrollo de Entornos Web|3|B|6"
  "GPR|Gestión de Proyectos|3|B|6"
  "SEG|Seguridad Web|4|B|4.5"
  "PRG|Programación|1|A|9"
  "BDA|Bases de Datos|2|A|6"
  "ISW|Ingeniería del Software|2|B|6"
  "IAR|Inteligencia Artificial|4|A|6"
  "RED|Redes de Computadores|2|A|6"
  "SOP|Sistemas Operativos|1|B|6"
  "MAT|Matemáticas|1|A|6"
)

# Asignatura -> profesor que la imparte: acronimo|dniProfesor
IMPARTE=(
  "DEW|22222222P"
  "GPR|22222222P"
  "SEG|22222222P"
  "PRG|33333333P"
  "BDA|33333333P"
  "ISW|44444444R"
  "IAR|44444444R"
  "RED|55555555S"
  "SOP|55555555S"
  "MAT|66666666T"
)

# Matrículas + notas: alumno|asignatura|nota
# Usa "-" como nota para matricular sin calificar todavía.
MATRICULAS=(
  # DEW
  "33445566X|DEW|9.5"
  "12345678A|DEW|7.8"
  "87654321B|DEW|6.4"
  "11223344C|DEW|8.2"
  "22334455D|DEW|-"
  # GPR
  "33445566X|GPR|8.7"
  "87654321B|GPR|9.2"
  "44556677F|GPR|5.5"
  "55667788G|GPR|7.1"
  # SEG
  "12345678A|SEG|8.1"
  "87654321B|SEG|9.0"
  "66778899H|SEG|4.3"
  "77889900J|SEG|-"
  # PRG
  "11223344C|PRG|6.0"
  "22334455D|PRG|7.5"
  "33445567E|PRG|9.8"
  "44556677F|PRG|8.4"
  "55667788G|PRG|5.0"
  "10111213M|PRG|3.2"
  # BDA
  "33445567E|BDA|7.7"
  "44556677F|BDA|6.6"
  "66778899H|BDA|8.9"
  "88990011K|BDA|-"
  # ISW
  "55667788G|ISW|7.0"
  "66778899H|ISW|8.5"
  "99001122L|ISW|6.1"
  "12131415N|ISW|9.4"
  # IAR
  "77889900J|IAR|9.1"
  "88990011K|IAR|7.3"
  "14151617P|IAR|8.0"
  "16171819Q|IAR|-"
  # RED
  "99001122L|RED|5.8"
  "10111213M|RED|6.9"
  "12131415N|RED|7.6"
  # SOP
  "10111213M|SOP|4.0"
  "14151617P|SOP|8.8"
  "16171819Q|SOP|9.3"
  # MAT
  "33445566X|MAT|6.5"
  "11223344C|MAT|7.2"
  "12131415N|MAT|8.1"
  "14151617P|MAT|9.0"
  "16171819Q|MAT|5.4"
)

# ------------------------------------------------------------
# Utilidades
# ------------------------------------------------------------

sep() { echo "------------------------------------------------------------"; }

# curl_json COOKIE METHOD URL [DATA]
curl_json() {
  local cookie="$1" method="$2" url="$3" data="${4:-}"
  if [[ -n "$data" ]]; then
    curl -sS -X "$method" \
      -H "content-type: application/json" \
      -H "accept: application/json, text/plain, */*" \
      --data "$data" "$url" \
      -c "$cookie" -b "$cookie"
  else
    curl -sS -X "$method" \
      -H "accept: application/json, text/plain, */*" \
      "$url" \
      -c "$cookie" -b "$cookie"
  fi
}

# login DNI PASS COOKIE  -> imprime la key
login() {
  local dni="$1" pass="$2" cookie="$3"
  rm -f "$cookie"
  curl -sS -X POST \
    -H "content-type: application/json" \
    -H "accept: text/plain, */*" \
    --data "{\"dni\":\"$dni\",\"password\":\"$pass\"}" \
    "$API_URL/login" \
    -c "$cookie" -b "$cookie"
}

require_key() {
  local key="$1" label="$2"
  if [[ -z "$key" \
        || "$key" == *"error"* || "$key" == *"Error"* \
        || "$key" == *"Exception"* || "$key" == *"timestamp"* \
        || "$key" == *"Not Acceptable"* || "$key" == *"not logged"* \
        || "$key" == *"<html"* ]]; then
    echo "ERROR: la key de '$label' no es válida:" >&2
    echo "$key" >&2
    exit 1
  fi
}

# request COOKIE METHOD URL DATA LABEL
# IMPORTANTE sobre el body:
#   - Los endpoints cuyo cuerpo es "string" (asignar profesor, matricular) esperan
#     el valor como TEXTO PLANO sin comillas JSON: 22222222P  (NO "22222222P").
#   - El endpoint de nota espera un número crudo: 9.5
request() {
  local cookie="$1" method="$2" url="$3" data="$4" label="$5"
  local resp
  resp="$(curl_json "$cookie" "$method" "$url" "$data" 2>&1)"
  echo "$resp" > "$LAST_RESPONSE"
  local trimmed="${resp//[$'\t\r\n ']/}"
  if [[ "$resp" == *"not logged"* || "$resp" == *"User is not logged"* \
        || "$resp" == *"Exception"* || "$resp" == *"Not Acceptable"* \
        || "$resp" == *"Not Allowed"* || "$trimmed" == "Error" ]]; then
    echo "   -> $label : FALLO ($resp)" >&2
  else
    echo "   -> $label : ${resp:-OK}"
  fi
}

# ------------------------------------------------------------
# 1. Login admin
# ------------------------------------------------------------

echo "=== Poblado COMPLETO NOL 25/26 ==="
echo "API_URL: $API_URL"
sep

KEY_ADMIN="$(login "$ADMIN_DNI" "$ADMIN_PASS" "$COOKIE_ADMIN")"
require_key "$KEY_ADMIN" "admin"
echo "Admin autenticado."
sep

# ------------------------------------------------------------
# 2. Crear profesores, alumnos y asignaturas (sesión admin)
# ------------------------------------------------------------

echo "Creando profesores..."
for row in "${PROFESORES[@]}"; do
  IFS='|' read -r dni nombre apellidos <<< "$row"
  request "$COOKIE_ADMIN" POST "$API_URL/profesores?key=$KEY_ADMIN" \
    "{\"dni\":\"$dni\",\"nombre\":\"$nombre\",\"apellidos\":\"$apellidos\",\"password\":\"$DEFAULT_PASS\"}" \
    "Profesor $nombre $apellidos ($dni)"
done
sep

echo "Creando alumnos..."
for row in "${ALUMNOS[@]}"; do
  IFS='|' read -r dni nombre apellidos <<< "$row"
  request "$COOKIE_ADMIN" POST "$API_URL/alumnos?key=$KEY_ADMIN" \
    "{\"dni\":\"$dni\",\"nombre\":\"$nombre\",\"apellidos\":\"$apellidos\",\"password\":\"$DEFAULT_PASS\"}" \
    "Alumno $nombre $apellidos ($dni)"
done
sep

echo "Creando asignaturas..."
for row in "${ASIGNATURAS[@]}"; do
  IFS='|' read -r acr nombre curso cuatri creditos <<< "$row"
  request "$COOKIE_ADMIN" POST "$API_URL/asignaturas?key=$KEY_ADMIN" \
    "{\"acronimo\":\"$acr\",\"nombre\":\"$nombre\",\"curso\":$curso,\"cuatrimestre\":\"$cuatri\",\"creditos\":$creditos}" \
    "Asignatura $acr - $nombre"
done
sep

# ------------------------------------------------------------
# 3. Vincular profesores a asignaturas (sesión admin)
#    y construir el mapa asignatura -> profesor.
# ------------------------------------------------------------

declare -A ASIG_PROF   # acronimo -> dni del profesor que la imparte

echo "Vinculando profesores a asignaturas..."
for row in "${IMPARTE[@]}"; do
  IFS='|' read -r acr dni <<< "$row"
  ASIG_PROF["$acr"]="$dni"
  request "$COOKIE_ADMIN" POST "$API_URL/asignaturas/$acr/profesores?key=$KEY_ADMIN" \
    "$dni" \
    "Profesor $dni imparte $acr"
done
sep

# ------------------------------------------------------------
# 4. Login de cada profesor (cookie y key propias)
# ------------------------------------------------------------

declare -A PROF_KEY     # dni -> key
declare -A PROF_COOKIE  # dni -> fichero cookie

echo "Autenticando profesores..."
for row in "${PROFESORES[@]}"; do
  IFS='|' read -r dni _ _ <<< "$row"
  cookie="$COOKIE_DIR/prof-$dni.txt"
  key="$(login "$dni" "$DEFAULT_PASS" "$cookie")"
  require_key "$key" "profesor $dni"
  PROF_KEY["$dni"]="$key"
  PROF_COOKIE["$dni"]="$cookie"
  echo "   -> Profesor $dni autenticado."
done
sep

# ------------------------------------------------------------
# 5. Matrículas (sesión admin) + notas (sesión del profesor que imparte)
# ------------------------------------------------------------

echo "Matriculando alumnos y poniendo notas..."
for row in "${MATRICULAS[@]}"; do
  IFS='|' read -r alumno acr nota <<< "$row"

  prof="${ASIG_PROF[$acr]:-}"
  if [[ -z "$prof" ]]; then
    echo "   AVISO: la asignatura '$acr' no tiene profesor asignado. Se omite $alumno." >&2
    continue
  fi

  cookie="${PROF_COOKIE[$prof]}"
  key="${PROF_KEY[$prof]}"

  # Matrícula: la realiza el ADMIN (el profesor no tiene permiso). Body texto plano.
  request "$COOKIE_ADMIN" POST "$API_URL/asignaturas/$acr/alumnos?key=$KEY_ADMIN" \
    "$alumno" \
    "Matrícula $alumno en $acr"

  # Nota: la pone el PROFESOR que imparte la asignatura. Body número crudo.
  if [[ "$nota" != "-" ]]; then
    request "$cookie" PUT "$API_URL/alumnos/$alumno/asignaturas/$acr?key=$key" \
      "$nota" \
      "Nota $alumno en $acr = $nota"
  fi
done
sep

# ------------------------------------------------------------
# 6. Comprobaciones finales
# ------------------------------------------------------------

echo "Comprobaciones finales:"
echo "Admin -> alumnos y asignaturas (primeros 800 chars):"
curl_json "$COOKIE_ADMIN" GET "$API_URL/alumnosyasignaturas?key=$KEY_ADMIN" "" | head -c 800
echo
echo
echo "Profesor Ava -> alumnos de DEW (primeros 800 chars):"
curl_json "${PROF_COOKIE[22222222P]}" GET "$API_URL/asignaturas/DEW/alumnos?key=${PROF_KEY[22222222P]}" "" | head -c 800
echo
sep

echo "=== Fin. Backend poblado. ==="
echo "Profesores: ${#PROFESORES[@]} | Alumnos: ${#ALUMNOS[@]} | Asignaturas: ${#ASIGNATURAS[@]} | Matrículas: ${#MATRICULAS[@]}"
echo "Contraseña de profesores/alumnos: $DEFAULT_PASS"
echo "Cookies en: $COOKIE_DIR"
