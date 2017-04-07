package informe.informes.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
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
public class InformeEjeEvaluacion implements IInforme {

  private static final String ASIGNATURA_ID = "asignatura_id";
  private static final String COLEGIO_ID = "colegio_id";

  static Logger log = Logger.getLogger(InformeEjeEvaluacion.class.getName());
  private R_TipoAlumno tipoAlumno;
  private Map<Long, List<OTAcumulador>> resultado;
  private List<R_RangoEvaluacion> rangos;
  private List<R_Curso> lstCursos;
  private boolean kinder_ciclo;
  private boolean media_ciclo;
  private boolean basica_ciclo;

  public InformeEjeEvaluacion() {
    super();
  }

  @Override
  public void execute(R_TipoAlumno tipoAlumno, R_Colegio colegio, R_Asignatura asignatura) {
    rangos =
        PersistenceServiceFactory.getPersistenceService().findAllSynchro(R_RangoEvaluacion.class);
    rangos.stream().sorted(Comparadores.rangoEvaluacionComparator()).collect(Collectors.toList());

    this.tipoAlumno = tipoAlumno;

    final Map<String, Object> params = new HashMap<>();
    params.put(InformeEjeEvaluacion.COLEGIO_ID, colegio.getId());
    params.put(InformeEjeEvaluacion.ASIGNATURA_ID, asignatura.getId());
    final List<R_EvaluacionPrueba> lst = PersistenceServiceFactory.getPersistenceService()
        .findByParamsSynchro(R_EvaluacionPrueba.class, params);

    if (lst == null || lst.isEmpty())
      return;
    params.clear();
    params.put(COLEGIO_ID, colegio.getId());
    lstCursos = PersistenceServiceFactory.getPersistenceService().findByParamsSynchro(R_Curso.class,
        params);

    if (Objects.isNull(lst) || Objects.isNull(lstCursos) || lstCursos.isEmpty() || lst.isEmpty())
      return;
    resultado = procesar(lst);
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
      tableTitle =
          String.format("Tabla  %d: RESULTADOS DESDE 1º a 4º MEDIO", InformeManager.TABLA++);
      media_ciclo = true;
    }
    return tableTitle;
  }

  @Override
  public void graph(XWPFDocument document) {

  }

  private float obtenerPorcentaje(String respuestas,
      List<R_RespuestasEsperadasPrueba> respEsperadas, Long eje) {
    float nroBuenas = 0;
    float nroPreguntas = 0;
    for (int n = 0; n < respEsperadas.size(); n++) {
      final R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
      if (!resp.getAnulada()) {

        if (resp.getEjetematico_id().equals(eje)) {

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
    
    Long[] ids = resultado.keySet().toArray(new Long[resultado.keySet().size()]);
    List<R_Ejetematico> lstEjes = PersistenceServiceFactory.getPersistenceService()
        .findByAllIdSynchro(R_Ejetematico.class, ids);
    
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
      for (final Long eje : resultado.keySet()) {
        final List<OTAcumulador> lstValues = resultado.get(eje);
        final OTAcumulador ot = lstValues.get(idxCurso);
        if (ot == null || ot.getNroPersonas() == null || ot.getNroPersonas().length == 0)
          continue;
        nRow++;
      }

      final XWPFTable table = document.createTable(nRow + 3, nroRangos + 1);
      WordUtil.setTableFormat(table, 3, 0);

      XWPFTableRow tableRow = table.getRow(0);
      tableRow.getCell(0).setText("Eje Aprendizaje");
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
      for (final Long eje : resultado.keySet()) {
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
        R_Ejetematico oEje = lstEjes.stream().filter(e -> e.getId().equals(eje)).findFirst().orElse(null);
        
        tableRow.getCell(0).setText(oEje.getName().toUpperCase());
        final int[] values = ot.getNroPersonas();
        for (int n = 0; n < values.length; n++) {
          tableRow.getCell(n + 1).setText(String.format("%d", values[n]));
        }
      }
      idxCurso++;

    }
  }

  protected Map<Long, List<OTAcumulador>> procesar(List<R_EvaluacionPrueba> evaluacionesPrueba) {

    final int nroCursos = lstCursos.size();
    final int nroRangos = rangos.size();
    final Map<Long, List<OTAcumulador>> cursosXeje = new HashMap<>();
    Map<Long, List<R_RespuestasEsperadasPrueba>> mapRespEsperadas = new HashMap<>();
    for (final R_EvaluacionPrueba eval : evaluacionesPrueba) {
      Map<String, Object> params =
          MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", eval.getId()).build();
      final List<R_PruebaRendida> pruebasRendidas = PersistenceServiceFactory
          .getPersistenceService().findByParamsSynchro(R_PruebaRendida.class, params);


      List<R_RespuestasEsperadasPrueba> respEsperadas = mapRespEsperadas.get(eval.getPrueba_id());
      if (respEsperadas == null) {
        params =
            MapBuilder.<String, Object>unordered().put("prueba_id", eval.getPrueba_id()).build();
        respEsperadas = PersistenceServiceFactory.getPersistenceService()
            .findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
        mapRespEsperadas.put(eval.getPrueba_id(), respEsperadas);
      }
      for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {
        if (pruebaRendida.getAlumno_id() == null) {
          continue;
        }
        if (tipoAlumno.getId() != Constants.PIE_ALL
            && tipoAlumno.getId() != pruebaRendida.getTipoalumno_id()) {
          continue;
        }

        R_Curso curso = lstCursos.stream().filter(c -> c.getId().equals(eval.getCurso_id()))
            .findFirst().orElse(null);
        int index = lstCursos.indexOf(curso);

        if (index == -1)
          continue;

        final String respuestas = pruebaRendida.getRespuestas();
        if (respuestas == null || respuestas.isEmpty()) {
          continue;
        }

        for (int n = 0; n < respEsperadas.size(); n++) {
          // Sumando a ejes tematicos
          final Long eje = respEsperadas.get(n).getEjetematico_id();
          if (!cursosXeje.containsKey(eje)) {
            final List<OTAcumulador> lista = new ArrayList<OTAcumulador>(nroCursos);
            for (int idx = 0; idx < nroCursos; idx++) {
              lista.add(null);
            }
            cursosXeje.put(eje, lista);
          }
          final List<OTAcumulador> lstEjes = cursosXeje.get(eje);
          OTAcumulador otEjeEval = lstEjes.get(index);
          if (otEjeEval == null) {
            otEjeEval = new OTAcumulador();
            final int[] nroPersonas = new int[nroRangos];
            Arrays.fill(nroPersonas, 0);
            otEjeEval.setNroPersonas(nroPersonas);
            lstEjes.set(index, otEjeEval);
          }
        }
        for (final Long eje : cursosXeje.keySet()) {
          final List<OTAcumulador> lstEjes = cursosXeje.get(eje);
          final OTAcumulador otEjeEval = lstEjes.get(index);
          final float porcentaje = obtenerPorcentaje(respuestas, respEsperadas, eje);

          for (int idx = 0; idx < nroRangos; idx++) {
            final R_RangoEvaluacion rango = rangos.get(idx);
            if (rango.isInside(porcentaje)) {
              otEjeEval.getNroPersonas()[idx] = otEjeEval.getNroPersonas()[idx] + 1;
              break;
            }
          }

          lstEjes.set(index, otEjeEval);
        }

      }
    }
    return cursosXeje;
  }

}
