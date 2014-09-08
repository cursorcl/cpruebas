package cl.eos.view;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.TipoPrueba;

public class PruebasView extends AFormView {

	@FXML
	private TableView<Prueba> tblListadoPruebas;
	@FXML
	private TableColumn<Prueba, Date> fechaCol;
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
	private ComboBox<TipoPrueba> cmbTipoPrueba;
	@FXML
	private ComboBox<Profesor> cmbProfesor;
	@FXML
	private ComboBox<Curso> cmbCurso;
	@FXML
	private ComboBox<Asignatura> cmbAsignatura;



	public PruebasView() {
	}

	@FXML
	public void initialize() {
		tblListadoPruebas.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		fechaCol.setCellValueFactory(new PropertyValueFactory<Prueba, Date>(
				"fecha"));
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
	public void onDataArrived(List<IEntity> list) {

		if (list != null && !list.isEmpty()) {
			IEntity entity = list.get(0);
			if (entity instanceof Prueba) {
				ObservableList<Prueba> pruebas = FXCollections
						.observableArrayList();
				for (IEntity lEntity : list) {
					pruebas.add((Prueba) lEntity);
				}
				tblListadoPruebas.setItems(pruebas);
			}
			if(entity instanceof TipoPrueba)
			{
				ObservableList<TipoPrueba> tipoPruebas = FXCollections
						.observableArrayList();
				for (IEntity lEntity : list) {
					tipoPruebas.add((TipoPrueba) lEntity);
				}
				cmbTipoPrueba.setItems(tipoPruebas);
			}
			if(entity instanceof Profesor)
			{
				
			}
			if(entity instanceof Curso)
			{
				
			}
		}

	}

}
