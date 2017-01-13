package colegio.nivel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.view.ots.ejeevaluacion.OTAcumulador;
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

public class Nivel_ComparativoColegioEjeEvaluacionView extends AFormView implements EventHandler<ActionEvent> {
    private static final String ASIGNATURA_ID = "asignatura_id";
    private static final String COLEGIO_ID = "colegio_id";
    @FXML private TableView<ObservableList<String>> tblEjesCantidad;
    @FXML private ComboBox<R_Colegio> cmbColegios;
    @FXML private ComboBox<R_Asignatura> cmbAsignatura;
    @FXML private ComboBox<R_TipoAlumno> cmbTipoAlumno;
    @FXML private Button btnReportes;
    @FXML private Label lblColegio;
    @FXML private Label lblTitulo;
    @FXML private MenuItem mnuExportarAlumnos;
    private Map<String, Object> parameters = new HashMap<>();
    private ObservableList<R_TipoCurso> tipoCursoList;
    private ObservableList<R_RangoEvaluacion> rangoEvalList;
    private ObservableList<R_EvaluacionPrueba> evaluacionesPrueba;
    private R_Prueba prueba;
    private R_NivelEvaluacion nivelEvaluacion;
    private List<R_Curso> cursoList;
    public Nivel_ComparativoColegioEjeEvaluacionView() {
        setTitle("Comparativo Colegios Ejes x Nivel");
    }
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
        if (source == mnuExportarAlumnos) {
            tblEjesCantidad.setId("Comparativo Colegios Ejes x Nivel");
            List<TableView<? extends Object>> listaTablas = new ArrayList<>();
            listaTablas.add((TableView<? extends Object>) tblEjesCantidad);
            ExcelSheetWriterObj.convertirDatosColumnasDoblesALibroDeExcel(listaTablas);
        }
    }
    private void handleColegios() {
        R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
        if (colegio != null) {
            parameters.put(COLEGIO_ID, colegio.getId());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("colegio_id", colegio.getId());
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
    }
    @Override
    public void onDataArrived(List<Object> list) {
        if (list == null || list.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No hay registros.");
            alert.setHeaderText(this.getName());
            alert.setContentText("No se ha encontrado registros para la consulta.");
            alert.showAndWait();
            return;
        }
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
        if (entity instanceof R_Curso) {
            List<R_TipoCurso> lstTipoCursos = controller.findAllSynchro(R_TipoCurso.class);
            tipoCursoList = FXCollections.observableArrayList();
            cursoList = FXCollections.observableArrayList();
            for (Object iEntity : list) {
                R_Curso curso = (R_Curso) iEntity;
                cursoList.add(curso);
                Optional<R_TipoCurso> op = lstTipoCursos.stream().filter(t -> t.getId().equals(curso.getTipocurso_id())).findFirst();
                R_TipoCurso tipoCurso = op.isPresent() ? op.get() : null;
                if (!tipoCursoList.contains(tipoCurso)) {
                    tipoCursoList.add(tipoCurso);
                }
            }
            FXCollections.sort(tipoCursoList, Comparadores.comparaTipoCurso());
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
            rangoEvalList = FXCollections.observableArrayList();
            Map<String, Object> params = MapBuilder.<String, Object> unordered().put("nivelevaluacion_id", nivelEvaluacion.getId()).build();
            List<R_RangoEvaluacion> rngs = controller.findByParamsSynchro(R_RangoEvaluacion.class, params);
            for (R_RangoEvaluacion rng : rngs) {
                rangoEvalList.add(rng);
            }
            generarReporte();
        }
    }
    /**
     * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los cursos que estan asociados al colegio,
     * independiente que tenga o no evaluaciones.
     * 
     * @param pCursoList
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void llenarColumnas(ObservableList<R_TipoCurso> pCursoList, ObservableList<R_RangoEvaluacion> rangos) {
        TableColumn tc = new TableColumn("EJES");
        tc.setSortable(false);
        tc.setStyle("-fx-alignment: CENTER-LEFT;");
        tc.prefWidthProperty().set(250f);
        tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        tblEjesCantidad.getColumns().add(tc);
        int indice = 1;
        for (R_TipoCurso curso : pCursoList) {
            tc = new TableColumn(curso.getName());
            tc.prefWidthProperty().set(50f);
            tc.setStyle("-fx-alignment: CENTER;");
            tc.setSortable(false);
            tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(0).toString());
                }
            });
            // Estoy agregando subcolumnas
            for (R_RangoEvaluacion rng : rangos) {
                final int lIdx = indice;
                TableColumn stc = new TableColumn(rng.getAbreviacion());
                stc.prefWidthProperty().set(50f);
                stc.setStyle("-fx-alignment: CENTER;");
                stc.setSortable(false);
                stc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(lIdx).toString());
                    }
                });
                tc.getColumns().add(stc);
                indice++;
            }
            tblEjesCantidad.getColumns().add(tc);
        }
    }
    /**
     * Aqui se llenan las tablas con los valores correspondientes.<br>
     * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
     * 2) Se obtienen las habilidades de todas las pruebas.<br>
     * 3) Se obtienen los porcentajes de aprobacion de cada colegio con respecto a cada eje y habilidad.
     */
    private void generarReporte() {
        if (evaluacionesPrueba == null || rangoEvalList == null) {
            return;
        }
        llenarColumnas(tipoCursoList, rangoEvalList);
        int nroCursos = tipoCursoList.size();
        int nroRangos = rangoEvalList.size();
        // Map<R_Ejetematico, List<OTAcumulador>> mapEjes = new HashMap<>();
        Map<Long, List<OTAcumulador>> mapEjes = new HashMap<>();
        /*
         * Aqui verificamos el TIPO ALUMNO SELECCIONADO PARA EL REPORTE
         */
        long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        // Todas las evaluaciones asociadas (Todos los cursos)
        for (R_EvaluacionPrueba eval : evaluacionesPrueba) {
            Map<String, Object> params = MapBuilder.<String, Object> unordered().put("evaluacionprueba_id", eval.getId()).build();
            List<R_PruebaRendida> pruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
            params = MapBuilder.<String, Object> unordered().put("prueba_id", prueba.getId()).build();
            List<R_RespuestasEsperadasPrueba> respEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
            // Estamos procesando un colegio/una prueba
            for (R_PruebaRendida pruebaRendida : pruebasRendidas) {
                // Se procesa un alumno.
                // Obtengo el index de la columna que tengo que llenar (mas 1
                // por que la primera es de contenido
                // index * nroRangos Ya que cada colegio tiene nroRangos columnas
                // asociadas.
                R_Alumno alumno = controller.findSynchroById(R_Alumno.class, pruebaRendida.getAlumno_id());
                if (alumno == null) {
                    // Caso especial que indica que la prueba esta sin alumno.
                    continue;
                }
                if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoalumno_id()) {
                    // En este caso, no se considera este alumno para el
                    // cálculo.
                    continue;
                }
                Optional<R_Curso> op = cursoList.stream().filter(c -> c.getId().equals(alumno.getCurso_id())).findFirst();
                if (!op.isPresent()) // No hay curso asociado al alumno, algo imposible.
                    continue;
                Optional<R_TipoCurso> opTipoCurso = tipoCursoList.stream().filter(t -> t.getId().equals(op.get().getTipocurso_id())).findFirst();
                if (!opTipoCurso.isPresent()) continue;
                int index = tipoCursoList.indexOf(opTipoCurso.get());
                if (index == -1) {
                    continue;
                }
                String respuestas = pruebaRendida.getRespuestas();
                if (respuestas == null || respuestas.isEmpty()) {
                    continue;
                }
                for (int n = 0; n < respEsperadas.size(); n++) {
                    // Sumando a ejes tematicos
                    // R_Ejetematico eje = respEsperadas.get(n).getEjeTematico();
                    Long eje = respEsperadas.get(n).getEjetematico_id();
                    if (!mapEjes.containsKey(eje)) {
                        List<OTAcumulador> lista = new ArrayList<OTAcumulador>(nroCursos);
                        for (int idx = 0; idx < nroCursos; idx++) {
                            lista.add(null);
                        }
                        mapEjes.put(eje, lista);
                    }
                    List<OTAcumulador> lstEjes = mapEjes.get(eje);
                    OTAcumulador otEjeEval = lstEjes.get(index); // Que columna
                                                                 // (colegio
                                                                 // es)
                    if (otEjeEval == null) {
                        otEjeEval = new OTAcumulador();
                        int[] nroPersonas = new int[nroRangos];
                        Arrays.fill(nroPersonas, 0);
                        otEjeEval.setNroPersonas(nroPersonas);
                        lstEjes.set(index, otEjeEval);
                    }
                }
                // for (R_Ejetematico eje : mapEjes.keySet()) {
                for (Long eje : mapEjes.keySet()) {
                    List<OTAcumulador> lstEjes = mapEjes.get(eje);
                    OTAcumulador otEjeEval = lstEjes.get(index);
                    float porcentaje = obtenerPorcentaje(respuestas, respEsperadas, eje);
                    for (int idx = 0; idx < nroRangos; idx++) {
                        R_RangoEvaluacion rango = rangoEvalList.get(idx);
                        if (rango.isInside(porcentaje)) {
                            otEjeEval.getNroPersonas()[idx] = otEjeEval.getNroPersonas()[idx] + 1;
                            break;
                        }
                    }
                    lstEjes.set(index, otEjeEval);
                }
            }
        }
        // Ahora se debe llenar las tablas.
        generarTablaEjes(mapEjes);
    }
    /**
     * Se genera la tabal que contiene los % de logro por cada eje y por cada habilidad asociado a cada colegio.
     * 
     * @param mapEjes
     *            Mapa que contiene los valores para cada colegio de los ejes.
     * @param mapHabilidades
     *            Mapa que contiene los valores para cada colegio de las habilidades.
     */
