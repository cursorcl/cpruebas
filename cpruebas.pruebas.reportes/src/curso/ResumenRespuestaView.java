package curso;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTRespuestaPreguntas;
import cl.eos.ot.OTRespuestasPorcentaje;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Utils;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ResumenRespuestaView extends AFormView implements EventHandler<ActionEvent> {

  @FXML
  private TableView<OTRespuestaPreguntas> tblPreguntas;
  @FXML
  private TableColumn<OTRespuestaPreguntas, Integer> colPregunta;
  @FXML
  private TableColumn<OTRespuestaPreguntas, Integer> colBuenas;
  @FXML
  private TableColumn<OTRespuestaPreguntas, Integer> colMalas;
  @FXML
  private TableColumn<OTRespuestaPreguntas, Integer> colOmitidas;

  @FXML
  private TableView<OTRespuestasPorcentaje> tblPorcentaje;
  @FXML
  private TableColumn<OTRespuestasPorcentaje, String> colTitulo;
  @FXML
  private TableColumn<OTRespuestasPorcentaje, Float> colPorcentaje;

  @FXML
  private TextField txtPrueba;
  @FXML
  private TextField txtCurso;
  @FXML
  private TextField txtAsignatura;
  @FXML
  private TextField txtNroPreguntas;
  @FXML
  private TextField txtPuntaje;

  @FXML
  private BarChart<String, Number> graficoBarra = new BarChart<String, Number>(new CategoryAxis(), new NumberAxis());


  @FXML
  private MenuItem mnuExportarRespuestas;

  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  @FXML
  private Button btnGenerar;

  long tipoAlumno = Constants.PIE_ALL;

  private LinkedList<OTRespuestaPreguntas> listaRespuestas;
  private LinkedList<OTRespuestasPorcentaje> listaPorcentaje;
  private R_EvaluacionPrueba evaluacionPrueba;

  private R_Prueba prueba;
  private R_Asignatura asignatura;
  private R_Curso curso;
  private List<R_PruebaRendida> lstPruebasRendidas;

  public ResumenRespuestaView() {

  }

  private void generateReport() {
    if (asignatura != null && evaluacionPrueba != null && cmbTipoAlumno.getItems() != null
        && !cmbTipoAlumno.getItems().isEmpty() && curso != null && lstPruebasRendidas != null && prueba != null) {

      ((NumberAxis) graficoBarra.getYAxis()).setAutoRanging(false);
      ((NumberAxis) graficoBarra.getYAxis()).setUpperBound(prueba.getNropreguntas());
      txtAsignatura.setText(asignatura.getName());
      txtCurso.setText(curso.getName());
      final String nroPreguntas = String.valueOf(prueba.getNropreguntas());
      txtNroPreguntas.setText(nroPreguntas);
      txtPrueba.setText(prueba.getName());
      obtenerResultados(evaluacionPrueba);
      if (listaRespuestas != null && !listaRespuestas.isEmpty()) {
        final ObservableList<OTRespuestaPreguntas> oList = FXCollections.observableArrayList(listaRespuestas);
        tblPreguntas.setItems(oList);
      }

      if (listaPorcentaje != null && !listaPorcentaje.isEmpty()) {
        final ObservableList<OTRespuestasPorcentaje> oList = FXCollections.observableArrayList(listaPorcentaje);
        tblPorcentaje.setItems(oList);
      }
    }

  }

  @Override
  public void handle(ActionEvent event) {
    final Object source = event.getSource();
    if (source == mnuExportarRespuestas) {

      tblPreguntas.setId("Alumnos");
      tblPorcentaje.setId("Porcentaje");

      final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
      listaTablas.add(tblPreguntas);
      listaTablas.add(tblPorcentaje);

      ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
    } else if (source == btnGenerar) {
      generateReport();
    }
  }

  private void inicializarTablaPorcentaje() {
    tblPorcentaje.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colTitulo.setCellValueFactory(new PropertyValueFactory<OTRespuestasPorcentaje, String>("titulo"));
    colPorcentaje.setCellValueFactory(new PropertyValueFactory<OTRespuestasPorcentaje, Float>("porcentaje"));
  }

  private void inicializarTablaRespuestas() {
    tblPreguntas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colPregunta.setCellValueFactory(new PropertyValueFactory<OTRespuestaPreguntas, Integer>("numero"));
    colBuenas.setCellValueFactory(new PropertyValueFactory<OTRespuestaPreguntas, Integer>("buenas"));
    colMalas.setCellValueFactory(new PropertyValueFactory<OTRespuestaPreguntas, Integer>("malas"));
    colOmitidas.setCellValueFactory(new PropertyValueFactory<OTRespuestaPreguntas, Integer>("omitidas"));
    tblPreguntas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OTRespuestaPreguntas>() {

      @SuppressWarnings({"rawtypes", "unchecked"})
      @Override
      public void changed(ObservableValue<? extends OTRespuestaPreguntas> observable, OTRespuestaPreguntas oldValue,
          OTRespuestaPreguntas newValue) {
        final ObservableList<OTRespuestaPreguntas> itemsSelec = tblPreguntas.getSelectionModel().getSelectedItems();

        if (itemsSelec.size() == 1) {

          final OTRespuestaPreguntas ot = itemsSelec.get(0);
          final XYChart.Series series1 = new XYChart.Series();
          series1.setName("Nro. Preguntas");
          series1.getData().clear();
          series1.getData().add(new XYChart.Data("Buenas", ot.getBuenas()));
          series1.getData().add(new XYChart.Data("Malas", ot.getMalas()));
          series1.getData().add(new XYChart.Data("Omitidas", ot.getOmitidas()));

          graficoBarra.getData().clear();
          graficoBarra.getData().add(series1);
          graficoBarra.setLegendVisible(false);

        }
      }
    });
  }

  @FXML
  public void initialize() {
    setTitle("Resumen de respuestas por pregunta");
    inicializarTablaRespuestas();
    inicializarTablaPorcentaje();
    mnuExportarRespuestas.setOnAction(this);
    btnGenerar.setOnAction(this);
    cmbTipoAlumno.setOnAction(event -> tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void obtenerResultados(R_EvaluacionPrueba entity) {
    final Integer nroPreguntas = prueba.getNropreguntas();

    Integer buenas = 0;
    Integer malas = 0;
    Integer omitidas = 0;
    Float notas = 0f;

    final int[] sBuenas = new int[nroPreguntas];
    final int[] sMalas = new int[nroPreguntas];
    final int[] sOmitidas = new int[nroPreguntas];


    for (final R_PruebaRendida pruebaRendida : lstPruebasRendidas) {

      if (tipoAlumno != Constants.PIE_ALL && !pruebaRendida.getTipoalumno_id().equals(tipoAlumno)) {
        continue;
      }
      final String responses = prueba.getResponses();
      final String respuesta = pruebaRendida.getRespuestas();

      final char[] cResponses = responses.toUpperCase().toCharArray();
      final char[] cRespuesta = respuesta.toUpperCase().toCharArray();
      int contadorBuenas = 0;
      for (int i = 0; i < cResponses.length; i++) {

        if (cRespuesta[i] == 'O') {
          sOmitidas[i] = sOmitidas[i] + 1;
          omitidas = omitidas + 1;
        } else if (String.valueOf(cRespuesta[i]).toUpperCase().equals(String.valueOf(cResponses[i]).toUpperCase())
            || cRespuesta[i] == '+') {
          sBuenas[i] = sBuenas[i] + 1;
          buenas = buenas + 1;
          contadorBuenas++;
        } else {
          sMalas[i] = sMalas[i] + 1;
          malas = malas + 1;
        }
      }

      notas = notas + Utils.getNota(nroPreguntas, prueba.getExigencia(), contadorBuenas, prueba.getPuntajebase());
    }

    final float nroAlumnos = lstPruebasRendidas.size();
    final float nota = notas / nroAlumnos;

    txtPuntaje.setText(String.valueOf(Utils.getPuntaje(nota)));

    listaRespuestas = new LinkedList<OTRespuestaPreguntas>();
    for (int i = 0; i < nroPreguntas; i++) {
      final OTRespuestaPreguntas otRespuesta = new OTRespuestaPreguntas();
      otRespuesta.setName(String.valueOf(i + 1));
      otRespuesta.setNumero(i + 1);
      otRespuesta.setBuenas(sBuenas[i]);
      otRespuesta.setMalas(sMalas[i]);
      otRespuesta.setOmitidas(sOmitidas[i]);
      listaRespuestas.add(otRespuesta);
    }

    listaPorcentaje = new LinkedList<OTRespuestasPorcentaje>();
    final OTRespuestasPorcentaje respuestaPorcentajeB = new OTRespuestasPorcentaje();
    respuestaPorcentajeB.setTitulo("Buenas");
    final float porcentajeBuenas = buenas / nroAlumnos / nroPreguntas;
    respuestaPorcentajeB.setPorcentaje(porcentajeBuenas * 100);
    listaPorcentaje.add(respuestaPorcentajeB);

    final OTRespuestasPorcentaje respuestaPorcentajeM = new OTRespuestasPorcentaje();
    respuestaPorcentajeM.setTitulo("Malas");
    final float porcentajeMalas = malas / nroAlumnos / nroPreguntas;
    respuestaPorcentajeM.setPorcentaje(porcentajeMalas * 100);
    listaPorcentaje.add(respuestaPorcentajeM);

    final OTRespuestasPorcentaje respuestaPorcentaje = new OTRespuestasPorcentaje();
    respuestaPorcentaje.setTitulo("Omitidas");
    final float porcentajeOmitidas = omitidas / nroAlumnos / nroPreguntas;
    respuestaPorcentaje.setPorcentaje(porcentajeOmitidas * 100);
    listaPorcentaje.add(respuestaPorcentaje);

    final XYChart.Series series1 = new XYChart.Series();
    series1.setName("Porcentaje de Respuestas");
    series1.getData().clear();
    series1.getData().add(new XYChart.Data<String, Float>("Buenas", porcentajeBuenas));
    series1.getData().add(new XYChart.Data<String, Float>("Malas", porcentajeMalas));
    series1.getData().add(new XYChart.Data<String, Float>("Omitidas", porcentajeOmitidas));
    graficoBarra.getData().clear();
    graficoBarra.getData().add(series1);
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
      curso = controller.findByIdSynchro(R_Curso.class, evaluacionPrueba.getCurso_id());
      Map<String, Object> params =
          MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
      lstPruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);

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
