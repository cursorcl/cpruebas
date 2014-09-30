package cl.eos.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.EEvaluados;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.TipoCurso;

public class ComunalCursoEjeTematico extends AFormView implements
		EventHandler<ActionEvent> {

	@FXML
	private Label lblTitulo;
	private boolean llegaOnDataArrived;
	private boolean llegaOnFound;
	@FXML
	private TableView tblEvaluaciones;
	@FXML
	private TableView tblTotales;

	private HashMap<Long, EvaluacionEjeTematico> mEvaluaciones;
	private Map<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;
	private ArrayList<String> titulosColumnas;

	public ComunalCursoEjeTematico() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFound(IEntity entity) {

	}

	private void desplegarDatosEvaluaciones() {
		ObservableList<ObservableList> registros = FXCollections
				.observableArrayList();

		for (Entry<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapa : mapEvaAlumnos
				.entrySet()) {

			ObservableList<String> row = FXCollections.observableArrayList();

			row.add(((EvaluacionEjeTematico) mapa.getKey()).getName());

			HashMap<String, OTPreguntasEvaluacion> resultados = mapa.getValue();

			for (String string : titulosColumnas) {
				OTPreguntasEvaluacion otPregunta = resultados.get(string);
				row.add(String.valueOf(otPregunta.getAlumnos()));
			}

			registros.add(row);
		}
		tblEvaluaciones.setItems(registros);
	}

	private void desplegarDatosTotales() {

		Map<String, Integer> totales = new HashMap<String, Integer>();

		ObservableList<ObservableList> registroseEva = FXCollections
				.observableArrayList();
		ObservableList<String> row = null;
		int total = 0;

		for (EEvaluados nameEvaluados : EEvaluados.values()) {

			row = FXCollections.observableArrayList();
			row.add(nameEvaluados.getName());
			for (Entry<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapa : mapEvaAlumnos
					.entrySet()) {
				HashMap<String, OTPreguntasEvaluacion> resultados = mapEvaAlumnos
						.get(mapa.getKey());
				for (String string : titulosColumnas) {

					OTPreguntasEvaluacion otPregunta = resultados.get(string);
					if (totales.containsKey(string)) {
						total = otPregunta.getAlumnos() + totales.get(string);
						totales.replace(string, total);
					} else {
						totales.put(string, otPregunta.getAlumnos());
					}
					row.add(String.valueOf(otPregunta.getAlumnos()));
				}
			}
			registroseEva.add(row);

		}
		row = FXCollections.observableArrayList();
		row.add("Totales");
		for (String string : titulosColumnas) {
			Integer valor = totales.get(string);
			row.add(String.valueOf(valor));
		}
		registroseEva.add(row);

		tblTotales.setItems(registroseEva);
	}

	private void llenarDatosTabla(Prueba prueba) {

		if (llegaOnFound && llegaOnDataArrived) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(prueba.getAsignatura());
			buffer.append(" ");
			buffer.append(prueba.getCurso());
			lblTitulo.setText(buffer.toString());

			mapEvaAlumnos = new HashMap<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>>();

			List<EvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();

			creacionColumnasEvaluaciones(listaEvaluaciones);
			creacionColumnasTotalesEvaluaciones(listaEvaluaciones);

			// ********** generar datos evaluaciones y totales
			for (EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
				TipoCurso tipoCurso = evaluacionPrueba.getCurso()
						.getTipoCurso();
				String nameTpCurso = tipoCurso.getName();

				HashMap<String, OTPreguntasEvaluacion> mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
				for (PruebaRendida pruebaRendida : evaluacionPrueba
						.getPruebasRendidas()) {

					Float pBuenas = pruebaRendida.getPbuenas();
					for (Entry<Long, EvaluacionEjeTematico> mEvaluacion : mEvaluaciones
							.entrySet()) {

						EvaluacionEjeTematico evaluacionAl = mEvaluacion
								.getValue();
						if (mapEvaAlumnos.containsKey(evaluacionAl)) {
							HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos
									.get(evaluacionAl);
							if (evaluacion.containsKey(nameTpCurso)) {
								OTPreguntasEvaluacion otPreguntas = evaluacion
										.get(nameTpCurso);

								if (pBuenas >= evaluacionAl.getNroRangoMin()
										&& pBuenas <= evaluacionAl
												.getNroRangoMax()) {
									otPreguntas.setAlumnos(otPreguntas
											.getAlumnos() + 1);
								}
							} else {

								OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
								if (pBuenas >= evaluacionAl.getNroRangoMin()
										&& pBuenas <= evaluacionAl
												.getNroRangoMax()) {
									pregunta.setAlumnos(1);
								} else {
									pregunta.setAlumnos(0);
								}
								pregunta.setEvaluacion(evaluacionAl);
								evaluacion.put(nameTpCurso, pregunta);
							}
						} else {
							OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
							if (pBuenas >= evaluacionAl.getNroRangoMin()
									&& pBuenas <= evaluacionAl.getNroRangoMax()) {
								pregunta.setAlumnos(1);
							} else {
								pregunta.setAlumnos(0);
							}
							pregunta.setEvaluacion(evaluacionAl);

							mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
							mapaOT.put(nameTpCurso, pregunta);
							mapEvaAlumnos.put(evaluacionAl, mapaOT);
							System.out.println("name tp" + nameTpCurso + " "
									+ evaluacionAl.getName() + " "
									+ pregunta.getAlumnos());
						}
					}
				}

			}

		}
	}

	private void creacionColumnasEvaluaciones(
			List<EvaluacionPrueba> pListaEvaluaciones) {
		TableColumn columna0 = new TableColumn("Niveles de logros");
		columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {

				return new SimpleStringProperty(param.getValue().get(0)
						.toString());
			}
		});
		columna0.setPrefWidth(100);
		tblEvaluaciones.getColumns().add(columna0);

		titulosColumnas = new ArrayList<>();
		int indice = 1;
		List<EvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
		for (EvaluacionPrueba evaluacion : listaEvaluaciones) {
			// Columnas
			final int col = indice;
			final String tipoCurso = evaluacion.getTipoCurso();
			titulosColumnas.add(tipoCurso);
			TableColumn columna = new TableColumn(tipoCurso);
			columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(
						CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(col)
							.toString());
				}
			});
			columna.setPrefWidth(100);
			tblEvaluaciones.getColumns().add(columna);
			indice++;
		}
	}

	private void creacionColumnasTotalesEvaluaciones(
			List<EvaluacionPrueba> pListaEvaluaciones) {
		TableColumn columna0 = new TableColumn("");
		columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(0)
						.toString());
			}
		});
		columna0.setPrefWidth(100);
		tblTotales.getColumns().add(columna0);

		int indice = 1;
		for (String tpoCurso : titulosColumnas) {

			// Columnas
			final int col = indice;
			final String tipoCurso = tpoCurso;
			TableColumn columna = new TableColumn(tipoCurso);
			columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(
						CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(col)
							.toString());
				}
			});
			columna.setPrefWidth(100);
			tblTotales.getColumns().add(columna);
			indice++;
		}
	}

	@Override
	public void onDataArrived(List<Object> list) {
		llegaOnDataArrived = true;
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof EvaluacionEjeTematico) {
				mEvaluaciones = new HashMap<Long, EvaluacionEjeTematico>();
				for (Object object : list) {
					EvaluacionEjeTematico eje = (EvaluacionEjeTematico) object;
					mEvaluaciones.put(eje.getId(), eje);
				}
			}

			if (entity instanceof Prueba) {
				Prueba prueba = (Prueba) entity;
				llegaOnFound = true;
				llenarDatosTabla(prueba);
				desplegarDatosEvaluaciones();
				desplegarDatosTotales();
			}
		}
	}

}
