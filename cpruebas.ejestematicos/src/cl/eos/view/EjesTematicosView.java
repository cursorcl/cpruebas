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
import cl.eos.persistence.models.EjeTematico;

public class EjesTematicosView extends AFormView {

	private static final int LARGO_CAMPO_TEXT = 100;

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

	@FXML
	private TextField txtNombres;

	@FXML
	private ComboBox<String> cmbTipoPrueba;

	@FXML
	private ComboBox<String> cmbAsignatura;
	
	@FXML
	private Label lblError;

	@FXML
	private TableView<EjeTematico> tblEjesTematicos;

	@FXML
	private TableColumn<EjeTematico, String> colNombre;

	@FXML
	private TableColumn<EjeTematico, String> colTipoPrueba;

	@FXML
	private TableColumn<EjeTematico, String> colEnsayo;

	public EjesTematicosView() {

	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionGrabar();
		accionEliminar();
		accionModificar();
		accionClicTabla();
	}

	private void accionGrabar() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				removeAllStyles();
				if (validate()) {
					EjeTematico ejetematico = new EjeTematico();
					ejetematico.setName(txtNombres.getText());
					ejetematico.setTipoprueba(cmbTipoPrueba.getValue());
					ejetematico.setAsignatura(cmbAsignatura.getValue());
					save(ejetematico);
				} else {
					lblError.getStyleClass().add("bad");
					lblError.setText("Corregir campos destacados en color rojo");
				}
			}
		});
	}

	private void accionModificar() {
		mnItemModificar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EjeTematico ejeTematico = tblEjesTematicos.getSelectionModel()
						.getSelectedItem();
				if (ejeTematico != null) {
					txtNombres.setText(ejeTematico.getName());
					cmbAsignatura.setValue(ejeTematico.getAsignatura());
					cmbTipoPrueba.setValue(ejeTematico.getTipoprueba());
				}
			}
		});
	}

	private void accionEliminar() {
		mnItemEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				ObservableList<EjeTematico> ejesTematicos = tblEjesTematicos.getItems();
				ObservableList<EjeTematico> ejesTematicosSelec = tblEjesTematicos
						.getSelectionModel().getSelectedItems();

				for (EjeTematico EjeTematicoSel : ejesTematicosSelec) {
					ejesTematicos.remove(EjeTematicoSel);
				}
				tblEjesTematicos.getSelectionModel().clearSelection();
			}
		});
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombres);
		removeAllStyle(cmbAsignatura);
		removeAllStyle(cmbTipoPrueba);
	}

	public void removeAllStyle(Node n) {
		n.getStyleClass().removeAll("bad", "med", "good", "best");
		n.applyCss();
	}

	@Override
	public void onSaved(IEntity otObject) {
		limpiarControles();
		System.out.println("Elemento grabando:" + otObject.toString());
	}

	private void limpiarControles() {
		txtNombres.clear();
		cmbAsignatura.getSelectionModel().clearSelection();
		cmbTipoPrueba.getSelectionModel().clearSelection();
		select(null);
	}

	private void inicializaTabla() {
		tblEjesTematicos.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNombre
				.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>(
						"name"));
		colEnsayo
				.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>(
						"asignatura"));
		colTipoPrueba
				.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>(
						"tipoprueba"));
	}

	@Override
	public boolean validate() {
		boolean valida = true;
		if (txtNombres.getText() == null || txtNombres.getText().equals("")) {
			txtNombres.getStyleClass().add("bad");
			valida = false;
		}
		if (txtNombres.getText() != null
				&& txtNombres.getText().length() > LARGO_CAMPO_TEXT) {
			txtNombres.getStyleClass().add("bad");
			valida = false;
		}
		if (cmbAsignatura.getValue() != null) {
			cmbAsignatura.getStyleClass().add("bad");
			valida = false;
		}
		if (cmbTipoPrueba.getValue() != null) {
			cmbTipoPrueba.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}
	
	@Override
	public void onDataArrived(List<IEntity> list) {
		ObservableList<EjeTematico> value = FXCollections.observableArrayList();
		for (IEntity iEntity : list) {
			value.add((EjeTematico) iEntity);
		}
	}
	private void accionClicTabla() {
		tblEjesTematicos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<EjeTematico> itemsSelec = tblEjesTematicos
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
}
