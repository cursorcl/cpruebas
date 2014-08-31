package cl.eos.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Colegio;

public class ColegiosView extends AFormView {

	@FXML
	private MenuItem mnuGrabar;
	
	@FXML
	private MenuItem btnImagen;

	@FXML
	private TextField txtNombre;

	@FXML
	private ImageView imgColegio;


	public ColegiosView() {

	}

	@FXML
	public void initialize() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String nombre = txtNombre.getText();
				
				Colegio colegio = new Colegio();
				colegio.setName(nombre);
				
//				colegio.setImage(imgColegio.getImage());
//				
//				File f = new File("D:/Imagen006.jpg"); //asociamos el archivo fisico
//				InputStream is = new FileInputStream(f); //lo abrimos. Lo importante es que sea un InputStream
//				byte[] buffer = new byte[(int) f.length()]; //creamos el buffer
//				int readers = is.read(buffer); //leemos el archivo al buffer
//				i.setImagen(buffer); 
				
				
				controller.save(colegio);
				
				
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

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabajdo:" + otObject.toString());
	}

}
