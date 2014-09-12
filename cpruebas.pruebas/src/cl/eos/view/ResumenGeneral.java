package cl.eos.view;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Prueba;

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
	private TableView<Prueba> tblResumen;
	@FXML
	private TableColumn<Prueba, Integer> colNotas;
	@FXML
	private TableColumn<Prueba, Integer> colBuenas;
	@FXML
	private TableColumn<Prueba, Integer> ColPuntos;
	@FXML
	private TableColumn<Prueba, Integer> colPuntaje;

	@FXML
	private TableView<Alumno> tblAlumnos;
	@FXML
	private TableColumn<Alumno, String> colRut;
	@FXML
	private TableColumn<Alumno, String> colPaterno;
	@FXML
	private TableColumn<Alumno, String> colMaterno;
	@FXML
	private TableColumn<Alumno, String> colName;
	@FXML
	private TableColumn<Alumno, String> colCurso;

	public ResumenGeneral() {
		// TODO Auto-generated constructor stub
	}
	
	private void inicializarTabla(){
		inicializarTablaResumen();
		inicializarTablaAlumnos();
	}

	private void inicializarTablaAlumnos() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRut.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
				"rut"));
		colName.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
				"name"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
						"paterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
						"materno"));
		colCurso.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
				"curso"));
		
	}

	private void inicializarTablaResumen() {
		tblResumen.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNotas.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>(
				"fechaLocal"));
		colBuenas.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>(
				"buenas"));
		ColPuntos
				.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>(
						"asignatura"));
		colPuntaje
				.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>(
						"profesor"));
	}

}
