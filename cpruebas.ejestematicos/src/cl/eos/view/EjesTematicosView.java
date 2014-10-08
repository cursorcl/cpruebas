package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

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

import org.controlsfx.dialog.Dialogs;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTEjeTematico;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.TipoPrueba;
import cl.eos.util.ExcelSheetWriterObj;

public class EjesTematicosView extends AFormView implements
		EventHandler<ActionEvent> {

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
	private ComboBox<TipoPrueba> cmbTipoPrueba;

	@FXML
	private ComboBox<Asignatura> cmbAsignatura;
	@FXML
	private Label lblError;

	@FXML
	private TableView<OTEjeTematico> tblEjesTematicos;

	@FXML
	private TableColumn<OTEjeTematico, Float> colId;
	@FXML
	private TableColumn<OTEjeTematico, String> colNombre;

	@FXML
	private TableColumn<OTEjeTematico, String> colTipoPrueba;

	@FXML
	private TableColumn<OTEjeTematico, String> colEnsayo;

	public EjesTematicosView() {
		setTitle("Ejes Temáticos");
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
			EjeTematico ejeTematico = null;
			if (entitySelected != null && entitySelected instanceof EjeTematico) {
				ejeTematico = (EjeTematico) entitySelected;
			} else {
				ejeTematico = new EjeTematico();
			}
			ejeTematico.setName(txtNombre.getText());
			ejeTematico.setTipoprueba(cmbTipoPrueba.getValue());
			ejeTematico.setAsignatura(cmbAsignatura.getValue());
			save(ejeTematico);
		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}
		limpiarControles();
	}

	private void accionModificar() {
		OTEjeTematico ejeTematico = tblEjesTematicos.getSelectionModel()
				.getSelectedItem();
		if (ejeTematico != null) {
			txtNombre.setText(ejeTematico.getName());
			cmbAsignatura.setValue(ejeTematico.getAsignatura());
			cmbTipoPrueba.setValue(ejeTematico.getTipoprueba());
		}
	}

	private void accionClicTabla() {
		tblEjesTematicos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<OTEjeTematico> itemsSelec = tblEjesTematicos
						.getSelectionModel().getSelectedItems();

				if (itemsSelec.size() > 1) {
					mnuModificar.setDisable(false);
					mnuEliminar.setDisable(true);

					menuModificar.setDisable(false);
					menuEliminar.setDisable(true);
				} else if (itemsSelec.size() == 1) {
					select((IEntity) itemsSelec.get(0));
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
		removeAllStyle(cmbAsignatura);
		removeAllStyle(cmbTipoPrueba);
	}

	@Override
	public void onSaved(IEntity otObject) {
		OTEjeTematico ejeTematico = new OTEjeTematico((EjeTematico) otObject);
		int indice = tblEjesTematicos.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblEjesTematicos.getItems().remove(otObject);
			tblEjesTematicos.getItems().add(indice, ejeTematico);
		} else {
			tblEjesTematicos.getItems().add(ejeTematico);
		}
		System.out.println("Elemento grabando:" + otObject.toString());
	}

	private void limpiarControles() {
		txtNombre.clear();
		cmbAsignatura.getSelectionModel().clearSelection();
		cmbTipoPrueba.getSelectionModel().clearSelection();
		select(null);
	}

	private void inicializaTabla() {
		tblEjesTematicos.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<OTEjeTematico, Float>(
				"id"));
		colNombre
				.setCellValueFactory(new PropertyValueFactory<OTEjeTematico, String>(
						"name"));
		colEnsayo
				.setCellValueFactory(new PropertyValueFactory<OTEjeTematico, String>(
						"asignatura"));
		colTipoPrueba
				.setCellValueFactory(new PropertyValueFactory<OTEjeTematico, String>(
						"tipoprueba"));
	}

	private void accionEliminar() {
		ObservableList<OTEjeTematico> otSeleccionados = tblEjesTematicos
				.getSelectionModel().getSelectedItems();
		if (otSeleccionados.size() == 0) {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(this.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
		} else {

			if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
				List<EjeTematico> ejeTematico = new ArrayList<EjeTematico>(
						otSeleccionados.size());
				for (OTEjeTematico ot : otSeleccionados) {
					ejeTematico.add(ot.getEjeTematico());
				}
				delete(ejeTematico);
				tblEjesTematicos.getSelectionModel().clearSelection();
				limpiarControles();
			}
		}
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof EjeTematico) {
				ObservableList<OTEjeTematico> value = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					value.add(new OTEjeTematico((EjeTematico) iEntity));
				}
				tblEjesTematicos.setItems(value);
				
			} else if (entity instanceof TipoPrueba) {
				ObservableList<TipoPrueba> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((TipoPrueba) iEntity);
				}
				cmbTipoPrueba.setItems(oList);
			} else if (entity instanceof Asignatura) {
				ObservableList<Asignatura> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Asignatura) iEntity);
				}
				cmbAsignatura.setItems(oList);

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
			tblEjesTematicos.setId("Ejes temáticos");
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblEjesTematicos);
		}

	}
}
