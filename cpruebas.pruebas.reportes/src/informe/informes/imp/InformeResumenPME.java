package informe.informes.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_TipoAlumno;
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

    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";

    static Logger log = Logger.getLogger(InformeResumenPME.class);
    private R_TipoAlumno tipoAlumno;
    private R_Colegio colegio;
    private R_Asignatura asignatura;
    private Map<R_Curso, Map<R_RangoEvaluacion, OTRangoCurso>> resultado;
    private List<R_RangoEvaluacion> rangos;

    public InformeResumenPME() {
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
        this.colegio = colegio;
        this.asignatura = asignatura;
        final Map<String, Object> params = new HashMap<>();
        params.put(InformeResumenPME.COLEGIO_ID, colegio.getId());
        params.put(InformeResumenPME.ASIGNATURA_ID, asignatura.getId());
        final List<R_EvaluacionPrueba> evaluaciones = (List<R_EvaluacionPrueba>) (Object) PersistenceServiceFactory
                .getPersistenceService().findByParamsSynchro(R_EvaluacionPrueba.class, params);
        if (evaluaciones == null || evaluaciones.isEmpty())
            return;
        if (Objects.isNull(evaluaciones) || evaluaciones.isEmpty())
            return;

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
        run.setText(String.format("Tabla  %d: INFORME PME %s en %s", InformeManager.TABLA++, colegio.getName(),
                asignatura.getName()));
        run.addCarriageReturn();
        paragraph = document.createParagraph();
        paragraph.setStyle("Normal");
        run = paragraph.createRun();
        run.addCarriageReturn();

    }

    protected Map<R_Curso, Map<R_RangoEvaluacion, OTRangoCurso>> procesar(List<R_EvaluacionPrueba> list) {

        final Map<R_Curso, Map<R_RangoEvaluacion, OTRangoCurso>> pmeCursos = new HashMap<>();

        final R_NivelEvaluacion nivel = list.get(0).getPrueba().getNivelEvaluacion();
        for (final R_EvaluacionPrueba evaluacion : list) {
            final List<R_PruebaRendida> rendidas = evaluacion.getPruebasRendidas();
            for (final R_PruebaRendida pruebaRendida : rendidas) {
                final R_Alumno alumno = pruebaRendida.getAlumno();
                if (alumno == null || alumno.getTipoAlumno() == null) {
                    continue;
                }
                if (tipoAlumno.getId() != Constants.PIE_ALL && tipoAlumno.getId() != alumno.getTipoAlumno().getId()) {
                    continue;
                }
                final float porcentaje = (float) pruebaRendida.getBuenas()
                        / (float) pruebaRendida.getEvaluacionPrueba().getPrueba().getNropreguntas() * 100f;
                final R_RangoEvaluacion rango = nivel.getRango(porcentaje);
                final R_Curso curso = pruebaRendida.getEvaluacionPrueba().getCurso();

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

}
