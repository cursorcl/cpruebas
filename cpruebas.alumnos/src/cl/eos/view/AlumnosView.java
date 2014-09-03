package cl.eos.view;

import com.sun.prism.paint.Color;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTAlumno;
import cl.eos.persistence.models.Alumno;
import cl.eos.util.Utils;

public class AlumnosView extends AFormView {

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private TextField txtRut;

	@FXML
	private TextField txtNombres;

	@FXML
	private TextField txtAPaterno;

	@FXML
	private TextField txtAMaterno;

	@FXML
	private TextField txtDireccion;

	@FXML
	private TableView<OTAlumno> tblAlumnos;

	@FXML
	private TableColumn<OTAlumno, String> colRut;

	@FXML
	private TableColumn<OTAlumno, String> colName;

	@FXML
	private TableColumn<OTAlumno, String> colPaterno;

	@FXML
	private TableColumn<OTAlumno, String> colMaterno;

	@FXML
	private TableColumn<OTAlumno, String> colCurso;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

	private ObservableList<OTAlumno> value;

	public AlumnosView() {

	}

	@FXML
	public void initialize() {
		inicializaTabla();

		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String rut = txtRut.getText();
				String nombres = txtNombres.getText();
				String aPaterno = txtAPaterno.getText();
				String aMaterno = txtAMaterno.getText();
				String direccion = txtDireccion.getText();

				Alumno alumno = new Alumno();
				alumno.setId(0L);
				alumno.setRut(rut);
				alumno.setName(nombres);
				alumno.setPaterno(aPaterno);
				alumno.setMaterno(aMaterno);
				alumno.setDireccion(direccion);
				save(alumno);
			}
		});

		mnItemEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<OTAlumno> itemsSelec = tblAlumnos
						.getSelectionModel().getSelectedItems();
				// for(int i = itemsSelec.size() - 1; i>=0; i--){
				// value.remove(itemsSelec.get(i));
				// }

				for (OTAlumno otAlumno : itemsSelec) {
					System.out.println("Alumno " + otAlumno.getRut());
					value.remove(otAlumno);
				}
				tblAlumnos.getSelectionModel().clearSelection();

			}
		});

		mnItemModificar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				OTAlumno alumno = tblAlumnos.getSelectionModel()
						.getSelectedItem();
				if (alumno != null) {
					txtRut.setText(alumno.getRut());
					txtNombres.setText(alumno.getName());
					txtAPaterno.setText(alumno.getAPaterno());
					txtAMaterno.setText(alumno.getAMaterno());

				}

			}
		});

		tblAlumnos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				ObservableList<OTAlumno> itemsSelec = tblAlumnos
						.getSelectionModel().getSelectedItems();
				if (itemsSelec.size() > 1) {
					mnItemModificar.setDisable(true);
				}
			}
		});
	}

	private void inicializaTabla() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRut.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>(
				"rut"));
		colName.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>(
				"name"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>(
						"aPaterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>(
						"aMaterno"));
		colCurso.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>(
				"curso"));

		value = FXCollections
				.observableArrayList(new OTAlumno("12.623.503-8", "Susan",
						"Farías", "Zavala", "AA"), new OTAlumno("12.623.502-k",
						"Ursula", "Farías", "Zavala", "AA"), new OTAlumno(
						"12.623.503-9", "Ursula", "Farías", "Zavala", "AA"));
		tblAlumnos.setItems(value);
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
	}

	@Override
	public boolean validate(IEntity otObject) {
		boolean valida = false;
		if (otObject != null) 
		{
			Alumno alumno = (Alumno) otObject;
			//Valida rut.
			valida = validaRut(alumno.getRut());
			// valida nombre			
			valida = validaNombre(alumno.getName());			
			// valida paterno			
			valida = validaAPaterno(alumno.getPaterno());
			// valida materno			
			valida = validaAMaterno(alumno.getMaterno());
		}
		return valida;
	}

	private boolean validaRut(String strRut) {
		boolean valida;
		if (strRut.length() > 0) {
			if (Utils.validarRut(strRut)) {
				valida = true;
			} else {				
				valida = false;
			}
		}
		else
		{
			valida = false;
		}
		return valida;
	}

	private boolean validaNombre(String strNombre) {
		boolean valida = true;
		if (strNombre==null || strNombre.equals(""))
		{
			valida = false;
		}
		return valida;
	}
	
	private boolean validaAPaterno(String strPaterno) {
		boolean valida = true;
		if (strPaterno==null || strPaterno.equals(""))
		{
			valida = false;
		}
		return valida;
	}
	
	private boolean validaAMaterno(String strMaterno) {
		boolean valida = true;
		if (strMaterno==null|| strMaterno.equals(""))
		{
			valida = false;
		}
		return valida;
	}
}
