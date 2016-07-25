package informe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.view.ots.ejeevaluacion.OTAcumulador;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 * 
 * @author curso
 *
 */
public class InformeHabilidades implements Informe {

    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";

    static Logger log = Logger.getLogger(InformeHabilidades.class);
    private TipoAlumno tipoAlumno;
    private Colegio colegio;
    private Asignatura asignatura;
    private Map<Habilidad, List<OTAcumulador>> resultado;
    private List<RangoEvaluacion> rangos;
    private List<Curso> lstCursos;

    public InformeHabilidades() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {
        rangos = PersistenceServiceFactory.getPersistenceService().findAllSynchro(RangoEvaluacion.class);
        rangos.stream().sorted(Comparadores.rangoEvaluacionComparator()).collect(Collectors.toList());

        this.tipoAlumno = tipoAlumno;
        this.colegio = colegio;
        this.asignatura = asignatura;

        Map<String, Object> params = new HashMap<>();
        params.put(COLEGIO_ID, colegio.getId());
        params.put(ASIGNATURA_ID, asignatura.getId());
        List<EvaluacionPrueba> lst = (List<EvaluacionPrueba>) (Object) PersistenceServiceFactory.getPersistenceService()
                .findSynchro("EvaluacionPrueba.findEvaluacionByColegioAsig", params);

        params.clear();
        params.put("coelgioId", colegio.getId());
        lstCursos = (List<Curso>) (Object) PersistenceServiceFactory.getPersistenceService()
                .findSynchro("Curso.findByColegio", params);
        resultado = procesar(lst);
    }

    public void page(XWPFDocument document) {

        int nroRangos = rangos.size();

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun(); // create new run
        paragraph.setStyle("Heading1");
        run.setText("INFORME DE RESULTADOS A NIVEL DE ESTABLECIMIENTO (" + colegio.getName().toUpperCase() + ")");
        run.addCarriageReturn();
        paragraph.setStyle("Heading2");
        run.setText("Instrumento de Evaluación y Resultados Obtenidos en la asignatura de "
                + asignatura.getName().toUpperCase() + ".");
        paragraph.setStyle("Normal");
        paragraph.setAlignment(ParagraphAlignment.CENTER);

        int idxCurso = 0;
        for (Curso curso : lstCursos) {
            
            paragraph = document.createParagraph();
            run = paragraph.createRun(); // create new run
            run.addCarriageReturn();
            
            int nRow = 0;
            for (Habilidad eje : resultado.keySet())
            {
                List<OTAcumulador> lstValues = resultado.get(eje);
                OTAcumulador ot = lstValues.get(idxCurso);
                if (ot == null || ot.getNroPersonas() == null || ot.getNroPersonas().length == 0)
                    continue;
                nRow++;
            }
            
            XWPFTable table = document.createTable( nRow + 3, nroRangos + 1);
            WordUtil.setTableFormat(table, 3, 0);

            XWPFTableRow tableRow = table.getRow(0);
            tableRow.getCell(0).setText("Eje Aprendizaje");
            tableRow.getCell(1).setText("Curso: " + curso.getName());

            WordUtil.mergeCellHorizontally(table, 0, 1, nroRangos);
            WordUtil.mergeCellVertically(table, 0, 0, 2);

            tableRow = table.getRow(2);
            
            for (int n = 0; n < rangos.size(); n++) {
                tableRow.getCell(n + 1).setText(rangos.get(n).getName());
                XWPFParagraph para = tableRow.getCell(n + 1).getParagraphs().get(0);
                para.setAlignment(ParagraphAlignment.LEFT);
            }
            boolean isTitleSetted = false;
            nRow = 3;
            for (Habilidad eje : resultado.keySet()) {
                List<OTAcumulador> lstValues = resultado.get(eje);
                OTAcumulador ot = lstValues.get(idxCurso);
                if (ot == null || ot.getNroPersonas() == null || ot.getNroPersonas().length == 0)
                    continue;
                
                if (!isTitleSetted) {
                    tableRow = table.getRow(1);
                    tableRow.getCell(1).setText("Nº estudiantes alcanzan nivel de logro: " + ot.getAlumnos());
                    WordUtil.mergeCellHorizontally(table, 1, 1, nroRangos);
                    isTitleSetted = true;
                }
                
                tableRow = table.getRow(nRow++);
                tableRow.getCell(0).setText(eje.getName().toUpperCase());
                int[] values = ot.getNroPersonas();
                for (int n = 0; n < values.length; n++) {
                    tableRow.getCell(n + 1).setText(String.format("%d", values[n]));
                }
            }
            idxCurso++;

        }
    }

