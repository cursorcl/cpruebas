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

import org.controlsfx.dialog.Dialogs;

import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.TipoPrueba;
import cl.eos.util.ExcelSheetWriterObj;

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
	private MenuItem mnuRespuestasHabilidad;
	@FXML
	private MenuItem mnuRespuestasEje;
	@FXML
	private MenuItem menuResumenGeneral;
	@FXML
	private MenuItem menuResumenAlumno;
	@FXML
	private MenuItem menuRespuestasPregunta;
	@FXML
	private MenuItem menuRespuestasHabilidad;
	@FXML
	private MenuItem menuRespuestasEje;
	@FXML
	private MenuItem mnuResumenPME;
	@FXML
	private MenuItem mnuExportarExcel;
	@FXML
	private MenuItem menuExportarExcel;

	private EvaluacionPrueba evaluacionPrueba;
	private ResumenGeneralView resumenGeneral;
	private ResumenAlumnoView resumenAlumno;
	private ResumenRespuestaView resumenRespuestas;
	private ResumenHabilidadesView resumeHabilidad;
	private ResumenEjesTematicosView resumeEjeTematico;
	private ResumenGeneralPMEView resumenGeneralPME;

	public EvaluacionPruebaView() {
		setTitle("Listado de evaluaciones");
	}

	@FXML
	public void initialize() {
		mnuResumenGeneral.setOnAction(this);
		mnuResumenAlumno.setOnAction(this);
		mnuRespuestasPregunta.setOnAction(this);
		mnuRespuestasHabilidad.setOnAction(this);
		mnuRespuestasEje.setOnAction(this);

		menuResumenGeneral.setOnAction(this);
		menuResumenAlumno.setOnAction(this);
		menuRespuestasPregunta.setOnAction(this);
		menuRespuestasHabilidad.setOnAction(this);
		menuRespuestasEje.setOnAction(this);
		mnuResumenPME.setOnAction(this);
		mnuExportarExcel.setOnAction(this);
		menuExportarExcel.setOnAction(this);

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
		colExigencia
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
						"exigencia"));

	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof EvaluacionPrueba) {
				ObservableList<EvaluacionPrueba> evaluaciones = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					if (((EvaluacionPrueba) lEntity).getPruebasRendidas()
							.size() > 0) {
						evaluaciones.add((EvaluacionPrueba) lEntity);
					}
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
		} else if (source == mnuResumenAlumno || source == menuResumenAlumno) {
			handleResumenAlumno();
		} else if (source == mnuRespuestasPregunta
				|| source == menuRespuestasPregunta) {
			handleResumenRespuesta();
		} else if (source == mnuRespuestasHabilidad
				|| source == menuRespuestasHabilidad) {
			handleResumenHabilidad();
		} else if (source == mnuRespuestasEje || source == menuRespuestasEje) {
			handleResumenEje();
		} else if (source == mnuResumenPME) {
			handlerResumenPME();
		} else if (source == mnuExportarExcel || source == menuExportarExcel) {
			handlerResumenExcel();
		}

	}

	private void handlerResumenExcel() {
		tblListadoPruebas.setId("Listado de pruebas");
		ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblListadoPruebas);

	}

	private void handleResumenEje() {
		if (resumeEjeTematico == null) {
			resumeEjeTematico = (ResumenEjesTematicosView) show("/cl/eos/view/ResumenEjesTematicos.fxml");
		} else {
			show(resumeEjeTematico);
		}
		evaluacionPrueba = tblListadoPruebas.getSelectionModel()
				.getSelectedItem();
		if (evaluacionPrueba != null) {
			controller.findById(EvaluacionPrueba.class,
					evaluacionPrueba.getId(), resumeEjeTematico);
		} else {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(resumeEjeTematico.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
		}

	}

	private void handleResumenHabilidad() {
		if (resumeHabilidad == null) {
			resumeHabilidad = (ResumenHabilidadesView) show("/cl/eos/view/ResumenHabilidades.fxml");
		} else {
			show(resumeHabilidad);
		}
		evaluacionPrueba = tblListadoPruebas.getSelectionModel()
				.getSelectedItem();
		if (evaluacionPrueba != null) {
			controller.findById(EvaluacionPrueba.class,
					evaluacionPrueba.getId(), resumeHabilidad);
		} else {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(resumeHabilidad.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
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
					evaluacionPrueba.getId(), resumenAlumno);
		} else {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(resumenAlumno.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
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
					evaluacionPrueba.getId(), resumenGeneral);
		} else {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(resumenGeneral.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
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
					evaluacionPrueba.getId(), resumenRespuestas);
		} else {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(resumenRespuestas.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
		}
	}

	private void handlerResumenPME() {
		if (resumenGeneralPME == null) {
			resumenGeneralPME = (ResumenGeneralPMEView) show("/cl/eos/view/ResumenGeneralPME.fxml");
		} else {
			show(resumenGeneralPME);
		}
		EvaluacionPrueba evaluacion = tblListadoPruebas.getSelectionModel()
				.getSelectedItem();
		if (evaluacion != null) {
			controller.findById(EvaluacionPrueba.class, evaluacion.getId(),
					resumenGeneralPME);
			controller.findAll(RangoEvaluacion.class);
		}
	}
}
