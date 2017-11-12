package cl.eos.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.persistence.models.TipoPrueba;
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
    ComboBox<Habilidad> cmbHabilidades;
    @FXML
    ComboBox<Asignatura> cmbAsignatura;
    @FXML
    ComboBox<Profesor> cmbProfesor;
    @FXML
    TextArea txtPregunta;
    @FXML
    BigDecimalField spnExigencia;
    @FXML
    BigDecimalField spnPjeBase;
    @FXML
    ComboBox<NivelEvaluacion> cmbNivelEvaluacion;
    @FXML
    ComboBox<Objetivo> cmbObjetivos;

    @FXML
    ComboBox<EjeTematico> cmbEjesTematicos;
    @FXML
    ComboBox<TipoPrueba> cmbTipoPrueba;
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
    ComboBox<TipoCurso> cmbCurso;
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

    Prueba prueba;

    public DefinirPrueba() {
        prueba = null;
    }

    public DefinirPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

    @FXML
    void initialize() {
        setTitle("Creación de Pruebas");
        Initializer.initialize(this);

    }

    
    @Override
    public void onDataArrived(List<Object> list) {
        DataProcessor.process(list, this);
    }

	public Prueba getPrueba() {
		return prueba;
	}

	public void setPrueba(Prueba prueba) {
		this.prueba = prueba;
		
	}

}
