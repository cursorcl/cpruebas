package cl.eos.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jfxtras.labs.scene.control.BigDecimalField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;
// github.com/cursorcl/cpruebas
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.Prueba.Estado;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.persistence.models.TipoPrueba;
import cl.eos.view.editablecells.PruebaCellFactory;
import cl.eos.view.ots.OTPrueba;

public class PruebasView extends AFormView implements EventHandler<ActionEvent> {

	@FXML
	private StackPane pnlRoot;
	@FXML
	private AnchorPane pnlEdition;
	@FXML
	private TableView<OTPrueba> tblListadoPruebas;
	@FXML
	private TableColumn<OTPrueba, LocalDate> fechaCol;
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
	@FXML
	private TableColumn<OTPrueba, Estado> estadoCol;
	@FXML
	private ComboBox<TipoPrueba> cmbTipoPrueba;
	@FXML
	private ComboBox<Profesor> cmbProfesor;
	@FXML
	private ComboBox<TipoCurso> cmbCurso;
	@FXML
	private ComboBox<Asignatura> cmbAsignatura;
	@FXML
	private BigDecimalField bigDecimalForma;
	@FXML
	private BigDecimalField bigDecimaNroAlternativas;
	@FXML
	private BigDecimalField bigDecimalNroPreguntas;
	@FXML
	private BigDecimalField bigDecimalPuntajePregunta;
	@FXML
	private BigDecimalField bigDecimalExigencia;
	@FXML
	private ComboBox<NivelEvaluacion> cmbNivelEvaluacion;
	@FXML
	private Label lblError;
	@FXML
	private DatePicker dpFecha;
	@FXML
	private TextField txtName;
	@FXML
	private TextField filterField;
	@FXML
	private MenuItem mnuGrabar;
	@FXML
	private MenuItem mnuModificar;
	@FXML
	private MenuItem mnuEliminar;
	@FXML
	private MenuItem mnuPopupModificar;
	@FXML
	private MenuItem mnuPopupEliminar;
	@FXML
	private MenuItem mnuEvaluarPrueba;
	@FXML
	private MenuItem mnuDefinirPrueba;
	@FXML
	private MenuItem mnuListaEvaluaciones;
	@FXML
	private MenuItem mnuComparativoComunal;
	@FXML
	private MenuItem mnuComparativoComunalHab;

	@FXML
	private MenuItem mnuComunalEje;

	@FXML
	private MenuItem mnuNueva;
	@FXML
	private MenuItem mnuImprimirPrueba;

	private EvaluacionPruebaView evaluacionPrueba;
	private DefinePruebaViewController definePrueba;
	private Prueba prueba;
	private ComparativoComunalEjeView comparativoComunal;
	private ComparativoComunalHabilidadView comparativoComunalHabilidad;
	private ComunalCursoView comunalEje;

	private EvaluarPruebaView evaluarPruebaView;
	private ImprimirPruebaView imprimirPrueba;

	public PruebasView() {
		setTitle("Pruebas");
	}

