package cl.eos.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;

public class HabilidadesView extends AFormView {

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private TextField txtRut;

	@FXML
	private TextField txtNombres;

	@FXML
	private TextField txtAPaterno;

	@FXML
	private TextField txtAMaterno;

	public HabilidadesView() {

	}

	@FXML
	public void initialize() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String rut = txtRut.getText();
				String nombres = txtNombres.getText();
				String aPaterno = txtAPaterno.getText();
				String aMaterno = txtAMaterno.getText();
				Alumno alumno = new Alumno();
				alumno.setId(0L);
				alumno.setRut(rut);
				alumno.setName(nombres);
				alumno.setPaterno(aPaterno);
				alumno.setMaterno(aMaterno);

				controller.save(alumno);
			}
		});
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabajdo:" + otObject.toString());
	}

}
