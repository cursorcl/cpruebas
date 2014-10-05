package cl.eos.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jfxtras.labs.scene.control.BigDecimalField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.Prueba.Estado;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.persistence.models.TipoPrueba;
import cl.eos.view.editablecells.PruebaCellFactory;
// github.com/cursorcl/cpruebas
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.Habilidad;

public class PruebasView extends AFormView implements EventHandler<ActionEvent> {

  @FXML
  private StackPane pnlRoot;
  @FXML
  private AnchorPane pnlEdition;
  @FXML
  private TableView<Prueba> tblListadoPruebas;
  @FXML
  private TableColumn<Prueba, LocalDate> fechaCol;
  @FXML
  private TableColumn<Prueba, String> cursoCol;
  @FXML
  private TableColumn<Prueba, String> nameCol;
  @FXML
  private TableColumn<Prueba, String> asignaturaCol;
  @FXML
  private TableColumn<Prueba, String> profesorCol;
  @FXML
  private TableColumn<Prueba, Integer> nroPreguntasCol;
  @FXML
  private TableColumn<Prueba, Integer> formasCol;
  @FXML
  private TableColumn<Prueba, Integer> alternativasCol;
  @FXML
  private TableColumn<Prueba, Estado> estadoCol;
  @FXML
  private ComboBox<TipoPrueba> cmbTipoPrueba;
  @FXML
  private ComboBox<Profesor> cmbProfesor;
  @FXML
  private ComboBox<TipoCurso> cmbCurso;
  @FXML
  private ComboBox<Asignatura> cmbAsignatura;
  @FXML
  private BigDecimalField bigDecimalForma;
  @FXML
  private BigDecimalField bigDecimaNroAlternativas;
  @FXML
  private BigDecimalField bigDecimalNroPreguntas;
  @FXML
  private BigDecimalField bigDecimalPuntajePregunta;
  @FXML
  private BigDecimalField bigDecimalExigencia;
  @FXML
  private ComboBox<NivelEvaluacion> cmbNivelEvaluacion;
  @FXML
  private Label lblError;
  @FXML
  private DatePicker dpFecha;
  @FXML
  private TextField txtName;
  @FXML
  private MenuItem mnuGrabar;
  @FXML
  private MenuItem mnuModificar;
  @FXML
  private MenuItem mnuEliminar;
  @FXML
  private MenuItem mnuPopupModificar;
  @FXML
  private MenuItem mnuPopupEliminar;
  @FXML
  private MenuItem mnuEvaluarPrueba;
  @FXML
  private MenuItem mnuDefinirPrueba;
  @FXML
  private MenuItem mnuListaEvaluaciones;
  @FXML
  private MenuItem mnuComparativoComunal;
  @FXML
  private MenuItem mnuComparativoComunalHab;

  @FXML
  private MenuItem mnuComunalEje;

  @FXML
  private MenuItem mnuNueva;
  @FXML
  private MenuItem mnuImprimirPrueba;

  private EvaluacionPruebaView evaluacionPrueba;
  private DefinePruebaViewController definePrueba;
  private Prueba prueba;
  private ComparativoComunalEjeView comparativoComunal;
  private ComparativoComunalHabilidadView comparativoComunalHabilidad;
  private ComunalCursoView comunalEje;

  private EvaluarPruebaView evaluarPruebaView;
  private ImprimirPruebaView imprimirPrueba;

  public PruebasView() {
    setTitle("Pruebas");
  }

