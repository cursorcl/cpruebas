package informe.informes.imp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.MapBuilder;
import cl.eos.util.Pair;
import informe.InformeManager;
import informe.informes.IInforme;
import ot.XItemObjetivo;
import ot.XItemTablaObjetivo;
import ot.XUtilReportBuilder;
import utils.ChartsUtil;
import utils.WordUtil;

public class InformeXObjetivos_Nivel implements IInforme {

  private static final String ASIGNATURA_ID = "asignatura_id";
  private static final String COLEGIO_ID = "colegio_id";
  private R_Colegio colegio;
  private R_Asignatura asignatura;
  Pair<List<R_TipoCurso>, List<XItemTablaObjetivo>> reporte;

  @Override
  public void execute(R_TipoAlumno tipoAlumno, R_Colegio colegio, R_Asignatura asignatura) {
    this.colegio = colegio;
    this.asignatura = asignatura;
    Map<String, Object> params = new HashMap<>();
    params.put(InformeXObjetivos_Nivel.COLEGIO_ID, colegio.getId());
    params.put(InformeXObjetivos_Nivel.ASIGNATURA_ID, asignatura.getId());

    final List<R_EvaluacionPrueba> evaluaciones = PersistenceServiceFactory.getPersistenceService()
        .findByParamsSynchro(R_EvaluacionPrueba.class, params);
    
    
    
    if (evaluaciones == null || evaluaciones.isEmpty()) {
      return;
    }
    params = MapBuilder.<String, Object>unordered().put("prueba_id", evaluaciones.get(0).getPrueba_id()).build();
    List<R_RespuestasEsperadasPrueba> lstRespEsperadas = PersistenceServiceFactory.getPersistenceService().findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);

    
    final List<R_PruebaRendida> pRendidas = new ArrayList<>();
    for (R_EvaluacionPrueba evaluacionPrueba : evaluaciones) {
      final List<R_PruebaRendida> rendidas = obtenerPruebasRendidas(evaluacionPrueba);
      pRendidas.addAll(rendidas);
    }

    if (pRendidas != null && !pRendidas.isEmpty()) {
      reporte = XUtilReportBuilder.reporteColegioxNivel(pRendidas, evaluaciones, lstRespEsperadas, tipoAlumno.getId());
    }
  }

  @Override
  public void page(XWPFDocument document) {
    if (reporte == null || reporte.getSecond().isEmpty())
      return;

    List<R_TipoCurso> tipoCursos = reporte.getFirst();
    List<XItemTablaObjetivo> resultado = reporte.getSecond();

    XWPFParagraph paragraph = document.createParagraph();

    paragraph.setStyle("PEREKE-TITULO");
    XWPFRun run = paragraph.createRun(); // create new run
    run.setText("INFORME DE RESULTADOS X OBJETIVOS y NIVEL");
    run.addCarriageReturn();
    run.setText(colegio.getName().toUpperCase());

    paragraph = document.createParagraph();
    paragraph.setStyle("PEREKE-SUBTITULO");
    run = paragraph.createRun(); // create new run
    run.setText("Instrumento de Evaluación y Resultados Obtenidos en la asignatura de "
        + asignatura.getName().toUpperCase() + ".");
    int idx = 0;
    for (R_TipoCurso tipoCurso : tipoCursos) {

      paragraph = document.createParagraph();
      paragraph.setStyle("Normal");
      run = paragraph.createRun();
      run.addCarriageReturn();

      final int nroColumnas = 5;
      final int nroRows = resultado.size() + 2;
      final XWPFTable table = document.createTable(nroRows, nroColumnas);
      WordUtil.setTableFormat(table, 2, 0);

      table.getRow(0).setRepeatHeader(true);
      table.getRow(0).setCantSplitRow(false);
      table.getRow(0).getCell(0).setText("Objetivos");
      WordUtil.mergeCellVertically(table, 0, 0, 1);
      XWPFTableRow tRowHeader1 = table.getRow(0);
      XWPFTableRow tRowHeader2 = table.getRow(1);

      int c = 1;

      tRowHeader1.getCell(c).setText(tipoCurso.getName());
      WordUtil.mergeCellHorizontally(table, 0, c, c + 3);
      tRowHeader2.getCell(c + 0).setText("Ejes");
      tRowHeader2.getCell(c + 1).setText("Habilidades");
      tRowHeader2.getCell(c + 2).setText("Preguntas");
      tRowHeader2.getCell(c + 3).setText("% Aprobación");

      int r = 2;
      for (XItemTablaObjetivo item : resultado) {
        XWPFTableRow tRow = table.getRow(r);
        tRow.getCell(0).setText(item.getObjetivo().getName());

        if (item == null || item.getItems() == null || item.getItems().get(idx) == null) {
          tRow.getCell(c + 0).setText("");
          tRow.getCell(c + 1).setText("");
          tRow.getCell(c + 2).setText("");
          tRow.getCell(c + 3).setText(String.format("%5.2f", 0f));

        } else {
          XItemObjetivo value = item.getItems().get(idx);
          tRow.getCell(c + 0).setText(value.getEjesAsociados());
          tRow.getCell(c + 1).setText(value.getHabilidades());
          tRow.getCell(c + 2).setText(value.getPreguntas());
          tRow.getCell(c + 3).setText(String.format("%5.2f", value.getPorcentajeAprobacion()));
        }
        r++;
      }

      paragraph = document.createParagraph();
      paragraph.setStyle("Descripción");
      run = paragraph.createRun();
      paragraph.setAlignment(ParagraphAlignment.CENTER);
      run.setText(
          String.format("Tabla  %d: Porcentaje aprobación por objetivo curso %s, asignatura %s",
              InformeManager.TABLA++, tipoCurso.getName(), asignatura.getName()));
      run.addCarriageReturn();
      idx++;
    }

  }

  @Override
  public void graph(XWPFDocument document) {
    if (reporte == null || reporte.getSecond().isEmpty())
      return;

    final XWPFParagraph paragraph = document.createParagraph();
    paragraph.setStyle("PEREKE-TITULO");

    List<R_TipoCurso> tiposCurso = reporte.getFirst();
    List<XItemTablaObjetivo> resultado = reporte.getSecond();

    // Creo la lista de tipo cursos.
    List<String> titles = tiposCurso.stream().map(t -> t.getName()).collect(Collectors.toList());
    final Map<String, List<Double>> values = new HashMap<>();

    for (XItemTablaObjetivo objetivo : resultado) {

      List<Double> porcentajes = new ArrayList<Double>();
      values.put(objetivo.getObjetivo().getName(), porcentajes);
      for (final XItemObjetivo xiob : objetivo.getItems()) {
        if (xiob == null) {
          porcentajes.add(new Double(0));
        } else {
          porcentajes.add(new Double(xiob.getPorcentajeAprobacion()));
        }
      }
    }
    try {
      final File file = ChartsUtil.createBarChartSeries("% ALUMNOS", titles, values);
      WordUtil.addImage(file, "TOTAL ALUMNOS", paragraph);
    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final InvalidFormatException e) {
      e.printStackTrace();
    }

  }
  
  private List<R_PruebaRendida> obtenerPruebasRendidas(R_EvaluacionPrueba eval) {
    Map<String, Object> params =
        MapBuilder.<String, Object>unordered().put("prueba_id", eval.getPrueba_id()).build();
    return PersistenceServiceFactory.getPersistenceService()
        .findByParamsSynchro(R_PruebaRendida.class, params);
  }
}
