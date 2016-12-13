package informe.informes.imp;

import java.util.ArrayList;
import java.util.Arrays;
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
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SHabilidad;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.SRangoEvaluacion;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.view.ots.ejeevaluacion.OTAcumulador;
import informe.InformeManager;
import informe.informes.IInforme;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 *
 * @author colegio
 *
 */
public class InformeHabilidades implements IInforme {

    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";

    static Logger log = Logger.getLogger(InformeHabilidades.class);
    private STipoAlumno tipoAlumno;
    private Map<SHabilidad, List<OTAcumulador>> resultado;
    private List<SRangoEvaluacion> rangos;
    private List<SCurso> lstCursos;
    private boolean kinder_ciclo;
    private boolean media_ciclo;
    private boolean basica_ciclo;

    public InformeHabilidades() {
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

        final Map<String, Object> params = new HashMap<>();
        params.put(InformeHabilidades.COLEGIO_ID, colegio.getId());
        params.put(InformeHabilidades.ASIGNATURA_ID, asignatura.getId());
        final List<SEvaluacionPrueba> evaluaciones = (List<SEvaluacionPrueba>) (Object) PersistenceServiceFactory
                .getPersistenceService().findSynchro("SEvaluacionPrueba.findEvaluacionByColegioAsig", params);
        if (evaluaciones == null || evaluaciones.isEmpty())
            return;
        params.clear();
        params.put("colegioId", colegio.getId());
        lstCursos = (List<SCurso>) (Object) PersistenceServiceFactory.getPersistenceService()
                .findSynchro("SCurso.findByColegio", params);

        if (Objects.isNull(evaluaciones) || Objects.isNull(lstCursos) || lstCursos.isEmpty() || evaluaciones.isEmpty())
            return;

        resultado = procesar(evaluaciones);
    }

    private String getTiteTables(SCurso curso) {
        String tableTitle = null;
        if (curso.getCiclo().getId() < InformeManager.CICLO_7 && !basica_ciclo) {
            tableTitle = String.format("Tabla  %d: RESULTADOS ENSEÑANZA BÁSICA", InformeManager.TABLA++);
            basica_ciclo = true;
        }
        if (curso.getCiclo().getId() == InformeManager.CICLO_7 && !kinder_ciclo) {
            tableTitle = String.format("Tabla  %d: RESULTADOS DE PRE-BÁSICA", InformeManager.TABLA++);
            kinder_ciclo = true;
        }
        if (curso.getCiclo().getId() > InformeManager.CICLO_7 && !media_ciclo) {
            tableTitle = String.format("Tabla  %d: RESULTADOS DESDE 1º a 4º MEDIO", InformeManager.TABLA++);
            media_ciclo = true;
        }
        return tableTitle;
    }

    @Override
    public void graph(XWPFDocument document) {
        // TODO Auto-generated method stub

    }

