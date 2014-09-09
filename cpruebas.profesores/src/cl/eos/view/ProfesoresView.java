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
import cl.eos.persistence.models.Profesor;
import cl.eos.util.Utils;

public class ProfesoresView extends AFormView {

	private static final int LARGO_CAMPO_TEXT = 100;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

	@FXML
	private MenuItem mnuGrabar;

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
	private TableView<Profesor> tblProfesores;

	@FXML
	private TableColumn<Profesor, String> colRut;

	@FXML
	private TableColumn<Profesor, String> colPaterno;

	@FXML
	private TableColumn<Profesor, String> colMaterno;

	@FXML
	private TableColumn<Profesor, String> ColNombres;

	public ProfesoresView() {
		inicializaTabla();
		accionGrabar();
		accionEliminar();
		accionModificar();
		accionClicTabla();
	}

	private void accionClicTabla() {
		tblProfesores.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Profesor> itemsSelec = tblProfesores
						.getSelectionModel().getSelectedItems();
				if (itemsSelec.size() > 1) {
					mnItemModificar.setDisable(false);
					mnItemEliminar.setDisable(true);
				} else if (itemsSelec.size() == 1) {
					select((IEntity) itemsSelec.get(0));
					mnItemModificar.setDisable(true);
					mnItemEliminar.setDisable(true);
				}
			}
		});
	}

	private void accionModificar() {
		mnItemModificar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Profesor Profesor = tblProfesores.getSelectionModel()
						.getSelectedItem();
				if (Profesor != null) {
					txtRut.setText(Profesor.getRut());
					txtNombres.setText(Profesor.getName());
					txtAPaterno.setText(Profesor.getPaterno());
					txtAMaterno.setText(Profesor.getMaterno());
				}
			}
		});
	}

	private void accionEliminar() {
		mnItemEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Profesor> ProfesorsSelec = tblProfesores
						.getSelectionModel().getSelectedItems();
				for (Profesor ProfesorSel : ProfesorsSelec) {
					delete(ProfesorSel);
				}
				tblProfesores.getSelectionModel().clearSelection();
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
					Profesor Profesor = null;
					if (entitySelected != null
							&& entitySelected instanceof Profesor) {
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
		});
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
		colRut.setCellValueFactory(new PropertyValueFactory<Profesor, String>(
				"rut"));
		colPaterno
				.setCellValueFactory(new PropertyValueFactory<Profesor, String>(
						"paterno"));
		colMaterno
				.setCellValueFactory(new PropertyValueFactory<Profesor, String>(
						"materno"));
		ColNombres
				.setCellValueFactory(new PropertyValueFactory<Profesor, String>(
						"name"));

	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtRut);
		removeAllStyle(txtNombres);
		removeAllStyle(txtAPaterno);
		removeAllStyle(txtAMaterno);
	}

	public void removeAllStyle(Node n) {
		n.getStyleClass().removeAll("bad", "med", "good", "best");
		n.applyCss();
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
		int indice = tblProfesores.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblProfesores.getItems().remove(otObject);
			tblProfesores.getItems().add(indice, (Profesor) otObject);
		} else {
			tblProfesores.getItems().add((Profesor) otObject);
		}
	}

	@Override
	public void onDeleted(IEntity entity) {
		System.out.println("Elementoeliminando:" + entity.toString());
		ObservableList<Profesor> profesores = tblProfesores.getItems();
		profesores.remove(entity);
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
	public void onDataArrived(List<IEntity> list) {
		if (list != null && !list.isEmpty()) {
			IEntity entity = list.get(0);
			if (entity instanceof Profesor) {
				ObservableList<Profesor> oList = FXCollections
						.observableArrayList();
				for (IEntity iEntity : list) {
					oList.add((Profesor) iEntity);
				}
				tblProfesores.setItems(oList);
			}
		}
	}

}
