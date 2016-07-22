package cl.eos.persistence.util;

import java.util.Comparator;

import cl.eos.ot.OTRangoEvaluacion;
import cl.eos.ot.OTResumenColegio;
import cl.eos.ot.OTResumenTipoCursoColegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoCurso;

public class Comparadores {

    public static Comparator<? super RespuestasEsperadasPrueba> compararRespuestasEsperadas() {
        return new Comparator<RespuestasEsperadasPrueba>() {
            @Override
            public int compare(final RespuestasEsperadasPrueba respSource, final RespuestasEsperadasPrueba respTarget) {
                return respSource.getNumero().compareTo(respTarget.getNumero());
            }
        };
    }

    public static Comparator<? super OTRangoEvaluacion> comparaRangoEvaluacion() {
        return new Comparator<OTRangoEvaluacion>() {
            @Override
            public int compare(final OTRangoEvaluacion respSource, final OTRangoEvaluacion respTarget) {
                return respSource.getRango().getMinimo().compareTo(respTarget.getRango().getMinimo());
            }
        };
    }

    public static Comparator<? super OTResumenColegio> comparaResumeColegio() {
        return new Comparator<OTResumenColegio>() {
            @Override
            public int compare(final OTResumenColegio respSource, final OTResumenColegio respTarget) {
                String source = "";
                String target = "";
                if (respSource.getCurso().getName() != null) {
                    source = respSource.getCurso().getName().toUpperCase();
                }
                if (respTarget.getCurso().getName() != null) {
                    target = respTarget.getCurso().getName().toUpperCase();
                }
                return source.compareTo(target);
            }
        };
    }

    public static Comparator<? super OTResumenTipoCursoColegio> comparaResumeTipoCursoColegio() {
        return new Comparator<OTResumenTipoCursoColegio>() {
            @Override
            public int compare(final OTResumenTipoCursoColegio respSource, final OTResumenTipoCursoColegio respTarget) {
                Long source = 0L;
                Long target = 0L;
                if (respSource.getTipoCurso().getId() != null) {
                    source = respSource.getTipoCurso().getId();
                }
                if (respTarget.getTipoCurso().getId() != null) {
                    target = respTarget.getTipoCurso().getId();
                }
                return source.compareTo(target);
            }
        };
    }

    public static Comparator<? super Curso> comparaResumeCurso() {
        return new Comparator<Curso>() {
            @Override
            public int compare(final Curso respSource, final Curso respTarget) {

                String source = "";
                String target = "";
                if (respSource.getName() != null) {
                    source = respSource.getName().toUpperCase();
                }
                if (respTarget.getName() != null) {
                    target = respTarget.getName().toUpperCase();
                }
                return source.compareTo(target);
            }
        };
    }

    public static Comparator<? super TipoCurso> comparaTipoCurso() {
        return new Comparator<TipoCurso>() {
            @Override
            public int compare(final TipoCurso respSource, final TipoCurso respTarget) {

                Long source = 0L;
                Long target = 0L;
                if (respSource.getId() != null) {
                    source = respSource.getId();
                }
                if (respTarget.getId() != null) {
                    target = respTarget.getId();
                }
                return source.compareTo(target);
            }
        };
    }

    public static Comparator<? super EvaluacionEjeTematico> comparaEvaluacionEjeTematico() {
        return new Comparator<EvaluacionEjeTematico>() {
            @Override
            public int compare(final EvaluacionEjeTematico eetSource, final EvaluacionEjeTematico eetTarget) {
                return eetSource.getNroRangoMin().compareTo(eetTarget.getNroRangoMin());
            }
        };
    }
    
    public static Comparator<? super EvaluacionPrueba> comparaEvaluacionPruebaXCurso() {
        return new Comparator<EvaluacionPrueba>() {
            @Override
            public int compare(final EvaluacionPrueba eetSource, final EvaluacionPrueba eetTarget) {
                return eetSource.getCurso().getName().compareTo(eetTarget.getCurso().getName());
            }
        };
    }
}
