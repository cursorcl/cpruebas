package cl.eos.view;

import com.sun.prism.paint.Color;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
	private Label lblError;

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
				
				if (validate()) {
					lblError.getStyleClass().add("good");
					lblError.setText(" ");
					Alumno alumno = new Alumno();
					alumno.setRut(txtRut.getText());
					alumno.setName(txtNombres.getText());
					alumno.setPaterno(txtAPaterno.getText());
					alumno.setMaterno(txtAMaterno.getText());
					alumno.setDireccion(txtDireccion.getText());
					save(alumno);
				}
				else{
					lblError.getStyleClass().add("bad");
					lblError.setText("Corregir campos destacados en color rojo");
				}
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
	public boolean validate() {
		boolean valida = false;
		// Valida rut.
		valida = validaRut();
		// valida nombre
		valida = validaNombre();
		// valida paterno
		valida = validaAPaterno();
		// valida materno
		valida = validaAMaterno();

		return valida;
	}

	public void removeAllStyle(Node n){
	   n.getStyleClass().removeAll("bad","med","good","best");
	}
	
	private boolean validaRut() {
		boolean valida;
		removeAllStyle(txtRut);
		String strRut = txtRut.getText();
		if (strRut .length() > 0) {
			if (Utils.validarRut(strRut)) {
				valida = true;
			} else {
				txtRut.getStyleClass().add("bad");
				valida = false;
			}
		} else {
			txtRut.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	private boolean validaNombre() {
		boolean valida = true;
		Object strNombre = txtNombres.getText();
		if (strNombre == null || strNombre.equals("")) {
			txtRut.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	private boolean validaAPaterno() {
		boolean valida = true;
		String strPaterno = txtAPaterno.getText();
		if (strPaterno == null || strPaterno.equals("")) {
			valida = false;
		}
		return valida;
	}

	private boolean validaAMaterno() {
		boolean valida = true;
		String strMaterno =txtAMaterno.getText();
		if (strMaterno == null || strMaterno.equals("")) {
			valida = false;
		}
		return valida;
	}
}
