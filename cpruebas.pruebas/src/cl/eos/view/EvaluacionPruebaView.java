package cl.eos.view;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Prueba;

public class EvaluacionPruebaView extends AFormView {

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

	public EvaluacionPruebaView() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	public void initialize() {
		tblListadoPruebas.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		fechaCol.setCellValueFactory(new PropertyValueFactory<Prueba, LocalDate>(
				"fechaLocal"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>(
				"name"));
		asignaturaCol
				.setCellValueFactory(new PropertyValueFactory<Prueba, String>(
						"asignatura"));
		profesorCol
				.setCellValueFactory(new PropertyValueFactory<Prueba, String>(
						"profesor"));
		formasCol
				.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>(
						"formas"));
		nroPreguntasCol
				.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>(
						"nroPreguntas"));

	}

	@Override
	public void onDataArrived(List<Object> list) {

		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Prueba) {
				ObservableList<Prueba> pruebas = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					pruebas.add((Prueba) lEntity);
				}
				tblListadoPruebas.setItems(pruebas);
			}
		}
	}
}
