package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;

public class ResumenGeneralView extends AFormView {
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
	private TableView<EvaluacionPrueba> tblResumen;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> colNotas;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> colBuenas;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> ColPuntos;
	@FXML
	private TableColumn<EvaluacionPrueba, Integer> colPuntaje;

	@FXML
	private TableView<PruebaRendida> tblAlumnos;
	@FXML
	private TableColumn<PruebaRendida, String> colRut;
	@FXML
	private TableColumn<PruebaRendida, String> colPaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colMaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colName;
	@FXML
	private TableColumn<PruebaRendida, String> colCurso;
	@FXML
	private TableColumn<PruebaRendida, Integer> colABuenas;
	@FXML
	private TableColumn<PruebaRendida, Integer> colAMalas;
	@FXML
	private TableColumn<PruebaRendida, Integer> colAOmitidas;
	@FXML
	private TableColumn<PruebaRendida, Float> colPBuenas;
	@FXML
	private TableColumn<PruebaRendida, Integer> colAPuntaje;
	@FXML
	private TableColumn<PruebaRendida, Float> colPPuntaje;
	@FXML
	private TableColumn<PruebaRendida, Float> colANota;

	public ResumenGeneralView() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	public void initialize() {
		inicializarTablaResumen();
		inicializarTablaAlumnos();
	}

	private void inicializarTablaAlumnos() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRut.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"rut"));
		colName.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"nombre"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"paterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
						"materno"));
		colCurso.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>(
				"curso"));
		colABuenas
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"buenas"));
		colAMalas
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"malas"));
		colAOmitidas
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"omitidas"));
		colPBuenas
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Float>(
						"pbuenas"));
		colAPuntaje
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>(
						"puntaje"));
		colPPuntaje
				.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Float>(
						"ppuntaje"));
		colANota.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Float>(
				"nota"));

	}

	private void inicializarTablaResumen() {
		tblResumen.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colNotas.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
				"fechaLocal"));
		colBuenas
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
						"buenas"));
		ColPuntos
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
						"asignatura"));
		colPuntaje
				.setCellValueFactory(new PropertyValueFactory<EvaluacionPrueba, Integer>(
						"profesor"));
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			((EvaluacionPrueba) entity).obtenerValoresMinMax();
			asignarDatosEvalucion((EvaluacionPrueba) entity);
		}
	}

	private void asignarDatosEvalucion(EvaluacionPrueba entity) {
		if (entity.getCurso() != null) {
			txtCurso.setText(entity.getCurso().getName());
		}

		if (entity.getPrueba() != null) {
			txtPrueba.setText(entity.getPrueba().getName());
			txtPjePrueba
					.setText(entity.getPrueba().getPuntajeBase().toString());
			txtNoPregunta.setText(entity.getPrueba().getNroPreguntas()
					.toString());
			if (entity.getPrueba().getAsignatura() != null) {
				txtAsignatura.setText(entity.getPrueba().getAsignatura()
						.getName());
			}
		}

		List<PruebaRendida> list = entity.getPruebasRendidas();
		if (list != null && !list.isEmpty()) {
			ObservableList<PruebaRendida> oList = FXCollections
					.observableArrayList();
			for (Object iEntity : list) {
				oList.add((PruebaRendida) iEntity);
			}
			tblAlumnos.setItems(oList);
		}

//		if (list != null && !list.isEmpty()) {
//			Object entityObj = list.get(0);
//			if (entityObj instanceof EvaluacionPrueba) {
//				ObservableList<EvaluacionPrueba> oList = FXCollections
//						.observableArrayList();
//				for (Object iEntity : list) {
//					oList.add((EvaluacionPrueba) iEntity);
//				}
//				tblResumen.setItems(oList);
//			}
//		}

	}
}
