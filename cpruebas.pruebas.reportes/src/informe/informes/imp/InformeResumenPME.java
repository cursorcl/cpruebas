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
import cl.eos.persistence.models.SAlumno;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SNivelEvaluacion;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.SRangoEvaluacion;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
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
    private STipoAlumno tipoAlumno;
    private SColegio colegio;
    private SAsignatura asignatura;
    private Map<SCurso, Map<SRangoEvaluacion, OTRangoCurso>> resultado;
    private List<SRangoEvaluacion> rangos;

    public InformeResumenPME() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(STipoAlumno tipoAlumno, SColegio colegio, SAsignatura asignatura) {
        
        rangos = PersistenceServiceFactory.getPersistenceService().findAllSynchro(SRangoEvaluacion.class);
        

        if (Objects.isNull(rangos) || rangos.isEmpty())
            return;

        rangos = rangos.stream().sorted(Comparadores.rangoEvaluacionComparator()).collect(Collectors.toList());
        this.tipoAlumno = tipoAlumno;
        this.colegio = colegio;
        this.asignatura = asignatura;
        final Map<String, Object> params = new HashMap<>();
        params.put(InformeResumenPME.COLEGIO_ID, colegio.getId());
        params.put(InformeResumenPME.ASIGNATURA_ID, asignatura.getId());
        final List<SEvaluacionPrueba> evaluaciones = (List<SEvaluacionPrueba>) (Object) PersistenceServiceFactory
                .getPersistenceService().findSynchro("SEvaluacionPrueba.findEvaluacionByColegioAsig", params);
        if (evaluaciones == null || evaluaciones.isEmpty())
            return;
        if (Objects.isNull(evaluaciones) || evaluaciones.isEmpty())
            return;

        resultado = procesar(evaluaciones);
    }

    @Override
    public void graph(XWPFDocument document) {
        // TODO Auto-generated method stub

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
        for (final SCurso curso : resultado.keySet()) {
            tableRow = table.getRow(row++);
            tableRow.getCell(0).setText(curso.getName().toUpperCase());
            final Map<SRangoEvaluacion, OTRangoCurso> item = resultado.get(curso);
            int col = 1;
            for (final SRangoEvaluacion rango : rangos) {
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

    protected Map<SCurso, Map<SRangoEvaluacion, OTRangoCurso>> procesar(List<SEvaluacionPrueba> list) {

        final Map<SCurso, Map<SRangoEvaluacion, OTRangoCurso>> pmeCursos = new HashMap<>();

        final SNivelEvaluacion nivel = list.get(0).getPrueba().getNivelEvaluacion();
        for (final SEvaluacionPrueba evaluacion : list) {
            final List<SPruebaRendida> rendidas = evaluacion.getPruebasRendidas();
            for (final SPruebaRendida pruebaRendida : rendidas) {
                final SAlumno alumno = pruebaRendida.getAlumno();
                if (alumno == null || alumno.getTipoAlumno() == null) {
                    continue;
                }
                if (tipoAlumno.getId() != Constants.PIE_ALL && tipoAlumno.getId() != alumno.getTipoAlumno().getId()) {
                    continue;
                }
                final float porcentaje = (float) pruebaRendida.getBuenas()
                        / (float) pruebaRendida.getEvaluacionPrueba().getPrueba().getNroPreguntas() * 100f;
                final SRangoEvaluacion rango = nivel.getRango(porcentaje);
                final SCurso curso = pruebaRendida.getEvaluacionPrueba().getCurso();

                if (pmeCursos.containsKey(curso)) {
                    final Map<SRangoEvaluacion, OTRangoCurso> prangos = pmeCursos.get(curso);
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

                    final Map<SRangoEvaluacion, OTRangoCurso> pmeRangos = new HashMap<>();
                    pmeRangos.put(rango, rangoCurso);
                    pmeCursos.put(curso, pmeRangos);
                }
            }
        }
        return pmeCursos;
    }

}
