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
import cl.eos.ot.OTHabilidad;
import cl.eos.persistence.models.Habilidad;
import cl.eos.util.ExcelSheetWriterObj;

public class HabilidadesView extends AFormView implements
		EventHandler<ActionEvent> {

	private static final int LARGO_CAMPO_TEXT = 100;
	@FXML
	private MenuItem mnuAgregar;

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem menuEliminar;

	@FXML
	private MenuItem menuModificar;

	@FXML
	private MenuItem mnuEliminar;

	@FXML
	private MenuItem mnuModificar;

	@FXML
	private MenuItem menuExportar;

	@FXML
	private MenuItem mnuExportar;

	@FXML
	private Label lblError;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtDescripcion;

	@FXML
	private TableView<OTHabilidad> tblHabilidades;

	@FXML
	private TableColumn<OTHabilidad, Long> colId;

	@FXML
	private TableColumn<OTHabilidad, String> colNombre;

	@FXML
	private TableColumn<OTHabilidad, String> colDescripcion;

	public HabilidadesView() {
		setTitle("Habilidades");
	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionClicTabla();
		mnuAgregar.setOnAction(this);
		mnuGrabar.setOnAction(this);
		mnuModificar.setOnAction(this);
		mnuEliminar.setOnAction(this);
		menuEliminar.setOnAction(this);
		menuModificar.setOnAction(this);
		menuExportar.setOnAction(this);
		mnuExportar.setOnAction(this);

		mnuModificar.setDisable(true);
		mnuEliminar.setDisable(true);
		menuEliminar.setDisable(true);
		menuModificar.setDisable(true);
	}

	private void inicializaTabla() {
		tblHabilidades.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<OTHabilidad, Long>(
				"id"));
		colNombre
				.setCellValueFactory(new PropertyValueFactory<OTHabilidad, String>(
						"name"));
		colDescripcion
				.setCellValueFactory(new PropertyValueFactory<OTHabilidad, String>(
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
		ObservableList<OTHabilidad> otSeleccionados = tblHabilidades
				.getSelectionModel().getSelectedItems();
		if (otSeleccionados.size() == 0) {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(this.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
		} else {

			if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
				List<Habilidad> habilidad = new ArrayList<Habilidad>(
						otSeleccionados.size());
				for (OTHabilidad ot : otSeleccionados) {
					habilidad.add(ot.getHabilidad());
				}
				delete(habilidad);
				tblHabilidades.getSelectionModel().clearSelection();
				limpiarControles();
			}
		}
	}

	private void accionModificar() {
		OTHabilidad habilidades = tblHabilidades.getSelectionModel()
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
				ObservableList<OTHabilidad> itemsSelec = tblHabilidades
						.getSelectionModel().getSelectedItems();

				if (itemsSelec.size() > 1) {
					menuModificar.setDisable(true);
					menuEliminar.setDisable(false);

					mnuModificar.setDisable(true);
					mnuEliminar.setDisable(false);

				} else if (itemsSelec.size() == 1) {
					select((IEntity) itemsSelec.get(0).getHabilidad());
					menuModificar.setDisable(false);
					menuEliminar.setDisable(false);

					mnuModificar.setDisable(false);
					mnuEliminar.setDisable(false);
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
		OTHabilidad habilidad = new OTHabilidad((Habilidad) otObject);
		int indice = tblHabilidades.getItems().lastIndexOf(habilidad);
		if (indice != -1) {
			tblHabilidades.getItems().set(indice, habilidad);
		} else {
			tblHabilidades.getItems().add(habilidad);
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
				ObservableList<OTHabilidad> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add(new OTHabilidad((Habilidad) iEntity));
				}
				tblHabilidades.setItems(oList);
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
			tblHabilidades.setId("Habilidades");
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblHabilidades);
		}
	}
}