	@FXML
	public void initialize() {
		lblError.setText(" ");
		tblListadoPruebas.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		fechaCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, LocalDate>(
				"fechaLocal"));
		nameCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>(
				"name"));
		asignaturaCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>(
						"asignatura"));
		profesorCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>(
						"profesor"));

		cursoCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>(
				"curso"));
		nroPreguntasCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, Integer>(
						"nroPreguntas"));

		estadoCol
				.setCellValueFactory(new PropertyValueFactory<OTPrueba, Estado>(
						"estado"));

		estadoCol.setCellFactory(new PruebaCellFactory());

		bigDecimalForma.setMinValue(new BigDecimal(1));
		bigDecimalForma.setMaxValue(new BigDecimal(5));
		bigDecimalForma.setStepwidth(new BigDecimal(1));
		bigDecimalForma.setNumber(new BigDecimal(1));

		bigDecimaNroAlternativas.setMinValue(new BigDecimal(3));
		bigDecimaNroAlternativas.setMaxValue(new BigDecimal(5));
		bigDecimaNroAlternativas.setStepwidth(new BigDecimal(1));
		bigDecimaNroAlternativas.setNumber(new BigDecimal(3));

		bigDecimalNroPreguntas.setMinValue(new BigDecimal(5));
		bigDecimalNroPreguntas.setMaxValue(new BigDecimal(90));
		bigDecimalNroPreguntas.setStepwidth(new BigDecimal(5));
		bigDecimalNroPreguntas.setNumber(new BigDecimal(5));

		bigDecimalPuntajePregunta.setMinValue(new BigDecimal(1));
		bigDecimalPuntajePregunta.setMaxValue(new BigDecimal(3));
		bigDecimalPuntajePregunta.setStepwidth(new BigDecimal(1));
		bigDecimalPuntajePregunta.setNumber(new BigDecimal(1));

		bigDecimalExigencia.setMinValue(new BigDecimal(40));
		bigDecimalExigencia.setMaxValue(new BigDecimal(60));
		bigDecimalExigencia.setStepwidth(new BigDecimal(10));
		bigDecimalExigencia.setNumber(new BigDecimal(60));

		dpFecha.setValue(LocalDate.now());
		mnuGrabar.setOnAction(this);
		mnuModificar.setOnAction(this);
		mnuPopupModificar.setOnAction(this);
		mnuEliminar.setOnAction(this);
		mnuPopupEliminar.setOnAction(this);
		mnuEvaluarPrueba.setOnAction(this);
		mnuDefinirPrueba.setOnAction(this);
		mnuListaEvaluaciones.setOnAction(this);
		mnuComparativoComunal.setOnAction(this);
		mnuComparativoComunalHab.setOnAction(this);
		mnuComunalEje.setOnAction(this);
		mnuImprimirPrueba.setOnAction(this);
		mnuNueva.setOnAction(this);
	}

	@Override
	public void onDataArrived(List<Object> list) {

		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Prueba) {
				ObservableList<OTPrueba> pruebas = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					pruebas.add(new OTPrueba((Prueba) lEntity));
				}
				 tblListadoPruebas.setItems(pruebas);
