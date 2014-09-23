package cl.eos.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;

public class ResumenHabilidadesView extends AFormView {

	@FXML
	private TableView<OTPreguntasHabilidad> tblHabilidades;
	@FXML
	private TableColumn<OTPreguntasHabilidad, String> colNombre;
	@FXML
	private TableColumn<OTPreguntasHabilidad, String> colDescripcion;
	@FXML
	private TableColumn<OTPreguntasHabilidad, Float> colLogrado;
	@FXML
	private TableColumn<OTPreguntasHabilidad, Float> colNoLogrado;

	@FXML
	private TextField txtPrueba;
	@FXML
	private TextField txtCurso;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private TextField txtHabilidad;

	final NumberAxis xAxis = new NumberAxis();
	final CategoryAxis yAxis = new CategoryAxis();
	@FXML
	private BarChart<String, Number> graficoBarra = new BarChart<String, Number>(
			yAxis, xAxis);

	private HashMap<Habilidad, OTPreguntasHabilidad> mapaHabilidades;

	public ResumenHabilidadesView() {

	}

	@FXML
	public void initialize() {
		inicializarTablaHabilidades();
		accionClicTabla();
		this.setTitle("Resumen Respuestas por Habilidades");
		graficoBarra.setTitle("Gráfico Respuestas por habilidad");
		xAxis.setLabel("Country");
		yAxis.setLabel("Value");
	}

	private void inicializarTablaHabilidades() {
		tblHabilidades.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNombre
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>(
						"name"));
		colDescripcion
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>(
						"descripcion"));
		colLogrado
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, Float>(
						"logrado"));
		colNoLogrado
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, Float>(
						"nologrado"));
	}

	private void accionClicTabla() {
		tblHabilidades.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<OTPreguntasHabilidad> itemsSelec = tblHabilidades
						.getSelectionModel().getSelectedItems();

				if (itemsSelec.size() == 1) {
					// select((OTPreguntasHabilidad) itemsSelec.get(0));
					OTPreguntasHabilidad habilidad = itemsSelec.get(0);
					txtHabilidad.setText(habilidad.getName());
					
					Float porcentajeLogrado = habilidad.getLogrado(); 
					Float porcentajeNologrado=habilidad.getNologrado();
					
					XYChart.Series series1 = new XYChart.Series();
					series1.setName("Porcentaje de Respuestas");
					series1.getData().add(new XYChart.Data<String, Float>("Logrado", porcentajeLogrado));
					series1.getData().add(new XYChart.Data<String, Float>("No Logrado", porcentajeNologrado));
					graficoBarra.getData().clear();
					graficoBarra.getData().add(series1);
				}
			}
		});
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {

			txtAsignatura.setText(((EvaluacionPrueba) entity).getAsignatura());
			txtCurso.setText(((EvaluacionPrueba) entity).getCurso().getName());
			txtPrueba
					.setText(((EvaluacionPrueba) entity).getPrueba().getName());
			obtenerResultados((EvaluacionPrueba) entity);

			if (mapaHabilidades != null && !mapaHabilidades.isEmpty()) {

				ArrayList<OTPreguntasHabilidad> listado = new ArrayList<>(
						mapaHabilidades.values());
				ObservableList<OTPreguntasHabilidad> oList = FXCollections
						.observableList(listado);
				tblHabilidades.setItems(oList);
			}
		}
	}

	private void obtenerResultados(EvaluacionPrueba entity) {
		List<PruebaRendida> pruebasRendidas = entity.getPruebasRendidas();

		mapaHabilidades = new HashMap<Habilidad, OTPreguntasHabilidad>();

		Prueba prueba = entity.getPrueba();
		List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba
				.getRespuestas();

		for (PruebaRendida pruebaRendida : pruebasRendidas) {
			String respuesta = pruebaRendida.getRespuestas();
			char[] cRespuesta = respuesta.toCharArray();

			for (RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {

				Habilidad hab = respuestasEsperadasPrueba.getHabilidad();
				Integer numeroPreg = respuestasEsperadasPrueba.getNumero();

				if (mapaHabilidades.containsKey(hab)) {

					OTPreguntasHabilidad otPreguntas = mapaHabilidades.get(hab);
					if (cRespuesta[numeroPreg-1] == respuestasEsperadasPrueba
							.getRespuesta().toCharArray()[0]) {
						otPreguntas.setBuenas(otPreguntas.getBuenas() + 1);
					}
					otPreguntas.setTotal(otPreguntas.getTotal() + 1);

				} else {
					Integer valor = 1;
					OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
					otPreguntas.setHabilidad(hab);
					if (cRespuesta[numeroPreg-1] == respuestasEsperadasPrueba
							.getRespuesta().toCharArray()[0]) {
						otPreguntas.setBuenas(valor);
					}
					else{
						otPreguntas.setBuenas(0);
					}
					otPreguntas.setTotal(valor);
					mapaHabilidades.put(hab, otPreguntas);
				}
			}
		}

		// XYChart.Series series1 = new XYChart.Series();
		// series1.setName("Porcentaje de Respuestas");
		// series1.getData().add(
		// new XYChart.Data<String, Float>("Buenas", porcentajeBuenas));
		// series1.getData().add(
		// new XYChart.Data<String, Float>("Malas", porcentajeMalas));
		// series1.getData()
		// .add(new XYChart.Data<String, Float>("Omitidas",
		// porcentajeOmitidas));
		// graficoBarra.getData().add(series1);
	}
}
