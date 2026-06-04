# Fotografías del alumnado

Esta carpeta contiene las fotos que muestra la aplicación:

- En el **expediente del alumno** (su propia foto).
- En la **ficha de alumno del profesor** (foto del alumno consultado).

## Cómo añadir las fotos

La aplicación busca cada foto por el **DNI** del alumno, con este nombre exacto:

```
/fotos/<DNI>.png
```

Por ejemplo, para el alumno con DNI `12345678W`:

```
src/main/webapp/fotos/12345678W.png
```

### Pasos

1. Abre el `fotos.zip` que viene con el enunciado. Trae dos carpetas
   (`hombres/` y `mujeres/`) con imágenes en PNG.
2. Elige una imagen para cada alumno y **renómbrala con su DNI**
   (p. ej. `23456387R.png`).
3. Cópiala a esta carpeta (`src/main/webapp/fotos/`).
4. Vuelve a desplegar la aplicación.

> El nombre del fichero debe coincidir EXACTAMENTE con el DNI que devuelve
> CentroEducativo (mayúsculas incluidas), porque así es como la app construye
> la URL de la imagen (`fotoUrl(dni)` en `js/api.js`).

## ¿Y si no existe la foto?

Si no hay un `<DNI>.png`, la aplicación muestra automáticamente el avatar
genérico `_placeholder.svg` (sin romper la página). Así que puedes ir
añadiendo fotos poco a poco.

## Sobre el formato base64

El enunciado también entrega las fotos en base64. Aquí se ha optado por el
formato **PNG como fichero estático**, que es más simple y suficiente: el
navegador las cachea y la URL es directa. Si en algún momento quisieras
incrustarlas en base64 (útil para evitar peticiones extra en vistas AJAX),
habría que cambiar `fotoUrl`/`fotoImg` en `js/api.js` para devolver un
`data:image/png;base64,...` en lugar de una URL.
