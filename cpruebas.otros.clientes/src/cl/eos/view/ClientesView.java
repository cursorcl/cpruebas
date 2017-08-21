package cl.eos.view;

import java.util.ResourceBundle;

import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Clientes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientesView extends AFormView{
	@FXML
	private ResourceBundle resources;

	@FXML
	private TextField txtRut;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtContacto;

	@FXML
	private TextField txtDireccion;

	@FXML
	private TextField txtFonoContacto;

	@FXML
	private TextField txtEmail;

	@FXML
	private Button btnGrabar;

	@FXML
	private TextField txtAlias;

	@FXML
	private TableView<Clientes> tblClientes;

	@FXML
	private TableColumn<Clientes, String> colRut;

	@FXML
	private TableColumn<Clientes, String> colNombre;

	@FXML
	private TableColumn<Clientes, String> colContacto;

	@FXML
	private TableColumn<Clientes, String> colDireccion;

	@FXML
	private TableColumn<Clientes, String> colFono;

	@FXML
	private TableColumn<Clientes, String> colEmail;

	@FXML
	private TableColumn<Clientes, String> colFantasia;

	@FXML
	private MenuItem mnuItemEditar;

	@FXML
	private MenuItem mnuEliminar;

	@FXML
	void initialize() {
		inicializaTabla();
	}

	private void inicializaTabla() {
		tblClientes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRut.setCellValueFactory(new PropertyValueFactory<Clientes, String>("rut"));
		colNombre.setCellValueFactory(new PropertyValueFactory<Clientes, String>("name"));
		colContacto.setCellValueFactory(new PropertyValueFactory<Clientes, String>("contacto"));
		colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
		colFono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("fono"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
		colFantasia.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombrefantasia"));
	}
}