    private float obtenerPorcentaje(String respuestas, List<SRespuestasEsperadasPrueba> respEsperadas, SHabilidad eje) {
        float nroBuenas = 0;
        float nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            final SRespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (!resp.isAnulada()) {

                if (resp.getHabilidad().equals(eje)) {

                    if (respuestas.length() > n) {
                        final String sResp = respuestas.substring(n, n + 1);
                        if ("+".equals(sResp) || resp.getRespuesta().equalsIgnoreCase(sResp)) {
                            nroBuenas++;
                        }
                    }

                    nroPreguntas++;
                }
            }
        }
        final float porcentaje = nroBuenas / nroPreguntas * 100f;
        return porcentaje;
    }

    @Override
    public void page(XWPFDocument document) {

        if (resultado == null || resultado.isEmpty())
            return;
        
        final int nroRangos = rangos.size();

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addCarriageReturn();

        int idxCurso = 0;
        for (final SCurso curso : lstCursos) {

            final String title = getTiteTables(curso);
            if (title != null) {
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run.setText(title);
                run.addCarriageReturn();
            }

            paragraph = document.createParagraph();
            run = paragraph.createRun(); // create new run
            run.addCarriageReturn();

            int nRow = 0;
            for (final SHabilidad eje : resultado.keySet()) {
                final List<OTAcumulador> lstValues = resultado.get(eje);
                final OTAcumulador ot = lstValues.get(idxCurso);
                if (ot == null || ot.getNroPersonas() == null || ot.getNroPersonas().length == 0)
                    continue;
                nRow++;
            }

            final XWPFTable table = document.createTable(nRow + 3, nroRangos + 1);
            WordUtil.setTableFormat(table, 3, 0);

            XWPFTableRow tableRow = table.getRow(0);
            tableRow.getCell(0).setText("HABILIDADES");
            tableRow.getCell(1).setText("SCurso: " + curso.getName());

            WordUtil.mergeCellHorizontally(table, 0, 1, nroRangos);
            WordUtil.mergeCellVertically(table, 0, 0, 2);

            tableRow = table.getRow(2);

            if (curso.getCiclo().getId() == InformeManager.CICLO_7) {
                tableRow.getCell(1).setText("<NT1");
                XWPFParagraph para = tableRow.getCell(1).getParagraphs().get(0);
                para.setAlignment(ParagraphAlignment.LEFT);
                tableRow.getCell(2).setText("NT1");
                para = tableRow.getCell(2).getParagraphs().get(0);
                para.setAlignment(ParagraphAlignment.LEFT);
                tableRow.getCell(3).setText("NT2");
                para = tableRow.getCell(3).getParagraphs().get(0);
                para.setAlignment(ParagraphAlignment.LEFT);
                tableRow.getCell(4).setText("1ºEGB");
                para = tableRow.getCell(4).getParagraphs().get(0);
                para.setAlignment(ParagraphAlignment.LEFT);
            } else {
                for (int n = 0; n < rangos.size(); n++) {
                    tableRow.getCell(n + 1).setText(rangos.get(n).getName());
                    final XWPFParagraph para = tableRow.getCell(n + 1).getParagraphs().get(0);
                    para.setAlignment(ParagraphAlignment.LEFT);
                }
            }
            boolean isTitleSetted = false;
            nRow = 3;
            for (final SHabilidad eje : resultado.keySet()) {
                final List<OTAcumulador> lstValues = resultado.get(eje);
                final OTAcumulador ot = lstValues.get(idxCurso);
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
                final int[] values = ot.getNroPersonas();
                for (int n = 0; n < values.length; n++) {
                    tableRow.getCell(n + 1).setText(String.format("%d", values[n]));
                }
            }

            idxCurso++;

        }
    }

    protected Map<SHabilidad, List<OTAcumulador>> procesar(List<SEvaluacionPrueba> evaluacionesPrueba) {

        final int nroCursos = lstCursos.size();
        final int nroRangos = rangos.size();
        final Map<SHabilidad, List<OTAcumulador>> cursosXeje = new HashMap<>();

        for (final SEvaluacionPrueba eval : evaluacionesPrueba) {
            eval.getPruebasRendidas().size();
            final List<SPruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
            eval.getPrueba().getRespuestas().size();
            final List<SRespuestasEsperadasPrueba> respEsperadas = eval.getPrueba().getRespuestas();
            for (final SPruebaRendida pruebaRendida : pruebasRendidas) {
                if (pruebaRendida.getAlumno() == null) {
                    continue;
                }
                if (tipoAlumno.getId() != Constants.PIE_ALL
                        && tipoAlumno.getId() != pruebaRendida.getAlumno().getTipoAlumno().getId()) {
                    continue;
                }

                final int index = lstCursos.indexOf(pruebaRendida.getAlumno().getCurso());

                if (index == -1)
                    continue;

                final String respuestas = pruebaRendida.getRespuestas();
                if (respuestas == null || respuestas.isEmpty()) {
                    continue;
                }

                for (int n = 0; n < respEsperadas.size(); n++) {
                    // Sumando a ejes tematicos
                    final SHabilidad eje = respEsperadas.get(n).getHabilidad();
                    if (!cursosXeje.containsKey(eje)) {
                        final List<OTAcumulador> lista = new ArrayList<OTAcumulador>(nroCursos);
                        for (int idx = 0; idx < nroCursos; idx++) {
                            lista.add(null);
                        }
                        cursosXeje.put(eje, lista);
                    }
                    final List<OTAcumulador> lstEjes = cursosXeje.get(eje);
                    OTAcumulador otEjeEval = lstEjes.get(index);
                    if (otEjeEval == null) {
                        otEjeEval = new OTAcumulador();
                        final int[] nroPersonas = new int[nroRangos];
                        Arrays.fill(nroPersonas, 0);
                        otEjeEval.setNroPersonas(nroPersonas);
                        lstEjes.set(index, otEjeEval);
                    }
                }
                for (final SHabilidad eje : cursosXeje.keySet()) {
                    final List<OTAcumulador> lstEjes = cursosXeje.get(eje);
                    final OTAcumulador otEjeEval = lstEjes.get(index);
                    final float porcentaje = obtenerPorcentaje(respuestas, respEsperadas, eje);

                    for (int idx = 0; idx < nroRangos; idx++) {
                        final SRangoEvaluacion rango = rangos.get(idx);
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

}
