package colegio.nivel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Pair;
import cl.eos.util.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class Nivel_ComparativoColegioEjeHabilidadView extends AFormView implements EventHandler<ActionEvent> {
    private Logger log = Logger.getLogger(Nivel_ComparativoColegioEjeHabilidadView.class.getName());
    private static final String ASIGNATURA_ID = "asignatura_id";
    private static final String COLEGIO_ID = "colegio_id";
    @FXML private TableView tblEjeshabilidades;
    @FXML private TableView tblEvaluacion;
    @FXML private ComboBox<R_Colegio> cmbColegios;
    @FXML private ComboBox<R_Asignatura> cmbAsignatura;
    @FXML private ComboBox<R_TipoAlumno> cmbTipoAlumno;
    @FXML private Button btnReportes;
    @FXML private Label lblColegio;
    @FXML private Label lblTitulo;
    @FXML private MenuItem mnuExportarGeneral;
    @FXML private MenuItem mnuExportarAlumnos;
    private Map<String, Object> parameters = new HashMap<String, Object>();
    private ObservableList<R_Curso> cursoList;
    private ObservableList<R_EvaluacionEjetematico> evalEjeTematicoList;
    private ObservableList<R_EvaluacionPrueba> evaluacionesPrueba;
    private ArrayList<OTPreguntasEvaluacion> lst;
    private ObservableList<R_TipoCurso> tipoCurso;
    private List<R_RespuestasEsperadasPrueba> respEsperadas;
    private R_Prueba prueba;
    
    private Map<Long, R_Ejetematico> mEjes = new HashMap<>();
    private Map<Long, R_Habilidad> mHabs = new HashMap<>();
    public Nivel_ComparativoColegioEjeHabilidadView() {
        setTitle("Comparativo R_Colegio Ejes Temáticos y Habilidades x Nivel");
    }
    @SuppressWarnings("unchecked")
    @Override
    public void handle(ActionEvent event) {
        Object source = event.getSource();
        if (source == cmbColegios) {
            handleColegios();
        }
        if (source == cmbAsignatura) {
            handleAsignatura();
        }
        if (source == btnReportes) {
            handleReportes();
        }
        if (source == mnuExportarAlumnos || source == mnuExportarGeneral) {
            tblEjeshabilidades.setId("Comparativo Ejes y Habilidades");
            tblEvaluacion.setId("Rango de Evaluaciones");
            //
            List<TableView<? extends Object>> listaTablas = new ArrayList<>();
            listaTablas.add((TableView<? extends Object>) tblEjeshabilidades);
            listaTablas.add((TableView<? extends Object>) tblEvaluacion);
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
        }
    }
    private void handleColegios() {
        R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
        if (colegio != null) {
            parameters.put(COLEGIO_ID, colegio.getId());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("colegioId", colegio.getId());
            lblTitulo.setText(colegio.getName());
            controller.findByParam(R_Curso.class, param, this);
            clearContent();
        }
    }
    private void handleAsignatura() {
        R_Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
        if (asignatura != null) {
            parameters.put(ASIGNATURA_ID, asignatura.getId());
            clearContent();
        }
    }
    private void handleReportes() {
        if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID) && parameters.containsKey(ASIGNATURA_ID)) {
            controller.findByParam(R_EvaluacionPrueba.class, parameters, this);
        }
    }
    @FXML
    public void initialize() {
        inicializaComponentes();
    }
    private void inicializaComponentes() {
        cmbColegios.setOnAction(this);
        cmbAsignatura.setOnAction(this);
        btnReportes.setOnAction(this);
        mnuExportarAlumnos.setOnAction(this);
        mnuExportarGeneral.setOnAction(this);
    }
    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof R_Colegio) {
                ObservableList<R_Colegio> oList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oList.add((R_Colegio) iEntity);
                }
                cmbColegios.setItems(oList);
            }
            if (entity instanceof R_Asignatura) {
                ObservableList<R_Asignatura> oList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oList.add((R_Asignatura) iEntity);
                }
                cmbAsignatura.setItems(oList);
            }
            if (entity instanceof R_TipoCurso) {
                tipoCurso = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    tipoCurso.add((R_TipoCurso) iEntity);
                }
            }
            if (entity instanceof R_Curso) {
                cursoList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    cursoList.add((R_Curso) iEntity);
                }
                FXCollections.sort(cursoList, Comparadores.comparaResumeCurso());
            }
            if (entity instanceof R_EvaluacionEjetematico) {
                evalEjeTematicoList = FXCollections.observableArrayList();
                for (Object object : list) {
                    R_EvaluacionEjetematico evaluacion = (R_EvaluacionEjetematico) object;
                    evalEjeTematicoList.add(evaluacion);
                }
                generarReporte();
            }
            if (entity instanceof R_TipoAlumno) {
                ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    tAlumnoList.add((R_TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
            }
            if (entity instanceof R_EvaluacionPrueba) {
                evaluacionesPrueba = FXCollections.observableArrayList();
                for (Object object : list) {
                    R_EvaluacionPrueba evaluacion = (R_EvaluacionPrueba) object;
                    evaluacionesPrueba.add(evaluacion);
                }
                generarReporte();
            }
            if (entity instanceof R_RespuestasEsperadasPrueba) {
                respEsperadas = new ArrayList<>();
                for (Object object : list) {
                    respEsperadas.add((R_RespuestasEsperadasPrueba) object);
                }
                generarReporte();
            }
        } else if (list != null && list.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No hay registros.");
            alert.setHeaderText(null);
            alert.setContentText("No se ha encontrado registros para la consulta.");
            alert.showAndWait();
        }
    }
    /**
     * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los cursos que estan asociados al colegio,
     * independiente que tenga o no evaluaciones.
     * 
     * @param pCursoList
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void llenarColumnas(ObservableList<R_TipoCurso> tiposCurso) {
        TableColumn tc = new TableColumn("EJE / HABILIDAD");
        tc.setSortable(false);
        tc.setStyle("-fx-alignment: CENTER-LEFT;");
        tc.prefWidthProperty().set(250f);
        tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        tblEjeshabilidades.getColumns().add(tc);
        tc = new TableColumn("EVALUACION");
        tc.setStyle("-fx-alignment: CENTER-LEFT;");
        tc.prefWidthProperty().set(250f);
        tc.setSortable(false);
        tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        tblEvaluacion.getColumns().add(tc);
        int indice = 1;
        for (R_TipoCurso curso : tiposCurso) {
            final int idx = indice;
            tc = new TableColumn(curso.getName());
            tc.prefWidthProperty().set(50f);
            tc.setStyle("-fx-alignment: CENTER;");
            tc.setSortable(false);
            tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(idx).toString());
                }
            });
            tblEjeshabilidades.getColumns().add(tc);
            tc = new TableColumn(curso.getName());
            tc.prefWidthProperty().set(50f);
            tc.setStyle("-fx-alignment: CENTER;");
            tc.setSortable(false);
            tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(idx).toString());
                }
            });
            tblEvaluacion.getColumns().add(tc);
            indice++;
        }
    }
    /**
     * Aqui se llenan las tablas con los valores correspondientes.<br>
     * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
     * 2) Se obtienen las habilidades de todas las pruebas.<br>
     * 3) Se obtienen los porcentajes de aprobacion de de cada colegio con respecto a cada eje y habilidad.
     */
    private void generarReporte() {
        if (evaluacionesPrueba == null || evalEjeTematicoList == null || respEsperadas == null) {
            return;
        }
        long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        ObservableList<R_TipoCurso> tiposCurso = FXCollections.observableArrayList();
        for (R_Curso curso : cursoList) {
            Optional<R_TipoCurso> op = tipoCurso.stream().filter(t -> t.getId().equals(curso.getTipocurso_id())).findFirst();
            if (op.isPresent() && !tiposCurso.contains(op.get())) {
                tiposCurso.add(op.get());
            }
        }
        llenarColumnas(tiposCurso);
        int nroTiposCursos = tiposCurso.size();
        Map<Long, List<OTPreguntasEjes>> mapEjes = new HashMap<>();
        Map<Long, List<OTPreguntasHabilidad>> mapHabilidades = new HashMap<>();
        Map<R_EvaluacionEjetematico, List<OTPreguntasEvaluacion>> mapEvaluaciones = new HashMap<>();
        for (R_EvaluacionEjetematico ejetem : evalEjeTematicoList) {
            lst = new ArrayList<>(nroTiposCursos);
            for (int idx = 0; idx < nroTiposCursos; idx++) {
                OTPreguntasEvaluacion otEval = new OTPreguntasEvaluacion();
                otEval.setEvaluacion(ejetem);
                lst.add(idx, otEval);
            }
            mapEvaluaciones.put(ejetem, lst);
        }
        int[] totalAlumnos = new int[nroTiposCursos];
        Arrays.fill(totalAlumnos, 0);
        int[] alumnosEvaluados = new int[nroTiposCursos];
        Arrays.fill(alumnosEvaluados, 0);
        // AQUI VOY A AGRUPAR CURSOS POR TIPO CURSO
        // Todas las evaluaciones asociadas (Todos los cursos)
        for (R_EvaluacionPrueba eval : evaluacionesPrueba) {
            // Se esta revisando un colegio.
            Map<String, Object> params = MapBuilder.<String, Object> unordered().put("evaluacionprueba_id", eval.getId()).build();
            List<R_PruebaRendida> pruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
            // Estamos procesando un colegio/una prueba
            // Obtengo el index de la columna que tengo que llenar (mas 1
            // por que la primera es de
            // contenido
            Optional<R_TipoCurso> op = tiposCurso.stream().filter(t -> t.getId().equals(eval.getCurso_id())).findFirst();
            if(!op.isPresent())
                continue;
            int index = tiposCurso.indexOf(op.get());
            if (index == -1) {
                continue;
            }
            
            params = MapBuilder.<String, Object> unordered().put("curso_id", eval.getCurso_id()).build();
            List<R_Alumno> alumnos =  controller.findByParamsSynchro(R_Alumno.class, params);
            
            // Obtengo los items a considerar en el caso que hayan items
            // PIE.
            for (R_Alumno alumno : alumnos) {
                if (tipoAlumno == Constants.PIE_ALL || alumno.getTipoalumno_id().equals(tipoAlumno)) {
                    // le quito 1 al total de items, ya que este alumno no es
                    // del grupo que sequiere representar en el reporte.
                    totalAlumnos[index] = totalAlumnos[index] + 1;
                }
            }
            for (R_PruebaRendida pruebaRendida : pruebasRendidas) {
                
                Optional<R_Alumno> opAlumno = alumnos.stream().filter(a -> pruebaRendida.getAlumno_id().equals(a.getId())).findFirst();
                if(!opAlumno.isPresent())
                    continue;
                R_Alumno alumno = opAlumno.get();
                // Se procesa un alumno.
                if (tipoAlumno != Constants.PIE_ALL && !alumno.getTipoalumno_id().equals(tipoAlumno)) {
                    continue;
                }
                alumnosEvaluados[index] = alumnosEvaluados[index] + 1;
                String respuestas = pruebaRendida.getRespuestas();
                if (respuestas == null || respuestas.isEmpty()) {
                    continue;
                }
                // Obtener ejes y habilidades de esta prueba
                for (int n = 0; n < respEsperadas.size(); n++) {
                    Long eje = respEsperadas.get(n).getEjetematico_id();
                    if (!mapEjes.containsKey(eje)) {
                        R_Ejetematico rEje =  controller.findByIdSynchro(R_Ejetematico.class, eje);
                        mEjes.put(eje, rEje);
                        List<OTPreguntasEjes> lista = new ArrayList<>();
                        for (int idx = 0; idx < nroTiposCursos; idx++) {
                            lista.add(null);
                        }
                        mapEjes.put(eje, lista);
                    }
                    Long hab = respEsperadas.get(n).getHabilidad_id();
                    if (!mapHabilidades.containsKey(hab)) {
                        R_Habilidad rHab =  controller.findByIdSynchro(R_Habilidad.class, hab);
                        mHabs.put(hab, rHab);

                        List<OTPreguntasHabilidad> lista = new ArrayList<>();
                        for (int idx = 0; idx < nroTiposCursos; idx++) {
                            lista.add(null);
                        }
                        mapHabilidades.put(hab, lista);
                    }
                }
                for (Long eje : mapEjes.keySet()) {
                    List<OTPreguntasEjes> lstEjes = mapEjes.get(eje);
                    OTPreguntasEjes otEje = lstEjes.get(index); // Se obtiene el
                                                                // asociado a la
                                                                // columna.
                    
                    if (otEje == null) {
                        otEje = new OTPreguntasEjes();
                        otEje.setEjeTematico(mEjes.get(eje));
                        lstEjes.set(index, otEje);
                    }
                    Pair<Integer, Integer> buenasTotal = obtenerBuenasTotalesEje(respuestas, respEsperadas, eje);
                    otEje.setBuenas(otEje.getBuenas() + buenasTotal.getFirst());
                    otEje.setTotal(otEje.getTotal() + buenasTotal.getSecond());
                    lstEjes.set(index, otEje);
                }
                for (Long hab : mapHabilidades.keySet()) {
                    List<OTPreguntasHabilidad> lstHabs = mapHabilidades.get(hab);
                    // Se obtiene el asociado a la columna.
                    OTPreguntasHabilidad otHabilidad = lstHabs.get(index);
                    if (otHabilidad == null) {
                        otHabilidad = new OTPreguntasHabilidad();
                        otHabilidad.setHabilidad(mHabs.get(hab));
                        lstHabs.set(index, otHabilidad);
                    }
                    Pair<Integer, Integer> buenasTotal = obtenerBuenasTotalesHab(respuestas, respEsperadas, hab);
                    otHabilidad.setBuenas(otHabilidad.getBuenas() + buenasTotal.getFirst());
                    otHabilidad.setTotal(otHabilidad.getTotal() + buenasTotal.getSecond());
                    log.fine(String.format("HAB: %d %d/%d  ACUM: %d/%d", hab, buenasTotal.getFirst(), buenasTotal.getSecond(),
                            otHabilidad.getBuenas(), otHabilidad.getTotal()));
                    lstHabs.set(index, otHabilidad);
                }
                for (R_EvaluacionEjetematico ejetem : evalEjeTematicoList) {
                    if (ejetem.isInside(pruebaRendida.getBuenas())) {
                        List<OTPreguntasEvaluacion> lstOt = mapEvaluaciones.get(ejetem);
                        OTPreguntasEvaluacion ot = lstOt.get(index);
                        ot.setAlumnos(ot.getAlumnos() + 1);
                        break;
                    }
                }
            }
        }
        // Ahora se debe llenar las tablas.
        generarTablaEjesHabilidades(mapEjes, mapHabilidades);
        generarTablaEvaluaciones(mapEvaluaciones, totalAlumnos, alumnosEvaluados);
    }
    /**
     * @return the prueba
     */
    public final R_Prueba getPrueba() {
        return prueba;
    }
    /**
     * @param prueba
     *            the prueba to set
     */
    public final void setPrueba(R_Prueba prueba) {
        this.prueba = prueba;
    }
    @SuppressWarnings("unchecked")
    private void generarTablaEvaluaciones(Map<R_EvaluacionEjetematico, List<OTPreguntasEvaluacion>> mapEvaluaciones, int[] totalAlumnos,
            int[] alumnosEvaluados) {
        ObservableList<String> row = null;
        ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
        Collections.sort(evalEjeTematicoList, Comparadores.comparaEvaluacionEjeTematico());
        for (R_EvaluacionEjetematico eval : evalEjeTematicoList) {
            row = FXCollections.observableArrayList();
            List<OTPreguntasEvaluacion> lst = mapEvaluaciones.get(eval);
            row.add(eval.getName());
            for (OTPreguntasEvaluacion ot : lst) {
                if (ot != null && ot.getAlumnos() != null) {
                    row.add(String.valueOf(ot.getAlumnos()));
                } else {
                    row.add(" ");
                }
            }
            items.add(row);
        }
        row = FXCollections.observableArrayList();
        for (int m = 0; m <= alumnosEvaluados.length; m++) {
            row.add(" ");
        }
        items.add(row);
        row = FXCollections.observableArrayList();
        row.add("EVALUADOS");
        for (int val : alumnosEvaluados) {
            row.add(String.valueOf(val));
        }
        items.add(row);
        row = FXCollections.observableArrayList();
        row.add("TOTAL");
        for (int val : totalAlumnos) {
            row.add(String.valueOf(val));
        }
        items.add(row);
        tblEvaluacion.setItems(items);
    }
    /**
     * Se genera la tabal que contiene los % de logro por cada eje y por cada habilidad asociado a cada colegio.
     * 
     * @param mapEjes
     *            Mapa que contiene los valores para cada colegio de los ejes.
     * @param mapHabilidades
     *            Mapa que contiene los valores para cada colegio de las habilidades.
     */
    @SuppressWarnings("unchecked")
    private void generarTablaEjesHabilidades(Map<Long, List<OTPreguntasEjes>> mapEjes,
            Map<Long, List<OTPreguntasHabilidad>> mapHabilidades) {
        ObservableList<String> row = null;
        ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
        int nroCols = 0;
        for (Long ejeId : mapEjes.keySet()) {
            R_Ejetematico eje = mEjes.get(ejeId);
            row = FXCollections.observableArrayList();
            List<OTPreguntasEjes> lst = mapEjes.get(eje);
            nroCols = lst.size();
            row.add(eje.getName());
            for (OTPreguntasEjes ot : lst) {
                if (ot != null && ot.getBuenas() != null && ot.getTotal() != null && ot.getTotal() != 0F) {
                    float porcentaje = ot.getBuenas().floatValue() / ot.getTotal().floatValue() * 100f;
                    row.add(String.valueOf(Utils.redondeo1Decimal(porcentaje)));
                } else {
                    row.add(" ");
                }
            }
            items.add(row);
        }
        row = FXCollections.observableArrayList();
        for (int m = 0; m <= nroCols; m++) {
            row.add(" ");
        }
        items.add(row);
        for (Long habId : mapHabilidades.keySet()) {
            R_Habilidad hab = mHabs.get(habId);
            row = FXCollections.observableArrayList();
            List<OTPreguntasHabilidad> lst = mapHabilidades.get(hab);
            row.add(hab.getName());
            for (OTPreguntasHabilidad ot : lst) {
                if (ot != null && ot.getBuenas() != null && ot.getTotal() != null && ot.getTotal() != 0F) {
                    float porcentaje = ot.getBuenas().floatValue() / ot.getTotal().floatValue() * 100f;
                    row.add(String.valueOf(Utils.redondeo1Decimal(porcentaje)));
                } else {
                    row.add(" ");
                }
            }
            items.add(row);
        }
        tblEjeshabilidades.setItems(items);
    }
    /**
     * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las respuestas esperadas.
     * 
     * @param respuestas
     *            Las respuestas del alumno.
     * @param respEsperadas
     *            Las respuestas correctas definidas en la prueba.
     * @param ahb
     *            La R_Habilidad en base al que se realiza el calculo.
     * @return Par <Preguntas buenas, Total de Preguntas> del eje.
     */
    private Pair<Integer, Integer> obtenerBuenasTotalesHab(String respuestas, List<R_RespuestasEsperadasPrueba> respEsperadas, Long hab) {
        int nroBuenas = 0;
        int nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (!resp.getAnulada()) {
                if (resp.getHabilidad_id().equals(hab)) {
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
    /**
     * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las respuestas eperadas.
     * 
     * @param respuestas
     *            Las respuestas del alumno.
     * @param respEsperadas
     *            Las respuestas correctas definidas en la prueba.
     * @param eje
     *            El Eje tematico en base al que se realiza el calculo.
     * @return Par <Preguntas buenas, Total de Preguntas> del eje.
     */
    private Pair<Integer, Integer> obtenerBuenasTotalesEje(String respuestas, List<R_RespuestasEsperadasPrueba> respEsperadas, Long eje) {
        int nroBuenas = 0;
        int nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (!resp.getAnulada()) {
                if (resp.getEjetematico_id().equals(eje)) {
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
    private void clearContent() {
        tblEjeshabilidades.getItems().clear();
        tblEvaluacion.getItems().clear();
        tblEjeshabilidades.getColumns().clear();
        ;
        tblEvaluacion.getColumns().clear();
    }
}
