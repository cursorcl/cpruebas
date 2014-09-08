package cl.eos.view;

import java.util.List;

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
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;

public class AsignaturasView extends AFormView {

	private static final int LARGO_CAMPO_TEXT = 100;

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

	@FXML
	private TextField txtNombre;

	@FXML
	private Label lblError;

	@FXML
	private TableView<Asignatura> tblAsignatura;

	@FXML
	private TableColumn<Asignatura, String> colNombre;

	public AsignaturasView() {

	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionGrabar();
		accionEliminar();
		accionModificar();
		accionClicTabla();
	}

	private void inicializaTabla() {
		tblAsignatura.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNombre
				.setCellValueFactory(new PropertyValueFactory<Asignatura, String>(
						"name"));
	}

	private void accionModificar() {
		mnItemModificar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Asignatura asignatura = tblAsignatura.getSelectionModel()
						.getSelectedItem();
				if (asignatura != null) {
					txtNombre.setText(asignatura.getName());
				}
			}
		});
	}

	private void accionEliminar() {
		mnItemEliminar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ObservableList<Asignatura> asignaturasSelec = tblAsignatura
						.getSelectionModel().getSelectedItems();
				for (Asignatura asignaturaSel : asignaturasSelec) {
					delete(asignaturaSel);
				}

			}
		});
	}

	private void accionGrabar() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				IEntity entitySelected = getSelectedEntity();
				removeAllStyles();
				if (validate()) {
					if (lblError != null) {
						lblError.setText(" ");
					}
					Asignatura asignatura = null;
					if (entitySelected != null
							&& entitySelected instanceof Asignatura) {
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
		});
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
		ObservableList<Asignatura> asignaturas = tblAsignatura.getItems();
		asignaturas.remove(entity);
		tblAsignatura.getSelectionModel().clearSelection();
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
	}

	public void removeAllStyle(Node n) {
		if (n != null) {
			n.getStyleClass().removeAll("bad", "med", "good", "best");
			n.applyCss();
		}
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
	public void onDataArrived(List<IEntity> list) {
		if (list != null && !list.isEmpty()) {
			IEntity entity = list.get(0);
			if (entity instanceof Asignatura) {
				ObservableList<Asignatura> oList = FXCollections
						.observableArrayList();
				for (IEntity iEntity : list) {
					oList.add((Asignatura) iEntity);
				}
				tblAsignatura.setItems(oList);
			}
		}
	}

}
