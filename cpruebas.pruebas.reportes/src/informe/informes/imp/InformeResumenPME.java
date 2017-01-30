package informe.informes.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cl.eos.common.Constants;
import cl.eos.ot.OTRangoCurso;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.EntityUtils;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.MapBuilder;
import informe.InformeManager;
import informe.informes.IInforme;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 *
 * @author colegio
 *
 */
public class InformeResumenPME implements IInforme {

  private static final String ASIGNATURA_ID = "asignatura_id";
  private static final String COLEGIO_ID = "colegio_id";

  static Logger log = Logger.getLogger(InformeResumenPME.class);
  private R_TipoAlumno tipoAlumno;
  private R_Colegio colegio;
  private R_Asignatura asignatura;
  private Map<R_Curso, Map<R_RangoEvaluacion, OTRangoCurso>> resultado;
  private List<R_RangoEvaluacion> rangos;
  private R_Prueba prueba;
  private List<R_Curso> cursoList;

  public InformeResumenPME() {
    super();
  }

  @Override
  public void execute(R_TipoAlumno tipoAlumno, R_Colegio colegio, R_Asignatura asignatura) {



    if (Objects.isNull(rangos) || rangos.isEmpty())
      return;

    rangos = rangos.stream().sorted(Comparadores.rangoEvaluacionComparator())
        .collect(Collectors.toList());
    this.tipoAlumno = tipoAlumno;
    this.colegio = colegio;
    this.asignatura = asignatura;
    Map<String, Object> params = new HashMap<>();
    params.put(InformeResumenPME.COLEGIO_ID, colegio.getId());
    params.put(InformeResumenPME.ASIGNATURA_ID, asignatura.getId());
    final List<R_EvaluacionPrueba> evaluaciones = PersistenceServiceFactory.getPersistenceService()
        .findByParamsSynchro(R_EvaluacionPrueba.class, params);
    if (evaluaciones == null || evaluaciones.isEmpty())
      return;
    if (Objects.isNull(evaluaciones) || evaluaciones.isEmpty())
      return;

    prueba = PersistenceServiceFactory.getPersistenceService().findByIdSynchro(R_Prueba.class,
        evaluaciones.get(0).getPrueba_id());

    params = MapBuilder.<String, Object>unordered()
        .put("nivelevaluacion_id", prueba.getNivelevaluacion_id()).build();
    rangos = PersistenceServiceFactory.getPersistenceService()
        .findByParamsSynchro(R_RangoEvaluacion.class, params);

    resultado = procesar(evaluaciones);
  }

  @Override
  public void graph(XWPFDocument document) {

  }

  @Override
  public void page(XWPFDocument document) {

    if (resultado == null || resultado.isEmpty())
      return;

    XWPFParagraph paragraph = document.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.addCarriageReturn();

    final XWPFTable table = document.createTable(resultado.size() + 1, rangos.size() + 1);
    WordUtil.setTableFormat(table, 1, 0);

    XWPFTableRow tableRow = table.getRow(0);
    tableRow.getCell(0).setText("Cursos");
    for (int n = 0; n < rangos.size(); n++) {
      tableRow.getCell(n + 1).setText(rangos.get(n).getName());
    }

    int row = 1;
    for (final R_Curso curso : resultado.keySet()) {
      tableRow = table.getRow(row++);
      tableRow.getCell(0).setText(curso.getName().toUpperCase());
      final Map<R_RangoEvaluacion, OTRangoCurso> item = resultado.get(curso);
      int col = 1;
      for (final R_RangoEvaluacion rango : rangos) {
        final OTRangoCurso rngCurso = item.get(rango);
        if (rngCurso != null)
          tableRow.getCell(col++).setText(String.format("%d", rngCurso.getTotal()));
        else
          tableRow.getCell(col++).setText(String.format("%d", 0));
      }
    }
    paragraph = document.createParagraph();
    paragraph.setStyle("Descripción");
    run = paragraph.createRun();
    paragraph.setAlignment(ParagraphAlignment.CENTER);
    run.setText(String.format("Tabla  %d: INFORME PME %s en %s", InformeManager.TABLA++,
        colegio.getName(), asignatura.getName()));
    run.addCarriageReturn();
    paragraph = document.createParagraph();
    paragraph.setStyle("Normal");
    run = paragraph.createRun();
    run.addCarriageReturn();

  }

  protected Map<R_Curso, Map<R_RangoEvaluacion, OTRangoCurso>> procesar(
      List<R_EvaluacionPrueba> evaluacionesPrueba) {

    final Map<R_Curso, Map<R_RangoEvaluacion, OTRangoCurso>> pmeCursos = new HashMap<>();
    cursoList = obtenerCursos(evaluacionesPrueba);

    
    for (final R_EvaluacionPrueba evaluacion : evaluacionesPrueba) {

      final List<R_PruebaRendida> pruebasRendidas = obtenerPruebasRendidas(evaluacion);

      for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {
        if (tipoAlumno.getId() != Constants.PIE_ALL
            && tipoAlumno.getId() != pruebaRendida.getTipoalumno_id()) {
          continue;
        }
        final float porcentaje =
            (float) pruebaRendida.getBuenas() / (float) prueba.getNropreguntas() * 100f;

        final R_RangoEvaluacion rango = EntityUtils.getRango(porcentaje, rangos);
        
        final int index = IntStream.range(0, cursoList.size())
            .filter(i -> evaluacion.getCurso_id().equals(cursoList.get(i).getId())).findFirst().orElse(-1);

        if (index == -1) {
          continue;
        }
        final R_Curso curso = cursoList.get(index);

        if (pmeCursos.containsKey(curso)) {
          final Map<R_RangoEvaluacion, OTRangoCurso> prangos = pmeCursos.get(curso);
          if (prangos.containsKey(rango)) {
            final OTRangoCurso uRango = prangos.get(rango);
            uRango.setTotal(uRango.getTotal() + 1);
          } else {
            final OTRangoCurso rangoCurso = new OTRangoCurso();
            rangoCurso.setCurso(curso);
            rangoCurso.setRango(rango);
            rangoCurso.setTotal(rangoCurso.getTotal() + 1);
            prangos.put(rango, rangoCurso);
          }
        } else {
          final OTRangoCurso rangoCurso = new OTRangoCurso();
          rangoCurso.setCurso(curso);
          rangoCurso.setRango(rango);
          rangoCurso.setTotal(rangoCurso.getTotal() + 1);

          final Map<R_RangoEvaluacion, OTRangoCurso> pmeRangos = new HashMap<>();
          pmeRangos.put(rango, rangoCurso);
          pmeCursos.put(curso, pmeRangos);
        }
      }
    }
    return pmeCursos;
  }

  private List<R_PruebaRendida> obtenerPruebasRendidas(R_EvaluacionPrueba eval) {
    Map<String, Object> params =
        MapBuilder.<String, Object>unordered().put("prueba_id", eval.getPrueba_id()).build();
    return PersistenceServiceFactory.getPersistenceService()
        .findByParamsSynchro(R_PruebaRendida.class, params);
  }
  
  private List<R_Curso> obtenerCursos(List<R_EvaluacionPrueba> evaluacionesPrueba) {
    Long[] ids = evaluacionesPrueba.stream().map(e -> e.getCurso_id()).toArray(n -> new Long[n]);

    List<R_Curso> cursos = PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Curso.class, ids);
    cursos = cursos.stream().distinct().sorted(Comparadores.comparaResumeCurso()).collect(Collectors.toList());

    return cursos;
  }
}
