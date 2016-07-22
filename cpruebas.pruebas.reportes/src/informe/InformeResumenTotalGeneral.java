package informe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cl.eos.common.Constants;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 * 
 * @author curso
 *
 */
public class InformeResumenTotalGeneral implements Informe {

    private static final String REPORBADOS = "Reprobados";
    private static final String APROBADOS = "Aprobados";
    private static final String EVALUADOS = "Evaluados";
    private static final String TOTAL_ESCUELA = "Total Escuela";
    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";
    final static String[] TABLE_HEAD = { " ", TOTAL_ESCUELA, EVALUADOS, APROBADOS, REPORBADOS};
    static Logger log = Logger.getLogger(InformeResumenTotalGeneral.class);
    private TipoAlumno tipoAlumno;
    private Colegio colegio;
    private Asignatura asignatura;
    private Map<String, Integer> resultado;
    private List<Object> evalEjeTematico;

    public InformeResumenTotalGeneral() {
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
        evalEjeTematico = PersistenceServiceFactory.getPersistenceService().findAllSynchro(EvaluacionEjeTematico.class);
        List<Object> lst = PersistenceServiceFactory.getPersistenceService()
                .findSynchro("EvaluacionPrueba.findEvaluacionByColegioAsig", params);
        resultado = procesar(lst);
    }

    public void page(XWPFDocument document) {

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun(); // create new run
        paragraph.setStyle("Heading1");
        run.setText("INFORME DE RESULTADOS A NIVEL DE ESTABLECIMIENTO (" + colegio.getName().toUpperCase() + ")");
        run.addCarriageReturn();
        paragraph.setStyle("Heading2");
        run.setText("Instrumento de Evaluación y Resultados Obtenidos en la asignatura de "
                + asignatura.getName().toUpperCase() + ".");
        paragraph.setStyle("Normal");

        int nroColumnas = resultado.size() + 1;
        XWPFTable table = document.createTable(4, nroColumnas);
        for(int n = 0; n < nroColumnas; n++)
        {
            table.getRow(0).getCell(n).setColor("777777");
            table.getRow(1).getCell(n).setColor("777777");
        }
        
        XWPFTableCell cell = table.getRow(0).getCell(1);
        cell.setText("TOTAL ESCUELA");
        cell = table.getRow(0).getCell(2);
        cell.setText("ALUMNOS");
        cell = table.getRow(0).getCell(5);
        cell.setText("EVALUACIONES");
 
        WordUtil.mergeCellHorizontally(table, 0, 2, 3); 
        WordUtil.mergeCellHorizontally(table, 0, 4, nroColumnas - 5); 
        
        
        XWPFTableRow tableRow = table.getRow(1);
        int n = 1;
        for (Map.Entry<String, Integer> entry : resultado.entrySet())
        {
            table.getRow(1).getCell(n++).setText(entry.getKey());
        }
        

        
        

        // resultado =
        // resultado.stream().sorted(Comparadores.comparaResumeColegio())
        // .collect(Collectors.toList());
        // for (int n = 0; n < resultado.size(); n++) {
        // tableRow = table.getRow(n + 1);
        // OTResumenColegio ot = resultado.get(n);
        // tableRow.getCell(0).setText(ot.getCurso().getName());
        // }

    }

    /**
     * Realiza el computo de los valores de la tabla de valores TOTAL GENERAL.
     * @param list lista de valores obtenidos desde la base de datos.
     * @return Mapa con valores <Titulo, Cantidad Alumnos>.
     */
    protected Map<String, Integer> procesar(List<Object> list) {
        int totalColAlumnos = 0;
        int totalColEvaluados = 0;
        int totalColAprobados = 0;
        int totalColReprobados = 0;

        
        Map<String, Integer> mResumen = getMap();

        for (Object evaluacionPrueba : list) {
            EvaluacionPrueba evaluacion = (EvaluacionPrueba) evaluacionPrueba;
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

                Float pBuenas = pruebaRendida.getPbuenas();
                for (Object obj : evalEjeTematico) {
                    EvaluacionEjeTematico eET = (EvaluacionEjeTematico) obj;
                    if (eET.isInside(pBuenas)) {
                        if (mResumen.containsKey(eET.getName())) {
                            Integer valor = mResumen.get(eET.getName());
                            valor++;
                            mResumen.put(eET.getName(), valor);
                        } else {
                            mResumen.put(eET.getName(), 1);
                        }
                    }
                }

            }
            totalColAlumnos = totalColAlumnos + totalAlumnos;
            totalColEvaluados = totalColEvaluados + totalEvaluados;
            totalColAprobados = totalColAprobados + totalAprobados;
            totalColReprobados = totalColReprobados + totalReprobados;

        }
        mResumen.put(TOTAL_ESCUELA, totalColAlumnos);
        mResumen.put(EVALUADOS,totalColEvaluados);
        mResumen.put(APROBADOS,totalColAprobados);
        mResumen.put(REPORBADOS,totalColReprobados);

        return mResumen;
    }

    /**
     * Obtiene el mapa que se usa para los titulos de la tabla.
     * Establece nombres de las columnas y coloca valor 0, para luego realizar la acumulación. 
     * @return Mapa de titulos de la tabla y valores. 
     */
    private Map<String, Integer> getMap()
    {
        Map<String, Integer> mResumen = new LinkedHashMap<>();
        mResumen.put(TOTAL_ESCUELA, 0);
        mResumen.put(EVALUADOS, 0);
        mResumen.put(APROBADOS, 0);
        mResumen.put(REPORBADOS, 0);
        
        List<EvaluacionEjeTematico> ejes = new ArrayList<>();
        for (Object obj : evalEjeTematico) {
            EvaluacionEjeTematico eje = (EvaluacionEjeTematico)obj;
            ejes.add(eje);
        }
        
        ejes = ejes.stream().sorted(Comparadores.comparaEvaluacionEjeTematico()).collect(Collectors.toList());
        for(EvaluacionEjeTematico eje: ejes)
        {
            mResumen.put(eje.getName(), 0);
        }
        return mResumen;

    }
}
