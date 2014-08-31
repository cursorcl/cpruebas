package cl.eos.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Habilidad;

public class HabilidadesView extends AFormView {

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtDescripcion;


	public HabilidadesView() {

	}

	@FXML
	public void initialize() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String nombre = txtNombre.getText();
				String descripcion = txtDescripcion.getText();
				Habilidad habilidad = new Habilidad();
				habilidad.setName(nombre);
				habilidad.setDescripcion(descripcion);
				controller.save(habilidad);
			}
		});
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
	}

}
