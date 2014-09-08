package cl.eos.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Profesor;

public class ProfesoresView extends AFormView {

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
	
	@FXML
	private TableView<Profesor> tblProfesores;

	public ProfesoresView() {

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
				Profesor profesor = new Profesor();
				profesor.setRut(rut);
				profesor.setName(nombres);
				profesor.setPaterno(aPaterno);
				profesor.setMaterno(aMaterno);
				controller.save(profesor);
			}
		});
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
	}

}
