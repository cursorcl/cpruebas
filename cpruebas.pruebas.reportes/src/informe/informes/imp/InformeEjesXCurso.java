package informe.informes.imp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cl.eos.common.Constants;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.MapBuilder;
import cl.eos.util.Pair;
import informe.InformeManager;
import informe.informes.IInforme;
import utils.ChartsUtil;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 *
 * @author colegio
 *
 */
public class InformeEjesXCurso implements IInforme {

  private static final String ASIGNATURA_ID = "asignatura_id";
  private static final String COLEGIO_ID = "colegio_id";

  static Logger log = Logger.getLogger(InformeEjesXCurso.class);
  private R_TipoAlumno tipoAlumno;
  private R_Colegio colegio;
  private R_Asignatura asignatura;
  private Map<R_Ejetematico, List<OTPreguntasEjes>> resultado;
  private List<R_RangoEvaluacion> rangos;
  private List<R_Curso> cursoList;
  private int nroCursos;
  private Map<Long, List<R_RespuestasEsperadasPrueba>> mapRespEsperadas = new HashMap<>();


  public InformeEjesXCurso() {
    super();
  }

  @Override
  public void execute(R_TipoAlumno tipoAlumno, R_Colegio colegio, R_Asignatura asignatura) {
    rangos = PersistenceServiceFactory.getPersistenceService().findAllSynchro(R_RangoEvaluacion.class);

    if (Objects.isNull(rangos) || rangos.isEmpty())
      return;

    rangos = rangos.stream().sorted(Comparadores.rangoEvaluacionComparator()).collect(Collectors.toList());
    this.tipoAlumno = tipoAlumno;
    this.colegio = colegio;
    this.asignatura = asignatura;
    final Map<String, Object> params = new HashMap<>();
    params.put(InformeEjesXCurso.COLEGIO_ID, colegio.getId());
    params.put(InformeEjesXCurso.ASIGNATURA_ID, asignatura.getId());
    final List<R_EvaluacionPrueba> evaluaciones =
        PersistenceServiceFactory.getPersistenceService().findByParamsSynchro(R_EvaluacionPrueba.class, params);

    if (evaluaciones == null || evaluaciones.isEmpty())
      return;

    resultado = procesar(evaluaciones);
  }

  @Override
  public void graph(XWPFDocument document) {
    if (resultado == null || resultado.isEmpty())
      return;

    final XWPFParagraph paragraph = document.createParagraph();

    paragraph.setStyle("PEREKE-TITULO");
    final List<String> titles = new ArrayList<>();
    final List<Double> values = new ArrayList<>();

    for (final R_Ejetematico eje : resultado.keySet()) {
      final List<OTPreguntasEjes> percents = resultado.get(eje);
      double cantidad = 0;
      double percent = 0;
      for (final OTPreguntasEjes ot : percents) {
        if (ot.getBuenas() != 0 && ot.getTotal() != 0) {
          cantidad++;
          percent += ot.getBuenas().doubleValue() / ot.getTotal().doubleValue() * 100d;
        }
      }

      percent = percent / cantidad;
      titles.add(eje.getName());
      values.add(new Double(percent));
    }

    try {
      final File file = ChartsUtil.createLineChart("DISTRIBUCIÓN x EJES", "Ejes", titles, values);
      WordUtil.addImage(file, "DISTRIBUCIÓN x EJES", paragraph, 500, 350);
    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final InvalidFormatException e) {
      e.printStackTrace();
    }

  }

