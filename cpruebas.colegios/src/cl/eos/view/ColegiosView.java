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
import javafx.scene.image.ImageView;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
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
	private MenuItem btnImagen;

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
				ObservableList<Colegio> colegios = tblColegio.getItems();
				
				ObservableList<Colegio> colegiosSelec = tblColegio
						.getSelectionModel().getSelectedItems();
				for (Colegio colegio : colegiosSelec) {
					colegios.remove(colegio);
				}
				tblColegio.getSelectionModel().clearSelection();
			}
		});
	}

	private void accionGrabar() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				removeAllStyles();
				if (validate()) {
					lblError.setText(" ");
					Colegio colegio = new Colegio();				
					colegio.setName(txtNombre.getText());
					colegio.setDireccion(txtDireccion.getText());
					//TODO: Falta IMAGEN
					//colegio.setImage();
					save(colegio);
				}
				else{
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
		select(null);
	}
	
	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
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

		ObservableList<Colegio> value = FXCollections.observableArrayList();
		for (IEntity iEntity : list) {
			value.add((Colegio) iEntity);
		}
	}
	
	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
	}

	public void removeAllStyle(Node n) {
		n.getStyleClass().removeAll("bad", "med", "good", "best");
		n.applyCss();
	}
	
}
