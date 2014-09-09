package cl.eos.view;

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
import cl.eos.persistence.models.EjeTematico;

public class EjesTematicosView extends AFormView {

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
		accionGrabar();
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
		tblEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colNombre.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>(
				"rut"));
		colEnsayo.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>(
				"name"));
		colTipoPrueba
				.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>(
						"aPaterno"));
	}

}
