package informe.informes.imp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cl.eos.common.Constants;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
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
public class InformeEjesXCurso implements IInforme {

    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";

    static Logger log = Logger.getLogger(InformeEjesXCurso.class);
    private TipoAlumno tipoAlumno;
    private Colegio colegio;
    private Asignatura asignatura;
    private Map<EjeTematico, List<OTPreguntasEjes>> resultado;
    private List<RangoEvaluacion> rangos;
    private List<Curso> cursoList;
    private int nroCursos;

    public InformeEjesXCurso() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {
        rangos = PersistenceServiceFactory.getPersistenceService().findAllSynchro(RangoEvaluacion.class);
        
        if(Objects.isNull(rangos) || rangos.isEmpty())
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

        if(Objects.isNull(evaluaciones) ||  evaluaciones.isEmpty())
            return;
        resultado = procesar(evaluaciones);
    }

    public void page(XWPFDocument document) {

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addCarriageReturn();

        int nRows = resultado.keySet().size();

        int n = 0;
        while (n < nroCursos) {
            paragraph = document.createParagraph();
            run = paragraph.createRun(); // create new run
            run.addCarriageReturn();
            
            int cursos = Math.min(12, nroCursos - n);
            XWPFTable table = document.createTable(nRows + 1, cursos + 1);
            WordUtil.setTableFormat(table, 1, 0);

            XWPFTableRow tableRow = table.getRow(0);
            tableRow.getCell(0).getParagraphs().get(0).getRuns().get(0).setText("EJE");
            int col = 1;
            for (int r = n; r < n + cursos; r++) {
                String title = cursoList.get(r).getName();
                tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0).setText(title);
            }
            int row = 1;
            for (EjeTematico eje : resultado.keySet()) {
                tableRow = table.getRow(row++);
                tableRow.getCell(0).getParagraphs().get(0).getRuns().get(0).setText(eje.getName().toUpperCase());
                List<OTPreguntasEjes> notas = resultado.get(eje);
                col = 1;
                for (int r = n; r < n + cursos; r++) {
                    OTPreguntasEjes nota = notas.get(r);
                    if(nota != null)
                        tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0).setText(String.format("%5.1f", nota.getSlogrado()));
//                        tableRow.getCell(col++).setText(String.format("%5.2f", nota.getSlogrado()));
                    else
                        tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0).setText(String.format("%5.1f", 0f));
//                        tableRow.getCell(col++).setText(String.format("%5.2f", 0f));
                }
            }

            n = n + 12;
        }
        paragraph = document.createParagraph();
        paragraph.setStyle("Descripción");
        run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setText(String.format("Tabla  %d: DISTRIBUCIÓN DE RESULTADOS POR EJES DE APRENDIZAJES %s en %s",
                InformeManager.TABLA++, colegio.getName(), asignatura.getName()));
        run.addCarriageReturn();
        paragraph = document.createParagraph();
        paragraph.setStyle("Normal");
        run = paragraph.createRun();
        run.addCarriageReturn();
    }

    protected Map<EjeTematico, List<OTPreguntasEjes>> procesar(List<EvaluacionPrueba> evaluacionesPrueba) {

        Map<EjeTematico, List<OTPreguntasEjes>> mapEjes = new HashMap<>();

        cursoList = evaluacionesPrueba.stream().map(EvaluacionPrueba::getCurso).distinct()
                .sorted(Comparadores.comparaResumeCurso()).collect(Collectors.toList());
        nroCursos = cursoList.size();

        int[] totalAlumnos = new int[nroCursos];
        Arrays.fill(totalAlumnos, 0);
        int[] alumnosEvaluados = new int[nroCursos];
        Arrays.fill(alumnosEvaluados, 0);

        for (EvaluacionPrueba eval : evaluacionesPrueba) {
            eval.getPruebasRendidas().size();
            List<PruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
            eval.getPrueba().getRespuestas().size();
            List<RespuestasEsperadasPrueba> respEsperadas = eval.getPrueba().getRespuestas();
            int index = cursoList.indexOf(eval.getCurso());

            if (index == -1) {
                continue;
            }
            totalAlumnos[index] = 0;

            for (Alumno alumno : eval.getCurso().getAlumnos())
                if (tipoAlumno.getId() == Constants.PIE_ALL || alumno.getTipoAlumno().getId().equals(tipoAlumno))
                    totalAlumnos[index] = totalAlumnos[index] + 1;

            for (PruebaRendida pruebaRendida : pruebasRendidas) {
                if (tipoAlumno.getId() != Constants.PIE_ALL
                        && !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno))
                    continue;

                alumnosEvaluados[index] = alumnosEvaluados[index] + 1;

                String respuestas = pruebaRendida.getRespuestas();
                if (respuestas == null || respuestas.isEmpty())
                    continue;

                for (int n = 0; n < respEsperadas.size(); n++) {
                    EjeTematico eje = respEsperadas.get(n).getEjeTematico();
                    if (!mapEjes.containsKey(eje)) {
                        mapEjes.put(eje,
                                Stream.generate(OTPreguntasEjes::new).limit(nroCursos).collect(Collectors.toList()));
                    }
                }
                for (EjeTematico eje : mapEjes.keySet()) {
                    List<OTPreguntasEjes> lstEjes = mapEjes.get(eje);
                    OTPreguntasEjes otEje = lstEjes.get(index);
                    if (otEje == null || otEje.getEjeTematico() == null) {
                        otEje = new OTPreguntasEjes(eje);
                        lstEjes.set(index, otEje);
                    }
                    Pair<Integer, Integer> buenasTotal = obtenerBuenasTotales(respuestas, respEsperadas, eje);
                    otEje.setBuenas(otEje.getBuenas() + buenasTotal.getFirst());
                    otEje.setTotal(otEje.getTotal() + buenasTotal.getSecond());
                    lstEjes.set(index, otEje);
                }
            }
        }

        return mapEjes;
    }

    /**
     * Este metodo evalua la cantidad de buenas de un String de respuesta
     * contrastado contra las respuestas eperadas.
     * 
     * @param respuestas
     *            Las respuestas del alumno.
     * @param respEsperadas
     *            Las respuestas correctas definidas en la prueba.
     * @param eje
     *            El Eje tematico en base al que se realiza el calculo.
     * @return Par <Preguntas buenas, Total de Preguntas> del eje.
     */
    private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas,
            List<RespuestasEsperadasPrueba> respEsperadas, EjeTematico eje) {
        int nroBuenas = 0;
        int nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (!resp.isAnulada()) {
                if (resp.getEjeTematico().equals(eje)) {
                    if (respuestas.length() > n) {
                        String sResp = respuestas.substring(n, n + 1);
                        if ("+".equals(sResp) || resp.getRespuesta().equalsIgnoreCase(sResp)) {
                            nroBuenas++;
                        }
                    }
                    nroPreguntas++;
                }
            }
        }
        return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
    }

}
