package ot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoCurso;

public class UtilEvaluations {

    static class GroupByTipoCurso implements Function<PruebaRendida, TipoCurso> {
        @Override
        public TipoCurso apply(PruebaRendida t) {
            return t.getAlumno().getCurso().getTipoCurso();
        }

    }

    /**
     * Evaluamos solo un alumno. Obtenemos la cantidad de buenas que tiene por
     * cada objetivo asociado a la prueba.
     *
     * @param pRendida
     *            La prueba del alumno,
     */
    static List<ItemObjetivo> evaluarAlumno(PruebaRendida pRendida) {
        final String[] reps = pRendida.getRespuestas().split("");
        final List<RespuestasEsperadasPrueba> resEsperadas = pRendida.getEvaluacionPrueba().getPrueba().getRespuestas();
        final Map<Objetivo, List<RespuestasEsperadasPrueba>> byObjective = resEsperadas.stream()
                .collect(Collectors.groupingBy(RespuestasEsperadasPrueba::getObjetivo));

        final List<ItemObjetivo> result = new ArrayList<>();
        for (final Objetivo key : byObjective.keySet()) {
            final List<RespuestasEsperadasPrueba> resps = byObjective.get(key);
            final ItemObjetivo objxAlumno = new ItemObjetivo.Builder().objetivo(key).build();
            for (final RespuestasEsperadasPrueba r : resps) {
                if (reps[r.getNumero() - 1].equalsIgnoreCase(r.getRespuesta()))
                    objxAlumno.addBuena();
                objxAlumno.addPregunta();
            }
            result.add(objxAlumno);
        }

        return result;
    }

    static Map<Curso, List<ItemObjetivo>> evaluarColegio(List<PruebaRendida> pruebas) {

        final Map<Curso, List<ItemObjetivo>> result = new HashMap<>();

        final Map<Curso, List<PruebaRendida>> cursos = pruebas.stream()
                .collect(Collectors.groupingBy(PruebaRendida::getObjCurso));

        cursos.keySet().forEach(p -> result.put(p, UtilEvaluations.evaluarCurso(cursos.get(p))));

        return result;
    }

    static Map<TipoCurso, List<ItemObjetivo>> evaluarColegioxNivel(List<PruebaRendida> pruebas) {

        final Map<TipoCurso, List<ItemObjetivo>> result = new HashMap<>();

        final GroupByTipoCurso group = new GroupByTipoCurso();
        final Map<TipoCurso, List<PruebaRendida>> cursos = pruebas.stream().collect(Collectors.groupingBy(group));

        cursos.keySet().forEach(p -> result.put(p, UtilEvaluations.evaluarCurso(cursos.get(p))));
        return result;
    }

    static Map<Colegio, Map<Curso, List<ItemObjetivo>>> evaluarComuna(List<PruebaRendida> pruebas) {

        final Map<Colegio, Map<Curso, List<ItemObjetivo>>> resultado = new HashMap<>();
        final Map<Colegio, List<PruebaRendida>> colegio = pruebas.stream()
                .collect(Collectors.groupingBy(PruebaRendida::getColegio));

        colegio.keySet().forEach(p -> resultado.put(p, UtilEvaluations.evaluarColegio(colegio.get(p))));

        return resultado;
    }

    static List<ItemObjetivo> evaluarCurso(List<PruebaRendida> pruebas) {
        final List<ItemObjetivo> result = new ArrayList<>();
        for (final PruebaRendida p : pruebas) {
            final List<ItemObjetivo> rAlumno = UtilEvaluations.evaluarAlumno(p);
            if (result.isEmpty()) {
                result.addAll(rAlumno);
            } else {
                for (final ItemObjetivo item : rAlumno) {
                    int idx = -1;
                    if ((idx = result.indexOf(item)) == -1) {
                        result.add(item);
                    } else {
                        result.get(idx).add(item);
                    }
                }
            }
        }
        return result;
    }
}
