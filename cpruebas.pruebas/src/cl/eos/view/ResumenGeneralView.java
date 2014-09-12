package cl.eos.view;

import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.persistence.models.EvaluacionPrueba;

public class ResumenGeneralView {
	@FXML
	private TextField txtPrueba;
	@FXML
	private TextField txtCurso;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private TextField txtExigencia;
	@FXML
	private TextField txtNoPregunta;
	@FXML
	private TextField txtPjePrueba;

	@FXML
	private TableView<EvaluacionPrueba> tblResumen;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> colNotas;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> colBuenas;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> ColPuntos;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> colPuntaje;

	@FXML
	private TableView<EvaluacionPrueba> tblAlumnos;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colRut;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colPaterno;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colMaterno;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colName;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colCurso;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colABuenas;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colAMalas;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colAOmitidas;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colPBuenas;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colAPuntaje;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colPPuntaje;
	@FXML
	private TableColumn<EvaluacionPrueba, String> colANota;

	public ResumenGeneralView() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	public void initialize(){
		inicializarTablaResumen();
		inicializarTablaAlumnos();
	}

	private void inicializarTablaAlumnos() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRut.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"rut"));
		colName.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"name"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
						"paterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
						"materno"));
		colCurso.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"curso"));
		
		
		colABuenas.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"curso"));
		colAMalas.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"curso"));
		colAOmitidas.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"curso"));
		colPBuenas.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"curso"));
		colAPuntaje.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"curso"));
		colPPuntaje.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"curso"));
		colANota.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"curso"));
		
		
		
	}

	private void inicializarTablaResumen() {
		tblResumen.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNotas.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
				"fechaLocal"));
		colBuenas.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
				"buenas"));
		ColPuntos
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
						"asignatura"));
		colPuntaje
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
						"profesor"));
	}

}
