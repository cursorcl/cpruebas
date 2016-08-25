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

    /**
     * Evaluamos solo un alumno. Obtenemos la cantidad de buenas que tiene por
     * cada objetivo asociado a la prueba.
     * 
     * @param pRendida
     *            La prueba del alumno,
     */
    static List<ItemObjetivo> evaluarAlumno(PruebaRendida pRendida) {
        String[] reps = pRendida.getRespuestas().split("");
        List<RespuestasEsperadasPrueba> resEsperadas = pRendida.getEvaluacionPrueba().getPrueba().getRespuestas();
        Map<Objetivo, List<RespuestasEsperadasPrueba>> byObjective = resEsperadas.stream()
                .collect(Collectors.groupingBy(RespuestasEsperadasPrueba::getObjetivo));

        List<ItemObjetivo> result = new ArrayList<>();
        for (Objetivo key : byObjective.keySet()) {
            List<RespuestasEsperadasPrueba> resps = byObjective.get(key);
            ItemObjetivo objxAlumno = new ItemObjetivo.Builder().objetivo(key).build();
            for (RespuestasEsperadasPrueba r : resps) {
                if (reps[r.getNumero() - 1].equalsIgnoreCase(r.getRespuesta()))
                    objxAlumno.addBuena();
                objxAlumno.addPregunta();
            }
            result.add(objxAlumno);
        }

        return result;
    }

    static List<ItemObjetivo> evaluarCurso(List<PruebaRendida> pruebas) {
        List<ItemObjetivo> result = new ArrayList<>();
        for (PruebaRendida p : pruebas) {
            List<ItemObjetivo> rAlumno = evaluarAlumno(p);
            if (result.isEmpty()) {
                result.addAll(rAlumno);
            } else {
                for (ItemObjetivo item : rAlumno) {
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

    static Map<Curso, List<ItemObjetivo>> evaluarColegio(List<PruebaRendida> pruebas) {

        Map<Curso, List<ItemObjetivo>> result = new HashMap<>();

        Map<Curso, List<PruebaRendida>> cursos = pruebas.stream()
                .collect(Collectors.groupingBy(PruebaRendida::getObjCurso));

        cursos.keySet().forEach(p -> result.put(p, evaluarCurso(cursos.get(p))));

        return result;
    }
    
    static Map<TipoCurso, List<ItemObjetivo>> evaluarColegioxNivel(List<PruebaRendida> pruebas) {

        Map<TipoCurso, List<ItemObjetivo>> result = new HashMap<>();

        GroupByTipoCurso group = new GroupByTipoCurso();
        Map<TipoCurso, List<PruebaRendida>> cursos = pruebas.stream().collect(Collectors.groupingBy(group));

        cursos.keySet().forEach(p -> result.put(p, evaluarCurso(cursos.get(p))));
        return result;
    }

    static Map<Colegio, Map<Curso, List<ItemObjetivo>>> evaluarComuna(List<PruebaRendida> pruebas) {

        Map<Colegio, Map<Curso, List<ItemObjetivo>>> resultado = new HashMap<>();
        Map<Colegio, List<PruebaRendida>> colegio = pruebas.stream()
                .collect(Collectors.groupingBy(PruebaRendida::getColegio));

        colegio.keySet().forEach(p -> resultado.put(p, evaluarColegio(colegio.get(p))));

        return resultado;
    }

    static class GroupByTipoCurso implements Function<PruebaRendida, TipoCurso>
    {
        @Override
        public TipoCurso apply(PruebaRendida t) {
            return t.getAlumno().getCurso().getTipoCurso();
        }
        
    }
}
