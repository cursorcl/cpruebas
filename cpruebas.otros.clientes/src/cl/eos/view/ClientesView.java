package cl.eos.view;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import cl.eos.clone.Migrator;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Clientes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientesView extends AFormView {
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
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnuEliminar;

	@FXML
	private MenuItem mnuItemEditar;

	@FXML
	private MenuItem mnuItemEliminar;

	@FXML
	void initialize() {
		setTitle("Clientes");
		inicializaTabla();

	}

	private void inicializaTabla() {
		tblClientes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRut.setCellValueFactory(new PropertyValueFactory<Clientes, String>("rut"));
		colNombre.setCellValueFactory(new PropertyValueFactory<Clientes, String>("name"));
		colContacto.setCellValueFactory(new PropertyValueFactory<Clientes, String>("contacto"));
		colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
		colFono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("fono"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correo"));
		colFantasia.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombrefantasia"));
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list == null || list.isEmpty())
			return;

		final Object entity = list.get(0);
		if (entity instanceof Clientes) {
			List<Clientes> lst = list.stream().map(o -> (Clientes) o).collect(Collectors.toList());
			final ObservableList<Clientes> pruebas = FXCollections.observableArrayList(lst);
			tblClientes.setItems(pruebas);
		}
		mnuGrabar.setOnAction(e -> grabar());
		mnuItemEditar.setOnAction(e -> editar());
		mnuEliminar.setOnAction(e -> eliminar());
		mnuItemEliminar.setOnAction(e -> eliminar());
	}

	@Override
	public void onSaved(IEntity entity) {
		if (entity instanceof Clientes) {
			int idx = tblClientes.getItems().indexOf(entity);
			if (idx == -1) {
				tblClientes.getItems().add((Clientes) entity);
			} else {
				tblClientes.getItems().set(idx, (Clientes) entity);
			}

		}
	}

	private void eliminar() {
		Clientes c = tblClientes.getSelectionModel().getSelectedItem();
		if (c == null)
			return;
		String name = "cpr_" + c.getNombrefantasia();
		if (!Migrator.exists(name)) {
			JOptionPane.showMessageDialog(null, "La base de datos " + name + " no existe.",
					"Base de datos no existente", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (Migrator.deleteClient(name) != 0) {
			JOptionPane.showMessageDialog(null, "La base de datos " + name + " no pudo eliminarse.",
					"Base de datos no se puede eliminar", JOptionPane.ERROR_MESSAGE);
			return;

		}
		delete(c);
		txtAlias.setDisable(false);
	}

	private void editar() {
		Clientes c = tblClientes.getSelectionModel().getSelectedItem();
		if (c == null)
			return;
		txtRut.setText(c.getRut());
		txtNombre.setText(c.getName());
		txtContacto.setText(c.getContacto());
		txtDireccion.setText(c.getDireccion());
		txtFonoContacto.setText(c.getFono());
		txtEmail.setText(c.getCorreo());
		txtAlias.setText(c.getNombrefantasia());
		txtAlias.setDisable(true);
	}

	private void grabar() {

		// Se procede a crear la BD
		String name = "cpr_" + txtAlias.getText();
		if (Migrator.exists(name)) {
			JOptionPane.showMessageDialog(null, "Existe base de datos con el mismo nombre", "Base de datos existente",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (Migrator.createClient(getName()) != 0) {
			JOptionPane.showMessageDialog(null, "No se ha podido crear la base de datos",
					"Error al crear base de datos", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Clientes c = new Clientes();
		c.setContacto(txtContacto.getText());
		c.setCorreo(txtEmail.getText());
		c.setDireccion(txtDireccion.getText());
		c.setFono(txtFonoContacto.getText());
		c.setName(txtNombre.getText());
		c.setNombrefantasia(txtAlias.getText());
		c.setRut(txtRut.getText());
		txtAlias.setDisable(false);
		save(c);
	}

}
