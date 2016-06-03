package cl.eos.view;

import java.time.LocalDate;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.models.TipoPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import curso.ResumenAlumnoView;
import curso.ResumenEjesTematicosView;
import curso.ResumenGeneralPMEView;
import curso.ResumenGeneralView;
import curso.ResumenHabilidadesView;
import curso.ResumenRespuestaView;
import curso.ResumenXAlumnoEjeHabilidadView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EvaluacionPruebaView extends AFormView implements EventHandler<ActionEvent> {

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
	private MenuItem mnuResEjeHabxAlumno;
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
	private MenuItem menuResumenPME;
	@FXML
	private MenuItem mnuEjeHabXAlumno;
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
		mnuResumenPME.setOnAction(this);

		menuResumenGeneral.setOnAction(this);
		menuResumenAlumno.setOnAction(this);
		menuRespuestasPregunta.setOnAction(this);
		menuRespuestasHabilidad.setOnAction(this);
		menuRespuestasEje.setOnAction(this);
		menuResumenPME.setOnAction(this);
		mnuEjeHabXAlumno.setOnAction(this);
		mnuResEjeHabxAlumno.setOnAction(this);

		mnuExportarExcel.setOnAction(this);
		menuExportarExcel.setOnAction(this);

		tblListadoPruebas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		nameCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>("name"));
		fechaCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, LocalDate>("fechaLocal"));
		colTipo.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, TipoPrueba>("tipo"));
		cursoCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Curso>("curso"));
		asignaturaCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>("asignatura"));
		formasCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>("formas"));
		profesorCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, String>("profesor"));
		nroPreguntasCol.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>("nroPreguntas"));
		colExigencia.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>("exigencia"));
		accionClicTabla();
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			tblListadoPruebas.getSelectionModel().clearSelection();
			Object entity = list.get(0);
			if (entity instanceof EvaluacionPrueba) {
				ObservableList<EvaluacionPrueba> evaluaciones = FXCollections.observableArrayList();
				for (Object lEntity : list) {
					if (((EvaluacionPrueba) lEntity).getPruebasRendidas().size() > 0) {
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
		} else if (source == mnuRespuestasPregunta || source == menuRespuestasPregunta) {
			handleResumenRespuesta();
		} else if (source == mnuRespuestasHabilidad || source == menuRespuestasHabilidad) {
			handleResumenHabilidad();
		} else if (source == mnuRespuestasEje || source == menuRespuestasEje) {
			handleResumenEje();
		} else if (source == mnuResumenPME || source == menuResumenPME) {
			handlerResumenPME();
		} else if (source == mnuExportarExcel || source == menuExportarExcel) {
			handlerResumenExcel();
		} else if (source == mnuEjeHabXAlumno || source == mnuResEjeHabxAlumno) {
			handlerResumenEjeHabXAlumno();
		}
		tblListadoPruebas.getSelectionModel().clearSelection();
	}

	/**
	 * Despliega HMI para presentar resultados de EjeHabXAlumno:
	 */
	private void handlerResumenEjeHabXAlumno() {
		evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
		if (evaluacionPrueba != null) {
			ResumenXAlumnoEjeHabilidadView resXAlumnoEjeHab = (ResumenXAlumnoEjeHabilidadView) show(
					"/curso/fxml/ResumenXAlumnoEjeHabilidad.fxml");
			controller.findById(EvaluacionPrueba.class, evaluacionPrueba.getId(), resXAlumnoEjeHab);
			controller.findAll(TipoAlumno.class, resXAlumnoEjeHab);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selección registro");
			alert.setHeaderText("Resumen Eje/Habilidad por alumno.");
			alert.setContentText("Debe seleccionar registro a procesar");
			alert.show();
		}
	}

	private void handlerResumenExcel() {
		tblListadoPruebas.setId("Listado de pruebas");
		ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblListadoPruebas);
	}

	private void handleResumenEje() {
		evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
		if (evaluacionPrueba != null) {
			resumeEjeTematico = (ResumenEjesTematicosView) show("/curso/fxml/ResumenEjesTematicos.fxml");
			controller.findById(EvaluacionPrueba.class, evaluacionPrueba.getId(), resumeEjeTematico);
			controller.findAll(TipoAlumno.class, resumeEjeTematico);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selección registro");
			alert.setHeaderText("Resumen Respuestas por Ejes Temáticos");
			alert.setContentText("Debe seleccionar registro a procesar");
			alert.show();
		}

	}

	private void handleResumenHabilidad() {
		evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
		if (evaluacionPrueba != null) {
			resumeHabilidad = (ResumenHabilidadesView) show("/curso/fxml/ResumenHabilidades.fxml");
			controller.findById(EvaluacionPrueba.class, evaluacionPrueba.getId(), resumeHabilidad);
			controller.findAll(TipoAlumno.class, resumeHabilidad);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selección registro");
			alert.setHeaderText("Resumen Respuestas por Habilidades");
			alert.setContentText("Debe seleccionar registro a procesar");
			alert.show();			
		}
	}

	private void handleResumenAlumno() {
		evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
		if (evaluacionPrueba != null) {
			resumenAlumno = (ResumenAlumnoView) show("/curso/fxml/ResumenAlumno.fxml");

			controller.findById(EvaluacionPrueba.class, evaluacionPrueba.getId(), resumenAlumno);
			controller.findAll(TipoAlumno.class, resumenAlumno);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selección registro");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar registro a procesar");
			alert.show();	
		}
	}

	private void handleResumenGeneral() {
		evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
		if (evaluacionPrueba != null) {
			resumenGeneral = (ResumenGeneralView) show("/curso/fxml/ResumenGeneral.fxml");
			controller.findById(EvaluacionPrueba.class, evaluacionPrueba.getId(), resumenGeneral);
			controller.findAll(TipoAlumno.class, resumenGeneral);
		} else {
						
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selección registro");
			alert.setHeaderText("Resumen de respuestas generales");
			alert.setContentText("Debe seleccionar registro a procesar");
			alert.show();	
		}
	}

	private void handleResumenRespuesta() {
		evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
		if (evaluacionPrueba != null) {
			if (resumenRespuestas == null) {
				resumenRespuestas = (ResumenRespuestaView) show("/curso/fxml/ResumenRespuestas.fxml");
			} else {
				show(resumenRespuestas);
			}
			controller.findById(EvaluacionPrueba.class, evaluacionPrueba.getId(), resumenRespuestas);
			controller.findAll(TipoAlumno.class, resumenRespuestas);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selección registro");
			alert.setHeaderText("Resumen de respuestas por pregunta");
			alert.setContentText("Debe seleccionar registro a procesar");
			alert.show();
		}
	}

	private void handlerResumenPME() {
		EvaluacionPrueba evaluacion = tblListadoPruebas.getSelectionModel().getSelectedItem();
		if (evaluacion != null) {
			if (resumenGeneralPME == null) {
				resumenGeneralPME = (ResumenGeneralPMEView) show("/curso/fxml/ResumenGeneralPME.fxml");
			} else {
				show(resumenGeneralPME);
			}
			controller.findById(EvaluacionPrueba.class, evaluacion.getId(), resumenGeneralPME);
			controller.findAll(RangoEvaluacion.class);
			controller.findAll(TipoAlumno.class);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selección registro");
			alert.setHeaderText("Resumen general P.M.E.");
			alert.setContentText("Debe seleccionar registro a procesar");
			alert.show();
		}
	}

	private void accionClicTabla() {
		tblListadoPruebas.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<EvaluacionPrueba>() {

					@Override
					public void changed(ObservableValue<? extends EvaluacionPrueba> arg0, EvaluacionPrueba arg1,
							EvaluacionPrueba arg2) {
						ObservableList<EvaluacionPrueba> itemsSelec = tblListadoPruebas.getSelectionModel()
								.getSelectedItems();
					}
				});
	}

}
