package cl.eos.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;

public class AsignaturasView extends AFormView {

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private TextField txtCodAsignatura;

	@FXML
	private TextField txtNombre;

	
	public AsignaturasView() {

	}

	@FXML
	public void initialize() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String nombre = txtNombre.getText();
			
				Asignatura asignatura = new Asignatura();				
				asignatura.setName(nombre);
				controller.save(asignatura);
			}
		});
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
	}

}
