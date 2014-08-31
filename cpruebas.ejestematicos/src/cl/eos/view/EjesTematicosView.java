package cl.eos.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EjeTematico;

public class EjesTematicosView extends AFormView {

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private TextField txtNombres;

	@FXML
	private  ComboBox<String> cmbTipoPrueba;

	@FXML
	private  ComboBox<String> cmbAsignatura;

	public EjesTematicosView() {

	}

	@FXML
	public void initialize() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String nombres = txtNombres.getText();
				String tipoPrueba = cmbTipoPrueba.getValue();
				String asignatura = cmbAsignatura.getValue();
				EjeTematico ejetematico = new EjeTematico();
				ejetematico.setName(nombres);
				ejetematico.setTipoprueba(tipoPrueba);
				ejetematico.setAsignatura(asignatura);
				controller.save(ejetematico);
			}
		});
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
	}

}
