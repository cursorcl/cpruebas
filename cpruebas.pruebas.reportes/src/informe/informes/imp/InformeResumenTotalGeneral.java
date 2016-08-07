package informe.informes.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
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
import cl.eos.util.Pair;
import informe.InformeManager;
import informe.informes.IInforme;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 * 
 * @author curso
 *
 */
public class InformeResumenTotalGeneral implements IInforme {

    private static final String ALUMNOS = "ALUMNOS";
    private static final String REPORBADOS = "Reprobados";
    private static final String APROBADOS = "Aprobados";
    private static final String EVALUADOS = "Evaluados";
    private static final String TOTAL_ESCUELA = "Total Escuela";
    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";
    final static String[] TABLE_HEAD = { " ", TOTAL_ESCUELA, EVALUADOS, APROBADOS, REPORBADOS };
    static Logger log = Logger.getLogger(InformeResumenTotalGeneral.class);
    private TipoAlumno tipoAlumno;
    private Colegio colegio;
    private Asignatura asignatura;
    private Map<String, Pair<Integer, Float>> resultado;
    private List<EvaluacionEjeTematico> evalEjeTematico;

    public InformeResumenTotalGeneral() {
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
        evalEjeTematico = PersistenceServiceFactory.getPersistenceService().findAllSynchro(EvaluacionEjeTematico.class);
        List<EvaluacionPrueba> evaluaciones =  (List<EvaluacionPrueba>) (Object)PersistenceServiceFactory.getPersistenceService()
                .findSynchro("EvaluacionPrueba.findEvaluacionByColegioAsig", params);
        resultado = procesar(evaluaciones);
    }

    public void page(XWPFDocument document) {

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

        int nroColumnas = resultado.size() + 1;
        XWPFTable table = document.createTable(5, nroColumnas);
        WordUtil.setTableFormat(table, 2, 0);

        table.getRow(0).setRepeatHeader(true);
        table.getRow(0).setCantSplitRow(false);
        table.getRow(0).getCell(0).setText(TOTAL_ESCUELA);
        table.getRow(0).getCell(2).setText(ALUMNOS);
        table.getRow(0).getCell(5).setText("ESTÁNDARES DE APRENDIZAJE");
        table.getRow(2).getCell(0).setText(ALUMNOS);
        table.getRow(3).getCell(0).setText("%");
        table.getRow(4).getCell(3).setText("100%");
        table.getRow(4).getCell(5).setText("100%");

        XWPFTableRow tableRow = table.getRow(1);
        int n = 1;
        for (Map.Entry<String, Pair<Integer, Float>> entry : resultado.entrySet()) {
            tableRow.getCell(n++).setText(entry.getKey());
        }

        WordUtil.mergeCellHorizontally(table, 0, 0, 1);
        WordUtil.mergeCellHorizontally(table, 1, 0, 1);
        WordUtil.mergeCellVertically(table, 0, 0, 1);
        WordUtil.mergeCellHorizontally(table, 0, 2, 4);
        WordUtil.mergeCellHorizontally(table, 0, 5, nroColumnas - 1);
        WordUtil.mergeCellHorizontally(table, 4, 0, 2);
        WordUtil.mergeCellHorizontally(table, 4, 3, 4);
        WordUtil.mergeCellHorizontally(table, 4, 5, nroColumnas - 1);

        n = 1;
        for (Map.Entry<String, Pair<Integer, Float>> entry : resultado.entrySet()) {
            Pair<Integer, Float> res = entry.getValue();
            table.getRow(2).getCell(n).setText(String.format("%d", res.getFirst().intValue()));
            table.getRow(3).getCell(n++).setText(String.format("%d%%", res.getSecond().intValue()));

        }

        paragraph = document.createParagraph();
        paragraph.setStyle("Descripción");
        run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setText(String.format("Tabla  %d: TOTAL GENERAL %s en %s", InformeManager.TABLA++, colegio.getName(),
                asignatura.getName()));
        run.addCarriageReturn();

        Pair<Integer, Float> pEvaluados = resultado.get(EVALUADOS);
        Pair<Integer, Float> pAprobados = resultado.get(APROBADOS);
        paragraph = document.createParagraph();
        paragraph.setStyle("Normal");
        run = paragraph.createRun();
        String f = "De la matrícula total del establecimiento %s, se evaluaron %d alumnos que corresponden al %5.2f del total del liceo. El nivel de aprobación es de %5.2f.";
        String row = String.format(f, colegio.getName(), pEvaluados.getFirst(), pEvaluados.getSecond(),
                pAprobados.getSecond());
        run.setText(row);
        run.addCarriageReturn();
    }