//    private void generarTablaEjes(Map<R_Ejetematico, List<OTAcumulador>> mapEjes) {
    private void generarTablaEjes(Map<Long, List<OTAcumulador>> mapEjes) {
        ObservableList<String> row = null;
        Map<Long, R_Ejetematico> ejes = new HashMap<>();
        
        ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
//        for (R_Ejetematico eje : mapEjes.keySet()) {
            for (Long eje : mapEjes.keySet()) {
            row = FXCollections.observableArrayList();
            List<OTAcumulador> lst = mapEjes.get(eje);
            
            //TODO ESTO, DEBE CAMBIAR SI ES MUY LENTO
            R_Ejetematico rEje = ejes.get(eje);
            if(rEje == null)
            {
                rEje = controller.findByIdSynchro(R_Ejetematico.class, eje);
                ejes.put(eje, rEje);
            }
            row.add(rEje.getName());
            for (OTAcumulador otEje : lst) {
                if (otEje != null && otEje.getNroPersonas() != null) {
                    int[] personas = otEje.getNroPersonas();
                    for (int n = 0; n < rangoEvalList.size(); n++) {
                        row.add(String.valueOf(personas[n]));
                    }
                } else {
                    for (int n = 0; n < rangoEvalList.size(); n++) {
                        row.add("0");
                    }
                }
            }
            items.add(row);
        }
        tblEjesCantidad.setItems(items);
    }
//    private float obtenerPorcentaje(String respuestas, List<R_RespuestasEsperadasPrueba> respEsperadas, R_Ejetematico eje) {
    private float obtenerPorcentaje(String respuestas, List<R_RespuestasEsperadasPrueba> respEsperadas, Long eje) {
        float nroBuenas = 0;
        float nroPreguntas = 0;
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
        float porcentaje = nroBuenas / nroPreguntas * 100f;
        return porcentaje;
    }
    private void clearContent() {
        tblEjesCantidad.getItems().clear();
        tblEjesCantidad.getColumns().clear();
        ;
    }
    public void setPrueba(R_Prueba prueba) {
        this.prueba = prueba;
    }
    public void setNivelEvaluacion(R_NivelEvaluacion nivelEvaluacion) {
        this.nivelEvaluacion = nivelEvaluacion;
    }
}
