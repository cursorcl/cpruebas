package informe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
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

/**
 * Esta clase genera los valores para el resumen.
 * 
 * @author curso
 *
 */
public class InformeResumen implements Informe {


    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";
    final static String[] TABLE_HEAD = { "CURSO", "TOTAL ALUMNOS", "ALUMNOS EVALUADOS", "TOTAL APROBADOS",
            "TOTAL REPROBADOS", "% ALUMNOS EVALUADOS", "% REPROBADOS", "% APROBADOS" };
    static Logger log = Logger.getLogger(InformeResumen.class);
    private TipoAlumno tipoAlumno;
    private Colegio colegio;
    private Asignatura asignatura;
    private List<OTResumenColegio> resultado;
    

    public InformeResumen() {
        super();
    }

    @Override
    public void execute(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {
        this.tipoAlumno = tipoAlumno;
        this.colegio = colegio;
        this.asignatura = asignatura;
        Map<String, Object> params = new HashMap<>();
        params.put(COLEGIO_ID, colegio.getId());
        params.put(ASIGNATURA_ID, asignatura.getId());
        List<Object> lst = PersistenceServiceFactory.getPersistenceService().findSynchro("EvaluacionPrueba.findEvaluacionByColegioAsig", params);
        resultado = procesar(lst);
    }
    
    public void page(XWPFDocument document) {
        
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun(); // create new run
        paragraph.setStyle("Heading1");
        run.setText("INFORME DE RESULTADOS A NIVEL DE ESTABLECIMIENTO (" + colegio.getName().toUpperCase() + ")" );
        run.addCarriageReturn();
        paragraph.setStyle("Heading2");
        run.setText("Instrumento de Evaluación y Resultados Obtenidos en la asignatura de "
                + asignatura.getName().toUpperCase() + ".");
        paragraph.setStyle("Normal");

        XWPFTable table = document.createTable(resultado.size() + 1, TABLE_HEAD.length);
        XWPFTableRow tableRow = table.getRow(0);
        for (int n = 0; n < TABLE_HEAD.length; n++) {
            tableRow.getCell(n).setText(TABLE_HEAD[n]);
            tableRow.getCell(n).setColor("777777");
        }

        resultado = resultado.stream().sorted(Comparadores.comparaResumeColegio())
                .collect(Collectors.toList());
        for (int n = 0; n < resultado.size(); n++) {
            tableRow = table.getRow(n + 1);
            OTResumenColegio ot = resultado.get(n);
            tableRow.getCell(0).setText(ot.getCurso().getName());
        }

    }

    protected ArrayList<OTResumenColegio> procesar(List<Object> list) {
        ArrayList<OTResumenColegio> lstCursos = new ArrayList<>();
        int totalColAlumnos = 0;
        int totalColEvaluados = 0;
        int totalColAprobados = 0;
        int totalColReprobados = 0;
        for (Object evaluacionPrueba : list) {
            EvaluacionPrueba evaluacion = (EvaluacionPrueba) evaluacionPrueba;
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
