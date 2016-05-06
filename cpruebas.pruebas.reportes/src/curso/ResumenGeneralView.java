package curso;

import java.util.LinkedList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTResumenGeneral;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.util.ExcelSheetWriterObj;
// TODO Auto-generated constructor stub
public class ResumenGeneralView extends AFormView implements
EventHandler<ActionEvent> {
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
	@FXML
	private MenuItem mnuExportarAlumnos;
	@FXML
	private MenuItem mnuExportarResumen;
	

	private Float notaMin = Float.MAX_VALUE;
	private Float notaMax = Float.MIN_VALUE;
	private Float pbuenasMin = Float.MAX_VALUE;
	private Float pbuenasMax = Float.MIN_VALUE;
	private Float ppuntajeMin = Float.MAX_VALUE;
	private Float ppuntajeMax =Float.MIN_VALUE;
	private Integer puntajeMin = Integer.MAX_VALUE;
	private Integer puntajeMax = Integer.MIN_VALUE;

	public ResumenGeneralView() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	public void initialize() {
		this.setTitle("Resumen de respuestas generales");
		inicializarTablaResumen();
		inicializarTablaAlumnos();
		mnuExportarAlumnos.setOnAction(this);
		mnuExportarResumen.setOnAction(this);
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
						"ppuntajes"));
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
			List<PruebaRendida> pRendidas = evaluacionPrueba
					.getPruebasRendidas();
			List<OTResumenGeneral> listaResumen = procesarValoresMinMax(pRendidas);
			asignarDatosEvalucion(evaluacionPrueba, listaResumen);
		}
	}

	private List<OTResumenGeneral> procesarValoresMinMax(
			List<PruebaRendida> pruebasRendidas) {

		for (PruebaRendida pruebaRendida : pruebasRendidas) {
			if (pruebaRendida.getNota() < notaMin) {
				notaMin = pruebaRendida.getNota();
			}
			if (pruebaRendida.getNota() > notaMax) {
				notaMax = pruebaRendida.getNota();
			}

			if (pruebaRendida.getPbuenas() < pbuenasMin) {
				pbuenasMin = pruebaRendida.getPbuenas();
			}
			if (pruebaRendida.getPbuenas() > pbuenasMax) {
				pbuenasMax = pruebaRendida.getPbuenas();
			}

			if (pruebaRendida.getPpuntaje() < ppuntajeMin) {
				ppuntajeMin = pruebaRendida.getPpuntaje();
			}
			if (pruebaRendida.getPpuntaje() > ppuntajeMax) {
				ppuntajeMax = pruebaRendida.getPpuntaje();
			}

			if (pruebaRendida.getPuntaje() < puntajeMin) {
				puntajeMin = pruebaRendida.getPuntaje();
			}
			if (pruebaRendida.getPuntaje() > puntajeMax) {
				puntajeMax = pruebaRendida.getPuntaje();
			}
		}

		List<OTResumenGeneral> listaResumen = new LinkedList<OTResumenGeneral>();
		listaResumen.add(new OTResumenGeneral("Máximo", notaMax, pbuenasMax,
				ppuntajeMax, puntajeMax));

		listaResumen.add(new OTResumenGeneral("Mínimo", notaMin, pbuenasMin,
				ppuntajeMin, puntajeMin));

		Float promNota = (notaMax + notaMin) / 2;
		Float prompBuenas = (pbuenasMax + pbuenasMin) / 2;
		Float prompPuntaje = (ppuntajeMax + ppuntajeMin) / 2;
		Integer promPuntaje = (puntajeMax + puntajeMin) / 2;

		listaResumen.add(new OTResumenGeneral("Promedio", promNota,
				prompBuenas, prompPuntaje, promPuntaje));

		return listaResumen;
	}

	private void asignarDatosEvalucion(EvaluacionPrueba entity,
			List<OTResumenGeneral> listaResumen) {
		if (entity.getCurso() != null) {
			txtCurso.setText(entity.getCurso().getName());
			txtExigencia.setText(String.valueOf(entity.getExigencia()));
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

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuExportarResumen || source == mnuExportarAlumnos) {
			
			tblAlumnos.setId("Alumnos");
			tblResumen.setId("Resumen");
			
			List<TableView<? extends Object>> listaTablas = new LinkedList<>();
			listaTablas.add((TableView<? extends Object>) tblAlumnos);
			listaTablas.add((TableView<? extends Object>) tblResumen);
			
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
		} 
		
	}
}