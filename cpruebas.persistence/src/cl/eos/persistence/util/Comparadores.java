package cl.eos.persistence.util;

import java.util.Comparator;

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
}
