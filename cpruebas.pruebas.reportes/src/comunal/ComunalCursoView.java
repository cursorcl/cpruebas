package comunal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import javafx.scene.control.Button;
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
import cl.eos.persistence.models.SAlumno;
import cl.eos.persistence.models.SEvaluacionEjeTematico;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.models.STipoColegio;
import cl.eos.persistence.models.STipoCurso;
import cl.eos.persistence.util.Comparadores;

public class ComunalCursoView extends AFormView implements EventHandler<ActionEvent> {

	@FXML
	private Label lblTitulo;
	@FXML
	private TableView<ObservableList<String>> tblEvaluaciones;
	@FXML
	private TableView<ObservableList<String>> tblTotales;

	private HashMap<Long, SEvaluacionEjeTematico> mEvaluaciones;

	private ArrayList<String> titulosColumnas;
	@FXML
	private ComboBox<STipoAlumno> cmbTipoAlumno;
	@FXML
	private Button btnGenerar;
	@FXML
	private ComboBox<STipoColegio> cmbTipoColegio;

	
	long tipoAlumno = Constants.PIE_ALL;
	long tipoColegio = Constants.TIPO_COLEGIO_ALL;

	private boolean llegaEvaluacionEjeTematico;
	private boolean llegaOnFound;
	private boolean llegaTipoAlumno = false;
	private List<Object> listaPruebas;

	public ComunalCursoView() {
		setTitle("Resumen comunal");
	}

