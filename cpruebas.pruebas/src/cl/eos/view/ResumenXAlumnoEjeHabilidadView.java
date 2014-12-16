package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;

public class ResumenXAlumnoEjeHabilidadView extends AFormView implements
		EventHandler<ActionEvent> {

	private class TituloTabla {
		EjeTematico eje;
		Habilidad hab;
		int cantidad;

		String getTitulo() {
			String resp = "";
			if (eje != null) {
				resp = eje.getName().toUpperCase();
			}
			if (hab != null) {
				resp = hab.getName().toUpperCase();
			}
			return resp;
		}
	}

	@FXML
	private TableView tblAlumnos;
	@FXML
	private LineChart<EjeTematico, Float> grfEjes;
	@FXML
	private LineChart<Habilidad, Float> grfHabilidades;

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			EvaluacionPrueba evaluacionPrueba = (EvaluacionPrueba) entity;
			List<PruebaRendida> pRendidas = evaluacionPrueba
					.getPruebasRendidas();

			if (pRendidas != null && !pRendidas.isEmpty()) {
				List<RespuestasEsperadasPrueba> respEsperadas = evaluacionPrueba
						.getPrueba().getRespuestas();
				List<TituloTabla> columnTitle = new ArrayList<TituloTabla>();
				for (RespuestasEsperadasPrueba r : respEsperadas) {
					
				}

			}

		}
	}

	/**
	 * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los
	 * cursos que estan asociados al colegio, independiente que tenga o no
	 * evaluaciones.
	 * 
	 * @param pCursoList
	 */
	@SuppressWarnings("unchecked")
	private void llenarColumnas(ObservableList<Curso> pCursoList) {
		int index = 0;
		tblAlumnos.getColumns().add(getFixedColumns("RUT", index++));
		tblAlumnos.getColumns().add(getFixedColumns("PATERNO", index++));
		tblAlumnos.getColumns().add(getFixedColumns("MATERNO", index++));
		tblAlumnos.getColumns().add(getFixedColumns("NOMBRE", index++));

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TableColumn getFixedColumns(String string, final int index) {
		TableColumn tc = new TableColumn("Nombre");
		tc.setSortable(false);
		tc.setStyle("-fx-alignment: CENTER-LEFT;");
		tc.prefWidthProperty().set(250f);
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(index)
						.toString());
			}
		});
		return tc;
	}
}
