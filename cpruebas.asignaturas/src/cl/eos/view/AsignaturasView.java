package cl.eos.view;

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
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;
import cl.eos.util.ExcelSheetWriter;

public class AsignaturasView extends AFormView implements
		EventHandler<ActionEvent> {

	private static final int LARGO_CAMPO_TEXT = 100;
	@FXML
	private MenuItem mnuAgregar;
	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnuEliminar;

	@FXML
	private MenuItem mnuModificar;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

	@FXML
	private MenuItem menuExportar;

	@FXML
	private MenuItem mnuExportar;

	@FXML
	private TextField txtNombre;

	@FXML
	private Label lblError;

	@FXML
	private TableView<Asignatura> tblAsignatura;
	@FXML
	private TableColumn<Asignatura, Long> colId;
	@FXML
	private TableColumn<Asignatura, String> colNombre;

	public AsignaturasView() {
		setTitle("Asignaturas");
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
		menuExportar.setOnAction(this);
		mnuExportar.setOnAction(this);
	}

	private void inicializaTabla() {
		tblAsignatura.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<Asignatura, Long>(
				"id"));
		colNombre
				.setCellValueFactory(new PropertyValueFactory<Asignatura, String>(
						"name"));
	}

	private void accionModificar() {
		Asignatura asignatura = tblAsignatura.getSelectionModel()
				.getSelectedItem();
		if (asignatura != null) {
			txtNombre.setText(asignatura.getName());
		}
	}

	private void accionEliminar() {
		ObservableList<Asignatura> asignaturasSelec = tblAsignatura
				.getSelectionModel().getSelectedItems();
		delete(asignaturasSelec);
		tblAsignatura.getSelectionModel().clearSelection();
	}

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			Asignatura asignatura = null;
			if (entitySelected != null && entitySelected instanceof Asignatura) {
				asignatura = (Asignatura) entitySelected;
			} else {
				asignatura = new Asignatura();
			}
			asignatura.setName(txtNombre.getText());
			save(asignatura);

		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}
		limpiarControles();
	}

	private void accionClicTabla() {
		tblAsignatura.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Asignatura> itemsSelec = tblAsignatura
						.getSelectionModel().getSelectedItems();
				if (itemsSelec.size() > 1) {
					mnItemModificar.setDisable(true);
					mnItemEliminar.setDisable(false);
				} else if (itemsSelec.size() == 1) {
					select((IEntity) itemsSelec.get(0));
					mnItemModificar.setDisable(false);
					mnItemEliminar.setDisable(false);
				}
			}
		});
	}

	private void limpiarControles() {
		txtNombre.clear();
		select(null);
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
		int indice = tblAsignatura.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblAsignatura.getItems().remove(otObject);
			tblAsignatura.getItems().add(indice, (Asignatura) otObject);
		} else {
			tblAsignatura.getItems().add((Asignatura) otObject);
		}
	}

	@Override
	public void onDeleted(IEntity entity) {
		System.out.println("Elementoeliminando:" + entity.toString());
		tblAsignatura.getItems().remove(entity);
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
	}

	@Override
	public boolean validate() {
		boolean valida = true;
		if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
			txtNombre.getStyleClass().add("bad");
			valida = false;
		}
		if (txtNombre.getText() != null
				&& txtNombre.getText().length() > LARGO_CAMPO_TEXT) {
			txtNombre.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Asignatura) {
				ObservableList<Asignatura> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Asignatura) iEntity);
				}
				tblAsignatura.setItems(oList);
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
			ExcelSheetWriter.convertirDatosALibroDeExcel(tblAsignatura);
		}
	}

}
