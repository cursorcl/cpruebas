package cl.eos.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.util.ExcelSheetWriterObj;

public class ComparativoComunalHabilidadView extends AFormView implements EventHandler<ActionEvent> {

  private NumberFormat formatter = new DecimalFormat("#0.00");
  @FXML
  private Label lblTitulo;
  @FXML
  private MenuItem mnuExportarHabilidad;
  @FXML
  private MenuItem mnuExportarEvaluacion;
  @FXML
  private TableView tblHabilidades;
  @FXML
  private TableView tblEvaluaciones;

  private HashMap<Habilidad, HashMap<String, OTPreguntasHabilidad>> mapaHabilidad;

  private Map<Long, EvaluacionEjeTematico> mEvaluaciones;

  private Map<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;

  public ComparativoComunalHabilidadView() {

  }

  @FXML
  public void initialize() {
    this.setTitle("Resumen comparativo comunal habilidad");
    inicializarTablaHabilidades();
    inicializarTablaEvaluacion();
    mnuExportarHabilidad.setOnAction(this);
    mnuExportarEvaluacion.setOnAction(this);
  }

  private void inicializarTablaHabilidades() {
    tblHabilidades.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void inicializarTablaEvaluacion() {
    tblEvaluaciones.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private boolean llegaOnFound = false;
  private boolean llegaOnDataArrived = false;
  private ArrayList<String> titulosColumnas;
  private Prueba prueba;

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof Prueba) {
      prueba = (Prueba) entity;
      llegaOnFound = true;
    }
    procesaDatosReporte();
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof EvaluacionEjeTematico) {
        llegaOnDataArrived = true;
        mEvaluaciones = new HashMap<Long, EvaluacionEjeTematico>();
        for (Object object : list) {
          EvaluacionEjeTematico eje = (EvaluacionEjeTematico) object;
          mEvaluaciones.put(eje.getId(), eje);
        }
      }
    }
    procesaDatosReporte();
  }

  private void procesaDatosReporte() {
    if (llegaOnDataArrived && llegaOnFound) {
      llenarDatosTabla();
      desplegarDatosHabilidades();
      desplegarDatosEvaluaciones();
    }
  }

  private void llenarDatosTabla() {

    if (llegaOnFound && llegaOnDataArrived) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(prueba.getAsignatura());
      buffer.append(" ");
      buffer.append(prueba.getCurso());
      lblTitulo.setText(buffer.toString());

      mapaHabilidad = new HashMap<Habilidad, HashMap<String, OTPreguntasHabilidad>>();
      mapEvaAlumnos = new HashMap<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>>();
      HashMap<String, OTPreguntasHabilidad> mapaColegios = null;

      List<EvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();

      creacionColumnasEjesTematicos(listaEvaluaciones);
      creacionColumnasEvaluaciones(listaEvaluaciones);

      // ********** generar datos ejes tematicos y evaluaciones

      for (EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
        String colegioCurso = evaluacionPrueba.getColegiocurso();

        List<PruebaRendida> pruebasRendidas = evaluacionPrueba.getPruebasRendidas();
        List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba.getRespuestas();

        for (PruebaRendida pruebaRendida : pruebasRendidas) {
          generaDatosEvaluacion(pruebaRendida, colegioCurso);

          String respuesta = pruebaRendida.getRespuestas().toUpperCase();
          char[] cRespuesta = respuesta.toUpperCase().toCharArray();

          for (RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {

            Habilidad habilidad = respuestasEsperadasPrueba.getHabilidad();
            Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
            if (mapaHabilidad.containsKey(habilidad)) {
              HashMap<String, OTPreguntasHabilidad> mapa = mapaHabilidad.get(habilidad);

              if (mapa.containsKey(colegioCurso)) {
                OTPreguntasHabilidad otPregunta = mapa.get(colegioCurso);

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

                mapa.put(colegioCurso, otPreguntas);
              }
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

              mapaColegios = new HashMap<String, OTPreguntasHabilidad>();
              mapaColegios.put(colegioCurso, otPreguntas);
              mapaHabilidad.put(habilidad, mapaColegios);
            }
          }
        }
      }
    }
  }

  private void generaDatosEvaluacion(PruebaRendida pruebaRendida, String colegioCurso) {

    HashMap<String, OTPreguntasEvaluacion> mapaOT = new HashMap<String, OTPreguntasEvaluacion>();

    Float pBuenas = pruebaRendida.getPbuenas();
    for (Entry<Long, EvaluacionEjeTematico> mEvaluacion : mEvaluaciones.entrySet()) {

      EvaluacionEjeTematico evaluacionAl = mEvaluacion.getValue();
      if (mapEvaAlumnos.containsKey(evaluacionAl)) {
        HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
        if (evaluacion.containsKey(colegioCurso)) {
          OTPreguntasEvaluacion otPreguntas = evaluacion.get(colegioCurso);

          if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
            otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
          }
        } else {

          OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
          if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
            pregunta.setAlumnos(1);
          } else {
            pregunta.setAlumnos(0);
          }
          pregunta.setEvaluacion(evaluacionAl);
          evaluacion.put(colegioCurso, pregunta);
        }
      } else {
        OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
        if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
          pregunta.setAlumnos(1);
        } else {
          pregunta.setAlumnos(0);
        }
        pregunta.setEvaluacion(evaluacionAl);

        mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
        mapaOT.put(colegioCurso, pregunta);
        mapEvaAlumnos.put(evaluacionAl, mapaOT);
      }
    }

  }

  private void desplegarDatosHabilidades() {
    ObservableList<ObservableList> registros = FXCollections.observableArrayList();

    for (Entry<Habilidad, HashMap<String, OTPreguntasHabilidad>> mapa : mapaHabilidad.entrySet()) {

      ObservableList<String> row = FXCollections.observableArrayList();

      row.add(((Habilidad) mapa.getKey()).getName());

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

    ObservableList<ObservableList> registroseEva = FXCollections.observableArrayList();
    ObservableList<String> row = null;
    int total = 0;
    for (Entry<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapa : mapEvaAlumnos
        .entrySet()) {
      row = FXCollections.observableArrayList();
      row.add(((EvaluacionEjeTematico) mapa.getKey()).getName());
      HashMap<String, OTPreguntasEvaluacion> resultados = mapa.getValue();

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
      row.add(String.valueOf(valor));
    }
    registroseEva.add(row);

    tblEvaluaciones.setItems(registroseEva);
  }

  private void creacionColumnasEjesTematicos(List<EvaluacionPrueba> pListaEvaluaciones) {
    TableColumn columna0 = new TableColumn("Eje Temático");
    columna0
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {

            return new SimpleStringProperty(param.getValue().get(0).toString());
          }
        });
    columna0.setPrefWidth(100);
    tblHabilidades.getColumns().add(columna0);

    titulosColumnas = new ArrayList<>();
    int indice = 1;
    List<EvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
    for (EvaluacionPrueba evaluacion : listaEvaluaciones) {
      // Columnas
      final int col = indice;
      final String colegioCurso = evaluacion.getColegiocurso();
      titulosColumnas.add(colegioCurso);
      TableColumn columna = new TableColumn(colegioCurso);
      columna
          .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(col).toString());
            }
          });
      columna.setPrefWidth(100);
      tblHabilidades.getColumns().add(columna);
      indice++;
    }
  }

  private void creacionColumnasEvaluaciones(List<EvaluacionPrueba> pListaEvaluaciones) {
    TableColumn columna0 = new TableColumn("");
    columna0
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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
      columna
          .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(col).toString());
            }
          });
      columna.setPrefWidth(100);
      tblEvaluaciones.getColumns().add(columna);
      indice++;
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
    }
  }
}
