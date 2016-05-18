package comunal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.ot.EEvaluados;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.models.TipoCurso;

public class ComunalCursoView extends AFormView implements
		EventHandler<ActionEvent> {

	@FXML
	private Label lblTitulo;
	@FXML
	private TableView<ObservableList<String>> tblEvaluaciones;
	@FXML
	private TableView<ObservableList<String>> tblTotales;
	
	@FXML
	private ComboBox<TipoAlumno> cmbTipoAlumno;

	private HashMap<Long, EvaluacionEjeTematico> mEvaluaciones;

	private ArrayList<String> titulosColumnas;
	
	long tipoAlumno = Constants.PIE_ALL;
	
	private boolean llegaEvaluacionEjeTematico;
	private boolean llegaOnFound;
	private boolean llegaTipoAlumno = false;
	private List<Object> listaPruebas;

	public ComunalCursoView() {
		setTitle("Resumen comunal");
	}

	@FXML
	public void initialize() {
		cmbTipoAlumno.getSelectionModel().select(0);
		cmbTipoAlumno.setOnAction(event -> {
			tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex();
			if (listaPruebas != null && tipoAlumno != -1) {
				procesaDatosReporte();
			}
		});
	}

	private void procesaDatosReporte() {
		if (llegaEvaluacionEjeTematico && llegaOnFound && llegaTipoAlumno) {
			inicializarComponentes();
			llenarDatosTabla();
			creacionColumnasEvaluaciones(listaEvaluacionesTitulos);
			creacionColumnasTotalesEvaluaciones(listaEvaluacionesTitulos);
			desplegarDatosEvaluaciones();
			desplegarDatosTotales();
		}		
	}

	private void desplegarDatosEvaluaciones() {
		ObservableList<ObservableList<String>> registros = FXCollections
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

		Map<String, Float> totalEvaluados = new HashMap<String, Float>();
		Map<String, Float> totalInformados = new HashMap<String, Float>();

		ObservableList<ObservableList<String>> registroseEva = FXCollections
				.observableArrayList();
		ObservableList<String> row = FXCollections.observableArrayList();

		// Total evaluados
		float total = 0;
		for (HashMap<String, OTPreguntasEvaluacion> resultados : mapEvaAlumnos
				.values()) {

			for (String string : titulosColumnas) {
				OTPreguntasEvaluacion otPregunta = resultados.get(string);

				if (totalEvaluados.containsKey(string)) {
					total = otPregunta.getAlumnos()
							+ totalEvaluados.get(string);
					totalEvaluados.replace(string, total);
				} else {
					totalEvaluados.put(string, (float) otPregunta.getAlumnos());
				}
			}
		}

		// Total informados
		for (Object objeto : listaPruebas) {
			if (objeto instanceof Prueba) {
				Prueba prueba = (Prueba) objeto;
				String tipoCurso = prueba.getCurso().getName();

				List<EvaluacionPrueba> listaEvaluaciones = prueba
						.getEvaluaciones();
				for (EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
					int totalAlumnos = evaluacionPrueba.getCurso().getAlumnos()
							.size();
					if (totalInformados.containsKey(tipoCurso)) {
						total = totalInformados.get(tipoCurso) + totalAlumnos;
						totalInformados.replace(tipoCurso, total);
					} else {
						totalInformados.put(tipoCurso, (float) totalAlumnos);
					}
				}
			}
		}

		row.add(EEvaluados.TOTAL_EVA.getName());
		for (String tipoCurso : titulosColumnas) {
			row.add(String.valueOf(totalEvaluados.get(tipoCurso)));
		}
		registroseEva.add(row);

		row = FXCollections.observableArrayList();
		row.add(EEvaluados.TOTAL_INF.getName());
		for (String tipoCurso : titulosColumnas) {
			row.add(String.valueOf(totalInformados.get(tipoCurso)));
		}
		registroseEva.add(row);

		row = FXCollections.observableArrayList();
		row.add(EEvaluados.VALIDACION.getName());
		for (String tipoCurso : titulosColumnas) {
			total = (totalEvaluados.get(tipoCurso) / totalInformados
					.get(tipoCurso)) * 100;
			row.add(String.valueOf(Math.round(total)));
		}
		registroseEva.add(row);

		tblTotales.setItems(registroseEva);
	}

	private List<EvaluacionPrueba> listaEvaluacionesTitulos = new LinkedList<EvaluacionPrueba>();
	private Map<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos;

	private void llenarDatosTabla() {
		
		mapEvaAlumnos = new HashMap<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>>();
		for (Object object : listaPruebas) {
			if (object instanceof Prueba) {
				Prueba prueba = (Prueba) object;

				StringBuffer buffer = new StringBuffer();
				buffer.append(prueba.getAsignatura());
				buffer.append(" ");
				buffer.append(prueba.getCurso());
				lblTitulo.setText(buffer.toString());

				List<EvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();

				listaEvaluacionesTitulos.addAll(listaEvaluaciones);

				// ********** generar datos evaluaciones y totales.

				for (EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
					TipoCurso tipoCurso = evaluacionPrueba.getCurso().getTipoCurso();
					String nameTpCurso = tipoCurso.getName();

					HashMap<String, OTPreguntasEvaluacion> mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
					for (PruebaRendida pruebaRendida : evaluacionPrueba.getPruebasRendidas()) {

						
						if (tipoAlumno != Constants.PIE_ALL
								&& tipoAlumno != pruebaRendida.getAlumno().getTipoAlumno().getId()) {
							// En este caso, no se considera este alumno para el
							// cálculo.
							continue;
						}
						Float pBuenas = pruebaRendida.getPbuenas();
						for (Entry<Long, EvaluacionEjeTematico> mEvaluacion : mEvaluaciones.entrySet()) {

							EvaluacionEjeTematico evaluacionAl = mEvaluacion.getValue();
							if (mapEvaAlumnos.containsKey(evaluacionAl)) {
								HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
								if (evaluacion.containsKey(nameTpCurso)) {
									OTPreguntasEvaluacion otPreguntas = evaluacion
											.get(nameTpCurso);

									if (pBuenas >= evaluacionAl
											.getNroRangoMin()
											&& pBuenas <= evaluacionAl
													.getNroRangoMax()) {
										otPreguntas.setAlumnos(otPreguntas
												.getAlumnos() + 1);
									}
									System.out.println("name tp " + nameTpCurso
											+ " " + evaluacionAl.getName()
											+ " " + otPreguntas.getAlumnos());
								} else {

									OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
									if (pBuenas >= evaluacionAl
											.getNroRangoMin()
											&& pBuenas <= evaluacionAl
													.getNroRangoMax()) {
										pregunta.setAlumnos(1);
									} else {
										pregunta.setAlumnos(0);
									}
									pregunta.setEvaluacion(evaluacionAl);
									evaluacion.put(nameTpCurso, pregunta);
									System.out.println("name tp " + nameTpCurso
											+ " " + evaluacionAl.getName()
											+ " " + pregunta.getAlumnos());
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

								mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
								mapaOT.put(nameTpCurso, pregunta);
								mapEvaAlumnos.put(evaluacionAl, mapaOT);
								// System.out.println("name tp " + nameTpCurso
								// + " " + evaluacionAl.getName() + " "
								// + pregunta.getAlumnos());
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		// columna0.setStyle("-fx-alignment: CENTER;");
		tblEvaluaciones.getColumns().add(columna0);

		titulosColumnas = new ArrayList<>();
		int indice = 1;
		List<EvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
		for (EvaluacionPrueba evaluacion : listaEvaluaciones) {

			// Columnas
			final int col = indice;
			final String tipoCurso = evaluacion.getTipoCurso();
			if (!titulosColumnas.contains(tipoCurso)) {
				titulosColumnas.add(tipoCurso);
				TableColumn columna = new TableColumn(tipoCurso);
				columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(
							CellDataFeatures<ObservableList, String> param) {
						return new SimpleStringProperty(param.getValue()
								.get(col).toString());
					}
				});
				columna.setPrefWidth(100);
				tblEvaluaciones.getColumns().add(columna);
				indice++;
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof EvaluacionEjeTematico) {
				llegaEvaluacionEjeTematico = true;
				mEvaluaciones = new HashMap<Long, EvaluacionEjeTematico>();
				for (Object object : list) {
					EvaluacionEjeTematico eje = (EvaluacionEjeTematico) object;
					mEvaluaciones.put(eje.getId(), eje);
				}
			}
			if (entity instanceof Prueba) {
				llegaOnFound = true;
				listaPruebas = list;
			}

			if (entity instanceof TipoAlumno) {
				ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
				llegaTipoAlumno = true;
				for (Object iEntity : list) {
					tAlumnoList.add((TipoAlumno) iEntity);
				}
				cmbTipoAlumno.setItems(tAlumnoList);
				cmbTipoAlumno.getSelectionModel().select((int)Constants.PIE_ALL);
			}
			procesaDatosReporte();
		}
	}

	private void inicializarComponentes() {
		tblEvaluaciones.getColumns().clear();
		tblTotales.getColumns().clear();
		listaEvaluacionesTitulos.clear();
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