	@FXML
	public void initialize() {
		btnGenerar.setOnAction(this);
		cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(cmbTipoAlumno.getSelectionModel() == null)
					return;
				tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
			}
		});
		
		cmbTipoColegio.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(cmbTipoColegio.getSelectionModel().getSelectedItem() == null)
					return;
				tipoColegio	= cmbTipoColegio.getSelectionModel().getSelectedItem().getId();
			}
		});
		
	}

	private void procesaDatosReporte() {
		if (llegaEvaluacionEjeTematico && llegaOnFound && llegaTipoAlumno && llegaTipoColegio) {
			inicializarComponentes();
			llenarDatosTabla();
			creacionColumnasEvaluaciones(listaEvaluacionesTitulos);
			creacionColumnasTotalesEvaluaciones(listaEvaluacionesTitulos);
			desplegarDatosEvaluaciones();
			desplegarDatosTotales();
		}
	}

	private void desplegarDatosEvaluaciones() {
		ObservableList<ObservableList<String>> registros = FXCollections.observableArrayList();

		List<SEvaluacionEjeTematico> ejes = new ArrayList<>(mapEvaAlumnos.keySet());
		Collections.sort(ejes, Comparadores.comparaEvaluacionEjeTematico());
		for(SEvaluacionEjeTematico eje: ejes)
		{
			HashMap<String, OTPreguntasEvaluacion> resultados = mapEvaAlumnos.get(eje);
			
			ObservableList<String> row = FXCollections.observableArrayList();
			row.add(eje.getName());
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

		ObservableList<ObservableList<String>> registroseEva = FXCollections.observableArrayList();
		ObservableList<String> row = FXCollections.observableArrayList();

		// Total evaluados
		float total = 0;
		for (HashMap<String, OTPreguntasEvaluacion> resultados : mapEvaAlumnos.values()) {

			for (String string : titulosColumnas) {
				OTPreguntasEvaluacion otPregunta = resultados.get(string);

				if (totalEvaluados.containsKey(string)) {
					total = otPregunta.getAlumnos() + totalEvaluados.get(string);
					totalEvaluados.replace(string, total);
				} else {
					totalEvaluados.put(string, (float) otPregunta.getAlumnos());
				}
			}
		}

		// Total informados
		for (Object objeto : listaPruebas) {
			if (objeto instanceof SPrueba) {
				SPrueba prueba = (SPrueba) objeto;
				String tipoCurso = prueba.getCurso().getName();

				List<SEvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();
				for (SEvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
					Collection<SAlumno> alumnos = evaluacionPrueba.getCurso().getAlumnos();
					int totalAlumnos = 0;
					for (SAlumno alumno : alumnos) {
						if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoAlumno().getId()) 
							continue;
						if(tipoColegio != Constants.TIPO_COLEGIO_ALL && tipoColegio !=  alumno.getColegio().getTipoColegio().getId())
							continue;
							
						totalAlumnos++;
					}
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
			row.add(String.valueOf(totalEvaluados.get(tipoCurso) == null ? 0: totalEvaluados.get(tipoCurso)));
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
			float nroEvaluados = 0f;
			if (totalEvaluados.get(tipoCurso) != null)
				nroEvaluados = totalEvaluados.get(tipoCurso);
			total = (nroEvaluados / totalInformados.get(tipoCurso)) * 100;
			row.add(String.valueOf(Math.round(total)));
		}
		registroseEva.add(row);

		tblTotales.setItems(registroseEva);
	}

	private List<SEvaluacionPrueba> listaEvaluacionesTitulos = new LinkedList<SEvaluacionPrueba>();
	private Map<SEvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos;
	private boolean llegaTipoColegio;

	private void llenarDatosTabla() {

		mapEvaAlumnos = new HashMap<SEvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>>();
		for (Object object : listaPruebas) {
			if (object instanceof SPrueba) {
				SPrueba prueba = (SPrueba) object;

				StringBuffer buffer = new StringBuffer();
				buffer.append(prueba.getAsignatura());
				buffer.append(" ");
				buffer.append(prueba.getCurso());
				lblTitulo.setText(buffer.toString());

				List<SEvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();

				listaEvaluacionesTitulos.addAll(listaEvaluaciones);

				// ********** generar datos evaluaciones y totales.

				for (SEvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
					STipoCurso tipoCurso = evaluacionPrueba.getCurso().getTipoCurso();
					String nameTpCurso = tipoCurso.getName();

					HashMap<String, OTPreguntasEvaluacion> mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
					for (SPruebaRendida pruebaRendida : evaluacionPrueba.getPruebasRendidas()) {
						SAlumno alumno = pruebaRendida.getAlumno();
						if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoAlumno().getId()) 
							continue;
						if(tipoColegio != Constants.TIPO_COLEGIO_ALL && tipoColegio !=  alumno.getColegio().getTipoColegio().getId())
							continue;
						
						
						Float pBuenas = pruebaRendida.getPbuenas();
						for (Entry<Long, SEvaluacionEjeTematico> mEvaluacion : mEvaluaciones.entrySet()) {

							SEvaluacionEjeTematico evaluacionAl = mEvaluacion.getValue();
							if (mapEvaAlumnos.containsKey(evaluacionAl)) {
								HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
								if (evaluacion.containsKey(nameTpCurso)) {
									OTPreguntasEvaluacion otPreguntas = evaluacion.get(nameTpCurso);

									if (pBuenas >= evaluacionAl.getNroRangoMin()
											&& pBuenas <= evaluacionAl.getNroRangoMax()) {
										otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
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
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void creacionColumnasEvaluaciones(List<SEvaluacionPrueba> pListaEvaluaciones) {
		TableColumn columna0 = new TableColumn("Niveles de logros");
		columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {

				return new SimpleStringProperty(param.getValue().get(0).toString());
			}
		});
		columna0.setPrefWidth(100);
		// columna0.setStyle("-fx-alignment: CENTER;");
		tblEvaluaciones.getColumns().add(columna0);

		titulosColumnas = new ArrayList<>();
		int indice = 1;
		List<SEvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
		for (SEvaluacionPrueba evaluacion : listaEvaluaciones) {

			// Columnas
			final int col = indice;
			final String tipoCurso = evaluacion.getTipoCurso();
			if (!titulosColumnas.contains(tipoCurso)) {
				titulosColumnas.add(tipoCurso);
				TableColumn columna = new TableColumn(tipoCurso);
				columna.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(col).toString());
							}
						});
				columna.setPrefWidth(100);
				tblEvaluaciones.getColumns().add(columna);
				indice++;
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void creacionColumnasTotalesEvaluaciones(List<SEvaluacionPrueba> pListaEvaluaciones) {
		TableColumn columna0 = new TableColumn("");
		columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(0).toString());
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
			columna.setCellValueFactory(
					new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
						public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
							return new SimpleStringProperty(param.getValue().get(col).toString());
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
			if (entity instanceof SEvaluacionEjeTematico) {
				llegaEvaluacionEjeTematico = true;
				mEvaluaciones = new HashMap<Long, SEvaluacionEjeTematico>();
				for (Object object : list) {
					SEvaluacionEjeTematico eje = (SEvaluacionEjeTematico) object;
					mEvaluaciones.put(eje.getId(), eje);
				}
			}
			if (entity instanceof SPrueba) {
				llegaOnFound = true;
				listaPruebas = list;
			}

			if (entity instanceof STipoAlumno) {
				ObservableList<STipoAlumno> tAlumnoList = FXCollections.observableArrayList();
				llegaTipoAlumno = true;
				for (Object iEntity : list) {
					tAlumnoList.add((STipoAlumno) iEntity);
				}
				cmbTipoAlumno.setItems(tAlumnoList);
				cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
			}
			if (entity instanceof STipoColegio) {
				ObservableList<STipoColegio> tColegioList = FXCollections.observableArrayList();
				llegaTipoColegio = true;
				for (Object iEntity : list) {
					tColegioList.add((STipoColegio) iEntity);
				}
				cmbTipoColegio.setItems(tColegioList);
				STipoColegio tColegio = new STipoColegio();
				tColegio.setId(Constants.TIPO_COLEGIO_ALL);
				cmbTipoColegio.getSelectionModel().select(tColegio);
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
		if (event.getSource() == btnGenerar) {
			procesaDatosReporte();
		}

	}

}
