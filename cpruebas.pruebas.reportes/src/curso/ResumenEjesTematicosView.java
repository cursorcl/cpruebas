package curso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Pair;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

public class ResumenEjesTematicosView extends AFormView implements EventHandler<ActionEvent> {

    @FXML
    private TableView<OTPreguntasEjes> tblEjesTematicos;
    @FXML
    private TableColumn<OTPreguntasEjes, String> colNombre;
    @FXML
    private TableColumn<OTPreguntasEjes, String> colDescripcion;
    @FXML
    private TableColumn<OTPreguntasEjes, Float> colLogrado;
    @FXML
    private TableColumn<OTPreguntasEjes, Float> colNoLogrado;

    @FXML
    private TextField txtPrueba;
    @FXML
    private TextField txtCurso;
    @FXML
    private TextField txtAsignatura;
    @FXML
    private TextField txtEjeTematico;

    final NumberAxis xAxis = new NumberAxis();
    final CategoryAxis yAxis = new CategoryAxis();
    @FXML
    private final BarChart<String, Number> graficoBarra = new BarChart<String, Number>(yAxis, xAxis);

    @FXML
    private MenuItem mnuExportarEjes;

    private HashMap<R_Ejetematico, OTPreguntasEjes> mapaEjesTematicos;

    @FXML
    private ComboBox<R_TipoAlumno> cmbTipoAlumno;
    @FXML
    private Button btnGenerar;

    long tipoAlumno = Constants.PIE_ALL;
    private R_EvaluacionPrueba evaluacionPrueba;

    public ResumenEjesTematicosView() {

    }

    private void accionClicTabla() {
        tblEjesTematicos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OTPreguntasEjes>() {

            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            public void changed(ObservableValue<? extends OTPreguntasEjes> observable, OTPreguntasEjes oldValue,
                    OTPreguntasEjes newValue) {
                final ObservableList<OTPreguntasEjes> itemsSelec = tblEjesTematicos.getSelectionModel()
                        .getSelectedItems();

                if (itemsSelec.size() == 1) {
                    final OTPreguntasEjes ejeTematico = itemsSelec.get(0);
                    txtEjeTematico.setText(ejeTematico.getName());

                    final Float porcentajeLogrado = ejeTematico.getLogrado();
                    final Float porcentajeNologrado = ejeTematico.getNologrado();

                    final XYChart.Series series1 = new XYChart.Series();
                    series1.setName("%");
                    series1.getData().add(new XYChart.Data<String, Float>("Logrado", porcentajeLogrado));
                    series1.getData().add(new XYChart.Data<String, Float>("No Logrado", porcentajeNologrado));
                    graficoBarra.getData().clear();
                    graficoBarra.getData().add(series1);

                    for (final Series<String, Number> s : graficoBarra.getData()) {
                        for (final Data<String, Number> d : s.getData()) {
                            Tooltip.install(d.getNode(), new Tooltip(String.format("%s = %2.1f",
                                    d.getXValue().toString(), d.getYValue().doubleValue())));
                        }
                    }
                }
            }
        });

    }

    private void generateReport() {
        if (evaluacionPrueba != null && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty()) {
            txtAsignatura.setText(evaluacionPrueba.getAsignatura());
            txtCurso.setText(evaluacionPrueba.getCurso().getName());
            txtPrueba.setText(evaluacionPrueba.getPrueba().getName());
            obtenerResultados(evaluacionPrueba);

            if (mapaEjesTematicos != null && !mapaEjesTematicos.isEmpty()) {

                final ArrayList<OTPreguntasEjes> listado = new ArrayList<>(mapaEjesTematicos.values());
                final ObservableList<OTPreguntasEjes> oList = FXCollections.observableList(listado);
                tblEjesTematicos.setItems(oList);
            }
        }
    }

    @Override
    public void handle(ActionEvent event) {
        final Object source = event.getSource();
        if (source == mnuExportarEjes) {

            tblEjesTematicos.setId("Ejes temáticos");

            final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
            listaTablas.add(tblEjesTematicos);

            ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
        } else if (source == btnGenerar) {
            generateReport();
        }

    }

    private void inicializarTablaEjes() {
        tblEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colNombre.setCellValueFactory(new PropertyValueFactory<OTPreguntasEjes, String>("name"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<OTPreguntasEjes, String>("descripcion"));
        colLogrado.setCellValueFactory(new PropertyValueFactory<OTPreguntasEjes, Float>("slogrado"));
        colNoLogrado.setCellValueFactory(new PropertyValueFactory<OTPreguntasEjes, Float>("snlogrado"));
    }

    @FXML
    public void initialize() {
        btnGenerar.setOnAction(this);
        cmbTipoAlumno.setOnAction(event -> tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex());

        inicializarTablaEjes();
        accionClicTabla();
        setTitle("Resumen Respuestas por Ejes Temáticos");
        graficoBarra.setTitle("Gráfico Respuestas por ejes temáticos");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");
        mnuExportarEjes.setOnAction(this);
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
                if (eje.getId().equals(resp.getEjetematico_id())) {
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

    private void obtenerResultados(R_EvaluacionPrueba entity) {
        final List<R_PruebaRendida> pruebasRendidas = entity.getPruebasRendidas();

        mapaEjesTematicos = new HashMap<R_Ejetematico, OTPreguntasEjes>();

        final R_Prueba prueba = entity.getPrueba();
        final List<R_RespuestasEsperadasPrueba> respuestasEsperadas = prueba.getRespuestas();

        for (final R_RespuestasEsperadasPrueba resp : respuestasEsperadas) {
            if (mapaEjesTematicos.containsKey(resp.getEjeTematico()))
                continue;
            final OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
            otPreguntas.setEjeTematico(resp.getEjeTematico());
            otPreguntas.setBuenas(0);
            otPreguntas.setTotal(0);
            mapaEjesTematicos.put(resp.getEjeTematico(), otPreguntas);
        }

        for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {
            if (tipoAlumno != Constants.PIE_ALL
                    && !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
                continue;
            }
            final String respuesta = pruebaRendida.getRespuestas();

            for (final R_Ejetematico hab : mapaEjesTematicos.keySet()) {
                final OTPreguntasEjes otPreguntas = mapaEjesTematicos.get(hab);
                final Pair<Integer, Integer> result = obtenerBuenasTotales(respuesta, respuestasEsperadas, hab);
                otPreguntas.setEjeTematico(hab);
                otPreguntas.setBuenas(otPreguntas.getBuenas() + result.getFirst());
                otPreguntas.setTotal(otPreguntas.getTotal() + result.getSecond());

            }
        }
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof R_TipoAlumno) {
                final ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    tAlumnoList.add((R_TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
                cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
                generateReport();
            }
        }
    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof R_EvaluacionPrueba) {
            evaluacionPrueba = (R_EvaluacionPrueba) entity;
            generateReport();
        }
    }
}
