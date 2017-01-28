package informe.informes.imp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cl.eos.common.Constants;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.Pair;
import informe.InformeManager;
import informe.informes.IInforme;
import utils.ChartsUtil;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 *
 * @author colegio
 *
 */
public class InformeEjesXCurso implements IInforme {

    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";

    static Logger log = Logger.getLogger(InformeEjesXCurso.class);
    private R_TipoAlumno tipoAlumno;
    private R_Colegio colegio;
    private R_Asignatura asignatura;
    private Map<R_Ejetematico, List<OTPreguntasEjes>> resultado;
    private List<R_RangoEvaluacion> rangos;
    private List<R_Curso> cursoList;
    private int nroCursos;

    public InformeEjesXCurso() {
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
        params.put(InformeEjesXCurso.COLEGIO_ID, colegio.getId());
        params.put(InformeEjesXCurso.ASIGNATURA_ID, asignatura.getId());
        final List<R_EvaluacionPrueba> evaluaciones = (List<R_EvaluacionPrueba>) (Object) PersistenceServiceFactory
                .getPersistenceService().findSynchro("R_EvaluacionPrueba.findEvaluacionByColegioAsig", params);

        if (evaluaciones == null || evaluaciones.isEmpty())
            return;

        if (Objects.isNull(evaluaciones) || evaluaciones.isEmpty())
            return;
        resultado = procesar(evaluaciones);
    }

    @Override
    public void graph(XWPFDocument document) {
        if (resultado == null || resultado.isEmpty())
            return;

        final XWPFParagraph paragraph = document.createParagraph();

        paragraph.setStyle("PEREKE-TITULO");
        final List<String> titles = new ArrayList<>();
        final List<Double> values = new ArrayList<>();

        for (final R_Ejetematico eje : resultado.keySet()) {
            final List<OTPreguntasEjes> percents = resultado.get(eje);
            double cantidad = 0;
            double percent = 0;
            for (final OTPreguntasEjes ot : percents) {
                if (ot.getBuenas() != 0 && ot.getTotal() != 0) {
                    cantidad++;
                    percent += ot.getBuenas().doubleValue() / ot.getTotal().doubleValue() * 100d;
                }
            }

            percent = percent / cantidad;
            titles.add(eje.getName());
            values.add(new Double(percent));
        }

        try {
            final File file = ChartsUtil.createLineChart("DISTRIBUCIÓN x EJES", "Ejes", titles, values);
            WordUtil.addImage(file, "DISTRIBUCIÓN x EJES", paragraph, 500, 350);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final InvalidFormatException e) {
            e.printStackTrace();
        }

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
            List<R_RespuestasEsperadasPrueba> respEsperadas, R_Ejetematico eje) {
        int nroBuenas = 0;
        int nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            final R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (!resp.getAnulada()) {
                if (resp.getEjeTematico().equals(eje)) {
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
        return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
    }

    @Override
    public void page(XWPFDocument document) {

        if (resultado == null || resultado.isEmpty())
            return;
        
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addCarriageReturn();

        final int nRows = resultado.keySet().size();

        int n = 0;
        while (n < nroCursos) {
            paragraph = document.createParagraph();
            run = paragraph.createRun(); // create new run
            run.addCarriageReturn();

            final int cursos = Math.min(12, nroCursos - n);
            final XWPFTable table = document.createTable(nRows + 1, cursos + 1);
            WordUtil.setTableFormat(table, 1, 0);

            XWPFTableRow tableRow = table.getRow(0);
            tableRow.getCell(0).getParagraphs().get(0).getRuns().get(0).setText("EJE");
            int col = 1;
            for (int r = n; r < n + cursos; r++) {
                final String title = cursoList.get(r).getName();
                tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0).setText(title);
            }
            int row = 1;
            for (final R_Ejetematico eje : resultado.keySet()) {
                tableRow = table.getRow(row++);
                tableRow.getCell(0).getParagraphs().get(0).getRuns().get(0).setText(eje.getName().toUpperCase());
                final List<OTPreguntasEjes> notas = resultado.get(eje);
                col = 1;
                for (int r = n; r < n + cursos; r++) {
                    final OTPreguntasEjes nota = notas.get(r);
                    if (nota != null)
                        tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0)
                                .setText(String.format("%5.1f", nota.getSlogrado()));
                    // tableRow.getCell(col++).setText(String.format("%5.2f",
                    // nota.getSlogrado()));
                    else
                        tableRow.getCell(col++).getParagraphs().get(0).getRuns().get(0)
                                .setText(String.format("%5.1f", 0f));
                    // tableRow.getCell(col++).setText(String.format("%5.2f",
                    // 0f));
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

    protected Map<R_Ejetematico, List<OTPreguntasEjes>> procesar(List<R_EvaluacionPrueba> evaluacionesPrueba) {

        final Map<R_Ejetematico, List<OTPreguntasEjes>> mapEjes = new HashMap<>();

        cursoList = evaluacionesPrueba.stream().map(R_EvaluacionPrueba::getCurso).distinct()
                .sorted(Comparadores.comparaResumeCurso()).collect(Collectors.toList());
        nroCursos = cursoList.size();

        final int[] totalAlumnos = new int[nroCursos];
        Arrays.fill(totalAlumnos, 0);
        final int[] alumnosEvaluados = new int[nroCursos];
        Arrays.fill(alumnosEvaluados, 0);

        for (final R_EvaluacionPrueba eval : evaluacionesPrueba) {
            eval.getPruebasRendidas().size();
            final List<R_PruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
            eval.getPrueba().getRespuestas().size();
            final List<R_RespuestasEsperadasPrueba> respEsperadas = eval.getPrueba().getRespuestas();
            final int index = cursoList.indexOf(eval.getCurso());

            if (index == -1) {
                continue;
            }
            totalAlumnos[index] = 0;

            for (final R_Alumno alumno : eval.getCurso().getAlumnos())
                if (tipoAlumno.getId() == Constants.PIE_ALL || alumno.getTipoAlumno().getId().equals(tipoAlumno))
                    totalAlumnos[index] = totalAlumnos[index] + 1;

            for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {
                if (tipoAlumno.getId() != Constants.PIE_ALL
                        && !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno))
                    continue;

                alumnosEvaluados[index] = alumnosEvaluados[index] + 1;

                final String respuestas = pruebaRendida.getRespuestas();
                if (respuestas == null || respuestas.isEmpty())
                    continue;

                for (int n = 0; n < respEsperadas.size(); n++) {
                    final R_Ejetematico eje = respEsperadas.get(n).getEjeTematico();
                    if (!mapEjes.containsKey(eje)) {
                        mapEjes.put(eje,
                                Stream.generate(OTPreguntasEjes::new).limit(nroCursos).collect(Collectors.toList()));
                    }
                }
                for (final R_Ejetematico eje : mapEjes.keySet()) {
                    final List<OTPreguntasEjes> lstEjes = mapEjes.get(eje);
                    OTPreguntasEjes otEje = lstEjes.get(index);
                    if (otEje == null || otEje.getEjeTematico() == null) {
                        otEje = new OTPreguntasEjes(eje);
                        lstEjes.set(index, otEje);
                    }
                    final Pair<Integer, Integer> buenasTotal = obtenerBuenasTotales(respuestas, respEsperadas, eje);
                    otEje.setBuenas(otEje.getBuenas() + buenasTotal.getFirst());
                    otEje.setTotal(otEje.getTotal() + buenasTotal.getSecond());
                    lstEjes.set(index, otEje);
                }
            }
        }

        return mapEjes;
    }

}
