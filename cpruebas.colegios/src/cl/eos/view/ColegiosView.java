package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;

public class ColegiosView extends AFormView {

	private static final int LARGO_CAMPO_TEXT = 100;
	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;
	
	@FXML
	private Button btnImagen;

	@FXML
	private TextField txtNombre;
	
	@FXML
	private TextField txtDireccion;

	@FXML
	private ImageView imgColegio;

	@FXML
	private TableView<Colegio> tblColegio;

	@FXML
	private TableColumn<Colegio, String> colNombre;

	@FXML
	private TableColumn<Colegio, String> colDireccion;
	
	@FXML
	private Label lblError;

	public ColegiosView() {

	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionGrabar();
		accionEliminar();
		accionModificar();
		accionClicTabla();
	}

	
	private void accionClicTabla() {
		tblColegio.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Colegio> itemsSelec = tblColegio
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
	
	private void inicializaTabla() {
		tblColegio.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colNombre.setCellValueFactory(new PropertyValueFactory<Colegio, String>(
				"name"));
	}
	private void accionModificar() {
		mnItemModificar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Colegio colegio = tblColegio.getSelectionModel().getSelectedItem();
				if (colegio != null) {
					txtNombre.setText(colegio.getName());
					txtDireccion.setText(colegio.getDireccion());
					//imgColegio.set
				}
			}
		});
	}

	private void accionEliminar() {
		mnItemEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Colegio> colegiosSelec = tblColegio
						.getSelectionModel().getSelectedItems();
				for (Colegio colegio : colegiosSelec) {
					delete(colegio);
				}
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
					Colegio colegio = null;
					if (entitySelected != null
							&& entitySelected instanceof Colegio) {
						colegio = (Colegio) entitySelected;
					} else {
						colegio = new Colegio();
					}
					colegio.setName(txtNombre.getText());
					colegio.setDireccion(txtDireccion.getText());
					colegio.setImage(null);
					
					save(colegio);
				} else {
					lblError.getStyleClass().add("bad");
					lblError.setText("Corregir campos destacados en color rojo");
				}
				limpiarControles();
//			
//				File f = new File("D:/Imagen006.jpg"); //asociamos el archivo fisico
//				InputStream is = new FileInputStream(f); //lo abrimos. Lo importante es que sea un InputStream
//				byte[] buffer = new byte[(int) f.length()]; //creamos el buffer
//				int readers = is.read(buffer); //leemos el archivo al buffer
//				i.setImagen(buffer); 
				
				
//				
	//			Obtener desde la BD.
		//		Imagen imagen=new Imagen();
//				Blob blob = rs.getBlob("imagen");
//				String nombre = rs.getObject("nombre").toString();
//				byte[] data = blob.getBytes(1, (int)blob.length());
//				BufferedImage img = null;
//				try {
//					img = ImageIO.read(new ByteArrayInputStream(data));
//				} catch (IOException ex) {
//					Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
//				}
//				imagen.setImagen(img);
//				imagen.setNombre(nombre);
//				lista.add(imagen);
			}
			
		});
	}

	private void limpiarControles() {
		txtNombre.clear();
		txtDireccion.clear();
		select(null);
	}
	
	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
		int indice = tblColegio.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblColegio.getItems().remove(otObject);
			tblColegio.getItems().add(indice, (Colegio) otObject);
		}
		else{
			tblColegio.getItems().add((Colegio) otObject);
		}
	}

	@Override
	public void onDeleted(IEntity entity) {
		System.out.println("Elementoeliminando:" + entity.toString());
		ObservableList<Colegio> asignaturas = tblColegio.getItems();
		asignaturas.remove(entity);
		tblColegio.getSelectionModel().clearSelection();
	}
	
	public boolean validate()
	{
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
		if (list != null && !list.isEmpty()) {
			IEntity entity = list.get(0);
			if (entity instanceof Asignatura) {
				ObservableList<Colegio> oList = FXCollections
						.observableArrayList();
				for (IEntity iEntity : list) {
					oList.add((Colegio) iEntity);
				}
				tblColegio.setItems(oList);
			}
		}
	}
	
	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
		removeAllStyle(txtDireccion);
	}

	public void removeAllStyle(Node n) {
		n.getStyleClass().removeAll("bad", "med", "good", "best");
		n.applyCss();
	}
	
}
