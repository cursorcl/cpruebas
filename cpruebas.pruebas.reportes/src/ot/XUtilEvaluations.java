package ot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

import cl.eos.common.Constants;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SObjetivo;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.models.STipoCurso;

public class XUtilEvaluations {

    private static final Logger log = Logger.getLogger(XUtilEvaluations.class.getName());

    static class GroupByTipoCurso implements Function<SPruebaRendida, STipoCurso> {
        @Override
        public STipoCurso apply(SPruebaRendida t) {
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
    static List<XItemObjetivo> evaluarAlumno(SPruebaRendida pRendida) {

        final List<SRespuestasEsperadasPrueba> resEsperadas = pRendida.getEvaluacionPrueba().getPrueba().getRespuestas();

        String respuestas = pRendida.getRespuestas();
        if (respuestas.length() < resEsperadas.size()) {
            respuestas = Strings.padEnd(respuestas, resEsperadas.size(), 'O');
        }
        final String[] reps = respuestas.split("");

        final Map<SObjetivo, List<SRespuestasEsperadasPrueba>> byObjective = resEsperadas.stream()
                .collect(Collectors.groupingBy(SRespuestasEsperadasPrueba::getObjetivo));

        final List<XItemObjetivo> result = new ArrayList<>();
        for (final SObjetivo key : byObjective.keySet()) {
            final List<SRespuestasEsperadasPrueba> resps = byObjective.get(key);
            final XItemObjetivo objxAlumno = new XItemObjetivo.Builder().objetivo(key).build();
            for (final SRespuestasEsperadasPrueba re : resps) {
                if (reps[re.getNumero() - 1].equalsIgnoreCase(re.getRespuesta()))
                    objxAlumno.addBuena();
                objxAlumno.addPregunta();
                String value;
                if (!objxAlumno.getEjesAsociados().contains(re.getEjeTematico().getName())) {
                    value = objxAlumno.getEjesAsociados() + (objxAlumno.getEjesAsociados().isEmpty() ? "" : "\n")
                            + re.getEjeTematico().getName().toUpperCase();
                    objxAlumno.setEjesAsociados(value);
                }
                if (!objxAlumno.getHabilidades().contains(re.getHabilidad().getName())) {
                    value = objxAlumno.getHabilidades() + (objxAlumno.getHabilidades().isEmpty() ? "" : "\n")
                            + re.getHabilidad().getName().toUpperCase();
                    objxAlumno.setHabilidades(value);
                }
                if (!objxAlumno.getPreguntas().contains("" + re.getNumero())) {
                    value = objxAlumno.getPreguntas() + (objxAlumno.getPreguntas().isEmpty() ? "" : "-")
                            + re.getNumero();
                    objxAlumno.setPreguntas(value);
                }
            }
            result.add(objxAlumno);
        }

        return result;
    }

    static Map<SCurso, List<XItemObjetivo>> evaluarColegio(List<SPruebaRendida> pruebas, long tipoAlumno) {

        final Map<SCurso, List<XItemObjetivo>> result = new HashMap<>();

        final Map<SCurso, List<SPruebaRendida>> mapCursos = pruebas.stream()
                .collect(Collectors.groupingBy(SPruebaRendida::getObjCurso));

        for (SCurso curso : mapCursos.keySet()) {
            log.fine("Evaluando el curso:" + curso.getName());
            List<SPruebaRendida> pRendidas = mapCursos.get(curso);
            if (pRendidas != null && !pRendidas.isEmpty()) {
                log.fine("Tiene:" + pRendidas.size() + " pruebas rendidas.");
                List<XItemObjetivo> r = XUtilEvaluations.evaluarCurso(pRendidas, tipoAlumno);
                log.fine("Se obtienen:" + r.size() + " ItemObjectivo.");
                result.put(curso, r);
            }
        }
        return result;
    }

    static Map<STipoCurso, List<XItemObjetivo>> evaluarColegioxNivel(List<SPruebaRendida> pruebas, long tipoAlumno) {

        final Map<STipoCurso, List<XItemObjetivo>> result = new HashMap<>();
        final Map<STipoCurso, List<SPruebaRendida>> mapCursos = pruebas.stream()
                .collect(Collectors.groupingBy(SPruebaRendida::getTipoCurso));

        for (STipoCurso tipoCurso : mapCursos.keySet()) {
            log.fine("Evaluando el curso:" + tipoCurso.getName());
            List<SPruebaRendida> pRendidas = mapCursos.get(tipoCurso);
            if (pRendidas != null && !pRendidas.isEmpty()) {
                log.fine("Tiene:" + pRendidas.size() + " pruebas rendidas.");
                List<XItemObjetivo> r = XUtilEvaluations.evaluarCurso(pRendidas, tipoAlumno);
                log.fine("Se obtienen:" + r.size() + " ItemObjectivo.");
                if (result.containsKey(tipoCurso)) {
                    List<XItemObjetivo> lst = result.get(tipoCurso);
                    for (XItemObjetivo o : r) {
                        int idx = -1;
                        if ((idx = lst.indexOf(o)) != -1) {
                            XItemObjetivo itemObjetivo = lst.get(idx);
                            itemObjetivo.buenas += o.buenas;
                            itemObjetivo.nroPreguntas += o.nroPreguntas;
                        } else {
                            lst.add(o);
                        }
                    }
                } else {
                    result.put(tipoCurso, r);
                }
            }
        }

        return result;
    }

    static List<XItemObjetivo> evaluarCurso(List<SPruebaRendida> pruebas, long tipoAlumno) {
        if (pruebas == null || pruebas.isEmpty()) {
            log.severe("En evaluarCuso viene una lista de pruebas vacía");
        }
        final List<XItemObjetivo> result = new ArrayList<>();
        for (final SPruebaRendida p : pruebas) {
            Long idTipoAlumno = p.getAlumno().getTipoAlumno().getId();

            if (tipoAlumno == Constants.PIE_ALL || tipoAlumno == idTipoAlumno) {

                log.fine("Evaluando prueba:" + p.getNombre() + " " + p.getPaterno() + " " + p.getMaterno());
                final List<XItemObjetivo> rAlumno = XUtilEvaluations.evaluarAlumno(p);
                if (result.isEmpty()) {
                    result.addAll(rAlumno);
                } else {
                    for (final XItemObjetivo item : rAlumno) {
                        int idx = -1;
                        if ((idx = result.indexOf(item)) == -1) {
                            result.add(item);
                        } else {
                            result.get(idx).add(item);
                        }
                    }
                }
            }
            else
            {
                log.info("POR CRITERIO PIE: No se a evalúa prueba:" + p.getNombre() + " " + p.getPaterno() + " " + p.getMaterno());
            }
        }
        return result;
    }
}
