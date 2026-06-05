package dew.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Utilidades para combinar la matrícula del alumno con el catálogo de asignaturas.
 *
 * El endpoint /alumnos/{dni}/asignaturas devuelve la matrícula (acrónimo + nota),
 * pero NO los datos académicos de la asignatura (créditos, nombre, curso,
 * cuatrimestre). Esos viven en el catálogo /asignaturas. Aquí se cruzan ambos
 * por acrónimo para que las vistas dispongan, entre otras cosas, de los créditos.
 */
public final class AsignaturasUtils {

    private AsignaturasUtils() {
    }

 
    public static String enriquecerConCatalogo(String matriculasJson, String catalogoJson) {

        try {
            return cruzar(matriculasJson, catalogoJson);
        } catch (RuntimeException e) {
            return matriculasJson;
        }
    }

    private static String cruzar(String matriculasJson, String catalogoJson) {

        JsonArray matriculas = JsonParser.parseString(matriculasJson).getAsJsonArray();
        JsonArray catalogo = JsonParser.parseString(catalogoJson).getAsJsonArray();

        Map<String, JsonObject> porAcronimo = new HashMap<>();
        for (int i = 0; i < catalogo.size(); i++) {
            JsonObject asig = catalogo.get(i).getAsJsonObject();
            if (asig.has("acronimo") && !asig.get("acronimo").isJsonNull()) {
                porAcronimo.put(asig.get("acronimo").getAsString().toUpperCase(), asig);
            }
        }

        for (int i = 0; i < matriculas.size(); i++) {
            JsonObject matricula = matriculas.get(i).getAsJsonObject();

            String acr = matricula.has("asignatura") && !matricula.get("asignatura").isJsonNull()
                    ? matricula.get("asignatura").getAsString().toUpperCase()
                    : null;

            JsonObject asig = acr != null ? porAcronimo.get(acr) : null;
            if (asig == null) {
                continue;
            }

            copiarSiFalta(matricula, asig, "creditos");
            copiarSiFalta(matricula, asig, "nombre");
            copiarSiFalta(matricula, asig, "curso");
            copiarSiFalta(matricula, asig, "cuatrimestre");
        }

        return matriculas.toString();
    }

    private static void copiarSiFalta(JsonObject destino, JsonObject origen, String campo) {
        boolean destinoVacio = !destino.has(campo) || destino.get(campo).isJsonNull();
        if (destinoVacio && origen.has(campo) && !origen.get(campo).isJsonNull()) {
            JsonElement valor = origen.get(campo);
            destino.add(campo, valor);
        }
    }
}
