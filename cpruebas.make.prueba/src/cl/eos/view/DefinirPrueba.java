package cl.eos.view;

import java.net.URL;
import java.util.ResourceBundle;

import cl.eos.imp.view.AFormView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import jfxtras.labs.scene.control.BigDecimalField;

public class DefinirPrueba extends AFormView{
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ComboBox<?> cmbHabilidades;
    @FXML
    private ComboBox<?> cmbAdignatura;
    @FXML
    private ComboBox<?> cmbProfesor;
    @FXML
    private TextArea txtPregunta;
    @FXML
    private RadioButton chkOpcionV;
    @FXML
    private BigDecimalField spnExigencia;
    @FXML
    private BigDecimalField spnPjeBase;
    @FXML
    private ComboBox<?> spnNivelEvaluacion;
    @FXML
    private ComboBox<?> cmbObjetivos;
    @FXML
    private RadioButton chkOpcionMental;
    @FXML
    private TextField txtAlternativaE;
    @FXML
    private ImageView img4;
    @FXML
    private ImageView img3;
    @FXML
    private ComboBox<?> cmbEjesTematicos;
    @FXML
    private ComboBox<?> cmbTipoPrueba;
    @FXML
    private TextField txtAlternativaA;
    @FXML
    private BigDecimalField spnNroAlternativas;
    @FXML
    private TextField txtAlternativaB;
    @FXML
    private TextField txtAlternativaC;
    @FXML
    private TextField txtAlternativaD;
    @FXML
    private ListView<?> lstPreguntas;
    @FXML
    private RadioButton chkOpcionA;
    @FXML
    private BigDecimalField spnNroPreguntas;
    @FXML
    private DatePicker fecFeha;
    @FXML
    private RadioButton chkOpcionC;
    @FXML
    private RadioButton chkOpcionB;
    @FXML
    private RadioButton chkOpcionE;
    @FXML
    private RadioButton chkOpcionD;
    @FXML
    private ImageView emg5;
    @FXML
    private ComboBox<?> cmbCurso;
    @FXML
    private RadioButton chkOpcionF;
    @FXML
    private ImageView img2;
    @FXML
    private ImageView img1;
    @FXML
    void initialize() {
        setTitle("Creación de Pruebas");
    }

}
