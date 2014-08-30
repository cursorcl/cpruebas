package cl.eos.view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Curso;

public class CursosView extends AFormView {

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private TextField txtCodCurso;

	@FXML
	private TextField txtNombre;

	@FXML
	private ComboBox<String> cmbNivel;

	public CursosView() {

	}

	@FXML
	public void initialize() {
		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String nombres = txtNombre.getText();
				String nivel = cmbNivel.getValue();
				Curso curso = new Curso();
				curso.setName(nombres);
				curso.setNivel(nivel);
				controller.save(curso);
			}
		});
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
	}

}
