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
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.MapBuilder;

public class XUtilEvaluations {
  private static final Logger log = Logger.getLogger(XUtilEvaluations.class.getName());

  /**
   * Evaluamos solo un alumno. Obtenemos la cantidad de buenas que tiene por cada objetivo asociado
   * a la prueba.
   *
   * @param pRendida La prueba del alumno,
   */
  static List<XItemObjetivo> evaluarAlumno(final R_PruebaRendida pRendida,
      final List<R_RespuestasEsperadasPrueba> resEsperadas, final List<R_Ejetematico> lstEjes,
      final List<R_Habilidad> lstHab, final List<R_Objetivo> lstObj) {

        String respuestas = pRendida.getRespuestas();
        if (respuestas.length() < resEsperadas.size()) {
          respuestas = Strings.padEnd(respuestas, resEsperadas.size(), 'O');
        }

        final String[] strResps = respuestas.split("");
        final Map<Long, List<R_RespuestasEsperadasPrueba>> byObjective =
            resEsperadas.stream().collect(Collectors.groupingBy(R_RespuestasEsperadasPrueba::getObjetivo_id));


        final List<XItemObjetivo> result = new ArrayList<>();
        for (final Long key : byObjective.keySet()) {
          final List<R_RespuestasEsperadasPrueba> resps = byObjective.get(key);
          // Buscamos los objetivos
          R_Objetivo objetivo = lstObj.parallelStream().filter(o -> o.getId().equals(key)).findFirst().orElse(null);
          if (objetivo == null) {
            continue;
          }
          final XItemObjetivo objxAlumno = new XItemObjetivo.Builder().objetivo(objetivo).build();
          for (final R_RespuestasEsperadasPrueba re : resps) {

            if ("+".equals(re.getRespuesta()) || strResps[re.getNumero() - 1].equalsIgnoreCase(re.getRespuesta())) {
              objxAlumno.addBuena();
            }
            objxAlumno.addPregunta();
            String value;

            R_Ejetematico eje =
                lstEjes.parallelStream().filter(e -> e.getId().equals(re.getEjetematico_id())).findFirst().orElse(null);
            if (!objxAlumno.getEjesAsociados().contains(eje.getName())) {
              value = objxAlumno.getEjesAsociados() + (objxAlumno.getEjesAsociados().isEmpty() ? "" : "\n")
                  + eje.getName().toUpperCase();
              objxAlumno.setEjesAsociados(value);
            }

            R_Habilidad hab =
                lstHab.parallelStream().filter(e -> e.getId().equals(re.getHabilidad_id())).findFirst().orElse(null);
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


  static Map<R_Curso, List<XItemObjetivo>> evaluarColegio(List<R_EvaluacionPrueba> evaluaciones,
      List<R_PruebaRendida> pruebas, List<R_RespuestasEsperadasPrueba> lstRespEsperadas, long tipoAlumno) {

    final Map<R_Curso, List<XItemObjetivo>> result = new HashMap<>();

    List<R_Alumno> lstAlumnos = new ArrayList<>();

    List<Long> lstIdCurso = evaluaciones.parallelStream().map(e -> e.getCurso_id()).collect(Collectors.toList());
    Long[] ids = lstIdCurso.toArray(new Long[lstIdCurso.size()]);
    List<R_Curso> lstCurso = PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Curso.class, ids);
    // Obtengo todos los alumnos de todas las pruebas
    for (Long id : lstIdCurso) {
      Map<String, Object> params = MapBuilder.<String, Object>unordered().put("curso_id", id).build();
      lstAlumnos.addAll(PersistenceServiceFactory.getPersistenceService().findByParamsSynchro(R_Alumno.class, params));
    }

    // Agrupo las pruebas rendidas por curso.
    Map<R_Curso, List<R_PruebaRendida>> mapCursos = new HashMap<>();
    for (R_PruebaRendida prueba : pruebas) {
      R_Alumno alumno =
          lstAlumnos.parallelStream().filter(a -> a.getId().equals(prueba.getAlumno_id())).findFirst().orElse(null);
      if (alumno == null)
        continue;
      R_Curso curso =
          lstCurso.parallelStream().filter(t -> t.getId().equals(alumno.getCurso_id())).findFirst().orElse(null);
      if (curso == null)
        continue;

      List<R_PruebaRendida> lstPRendidas = mapCursos.get(curso);
      if (lstPRendidas == null) {
        lstPRendidas = new ArrayList<>();
        mapCursos.put(curso, lstPRendidas);
      }
      lstPRendidas.add(prueba);
    }
    // Agrupo las respuestas esperadas por cada curso.
    Map<R_Curso, List<R_RespuestasEsperadasPrueba>> mapCursoRespEsp = new HashMap<>();
    for (R_RespuestasEsperadasPrueba rEsp : lstRespEsperadas) {
      R_EvaluacionPrueba eval = evaluaciones.parallelStream().filter(e -> e.getPrueba_id().equals(rEsp.getPrueba_id()))
          .findFirst().orElse(null);
      if (eval == null) {
        continue;
      }
      R_Curso curso =
          lstCurso.parallelStream().filter(c -> c.getId().equals(eval.getCurso_id())).findFirst().orElse(null);
      if (curso == null) {
        continue;
      }
      List<R_RespuestasEsperadasPrueba> lstRespEsp = mapCursoRespEsp.get(curso);
      if (lstRespEsp == null) {
        lstRespEsp = new ArrayList<>();
        mapCursoRespEsp.put(curso, lstRespEsp);
      }
      lstRespEsp.add(rEsp);
    }



    // Por cada curso lo evalúo.
    for (R_Curso curso : mapCursos.keySet()) {
      List<R_PruebaRendida> pRendidas = mapCursos.get(curso);
      List<R_RespuestasEsperadasPrueba> rEsperadas = mapCursoRespEsp.get(curso);
      if (pRendidas != null && !pRendidas.isEmpty() && rEsperadas != null && !rEsperadas.isEmpty()) {
        List<XItemObjetivo> r = XUtilEvaluations.evaluarCurso(pRendidas, rEsperadas, tipoAlumno);
        result.put(curso, r);
      }
    }
    return result;
  }

  static Map<R_TipoCurso, List<XItemObjetivo>> evaluarColegioxNivel(List<R_EvaluacionPrueba> evaluacionesPrueba,
      List<R_PruebaRendida> pruebas, List<R_RespuestasEsperadasPrueba> lstRespEsperadas, long tipoAlumno) {
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
        // Solo las respuestas esperadas del curso.
        R_EvaluacionPrueba eval = evaluacionesPrueba.parallelStream()
            .filter(e -> e.getId().equals(pRendidas.get(0).getEvaluacionprueba_id())).findFirst().orElse(null);
        List<R_RespuestasEsperadasPrueba> lLstRespEsperadas = lstRespEsperadas.stream()
            .filter(r -> r.getPrueba_id().equals(eval.getPrueba_id())).collect(Collectors.toList());


        List<XItemObjetivo> r = XUtilEvaluations.evaluarCurso(pRendidas, lLstRespEsperadas, tipoAlumno);
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
    Long[] ejesId =
        lstRespEsperadas.parallelStream().map(r -> r.getEjetematico_id()).distinct().toArray(size -> new Long[size]);
    List<R_Ejetematico> lstEjes =
        PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Ejetematico.class, ejesId);
    Long[] habsId =
        lstRespEsperadas.parallelStream().map(r -> r.getHabilidad_id()).distinct().toArray(size -> new Long[size]);
    List<R_Habilidad> lstHab =
        PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Habilidad.class, habsId);
    Long[] objsId =
        lstRespEsperadas.parallelStream().map(r -> r.getObjetivo_id()).distinct().toArray(size -> new Long[size]);
    List<R_Objetivo> lstObj =
        PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Objetivo.class, objsId);

    final List<XItemObjetivo> result = new ArrayList<>();
    for (final R_PruebaRendida p : pruebas) {
      Long idTipoAlumno = p.getTipoalumno_id();
      if (tipoAlumno == Constants.PIE_ALL || tipoAlumno == idTipoAlumno) {
        log.fine("Evaluando prueba:" + p.getAlumno_id());



        final List<XItemObjetivo> rAlumno =
            XUtilEvaluations.evaluarAlumno(p, lstRespEsperadas, lstEjes, lstHab, lstObj);
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
