package cl.eos.view;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.util.Callback;
import cl.eos.PruebasActivator;
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
        handlerLeerImagenes();

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
    List<RespuestasEsperadasPrueba> respEsperadas = prueba.getRespuestas();

    int nMax = Math.min(value.length(), respEsperadas.size());
    otRendida.setOmitidas(Math.abs(value.length() - respEsperadas.size()));
    otRendida.setBuenas(0);
    otRendida.setMalas(0);
    for (int n = 0; n < nMax; n++) {
      RespuestasEsperadasPrueba resp = respEsperadas.get(n);
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
    int nroPreguntas = respEsperadas.size();
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
    if (entity instanceof Prueba) {
      tblListadoPruebas.getItems().clear();
      cmbColegios.getSelectionModel().clearSelection();
      cmbProfesor.getSelectionModel().clearSelection();
      cmbCursos.getSelectionModel().clearSelection();
      prueba = (Prueba) entity;
      txtName.setText(prueba.getName());
      txtAsignatura.setText(prueba.getAsignatura().getName());
      List<RespuestasEsperadasPrueba> respuestas = prueba.getRespuestas();
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
            if (evaluacion.getPrueba().equals(prueba) && evaluacion.getColegio().equals(colegio)
                && evaluacion.getCurso().equals(curso)) {
              evalPrueba = evaluacion;
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
          if (curso.getAlumnos() != null && !curso.getAlumnos().isEmpty()) {
            for (Alumno alumno : curso.getAlumnos()) {
              PruebaRendida pRendida = new PruebaRendida();
              pRendida.setAlumno(alumno);
              pRendida.setEvaluacionPrueba(evalPrueba);
              evalPrueba.getPruebasRendidas().add(pRendida);
              oList.add(new OTPruebaRendida(pRendida));
            }
            prueba.getEvaluaciones().add(evalPrueba);
          }
          tblListadoPruebas.setItems(oList);
        } else {
          for (PruebaRendida pRendida : evalPrueba.getPruebasRendidas()) {
            oList.add(new OTPruebaRendida(pRendida));
          }
          tblListadoPruebas.setItems(oList);
        }
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
      String s =
          String.format("%s-%s-%s-%s", evalPrueba.getAsignatura(), evalPrueba.getColegio(),
              evalPrueba.getCurso(), evalPrueba.getFechaLocal().toString());
      evalPrueba.setName(s);
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
  
  
  protected void handlerLeerImagenes() {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter pngExtFilter =
        new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
    fileChooser.getExtensionFilters().add(pngExtFilter);
    FileChooser.ExtensionFilter jpgExtFilter =
        new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
    fileChooser.getExtensionFilters().add(jpgExtFilter);
    fileChooser.setTitle("Seleccione Imégenes Respuesta");
    List<File> files = fileChooser.showOpenMultipleDialog(null);

    TareaProcesaEvaluacionScanner procesador = new TareaProcesaEvaluacionScanner(prueba, cmbCursos.getValue(), files);
  }
}