    protected Map<Habilidad, List<OTAcumulador>> procesar(List<EvaluacionPrueba> evaluacionesPrueba) {

        int nroCursos = lstCursos.size();
        int nroRangos = rangos.size();
        Map<Habilidad, List<OTAcumulador>> cursosXeje = new HashMap<>();

        for (EvaluacionPrueba eval : evaluacionesPrueba) {
            eval.getPruebasRendidas().size();
            List<PruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
            eval.getPrueba().getRespuestas().size();
            List<RespuestasEsperadasPrueba> respEsperadas = eval.getPrueba().getRespuestas();
            for (PruebaRendida pruebaRendida : pruebasRendidas) {
                if (pruebaRendida.getAlumno() == null) {
                    continue;
                }
                if (tipoAlumno.getId() != Constants.PIE_ALL
                        && tipoAlumno.getId() != pruebaRendida.getAlumno().getTipoAlumno().getId()) {
                    continue;
                }

                int index = lstCursos.indexOf(pruebaRendida.getAlumno().getCurso());

                if (index == -1)
                    continue;

                String respuestas = pruebaRendida.getRespuestas();
                if (respuestas == null || respuestas.isEmpty()) {
                    continue;
                }

                for (int n = 0; n < respEsperadas.size(); n++) {
                    // Sumando a ejes tematicos
                    Habilidad eje = respEsperadas.get(n).getHabilidad();
                    if (!cursosXeje.containsKey(eje)) {
                        List<OTAcumulador> lista = new ArrayList<OTAcumulador>(nroCursos);
                        for (int idx = 0; idx < nroCursos; idx++) {
                            lista.add(null);
                        }
                        cursosXeje.put(eje, lista);
                    }
                    List<OTAcumulador> lstEjes = cursosXeje.get(eje);
                    OTAcumulador otEjeEval = lstEjes.get(index);
                    if (otEjeEval == null) {
                        otEjeEval = new OTAcumulador();
                        int[] nroPersonas = new int[nroRangos];
                        Arrays.fill(nroPersonas, 0);
                        otEjeEval.setNroPersonas(nroPersonas);
                        lstEjes.set(index, otEjeEval);
                    }
                }
                for (Habilidad eje : cursosXeje.keySet()) {
                    List<OTAcumulador> lstEjes = cursosXeje.get(eje);
                    OTAcumulador otEjeEval = lstEjes.get(index);
                    float porcentaje = obtenerPorcentaje(respuestas, respEsperadas, eje);

                    for (int idx = 0; idx < nroRangos; idx++) {
                        RangoEvaluacion rango = rangos.get(idx);
                        if (rango.isInside(porcentaje)) {
                            otEjeEval.getNroPersonas()[idx] = otEjeEval.getNroPersonas()[idx] + 1;
                            break;
                        }
                    }

                    lstEjes.set(index, otEjeEval);
                }

            }
        }
        return cursosXeje;
    }

    private float obtenerPorcentaje(String respuestas, List<RespuestasEsperadasPrueba> respEsperadas, Habilidad eje) {
        float nroBuenas = 0;
        float nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (!resp.isAnulada()) {

                if (resp.getHabilidad().equals(eje)) {

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
        float porcentaje = nroBuenas / nroPreguntas * 100f;
        return porcentaje;
    }

}
