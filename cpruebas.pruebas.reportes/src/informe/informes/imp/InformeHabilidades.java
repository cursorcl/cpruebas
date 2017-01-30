package informe.informes.imp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cl.eos.common.Constants;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.MapBuilder;
import cl.eos.view.ots.ejeevaluacion.OTAcumulador;
import informe.InformeManager;
import informe.informes.IInforme;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 *
 * @author colegio
 *
 */
public class InformeHabilidades implements IInforme {
  private static final String ASIGNATURA_ID = "idAsignatura";
  private static final String COLEGIO_ID = "idColegio";
  static Logger log = Logger.getLogger(InformeHabilidades.class);
  private R_TipoAlumno tipoAlumno;
  private Map<R_Habilidad, List<OTAcumulador>> resultado;
  private Map<Long, List<R_RespuestasEsperadasPrueba>> mapRespEsperadas = new HashMap<>();
  private List<R_RangoEvaluacion> rangos;
  private List<R_Curso> lstCursos;
  private boolean kinder_ciclo;
  private boolean media_ciclo;
  private boolean basica_ciclo;
  private int nroRangos;
  private int nroCursos;

  public InformeHabilidades() {
    super();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void execute(R_TipoAlumno tipoAlumno, R_Colegio colegio, R_Asignatura asignatura) {
    rangos = PersistenceServiceFactory.getPersistenceService().findAllSynchro(R_RangoEvaluacion.class);

    if (Objects.isNull(rangos) || rangos.isEmpty())
      return;

    rangos = rangos.stream().sorted(Comparadores.rangoEvaluacionComparator()).collect(Collectors.toList());
    this.tipoAlumno = tipoAlumno;

    final Map<String, Object> params = new HashMap<>();
    params.put(InformeHabilidades.COLEGIO_ID, colegio.getId());
    params.put(InformeHabilidades.ASIGNATURA_ID, asignatura.getId());
    final List<R_EvaluacionPrueba> evaluaciones = (List<R_EvaluacionPrueba>) (Object) PersistenceServiceFactory
        .getPersistenceService().findByParamsSynchro(R_EvaluacionPrueba.class, params);

    if (Objects.isNull(evaluaciones) || evaluaciones.isEmpty())
      return;

    params.clear();
    params.put("colegio_id", colegio.getId());
    lstCursos = (List<R_Curso>) (Object) PersistenceServiceFactory.getPersistenceService()
        .findByParamsSynchro(R_Curso.class, params);

    if (Objects.isNull(evaluaciones) || Objects.isNull(lstCursos) || lstCursos.isEmpty() || evaluaciones.isEmpty())
      return;

    resultado = procesar(evaluaciones);
  }

  private String getTiteTables(R_Curso curso) {
    String tableTitle = null;
    if (curso.getCiclo_id() < InformeManager.CICLO_7 && !basica_ciclo) {
      tableTitle = String.format("Tabla  %d: RESULTADOS ENSEÑANZA BÁSICA", InformeManager.TABLA++);
      basica_ciclo = true;
    }
    if (curso.getCiclo_id() == InformeManager.CICLO_7 && !kinder_ciclo) {
      tableTitle = String.format("Tabla  %d: RESULTADOS DE PRE-BÁSICA", InformeManager.TABLA++);
      kinder_ciclo = true;
    }
    if (curso.getCiclo_id() > InformeManager.CICLO_7 && !media_ciclo) {
      tableTitle = String.format("Tabla  %d: RESULTADOS DESDE 1º a 4º MEDIO", InformeManager.TABLA++);
      media_ciclo = true;
    }
    return tableTitle;
  }

  @Override
  public void graph(XWPFDocument document) {}

  private float obtenerPorcentaje(String respuestas, List<R_RespuestasEsperadasPrueba> respEsperadas, R_Habilidad eje) {
    float nroBuenas = 0;
    float nroPreguntas = 0;
    for (int n = 0; n < respEsperadas.size(); n++) {
      final R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
      if (!resp.getAnulada()) {
        if (resp.getHabilidad_id() == eje.getId()) {
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
    final float porcentaje = nroBuenas / nroPreguntas * 100f;
    return porcentaje;
  }

  @Override
  public void page(XWPFDocument document) {
    if (resultado == null || resultado.isEmpty())
      return;
    final int nroRangos = rangos.size();
    XWPFParagraph paragraph = document.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.addCarriageReturn();
    int idxCurso = 0;
    for (final R_Curso curso : lstCursos) {
      final String title = getTiteTables(curso);
      if (title != null) {
        paragraph = document.createParagraph();
        run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setText(title);
        run.addCarriageReturn();
      }
      paragraph = document.createParagraph();
      run = paragraph.createRun(); // create new run
      run.addCarriageReturn();
      int nRow = 0;
      for (final R_Habilidad eje : resultado.keySet()) {
        final List<OTAcumulador> lstValues = resultado.get(eje);
        final OTAcumulador ot = lstValues.get(idxCurso);
        if (ot == null || ot.getNroPersonas() == null || ot.getNroPersonas().length == 0)
          continue;
        nRow++;
      }
      final XWPFTable table = document.createTable(nRow + 3, nroRangos + 1);
      WordUtil.setTableFormat(table, 3, 0);
      XWPFTableRow tableRow = table.getRow(0);
      tableRow.getCell(0).setText("HABILIDADES");
      tableRow.getCell(1).setText("Curso: " + curso.getName());
      WordUtil.mergeCellHorizontally(table, 0, 1, nroRangos);
      WordUtil.mergeCellVertically(table, 0, 0, 2);
      tableRow = table.getRow(2);
      if (curso.getCiclo_id() == InformeManager.CICLO_7) {
        tableRow.getCell(1).setText("<NT1");
        XWPFParagraph para = tableRow.getCell(1).getParagraphs().get(0);
        para.setAlignment(ParagraphAlignment.LEFT);
        tableRow.getCell(2).setText("NT1");
        para = tableRow.getCell(2).getParagraphs().get(0);
        para.setAlignment(ParagraphAlignment.LEFT);
        tableRow.getCell(3).setText("NT2");
        para = tableRow.getCell(3).getParagraphs().get(0);
        para.setAlignment(ParagraphAlignment.LEFT);
        tableRow.getCell(4).setText("1ºEGB");
        para = tableRow.getCell(4).getParagraphs().get(0);
        para.setAlignment(ParagraphAlignment.LEFT);
      } else {
        for (int n = 0; n < rangos.size(); n++) {
          tableRow.getCell(n + 1).setText(rangos.get(n).getName());
          final XWPFParagraph para = tableRow.getCell(n + 1).getParagraphs().get(0);
          para.setAlignment(ParagraphAlignment.LEFT);
        }
      }
      boolean isTitleSetted = false;
      nRow = 3;
      for (final R_Habilidad eje : resultado.keySet()) {
        final List<OTAcumulador> lstValues = resultado.get(eje);
        final OTAcumulador ot = lstValues.get(idxCurso);
        if (ot == null || ot.getNroPersonas() == null || ot.getNroPersonas().length == 0)
          continue;
        if (!isTitleSetted) {
          tableRow = table.getRow(1);
          tableRow.getCell(1).setText("Nº estudiantes alcanzan nivel de logro: " + ot.getAlumnos());
          WordUtil.mergeCellHorizontally(table, 1, 1, nroRangos);
          isTitleSetted = true;
        }
        tableRow = table.getRow(nRow++);
        tableRow.getCell(0).setText(eje.getName().toUpperCase());
        final int[] values = ot.getNroPersonas();
        for (int n = 0; n < values.length; n++) {
          tableRow.getCell(n + 1).setText(String.format("%d", values[n]));
        }
      }
      idxCurso++;
    }
  }

  protected Map<R_Habilidad, List<OTAcumulador>> procesar(List<R_EvaluacionPrueba> evaluacionesPrueba) {
    nroCursos = lstCursos.size();
    nroRangos = rangos.size();
    Map<R_Habilidad, List<OTAcumulador>> mapHab = new HashMap<>();
    for (final R_EvaluacionPrueba eval : evaluacionesPrueba) {
      final List<R_PruebaRendida> pruebasRendidas = obtenerPruebasRendidas(eval);
      final List<R_RespuestasEsperadasPrueba> respEsperadas = obtenerRespEsperadas(eval);
      mapHab = obtenerHabilidades(mapHab, respEsperadas);

      final int index = IntStream.range(0, lstCursos.size())
          .filter(i -> eval.getCurso_id().equals(lstCursos.get(i).getId())).findFirst().orElse(-1);

      if (index == -1)
        continue;


      for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {
        if (tipoAlumno.getId() != Constants.PIE_ALL && tipoAlumno.getId() != pruebaRendida.getTipoalumno_id()) {
          continue;
        }

        final String respuestas = pruebaRendida.getRespuestas();
        if (respuestas == null || respuestas.isEmpty())
          continue;

        for (final R_Habilidad eje : mapHab.keySet()) {
          final List<OTAcumulador> lstEjes = mapHab.get(eje);
          final OTAcumulador otAcum = lstEjes.get(index);
          final float porcentaje = obtenerPorcentaje(respuestas, respEsperadas, eje);
          for (int idx = 0; idx < nroRangos; idx++) {
            final R_RangoEvaluacion rango = rangos.get(idx);
            if (rango.isInside(porcentaje)) {
              otAcum.getNroPersonas()[idx] = otAcum.getNroPersonas()[idx] + 1;
              break;
            }
          }
          lstEjes.set(index, otAcum);
        }
      }
    }
    return mapHab;
  }


  private Map<R_Habilidad, List<OTAcumulador>> obtenerHabilidades(Map<R_Habilidad, List<OTAcumulador>> mapHabs,
      List<R_RespuestasEsperadasPrueba> respEsperadas) {

    for (R_RespuestasEsperadasPrueba resp : respEsperadas) {
      Long id = resp.getHabilidad_id();
      List<Long> ejesId = mapHabs.keySet().stream().map(e -> e.getId()).collect(Collectors.toList());
      int idx = IntStream.range(0, ejesId.size()).filter(i -> ejesId.get(i) == id).findFirst().orElse(-1);

      if (idx == -1) {
        R_Habilidad eje = PersistenceServiceFactory.getPersistenceService().findByIdSynchro(R_Habilidad.class, id);
        List<OTAcumulador> lHabs = Stream.generate(OTAcumulador::new).limit(nroCursos).collect(Collectors.toList());
        for (OTAcumulador ot : lHabs) {
          int[] nroPersonas = new int[nroRangos];
          Arrays.fill(nroPersonas, 0);
          ot.setNroPersonas(nroPersonas);
        }
        mapHabs.put(eje, lHabs);
      }
    }
    return mapHabs;
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

}
