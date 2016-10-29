package cl.eos.persistence.util;

import java.util.Comparator;

import cl.eos.ot.OTRangoEvaluacion;
import cl.eos.ot.OTResumenColegio;
import cl.eos.ot.OTResumenTipoCursoColegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoCurso;

public class Comparadores {

    public static Comparator<? super EvaluacionEjeTematico> comparaEvaluacionEjeTematico() {
        return (eetSource, eetTarget) -> eetSource.getNroRangoMin().compareTo(eetTarget.getNroRangoMin());
    }

    public static Comparator<? super EvaluacionPrueba> comparaEvaluacionPruebaXCurso() {
        return (eetSource, eetTarget) -> eetSource.getCurso().getName().compareTo(eetTarget.getCurso().getName());
    }

    public static Comparator<? super OTRangoEvaluacion> comparaRangoEvaluacion() {
        return (respSource, respTarget) -> respSource.getRango().getMinimo()
                .compareTo(respTarget.getRango().getMinimo());
    }

    public static Comparator<? super OTResumenColegio> comparaResumeColegio() {
        return (respSource, respTarget) -> {
            String source = "";
            String target = "";
            if (respSource.getCurso().getName() != null) {
                source = respSource.getCurso().getName().toUpperCase();
            }
            if (respTarget.getCurso().getName() != null) {
                target = respTarget.getCurso().getName().toUpperCase();
            }
            return source.compareTo(target);
        };
    }

    public static Comparator<? super Curso> comparaResumeCurso() {
        return (respSource, respTarget) -> {

            String source = "";
            String target = "";
            if (respSource.getName() != null) {
                source = respSource.getName().toUpperCase();
            }
            if (respTarget.getName() != null) {
                target = respTarget.getName().toUpperCase();
            }
            return source.compareTo(target);
        };
    }

    public static Comparator<? super OTResumenTipoCursoColegio> comparaResumeTipoCursoColegio() {
        return (respSource, respTarget) -> {
            Long source = 0L;
            Long target = 0L;
            if (respSource.getTipoCurso().getId() != null) {
                source = respSource.getTipoCurso().getId();
            }
            if (respTarget.getTipoCurso().getId() != null) {
                target = respTarget.getTipoCurso().getId();
            }
            return source.compareTo(target);
        };
    }

    public static Comparator<? super RespuestasEsperadasPrueba> compararRespuestasEsperadas() {
        return (respSource, respTarget) -> respSource.getNumero().compareTo(respTarget.getNumero());
    }

    public static Comparator<? super TipoCurso> comparaTipoCurso() {
        return (respSource, respTarget) -> {

            Long source = 0L;
            Long target = 0L;
            if (respSource.getId() != null) {
                source = respSource.getId();
            }
            if (respTarget.getId() != null) {
                target = respTarget.getId();
            }
            return source.compareTo(target);
        };
    }

    public static Comparator<? super Curso> odenarCurso() {
        return (cursoSource, cursoTarget) -> {

            String source = "";
            String target = "";
            if (cursoSource.getName() != null) {
                source = cursoSource.getName().toUpperCase();
            }
            if (cursoTarget.getName() != null) {
                target = cursoTarget.getName().toUpperCase();
            }
            final int result = cursoSource.getTipoCurso().getId().compareTo(cursoTarget.getTipoCurso().getId());
            return result == 0 ? source.compareTo(target) : result;
        };
    }

    public static Comparator<? super RangoEvaluacion> rangoEvaluacionComparator() {
        return (respSource, respTarget) -> respSource.getMinimo().compareTo(respTarget.getMinimo());
    }
}
