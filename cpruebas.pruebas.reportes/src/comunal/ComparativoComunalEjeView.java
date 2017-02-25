package comunal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoColegio;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;

public class ComparativoComunalEjeView extends AFormView implements EventHandler<ActionEvent> {

  private static Logger log = Logger.getLogger(ComparativoComunalEjeView.class.getName());
  private final NumberFormat formatter = new DecimalFormat("#0.00");
  @FXML
  private Label lblTitulo;
  @FXML
  private MenuItem mnuExportarEjesTematicos;
  @FXML
  private MenuItem mnuExportarEvaluacion;
  @FXML
  private TableView<ObservableList<String>> tblEjesTematicos;
  @FXML
  private TableView<ObservableList<String>> tblEvaluacionEjesTematicos;

  private HashMap<R_Ejetematico, HashMap<String, OTPreguntasEjes>> mapaEjesTematicos;

  private Map<Long, R_EvaluacionEjetematico> mEvaluaciones;

  private Map<R_EvaluacionEjetematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;

  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  @FXML
  private Button btnGenerar;
  @FXML
  private ComboBox<R_TipoColegio> cmbTipoColegio;

  long tipoAlumno = Constants.PIE_ALL;
  long tipoColegio = Constants.TIPO_COLEGIO_ALL;

  List<R_EvaluacionPrueba> listaEvaluaciones;
  List<R_RespuestasEsperadasPrueba> respuestasEsperadas;

