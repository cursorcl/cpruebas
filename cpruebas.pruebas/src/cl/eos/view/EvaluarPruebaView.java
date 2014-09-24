package cl.eos.view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.view.ots.OTPruebaRendida;

public class EvaluarPruebaView extends AFormView {

	private Prueba prueba;
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
	private TableColumn<OTPruebaRendida, Float> puntajeCol;
	@FXML
	private TableColumn<OTPruebaRendida, String> nivelCol;
	@FXML
	private ComboBox<Colegio> cmbColegios;
	@FXML
	private ComboBox<Curso> cmbCursos;
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
	public void initialize() {

	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof Prueba) {
			prueba = (Prueba) entity;
			txtName.setText(prueba.getName());
			txtAsignatura.setText(prueba.getAsignatura().getName());
		}
	}

	@Override
	public void onDataArrived(List<Object> list) {
		// TODO Auto-generated method stub
		super.onDataArrived(list);
	}

}
