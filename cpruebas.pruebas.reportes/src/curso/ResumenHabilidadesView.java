package curso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
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

  @FXML
  final NumberAxis yNumber = new NumberAxis();
  @FXML
  final CategoryAxis xCategory = new CategoryAxis();
  @FXML
  private BarChart<String, Number> graficoBarra = new BarChart<String, Number>(xCategory, yNumber);

  private HashMap<R_Habilidad, OTPreguntasHabilidad> mapaHabilidades;
  @FXML
  private MenuItem mnuExportarHabilidad;

  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  @FXML
  private Button btnGenerar;
  long tipoAlumno = Constants.PIE_ALL;

  private R_EvaluacionPrueba evaluacionPrueba;


  private R_Prueba prueba;
  private R_Asignatura asignatura;
  private R_Curso curso;
  private List<R_PruebaRendida> lstPruebasRendidas;
  private List<R_RespuestasEsperadasPrueba> respuestasEsperadas;

  public ResumenHabilidadesView() {

  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void accionClicTabla() {
    tblHabilidades.getSelectionModel().selectedItemProperty()
        .addListener((ChangeListener<OTPreguntasHabilidad>) (observable, oldValue, newValue) -> {
          final ObservableList<OTPreguntasHabilidad> itemsSelec = tblHabilidades.getSelectionModel().getSelectedItems();

          if (itemsSelec.size() == 1) {
            final OTPreguntasHabilidad habilidad = itemsSelec.get(0);
            txtHabilidad.setText(habilidad.getName());

            final Float porcentajeLogrado = habilidad.getLogrado();
            final Float porcentajeNologrado = habilidad.getNologrado();

            final XYChart.Series series1 = new XYChart.Series();
            series1.setName("Porcentaje");
            series1.getData().add(new XYChart.Data<String, Float>("Logrado", porcentajeLogrado));
            series1.getData().add(new XYChart.Data<String, Float>("No Logrado", porcentajeNologrado));
            graficoBarra.getData().clear();
            graficoBarra.getData().add(series1);
            graficoBarra.setLegendVisible(false);

            for (final Series<String, Number> s : graficoBarra.getData()) {
              for (final Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(),
                    new Tooltip(String.format("%s = %2.1f", d.getXValue().toString(), d.getYValue().doubleValue())));
              }
            }
          }
        });
  }

  private void generateReport() {
    if (asignatura != null && evaluacionPrueba != null && cmbTipoAlumno.getItems() != null
        && !cmbTipoAlumno.getItems().isEmpty() && prueba != null &&  lstPruebasRendidas != null
            && respuestasEsperadas != null && curso != null) {
      txtAsignatura.setText(asignatura.getName());
      txtCurso.setText(curso.getName());
      txtPrueba.setText(prueba.getName());
      obtenerResultados();

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
    
    yNumber.setAutoRanging(false);
    yNumber.setUpperBound(100);
    inicializarTablaHabilidades();
    accionClicTabla();
    setTitle("Resumen Respuestas por Habilidades");
    graficoBarra.setTitle("Gráfico Respuestas por habilidad");
    mnuExportarHabilidad.setOnAction(this);
    btnGenerar.setOnAction(this);
    cmbTipoAlumno.setOnAction(event -> tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex());
  }

  /**
   * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las
   * respuestas esperadas.
   * 
   * @param respuestas Las respuestas del alumno.
   * @param respEsperadas Las respuestas correctas definidas en la prueba.
   * @param ahb La R_Habilidad en base al que se realiza el calculo.
   * @return Par <Preguntas buenas, Total de Preguntas> del eje.
   */
  private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas,
      List<R_RespuestasEsperadasPrueba> respEsperadas, R_Habilidad hab) {
    int nroBuenas = 0;
    int nroPreguntas = 0;
    for (int n = 0; n < respEsperadas.size(); n++) {
      final R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
      if (!resp.getAnulada()) {
        if (hab.getId().equals(resp.getHabilidad_id())) {
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

  private void obtenerResultados() {

    mapaHabilidades = new HashMap<R_Habilidad, OTPreguntasHabilidad>();

    Long[] ids = respuestasEsperadas.stream().map(r -> r.getHabilidad_id()).distinct().toArray(n -> new Long[n]);
    List<R_Habilidad> lstHabilidades = controller.findByAllIdSynchro(R_Habilidad.class, ids);


    for (final R_Habilidad hab : lstHabilidades) {
      if (mapaHabilidades.containsKey(hab))
        continue;
      final OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
      otPreguntas.setHabilidad(hab);
      otPreguntas.setBuenas(0);
      otPreguntas.setTotal(0);
      mapaHabilidades.put(hab, otPreguntas);
    }

    for (final R_PruebaRendida pruebaRendida : lstPruebasRendidas) {
      if (tipoAlumno != Constants.PIE_ALL && !pruebaRendida.getAlumno_id().equals(tipoAlumno)) {
        continue;
      }
      final String respuesta = pruebaRendida.getRespuestas();

      for (final R_Habilidad hab : mapaHabilidades.keySet()) {
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
      Map<String, Object> params =
          MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
      lstPruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
      params =
          MapBuilder.<String, Object>unordered().put("prueba_id", evaluacionPrueba.getPrueba_id()).build();
      respuestasEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
      curso = controller.findByIdSynchro(R_Curso.class, evaluacionPrueba.getCurso_id());
      generateReport();
    }

    if (entity instanceof R_Prueba) {
      prueba = (R_Prueba) entity;
      generateReport();
    }

    if (entity instanceof R_Asignatura) {
      asignatura = (R_Asignatura) entity;
      generateReport();
    }
  }
}
