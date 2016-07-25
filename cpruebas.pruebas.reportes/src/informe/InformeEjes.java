package informe;

import java.math.BigInteger;
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
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.ot.OTRangoCurso;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.util.Pair;
import javafx.collections.ObservableList;
import utils.WordUtil;

/**
 * Esta clase genera los valores para el resumen.
 * 
 * @author curso
 *
 */
public class InformeEjes implements Informe {

    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";

    static Logger log = Logger.getLogger(InformeEjes.class);
    private TipoAlumno tipoAlumno;
    private Colegio colegio;
    private Asignatura asignatura;
    private Map<Curso, Map<RangoEvaluacion, OTRangoCurso>> resultado;
    private List<RangoEvaluacion> rangos;

    private List<EvaluacionEjeTematico> evalEjeTematicoList;
    private List<Curso> cursoList;

    public InformeEjes() {
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
        List<Object> lst = PersistenceServiceFactory.getPersistenceService()
                .findSynchro("EvaluacionPrueba.findEvaluacionByColegioAsig", params);

        params.clear();
        params.put(COLEGIO_ID, colegio.getId());
        cursoList = (List<Curso>) (Object) PersistenceServiceFactory.getPersistenceService()
                .findSynchro("Curso.findByColegio", params);

        evalEjeTematicoList = PersistenceServiceFactory.getPersistenceService()
                .findAllSynchro(EvaluacionEjeTematico.class);
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
        paragraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFTable table = document.createTable(resultado.size() + 1, rangos.size() + 1);
        table.getCTTbl().addNewTblPr().addNewTblW().setW(BigInteger.valueOf(10000));
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
    }