  @FXML
  public void initialize() {
    lblError.setText(" ");
    tblListadoPruebas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    fechaCol.setCellValueFactory(new PropertyValueFactory<Prueba, LocalDate>("fechaLocal"));
    nameCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>("name"));
    asignaturaCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>("asignatura"));
    profesorCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>("profesor"));

    cursoCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>("curso"));
    nroPreguntasCol.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>("nroPreguntas"));

    estadoCol.setCellValueFactory(new PropertyValueFactory<Prueba, Estado>("estado"));

    estadoCol.setCellFactory(new PruebaCellFactory());

    bigDecimalForma.setMinValue(new BigDecimal(1));
    bigDecimalForma.setMaxValue(new BigDecimal(5));
    bigDecimalForma.setStepwidth(new BigDecimal(1));
    bigDecimalForma.setNumber(new BigDecimal(1));

    bigDecimaNroAlternativas.setMinValue(new BigDecimal(3));
    bigDecimaNroAlternativas.setMaxValue(new BigDecimal(5));
    bigDecimaNroAlternativas.setStepwidth(new BigDecimal(1));
    bigDecimaNroAlternativas.setNumber(new BigDecimal(3));

    bigDecimalNroPreguntas.setMinValue(new BigDecimal(5));
    bigDecimalNroPreguntas.setMaxValue(new BigDecimal(90));
    bigDecimalNroPreguntas.setStepwidth(new BigDecimal(5));
    bigDecimalNroPreguntas.setNumber(new BigDecimal(5));

    bigDecimalPuntajePregunta.setMinValue(new BigDecimal(1));
    bigDecimalPuntajePregunta.setMaxValue(new BigDecimal(3));
    bigDecimalPuntajePregunta.setStepwidth(new BigDecimal(1));
    bigDecimalPuntajePregunta.setNumber(new BigDecimal(1));

    bigDecimalExigencia.setMinValue(new BigDecimal(40));
    bigDecimalExigencia.setMaxValue(new BigDecimal(60));
    bigDecimalExigencia.setStepwidth(new BigDecimal(10));
    bigDecimalExigencia.setNumber(new BigDecimal(60));

    dpFecha.setValue(LocalDate.now());
    mnuGrabar.setOnAction(this);
    mnuModificar.setOnAction(this);
    mnuPopupModificar.setOnAction(this);
    mnuEliminar.setOnAction(this);
    mnuPopupEliminar.setOnAction(this);
    mnuEvaluarPrueba.setOnAction(this);
    mnuDefinirPrueba.setOnAction(this);
    mnuListaEvaluaciones.setOnAction(this);
    mnuComparativoComunal.setOnAction(this);
    mnuComparativoComunalHab.setOnAction(this);
    mnuComunalEje.setOnAction(this);


    mnuImprimirPrueba.setOnAction(this);
    mnuNueva.setOnAction(this);
  }

  @Override
  public void onDataArrived(List<Object> list) {

    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof Prueba) {
        ObservableList<Prueba> pruebas = FXCollections.observableArrayList();
        for (Object lEntity : list) {
          pruebas.add((Prueba) lEntity);
        }
        tblListadoPruebas.setItems(pruebas);
      }
      if (entity instanceof TipoPrueba) {
        ObservableList<TipoPrueba> tipoPruebas = FXCollections.observableArrayList();
        for (Object lEntity : list) {
          tipoPruebas.add((TipoPrueba) lEntity);
        }
        cmbTipoPrueba.setItems(tipoPruebas);
      }
      if (entity instanceof Profesor) {
        ObservableList<Profesor> profesores = FXCollections.observableArrayList();
        for (Object lEntity : list) {
          profesores.add((Profesor) lEntity);
        }
        cmbProfesor.setItems(profesores);
      }
      if (entity instanceof TipoCurso) {
        ObservableList<TipoCurso> cursos = FXCollections.observableArrayList();
        for (Object lEntity : list) {
          cursos.add((TipoCurso) lEntity);
        }
        cmbCurso.setItems(cursos);
      }
      if (entity instanceof Asignatura) {
        ObservableList<Asignatura> asignaturas = FXCollections.observableArrayList();
        for (Object lEntity : list) {
          asignaturas.add((Asignatura) lEntity);
        }
        cmbAsignatura.setItems(asignaturas);
      }
      if (entity instanceof NivelEvaluacion) {
        ObservableList<NivelEvaluacion> nivelEvaluacion = FXCollections.observableArrayList();
        for (Object lEntity : list) {
          nivelEvaluacion.add((NivelEvaluacion) lEntity);
        }
        cmbNivelEvaluacion.setItems(nivelEvaluacion);
      }
    }
  }

  @Override
  public void onSaved(IEntity otObject) {
    if (otObject instanceof Prueba) {
      int indice = tblListadoPruebas.getItems().lastIndexOf(otObject);
      if (indice != -1) {
        tblListadoPruebas.getItems().remove(otObject);
        tblListadoPruebas.getItems().add(indice, (Prueba) otObject);
      } else {
        tblListadoPruebas.getItems().add((Prueba) otObject);
      }
      limpiarCampos();
      prueba = null;
    }
  }

  @Override
  public boolean validate() {
    boolean valid = true;
    if (cmbTipoPrueba.getValue() == null) {
      valid = false;
      cmbTipoPrueba.getStyleClass().add("bad");
    }
    if (cmbProfesor.getValue() == null) {
      valid = false;
      cmbProfesor.getStyleClass().add("bad");
    }
    if (cmbCurso.getValue() == null) {
      valid = false;
      cmbCurso.getStyleClass().add("bad");
    }
    if (cmbAsignatura.getValue() == null) {
      valid = false;
      cmbAsignatura.getStyleClass().add("bad");
    }
    if (cmbNivelEvaluacion.getValue() == null) {
      valid = false;
      cmbNivelEvaluacion.getStyleClass().add("bad");
    }
    if (txtName.getText() == null || txtName.getText().isEmpty()) {
      valid = false;
      txtName.getStyleClass().add("bad");
    }
    if (bigDecimalForma.getNumber() == null) {
      valid = false;
      bigDecimalForma.getStyleClass().add("bad");
    }
    if (bigDecimaNroAlternativas.getNumber() == null) {
      valid = false;
      bigDecimaNroAlternativas.getStyleClass().add("bad");
    }
    if (bigDecimalNroPreguntas.getNumber() == null) {
      valid = false;
      bigDecimalNroPreguntas.getStyleClass().add("bad");
    }
    if (bigDecimalPuntajePregunta.getNumber() == null) {
      valid = false;
      bigDecimalPuntajePregunta.getStyleClass().add("bad");
    }
    if (bigDecimalExigencia.getNumber() == null) {
      valid = false;
      bigDecimalExigencia.getStyleClass().add("bad");
    }
    if (dpFecha.getValue() == null) {
      valid = false;
      dpFecha.getStyleClass().add("bad");
    }
    if (valid) {
      lblError.setText(" ");
    } else {
      lblError.getStyleClass().add("bad");
      lblError.setText("Corregir campos destacados en color rojo");
    }
    return valid;
  }

  private void removeAllStyles() {
    removeAllStyle(lblError);
    removeAllStyle(cmbTipoPrueba);
    removeAllStyle(cmbProfesor);
    removeAllStyle(cmbCurso);
    removeAllStyle(cmbAsignatura);
    removeAllStyle(cmbNivelEvaluacion);
    removeAllStyle(txtName);
    removeAllStyle(bigDecimalForma);
    removeAllStyle(bigDecimaNroAlternativas);
    removeAllStyle(bigDecimalNroPreguntas);
    removeAllStyle(bigDecimalPuntajePregunta);
    removeAllStyle(dpFecha);
    removeAllStyle(bigDecimalExigencia);
  }

  @Override
  public void handle(ActionEvent event) {
    Object source = event.getSource();
    if (source == mnuModificar || source == mnuPopupModificar) {
      handleModificar();
    } else if (source == mnuGrabar) {
      handleGrabar();
    } else if (source == mnuEliminar || source == mnuPopupEliminar) {
      handleEliminar();
    } else if (source == mnuEvaluarPrueba) {
      handlerEvaluar();
    } else if (source == mnuDefinirPrueba) {
      handlerDefinirPrueba();
    } else if (source == mnuListaEvaluaciones) {
      handlerListaEvaluaciones();
    } else if (source == mnuComparativoComunal) {
      handlerComparativoComunal();
    } else if (source == mnuComparativoComunalHab) {
      handlerComparativoComunalHab();
    } else if (source == mnuComunalEje) {
      handlerComunalEje();
    } else if (source == mnuImprimirPrueba) {
      handlerImrpimirPrueba();
    } else if (source == mnuNueva) {
      handlerNuevaPrueba();
    }
  }


  private void handlerComunalEje() {
    if (comunalEje == null) {
      comunalEje = (ComunalCursoView) show("/cl/eos/view/ComunalEje.fxml", true);
    } else {
      show(comunalEje, true);
    }
    ObservableList<Prueba> prueba = tblListadoPruebas.getSelectionModel().getSelectedItems();
    if (prueba != null) {
      controller.findByAllId(Prueba.class, prueba.toArray());
      controller.findAll(EvaluacionEjeTematico.class);
      // controller.findAll(Curso.class);
    }
  }

  private void handlerComparativoComunal() {
    if (comparativoComunal == null) {
      comparativoComunal =
          (ComparativoComunalEjeView) show("/cl/eos/view/ComparativoComunalEje.fxml", true);
    } else {
      show(comparativoComunal, true);
    }
    Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
    if (prueba != null) {
      controller.findById(Prueba.class, prueba.getId());
      controller.findAll(EvaluacionEjeTematico.class);
    }
  }

  private void handlerNuevaPrueba() {
    removeAllStyles();
    limpiarCampos();
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        txtName.requestFocus();
      }
    });
  }

  private void handlerImrpimirPrueba() {
    if (imprimirPrueba == null) {
      imprimirPrueba = (ImprimirPruebaView) show("/cl/eos/view/ImprimirPrueba.fxml", true);
    } else {
      show(imprimirPrueba, true);
    }
    Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
    if (prueba != null) {
      controller.findById(Prueba.class, prueba.getId());
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("idAsignatura", prueba.getAsignatura().getId());
      controller.find("EjeTematico.findByAsigntura", parameters);
      controller.findAll(Habilidad.class);
      controller.findAll(Profesor.class);
      controller.findAll(Colegio.class);
    }
  }

  private void handlerComparativoComunalHab() {
    if (comparativoComunalHabilidad == null) {
      comparativoComunalHabilidad =
          (ComparativoComunalHabilidadView) show("/cl/eos/view/ComparativoComunalHabilidad.fxml",
              true);
    } else {
      show(comparativoComunalHabilidad, true);
    }
    Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
    if (prueba != null) {
      controller.findById(Prueba.class, prueba.getId());
      controller.findAll(EvaluacionEjeTematico.class);
    }
  }

  private void handlerEvaluar() {
    if (evaluarPruebaView == null) {
      evaluarPruebaView = (EvaluarPruebaView) show("/cl/eos/view/EvaluarPrueba.fxml", true);
    } else {
      show(evaluarPruebaView, true);
    }
    Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
    if (prueba != null) {
      controller.findById(Prueba.class, prueba.getId());
      controller.findAll(Colegio.class);
      controller.findAll(Profesor.class);
    }
  }

  private void handlerDefinirPrueba() {
    if (definePrueba == null) {
      definePrueba = (DefinePruebaViewController) show("/cl/eos/view/DefinePruebaView.fxml", true);
    } else {
      show(definePrueba, true);
    }
    Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
    if (prueba != null) {
      controller.findById(Prueba.class, prueba.getId());
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("idAsignatura", prueba.getAsignatura().getId());
      controller.find("EjeTematico.findByAsigntura", parameters);
      controller.findAll(Habilidad.class);
    }
  }

  private void handlerListaEvaluaciones() {
    if (evaluacionPrueba == null) {
      evaluacionPrueba = (EvaluacionPruebaView) show("/cl/eos/view/EvaluacionPrueba.fxml", true);
    } else {
      show(evaluacionPrueba, true);
    }
    Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("idPrueba", prueba.getId());
    controller.find("EvaluacionPrueba.findByPrueba", parameters);
  }

  private void handleEliminar() {

    ObservableList<Prueba> pruebasSeleccionadas =
        tblListadoPruebas.getSelectionModel().getSelectedItems();
    delete(pruebasSeleccionadas);
    tblListadoPruebas.getSelectionModel().clearSelection();
    limpiarCampos();
  }

  private void handleGrabar() {
    removeAllStyles();
    if (validate()) {
      if (prueba == null) {
        prueba = new Prueba();
      }
      prueba.setAlternativas(bigDecimaNroAlternativas.getNumber().intValue());
      prueba.setAsignatura(cmbAsignatura.getValue());
      prueba.setCurso(cmbCurso.getValue());
      prueba.setFecha(dpFecha.getValue().toEpochDay());
      prueba.setNroFormas(bigDecimalForma.getNumber().intValue());
      prueba.setName(txtName.getText());
      prueba.setNivelEvaluacion(cmbNivelEvaluacion.getValue());
      prueba.setProfesor(cmbProfesor.getValue());
      prueba.setPuntajeBase(bigDecimalPuntajePregunta.getNumber().intValue());
      prueba.setNroPreguntas(bigDecimalNroPreguntas.getNumber().intValue());
      prueba.setAlternativas(bigDecimaNroAlternativas.getNumber().intValue());
      prueba.setTipoPrueba(cmbTipoPrueba.getValue());
      prueba.setExigencia(bigDecimalExigencia.getNumber().intValue());

      save(prueba);
    }
  }

  private void handleModificar() {
    prueba = tblListadoPruebas.getSelectionModel().getSelectedItem();

    if (prueba != null) {
      bigDecimaNroAlternativas.setNumber(new BigDecimal(prueba.getAlternativas()));
      cmbAsignatura.getSelectionModel().select(prueba.getAsignatura());
      cmbCurso.getSelectionModel().select(prueba.getCurso());
      dpFecha.setValue(prueba.getFechaLocal());
      bigDecimalForma.setNumber(new BigDecimal(prueba.getNroFormas()));
      txtName.setText(prueba.getName());
      cmbNivelEvaluacion.getSelectionModel().select(prueba.getNivelEvaluacion());
      cmbProfesor.getSelectionModel().select(prueba.getProfesor());
      bigDecimalPuntajePregunta.setNumber(new BigDecimal(prueba.getPuntajeBase()));
      bigDecimalNroPreguntas.setNumber(new BigDecimal(prueba.getNroPreguntas()));
      cmbTipoPrueba.getSelectionModel().select(prueba.getTipoPrueba());
      prueba.setExigencia(bigDecimalExigencia.getNumber().intValue());
    }
  }

  @Override
  public void onDeleted(IEntity entity) {
    tblListadoPruebas.getItems().remove(entity);
  }

  private void limpiarCampos() {
    cmbTipoPrueba.getSelectionModel().clearSelection();
    cmbProfesor.getSelectionModel().clearSelection();
    cmbCurso.getSelectionModel().clearSelection();
    cmbAsignatura.getSelectionModel().clearSelection();
    cmbNivelEvaluacion.getSelectionModel().clearSelection();
    bigDecimalForma.setNumber(new BigDecimal(1));
    bigDecimaNroAlternativas.setNumber(new BigDecimal(3));
    bigDecimalNroPreguntas.setNumber(new BigDecimal(5));
    bigDecimalPuntajePregunta.setNumber(new BigDecimal(1));
    dpFecha.setValue(LocalDate.now());
    txtName.setText(null);
  }
}
