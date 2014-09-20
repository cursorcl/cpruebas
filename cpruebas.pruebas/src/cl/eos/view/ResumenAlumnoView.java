package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.util.ExportadorDeTablasAExcel;

public class ResumenAlumnoView extends AFormView implements
		EventHandler<ActionEvent> {
	@FXML
	private TextField txtPrueba;
	@FXML
	private TextField txtCurso;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private MenuItem mnuExportarRespuestas;
	@FXML
	private MenuItem mnuExportarAlumnos;

	@FXML
	private TableView<PruebaRendida> tblAlumnos;
	@FXML
	private TableColumn<PruebaRendida, String> colARut;
	@FXML
	private TableColumn<PruebaRendida, String> colAPaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colAMaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colAName;
	@FXML
	private TableColumn<PruebaRendida, Integer> colABuenas;
	@FXML
	private TableColumn<PruebaRendida, Integer> colAMalas;
	@FXML
	private TableColumn<PruebaRendida, Integer> colAOmitidas;

	@FXML
	private TableView<PruebaRendida> tblRespuestas;
	@FXML
	private TableColumn<PruebaRendida, String> colRRut;
	@FXML
	private TableColumn<PruebaRendida, String> colRPaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colRMaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colRName;

	public ResumenAlumnoView() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	public void initialize() {
		inicializarTablaAlumnos();
		clicTablaRespuesta();
		clicTablaAlumnos();
		mnuExportarRespuestas.setOnAction(this);
		mnuExportarAlumnos.setOnAction(this);
	}

	private void clicTablaRespuesta() {
		tblAlumnos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tblRespuestas.getSelectionModel().clearSelection();
				PruebaRendida itemsSelec = tblAlumnos.getSelectionModel()
						.getSelectedItem();

				tblRespuestas.getSelectionModel().select(itemsSelec);
			}
		});

	}

	private void clicTablaAlumnos() {
		tblRespuestas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tblAlumnos.getSelectionModel().clearSelection();
				PruebaRendida itemsSelec = tblRespuestas.getSelectionModel()
						.getSelectedItem();

				tblAlumnos.getSelectionModel().select(itemsSelec);
			}
		});

	}

	private void inicializarTablaAlumnos() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colARut.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"rut"));
		colAName.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"nombre"));
		colAPaterno
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"paterno"));
		colAMaterno
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"materno"));
		colABuenas
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"buenas"));
		colAMalas
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"malas"));
		colAOmitidas
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"omitidas"));

	}

	private void inicializarTablaRespuesta() {
		tblRespuestas.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colRRut.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"rut"));
		colRName.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"nombre"));
		colRPaterno
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"paterno"));
		colRMaterno
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"materno"));
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			EvaluacionPrueba evaluacionPrueba = (EvaluacionPrueba) entity;

			int nroPreguntas = evaluacionPrueba.getNroPreguntas();
			final String responses = evaluacionPrueba.getResponses();

			inicializarTablaRespuesta();
			for (int indice = 1; indice <= nroPreguntas; indice++) {
				TableColumn<PruebaRendida, String> nro = new TableColumn<PruebaRendida, String>(
						String.valueOf(indice));
				nro.setPrefWidth(10);
				nro.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"respuestas"));

				nro.setCellFactory(new Callback<TableColumn<PruebaRendida, String>, TableCell<PruebaRendida, String>>() {

					@Override
					public TableCell<PruebaRendida, String> call(
							TableColumn<PruebaRendida, String> param) {
						LetraRespuesta letraRespuesta = new LetraRespuesta();
						letraRespuesta.setResponses(responses);
						return letraRespuesta;
					}
				});
				tblRespuestas.getColumns().add(nro);
			}

			if (evaluacionPrueba.getCurso() != null) {
				txtCurso.setText(evaluacionPrueba.getCurso().getName());
			}

			if (evaluacionPrueba.getPrueba() != null) {
				txtPrueba.setText(evaluacionPrueba.getPrueba().getName());
				if (evaluacionPrueba.getPrueba().getAsignatura() != null) {
					txtAsignatura.setText(evaluacionPrueba.getPrueba()
							.getAsignatura().getName());
				}
			}

			List<PruebaRendida> list = evaluacionPrueba.getPruebasRendidas();
			if (list != null && !list.isEmpty()) {
				ObservableList<PruebaRendida> oList = FXCollections
						.observableArrayList(list);
				tblAlumnos.setItems(oList);
				tblRespuestas.setItems(oList);
			}

		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuExportarRespuestas) {
			ExportadorDeTablasAExcel.convertirDatosALibroDeExcel(tblRespuestas);
		} else if (source == mnuExportarAlumnos) {
			ExportadorDeTablasAExcel.convertirDatosALibroDeExcel(tblAlumnos);
		}
	}
}