    /**
     * Realiza el computo de los valores de la tabla de valores TOTAL GENERAL.
     * 
     * @param list
     *            lista de valores obtenidos desde la base de datos.
     * @return Mapa con valores <Titulo, Cantidad Alumnos>.
     */
    protected Map<String, Pair<Integer, Float>> procesar(List<EvaluacionPrueba> list) {
        int totalColAlumnos = 0;
        int totalColEvaluados = 0;
        int totalColAprobados = 0;
        int totalColReprobados = 0;

        Map<String, Pair<Integer, Float>> mResumen = getMap();

        for (EvaluacionPrueba evaluacion : list) {
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
                            Pair<Integer, Float> pair = mResumen.get(eET.getName());
                            pair.setFirst(pair.getFirst() + 1);
                        }
                    }
                }

            }
            totalColAlumnos = totalColAlumnos + totalAlumnos;
            totalColEvaluados = totalColEvaluados + totalEvaluados;
            totalColAprobados = totalColAprobados + totalAprobados;
            totalColReprobados = totalColReprobados + totalReprobados;

        }
        float porEvaluados = 100f * ((float) totalColEvaluados) / ((float) totalColAlumnos);
        float porAprobados = 100f * ((float) totalColAprobados) / ((float) totalColEvaluados);
        float porReporbados = 100f * ((float) totalColReprobados) / ((float) totalColEvaluados);

        mResumen.put(TOTAL_ESCUELA, new Pair<Integer, Float>(totalColAlumnos, 100f));
        mResumen.put(EVALUADOS, new Pair<Integer, Float>(totalColEvaluados, porEvaluados));
        mResumen.put(APROBADOS, new Pair<Integer, Float>(totalColAprobados, porAprobados));
        mResumen.put(REPORBADOS, new Pair<Integer, Float>(totalColReprobados, porReporbados));

        for (Object obj : evalEjeTematico) {
            EvaluacionEjeTematico eET = (EvaluacionEjeTematico) obj;
            Pair<Integer, Float> pair = mResumen.get(eET.getName());
            pair.setSecond(100f * pair.getFirst().floatValue() / (float) totalColEvaluados);
        }

        return mResumen;
    }

    /**
     * Obtiene el mapa que se usa para los titulos de la tabla. Establece
     * nombres de las columnas y coloca valor 0, para luego realizar la
     * acumulación.
     * 
     * @return Mapa de titulos de la tabla y valores.
     */
    private Map<String, Pair<Integer, Float>> getMap() {
        Map<String, Pair<Integer, Float>> mResumen = new LinkedHashMap<>();
        mResumen.put(TOTAL_ESCUELA, new Pair<Integer, Float>(0, 0f));
        mResumen.put(EVALUADOS, new Pair<Integer, Float>(0, 0f));
        mResumen.put(APROBADOS, new Pair<Integer, Float>(0, 0f));
        mResumen.put(REPORBADOS, new Pair<Integer, Float>(0, 0f));

        List<EvaluacionEjeTematico> ejes = new ArrayList<>();
        for (Object obj : evalEjeTematico) {
            EvaluacionEjeTematico eje = (EvaluacionEjeTematico) obj;
            ejes.add(eje);
        }

        ejes = ejes.stream().sorted(Comparadores.comparaEvaluacionEjeTematico()).collect(Collectors.toList());
        for (EvaluacionEjeTematico eje : ejes) {
            mResumen.put(eje.getName(), new Pair<Integer, Float>(0, 0f));
        }
        return mResumen;

    }
}
