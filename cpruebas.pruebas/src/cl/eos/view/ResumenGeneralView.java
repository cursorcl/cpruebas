package cl.eos.view;

import java.util.LinkedList;
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
import cl.eos.ot.OTResumenGeneral;
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
	private TableView<OTResumenGeneral> tblResumen;
	@FXML
	private TableColumn<OTResumenGeneral, String> colNombre;
	@FXML
	private TableColumn<OTResumenGeneral, Float> colNotas;
	@FXML
	private TableColumn<OTResumenGeneral, Float> colBuenas;
	@FXML
	private TableColumn<OTResumenGeneral, Integer> ColPuntos;
	@FXML
	private TableColumn<OTResumenGeneral, Float> colPuntaje;

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
		colNombre
				.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, String>(
						"name"));
		colNotas.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>(
				"nota"));
		colBuenas
				.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>(
						"pbuenas"));
		ColPuntos
				.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Integer>(
						"puntaje"));
		colPuntaje
				.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>(
						"ppuntaje"));
		
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			EvaluacionPrueba evaluacionPrueba = (EvaluacionPrueba) entity;
			evaluacionPrueba.procesarValoresMinMax();

			List<OTResumenGeneral> listaResumen = new LinkedList<OTResumenGeneral>();
			listaResumen.add(new OTResumenGeneral("Máximo", evaluacionPrueba
					.getNotaMax(), evaluacionPrueba.getPBuenasMax(),
					evaluacionPrueba.getPpuntajeMax(), evaluacionPrueba
							.getPuntajeMax()));

			listaResumen.add(new OTResumenGeneral("Mínimo", evaluacionPrueba
					.getNotaMin(), evaluacionPrueba.getPBuenasMin(),
					evaluacionPrueba.getPpuntajeMin(), evaluacionPrueba
							.getPuntajeMin()));

			Float promNota = (evaluacionPrueba.getNotaMax() + evaluacionPrueba
					.getNotaMin()) / 2;
			Float prompBuenas = (evaluacionPrueba.getPBuenasMax() + evaluacionPrueba
					.getPBuenasMin()) / 2;
			Float prompPuntaje = (evaluacionPrueba.getPpuntajeMax() + evaluacionPrueba
					.getPpuntajeMin()) / 2;
			Integer promPuntaje = (evaluacionPrueba.getPuntajeMax() + evaluacionPrueba
					.getPuntajeMin()) / 2;

			listaResumen.add(new OTResumenGeneral("Promedio", promNota,
					prompBuenas, prompPuntaje, promPuntaje));
			asignarDatosEvalucion(evaluacionPrueba, listaResumen);
		}
	}

	private void asignarDatosEvalucion(EvaluacionPrueba entity,
			List<OTResumenGeneral> listaResumen) {
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
					.observableArrayList(list);
			tblAlumnos.setItems(oList);
		}

		ObservableList<OTResumenGeneral> oList = FXCollections
				.observableArrayList(listaResumen);
		tblResumen.setItems(oList);
	}
}
