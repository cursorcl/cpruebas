package cl.eos.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jfxtras.labs.scene.control.BigDecimalField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.TipoPrueba;

public class PruebasView extends AFormView {

	@FXML
	private TableView<Prueba> tblListadoPruebas;
	@FXML
	private TableColumn<Prueba, Date> fechaCol;
	@FXML
	private TableColumn<Prueba, String> cursoCol;
	@FXML
	private TableColumn<Prueba, String> nameCol;
	@FXML
	private TableColumn<Prueba, String> asignaturaCol;
	@FXML
	private TableColumn<Prueba, String> profesorCol;
	@FXML
	private TableColumn<Prueba, Integer> nroPreguntasCol;
	@FXML
	private TableColumn<Prueba, Integer> formasCol;
	@FXML
	private TableColumn<Prueba, Integer> alternativasCol;
	@FXML
	private ComboBox<TipoPrueba> cmbTipoPrueba;
	@FXML
	private ComboBox<Profesor> cmbProfesor;
	@FXML
	private ComboBox<Curso> cmbCurso;
	@FXML
	private ComboBox<Asignatura> cmbAsignatura;
	@FXML
	private BigDecimalField bigDecimalForma;
	@FXML
	private BigDecimalField bigDecimaNroAlternativas;
	@FXML
	private BigDecimalField bigDecimalNroPreguntas;
	@FXML
	private BigDecimalField bigDecimalPuntajePregunta;
	@FXML
	private BigDecimalField bigDecimaNivel;
	@FXML
	private Label lblError;
	@FXML
	private DatePicker dpFecha;
	@FXML
	private TextField txtName;
	@FXML
	private MenuItem mnuGrabar;

	public PruebasView() {
	}

	@FXML
	public void initialize() {
		tblListadoPruebas.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		fechaCol.setCellValueFactory(new PropertyValueFactory<Prueba, Date>(
				"fecha"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>(
				"name"));
		asignaturaCol
				.setCellValueFactory(new PropertyValueFactory<Prueba, String>(
						"asignatura"));
		profesorCol
				.setCellValueFactory(new PropertyValueFactory<Prueba, String>(
						"profesor"));
		formasCol
				.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>(
						"formas"));
		nroPreguntasCol
				.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>(
						"nroPreguntas"));
		
		bigDecimalForma.setMinValue(new BigDecimal(1));
		bigDecimalForma.setMaxValue(new BigDecimal(5));
		bigDecimalForma.setStepwidth(new BigDecimal(1));
		bigDecimalForma.setNumber(new BigDecimal(1));
		
		bigDecimaNroAlternativas.setMinValue(new BigDecimal(3));
		bigDecimaNroAlternativas.setMaxValue(new BigDecimal(5));
		bigDecimaNroAlternativas.setStepwidth(new BigDecimal(1));
		bigDecimaNroAlternativas.setNumber(new BigDecimal(3));	
		
		bigDecimalNroPreguntas.setMinValue(new BigDecimal(5));
		bigDecimalNroPreguntas.setMaxValue(new BigDecimal(90));
		bigDecimalNroPreguntas.setStepwidth(new BigDecimal(5));
		bigDecimalNroPreguntas.setNumber(new BigDecimal(5));		
		
		bigDecimalPuntajePregunta.setMinValue(new BigDecimal(1));
		bigDecimalPuntajePregunta.setMaxValue(new BigDecimal(3));
		bigDecimalPuntajePregunta.setStepwidth(new BigDecimal(1));
		bigDecimalPuntajePregunta.setNumber(new BigDecimal(1));
		
		bigDecimaNivel.setMinValue(new BigDecimal(1));
		bigDecimaNivel.setMaxValue(new BigDecimal(3));
		bigDecimaNivel.setStepwidth(new BigDecimal(1));
		bigDecimaNivel.setNumber(new BigDecimal(1));
		
		
	    mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent arg0) {
	          if (validate()) {
	            lblError.setText(" ");
	            Prueba prueba = new Prueba();
	            prueba.setAlternativas(bigDecimaNroAlternativas.getNumber().intValue());
	            prueba.setAsignatura(cmbAsignatura.getValue());
	            prueba.setCurso(cmbCurso.getValue());
	            prueba.setFecha(dpFecha.getValue().toEpochDay());
	            prueba.setFormas(bigDecimalForma.getNumber().intValue());
	            prueba.setName(txtName.getText());
	            prueba.setNivelEvaluacion(bigDecimaNivel.getNumber().intValue());
	            prueba.setProfesor(cmbProfesor.getValue());
	            prueba.setPuntajeBase(bigDecimalPuntajePregunta.getNumber().intValue());
	            prueba.setResponses(bigDecimaNroAlternativas.getNumber().intValue());
	            save(prueba);
	          } else {
	            lblError.getStyleClass().add("bad");
	            lblError.setText("Corregir campos destacados en color rojo");
	          }
	        }

	      });
	}

	@Override
	public void onDataArrived(List<IEntity> list) {

		if (list != null && !list.isEmpty()) {
			IEntity entity = list.get(0);
			if (entity instanceof Prueba) {
				ObservableList<Prueba> pruebas = FXCollections
						.observableArrayList();
				for (IEntity lEntity : list) {
					pruebas.add((Prueba) lEntity);
				}
				tblListadoPruebas.setItems(pruebas);
			}
			if(entity instanceof TipoPrueba)
			{
				ObservableList<TipoPrueba> tipoPruebas = FXCollections
						.observableArrayList();
				for (IEntity lEntity : list) {
					tipoPruebas.add((TipoPrueba) lEntity);
				}
				cmbTipoPrueba.setItems(tipoPruebas);
			}
			if(entity instanceof Profesor)
			{
				ObservableList<Profesor> profesores = FXCollections
						.observableArrayList();
				for (IEntity lEntity : list) {
					profesores.add((Profesor) lEntity);
				}
				cmbProfesor.setItems(profesores);				
			}
			if(entity instanceof Curso)
			{
				ObservableList<Curso> cursos = FXCollections
						.observableArrayList();
				for (IEntity lEntity : list) {
					cursos.add((Curso) lEntity);
				}
				cmbCurso.setItems(cursos);				
			}
			if(entity instanceof Asignatura)
			{
				ObservableList<Asignatura> asignaturas = FXCollections
						.observableArrayList();
				for (IEntity lEntity : list) {
					asignaturas.add((Asignatura) lEntity);
				}
				cmbAsignatura.setItems(asignaturas);				
			}
		}

	}

}
