package cl.eos.view;

import java.util.List;

import org.controlsfx.dialog.Dialogs;

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
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Ciclo;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.util.ExcelSheetWriterEntity;

public class CursosView extends AFormView implements EventHandler<ActionEvent> {

	private static final int LARGO_CAMPO_TEXT = 100;

	@FXML
	private MenuItem mnuAgregar;

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
	private MenuItem menuExportar;

	@FXML
	private MenuItem mnuExportar;

	@FXML
	private TextField txtNombre;

	@FXML
	private ComboBox<Ciclo> cmbNivel;

	@FXML
	private ComboBox<Colegio> cmbColegio;

	@FXML
	private ComboBox<TipoCurso> cmbTipoCurso;

	@FXML
	private Label lblError;

	@FXML
	private TableView<Curso> tblCurso;

	@FXML
	private TableColumn<Curso, Long> colId;

	@FXML
	private TableColumn<Curso, String> colNombre;

	@FXML
	private TableColumn<Curso, String> colNivel;

	@FXML
	private TableColumn<Curso, String> colColegio;

	@FXML
	private TableColumn<Curso, String> colTpCurso;

	public CursosView() {
		setTitle("Cursos");
	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionClicTabla();
		mnuAgregar.setOnAction(this);
		mnuGrabar.setOnAction(this);
		mnuModificar.setOnAction(this);
		mnuEliminar.setOnAction(this);
		mnuExportar.setOnAction(this);
		mnItemEliminar.setOnAction(this);
		mnItemModificar.setOnAction(this);
		menuExportar.setOnAction(this);
		mnuExportar.setOnAction(this);

	}

	private void accionModificar() {
		Curso curso = tblCurso.getSelectionModel().getSelectedItem();
		if (curso != null) {
			txtNombre.setText(curso.getName());
			cmbNivel.setValue(curso.getCiclo());
			cmbColegio.setValue(curso.getColegio());
			cmbTipoCurso.setValue(curso.getTipoCurso());
		}
	}

	private void accionEliminar() {
		ObservableList<Curso> cursosSelec = tblCurso.getSelectionModel()
				.getSelectedItems();
		if (cursosSelec.size() == 0) {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(this.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
		} else {
			delete(cursosSelec);
			tblCurso.getSelectionModel().clearSelection();
		}
	}

	private void accionClicTabla() {
		tblCurso.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Curso> itemsSelec = tblCurso.getSelectionModel()
						.getSelectedItems();
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

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			Curso curso = null;
			if (entitySelected != null && entitySelected instanceof Curso) {
				curso = (Curso) entitySelected;
			} else {
				curso = new Curso();
			}
			curso.setName(txtNombre.getText());
			curso.setCiclo(cmbNivel.getValue());
			curso.setColegio(cmbColegio.getValue());
			curso.setTipoCurso(cmbTipoCurso.getValue());
			save(curso);
		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}
		limpiarControles();
	}

	private void inicializaTabla() {
		tblCurso.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<Curso, Long>("id"));
		colNombre.setCellValueFactory(new PropertyValueFactory<Curso, String>(
				"name"));
		colColegio.setCellValueFactory(new PropertyValueFactory<Curso, String>(
				"colegio"));
		colNivel.setCellValueFactory(new PropertyValueFactory<Curso, String>(
				"ciclo"));
		colTpCurso.setCellValueFactory(new PropertyValueFactory<Curso, String>(
				"tipoCurso"));
	}

	private void limpiarControles() {
		txtNombre.clear();
		cmbNivel.getSelectionModel().clearSelection();
		cmbColegio.getSelectionModel().clearSelection();
		tblCurso.getSelectionModel().clearSelection();
		cmbTipoCurso.getSelectionModel().clearSelection();
		select(null);
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
		removeAllStyle(cmbColegio);
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
		if (cmbColegio.getValue() == null) {
			cmbColegio.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Curso) {
				ObservableList<Curso> value = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					value.add((Curso) iEntity);
				}
				tblCurso.setItems(value);
			} else if (entity instanceof Colegio) {
				ObservableList<Colegio> value = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					value.add((Colegio) iEntity);
				}
				cmbColegio.setItems(value);
			} else if (entity instanceof Ciclo) {
				ObservableList<Ciclo> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Ciclo) iEntity);
				}
				cmbNivel.setItems(oList);
			} else if (entity instanceof TipoCurso) {
				ObservableList<TipoCurso> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((TipoCurso) iEntity);
				}
				cmbTipoCurso.setItems(oList);
			}
		}
	}

	@Override
	public void onSaved(IEntity otObject) {
		int indice = tblCurso.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblCurso.getItems().remove(otObject);
			tblCurso.getItems().add(indice, (Curso) otObject);
		} else {
			tblCurso.getItems().add((Curso) otObject);
		}
		limpiarControles();
	}

	@Override
	public void onDeleted(IEntity entity) {
		tblCurso.getItems().remove(entity);
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
			ExcelSheetWriterEntity.convertirDatosALibroDeExcel(tblCurso);
		}

	}
}
