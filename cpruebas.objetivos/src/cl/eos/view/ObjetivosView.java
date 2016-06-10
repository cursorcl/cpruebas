package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Objetivo;
import cl.eos.util.ExcelSheetWriterObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ObjetivosView extends AFormView implements EventHandler<ActionEvent> {

	@FXML
	private MenuItem mnuAgregar;

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnuEliminar;

	@FXML
	private MenuItem mnuModificar;

	@FXML
	private MenuItem menuEliminar;

	@FXML
	private MenuItem menuModificar;

	@FXML
	private MenuItem menuExportar;

	@FXML
	private MenuItem mnuExportar;

	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtDescripcion;

	@FXML
	private Label lblError;

	@FXML
	private TableView<Objetivo> tblObjetivos;

	@FXML
	private TableColumn<Objetivo, Float> colId;
	@FXML
	private TableColumn<Objetivo, String> colNombre;

	@FXML
	private TableColumn<Objetivo, String> colDescripcion;

	public ObjetivosView() {
		setTitle("Objetivos");
	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionClicTabla();

		mnuAgregar.setOnAction(this);
		mnuGrabar.setOnAction(this);
		mnuModificar.setOnAction(this);
		mnuEliminar.setOnAction(this);
		menuModificar.setOnAction(this);
		menuEliminar.setOnAction(this);
		menuExportar.setOnAction(this);
		mnuExportar.setOnAction(this);

		mnuModificar.setDisable(true);
		mnuEliminar.setDisable(true);
		menuEliminar.setDisable(true);
		menuModificar.setDisable(true);

	}

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			Objetivo objetivo = null;
			if (entitySelected != null && entitySelected instanceof Objetivo) {
				objetivo = (Objetivo) entitySelected;
			} else {
				objetivo = new Objetivo();
			}
			objetivo.setName(txtNombre.getText());
			objetivo.setDescripcion(txtDescripcion.getText());
			save(objetivo);
			limpiarControles();
		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}

	}

	private void accionModificar() {
		Objetivo objetivo = tblObjetivos.getSelectionModel().getSelectedItem();
		if (objetivo != null) {
			txtNombre.setText(objetivo.getName());
			txtDescripcion.setText(objetivo.getDescripcion());
			select(objetivo);
		}
	}

	private void accionClicTabla() {
		tblObjetivos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Objetivo> itemsSelec = tblObjetivos.getSelectionModel().getSelectedItems();

				if (itemsSelec.size() > 1) {
					mnuModificar.setDisable(true);
					mnuEliminar.setDisable(false);

					menuModificar.setDisable(true);
					menuEliminar.setDisable(false);
				} else if (itemsSelec.size() == 1) {

					mnuModificar.setDisable(false);
					mnuEliminar.setDisable(false);

					menuModificar.setDisable(false);
					menuEliminar.setDisable(false);
				}
			}
		});
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
		removeAllStyle(txtDescripcion);
	}

	@Override
	public void onSaved(IEntity otObject) {
		Objetivo objetivo = (Objetivo) otObject;
		int indice = tblObjetivos.getItems().lastIndexOf(objetivo);
		if (indice != -1) {
			tblObjetivos.getItems().set(indice, objetivo);
		} else {
			tblObjetivos.getItems().add(objetivo);
		}
	}

	private void limpiarControles() {
		txtNombre.clear();
		txtDescripcion.clear();
		select(null);
	}

	private void inicializaTabla() {
		tblObjetivos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<Objetivo, Float>("id"));
		colNombre.setCellValueFactory(new PropertyValueFactory<Objetivo, String>("name"));
		colDescripcion.setCellValueFactory(new PropertyValueFactory<Objetivo, String>("descripcion"));
	}

	private void accionEliminar() {
		ObservableList<Objetivo> otSeleccionados = tblObjetivos.getSelectionModel().getSelectedItems();
		if (otSeleccionados.size() == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selección registro");
			alert.setHeaderText(this.getName());
			alert.setContentText("Debe seleccionar registro a procesar");
			alert.showAndWait();
		} else {

			if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
				List<Objetivo> objAborrar = new ArrayList<Objetivo>(otSeleccionados.size());
				for (Objetivo ot : otSeleccionados) {
					objAborrar.add(ot);
				}
				delete(objAborrar);
				tblObjetivos.getSelectionModel().clearSelection();
				limpiarControles();
			}
		}
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Objetivo) {
				ObservableList<Objetivo> value = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					value.add((Objetivo) iEntity);
				}
				tblObjetivos.setItems(value);
			}
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuAgregar) {
			limpiarControles();
		} else if (source == mnuModificar || source == menuModificar) {
			accionModificar();
		} else if (source == mnuGrabar) {
			accionGrabar();
		} else if (source == mnuEliminar || source == menuEliminar) {
			accionEliminar();
		} else if (source == mnuExportar || source == menuExportar) {
			tblObjetivos.setId("Ejes temáticos");
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblObjetivos);
		}

	}

	@Override
	public void onDeleted(IEntity entity) {
		tblObjetivos.getItems().remove((Objetivo) entity);
	}
}
