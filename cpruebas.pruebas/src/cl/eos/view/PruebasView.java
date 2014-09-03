package cl.eos.view;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPrueba;

public class PruebasView extends AFormView {

	@FXML
	private TableView<OTPrueba> tblListadoPruebas;
	@FXML
	private TableColumn<OTPrueba, Date> fechaCol;
	@FXML
	private TableColumn<OTPrueba, String> cursoCol;
	@FXML
	private TableColumn<OTPrueba, String> nameCol;
	@FXML
	private TableColumn<OTPrueba, String> asignaturaCol;
	@FXML
	private TableColumn<OTPrueba, String> profesorCol;
	@FXML
	private TableColumn<OTPrueba, Integer> nroPreguntasCol;
	@FXML
	private TableColumn<OTPrueba, Integer> formasCol;
	@FXML
	private TableColumn<OTPrueba, Integer> alternativasCol;

	public PruebasView() {
	}

	@FXML
	public void initialize() {
		tblListadoPruebas.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		
		nameCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>(
				"name"));
		fechaCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, Date>(
				"fecha"));
		cursoCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>(
				"aPaterno"));
		asignaturaCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>(
						"aMaterno"));
		profesorCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>(
						"curso"));
		nroPreguntasCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, Integer>(
						"nroPreguntas"));
		formasCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, Integer>(
						"formas"));
		alternativasCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, Integer>(
						"alternativas"));
//		value = FXCollections
//				.observableArrayList(new OTAlumno("12.623.503-8", "Susan",
//						"Farías", "Zavala", "AA"), new OTAlumno("12.623.502-k",
//						"Ursula", "Farías", "Zavala", "AA"), new OTAlumno(
//						"12.623.503-9", "Ursula", "Farías", "Zavala", "AA"));
//		tblListadoPruebas.setItems(value);
	}

	@Override
	public void onDataArrived(List<IEntity> list) {
		super.onDataArrived(list);

	}

}
