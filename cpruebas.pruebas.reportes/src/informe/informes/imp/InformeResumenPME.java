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
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import informe.InformeManager;
import informe.informes.IInforme;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 * 
 * @author curso
 *
 */
public class InformeResumenPME implements IInforme {

    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";

    static Logger log = Logger.getLogger(InformeResumenPME.class);
    private TipoAlumno tipoAlumno;
    private Colegio colegio;
    private Asignatura asignatura;
    private Map<Curso, Map<RangoEvaluacion, OTRangoCurso>> resultado;
    private List<RangoEvaluacion> rangos;

    public InformeResumenPME() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {
        rangos = PersistenceServiceFactory.getPersistenceService().findAllSynchro(RangoEvaluacion.class);

        if (Objects.isNull(rangos) || rangos.isEmpty())
            return;

        rangos = rangos.stream().sorted(Comparadores.rangoEvaluacionComparator()).collect(Collectors.toList());
        this.tipoAlumno = tipoAlumno;
        this.colegio = colegio;
        this.asignatura = asignatura;
        Map<String, Object> params = new HashMap<>();
        params.put(COLEGIO_ID, colegio.getId());
        params.put(ASIGNATURA_ID, asignatura.getId());
        List<EvaluacionPrueba> evaluaciones = (List<EvaluacionPrueba>) (Object) PersistenceServiceFactory
                .getPersistenceService().findSynchro("EvaluacionPrueba.findEvaluacionByColegioAsig", params);

        if (Objects.isNull(evaluaciones) || evaluaciones.isEmpty())
            return;

        resultado = procesar(evaluaciones);
    }

    public void page(XWPFDocument document) {

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addCarriageReturn();

        XWPFTable table = document.createTable(resultado.size() + 1, rangos.size() + 1);
        WordUtil.setTableFormat(table, 1, 0);

        XWPFTableRow tableRow = table.getRow(0);
        tableRow.getCell(0).setText("Cursos");
        for (int n = 0; n < rangos.size(); n++) {
            tableRow.getCell(n + 1).setText(rangos.get(n).getName());
        }

        int row = 1;
        for (Curso curso : resultado.keySet()) {
            tableRow = table.getRow(row++);
            tableRow.getCell(0).setText(curso.getName().toUpperCase());
            Map<RangoEvaluacion, OTRangoCurso> item = resultado.get(curso);
            int col = 1;
            for (RangoEvaluacion rango : rangos) {
                OTRangoCurso rngCurso = item.get(rango);
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

    protected Map<Curso, Map<RangoEvaluacion, OTRangoCurso>> procesar(List<EvaluacionPrueba> list) {

        Map<Curso, Map<RangoEvaluacion, OTRangoCurso>> pmeCursos = new HashMap<>();

        NivelEvaluacion nivel = ((EvaluacionPrueba) list.get(0)).getPrueba().getNivelEvaluacion();
        for (EvaluacionPrueba evaluacion : list) {
            List<PruebaRendida> rendidas = evaluacion.getPruebasRendidas();
            for (PruebaRendida pruebaRendida : rendidas) {
                Alumno alumno = pruebaRendida.getAlumno();
                if (alumno == null || alumno.getTipoAlumno() == null) {
                    continue;
                }
                if (tipoAlumno.getId() != Constants.PIE_ALL && tipoAlumno.getId() != alumno.getTipoAlumno().getId()) {
                    continue;
                }
                float porcentaje = (float) pruebaRendida.getBuenas()
                        / (float) pruebaRendida.getEvaluacionPrueba().getPrueba().getNroPreguntas() * 100f;
                RangoEvaluacion rango = nivel.getRango(porcentaje);
                Curso curso = pruebaRendida.getEvaluacionPrueba().getCurso();

                if (pmeCursos.containsKey(curso)) {
                    Map<RangoEvaluacion, OTRangoCurso> prangos = pmeCursos.get(curso);
                    if (prangos.containsKey(rango)) {
                        OTRangoCurso uRango = prangos.get(rango);
                        uRango.setTotal(uRango.getTotal() + 1);
                    } else {
                        OTRangoCurso rangoCurso = new OTRangoCurso();
                        rangoCurso.setCurso(curso);
                        rangoCurso.setRango(rango);
                        rangoCurso.setTotal(rangoCurso.getTotal() + 1);
                        prangos.put(rango, rangoCurso);
                    }
                } else {
                    OTRangoCurso rangoCurso = new OTRangoCurso();
                    rangoCurso.setCurso(curso);
                    rangoCurso.setRango(rango);
                    rangoCurso.setTotal(rangoCurso.getTotal() + 1);

                    Map<RangoEvaluacion, OTRangoCurso> pmeRangos = new HashMap<>();
                    pmeRangos.put(rango, rangoCurso);
                    pmeCursos.put(curso, pmeRangos);
                }
            }
        }
        return pmeCursos;
    }

}
