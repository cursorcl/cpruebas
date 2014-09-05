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
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Curso;

public class CursosView extends AFormView {

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
	private ComboBox<String> cmbNivel;

	@FXML
	private Label lblError;

	@FXML
	private TableView<Curso> tblCurso;

	@FXML
	private TableColumn<Curso, String> colNombre;

	@FXML
	private TableColumn<Curso, String> colNivel;

	public CursosView() {

	}

	@FXML
	public void initialize() {
		inicializaTabla();
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				removeAllStyles();
				if (validate()) {
					lblError.setText(" ");
					Curso curso = new Curso();
					curso.setName(txtNombre.getText());
					curso.setNivel(cmbNivel.getValue());
					save(curso);
				} else {
					lblError.getStyleClass().add("bad");
					lblError.setText("Corregir campos destacados en color rojo");
				}
			}
		});

		mnItemEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Curso> cursos = tblCurso.getItems();
				ObservableList<Curso> cursosSelec = tblCurso.getSelectionModel()
						.getSelectedItems();

				for (Curso curso : cursosSelec) {
					System.out.println("Asignatura " + curso.getName());
					cursos.remove(curso);
				}
				tblCurso.getSelectionModel().clearSelection();
			}
		});

		mnItemModificar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Curso curso = tblCurso.getSelectionModel().getSelectedItem();
				if (curso != null) {
					txtNombre.setText(curso.getName());
					cmbNivel.setValue(curso.getNivel());
				}
			}
		});
	}

	private void inicializaTabla() {
		tblCurso.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colNombre.setCellValueFactory(new PropertyValueFactory<Curso, String>(
				"name"));
		colNivel.setCellValueFactory(new PropertyValueFactory<Curso, String>(
				"nivel"));
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
	}

	public void removeAllStyle(Node n) {
		n.getStyleClass().removeAll("bad", "med", "good", "best");
		n.applyCss();
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

		ObservableList<Curso> value = FXCollections.observableArrayList();
		for (IEntity iEntity : list) {
			value.add((Curso) iEntity);
		}
		tblCurso.setItems(value);
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
	}

}