  /**
   * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las
   * respuestas eperadas.
   *
   * @param respuestas Las respuestas del alumno.
   * @param respEsperadas Las respuestas correctas definidas en la prueba.
   * @param eje El Eje tematico en base al que se realiza el calculo.
   * @return Par <Preguntas buenas, Total de Preguntas> del eje.
   */
  private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas,
      List<R_RespuestasEsperadasPrueba> respEsperadas, R_Ejetematico eje) {
    int nroBuenas = 0;
    int nroPreguntas = 0;
    for (int n = 0; n < respEsperadas.size(); n++) {
      final R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
      if (!resp.getAnulada()) {
        if (resp.getEjetematico_id().equals(eje.getId())) {
          if (respuestas.length() > n) {
            final String sResp = respuestas.substring(n, n + 1);
            if ("+".equals(sResp) || resp.getRespuesta().equalsIgnoreCase(sResp)) {
              nroBuenas++;
            }
          }
          nroPreguntas++;
        }
      }
    }
    return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
  }

  @Override
  public void page(XWPFDocument document) {

    if (resultado == null || resultado.isEmpty())
      return;

    XWPFParagraph paragraph = document.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.addCarriageReturn();

    final int nRows = resultado.keySet().size();

    int n = 0;
    while (n < nroCursos) {
      paragraph = document.createParagraph();
      run = paragraph.createRun(); // create new run
      run.addCarriageReturn();

      final int cursos = Math.min(12, nroCursos - n);
      final XWPFTable table = document.createTable(nRows + 1, cursos + 1);
      WordUtil.setTableFormat(table, 1, 0);

      XWPFTableRow tableRow = table.getRow(0);
      tableRow.getCell(0).getParagraphs().get(0).getRuns().get(0).setText("EJE");
      int col = 1;
      for (int r = n; r < n + cursos; r++) {
        final String title = cursoList.get(r).getName();
        tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0).setText(title);
      }
      int row = 1;
      for (final R_Ejetematico eje : resultado.keySet()) {
        tableRow = table.getRow(row++);
        tableRow.getCell(0).getParagraphs().get(0).getRuns().get(0).setText(eje.getName().toUpperCase());
        final List<OTPreguntasEjes> notas = resultado.get(eje);
        col = 1;
        for (int r = n; r < n + cursos; r++) {
          final OTPreguntasEjes nota = notas.get(r);
          if (nota != null)
            tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0)
                .setText(String.format("%5.1f", nota.getSlogrado()));
          // tableRow.getCell(col++).setText(String.format("%5.2f",
          // nota.getSlogrado()));
          else
            tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0).setText(String.format("%5.1f", 0f));
          // tableRow.getCell(col++).setText(String.format("%5.2f",
          // 0f));
        }
      }

      n = n + 12;
    }
    paragraph = document.createParagraph();
    paragraph.setStyle("Descripción");
    run = paragraph.createRun();
    paragraph.setAlignment(ParagraphAlignment.CENTER);
    run.setText(String.format("Tabla  %d: DISTRIBUCIÓN DE RESULTADOS POR EJES DE APRENDIZAJES %s en %s",
        InformeManager.TABLA++, colegio.getName(), asignatura.getName()));
    run.addCarriageReturn();
    paragraph = document.createParagraph();
    paragraph.setStyle("Normal");
    run = paragraph.createRun();
    run.addCarriageReturn();
  }

  protected Map<R_Ejetematico, List<OTPreguntasEjes>> procesar(List<R_EvaluacionPrueba> evaluacionesPrueba) {

    cursoList = obtenerCursos(evaluacionesPrueba);

    Map<R_Ejetematico, List<OTPreguntasEjes>> mapEjes = new HashMap<>();

    nroCursos = cursoList.size();

    final int[] totalAlumnos = new int[nroCursos];
    Arrays.fill(totalAlumnos, 0);
    final int[] alumnosEvaluados = new int[nroCursos];
    Arrays.fill(alumnosEvaluados, 0);

    for (final R_EvaluacionPrueba eval : evaluacionesPrueba) {

      final List<R_PruebaRendida> pruebasRendidas = obtenerPruebasRendidas(eval);
      final List<R_RespuestasEsperadasPrueba> respEsperadas = obtenerRespEsperadas(eval);
      mapEjes = obtenerEjesTematicos(mapEjes, respEsperadas);

      final int index = IntStream.range(0, cursoList.size())
          .filter(i -> eval.getCurso_id().equals(cursoList.get(i).getId())).findFirst().orElse(-1);

      if (index == -1) {
        continue;
      }
      totalAlumnos[index] = 0;

      R_Curso curso = cursoList.get(index);
      List<R_Alumno> lstAlumnos = obtenerAlumnos(curso);

      totalAlumnos[index] = (int) lstAlumnos.stream()
          .filter(a -> tipoAlumno.getId() == Constants.PIE_ALL || a.getTipoalumno_id().equals(tipoAlumno)).count();

      for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {
        if (tipoAlumno.getId() != Constants.PIE_ALL && !pruebaRendida.getTipoalumno_id().equals(tipoAlumno))
          continue;

        alumnosEvaluados[index] = alumnosEvaluados[index] + 1;

        final String respuestas = pruebaRendida.getRespuestas();
        if (respuestas == null || respuestas.isEmpty())
          continue;


        for (final R_Ejetematico eje : mapEjes.keySet()) {
          final List<OTPreguntasEjes> lstEjes = mapEjes.get(eje);
          OTPreguntasEjes otEje = lstEjes.get(index);
          if (otEje == null || otEje.getEjeTematico() == null) {
            otEje = new OTPreguntasEjes(eje);
            lstEjes.set(index, otEje);
          }
          final Pair<Integer, Integer> buenasTotal = obtenerBuenasTotales(respuestas, respEsperadas, eje);
          otEje.setBuenas(otEje.getBuenas() + buenasTotal.getFirst());
          otEje.setTotal(otEje.getTotal() + buenasTotal.getSecond());
          lstEjes.set(index, otEje);
        }
      }
    }

    return mapEjes;
  }

  private Map<R_Ejetematico, List<OTPreguntasEjes>> obtenerEjesTematicos(
      Map<R_Ejetematico, List<OTPreguntasEjes>> mapEjes, List<R_RespuestasEsperadasPrueba> respEsperadas) {

    for (R_RespuestasEsperadasPrueba resp : respEsperadas) {
      Long id = resp.getEjetematico_id();
      List<Long> ejesId = mapEjes.keySet().stream().map(e -> e.getId()).collect(Collectors.toList());
      int idx = IntStream.range(0, ejesId.size()).filter(i -> ejesId.get(i) == id).findFirst().orElse(-1);

      if (idx == -1) {
        R_Ejetematico eje = PersistenceServiceFactory.getPersistenceService().findByIdSynchro(R_Ejetematico.class, id);
        mapEjes.put(eje, Stream.generate(OTPreguntasEjes::new).limit(nroCursos).collect(Collectors.toList()));
      }
    }
    return mapEjes;
  }

  private List<R_Alumno> obtenerAlumnos(R_Curso curso) {
    Map<String, Object> params = MapBuilder.<String, Object>unordered().put("curso_id", curso.getId()).build();
    return PersistenceServiceFactory.getPersistenceService().findByParamsSynchro(R_Alumno.class, params);
  }

  private List<R_RespuestasEsperadasPrueba> obtenerRespEsperadas(R_EvaluacionPrueba eval) {
    Long pruebaId = eval.getPrueba_id();

    List<R_RespuestasEsperadasPrueba> respEsperadas = mapRespEsperadas.get(pruebaId);
    if (respEsperadas == null) {

      Map<String, Object> params = MapBuilder.<String, Object>unordered().put("prueba_id", eval.getPrueba_id()).build();
      respEsperadas = PersistenceServiceFactory.getPersistenceService()
          .findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
      mapRespEsperadas.put(pruebaId, respEsperadas);
    }
    return respEsperadas;
  }

  private List<R_PruebaRendida> obtenerPruebasRendidas(R_EvaluacionPrueba eval) {
    Map<String, Object> params = MapBuilder.<String, Object>unordered().put("prueba_id", eval.getPrueba_id()).build();
    return PersistenceServiceFactory.getPersistenceService().findByParamsSynchro(R_PruebaRendida.class, params);
  }

  private List<R_Curso> obtenerCursos(List<R_EvaluacionPrueba> evaluacionesPrueba) {
    Long[] ids = evaluacionesPrueba.stream().map(e -> e.getCurso_id()).toArray(n -> new Long[n]);

    List<R_Curso> cursos = PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Curso.class, ids);
    cursos = cursos.stream().distinct().sorted(Comparadores.comparaResumeCurso()).collect(Collectors.toList());

    return cursos;
  }

}
