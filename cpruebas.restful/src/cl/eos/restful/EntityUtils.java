package cl.eos.restful;

import java.util.List;

/**
 * Clase que contiene un conjunto de utilidades que involicran las entidades del sistema.
 */
import cl.eos.restful.tables.R_RangoEvaluacion;

public class EntityUtils {
  public static R_RangoEvaluacion getRango(float porcentaje, List<R_RangoEvaluacion> rangos) {
    R_RangoEvaluacion result = null;
    int nroRangos = rangos.size();
    int n = 0;
    for (R_RangoEvaluacion rango : rangos) {
      if (n == 0) {
        if (porcentaje < rango.getMaximo()) {
          result = rango;
          break;
        }
      } else if (n == nroRangos - 1) {
        if (porcentaje >= rango.getMinimo()) {
          result = rango;
          break;
        }
      } else {
        if (porcentaje >= rango.getMinimo() && porcentaje < rango.getMaximo()) {
          result = rango;
          break;
        }
      }
      n++;
    }
    return result;
  }
}
