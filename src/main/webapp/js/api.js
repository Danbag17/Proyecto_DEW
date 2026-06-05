/* Utilidades AJAX para NOL 25/26
   Versión compatible con Eclipse antiguo: sin async/await, sin const/let, sin spread.
   Las páginas llaman a los servlets propios de la app, no a CentroEducativo directamente.
*/

var CTX = '/' + window.location.pathname.split('/')[1] + '/';

var API = {
  logout: CTX + 'LogoutServlet',

  alumnoAsignaturas: CTX + 'AlumnoAsignaturasServlet',

  alumnoDetalle: function (acronimo) {
    return CTX + 'AlumnoDetalleServlet?asig=' + encodeURIComponent(acronimo);
  },

  alumnoExpediente: CTX + 'AlumnoExpedienteServlet',

  profesorAsignaturas: CTX + 'ProfesorAsignaturasServlet',

  profesorAlumnos: function (acronimo) {
    return CTX + 'AsignaturaAlumnosServlet?asig=' + encodeURIComponent(acronimo);
  },

  profesorNota: function (acronimo, dni) {
    return CTX + 'ModificarNotaServlet?asig='
      + encodeURIComponent(acronimo)
      + '&dni='
      + encodeURIComponent(dni);
  }
};

function getAcronimo(asig) {
  return asig.acronimo || asig.asignatura || asig.id || asig.codigo || '';
}

function getNombreAsignatura(asig) {
  return asig.nombre || asig.nombreAsignatura || asig.asignatura || getAcronimo(asig);
}

function safeArray(data) {
  if (Array.isArray(data)) return data;
  if (data && Array.isArray(data.asignaturas)) return data.asignaturas;
  if (data && Array.isArray(data.alumnos)) return data.alumnos;
  if (data && Array.isArray(data.calificaciones)) return data.calificaciones;
  return [];
}

function formatNota(value) {
  if (value === undefined || value === null || value === '') return 'Sin calificar';
  return value;
}
function ajax(url, options) {
  options = options || {};

  var headers = {
    'Accept': 'application/json'
  };

  if (options.body) {
    headers['Content-Type'] = 'application/json';
  }

  var fetchOptions = {
    credentials: 'same-origin',
    headers: headers
  };

  for (var key in options) {
    if (Object.prototype.hasOwnProperty.call(options, key)) {
      fetchOptions[key] = options[key];
    }
  }

  return fetch(url, fetchOptions).then(function (response) {
    if (response.status === 401 || response.status === 403) {
      window.location.href = CTX + 'login.html';
      throw new Error('No autorizado');
    }

    if (!response.ok) {
      var msg;
      switch (response.status) {
        case 400: msg = 'La solicitud no es válida. Revisa los datos e inténtalo de nuevo.'; break;
        case 404: msg = 'No se han encontrado los datos solicitados.'; break;
        case 500: msg = 'Ha ocurrido un error en el servidor. Inténtalo de nuevo más tarde.'; break;
        default:  msg = 'No se ha podido completar la operación (error ' + response.status + ').';
      }
      throw new Error(msg);
    }

    var contentType = response.headers.get('content-type') || '';

    if (contentType.indexOf('application/json') !== -1) {
      return response.json();
    }

    return response.text();
  });
}

function showError(err) {
  var box = document.getElementById('errorBox');

  if (box) {
    box.textContent = err && err.message ? err.message : String(err);
    box.classList.remove('d-none');
  }

  console.error(err);
}

function hideError() {
  var box = document.getElementById('errorBox');

  if (box) {
    box.textContent = '';
    box.classList.add('d-none');
  }
}

function logout() {
  window.location.href = API.logout;
}

function qs(name) {
  return new URLSearchParams(window.location.search).get(name);
}

function setText(id, value) {
  var el = document.getElementById(id);

  if (el) {
    el.textContent = value === undefined || value === null ? '' : value;
  }
}

function getDniAlumno(alumno) {
  return alumno.dni || alumno.dniAlumno || alumno.alumno || '';
}

function getNombreAlumno(alumno) {
  var nombre = alumno.nombre || '';
  var apellidos = alumno.apellidos || '';
  var completo = (nombre + ' ' + apellidos).trim();

  return completo || alumno.nombreCompleto || alumno.alumno || getDniAlumno(alumno);
}

/* ---------- Fotografías del alumnado ----------
   Las fotos son ficheros estáticos en /DEW/fotos/<dni>.png.
   Si no existe la del DNI, se muestra un avatar genérico (_placeholder.svg).
*/
var FOTO_PLACEHOLDER = CTX + 'fotos/_placeholder.svg';

function fotoUrl(dni) {
  if (!dni) return FOTO_PLACEHOLDER;
  return CTX + 'fotos/' + encodeURIComponent(dni) + '.png';
}

/* Devuelve el <img> con fallback al placeholder si la foto no existe. */
function fotoImg(dni, cssClass) {
  var url = fotoUrl(dni);
  return '<img src="' + url + '" alt="Foto" class="' + (cssClass || '') + '"'
    + ' onerror="this.onerror=null;this.src=\'' + FOTO_PLACEHOLDER + '\';">';
}

/* ---------- Cálculo de la nota media ----------
   Acepta notas como "7.8", 7.8 o "" (sin calificar). Ignora las no numéricas.
*/
function parseNotaNum(value) {
  if (value === undefined || value === null) return NaN;
  var s = String(value).replace(',', '.').trim();
  if (s === '') return NaN;
  var n = parseFloat(s);
  return isNaN(n) ? NaN : n;
}

function getNotaAlumno(item) {
  if (item.nota !== undefined && item.nota !== null) return item.nota;
  if (item.calificacion !== undefined) return item.calificacion;
  if (item.calificacionNumerica !== undefined) return item.calificacionNumerica;
  return '';
}

/* Devuelve { media: Number|null, calificados: n, total: m }. */
function calcMedia(items) {
  var suma = 0;
  var n = 0;
  for (var i = 0; i < items.length; i++) {
    var v = parseNotaNum(getNotaAlumno(items[i]));
    if (!isNaN(v)) {
      suma += v;
      n++;
    }
  }
  return {
    media: n ? (suma / n) : null,
    calificados: n,
    total: items.length
  };
}

/* Texto formateado de la media, listo para pintar. */
function textoMedia(items) {
  var r = calcMedia(items);
  if (r.media === null) {
    return 'Sin calificaciones (0 de ' + r.total + ')';
  }
  return r.media.toFixed(2) + ' · ' + r.calificados + ' calificados de ' + r.total;
}
