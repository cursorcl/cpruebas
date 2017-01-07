package cl.eos.persistence.util;

import java.util.Comparator;

import cl.eos.ot.OTRangoEvaluacion;
import cl.eos.ot.OTResumenColegio;
import cl.eos.ot.OTResumenTipoCursoColegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoCurso;

public class Comparadores {

    public static Comparator<? super R_EvaluacionEjetematico> comparaEvaluacionEjeTematico() {
        return (eetSource, eetTarget) -> (int)Math.abs((eetSource.getNrorangomin() - eetTarget.getNrorangomin()));
    }

    public static Comparator<? super R_EvaluacionPrueba> comparaEvaluacionPruebaXCurso() {
        return (eetSource, eetTarget) -> (int)(eetSource.getCurso_id()  - eetTarget.getCurso_id());
    }

    public static Comparator<? super OTRangoEvaluacion> comparaRangoEvaluacion() {
        return (respSource, respTarget) -> (int)Math.abs(respSource.getRango().getMinimo() - respTarget.getRango().getMinimo());
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

    public static Comparator<? super R_Curso> comparaResumeCurso() {
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

    public static Comparator<? super R_RespuestasEsperadasPrueba> compararRespuestasEsperadas() {
        return (respSource, respTarget) -> respSource.getNumero().compareTo(respTarget.getNumero());
    }

    public static Comparator<? super R_TipoCurso> comparaTipoCurso() {
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

    public static Comparator<? super R_Curso> odenarCurso() {
        return (cursoSource, cursoTarget) -> {

            String source = "";
            String target = "";
            if (cursoSource.getName() != null) {
                source = cursoSource.getName().toUpperCase();
            }
            if (cursoTarget.getName() != null) {
                target = cursoTarget.getName().toUpperCase();
            }
            final int result = (int) (cursoSource.getTipocurso_id() - cursoTarget.getTipocurso_id());
            return result == 0 ? source.compareTo(target) : result;
        };
    }

    public static Comparator<? super R_RangoEvaluacion> rangoEvaluacionComparator() {
        return (respSource, respTarget) -> (int)Math.abs(respSource.getMinimo() - respTarget.getMinimo());
    }
}
