package cl.eos.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Utils;

public class AlumnosView extends AFormView implements EventHandler<ActionEvent> {

	private static final int LARGO_CAMPO_TEXT = 100;

	@FXML
	private MenuItem mnuAgregar;
	
	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

	@FXML
	private MenuItem mnuEliminar;

	@FXML
	private MenuItem mnuModificar;
	
	@FXML
	private MenuItem menuExportar;
	
	@FXML
	private MenuItem mnuExportar;
	
	@FXML
	private MenuItem mnuImportar;

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
	private TableColumn<Alumno, Long> colId;
	
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
		setTitle("Alumnos");
		inicializaTabla();
		accionClicTabla();
		
		mnuAgregar.setOnAction(this);
		mnuGrabar.setOnAction(this);
		mnuModificar.setOnAction(this);
		mnuEliminar.setOnAction(this);
		mnItemEliminar.setOnAction(this);
		mnItemModificar.setOnAction(this);
		
		mnuExportar.setOnAction(this);
		menuExportar.setOnAction(this);
		mnuImportar.setOnAction(this);
		
		mnItemModificar.setDisable(true);
		mnuModificar.setDisable(true);
		mnuEliminar.setDisable(true);
		mnItemEliminar.setDisable(true);		
	}

	private void accionClicTabla() {
		tblAlumnos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Alumno> itemsSelec = tblAlumnos
						.getSelectionModel().getSelectedItems();

				if (itemsSelec.size() > 1) {
					mnItemModificar.setDisable(true);
					mnItemEliminar.setDisable(false);
					mnuModificar.setDisable(true);
					mnuEliminar.setDisable(false);
				} else if (itemsSelec.size() == 1) {
					select((IEntity) itemsSelec.get(0));
					mnItemModificar.setDisable(false);
					mnItemEliminar.setDisable(false);
					mnuModificar.setDisable(false);
					mnuEliminar.setDisable(false);
				}
			}
		});
	}

	private void accionModificar() {
		Alumno alumno = tblAlumnos.getSelectionModel().getSelectedItem();
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

	private void accionEliminar() {
		ObservableList<Alumno> alumnosSelec = tblAlumnos.getSelectionModel()
				.getSelectedItems();
		delete(alumnosSelec);
		
		limpiarControles();
	}

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			Alumno alumno = null;
			if (entitySelected != null && entitySelected instanceof Alumno) {
				alumno = (Alumno) entitySelected;
			} else {
				alumno = new Alumno();
			}
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
		limpiarControles();
	}

	private void limpiarControles() {
		txtRut.clear();
		txtNombres.clear();
		txtAPaterno.clear();
		txtAMaterno.clear();
		txtDireccion.clear();
		cmbColegio.getSelectionModel().clearSelection();
		cmbCurso.getSelectionModel().clearSelection();
		tblAlumnos.getSelectionModel().clearSelection();
		select(null);
	}

	private void inicializaTabla() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<Alumno, Long>(
				"id"));
		colRut.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
				"rut"));
		colName.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
				"name"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
						"paterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<Alumno, String>(
						"materno"));
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

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
		int indice = tblAlumnos.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblAlumnos.getItems().remove(otObject);
			tblAlumnos.getItems().add(indice, (Alumno) otObject);
		} else {
			tblAlumnos.getItems().add((Alumno) otObject);
		}
	}

	@Override
	public void onDeleted(IEntity entity) {
		System.out.println("Elementoeliminando:" + entity.toString());
		tblAlumnos.getItems().remove(entity);
	}

	@Override
	public boolean validate() {
		boolean valida = true;
		if (cmbColegio.getValue() == null) {
			cmbColegio.getStyleClass().add("bad");
			valida = false;
		}
		if (cmbCurso.getValue() == null) {
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
				&& txtAPaterno.getText().length() > LARGO_CAMPO_TEXT) {
			txtAPaterno.getStyleClass().add("bad");
			valida = false;
		}
		if (txtAMaterno.getText() == null || txtAMaterno.getText().equals("")) {
			txtAMaterno.getStyleClass().add("bad");
			valida = false;
		}
		if (txtAMaterno.getText() != null
				&& txtAMaterno.getText().length() > LARGO_CAMPO_TEXT) {
			txtAMaterno.getStyleClass().add("bad");
			valida = false;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		// param.put("dato", direccion);
		controller.find("Alumno.preguntita", param);
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
	public void onDataArrived(List<Object> list) {

		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Alumno) {
				ObservableList<Alumno> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Alumno) iEntity);
				}
				tblAlumnos.setItems(oList);
			} else if (entity instanceof Curso) {
				ObservableList<Curso> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Curso) iEntity);
				}
				cmbCurso.setItems(oList);
			} else if (entity instanceof Colegio) {
				ObservableList<Colegio> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Colegio) iEntity);
				}
				cmbColegio.setItems(oList);
			}
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuModificar || source == mnItemModificar) {
			accionModificar();
		} else if (source == mnuGrabar) {
			accionGrabar();
		} else if (source == mnuEliminar || source == mnItemEliminar) {
			accionEliminar();
		} else if (source == mnuAgregar ) {
			limpiarControles();
		} else if (source == mnuExportar || source == menuExportar) {
			tblAlumnos.setId("Alumnos");
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblAlumnos);
		}
		
	}
}
