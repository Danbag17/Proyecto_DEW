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
      return response.text().then(function (text) {
        throw new Error(response.status + ' ' + response.statusText + ' ' + text);
      });
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
