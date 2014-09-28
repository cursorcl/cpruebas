package cl.eos.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.TipoCurso;

public class ComunalCursoEjeTematico extends AFormView implements
		EventHandler<ActionEvent> {

	@FXML
	private Label lblTitulo;
	private boolean llegaOnDataArrived;
	private boolean llegaOnFound;
	@FXML
	private TableView tblEjesTematicos;
	@FXML
	private TableView tblEvaluacionEjesTematicos;
	
	private HashMap<Long, EvaluacionEjeTematico> mEvaluaciones;
	private Map<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;
	private HashMap<EjeTematico, HashMap<String, OTPreguntasEjes>> mapaEjesTematicos;
	private ArrayList<String> titulosColumnas;;


	

	public ComunalCursoEjeTematico() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof Prueba) {
			Prueba prueba = (Prueba) entity;
			llegaOnFound = true;
			llenarDatosTabla(prueba);
			desplegarDatosEjesTematicos();
			desplegarDatosEvaluaciones();
		}
	}

	private void desplegarDatosEvaluaciones() {
		// TODO Auto-generated method stub

	}

	private void desplegarDatosEjesTematicos() {
		// TODO Auto-generated method stub

	}

	private void llenarDatosTabla(Prueba prueba) {

		if (llegaOnFound && llegaOnDataArrived) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(prueba.getAsignatura());
			buffer.append(" ");
			buffer.append(prueba.getCurso());
			lblTitulo.setText(buffer.toString());
			
			mapaEjesTematicos = new HashMap<EjeTematico, HashMap<String, OTPreguntasEjes>>();
			mapEvaAlumnos = new HashMap<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>>();
			HashMap<String, OTPreguntasHabilidad> mapaColegios = null;

			List<EvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();

			creacionColumnasEjesTematicos(listaEvaluaciones);
			creacionColumnasEvaluaciones(listaEvaluaciones);

			// ********** generar datos ejes tematicos y evaluaciones
			for (EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
				TipoCurso tipoCurso = evaluacionPrueba.getCurso().getTipoCurso();
				
			}
			
		}
	}

	
	private void creacionColumnasEjesTematicos(
			List<EvaluacionPrueba> pListaEvaluaciones) {
		TableColumn columna0 = new TableColumn("Eje Temático");
		columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {

				return new SimpleStringProperty(param.getValue().get(0)
						.toString());
			}
		});
		columna0.setPrefWidth(100);
		tblEjesTematicos.getColumns().add(columna0);

		titulosColumnas = new ArrayList<>();
		int indice = 1;
		List<EvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
		for (EvaluacionPrueba evaluacion : listaEvaluaciones) {
			// Columnas
			final int col = indice;
			final String colegioCurso = evaluacion.getColegiocurso();
			titulosColumnas.add(colegioCurso);
			TableColumn columna = new TableColumn(colegioCurso);
			columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(
						CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(col)
							.toString());
				}
			});
			columna.setPrefWidth(100);
			tblEjesTematicos.getColumns().add(columna);
			indice++;
		}
	}

	private void creacionColumnasEvaluaciones(
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
		tblEjesTematicos.getColumns().add(columna0);

		int indice = 1;
		for (String evaluacion : titulosColumnas) {

			// Columnas
			final int col = indice;
			final String colegioCurso = evaluacion;
			TableColumn columna = new TableColumn(colegioCurso);
			columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(
						CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(col)
							.toString());
				}
			});
			columna.setPrefWidth(100);
			tblEjesTematicos.getColumns().add(columna);
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
		}
	}

}
