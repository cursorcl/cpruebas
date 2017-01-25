package comunal.nivel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoColegio;
import cl.eos.util.ExcelSheetWriterObj;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class Nivel_ComparativoComunalHabilidadView extends AFormView
    implements EventHandler<ActionEvent> {

  private NumberFormat formatter = new DecimalFormat("#0.00");
  @FXML
  private Label lblTitulo;
  @FXML
  private MenuItem mnuExportarHabilidad;
  @FXML
  private MenuItem mnuExportarEvaluacion;
  @FXML
  private TableView<ObservableList<String>> tblHabilidades;
  @FXML
  private TableView<ObservableList<String>> tblEvaluaciones;

  private HashMap<R_Habilidad, HashMap<String, OTPreguntasHabilidad>> mapaHabilidad;

  private Map<Long, R_EvaluacionEjetematico> mEvaluaciones;

  private Map<R_EvaluacionEjetematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;
  private List<R_EvaluacionPrueba> listaEvaluaciones;
  private List<R_RespuestasEsperadasPrueba> respuestasEsperadas;

  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  @FXML
  private Button btnGenerar;
  @FXML
  private ComboBox<R_TipoColegio> cmbTipoColegio;

  long tipoAlumno = Constants.PIE_ALL;
  long tipoColegio = Constants.TIPO_COLEGIO_ALL;

  private boolean llegaOnFound = false;
  private boolean llegaEvaluacionEjeTematico = false;
  private boolean llegaTipoAlumno = false;
  private ArrayList<String> titulosColumnas;
  private R_Prueba prueba;
  private boolean llegaTipoColegio;

  private R_Asignatura asignatura;
  private boolean llegaAsignatura;
  private boolean llegTipoCurso;
  private Map<Long, R_Colegio> mapColegios = new HashMap<>();


  @FXML
  public void initialize() {
    this.setTitle("Resumen Comparativo Comunal R_Habilidad x Nivel");
    tblHabilidades.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    tblEvaluaciones.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    mnuExportarHabilidad.setOnAction(this);
    mnuExportarEvaluacion.setOnAction(this);
    btnGenerar.setOnAction(this);
    cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (cmbTipoAlumno.getSelectionModel() == null)
          return;
        tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
      }
    });
    cmbTipoColegio.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (cmbTipoColegio.getSelectionModel().getSelectedItem() == null)
          return;
        tipoColegio = cmbTipoColegio.getSelectionModel().getSelectedItem().getId();
      }
    });
  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  private void creacionColumnasHabilidades() {
    tblHabilidades.getColumns().clear();
    TableColumn columna0 = new TableColumn("Habilidad");

    columna0.setCellValueFactory(param -> new SimpleStringProperty(
        ((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));
    columna0.setPrefWidth(100);
    tblHabilidades.getColumns().add(columna0);

    titulosColumnas = new ArrayList<>();
    int indice = 1;
    for (R_EvaluacionPrueba evaluacion : listaEvaluaciones) {
      // Columnas
      final int col = indice;
      R_Colegio colegio = mapColegios.get(evaluacion.getColegio_id());
      if (colegio == null) {
        colegio = controller.findByIdSynchro(R_Colegio.class, evaluacion.getColegio_id());
        mapColegios.put(evaluacion.getColegio_id(), colegio);
      }

      titulosColumnas.add(colegio.getName().toUpperCase());
      final TableColumn columna = new TableColumn(colegio.getName().toUpperCase());
      columna.setCellValueFactory(param -> new SimpleStringProperty(
          ((CellDataFeatures<ObservableList, String>) param).getValue().get(col).toString()));
      columna.setPrefWidth(100);
      tblHabilidades.getColumns().add(columna);
      indice++;
    }

  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void creacionColumnasEvaluaciones() {
    tblEvaluaciones.getColumns().clear();

    TableColumn columna0 = new TableColumn("");
    columna0.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(0).toString());
          }
        });
    columna0.setPrefWidth(100);
    tblEvaluaciones.getColumns().add(columna0);

    int indice = 1;
    for (String evaluacion : titulosColumnas) {

      // Columnas
      final int col = indice;
      final String colegioCurso = evaluacion;
      TableColumn columna = new TableColumn(colegioCurso);
      columna.setCellValueFactory(
          new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(col).toString());
            }
          });
      columna.setPrefWidth(100);
      tblEvaluaciones.getColumns().add(columna);
      indice++;
    }
  }

  private void desplegarDatosHabilidades() {
    ObservableList<ObservableList<String>> registros = FXCollections.observableArrayList();

    for (Entry<R_Habilidad, HashMap<String, OTPreguntasHabilidad>> mapa : mapaHabilidad
        .entrySet()) {

      ObservableList<String> row = FXCollections.observableArrayList();

      row.add(((R_Habilidad) mapa.getKey()).getName());

      HashMap<String, OTPreguntasHabilidad> resultados = mapa.getValue();

      for (String string : titulosColumnas) {
        OTPreguntasHabilidad otPregunta = resultados.get(string);
        if (otPregunta != null) {// EOS
          row.add(formatter.format(otPregunta.getLogrado()));
        } else {// EOS
          row.add("");
        }
      }

      registros.add(row);
    }
    tblHabilidades.setItems(registros);
  }
  
  private void desplegarDatosEvaluaciones() {

    Map<String, Integer> totales = new HashMap<String, Integer>();

    ObservableList<ObservableList<String>> registroseEva = FXCollections.observableArrayList();
    ObservableList<String> row = null;
    int total = 0;

    List<R_EvaluacionEjetematico> ejes = new ArrayList<>(mapEvaAlumnos.keySet());
    Collections.sort(ejes, Comparadores.comparaEvaluacionEjeTematico());
    for (R_EvaluacionEjetematico eje : ejes) {
      HashMap<String, OTPreguntasEvaluacion> resultados = mapEvaAlumnos.get(eje);
      row = FXCollections.observableArrayList();
      row.add(eje.getName());

      for (String string : titulosColumnas) {
        OTPreguntasEvaluacion otPregunta = resultados.get(string);
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
    for (String string : titulosColumnas) {
      Integer valor = totales.get(string);
      row.add(String.valueOf(valor == null ? 0 : valor));
    }
    registroseEva.add(row);

    tblEvaluaciones.setItems(registroseEva);
  }
  
  private void generaDatosEvaluacion(R_PruebaRendida pruebaRendida, String colegioNivel) {

    HashMap<String, OTPreguntasEvaluacion> mapaOT = new HashMap<String, OTPreguntasEvaluacion>();

    Float pBuenas = pruebaRendida.getBuenas().floatValue();
    for (Entry<Long, R_EvaluacionEjetematico> mEvaluacion : mEvaluaciones.entrySet()) {

      R_EvaluacionEjetematico evaluacionAl = mEvaluacion.getValue();
      if (mapEvaAlumnos.containsKey(evaluacionAl)) {
        HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
        if (evaluacion.containsKey(colegioNivel)) {
          OTPreguntasEvaluacion otPreguntas = evaluacion.get(colegioNivel);

          if (pBuenas >= evaluacionAl.getNrorangomin()
              && pBuenas <= evaluacionAl.getNrorangomax()) {
            otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
          }
        } else {

          OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
          if (pBuenas >= evaluacionAl.getNrorangomin()
              && pBuenas <= evaluacionAl.getNrorangomax()) {
            pregunta.setAlumnos(1);
          } else {
            pregunta.setAlumnos(0);
          }
          pregunta.setEvaluacion(evaluacionAl);
          evaluacion.put(colegioNivel, pregunta);
        }
      } else {
        OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
        if (pBuenas >= evaluacionAl.getNrorangomin() && pBuenas <= evaluacionAl.getNrorangomax()) {
          pregunta.setAlumnos(1);
        } else {
          pregunta.setAlumnos(0);
        }
        pregunta.setEvaluacion(evaluacionAl);

        mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
        mapaOT.put(colegioNivel, pregunta);
        mapEvaAlumnos.put(evaluacionAl, mapaOT);
      }
    }

  }

  @Override
  public void handle(ActionEvent event) {
    Object source = event.getSource();
    if (source == mnuExportarHabilidad || source == mnuExportarEvaluacion) {

      tblHabilidades.setId("Habilidades");
      tblEvaluaciones.setId("Evaluación");

      List<TableView<? extends Object>> listaTablas = new LinkedList<>();
      listaTablas.add((TableView<? extends Object>) tblHabilidades);
      listaTablas.add((TableView<? extends Object>) tblEvaluaciones);
      ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
    } else if (source == btnGenerar) {
      if (prueba != null && tipoAlumno != -1) {
        procesaDatosReporte();
      }
    }
  }
  

  private void informarProblemas(String colegioTipoCurso, Long al, String respuesta) {
    final Alert alert = new Alert(AlertType.ERROR);
    R_Alumno alumno = controller.findByIdSynchro(R_Alumno.class, al);
    alert.setTitle("Alumno con respuestas incompletas.");
    alert.setHeaderText(String.format("%s/%s", colegioTipoCurso, alumno.toString()));
    alert.setContentText(String.format("La respuesta [%s] es incompleta", respuesta));
    alert.showAndWait();

  }

  
  private void llenarDatosTabla() {

    if (llegaOnFound && llegaEvaluacionEjeTematico) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(asignatura);
      lblTitulo.setText(buffer.toString());

      mapaHabilidad = new HashMap<>();
      mapEvaAlumnos = new HashMap<>();
      HashMap<String, OTPreguntasHabilidad> mapaColegios = null;

      creacionColumnasHabilidades();
      creacionColumnasEvaluaciones();

      for (R_EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
        R_Colegio colegio = mapColegios.get(evaluacionPrueba.getColegio_id());
        final String colegioNivel = colegio.getName();

        final List<R_PruebaRendida> pruebasRendidas =
            controller.findByParamsSynchro(R_PruebaRendida.class, null);

        for (R_PruebaRendida pruebaRendida : pruebasRendidas) {


          if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != pruebaRendida.getTipoalumno_id())
            continue;
          if (tipoColegio != Constants.TIPO_COLEGIO_ALL
              && tipoColegio != colegio.getTipocolegio_id())
            continue;

          generaDatosEvaluacion(pruebaRendida, colegioNivel);

          String respuesta = pruebaRendida.getRespuestas().toUpperCase();
          
          if (respuesta == null || respuesta.length() < prueba.getNropreguntas()) {
            informarProblemas(colegioNivel, pruebaRendida.getAlumno_id(), respuesta);
            continue;
          }

          char[] cRespuesta = respuesta.toUpperCase().toCharArray();

          for (R_RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {
            if (respuestasEsperadasPrueba.getAnulada()) {
              continue;
            }

            Long idHabilidad = respuestasEsperadasPrueba.getHabilidad_id();
            R_Habilidad habilidad = mapaHabilidad.keySet().stream()
                .filter(e -> e.getId().equals(idHabilidad)).findFirst().orElse(null);
            
            Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
            if (habilidad  != null) {
              HashMap<String, OTPreguntasHabilidad> mapa = mapaHabilidad.get(habilidad);

              if (mapa.containsKey(colegioNivel)) {
                OTPreguntasHabilidad otPregunta = mapa.get(colegioNivel);

                if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
                    .toCharArray()[0]) {
                  otPregunta.setBuenas(otPregunta.getBuenas() + 1);
                }
                otPregunta.setTotal(otPregunta.getTotal() + 1);
              } else {
                OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
                otPreguntas.setHabilidad(habilidad);
                if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
                    .toCharArray()[0]) {
                  otPreguntas.setBuenas(1);
                } else {
                  otPreguntas.setBuenas(0);
                }
                otPreguntas.setTotal(1);

                mapa.put(colegioNivel, otPreguntas);
              }
            } else {
              habilidad = controller.findByIdSynchro(R_Habilidad.class, idHabilidad);
              OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
              otPreguntas.setHabilidad(habilidad);
              if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
                  .toCharArray()[0]) {
                otPreguntas.setBuenas(1);
              } else {
                otPreguntas.setBuenas(0);
              }
              otPreguntas.setTotal(1);

              mapaColegios = new HashMap<String, OTPreguntasHabilidad>();
              mapaColegios.put(colegioNivel, otPreguntas);
              mapaHabilidad.put(habilidad, mapaColegios);
            }
          }
        }
      }
    }
  }
  
  
  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof R_EvaluacionEjetematico) {
        llegaEvaluacionEjeTematico = true;
        mEvaluaciones = new HashMap<Long, R_EvaluacionEjetematico>();
        for (Object object : list) {
          R_EvaluacionEjetematico eje = (R_EvaluacionEjetematico) object;
          mEvaluaciones.put(eje.getId(), eje);
        }
      }
      if (entity instanceof R_TipoAlumno) {
        ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        llegaTipoAlumno = true;
        for (Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);
        cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
      }
      if (entity instanceof R_TipoColegio) {
        ObservableList<R_TipoColegio> tColegioList = FXCollections.observableArrayList();
        llegaTipoColegio = true;
        for (Object iEntity : list) {
          tColegioList.add((R_TipoColegio) iEntity);
        }
        cmbTipoColegio.setItems(tColegioList);
        R_TipoColegio tColegio = new R_TipoColegio.Builder().id(Utils.getLastIndex()).build();
        tColegio.setId(Constants.TIPO_COLEGIO_ALL);
        cmbTipoColegio.getSelectionModel().select(tColegio);
      }
      if (entity instanceof R_EvaluacionPrueba) {
        listaEvaluaciones = new ArrayList<>();
        for (final Object iEntity : list) {
          listaEvaluaciones.add((R_EvaluacionPrueba) iEntity);
        }
      }
      if (entity instanceof R_RespuestasEsperadasPrueba) {
        respuestasEsperadas = new ArrayList<>();
        for (final Object iEntity : list) {
          respuestasEsperadas.add((R_RespuestasEsperadasPrueba) iEntity);
        }
      }
    }
    procesaDatosReporte();
  }
  
  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof R_Prueba) {
      prueba = (R_Prueba) entity;
      llegaOnFound = true;
    }
    if (entity instanceof R_Asignatura) {
      asignatura = (R_Asignatura) entity;
      llegaAsignatura = true;
    }
    procesaDatosReporte();
  }
  private void procesaDatosReporte() {
    if (llegaEvaluacionEjeTematico && llegaTipoAlumno && llegaOnFound && llegaTipoColegio
        && llegaAsignatura ) {

      llenarDatosTabla();
      desplegarDatosHabilidades();
      desplegarDatosEvaluaciones();
    }
  }

  

}
