package cl.eos.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Prueba;
import cl.eos.view.editablecells.EditingCellRespuestasEvaluar;
import cl.eos.view.ots.OTAlumnosEvaluarManual;

public class EvaluarManualPruebaView extends AFormView {

	private static final String VALID_LETTERS = "ABCDE";
	private String respsValidas = "";

	@FXML
	private TextField txtColegio;
	@FXML
	private TextField txtCurso;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private TextField txtFecha;

	@FXML
	private TableView<OTAlumnosEvaluarManual> tblListadoAlumnos;
	@FXML
	private TableColumn<OTAlumnosEvaluarManual, String> rutCol;
	@FXML
	private TableColumn<OTAlumnosEvaluarManual, String> nombresCol;
	@FXML
	private TableColumn<OTAlumnosEvaluarManual, String> paternoCol;
	@FXML
	private TableColumn<OTAlumnosEvaluarManual, String> maternoCol;
	@FXML
	private TableColumn<OTAlumnosEvaluarManual, String> respuestasCol;
	@FXML
	private Label lblCount;

	private Prueba prueba;
	private Curso curso;
	private Colegio colegio;

	@FXML
	public void initialize() {
		tblListadoAlumnos.setEditable(true);
		rutCol.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
				"rut"));
		nombresCol
				.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
						"nombres"));
		paternoCol
				.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
						"paterno"));
		maternoCol
				.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
						"materno"));
		respuestasCol
				.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
						"respuestas"));
		respuestasCol.setEditable(true);
		respuestasCol.setCellFactory(new Callback<TableColumn<OTAlumnosEvaluarManual,String>, TableCell<OTAlumnosEvaluarManual,String>>() {
			
			@Override
			public TableCell<OTAlumnosEvaluarManual, String> call(
					TableColumn<OTAlumnosEvaluarManual, String> param) {
				return new EditingCellRespuestasEvaluar(prueba);
			}
		});
		
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof Prueba) {
			prueba = (Prueba) entity;
			txtName.setText(prueba.getName());
			txtAsignatura.setText(prueba.getAsignatura().getName());
			respsValidas = VALID_LETTERS.substring(0, prueba.getAlternativas());


		} else if (entity instanceof Colegio) {
			colegio = (Colegio) entity;
			txtColegio.setText(colegio.getName());
		} else if (entity instanceof Curso) {
			curso = (Curso) entity;
			txtCurso.setText(curso.getName());

			ObservableList<OTAlumnosEvaluarManual> lstAlumnos = FXCollections
					.observableArrayList();
			for (Alumno alumno : curso.getAlumnos()) {
				lstAlumnos.add(new OTAlumnosEvaluarManual(alumno, ""));
			}
			tblListadoAlumnos.setItems(lstAlumnos);
		}
	}

	public void setFecha(String fecha) {
		txtFecha.setText(fecha);
	}
}
