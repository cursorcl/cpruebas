package cl.eos.view;

import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.persistence.models.PruebaRendida;

public class ResumenGeneral {
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
	private TableView<PruebaRendida> tblResumen;
	@FXML
	private TableColumn<PruebaRendida, Integer> colNotas;
	@FXML
	private TableColumn<PruebaRendida, Integer> colBuenas;
	@FXML
	private TableColumn<PruebaRendida, Integer> ColPuntos;
	@FXML
	private TableColumn<PruebaRendida, Integer> colPuntaje;

	@FXML
	private TableView<PruebaRendida> tblAlumnos;
	@FXML
	private TableColumn<PruebaRendida, String> colRut;
	@FXML
	private TableColumn<PruebaRendida, String> colPaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colMaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colName;
	@FXML
	private TableColumn<PruebaRendida, String> colCurso;
	@FXML
	private TableColumn<PruebaRendida, String> colABuenas;
	@FXML
	private TableColumn<PruebaRendida, String> colAMalas;
	@FXML
	private TableColumn<PruebaRendida, String> colAOmitidas;
	@FXML
	private TableColumn<PruebaRendida, String> colPBuenas;
	@FXML
	private TableColumn<PruebaRendida, String> colAPuntaje;
	@FXML
	private TableColumn<PruebaRendida, String> colPPuntaje;
	@FXML
	private TableColumn<PruebaRendida, String> colANota;

	public ResumenGeneral() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	public void initialize(){
		inicializarTablaResumen();
		inicializarTablaAlumnos();
	}

	private void inicializarTablaAlumnos() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRut.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"rut"));
		colName.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"name"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"paterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"materno"));
		colCurso.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		
		
		colABuenas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		colAMalas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		colAOmitidas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		colPBuenas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		colAPuntaje.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		colPPuntaje.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		colANota.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		
		
		
	}

	private void inicializarTablaResumen() {
		tblResumen.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNotas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
				"fechaLocal"));
		colBuenas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
				"buenas"));
		ColPuntos
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"asignatura"));
		colPuntaje
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"profesor"));
	}

}
