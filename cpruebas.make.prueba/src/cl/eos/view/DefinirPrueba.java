package cl.eos.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cl.eos.imp.view.AFormView;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.restful.tables.R_TipoPrueba;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.BigDecimalField;

public class DefinirPrueba extends AFormView {

  @FXML
  VBox dataContainer;
  @FXML
  ResourceBundle resources;
  @FXML
  URL location;
  @FXML
  ComboBox<R_Habilidad> cmbHabilidades;
  @FXML
  ComboBox<R_Asignatura> cmbAsignatura;
  @FXML
  ComboBox<R_Profesor> cmbProfesor;
  @FXML
  TextArea txtPregunta;
  @FXML
  BigDecimalField spnExigencia;
  @FXML
  BigDecimalField spnPjeBase;
  @FXML
  ComboBox<R_NivelEvaluacion> cmbNivelEvaluacion;
  @FXML
  ComboBox<R_Objetivo> cmbObjetivos;

  @FXML
  ComboBox<R_Ejetematico> cmbEjesTematicos;
  @FXML
  ComboBox<R_TipoPrueba> cmbTipoPrueba;
  @FXML
  TextField txtNombre;
  @FXML
  TextField txtAlternativaA;
  @FXML
  TextField txtAlternativaB;
  @FXML
  TextField txtAlternativaC;
  @FXML
  TextField txtAlternativaD;
  @FXML
  TextField txtAlternativaE;
  @FXML
  ListView<ItemList> lstPreguntas;
  @FXML
  BigDecimalField spnNroAlternativas;
  @FXML
  BigDecimalField spnNroPreguntas;
  @FXML
  BigDecimalField spnForma;

  @FXML
  DatePicker fecFeha;
  @FXML
  Tooltip objetivoToolTips;

  @FXML
  RadioButton chkOpcionA;
  @FXML
  RadioButton chkOpcionB;
  @FXML
  RadioButton chkOpcionC;
  @FXML
  RadioButton chkOpcionE;
  @FXML
  RadioButton chkOpcionD;

  @FXML
  ComboBox<R_TipoCurso> cmbCurso;
  @FXML
  RadioButton chkOpcionV;
  @FXML
  RadioButton chkOpcionF;
  @FXML
  RadioButton chkOpcionMental;
  @FXML
  MenuItem mnuEliminarPregunta;
  @FXML
  MenuItem mnuGrabar;

  @FXML
  ImageView img1;
  @FXML
  ImageView img2;
  @FXML
  ImageView img3;
  @FXML
  ImageView img4;
  @FXML
  ImageView img5;

  ToggleGroup group;

  R_Prueba prueba;

  public DefinirPrueba() {
    prueba = null;
  }


  @FXML
  void initialize() {
    setTitle("Creación de Pruebas");
    Initializer.initialize(this);

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
    if (DataProcessor.isFinishedDataProcess() && prueba != null) {
      Initializer.setPrueba(prueba, this);
    }
  }


  @Override
  public void onDataArrived(List<Object> list) {
    DataProcessor.process(list, this);
    if (DataProcessor.isFinishedDataProcess() && prueba != null) {
      Initializer.setPrueba(prueba, this);
    }
  }

  /**
   * Se validan todos los datos necesatios para que se pueda grabar.
   */
  @Override
  public boolean validate() {
    return super.validate();
  }



}
