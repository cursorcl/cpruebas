package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import org.controlsfx.dialog.Dialogs;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTProfesor;
import cl.eos.persistence.models.Profesor;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Utils;

public class ProfesoresView extends AFormView implements
		EventHandler<ActionEvent> {

	private static final int LARGO_CAMPO_TEXT = 100;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

	@FXML
	private MenuItem mnuAgregar;

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnuEliminar;

	@FXML
	private MenuItem mnuModificar;

	@FXML
	private MenuItem menuExportar;

	@FXML
	private MenuItem mnuExportar;

	@FXML
	private TextField txtRut;

	@FXML
	private TextField txtNombres;

	@FXML
	private TextField txtAPaterno;

	@FXML
	private TextField txtAMaterno;

	@FXML
	private Label lblError;

	@FXML
	private TableView<OTProfesor> tblProfesores;

	@FXML
	private TableColumn<OTProfesor, Long> colId;

	@FXML
	private TableColumn<OTProfesor, String> colRut;

	@FXML
	private TableColumn<OTProfesor, String> colPaterno;

	@FXML
	private TableColumn<OTProfesor, String> colMaterno;

	@FXML
	private TableColumn<OTProfesor, String> ColNombres;

	public ProfesoresView() {
		setTitle("Profesores");
	}

	@FXML
	public void initialize() {
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

		mnuModificar.setDisable(true);
		mnuEliminar.setDisable(true);
		mnItemEliminar.setDisable(true);
		mnItemModificar.setDisable(true);
	}

	private void accionClicTabla() {
		tblProfesores.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<OTProfesor> itemsSelec = tblProfesores
						.getSelectionModel().getSelectedItems();
				if (itemsSelec.size() > 1) {
					mnItemModificar.setDisable(true);
					mnItemEliminar.setDisable(false);

					mnuModificar.setDisable(true);
					mnuEliminar.setDisable(false);

				} else if (itemsSelec.size() == 1) {
					
					mnItemModificar.setDisable(false);
					mnItemEliminar.setDisable(false);

					mnuModificar.setDisable(false);
					mnuEliminar.setDisable(false);
				}
			}
		});
	}

	private void accionModificar() {
		OTProfesor profesor = tblProfesores.getSelectionModel()
				.getSelectedItem();
		if (profesor != null) {
			txtRut.setText(profesor.getRut());
			txtNombres.setText(profesor.getName());
			txtAPaterno.setText(profesor.getPaterno());
			txtAMaterno.setText(profesor.getMaterno());
			select((IEntity) profesor.getProfesor());
		}
	}

	private void accionEliminar() {
		ObservableList<OTProfesor> otSeleccionados = tblProfesores
				.getSelectionModel().getSelectedItems();
		if (otSeleccionados.size() == 0) {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(this.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
		} else {

			if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
				List<Profesor> profesor = new ArrayList<Profesor>(
						otSeleccionados.size());
				for (OTProfesor ot : otSeleccionados) {
					profesor.add(ot.getProfesor());
				}
				delete(profesor);
				tblProfesores.getSelectionModel().clearSelection();
				limpiarControles();
			}
		}
	}

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			Profesor Profesor = null;
			if (entitySelected != null && entitySelected instanceof Profesor) {
				Profesor = (Profesor) entitySelected;
			} else {
				Profesor = new Profesor();
			}
			Profesor.setRut(txtRut.getText());
			Profesor.setName(txtNombres.getText());
			Profesor.setPaterno(txtAPaterno.getText());
			Profesor.setMaterno(txtAMaterno.getText());
			save(Profesor);
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
		select(null);
	}

	private void inicializaTabla() {
		tblProfesores.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<OTProfesor, Long>(
				"id"));
		colRut.setCellValueFactory(new PropertyValueFactory<OTProfesor, String>(
				"rut"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<OTProfesor, String>(
						"paterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<OTProfesor, String>(
						"materno"));
		ColNombres
				.setCellValueFactory(new PropertyValueFactory<OTProfesor, String>(
						"name"));
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtRut);
		removeAllStyle(txtNombres);
		removeAllStyle(txtAPaterno);
		removeAllStyle(txtAMaterno);
	}

	@Override
	public void onSaved(IEntity otObject) {
		OTProfesor profesor = new OTProfesor((Profesor) otObject);
		int indice = tblProfesores.getItems().lastIndexOf(profesor);
		if (indice != -1) {
			tblProfesores.getItems().set(indice, profesor);
		} else {
			tblProfesores.getItems().add(profesor);
		}
	}

	@Override
	public void onDeleted(IEntity entity) {
		tblProfesores.getItems().remove(new OTProfesor((Profesor) entity));
	}

	@Override
	public boolean validate() {
		boolean valida = true;
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
			if (entity instanceof Profesor) {
				ObservableList<OTProfesor> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add(new OTProfesor((Profesor) iEntity));
				}
				tblProfesores.setItems(oList);
			}
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuAgregar) {
			limpiarControles();
		} else if (source == mnuModificar || source == mnItemModificar) {
			accionModificar();
		} else if (source == mnuGrabar) {
			accionGrabar();
		} else if (source == mnuEliminar || source == mnItemEliminar) {
			accionEliminar();
		} else if (source == mnuExportar || source == menuExportar) {
			tblProfesores.setId("Profesores");
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblProfesores);
		}
	}
}
