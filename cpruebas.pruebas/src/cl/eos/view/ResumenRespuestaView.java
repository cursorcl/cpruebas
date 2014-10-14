package cl.eos.view;

import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTRespuestaPreguntas;
import cl.eos.ot.OTRespuestasPorcentaje;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.util.ExcelSheetWriterObj;

public class ResumenRespuestaView extends AFormView implements
EventHandler<ActionEvent>{

	@FXML
	private TableView<OTRespuestaPreguntas> tblPreguntas;
	@FXML
	private TableColumn<OTRespuestaPreguntas, String> colPregunta;
	@FXML
	private TableColumn<OTRespuestaPreguntas, Integer> colBuenas;
	@FXML
	private TableColumn<OTRespuestaPreguntas, Integer> colMalas;
	@FXML
	private TableColumn<OTRespuestaPreguntas, Integer> colOmitidas;

	@FXML
	private TableView<OTRespuestasPorcentaje> tblPorcentaje;
	@FXML
	private TableColumn<OTRespuestasPorcentaje, String> colTitulo;
	@FXML
	private TableColumn<OTRespuestasPorcentaje, Float> colPorcentaje;

	@FXML
	private TextField txtPrueba;
	@FXML
	private TextField txtCurso;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private TextField txtNroPreguntas;
	@FXML
	private TextField txtPuntaje;
	final NumberAxis xAxis = new NumberAxis();
	final CategoryAxis yAxis = new CategoryAxis();
	@FXML
	private BarChart<String, Number> graficoBarra = new BarChart<String, Number>(
			yAxis, xAxis);
	
	@FXML
	private MenuItem mnuExportarRespuestas;

	private LinkedList<OTRespuestaPreguntas> listaRespuestas;
	private LinkedList<OTRespuestasPorcentaje> listaPorcentaje;

	public ResumenRespuestaView() {
		
	}

	@FXML
	public void initialize() {
		this.setTitle("Resumen de respuestas por pregunta");
		inicializarTablaRespuestas();
		inicializarTablaPorcentaje();
		graficoBarra.setTitle("Gráfico Respuestas por pregunta");
		xAxis.setLabel("Country");
		yAxis.setLabel("Value");
		mnuExportarRespuestas.setOnAction(this);
	}

	private void inicializarTablaRespuestas() {
		tblPreguntas.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colPregunta
				.setCellValueFactory(new PropertyValueFactory<OTRespuestaPreguntas, String>(
						"name"));
		colBuenas
				.setCellValueFactory(new PropertyValueFactory<OTRespuestaPreguntas, Integer>(
						"buenas"));
		colMalas.setCellValueFactory(new PropertyValueFactory<OTRespuestaPreguntas, Integer>(
				"malas"));
		colOmitidas
				.setCellValueFactory(new PropertyValueFactory<OTRespuestaPreguntas, Integer>(
						"omitidas"));
	}

	private void inicializarTablaPorcentaje() {
		tblPorcentaje.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colTitulo
				.setCellValueFactory(new PropertyValueFactory<OTRespuestasPorcentaje, String>(
						"titulo"));
		colPorcentaje
				.setCellValueFactory(new PropertyValueFactory<OTRespuestasPorcentaje, Float>(
						"porcentaje"));
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {

			txtAsignatura.setText(((EvaluacionPrueba) entity).getAsignatura());
			txtCurso.setText(((EvaluacionPrueba) entity).getCurso().getName());
			txtNroPreguntas.setText(String.valueOf(((EvaluacionPrueba) entity)
					.getPrueba().getNroPreguntas()));
			txtPrueba
					.setText(((EvaluacionPrueba) entity).getPrueba().getName());
			txtPuntaje.setText(null);

			obtenerResultados((EvaluacionPrueba) entity);

			if (listaRespuestas != null && !listaRespuestas.isEmpty()) {
				ObservableList<OTRespuestaPreguntas> oList = FXCollections
						.observableArrayList(listaRespuestas);
				tblPreguntas.setItems(oList);
			}

			if (listaPorcentaje != null && !listaPorcentaje.isEmpty()) {
				ObservableList<OTRespuestasPorcentaje> oList = FXCollections
						.observableArrayList(listaPorcentaje);
				tblPorcentaje.setItems(oList);
			}

		}
	}

	private void obtenerResultados(EvaluacionPrueba entity) {
		List<PruebaRendida> pruebasRendidas = entity.getPruebasRendidas();
		Integer nroPreguntas = entity.getPrueba().getNroPreguntas();
		Integer buenas = 0;
		Integer malas = 0;
		Integer omitidas = 0;

		int[] sBuenas = new int[nroPreguntas];
		int[] sMalas = new int[nroPreguntas];
		int[] sOmitidas = new int[nroPreguntas];

		for (PruebaRendida pruebaRendida : pruebasRendidas) {

			String responses = entity.getPrueba().getResponses();
			String respuesta = pruebaRendida.getRespuestas();

			char[] cResponses = responses.toCharArray();
			char[] cRespuesta = respuesta.toCharArray();

			for (int i = 0; i < cResponses.length; i++) {

				if (cRespuesta[i] == ' ') {
					sOmitidas[i] = sOmitidas[i] + 1;
					omitidas = omitidas + 1;
				} else if (String.valueOf(cRespuesta[i]).toUpperCase().equals(String.valueOf(cResponses[i]).toUpperCase())) {
					sBuenas[i] = sBuenas[i] + 1;
					buenas = buenas + 1;
				} else {
					sMalas[i] = sMalas[i] + 1;
					malas = malas + 1;
				}
			}
		}

		listaRespuestas = new LinkedList<OTRespuestaPreguntas>();
		for (int i = 0; i < nroPreguntas ; i++) {
			OTRespuestaPreguntas otRespuesta = new OTRespuestaPreguntas();
			otRespuesta.setName(String.valueOf(i + 1));
			otRespuesta.setBuenas(sBuenas[i]);
			otRespuesta.setMalas(sMalas[i]);
			otRespuesta.setOmitidas(sOmitidas[i]);
			listaRespuestas.add(otRespuesta);
		}

		listaPorcentaje = new LinkedList<OTRespuestasPorcentaje>();
		OTRespuestasPorcentaje respuestaPorcentajeB = new OTRespuestasPorcentaje();
		respuestaPorcentajeB.setTitulo("Buenas");
		float porcentajeBuenas = (float) (buenas / nroPreguntas);
		respuestaPorcentajeB.setPorcentaje(porcentajeBuenas*100);
		listaPorcentaje.add(respuestaPorcentajeB);

		OTRespuestasPorcentaje respuestaPorcentajeM = new OTRespuestasPorcentaje();
		respuestaPorcentajeM.setTitulo("Malas");
		float porcentajeMalas = (float) (malas / nroPreguntas);
		respuestaPorcentajeM.setPorcentaje(porcentajeMalas*100);
		listaPorcentaje.add(respuestaPorcentajeM);

		OTRespuestasPorcentaje respuestaPorcentaje = new OTRespuestasPorcentaje();
		respuestaPorcentaje.setTitulo("Omitidas");
		float porcentajeOmitidas = (float) (omitidas / nroPreguntas);
		respuestaPorcentaje.setPorcentaje(porcentajeOmitidas*100);
		listaPorcentaje.add(respuestaPorcentaje);
		
		XYChart.Series series1 = new XYChart.Series();
		series1.setName("Porcentaje de Respuestas");
		series1.getData().add(new XYChart.Data<String, Float>("Buenas", porcentajeBuenas));
		series1.getData().add(new XYChart.Data<String, Float>("Malas", porcentajeMalas));
		series1.getData().add(new XYChart.Data<String, Float>("Omitidas",porcentajeOmitidas));
		graficoBarra.getData().add(series1);
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuExportarRespuestas) {
			
			tblPreguntas.setId("Alumnos");
			tblPorcentaje.setId("Porcentaje");
			
			List<TableView<? extends Object>> listaTablas = new LinkedList<>();
			listaTablas.add((TableView<? extends Object>) tblPreguntas);
			listaTablas.add((TableView<? extends Object>) tblPorcentaje);
			
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
		} 
	}
}
