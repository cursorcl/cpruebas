package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.util.Utils;

public class AlumnosView extends AFormView {

	private static final int LARGO_CAMPO_TEXT = 100;

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

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
	private ComboBox<Colegio> cmbColegio;

	@FXML
	private ComboBox<Curso> cmbCurso;

	@FXML
	private Label lblError;

	@FXML
	private TableView<Alumno> tblAlumnos;
 
	@FXML
	private TableColumn<Alumno, String> colRut;

	@FXML
	private TableColumn<Alumno, String> colName;

	@FXML
	private TableColumn<Alumno, String> colPaterno;

	@FXML
	private TableColumn<Alumno, String> colMaterno;

	@FXML
	private TableColumn<Alumno, String> colCurso;

	public AlumnosView() {

	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionGrabar();
		accionEliminar();
		accionModificar();
		accionClicTabla();
	}

	private void accionClicTabla() {
		tblAlumnos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Alumno> itemsSelec = tblAlumnos
						.getSelectionModel().getSelectedItems();
				
				if (itemsSelec.size() > 1) {
					mnItemModificar.setDisable(true);
				}
				else{
					select((IEntity) itemsSelec);
					mnItemModificar.setDisable(false);
				}
			}
		});
	}

	private void accionModificar() {
		mnItemModificar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Alumno alumno = tblAlumnos.getSelectionModel()
						.getSelectedItem();
				if (alumno != null) {
					txtRut.setText(alumno.getRut());
					txtNombres.setText(alumno.getName());
					txtAPaterno.setText(alumno.getPaterno());
					txtAMaterno.setText(alumno.getMaterno());
					txtDireccion.setText(alumno.getDireccion());
					cmbColegio.setValue(alumno.getColegio());
					cmbCurso.setValue(alumno.getCurso());
				}
			}
		});
	}

	private void accionEliminar() {
		mnItemEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				ObservableList<Alumno> alumnos = tblAlumnos.getItems();
				ObservableList<Alumno> alumnosSelec = tblAlumnos
						.getSelectionModel().getSelectedItems();

				for (Alumno alumnoSel : alumnosSelec) {
					alumnos.remove(alumnoSel);
				}
				tblAlumnos.getSelectionModel().clearSelection();
			}
		});
	}

	private void accionGrabar() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				removeAllStyles();
				if (validate()) {
					lblError.setText(" ");
					Alumno alumno = new Alumno();
					alumno.setRut(txtRut.getText());
					alumno.setName(txtNombres.getText());
					alumno.setPaterno(txtAPaterno.getText());
					alumno.setMaterno(txtAMaterno.getText());
					alumno.setDireccion(txtDireccion.getText());
					alumno.setColegio(cmbColegio.getValue());
					alumno.setCurso(cmbCurso.getValue());
					save(alumno);
				} else {
					lblError.getStyleClass().add("bad");
					lblError.setText("Corregir campos destacados en color rojo");
				}
			}

		});
	}

	private void limpiarControles() {
		txtRut.clear();
		txtNombres.clear();
		txtAPaterno.clear();
		txtAMaterno.clear();
		txtDireccion.clear();
		cmbColegio.getSelectionModel().clearSelection();
		cmbCurso.getSelectionModel().clearSelection();
		select(null);
	}

	private void inicializaTabla() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRut.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
				"rut"));
		colName.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
				"name"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
						"aPaterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
						"aMaterno"));
		colCurso.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
				"curso"));

	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtRut);
		removeAllStyle(txtNombres);
		removeAllStyle(txtAPaterno);
		removeAllStyle(txtAMaterno);
		removeAllStyle(cmbColegio);
		removeAllStyle(cmbCurso);
	}
	
	public void removeAllStyle(Node n) {
		n.getStyleClass().removeAll("bad", "med", "good", "best");
		n.applyCss();
	}
	
	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
		limpiarControles();
	}

	@Override
	public boolean validate() {
		boolean valida = true;
		if (cmbColegio.getValue() != null) {
			cmbColegio.getStyleClass().add("bad");
			valida = false;
		}
		if (cmbCurso.getValue() != null) {
			cmbCurso.getStyleClass().add("bad");
			valida = false;
		}
		valida = validaRut();
		if (txtNombres.getText() == null || txtNombres.getText().equals("")) {
			txtNombres.getStyleClass().add("bad");
			valida = false;
		}
		if (txtNombres.getText() != null
				&& txtNombres.getText().length() > LARGO_CAMPO_TEXT) {
			txtNombres.getStyleClass().add("bad");
			valida = false;
		}

		if (txtAPaterno.getText() == null || txtAPaterno.getText().equals("")) {
			txtAPaterno.getStyleClass().add("bad");
			valida = false;
		}
		if (txtAPaterno.getText() != null
				|| txtAPaterno.getText().length() > LARGO_CAMPO_TEXT) {
			txtAPaterno.getStyleClass().add("bad");
			valida = false;
		}
		if (txtAMaterno.getText() == null || txtAMaterno.getText().equals("")) {
			txtAMaterno.getStyleClass().add("bad");
			valida = false;
		}
		if (txtAMaterno.getText() != null
				|| txtAMaterno.getText().length() > LARGO_CAMPO_TEXT) {
			txtAMaterno.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	private boolean validaRut() {
		boolean valida;
		String strRut = txtRut.getText();
		if (strRut.length() > 0) {
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

	@Override
	public void onDataArrived(List<IEntity> list) {
		ObservableList<Alumno> value = FXCollections.observableArrayList();
		for (IEntity iEntity : list) {
			value.add((Alumno) iEntity);
		}
	}
}