  private ArrayList<String> titulosColumnas;
  private R_Prueba prueba;
  /**
   * Asociación del Evaluación (ID) con el Colegio correspondiente.
   */
  private Map<Long, String> mapColegioCurso = new HashMap<>();
  private List<R_Colegio> lstColegios = new ArrayList<>();
  private R_Asignatura asignatura;
  private boolean llegaOnFound = false;
  private boolean llegaTipoAlumno = false;
  private boolean llegaEvaluacionEjeTematico = false;
  private boolean llegaTipoColegio;
  private boolean llegaEvaluaciones;
  private List<R_Curso> lstCursos;

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void creacionColumnasEjesTematicos() {

    tblEjesTematicos.getColumns().clear();

    final TableColumn columna0 = new TableColumn("Eje Temático");
    columna0.setCellValueFactory(param -> new SimpleStringProperty(
        ((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));
    columna0.setPrefWidth(175);
    columna0.setStyle("-fx-font-size:11px;");
    tblEjesTematicos.getColumns().add(columna0);

    titulosColumnas = new ArrayList<>();
    int indice = 1;
    for (final R_EvaluacionPrueba evaluacion : listaEvaluaciones) {
      R_Curso curso = controller.findByIdSynchro(R_Curso.class, evaluacion.getCurso_id());
      R_Colegio colegio = controller.findByIdSynchro(R_Colegio.class, evaluacion.getColegio_id());
      final String colegioCurso = String.format("%s\n%s", colegio.getName().toUpperCase(), curso.getName());
      mapColegioCurso.put(evaluacion.getId(), colegioCurso);

      if (!lstColegios.contains(colegio))
        lstColegios.add(colegio);

      // Columnas
      final int col = indice;
      titulosColumnas.add(colegioCurso);
      final TableColumn columna = new TableColumn(colegioCurso);
      columna.setCellValueFactory(param -> new SimpleStringProperty(
          ((CellDataFeatures<ObservableList, String>) param).getValue().get(col).toString()));
      columna.setPrefWidth(100);
      columna.setStyle("-fx-alignment: CENTER-RIGHT;");
      tblEjesTematicos.getColumns().add(columna);
      indice++;
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void creacionColumnasEvaluaciones() {
    tblEvaluacionEjesTematicos.getColumns().clear();
    final TableColumn columna0 = new TableColumn("");
    columna0.setCellValueFactory(param -> new SimpleStringProperty(
        ((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));
    columna0.setPrefWidth(100);
    columna0.setStyle("-fx-font-size:11px;");
    tblEvaluacionEjesTematicos.getColumns().add(columna0);
    int indice = 1;
    for (final String evaluacion : titulosColumnas) {
      // Columnas
      final int col = indice;
      final String colegioCurso = evaluacion;
      final TableColumn columna = new TableColumn(colegioCurso);
      columna.setCellValueFactory(param -> new SimpleStringProperty(
          ((CellDataFeatures<ObservableList, String>) param).getValue().get(col).toString()));
      columna.setPrefWidth(100);
      columna.setStyle("-fx-alignment: CENTER-RIGHT;");
      tblEvaluacionEjesTematicos.getColumns().add(columna);
      indice++;
    }
  }

  private void desplegarDatosEjesTematicos() {

    final ObservableList<ObservableList<String>> registros = FXCollections.observableArrayList();


    for (final Entry<R_Ejetematico, HashMap<String, OTPreguntasEjes>> mapa : mapaEjesTematicos.entrySet()) {

      final ObservableList<String> row = FXCollections.observableArrayList();
      R_Ejetematico eje = mapa.getKey();
      if (eje == null)
        continue;
      row.add(eje.getName());

      final HashMap<String, OTPreguntasEjes> resultados = mapa.getValue();

      for (final String string : titulosColumnas) {
        final OTPreguntasEjes otPregunta = resultados.get(string);
        if (otPregunta != null) {// EOS
          row.add(formatter.format(otPregunta.getLogrado()));
        } else {// EOS
          row.add("");
        }
      }

      registros.add(row);
    }
    tblEjesTematicos.setItems(registros);
  }

  private void desplegarDatosEvaluaciones() {

    final Map<String, Integer> totales = new HashMap<>();

    final ObservableList<ObservableList<String>> registroseEva = FXCollections.observableArrayList();
    ObservableList<String> row = null;
    int total = 0;

    final List<R_EvaluacionEjetematico> ejes = new ArrayList<>(mapEvaAlumnos.keySet());
    Collections.sort(ejes, Comparadores.comparaEvaluacionEjeTematico());
    for (final R_EvaluacionEjetematico eje : ejes) {
      final HashMap<String, OTPreguntasEvaluacion> resultados = mapEvaAlumnos.get(eje);
      row = FXCollections.observableArrayList();
      row.add(eje.getName());

      for (final String string : titulosColumnas) {
        final OTPreguntasEvaluacion otPregunta = resultados.get(string);
        if (otPregunta != null) { // EOS
          if (totales.containsKey(string)) {
            total = otPregunta.getAlumnos() + totales.get(string);
            totales.replace(string, total);
          } else {
            totales.put(string, otPregunta.getAlumnos());
          }
          row.add(String.valueOf(otPregunta.getAlumnos()));
        } else {// EOS
          row.add("");
          totales.put(string, 0);
        }

      }
      registroseEva.add(row);

    }
    row = FXCollections.observableArrayList();
    row.add("Totales");
    for (final String string : titulosColumnas) {
      final Integer valor = totales.get(string);
      row.add(valor == null ? "0" : String.valueOf(valor));
    }
    registroseEva.add(row);

    tblEvaluacionEjesTematicos.setItems(registroseEva);
  }

  private void generaDatosEvaluacion(R_PruebaRendida pruebaRendida, String colegioCurso) {

    HashMap<String, OTPreguntasEvaluacion> mapaOT;

    final Float pBuenas = pruebaRendida.getBuenas().floatValue();
    for (final Entry<Long, R_EvaluacionEjetematico> mEvaluacion : mEvaluaciones.entrySet()) {

      final R_EvaluacionEjetematico evaluacionAl = mEvaluacion.getValue();
      if (mapEvaAlumnos.containsKey(evaluacionAl)) {
        final HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
        if (evaluacion.containsKey(colegioCurso)) {
          final OTPreguntasEvaluacion otPreguntas = evaluacion.get(colegioCurso);

          if (pBuenas >= evaluacionAl.getNrorangomin() && pBuenas <= evaluacionAl.getNrorangomax()) {
            otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
          }
        } else {

          final OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
          if (pBuenas >= evaluacionAl.getNrorangomin() && pBuenas <= evaluacionAl.getNrorangomax()) {
            pregunta.setAlumnos(1);
          } else {
            pregunta.setAlumnos(0);
          }
          pregunta.setEvaluacion(evaluacionAl);
          evaluacion.put(colegioCurso, pregunta);
        }
      } else {
        final OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
        if (pBuenas >= evaluacionAl.getNrorangomin() && pBuenas <= evaluacionAl.getNrorangomax()) {
          pregunta.setAlumnos(1);
        } else {
          pregunta.setAlumnos(0);
        }
        pregunta.setEvaluacion(evaluacionAl);

        mapaOT = new HashMap<>();
        mapaOT.put(colegioCurso, pregunta);
        mapEvaAlumnos.put(evaluacionAl, mapaOT);
      }
    }

  }

  @Override
  public void handle(ActionEvent event) {
    final Object source = event.getSource();
    if (source == mnuExportarEjesTematicos || source == mnuExportarEvaluacion) {

      tblEjesTematicos.setId("Ejes temáticos");
      tblEvaluacionEjesTematicos.setId("Evaluación");
      final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
      listaTablas.add(tblEjesTematicos);
      listaTablas.add(tblEvaluacionEjesTematicos);

      ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
    } else if (source == btnGenerar) {
      if (prueba != null && tipoAlumno != -1) {
        procesaDatosReporte();
      }
    }
  }

  private void informarProblemas(String colegioCurso, Long al, String respuesta) {
    final Alert alert = new Alert(AlertType.ERROR);
    R_Alumno alumno = controller.findByIdSynchro(R_Alumno.class, al);
    alert.setTitle("Alumno con respuestas incompletas.");
    alert.setHeaderText(String.format("%s/%s", colegioCurso, alumno.toString()));
    alert.setContentText(String.format("La respuesta [%s] es incompleta", respuesta));
    alert.showAndWait();

  }

  @FXML
  public void initialize() {
    setTitle("Resumen comparativo comunal ejes temáticas");
    tblEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    tblEvaluacionEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    mnuExportarEjesTematicos.setOnAction(this);
    mnuExportarEvaluacion.setOnAction(this);
    btnGenerar.setOnAction(this);
    cmbTipoAlumno.setOnAction(event -> {
      if (cmbTipoAlumno.getSelectionModel() == null)
        return;
      tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
    });

    cmbTipoColegio.setOnAction(event -> {
      if (cmbTipoColegio.getSelectionModel().getSelectedItem() == null)
        return;
      tipoColegio = cmbTipoColegio.getSelectionModel().getSelectedItem().getId();
    });

  }

  /**
   * @return the prueba
   */
  public final R_Prueba getPrueba() {
    return prueba;
  }

  /**
   * @param prueba the prueba to set
   */
  public final void setPrueba(R_Prueba prueba) {
    this.prueba = prueba;
  }

  private void generarReporte() {

    final StringBuilder buffer = new StringBuilder();
    buffer.append(asignatura);
    lblTitulo.setText(buffer.toString());


    ProgressForm pForm = new ProgressForm();
    pForm.title("Procesando");
    pForm.message("Esto tomará algunos segundos.");

    Task<Void> task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {

        mapaEjesTematicos = new HashMap<>();
        mapEvaAlumnos = new HashMap<>();
        HashMap<String, OTPreguntasEjes> mapaColegios;


        int total = listaEvaluaciones.size();
        int n = 0;
        for (final R_EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {

          updateMessage("Procesando:" + evaluacionPrueba.getName());
          updateProgress(++n, total);

          final String colegioCurso = mapColegioCurso.get(evaluacionPrueba.getId());


          Map<String, Object> params =
              MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
          final List<R_PruebaRendida> pruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);

          for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {
            Long alumno = pruebaRendida.getAlumno_id();
            if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != pruebaRendida.getTipoalumno_id())
              continue;

            R_Colegio colegio = lstColegios.stream().filter(c -> c.getId().equals(evaluacionPrueba.getColegio_id()))
                .findFirst().orElse(null);

            if (colegio == null
                || tipoColegio != Constants.TIPO_COLEGIO_ALL && tipoColegio != colegio.getTipocolegio_id())
              continue;

            generaDatosEvaluacion(pruebaRendida, colegioCurso);

            final String respuesta = pruebaRendida.getRespuestas().toUpperCase();

            if (respuesta == null || respuesta.length() < prueba.getNropreguntas()) {
              informarProblemas(colegioCurso, alumno, respuesta);
              continue;
            }
            final char[] cRespuesta = respuesta.toUpperCase().toCharArray();

            for (final R_RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {
              if (respuestasEsperadasPrueba.getAnulada()) {
                continue;
              }
              final Long idEjeTematico = respuestasEsperadasPrueba.getEjetematico_id();
              final Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
              R_Ejetematico ejeTematico = mapaEjesTematicos.keySet().stream()
                  .filter(e -> e.getId().equals(idEjeTematico)).findFirst().orElse(null);


              if (ejeTematico != null) {
                final HashMap<String, OTPreguntasEjes> mapa = mapaEjesTematicos.get(ejeTematico);

                if (mapa.containsKey(colegioCurso)) {
                  final OTPreguntasEjes otPregunta = mapa.get(colegioCurso);

                  if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
                    otPregunta.setBuenas(otPregunta.getBuenas() + 1);
                  }
                  otPregunta.setTotal(otPregunta.getTotal() + 1);
                } else {
                  final OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
                  otPreguntas.setEjeTematico(ejeTematico);
                  if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
                    otPreguntas.setBuenas(1);
                  } else {
                    otPreguntas.setBuenas(0);
                  }
                  otPreguntas.setTotal(1);

                  mapa.put(colegioCurso, otPreguntas);
                }
              } else {
                ejeTematico = controller.findByIdSynchro(R_Ejetematico.class, idEjeTematico);
                final OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
                otPreguntas.setEjeTematico(ejeTematico);
                if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
                  otPreguntas.setBuenas(1);
                } else {
                  otPreguntas.setBuenas(0);
                }
                otPreguntas.setTotal(1);

                mapaColegios = new HashMap<>();
                mapaColegios.put(colegioCurso, otPreguntas);
                mapaEjesTematicos.put(ejeTematico, mapaColegios);
              }
            }
          }
        }
        updateMessage("Desplegando datos....");
        Runnable r = () -> {
          creacionColumnasEjesTematicos();
          creacionColumnasEvaluaciones();
          desplegarDatosEjesTematicos();
          desplegarDatosEvaluaciones();
        };
        Platform.runLater(r);
        return null;

      }
    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        pForm.getDialogStage().hide();
      }
    });
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        log.severe("Se ha producido el siguiente error:" + event.getEventType().toString());
        pForm.getDialogStage().hide();
      }
    });

    pForm.showWorkerProgress(task);
    Executors.newSingleThreadExecutor().execute(task);


  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      final Object entity = list.get(0);
      if (entity instanceof R_EvaluacionEjetematico) {
        llegaEvaluacionEjeTematico = true;
        mEvaluaciones = new HashMap<>();
        for (final Object object : list) {
          final R_EvaluacionEjetematico eje = (R_EvaluacionEjetematico) object;
          mEvaluaciones.put(eje.getId(), eje);
        }
      }
      if (entity instanceof R_TipoAlumno) {
        final ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        llegaTipoAlumno = true;
        for (final Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);
        cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
      }
      if (entity instanceof R_TipoColegio) {
        final ObservableList<R_TipoColegio> tColegioList = FXCollections.observableArrayList();
        llegaTipoColegio = true;
        for (final Object iEntity : list) {
          tColegioList.add((R_TipoColegio) iEntity);
        }
        cmbTipoColegio.setItems(tColegioList);
        final R_TipoColegio tColegio = new R_TipoColegio.Builder().id(Constants.TIPO_COLEGIO_ALL).build();
        cmbTipoColegio.getSelectionModel().select(tColegio);
      }
      if (entity instanceof R_EvaluacionPrueba) {
        listaEvaluaciones = new ArrayList<>();
        llegaEvaluaciones = true;
        for (final Object iEntity : list) {
          listaEvaluaciones.add((R_EvaluacionPrueba) iEntity);
        }

        lstColegios = controller.findAllSynchro(R_Colegio.class);
        Long[] ids = listaEvaluaciones.stream().map(e -> e.getCurso_id()).toArray(size -> new Long[size]);
        lstCursos = controller.findByAllIdSynchro(R_Curso.class, ids);
        for (R_EvaluacionPrueba eva : listaEvaluaciones) {
          R_Colegio colegio =
              lstColegios.stream().filter(c -> c.getId().equals(eva.getColegio_id())).findFirst().orElse(null);
          R_Curso curso = lstCursos.stream().filter(c -> c.getId().equals(eva.getCurso_id())).findFirst().orElse(null);
          final String colegioCurso = String.format("%s\n%s", colegio.getName().toUpperCase(), curso.getName());
          mapColegioCurso.put(eva.getId(), colegioCurso);
        }


      }
      if (entity instanceof R_RespuestasEsperadasPrueba) {
        respuestasEsperadas = new ArrayList<>();
        for (final Object iEntity : list) {
          respuestasEsperadas.add((R_RespuestasEsperadasPrueba) iEntity);
        }
      }

    }
  }

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof R_Asignatura) {
      asignatura = (R_Asignatura) entity;
      llegaOnFound = true;
    }
  }

  private void procesaDatosReporte() {
    if (llegaEvaluacionEjeTematico && llegaTipoAlumno && llegaOnFound && llegaTipoColegio && llegaEvaluaciones) {
      generarReporte();

    }
  }
}
