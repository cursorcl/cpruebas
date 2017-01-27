package ot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

import cl.eos.common.Constants;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoCurso;

public class XUtilEvaluations {
  private static final Logger log = Logger.getLogger(XUtilEvaluations.class.getName());

  // static class GroupByTipoCurso implements Function<R_PruebaRendida, R_TipoCurso> {
  // @Override
  // public R_TipoCurso apply(R_PruebaRendida t) {
  // return t.getAlumno().getCurso().getTipoCurso();
  // }
  // }
  /**
   * Evaluamos solo un alumno. Obtenemos la cantidad de buenas que tiene por cada objetivo asociado
   * a la prueba.
   *
   * @param pRendida La prueba del alumno,
   */
  static List<XItemObjetivo> evaluarAlumno(R_PruebaRendida pRendida, List<R_RespuestasEsperadasPrueba> resEsperadas) {
    String respuestas = pRendida.getRespuestas();
    if (respuestas.length() < resEsperadas.size()) {
      respuestas = Strings.padEnd(respuestas, resEsperadas.size(), 'O');
    }
    final String[] reps = respuestas.split("");
    final Map<Long, List<R_RespuestasEsperadasPrueba>> byObjective =
        resEsperadas.stream().collect(Collectors.groupingBy(R_RespuestasEsperadasPrueba::getObjetivo_id));
    Map<Long, R_Objetivo> mapObjetivos = new HashMap<>();
    final List<XItemObjetivo> result = new ArrayList<>();
    for (final Long key : byObjective.keySet()) {
      final List<R_RespuestasEsperadasPrueba> resps = byObjective.get(key);
      // Buscamos los objetivos
      R_Objetivo objetivo = mapObjetivos.get(key);
      if (objetivo == null) {
        objetivo = PersistenceServiceFactory.getPersistenceService().findByIdSynchro(R_Objetivo.class, key);
        mapObjetivos.put(key, objetivo);
      }
      final XItemObjetivo objxAlumno = new XItemObjetivo.Builder().objetivo(objetivo).build();
      for (final R_RespuestasEsperadasPrueba re : resps) {
        if (reps[re.getNumero() - 1].equalsIgnoreCase(re.getRespuesta())) {
          objxAlumno.addBuena();
        }
        objxAlumno.addPregunta();
        String value;

        R_Ejetematico eje = PersistenceServiceFactory.getPersistenceService().findByIdSynchro(R_Ejetematico.class,
            re.getEjetematico_id());
        if (!objxAlumno.getEjesAsociados().contains(eje.getName())) {
          value = objxAlumno.getEjesAsociados() + (objxAlumno.getEjesAsociados().isEmpty() ? "" : "\n")
              + eje.getName().toUpperCase();
          objxAlumno.setEjesAsociados(value);
        }
        R_Habilidad hab =
            PersistenceServiceFactory.getPersistenceService().findByIdSynchro(R_Habilidad.class, re.getHabilidad_id());
        if (!objxAlumno.getHabilidades().contains(hab.getName())) {
          value = objxAlumno.getHabilidades() + (objxAlumno.getHabilidades().isEmpty() ? "" : "\n")
              + hab.getName().toUpperCase();
          objxAlumno.setHabilidades(value);
        }
        if (!objxAlumno.getPreguntas().contains("" + re.getNumero())) {
          value = objxAlumno.getPreguntas() + (objxAlumno.getPreguntas().isEmpty() ? "" : "-") + re.getNumero();
          objxAlumno.setPreguntas(value);
        }
      }
      result.add(objxAlumno);
    }
    return result;
  }


  static Map<R_Curso, List<XItemObjetivo>> evaluarColegio(List<R_PruebaRendida> pruebas,
      List<R_RespuestasEsperadasPrueba> lstRespEsperadas, long tipoAlumno) {
    final Map<R_Curso, List<XItemObjetivo>> result = new HashMap<>();


    List<Long> lstIdAlumnos = pruebas.stream().map(p -> p.getAlumno_id()).collect(Collectors.toList());
    Long[] ids = lstIdAlumnos.toArray(new Long[lstIdAlumnos.size()]);
    List<R_Alumno> lstAlumnos =
        PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Alumno.class, ids);