    protected Map<Curso, Map<RangoEvaluacion, OTRangoCurso>> procesar(List<Object> list) {

        int nroCursos = cursoList.size();
        
        Map<Curso, Map<RangoEvaluacion, OTRangoCurso>> pmeCursos = new HashMap<>();
        NivelEvaluacion nivel = ((EvaluacionPrueba) list.get(0)).getPrueba().getNivelEvaluacion();
        ArrayList<OTPreguntasEvaluacion> lst;

        Map<EjeTematico, List<OTPreguntasEjes>> mapEjes = new HashMap<>();
        Map<Habilidad, List<OTPreguntasHabilidad>> mapHabilidades = new HashMap<>();
        Map<EvaluacionEjeTematico, List<OTPreguntasEvaluacion>> mapEvaluaciones = new HashMap<>();

        for (EvaluacionEjeTematico ejetem : evalEjeTematicoList) {
            lst = new ArrayList<>(nroCursos);
            for (int idx = 0; idx < nroCursos; idx++) {
                OTPreguntasEvaluacion otEval = new OTPreguntasEvaluacion();
                otEval.setEvaluacion(ejetem);
                lst.add(idx, otEval);
            }
            mapEvaluaciones.put(ejetem, lst);
        }

        int[] totalAlumnos = new int[nroCursos];
        Arrays.fill(totalAlumnos, 0);
        int[] alumnosEvaluados = new int[nroCursos];
        Arrays.fill(alumnosEvaluados, 0);

        // Todas las evaluaciones asociadas (Todos los cursos)
        for (EvaluacionPrueba eval : evaluacionesPrueba) {

            // Se esta revisando un curso.
            eval.getPruebasRendidas().size();
            List<PruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
            eval.getPrueba().getRespuestas().size();
            List<RespuestasEsperadasPrueba> respEsperadas = eval.getPrueba().getRespuestas();
            // Estamos procesando un curso/una prueba

            // Obtengo el index de la columna que tengo que llenar (mas 1
            // por que la primera es de
            // contenido
            int index = cursoList.indexOf(eval.getCurso());

            if (index == -1) {
                continue;
            }
            totalAlumnos[index] = 0;

            // Obtengo los alumnos a considerar en el caso que hayan alumnos
            // PIE.
            for (Alumno alumno : eval.getCurso().getAlumnos()) {
                if (tipoAlumno == Constants.PIE_ALL || alumno.getTipoAlumno().getId().equals(tipoAlumno)) {
                    // le quito 1 al total de alumnos, ya que este alumno no es
                    // del grupo que sequiere representar en el reporte.
                    totalAlumnos[index] = totalAlumnos[index] + 1;
                }
            }

            for (PruebaRendida pruebaRendida : pruebasRendidas) {
                // Se procesa un alumno.
                if (tipoAlumno != Constants.PIE_ALL
                        && !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
                    continue;
                }

                alumnosEvaluados[index] = alumnosEvaluados[index] + 1;

                String respuestas = pruebaRendida.getRespuestas();
                if (respuestas == null || respuestas.isEmpty()) {
                    continue;
                }

                // Obtener ejes y habilidades de esta prueba
                for (int n = 0; n < respEsperadas.size(); n++) {
                    EjeTematico eje = respEsperadas.get(n).getEjeTematico();
                    if (!mapEjes.containsKey(eje)) {
                        List<OTPreguntasEjes> lista = new ArrayList<>();
                        for (int idx = 0; idx < nroCursos; idx++) {
                            lista.add(null);
                        }
                        mapEjes.put(eje, lista);
                    }
                    Habilidad hab = respEsperadas.get(n).getHabilidad();
                    if (!mapHabilidades.containsKey(hab)) {

                        List<OTPreguntasHabilidad> lista = new ArrayList<>();
                        for (int idx = 0; idx < nroCursos; idx++) {
                            lista.add(null);
                        }
                        mapHabilidades.put(hab, lista);
                    }
                }

                for (EjeTematico eje : mapEjes.keySet()) {
                    List<OTPreguntasEjes> lstEjes = mapEjes.get(eje);
                    OTPreguntasEjes otEje = lstEjes.get(index); // Se obtiene el
                                                                // asociado a la
                                                                // columna.
                    if (otEje == null) {
                        otEje = new OTPreguntasEjes();
                        otEje.setEjeTematico(eje);
                        lstEjes.set(index, otEje);
                    }
                    Pair<Integer, Integer> buenasTotal = obtenerBuenasTotales(respuestas, respEsperadas, eje);

                    otEje.setBuenas(otEje.getBuenas() + buenasTotal.getFirst());
                    otEje.setTotal(otEje.getTotal() + buenasTotal.getSecond());
                    lstEjes.set(index, otEje);
                }

                for (Habilidad hab : mapHabilidades.keySet()) {
                    List<OTPreguntasHabilidad> lstHabs = mapHabilidades.get(hab);

                    // Se obtiene el asociado a la columna.
                    OTPreguntasHabilidad otHabilidad = lstHabs.get(index);
                    if (otHabilidad == null) {
                        otHabilidad = new OTPreguntasHabilidad();
                        otHabilidad.setHabilidad(hab);
                        lstHabs.set(index, otHabilidad);
                    }
                    Pair<Integer, Integer> buenasTotal = obtenerBuenasTotales(respuestas, respEsperadas, hab);
                    otHabilidad.setBuenas(otHabilidad.getBuenas() + buenasTotal.getFirst());
                    otHabilidad.setTotal(otHabilidad.getTotal() + buenasTotal.getSecond());
                    log.debug(String.format("HAB: %s %d/%d  ACUM: %d/%d", hab.getName(), buenasTotal.getFirst(),
                            buenasTotal.getSecond(), otHabilidad.getBuenas(), otHabilidad.getTotal()));
                    lstHabs.set(index, otHabilidad);
                }

                for (EvaluacionEjeTematico ejetem : evalEjeTematicoList) {
                    if (ejetem.isInside(pruebaRendida.getPbuenas())) {
                        List<OTPreguntasEvaluacion> lstOt = mapEvaluaciones.get(ejetem);
                        OTPreguntasEvaluacion ot = lstOt.get(index);
                        ot.setAlumnos(ot.getAlumnos() + 1);
                        break;
                    }
                }
            }
        }

        return pmeCursos;
    }

}
