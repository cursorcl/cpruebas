package cl.eos.view;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import org.controlsfx.dialog.Dialogs;

import cl.eos.PruebasActivator;
import cl.eos.detection.ExtractorResultadosPruebas;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.exceptions.CPruebasException;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.WindowManager;
import cl.eos.interfaces.IActivator;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.Utils;
import cl.eos.view.editablecells.EditingCellRespuestasEvaluar;
import cl.eos.view.ots.OTPruebaRendida;


public class EvaluarPruebaView extends AFormView {

  private Prueba prueba;
  private EvaluacionPrueba evalPrueba = null;
  @FXML
  private Label lblError;
  @FXML
  private TableView<OTPruebaRendida> tblListadoPruebas;
  @FXML
  private TableColumn<OTPruebaRendida, String> paternoCol;
  @FXML
  private TableColumn<OTPruebaRendida, String> maternoCol;
  @FXML
  private TableColumn<OTPruebaRendida, String> nombresCol;
  @FXML
  private TableColumn<OTPruebaRendida, String> respuestasCol;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> buenasCol;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> malasCol;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> omitidasCol;
  @FXML
  private TableColumn<OTPruebaRendida, Float> notaCol;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> puntajeCol;
  @FXML
  private TableColumn<OTPruebaRendida, RangoEvaluacion> nivelCol;
  @FXML
  private ComboBox<Colegio> cmbColegios;
  @FXML
  private ComboBox<Curso> cmbCursos;
  @FXML
  private ComboBox<Profesor> cmbProfesor;
  @FXML
  private TextField txtName;
  @FXML
  private TextField txtAsignatura;
  @FXML
  private TextField txtNroPreguntas;
  @FXML
  private TextField txtNroAlternativas;
  @FXML
  private DatePicker dtpFecha;
  @FXML
  private ListView<EjeTematico> lstEjes;
  @FXML
  private ListView<Habilidad> lstHabilidad;
  @FXML
  private MenuItem mnuScanner;
  @FXML
  private MenuItem mnuGrabar;
  @FXML
  private MenuItem mnuVolver;

  @FXML
  private BorderPane mainPane;
  private ArrayList<RespuestasEsperadasPrueba> respuestas;

  public EvaluarPruebaView() {
    setTitle("Evaluar");
  }

