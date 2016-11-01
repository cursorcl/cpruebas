package ot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoCurso;

public class UtilEvaluations {

    private static final Logger log = Logger.getLogger(UtilEvaluations.class.getName());

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

        final List<RespuestasEsperadasPrueba> resEsperadas = pRendida.getEvaluacionPrueba().getPrueba().getRespuestas();

        String respuestas = pRendida.getRespuestas();
        if (respuestas.length() < resEsperadas.size()) {
            respuestas = Strings.padEnd(respuestas, resEsperadas.size(), 'O');
        }
        final String[] reps = respuestas.split("");

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

        final Map<Curso, List<PruebaRendida>> mapCursos = pruebas.stream()
                .collect(Collectors.groupingBy(PruebaRendida::getObjCurso));

        for (Curso curso : mapCursos.keySet()) {
            log.fine("Evaluando el curso:" + curso.getName());
            List<PruebaRendida> pRendidas = mapCursos.get(curso);
            if (pRendidas != null && !pRendidas.isEmpty()) {
                log.fine("Tiene:" + pRendidas.size() + " pruebas rendidas.");
                List<ItemObjetivo> r = UtilEvaluations.evaluarCurso(pRendidas);
                log.fine("Se obtienen:" + r.size() + " ItemObjectivo.");
                result.put(curso, r);
            }
        }
        return result;
    }

    static Map<TipoCurso, List<ItemObjetivo>> evaluarColegioxNivel(List<PruebaRendida> pruebas) {

        final Map<TipoCurso, List<ItemObjetivo>> result = new HashMap<>();
        final Map<Curso, List<PruebaRendida>> mapCursos = pruebas.stream()
                .collect(Collectors.groupingBy(PruebaRendida::getObjCurso));

        for (Curso curso : mapCursos.keySet()) {
            log.fine("Evaluando el curso:" + curso.getName() + " del tipo curso:" + curso.getTipoCurso().getName());
            List<PruebaRendida> pRendidas = mapCursos.get(curso);
            if (pRendidas != null && !pRendidas.isEmpty()) {
                log.fine("Tiene:" + pRendidas.size() + " pruebas rendidas.");
                List<ItemObjetivo> r = UtilEvaluations.evaluarCurso(pRendidas);
                log.fine("Se obtienen:" + r.size() + " ItemObjectivo.");
                if (result.containsKey(curso.getTipoCurso())) {
                    List<ItemObjetivo> lst = result.get(curso.getTipoCurso());
                    for (ItemObjetivo o : r) {
                        int idx = -1;
                        if ((idx = lst.indexOf(o)) != -1) {
                            ItemObjetivo itemObjetivo = lst.get(idx);
                            itemObjetivo.buenas += o.buenas;
                            itemObjetivo.nroPreguntas += o.nroPreguntas;
                        } else {
                            lst.add(o);
                        }
                    }
                } else {
                    result.put(curso.getTipoCurso(), r);
                }
            }
        }

        return result;
    }

    static List<ItemObjetivo> evaluarCurso(List<PruebaRendida> pruebas) {
        if (pruebas == null || pruebas.isEmpty()) {
            log.severe("En evaluarCuso viene una lista de pruebas vacía");
        }
        final List<ItemObjetivo> result = new ArrayList<>();
        for (final PruebaRendida p : pruebas) {
            Logger.getLogger("UtilEvaluations")
                    .info("Evaluando prueba:" + p.getNombre() + " " + p.getPaterno() + " " + p.getMaterno());
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
