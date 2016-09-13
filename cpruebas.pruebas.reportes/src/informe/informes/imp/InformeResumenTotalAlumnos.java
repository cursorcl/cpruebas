package informe.informes.imp;

import java.util.ArrayList;
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
import cl.eos.ot.OTResumenColegio;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.TipoAlumno;
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
public class InformeResumenTotalAlumnos implements IInforme {

    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";
    final static String[] TABLE_HEAD = { "CURSOS", "TOTAL", "EVALUADOS", "APROBADOS", "REPROBADOS", "% EVALUADOS",
            "% APROBADOS", "% REPROBADOS" };

    static Logger log = Logger.getLogger(InformeResumenTotalAlumnos.class);
    private TipoAlumno tipoAlumno;
    private Colegio colegio;
    private Asignatura asignatura;
    private List<OTResumenColegio> resultado;

    public InformeResumenTotalAlumnos() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {
        this.tipoAlumno = tipoAlumno;
        this.colegio = colegio;
        this.asignatura = asignatura;
        Map<String, Object> params = new HashMap<>();
        params.put(COLEGIO_ID, colegio.getId());
        params.put(ASIGNATURA_ID, asignatura.getId());
        List<EvaluacionPrueba> evaluaciones = (List<EvaluacionPrueba>) (Object) PersistenceServiceFactory
                .getPersistenceService().findSynchro("EvaluacionPrueba.findEvaluacionByColegioAsig", params);
        if(evaluaciones == null || evaluaciones.isEmpty())
            return;
        if (Objects.isNull(evaluaciones) || evaluaciones.isEmpty())
            return;
        
        resultado = procesar(evaluaciones);
    }

    public void page(XWPFDocument document) {

        resultado = resultado.stream().sorted(Comparadores.comparaResumeColegio()).collect(Collectors.toList());

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setStyle("PEREKE-TITULO");
        XWPFRun run = paragraph.createRun(); // create new run
        run.setText("INFORME DE RESULTADOS A NIVEL DE ESTABLECIMIENTO ");
        run.addCarriageReturn();
        run.setText(colegio.getName().toUpperCase());

        paragraph = document.createParagraph();
        paragraph.setStyle("PEREKE-SUBTITULO");
        run = paragraph.createRun(); // create new run
        run.setText("Instrumento de Evaluación y Resultados Obtenidos en la asignatura de "
                + asignatura.getName().toUpperCase() + ".");
        
        paragraph = document.createParagraph();
        paragraph.setStyle("Normal");
        run = paragraph.createRun();
        run.addCarriageReturn();
        

        XWPFTable table = document.createTable(resultado.size() + 2, TABLE_HEAD.length);
        WordUtil.setTableFormat(table, 2, 1);

        XWPFTableRow tableRow = table.getRow(0);
        tableRow.getCell(1).setText("ALUMNOS");
        WordUtil.mergeCellHorizontally(table, 0, 1, 7);

        tableRow = table.getRow(1);
        for (int n = 0; n < TABLE_HEAD.length; n++) {
            tableRow.getCell(n).setText(TABLE_HEAD[n]);
        }

        for (int n = 0; n < resultado.size(); n++) {
            tableRow = table.getRow(n + 2);
            OTResumenColegio ot = resultado.get(n);
            tableRow.getCell(0).setText(ot.getCurso().getName());
            tableRow.getCell(1).setText(String.format("%d", ot.getTotalAlumnos()));
            tableRow.getCell(2).setText(String.format("%d", ot.getTotalEvaluados()));
            tableRow.getCell(3).setText(String.format("%d", ot.getTotalAprobados()));
            tableRow.getCell(4).setText(String.format("%d", ot.getTotalReprobados()));
            tableRow.getCell(5).setText(String.format("%5.2f", ot.getPorcAlumnosEvaluados()));
            tableRow.getCell(6).setText(String.format("%5.2f", ot.getPorcAlumnosAprobados()));
            tableRow.getCell(7).setText(String.format("%5.2f", ot.getPorcAlumnosReprobados()));
        }
        paragraph = document.createParagraph();
        paragraph.setStyle("Descripción");
        run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setText(String.format("Tabla  %d: TOTAL ALUMNOS %s en %s", InformeManager.TABLA++, colegio.getName(),
                asignatura.getName()));
        run.addCarriageReturn();
        paragraph = document.createParagraph();
        paragraph.setStyle("Normal");
        run = paragraph.createRun();
        run.addCarriageReturn();

    }

    protected ArrayList<OTResumenColegio> procesar(List<EvaluacionPrueba> list) {
        ArrayList<OTResumenColegio> lstCursos = new ArrayList<>();
        int totalColAlumnos = 0;
        int totalColEvaluados = 0;
        int totalColAprobados = 0;
        int totalColReprobados = 0;
        for (EvaluacionPrueba evaluacion : list) {
            OTResumenColegio resumenCurso = new OTResumenColegio();
            resumenCurso.setColegio(evaluacion.getColegio());
            resumenCurso.setCurso(evaluacion.getCurso());

            int totalAlumnos = evaluacion.getCurso().getAlumnos().size();
            int totalEvaluados = 0;
            int totalAprobados = 0;
            int totalReprobados = 0;

            List<PruebaRendida> rendidas = evaluacion.getPruebasRendidas();
            for (PruebaRendida pruebaRendida : rendidas) {
                Alumno alumno = pruebaRendida.getAlumno();
                if (alumno == null || alumno.getTipoAlumno() == null) {
                    log.error("Alumno NULO");
                    totalAlumnos--;
                    continue;
                }
                if (tipoAlumno.getId() != Constants.PIE_ALL && tipoAlumno.getId() != alumno.getTipoAlumno().getId()) {
                    totalAlumnos--;
                    continue;
                }
                totalEvaluados++;
                if (pruebaRendida.getNota() >= 4) {
                    totalAprobados++;
                } else {
                    totalReprobados++;
                }
            }

            resumenCurso.setTotalAlumnos(totalAlumnos);
            resumenCurso.setTotalEvaluados(totalEvaluados);
            resumenCurso.setTotalAprobados(totalAprobados);
            resumenCurso.setTotalReprobados(totalReprobados);
            lstCursos.add(resumenCurso);

            totalColAlumnos = totalColAlumnos + totalAlumnos;
            totalColEvaluados = totalColEvaluados + totalEvaluados;
            totalColAprobados = totalColAprobados + totalAprobados;
            totalColReprobados = totalColReprobados + totalReprobados;

        }
        OTResumenColegio resumenTotal = new OTResumenColegio();
        Curso curso = new Curso();
        curso.setId(Long.MAX_VALUE);
        curso.setName("Total");
        resumenTotal.setCurso(curso);
        resumenTotal.setTotalAlumnos(totalColAlumnos);
        resumenTotal.setTotalEvaluados(totalColEvaluados);
        resumenTotal.setTotalAprobados(totalColAprobados);
        resumenTotal.setTotalReprobados(totalColReprobados);
        lstCursos.add(resumenTotal);
        return lstCursos;
    }

}