  @FXML
  public void initialize() {
    mnuScanner.setDisable(true);
    mnuGrabar.setDisable(true);

    cmbCursos.setDisable(true);
    dtpFecha.setValue(LocalDate.now());
    cmbColegios.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        cmbCursos.getItems().clear();
        Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
        if (colegio != null) {
          Map<String, Object> parameters = new HashMap<String, Object>();
          parameters.put("tcursoId", prueba.getCurso().getId());
          parameters.put("colegioId", colegio.getId());
          controller.find("Curso.findByTipoColegio", parameters);
        }
      }
    });
    cmbCursos.setOnAction(new EHandlerCmbCurso());
    definirTablaListadoPruebas();
    mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        handlerGrabar();
      }
    });
    mnuScanner.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        try {
          handlerLeerImagenes();
        } catch (IOException e) {
          Dialogs.create().owner(null).title("Error lectura Scanner.")
              .masthead("Se ha producido leer un los archivos.")
              .message("Revise las imagenes que se quieren importar").showError();

        }

      }
    });
    mnuVolver.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        IActivator activator = new PruebasActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });
  }

  private void definirTablaListadoPruebas() {
    paternoCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("paterno"));
    maternoCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("materno"));
    nombresCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("nombres"));
    buenasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("buenas"));
    malasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("malas"));
    omitidasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("omitidas"));
    notaCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Float>("nota"));
    puntajeCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("puntaje"));
    nivelCol
        .setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, RangoEvaluacion>("nivel"));
    tblListadoPruebas.setEditable(true);

    respuestasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>(
        "respuestas"));
    respuestasCol.setEditable(true);
    respuestasCol
        .setCellFactory(new Callback<TableColumn<OTPruebaRendida, String>, TableCell<OTPruebaRendida, String>>() {

          @Override
          public TableCell<OTPruebaRendida, String> call(TableColumn<OTPruebaRendida, String> param) {
            return new EditingCellRespuestasEvaluar(prueba);
          }
        });
    respuestasCol
        .setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<OTPruebaRendida, String>>() {
          @Override
          public void handle(CellEditEvent<OTPruebaRendida, String> event) {
            // Aqui debo validar el resultado de la prueba.
            String value = event.getNewValue();
            OTPruebaRendida pRendida = event.getRowValue();
            pRendida.setRespuestas(value);
            if (value != null && !value.isEmpty()) {
              evaluar(value, pRendida);
            }
          }
        });
  }

  protected void evaluar(String value, OTPruebaRendida otRendida) {

    int nroPreguntas = respuestas.size();
    int nMax = Math.min(value.length(), nroPreguntas);
    otRendida.setOmitidas(Math.abs(value.length() - nroPreguntas));
    otRendida.setBuenas(0);
    otRendida.setMalas(0);
    for (int n = 0; n < nMax; n++) {
      RespuestasEsperadasPrueba resp = respuestas.get(n);
      String userResp = value.substring(n, n + 1);
      String validResp = resp.getRespuesta();
      if (userResp.toUpperCase().equals("*")) {
        otRendida.setOmitidas(otRendida.getOmitidas() + 1);
      } else if (userResp.toUpperCase().equals(validResp.toUpperCase())) {
        otRendida.setBuenas(otRendida.getBuenas() + 1);
      } else {
        otRendida.setMalas(otRendida.getMalas() + 1);
      }
    }
    float porcDificultad = prueba.getExigencia() == null ? 60f : prueba.getExigencia();
    float notaMinima = 1.0f;
    otRendida
        .setNota(Utils.getNota(nroPreguntas, porcDificultad, otRendida.getBuenas(), notaMinima));

    float total = otRendida.getBuenas() + otRendida.getMalas() + otRendida.getOmitidas();
    float porcentaje = ((float) otRendida.getBuenas()) / total * 100f;
    RangoEvaluacion rango = prueba.getNivelEvaluacion().getRango(porcentaje);
    otRendida.setNivel(rango);
  }

  @Override
  public void onFound(IEntity entity) {
    txtNroAlternativas.setText("");
    txtNroPreguntas.setText("");
    if (entity instanceof Prueba) {

      tblListadoPruebas.getItems().clear();
      cmbColegios.getSelectionModel().clearSelection();
      cmbProfesor.getSelectionModel().clearSelection();
      cmbCursos.getSelectionModel().clearSelection();
      prueba = (Prueba) entity;
      respuestas = new ArrayList<RespuestasEsperadasPrueba>(prueba.getRespuestas());
      Collections.sort(respuestas, Comparadores.compararRespuestasEsperadas());


      txtName.setText(prueba.getName());
      txtNroAlternativas.setText(prueba.getAlternativas().toString());
      txtNroPreguntas.setText(prueba.getNroPreguntas().toString());
      txtAsignatura.setText(prueba.getAsignatura().getName());
      ObservableList<EjeTematico> lEjes = FXCollections.observableArrayList();
      ObservableList<Habilidad> lHabilidad = FXCollections.observableArrayList();

      for (RespuestasEsperadasPrueba respuesta : respuestas) {
        if (!lEjes.contains(respuesta.getEjeTematico())) {
          lEjes.add(respuesta.getEjeTematico());
        }
        if (!lHabilidad.contains(respuesta.getHabilidad())) {
          lHabilidad.add(respuesta.getHabilidad());
        }
      }
      lstEjes.setItems(lEjes);
      lstHabilidad.setItems(lHabilidad);
    }
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof Colegio) {
        ObservableList<Colegio> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((Colegio) iEntity);
        }
        cmbColegios.setItems(oList);
      } else if (entity instanceof Curso) {
        ObservableList<Curso> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((Curso) iEntity);
        }
        cmbCursos.setItems(oList);
        cmbCursos.setDisable(false);
      } else if (entity instanceof Profesor) {
        ObservableList<Profesor> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((Profesor) iEntity);
        }
        cmbProfesor.setItems(oList);
      }
    }
  }

  private class EHandlerCmbCurso implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
      evalPrueba = null;
      Curso curso = cmbCursos.getSelectionModel().getSelectedItem();
      if (curso != null) {
        Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
        Profesor profesor = cmbProfesor.getSelectionModel().getSelectedItem();
        List<EvaluacionPrueba> listEvaluaciones = prueba.getEvaluaciones();
        if (listEvaluaciones != null && !listEvaluaciones.isEmpty()) {
          for (EvaluacionPrueba evaluacion : listEvaluaciones) {
            if (evaluacion.getColegio().equals(colegio) && evaluacion.getCurso().equals(curso)) {
              evalPrueba = evaluacion;
              evalPrueba.getPruebasRendidas().isEmpty();
              break;
            }
          }
        }
        ObservableList<OTPruebaRendida> oList = FXCollections.observableArrayList();
        if (evalPrueba == null) {
          // Tengo que crear la evaluacion Prueba.
          evalPrueba = new EvaluacionPrueba();
          evalPrueba.setColegio(colegio);
          evalPrueba.setCurso(curso);
          evalPrueba.setPrueba(prueba);
          evalPrueba.setProfesor(profesor);
          evalPrueba.setFecha(dtpFecha.getValue().toEpochDay());
          evalPrueba.setPruebasRendidas(new ArrayList<PruebaRendida>());
        }

        if (curso.getAlumnos() != null && !curso.getAlumnos().isEmpty()) {
          for (Alumno alumno : curso.getAlumnos()) {
            PruebaRendida pRendida = new PruebaRendida();
            pRendida.setAlumno(alumno);
            pRendida.setEvaluacionPrueba(evalPrueba);
            if (evalPrueba.getPruebasRendidas().contains(pRendida)) {
              int idx = evalPrueba.getPruebasRendidas().indexOf(pRendida);
              pRendida = evalPrueba.getPruebasRendidas().get(idx);
            } else {
              evalPrueba.getPruebasRendidas().add(pRendida);
            }
            oList.add(new OTPruebaRendida(pRendida));
          }
        }
        FXCollections.sort(oList, comparaPruebaRendida());
        tblListadoPruebas.setItems(oList);
        mnuGrabar.setDisable(false);
        mnuScanner.setDisable(false);
      } else {
        tblListadoPruebas.getItems().clear();
      }
    }
  }

  protected void handlerGrabar() {
    evalPrueba.setProfesor(cmbProfesor.getSelectionModel().getSelectedItem());
    if (validate()) {
      if (!prueba.getEvaluaciones().contains(evalPrueba)) {
        prueba.getEvaluaciones().add(evalPrueba);
      }
      String s =
          String.format("%s-%s-%s-%s", evalPrueba.getAsignatura(), evalPrueba.getColegio(),
              evalPrueba.getCurso(), evalPrueba.getFechaLocal().toString());
      evalPrueba.setName(s);
      prueba.getFormas().size();
      prueba.getRespuestas().size();
      save(prueba);

      mnuGrabar.setDisable(true);
      mnuScanner.setDisable(true);
      cmbProfesor.getSelectionModel().clearSelection();
      cmbColegios.getSelectionModel().clearSelection();
      cmbCursos.getItems().clear();
      cmbProfesor.requestFocus();
    }
  }

  @Override
  public boolean validate() {
    boolean valid = true;
    if (cmbColegios.getValue() == null) {
      valid = false;
      cmbColegios.getStyleClass().add("bad");
    }
    if (cmbCursos.getValue() == null) {
      valid = false;
      cmbCursos.getStyleClass().add("bad");
    }
    if (cmbProfesor.getValue() == null) {
      valid = false;
      cmbProfesor.getStyleClass().add("bad");
    }
    if (dtpFecha.getValue() == null) {
      valid = false;
      dtpFecha.getStyleClass().add("bad");
    }
    if (valid) {
      lblError.setText(" ");
      removeAllStyles();
    } else {
      lblError.getStyleClass().add("bad");
      lblError.setText("Corregir campos destacados en color rojo");
    }
    return valid;
  }

  private void removeAllStyles() {
    removeAllStyle(lblError);
    removeAllStyle(cmbColegios);
    removeAllStyle(cmbProfesor);
    removeAllStyle(cmbCursos);
    removeAllStyle(dtpFecha);
  }

  protected void handlerLeerImagenes() throws IOException {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter pngExtFilter =
        new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
    fileChooser.getExtensionFilters().add(pngExtFilter);
    FileChooser.ExtensionFilter jpgExtFilter =
        new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
    fileChooser.getExtensionFilters().add(jpgExtFilter);
    fileChooser.setTitle("Seleccione Imégenes Respuesta");
    List<File> files = fileChooser.showOpenMultipleDialog(null);

    // Se limpia la lista de alumnos de la tabla.
    tblListadoPruebas.getItems().clear();

    Task<ObservableList<PruebaRendida>> task = new Task<ObservableList<PruebaRendida>>() {
      @Override
      protected ObservableList<PruebaRendida> call() throws Exception {
        int max = prueba.getNroPreguntas();
        int n = 1;
        ObservableList<PruebaRendida> results = FXCollections.observableArrayList();
        ExtractorResultadosPruebas procesador = ExtractorResultadosPruebas.getInstance();
        for (File archivo : files) {
          OTResultadoScanner resultado = procesador.process(archivo, max);
          PruebaRendida pRendida;
          try {
            pRendida = obtenerPruebaRendida(resultado);
            results.add(pRendida);
            updateProgress(n++, max);
          } catch (CPruebasException e) {

          }
        }
        return results;
      }
    };
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        ObservableList<PruebaRendida> pruebas = task.getValue();
        if (pruebas != null && !pruebas.isEmpty()) {
          for (PruebaRendida pr : pruebas) {
            evalPrueba.getPruebasRendidas().add(pr);
            OTPruebaRendida ot = new OTPruebaRendida(pr);
            tblListadoPruebas.getItems().add(ot);
          }
        }
        if (!prueba.getEvaluaciones().contains(evalPrueba)) {
          prueba.getEvaluaciones().add(evalPrueba);
        }
      }
    });
    Dialogs dlg = Dialogs.create();
    dlg.title("Procesando imágenes");
    dlg.masthead(null);
    dlg.showWorkerProgress(task);
    Executors.newSingleThreadExecutor().execute(task);
  }

  private PruebaRendida obtenerPruebaRendida(OTResultadoScanner resultado) throws CPruebasException {

    Curso curso = cmbCursos.getValue();
    PruebaRendida pRendida = null;

    String rut = resultado.getRut();
    if (rut != null && !rut.isEmpty()) {

      StringBuilder sbRut = (new StringBuilder(rut)).insert(rut.length() - 1, '-');
      rut = sbRut.toString();

      Alumno alumno = obtenerAlumno(rut, curso);
      if (alumno == null) {
        throw new CPruebasException(String.format("El rut: %s no pertenece al curso", rut));
      } else {
        System.out.println("Procesando:" + alumno);
        StringBuilder strResps = new StringBuilder(resultado.getRespuestas());

        int buenas = 0;
        int malas = 0;
        int omitidas = 0;

        for (int n = 0; n < prueba.getNroPreguntas(); n++) {
          String letter = strResps.substring(n, n + 1);
          RespuestasEsperadasPrueba rEsperada = respuestas.get(n);
          if ("O".equalsIgnoreCase(letter)) {
            omitidas++;
          } else if ("M".equalsIgnoreCase(letter)) {
            malas++;
          } else {
            if (rEsperada.getMental()) {
              if ("B".equalsIgnoreCase(letter)) {
                strResps.replace(n, n + 1, "B");
                buenas++;
              } else if ("D".equalsIgnoreCase(letter)) {
                strResps.replace(n, n + 1, "M");
                malas++;
              }
            } else if (rEsperada.getVerdaderoFalso()) {
              if ("V".equalsIgnoreCase(rEsperada.getRespuesta()) && "B".equalsIgnoreCase(letter)) {
                strResps.replace(n, n + 1, "V");
                buenas++;
              } else if ("F".equalsIgnoreCase(rEsperada.getRespuesta())
                  && "D".equalsIgnoreCase(letter)) {
                strResps.replace(n, n + 1, "F");
                buenas++;
              } else {
                malas++;
              }
            } else {
              if (rEsperada.getRespuesta().equalsIgnoreCase(letter)) {
                buenas++;
              } else {
                malas++;
              }
            }
          }
        }

        float nota =
            Utils.getNota(prueba.getNroPreguntas(), prueba.getExigencia(), buenas,
                prueba.getPuntajeBase());
        pRendida = new PruebaRendida();
        pRendida.setAlumno(alumno);
        pRendida.setBuenas(buenas);
        pRendida.setMalas(malas);
        pRendida.setOmitidas(omitidas);
        pRendida.setNota(nota);
        float porcentaje = ((float) pRendida.getBuenas()) / prueba.getNroPreguntas() * 100f;
        RangoEvaluacion rango = prueba.getNivelEvaluacion().getRango(porcentaje);
        pRendida.setRango(rango);

        pRendida.setRespuestas(strResps.toString());

      }

    }
    return pRendida;
  }

  private Alumno obtenerAlumno(String rut, Curso curso) {
    Alumno respuesta = null;
    for (Alumno alumno : curso.getAlumnos()) {
      if (alumno.getRut().equalsIgnoreCase(rut)) {
        respuesta = alumno;
        break;
      }
    }
    return respuesta;
  }

  public static Comparator<? super OTPruebaRendida> comparaPruebaRendida() {
    return new Comparator<OTPruebaRendida>() {

      @Override
      public int compare(OTPruebaRendida o1, OTPruebaRendida o2) {
        StringBuffer sb1 = new StringBuffer();
        sb1.append(o1.getPaterno());
        sb1.append(o1.getMaterno());
        sb1.append(o1.getNombres());
        StringBuffer sb2 = new StringBuffer();
        sb1.append(o2.getPaterno());
        sb1.append(o2.getMaterno());
        sb1.append(o2.getNombres());
        return sb1.toString().compareTo(sb2.toString());
      }
    };
  }
}
