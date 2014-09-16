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
import cl.eos.persistence.models.Habilidad;

public class HabilidadesView extends AFormView implements
		EventHandler<ActionEvent> {

	private static final int LARGO_CAMPO_TEXT = 100;

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
	private Label lblError;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtDescripcion;

	@FXML
	private TableView<Habilidad> tblHabilidades;

	@FXML
	private TableColumn<Habilidad, String> colNombre;

	@FXML
	private TableColumn<Habilidad, String> colDescripcion;

	public HabilidadesView() {

	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionClicTabla();

		mnuGrabar.setOnAction(this);
		mnuModificar.setOnAction(this);
		mnuEliminar.setOnAction(this);
		mnItemEliminar.setOnAction(this);
		mnItemModificar.setOnAction(this);
	}

	private void inicializaTabla() {
		tblHabilidades.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNombre
				.setCellValueFactory(new PropertyValueFactory<Habilidad, String>(
						"name"));
		colDescripcion
				.setCellValueFactory(new PropertyValueFactory<Habilidad, String>(
						"descripcion"));
	}

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			Habilidad habilidad = null;
			if (entitySelected != null && entitySelected instanceof Habilidad) {
				habilidad = (Habilidad) entitySelected;
			} else {
				habilidad = new Habilidad();
			}
			habilidad.setName(txtNombre.getText());
			habilidad.setDescripcion(txtDescripcion.getText());
			save(habilidad);

		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}
		limpiarControles();
	}

	private void limpiarControles() {
		txtNombre.clear();
		txtDescripcion.clear();
		select(null);
	}

	private void accionEliminar() {
		ObservableList<Habilidad> habilidadesSelec = tblHabilidades
				.getSelectionModel().getSelectedItems();
		delete(habilidadesSelec);
		tblHabilidades.getSelectionModel().clearSelection();
	}

	private void accionModificar() {
		Habilidad habilidades = tblHabilidades.getSelectionModel()
				.getSelectedItem();
		if (habilidades != null) {
			txtNombre.setText(habilidades.getName());
			txtDescripcion.setText(habilidades.getDescripcion());
		}
	}

	private void accionClicTabla() {
		tblHabilidades.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Habilidad> itemsSelec = tblHabilidades
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

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
		removeAllStyle(txtDescripcion);
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
		int indice = tblHabilidades.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblHabilidades.getItems().remove(otObject);
			tblHabilidades.getItems().add(indice, (Habilidad) otObject);
		} else {
			tblHabilidades.getItems().add((Habilidad) otObject);
		}

	}

	@Override
	public void onDeleted(IEntity entity) {
		System.out.println("Elementoeliminando:" + entity.toString());
		tblHabilidades.getItems().remove(entity);
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
		if (txtDescripcion.getText() == null || txtNombre.getText().equals("")) {
			txtDescripcion.getStyleClass().add("bad");
			valida = false;
		}
		if (txtDescripcion.getText() != null
				&& txtDescripcion.getText().length() > LARGO_CAMPO_TEXT) {
			txtDescripcion.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Habilidad) {
				ObservableList<Habilidad> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Habilidad) iEntity);
				}
				tblHabilidades.setItems(oList);
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
		}
	}
}
