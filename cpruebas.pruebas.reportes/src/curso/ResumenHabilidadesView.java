package curso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Pair;
import javafx.beans.value.ChangeListener;
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

public class ResumenHabilidadesView extends AFormView implements EventHandler<ActionEvent> {

    @FXML
    private TableView<OTPreguntasHabilidad> tblHabilidades;
    @FXML
    private TableColumn<OTPreguntasHabilidad, String> colNombre;
    @FXML
    private TableColumn<OTPreguntasHabilidad, String> colDescripcion;
    @FXML
    private TableColumn<OTPreguntasHabilidad, String> colLogrado;
    @FXML
    private TableColumn<OTPreguntasHabilidad, String> colNoLogrado;

    @FXML
    private TextField txtPrueba;
    @FXML
    private TextField txtCurso;
    @FXML
    private TextField txtAsignatura;
    @FXML
    private TextField txtHabilidad;

    final NumberAxis xAxis = new NumberAxis();
    final CategoryAxis yAxis = new CategoryAxis();
    @FXML
    private final BarChart<String, Number> graficoBarra = new BarChart<String, Number>(yAxis, xAxis);

    private HashMap<Habilidad, OTPreguntasHabilidad> mapaHabilidades;
    @FXML
    private MenuItem mnuExportarHabilidad;

    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;
    @FXML
    private Button btnGenerar;
    long tipoAlumno = Constants.PIE_ALL;

    private EvaluacionPrueba evaluacionPrueba;

    public ResumenHabilidadesView() {

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void accionClicTabla() {
        tblHabilidades.getSelectionModel().selectedItemProperty()
                .addListener((ChangeListener<OTPreguntasHabilidad>) (observable, oldValue, newValue) -> {
                    final ObservableList<OTPreguntasHabilidad> itemsSelec = tblHabilidades.getSelectionModel()
                            .getSelectedItems();

                    if (itemsSelec.size() == 1) {
                        final OTPreguntasHabilidad habilidad = itemsSelec.get(0);
                        txtHabilidad.setText(habilidad.getName());

                        final Float porcentajeLogrado = habilidad.getLogrado();
                        final Float porcentajeNologrado = habilidad.getNologrado();

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
                });
    }

    private void generateReport() {
        if (evaluacionPrueba != null && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty()) {
            txtAsignatura.setText(evaluacionPrueba.getAsignatura());
            txtCurso.setText(evaluacionPrueba.getCurso().getName());
            txtPrueba.setText(evaluacionPrueba.getPrueba().getName());
            obtenerResultados(evaluacionPrueba);

            if (mapaHabilidades != null && !mapaHabilidades.isEmpty()) {

                final ArrayList<OTPreguntasHabilidad> listado = new ArrayList<>(mapaHabilidades.values());
                final ObservableList<OTPreguntasHabilidad> oList = FXCollections.observableList(listado);
                tblHabilidades.setItems(oList);
            }
        }

    }

    @Override
    public void handle(ActionEvent event) {
        final Object source = event.getSource();
        if (source == mnuExportarHabilidad) {

            tblHabilidades.setId("Habilidades");

            final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
            listaTablas.add(tblHabilidades);

            ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
        } else if (source == btnGenerar) {
            generateReport();
        }
    }

    private void inicializarTablaHabilidades() {
        tblHabilidades.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colNombre.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>("name"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>("descripcion"));
        colLogrado.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>("slogrado"));
        colNoLogrado.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>("snlogrado"));
    }

    @FXML
    public void initialize() {
        inicializarTablaHabilidades();
        accionClicTabla();
        setTitle("Resumen Respuestas por Habilidades");
        graficoBarra.setTitle("Gráfico Respuestas por habilidad");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");
        mnuExportarHabilidad.setOnAction(this);
        btnGenerar.setOnAction(this);
        cmbTipoAlumno.setOnAction(event -> tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex());
    }

    /**
     * Este metodo evalua la cantidad de buenas de un String de respuesta
     * contrastado contra las respuestas esperadas.
     * 
     * @param respuestas
     *            Las respuestas del alumno.
     * @param respEsperadas
     *            Las respuestas correctas definidas en la prueba.
     * @param ahb
     *            La Habilidad en base al que se realiza el calculo.
     * @return Par <Preguntas buenas, Total de Preguntas> del eje.
     */
    private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas,
            List<RespuestasEsperadasPrueba> respEsperadas, Habilidad hab) {
        int nroBuenas = 0;
        int nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            final RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (!resp.isAnulada()) {
                if (resp.getHabilidad().equals(hab)) {
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

    private void obtenerResultados(EvaluacionPrueba entity) {
        final List<PruebaRendida> pruebasRendidas = entity.getPruebasRendidas();

        mapaHabilidades = new HashMap<Habilidad, OTPreguntasHabilidad>();

        final Prueba prueba = entity.getPrueba();
        final List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba.getRespuestas();

        for (final RespuestasEsperadasPrueba resp : respuestasEsperadas) {
            if (mapaHabilidades.containsKey(resp.getHabilidad()))
                continue;
            final OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
            otPreguntas.setHabilidad(resp.getHabilidad());
            otPreguntas.setBuenas(0);
            otPreguntas.setTotal(0);
            mapaHabilidades.put(resp.getHabilidad(), otPreguntas);
        }

        for (final PruebaRendida pruebaRendida : pruebasRendidas) {
            if (tipoAlumno != Constants.PIE_ALL
                    && !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
                continue;
            }
            final String respuesta = pruebaRendida.getRespuestas();

            for (final Habilidad hab : mapaHabilidades.keySet()) {
                final OTPreguntasHabilidad otPreguntas = mapaHabilidades.get(hab);
                final Pair<Integer, Integer> result = obtenerBuenasTotales(respuesta, respuestasEsperadas, hab);
                otPreguntas.setHabilidad(hab);
                otPreguntas.setBuenas(otPreguntas.getBuenas() + result.getFirst());
                otPreguntas.setTotal(otPreguntas.getTotal() + result.getSecond());

            }
        }
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof TipoAlumno) {
                final ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    tAlumnoList.add((TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
                cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
                generateReport();
            }
        }
    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof EvaluacionPrueba) {
            evaluacionPrueba = (EvaluacionPrueba) entity;
            generateReport();
        }
    }
}