    List<Long> lstIdCurso = lstAlumnos.stream().map(a -> a.getTipocurso_id()).collect(Collectors.toList());
    ids = lstIdCurso.toArray(new Long[lstIdCurso.size()]);
    List<R_Curso> lstCurso = PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Curso.class, ids);

    Map<R_Curso, List<R_PruebaRendida>> mapCursos = new HashMap<>();

    for (R_PruebaRendida prueba : pruebas) {
      R_Alumno alumno =
          lstAlumnos.stream().filter(a -> a.getId().equals(prueba.getAlumno_id())).findFirst().orElse(null);
      if (alumno == null)
        continue;
      R_Curso curso = lstCurso.stream().filter(t -> t.getId().equals(alumno.getCurso_id())).findFirst().orElse(null);
      if (curso == null)
        continue;

      List<R_PruebaRendida> lstPRendidas = mapCursos.get(curso);
      if (lstPRendidas == null) {
        lstPRendidas = new ArrayList<>();
        mapCursos.put(curso, lstPRendidas);
      }
      lstPRendidas.add(prueba);
    }


    for (R_Curso curso : mapCursos.keySet()) {
      log.fine("Evaluando el curso:" + curso.getName());
      List<R_PruebaRendida> pRendidas = mapCursos.get(curso);
      if (pRendidas != null && !pRendidas.isEmpty()) {
        log.fine("Tiene:" + pRendidas.size() + " pruebas rendidas.");
        List<XItemObjetivo> r = XUtilEvaluations.evaluarCurso(pRendidas, lstRespEsperadas, tipoAlumno);
        log.fine("Se obtienen:" + r.size() + " ItemObjectivo.");
        result.put(curso, r);
      }
    }
    return result;
  }

  static Map<R_TipoCurso, List<XItemObjetivo>> evaluarColegioxNivel(List<R_PruebaRendida> pruebas,
      List<R_RespuestasEsperadasPrueba> lstRespEsperadas, long tipoAlumno) {
    final Map<R_TipoCurso, List<XItemObjetivo>> result = new HashMap<>();

    List<Long> lstIdAlumnos = pruebas.stream().map(p -> p.getAlumno_id()).collect(Collectors.toList());
    Long[] ids = lstIdAlumnos.toArray(new Long[lstIdAlumnos.size()]);
    List<R_Alumno> lstAlumnos =
        PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Alumno.class, ids);

    List<Long> lstIdTipoCurso = lstAlumnos.stream().map(a -> a.getTipocurso_id()).collect(Collectors.toList());
    ids = lstIdTipoCurso.toArray(new Long[lstIdTipoCurso.size()]);
    List<R_TipoCurso> lstTipoCurso =
        PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_TipoCurso.class, ids);

    Map<R_TipoCurso, List<R_PruebaRendida>> mapCursos = new HashMap<>();

    for (R_PruebaRendida prueba : pruebas) {
      R_Alumno alumno =
          lstAlumnos.stream().filter(a -> a.getId().equals(prueba.getAlumno_id())).findFirst().orElse(null);
      if (alumno == null)
        continue;
      R_TipoCurso tipoCurso =
          lstTipoCurso.stream().filter(t -> t.getId().equals(alumno.getTipocurso_id())).findFirst().orElse(null);
      if (tipoCurso == null)
        continue;

      List<R_PruebaRendida> lstPRendidas = mapCursos.get(tipoCurso);
      if (lstPRendidas == null) {
        lstPRendidas = new ArrayList<>();
        mapCursos.put(tipoCurso, lstPRendidas);
      }
      lstPRendidas.add(prueba);
    }

    for (R_TipoCurso tipoCurso : mapCursos.keySet()) {
      log.fine("Evaluando el curso:" + tipoCurso.getName());
      List<R_PruebaRendida> pRendidas = mapCursos.get(tipoCurso);
      if (pRendidas != null && !pRendidas.isEmpty()) {
        log.fine("Tiene:" + pRendidas.size() + " pruebas rendidas.");
        List<XItemObjetivo> r = XUtilEvaluations.evaluarCurso(pRendidas, lstRespEsperadas, tipoAlumno);
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

  static List<XItemObjetivo> evaluarCurso(List<R_PruebaRendida> pruebas,
      List<R_RespuestasEsperadasPrueba> lstRespEsperadas, long tipoAlumno) {
    if (pruebas == null || pruebas.isEmpty()) {
      log.severe("En evaluarCuso viene una lista de pruebas vacía");
    }
    final List<XItemObjetivo> result = new ArrayList<>();
    for (final R_PruebaRendida p : pruebas) {
      Long idTipoAlumno = p.getTipoalumno_id();
      if (tipoAlumno == Constants.PIE_ALL || tipoAlumno == idTipoAlumno) {
        log.fine("Evaluando prueba:" + p.getAlumno_id());
        final List<XItemObjetivo> rAlumno = XUtilEvaluations.evaluarAlumno(p, lstRespEsperadas);
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
      } else {
        log.info("POR CRITERIO PIE: No se a evalúa prueba:" + p.getAlumno_id());
      }
    }
    return result;
  }
}
