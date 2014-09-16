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
import cl.eos.persistence.models.TipoPrueba;

public class TipoPruebaView extends AFormView implements
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
	private TextField txtNombre;

	@FXML
	private Label lblError;

	@FXML
	private TableView<TipoPrueba> tblTipoPrueba;

	@FXML
	private TableColumn<TipoPrueba, String> colNombre;

	public TipoPruebaView() {
		// TODO Auto-generated constructor stub
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
		tblTipoPrueba.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNombre
				.setCellValueFactory(new PropertyValueFactory<TipoPrueba, String>(
						"name"));
	}

	private void accionModificar() {
		TipoPrueba tipoPrueba = tblTipoPrueba.getSelectionModel()
				.getSelectedItem();
		if (tipoPrueba != null) {
			txtNombre.setText(tipoPrueba.getName());
		}
	}

	private void accionEliminar() {
		ObservableList<TipoPrueba> tipoPruebaSelec = tblTipoPrueba
				.getSelectionModel().getSelectedItems();
		delete(tipoPruebaSelec);
		tblTipoPrueba.getSelectionModel().clearSelection();
	}

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			TipoPrueba tipoPrueba = null;
			if (entitySelected != null && entitySelected instanceof TipoPrueba) {
				tipoPrueba = (TipoPrueba) entitySelected;
			} else {
				tipoPrueba = new TipoPrueba();
			}
			tipoPrueba.setName(txtNombre.getText());
			save(tipoPrueba);

		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}
		limpiarControles();
	}

	private void accionClicTabla() {
		tblTipoPrueba.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<TipoPrueba> itemsSelec = tblTipoPrueba
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
		int indice = tblTipoPrueba.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblTipoPrueba.getItems().remove(otObject);
			tblTipoPrueba.getItems().add(indice, (TipoPrueba) otObject);
		} else {
			tblTipoPrueba.getItems().add((TipoPrueba) otObject);
		}
	}

	@Override
	public void onDeleted(IEntity entity) {
		System.out.println("Elementoeliminando:" + entity.toString());
		tblTipoPrueba.getItems().remove(entity);
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
			if (entity instanceof TipoPrueba) {
				ObservableList<TipoPrueba> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((TipoPrueba) iEntity);
				}
				tblTipoPrueba.setItems(oList);
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
