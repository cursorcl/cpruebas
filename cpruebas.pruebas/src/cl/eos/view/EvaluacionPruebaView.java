package cl.eos.view;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.TipoPrueba;

public class EvaluacionPruebaView extends AFormView implements
		EventHandler<ActionEvent> {

	@FXML
	private TableView<EvaluacionPrueba> tblListadoPruebas;
	@FXML
	private TableColumn<EvaluacionPrueba, LocalDate> fechaCol;
	@FXML
	private TableColumn<EvaluacionPrueba, Curso> cursoCol;
	@FXML
	private TableColumn<EvaluacionPrueba, String> nameCol;
	@FXML
	private TableColumn<EvaluacionPrueba, TipoPrueba> colTipo;
	@FXML
	private TableColumn<EvaluacionPrueba, String> asignaturaCol;
	@FXML
	private TableColumn<EvaluacionPrueba, String> profesorCol;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> nroPreguntasCol;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> formasCol;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> colExigencia;
	@FXML
	private MenuItem mnuResumenGeneral;
	@FXML
	private MenuItem mnuResumenAlumno;
	@FXML
	private MenuItem mnuRespuestasPregunta;
	
	@FXML
	private MenuItem menuResumenGeneral;
	@FXML
	private MenuItem menuResumenAlumno;
	@FXML
	private MenuItem menuRespuestasPregunta;

	private ResumenGeneralView resumenGeneral;
	private EvaluacionPrueba evaluacionPrueba;
	private ResumenAlumnoView resumenAlumno;
	private ResumenRespuestaView resumenRespuestas;

	public EvaluacionPruebaView() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	public void initialize() {
		mnuResumenGeneral.setOnAction(this);
		mnuResumenAlumno.setOnAction(this);
		mnuRespuestasPregunta.setOnAction(this);
		
		menuResumenGeneral.setOnAction(this);
		menuResumenAlumno.setOnAction(this);
		menuRespuestasPregunta.setOnAction(this);

		tblListadoPruebas.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		nameCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
				"name"));
		fechaCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, LocalDate>(
				"fechaLocal"));
		colTipo.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, TipoPrueba>(
				"tipo"));
		cursoCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Curso>(
				"curso"));
		asignaturaCol
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
						"asignatura"));
		formasCol
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
						"formas"));
		profesorCol
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>(
						"profesor"));
		nroPreguntasCol
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
						"nroPreguntas"));
		// colExigencia
		// .setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba,
		// Integer>(
		// "nroPreguntas"));

	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof EvaluacionPrueba) {
				ObservableList<EvaluacionPrueba> evaluaciones = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					evaluaciones.add((EvaluacionPrueba) lEntity);
				}
				tblListadoPruebas.setItems(evaluaciones);
			}
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuResumenGeneral || source == menuResumenGeneral) {
			handleResumenGeneral();
		} else if (source == mnuResumenAlumno||source == menuResumenAlumno) {
			handleResumenAlumno();
		} else if (source == mnuRespuestasPregunta || source == menuRespuestasPregunta) {
			handleResumenRespuesta();
		}
	}

	private void handleResumenAlumno() {
		if (resumenAlumno == null) {
			resumenAlumno = (ResumenAlumnoView) show("/cl/eos/view/ResumenAlumno.fxml");
		} else {
			show(resumenAlumno);
		}
		evaluacionPrueba = tblListadoPruebas.getSelectionModel()
				.getSelectedItem();
		if (evaluacionPrueba != null) {
			controller.findById(EvaluacionPrueba.class,
					evaluacionPrueba.getId());
		}
	}

	private void handleResumenGeneral() {
		if (resumenGeneral == null) {
			resumenGeneral = (ResumenGeneralView) show("/cl/eos/view/ResumenGeneral.fxml");
		} else {
			show(resumenGeneral);
		}
		evaluacionPrueba = tblListadoPruebas.getSelectionModel()
				.getSelectedItem();
		if (evaluacionPrueba != null) {
			controller.findById(EvaluacionPrueba.class,
					evaluacionPrueba.getId());
		}

	}

	private void handleResumenRespuesta() {
		if (resumenRespuestas == null) {
			resumenRespuestas = (ResumenRespuestaView) show("/cl/eos/view/ResumenRespuestas.fxml");
		} else {
			show(resumenRespuestas);
		}
		evaluacionPrueba = tblListadoPruebas.getSelectionModel()
				.getSelectedItem();
		if (evaluacionPrueba != null) {
			controller.findById(EvaluacionPrueba.class,
					evaluacionPrueba.getId());
		}
	}
}
