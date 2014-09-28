package cl.eos.view;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.detection.ImpresionPrueba;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.view.ots.OTImprimirPrueba;

public class ImprimirPruebaView extends AFormView {

	private Prueba prueba;
	@FXML
	private TableView<OTImprimirPrueba> tblListadoPruebas;
	@FXML
	private TableColumn<OTImprimirPrueba, String> colegioCol;
	@FXML
	private TableColumn<OTImprimirPrueba, String> cursoCol;
	@FXML
	private TableColumn<OTImprimirPrueba, String> profesorCol;
	@FXML
	private TableColumn<OTImprimirPrueba, String> fechaCol;
	@FXML
	private TableColumn<OTImprimirPrueba, Integer> nroAlumnosCol;
	@FXML
	private ComboBox<Colegio> cmbColegios;
	@FXML
	private ComboBox<Curso> cmbCursos;
	@FXML
	private ComboBox<Profesor> cmbProfesor;
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
	private MenuItem mnuAgregar;
	@FXML
	private MenuItem mnuQuitar;
	@FXML
	private MenuItem mnuEliminar;
	@FXML
	private MenuItem mnuImprimir;

	public ImprimirPruebaView() {
		setTitle("Imprimir");
	}

	@FXML
	public void initialize() {
		cmbCursos.setDisable(true);
		dtpFecha.setValue(LocalDate.now());
		cmbColegios.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Colegio colegio = cmbColegios.getSelectionModel()
						.getSelectedItem();
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("tcursoId", prueba.getCurso().getId());
				parameters.put("colegioId", colegio.getId());
				controller.find("Curso.findByTipoColegio", parameters);
			}
		});
		RespondeEventos eventHandler = new RespondeEventos();
		mnuAgregar.setOnAction(eventHandler);
		mnuEliminar.setOnAction(eventHandler);
		mnuImprimir.setOnAction(eventHandler);
		mnuQuitar.setOnAction(eventHandler);

		colegioCol
				.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>(
						"colegio"));
		cursoCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>(
				"curso"));
		profesorCol
				.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>(
						"profesor"));
		fechaCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>(
				"fechaLocal"));
		nroAlumnosCol
				.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, Integer>(
						"nroAlumnos"));
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof Prueba) {
			prueba = (Prueba) entity;
			txtName.setText(prueba.getName());
			txtAsignatura.setText(prueba.getAsignatura().getName());
			List<RespuestasEsperadasPrueba> respuestas = prueba.getRespuestas();
			ObservableList<EjeTematico> lEjes = FXCollections
					.observableArrayList();
			ObservableList<Habilidad> lHabilidad = FXCollections
					.observableArrayList();

			for (RespuestasEsperadasPrueba respuesta : respuestas) {
				if (!lEjes.contains(respuesta.getEjeTematico())) {
					lEjes.add(respuesta.getEjeTematico());
				}
				if (!lHabilidad.contains(respuesta.getHabilidad())) {
					lHabilidad.add(respuesta.getHabilidad());
				}
			}
			lstEjes.setItems(lEjes);
			lstHabilidad.setItems(lHabilidad);
		}

	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Colegio) {
				ObservableList<Colegio> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Colegio) iEntity);
				}
				cmbColegios.setItems(oList);
			} else if (entity instanceof Curso) {
				ObservableList<Curso> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Curso) iEntity);
				}
				cmbCursos.setItems(oList);
				cmbCursos.setDisable(false);
			} else if (entity instanceof Profesor) {
				ObservableList<Profesor> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Profesor) iEntity);
				}
				cmbProfesor.setItems(oList);
				cmbCursos.setDisable(false);
			}

		}
	}

	/**
	 * Clase dedicada a responder los eventos de menu.
	 */
	private class RespondeEventos implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == mnuAgregar) {
				if (validate()) {
					OTImprimirPrueba ot = new OTImprimirPrueba(prueba);
					ot.setColegio(cmbColegios.getValue());
					ot.setCurso(cmbCursos.getValue());
					ot.setFecha(dtpFecha.getValue().toEpochDay());
					ot.setProfesor(cmbProfesor.getValue());
					tblListadoPruebas.getItems().add(ot);
				}
			}
			if (event.getSource() == mnuQuitar
					|| event.getSource() == mnuEliminar) {
				if (tblListadoPruebas.getSelectionModel().getSelectedItems() != null
						&& !tblListadoPruebas.getSelectionModel().isEmpty()) {
					ObservableList<OTImprimirPrueba> selecteds = tblListadoPruebas
							.getSelectionModel().getSelectedItems();
					while (!selecteds.isEmpty()) {
						tblListadoPruebas.getItems().remove(selecteds.get(0));
					}
				}
			}
			if (event.getSource() == mnuImprimir) {
				ImpresionPrueba impresion = new ImpresionPrueba();
				ObservableList<OTImprimirPrueba> selecteds = tblListadoPruebas.getItems();
				for (OTImprimirPrueba ot : selecteds) {
					impresion.imprimir(prueba, ot.getCurso(), ot.getProfesor(), ot.getColegio(), dtpFecha.getValue());
				}
			}
		}

	}
}
