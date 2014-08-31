package cl.eos.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.util.Utils;

public class AlumnosView extends AFormView {

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

	public AlumnosView() {
		
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
		System.out.println("Elemento grabando:" + otObject.toString());
	}

	@Override
	public boolean validate(IEntity otObject) {
		System.out.println("Validando");
		boolean valida = true;
		if (otObject != null) {
			Alumno alumno = (Alumno) otObject;
			String strRut = alumno.getRut();
			if (strRut.length() > 0) {
				// Creamos un arreglo con el rut y el digito verificador
				String[] rut_dv = strRut.split("-");
				// Las partes del rut (numero y dv) deben tener una longitud
				// positiva
				if (rut_dv.length == 2) {
					int rut = Integer.parseInt(rut_dv[0]);
					char dv = rut_dv[1].charAt(0);

					if (Utils.validarRut(rut, dv)) {
					//	JOptionPane.showMessageDialog(rootPane, "Rut correcto");
					} else {
						//JOptionPane.showMessageDialog(rootPane,						"Rut incorrecto");
					}
				}
			}
		}
		return valida;
	}

}
