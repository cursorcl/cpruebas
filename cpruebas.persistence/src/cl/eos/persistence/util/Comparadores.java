package cl.eos.persistence.util;

import java.util.Comparator;

import cl.eos.ot.OTRangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;

public class Comparadores {

    public static Comparator<? super RespuestasEsperadasPrueba> compararRespuestasEsperadas()
    {
        return new Comparator<RespuestasEsperadasPrueba>() {
            @Override
            public int compare(final RespuestasEsperadasPrueba respSource, final RespuestasEsperadasPrueba respTarget)
            {
                return respSource.getNumero().compareTo(respTarget.getNumero());
            }
        };
    }
    
    public static Comparator<? super OTRangoEvaluacion> comparaRangoEvaluacion()
    {
        return new Comparator<OTRangoEvaluacion>() {
            @Override
            public int compare(final OTRangoEvaluacion respSource, final OTRangoEvaluacion respTarget)
            {
                return respSource.getRango().getMinimo().compareTo(respTarget.getRango().getMinimo());
            }
        };
    }
}