//				FilteredList<OTPrueba> filteredItems = new FilteredList<OTPrueba>(
//						pruebas, p -> true);
//				tblListadoPruebas.setItems(filteredItems);
//				filterField.setOnAction(new EventHandler<ActionEvent>() {
//					public void handle(ActionEvent event) {
//						filteredItems.setPredicate(ot ->
//						ot.getName().contains(
//						filterField.getText()));
//					}
//				});

			}
			if (entity instanceof TipoPrueba) {
				ObservableList<TipoPrueba> tipoPruebas = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					tipoPruebas.add((TipoPrueba) lEntity);
				}
				cmbTipoPrueba.setItems(tipoPruebas);
			}
			if (entity instanceof Profesor) {
				ObservableList<Profesor> profesores = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					profesores.add((Profesor) lEntity);
				}
				cmbProfesor.setItems(profesores);
			}
			if (entity instanceof TipoCurso) {
				ObservableList<TipoCurso> cursos = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					cursos.add((TipoCurso) lEntity);
				}
				cmbCurso.setItems(cursos);
			}
			if (entity instanceof Asignatura) {
				ObservableList<Asignatura> asignaturas = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					asignaturas.add((Asignatura) lEntity);
				}
				cmbAsignatura.setItems(asignaturas);
			}
			if (entity instanceof NivelEvaluacion) {
				ObservableList<NivelEvaluacion> nivelEvaluacion = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					nivelEvaluacion.add((NivelEvaluacion) lEntity);
				}
				cmbNivelEvaluacion.setItems(nivelEvaluacion);
			}
		}
	}

	@Override
	public void onSaved(IEntity otObject) {
		if (otObject instanceof Prueba) {
			OTPrueba ot = new OTPrueba((Prueba) otObject);
			int indice = tblListadoPruebas.getItems().lastIndexOf(ot);
			if (indice != -1) {
				tblListadoPruebas.getItems().set(indice, ot);
			} else {
				tblListadoPruebas.getItems().add(ot);
			}
			limpiarCampos();
			prueba = null;
		}
	}

	@Override
	public boolean validate() {
		boolean valid = true;
		if (cmbTipoPrueba.getValue() == null) {
			valid = false;
			cmbTipoPrueba.getStyleClass().add("bad");
		}
		if (cmbProfesor.getValue() == null) {
			valid = false;
			cmbProfesor.getStyleClass().add("bad");
		}
		if (cmbCurso.getValue() == null) {
			valid = false;
			cmbCurso.getStyleClass().add("bad");
		}
		if (cmbAsignatura.getValue() == null) {
			valid = false;
			cmbAsignatura.getStyleClass().add("bad");
		}
		if (cmbNivelEvaluacion.getValue() == null) {
			valid = false;
			cmbNivelEvaluacion.getStyleClass().add("bad");
		}
		if (txtName.getText() == null || txtName.getText().isEmpty()) {
			valid = false;
			txtName.getStyleClass().add("bad");
		}
		if (bigDecimalForma.getNumber() == null) {
			valid = false;
			bigDecimalForma.getStyleClass().add("bad");
		}
		if (bigDecimaNroAlternativas.getNumber() == null) {
			valid = false;
			bigDecimaNroAlternativas.getStyleClass().add("bad");
		}
		if (bigDecimalNroPreguntas.getNumber() == null) {
			valid = false;
			bigDecimalNroPreguntas.getStyleClass().add("bad");
		}
		if (bigDecimalPuntajePregunta.getNumber() == null) {
			valid = false;
			bigDecimalPuntajePregunta.getStyleClass().add("bad");
		}
		if (bigDecimalExigencia.getNumber() == null) {
			valid = false;
			bigDecimalExigencia.getStyleClass().add("bad");
		}
		if (dpFecha.getValue() == null) {
			valid = false;
			dpFecha.getStyleClass().add("bad");
		}
		if (valid) {
			lblError.setText(" ");
		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}
		return valid;
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(cmbTipoPrueba);
		removeAllStyle(cmbProfesor);
		removeAllStyle(cmbCurso);
		removeAllStyle(cmbAsignatura);
		removeAllStyle(cmbNivelEvaluacion);
		removeAllStyle(txtName);
		removeAllStyle(bigDecimalForma);
		removeAllStyle(bigDecimaNroAlternativas);
		removeAllStyle(bigDecimalNroPreguntas);
		removeAllStyle(bigDecimalPuntajePregunta);
		removeAllStyle(dpFecha);
		removeAllStyle(bigDecimalExigencia);
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuModificar || source == mnuPopupModificar) {
			handleModificar();
		} else if (source == mnuGrabar) {
			handleGrabar();
		} else if (source == mnuEliminar || source == mnuPopupEliminar) {
			handleEliminar();
		} else if (source == mnuEvaluarPrueba) {
			handlerEvaluar();
		} else if (source == mnuDefinirPrueba) {
			handlerDefinirPrueba();
		} else if (source == mnuListaEvaluaciones) {
			handlerListaEvaluaciones();
		} else if (source == mnuComparativoComunal) {
			handlerComparativoComunal();
		} else if (source == mnuComparativoComunalHab) {
			handlerComparativoComunalHab();
		} else if (source == mnuComunalEje) {
			handlerComunalEje();
		} else if (source == mnuImprimirPrueba) {
			handlerImrpimirPrueba();
		} else if (source == mnuNueva) {
			handlerNuevaPrueba();
		}
	}

	private void handlerComunalEje() {
		if (comunalEje == null) {
			comunalEje = (ComunalCursoView) show("/cl/eos/view/ComunalEje.fxml");
		} else {
			show(comunalEje);
		}
		ObservableList<OTPrueba> otPruebas = tblListadoPruebas
				.getSelectionModel().getSelectedItems();
		if (otPruebas != null) {
			Prueba[] pruebas = new Prueba[otPruebas.size()];
			int n = 0;
			for (OTPrueba ot : otPruebas) {
				pruebas[n++] = ot.getPrueba();
			}
			controller.findByAllId(Prueba.class, pruebas, comunalEje);
			controller.findAll(EvaluacionEjeTematico.class, comunalEje);
		}
	}

	private void handlerComparativoComunal() {
		if (comparativoComunal == null) {
			comparativoComunal = (ComparativoComunalEjeView) show("/cl/eos/view/ComparativoComunalEje.fxml");
		} else {
			show(comparativoComunal);
		}
		if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
			Prueba prueba = tblListadoPruebas.getSelectionModel()
					.getSelectedItem().getPrueba();
			if (prueba != null) {
				controller.findById(Prueba.class, prueba.getId());
				controller.findAll(EvaluacionEjeTematico.class);
			}
		}
	}

	private void handlerNuevaPrueba() {
		removeAllStyles();
		limpiarCampos();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtName.requestFocus();
			}
		});
	}

	private void handlerImrpimirPrueba() {
		if (imprimirPrueba == null) {
			imprimirPrueba = (ImprimirPruebaView) show("/cl/eos/view/ImprimirPrueba.fxml");
		} else {
			show(imprimirPrueba);
		}
		if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
			Prueba prueba = tblListadoPruebas.getSelectionModel()
					.getSelectedItem().getPrueba();
			if (prueba != null) {
				controller.findById(Prueba.class, prueba.getId());
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("idAsignatura", prueba.getAsignatura().getId());
				controller.find("EjeTematico.findByAsigntura", parameters);
				controller.findAll(Habilidad.class);
				controller.findAll(Profesor.class);
				controller.findAll(Colegio.class);
			}
		}
	}

	private void handlerComparativoComunalHab() {
		if (comparativoComunalHabilidad == null) {
			comparativoComunalHabilidad = (ComparativoComunalHabilidadView) show("/cl/eos/view/ComparativoComunalHabilidad.fxml");
		} else {
			show(comparativoComunalHabilidad);
		}
		if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
			Prueba prueba = tblListadoPruebas.getSelectionModel()
					.getSelectedItem().getPrueba();
			if (prueba != null) {
				controller.findById(Prueba.class, prueba.getId());
				controller.findAll(EvaluacionEjeTematico.class);
			}
		}
	}

	private void handlerEvaluar() {
		if (evaluarPruebaView == null) {
			evaluarPruebaView = (EvaluarPruebaView) show("/cl/eos/view/EvaluarPrueba.fxml");
		} else {
			show(evaluarPruebaView);
		}
		if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
			Prueba prueba = tblListadoPruebas.getSelectionModel()
					.getSelectedItem().getPrueba();
			if (prueba != null) {
				controller.findById(Prueba.class, prueba.getId());
				controller.findAll(Colegio.class);
				controller.findAll(Profesor.class);
			}
		}
	}

	private void handlerDefinirPrueba() {
		if (definePrueba == null) {
			definePrueba = (DefinePruebaViewController) show("/cl/eos/view/DefinePruebaView.fxml");
		} else {
			show(definePrueba);
		}
		if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
			Prueba prueba = tblListadoPruebas.getSelectionModel()
					.getSelectedItem().getPrueba();
			if (prueba != null) {
				controller.findById(Prueba.class, prueba.getId());
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("idAsignatura", prueba.getAsignatura().getId());
				controller.find("EjeTematico.findByAsigntura", parameters);
				controller.findAll(Habilidad.class);
			}
		}
	}

	private void handlerListaEvaluaciones() {
		if (evaluacionPrueba == null) {
			evaluacionPrueba = (EvaluacionPruebaView) show("/cl/eos/view/EvaluacionPrueba.fxml");
		} else {
			show(evaluacionPrueba);
		}
		if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
			Prueba prueba = tblListadoPruebas.getSelectionModel()
					.getSelectedItem().getPrueba();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("idPrueba", prueba.getId());
			controller.find("EvaluacionPrueba.findByPrueba", parameters);
		}
	}

	private void handleEliminar() {

		ObservableList<OTPrueba> otSeleccionados = tblListadoPruebas
				.getSelectionModel().getSelectedItems();
		if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
			List<Prueba> pruebas = new ArrayList<Prueba>(otSeleccionados.size());
			for (OTPrueba ot : otSeleccionados) {
				pruebas.add(ot.getPrueba());
			}
			delete(pruebas);
			tblListadoPruebas.getSelectionModel().clearSelection();
			limpiarCampos();
		}
	}

	private void handleGrabar() {
		removeAllStyles();
		if (validate()) {
			if (prueba == null) {
				prueba = new Prueba();
			}
			prueba.setAlternativas(bigDecimaNroAlternativas.getNumber()
					.intValue());
			prueba.setAsignatura(cmbAsignatura.getValue());
			prueba.setCurso(cmbCurso.getValue());
			prueba.setFecha(dpFecha.getValue().toEpochDay());
			prueba.setNroFormas(bigDecimalForma.getNumber().intValue());
			prueba.setName(txtName.getText());
			prueba.setNivelEvaluacion(cmbNivelEvaluacion.getValue());
			prueba.setProfesor(cmbProfesor.getValue());
			prueba.setPuntajeBase(bigDecimalPuntajePregunta.getNumber()
					.intValue());
			prueba.setNroPreguntas(bigDecimalNroPreguntas.getNumber()
					.intValue());
			prueba.setAlternativas(bigDecimaNroAlternativas.getNumber()
					.intValue());
			prueba.setTipoPrueba(cmbTipoPrueba.getValue());
			prueba.setExigencia(bigDecimalExigencia.getNumber().intValue());

			save(prueba);
		}
	}

	private void handleModificar() {
		if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
			prueba = tblListadoPruebas.getSelectionModel().getSelectedItem()
					.getPrueba();

			if (prueba != null) {
				bigDecimaNroAlternativas.setNumber(new BigDecimal(prueba
						.getAlternativas()));
				cmbAsignatura.getSelectionModel()
						.select(prueba.getAsignatura());
				cmbCurso.getSelectionModel().select(prueba.getCurso());
				dpFecha.setValue(prueba.getFechaLocal());
				bigDecimalForma
						.setNumber(new BigDecimal(prueba.getNroFormas()));
				txtName.setText(prueba.getName());
				cmbNivelEvaluacion.getSelectionModel().select(
						prueba.getNivelEvaluacion());
				cmbProfesor.getSelectionModel().select(prueba.getProfesor());
				bigDecimalPuntajePregunta.setNumber(new BigDecimal(prueba
						.getPuntajeBase()));
				bigDecimalNroPreguntas.setNumber(new BigDecimal(prueba
						.getNroPreguntas()));
				cmbTipoPrueba.getSelectionModel()
						.select(prueba.getTipoPrueba());
				prueba.setExigencia(bigDecimalExigencia.getNumber().intValue());
			}
		}
	}

	@Override
	public void onDeleted(IEntity entity) {
		tblListadoPruebas.getItems().remove(entity);
	}

	private void limpiarCampos() {
		cmbTipoPrueba.getSelectionModel().clearSelection();
		cmbProfesor.getSelectionModel().clearSelection();
		cmbCurso.getSelectionModel().clearSelection();
		cmbAsignatura.getSelectionModel().clearSelection();
		cmbNivelEvaluacion.getSelectionModel().clearSelection();
		bigDecimalForma.setNumber(new BigDecimal(1));
		bigDecimaNroAlternativas.setNumber(new BigDecimal(3));
		bigDecimalNroPreguntas.setNumber(new BigDecimal(5));
		bigDecimalPuntajePregunta.setNumber(new BigDecimal(1));
		dpFecha.setValue(LocalDate.now());
		txtName.setText(null);
	}
}
