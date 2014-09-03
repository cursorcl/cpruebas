package cl.eos.view;

import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPrueba;

public class PruebasView extends AFormView {

	@FXML
	private TableView<OTPrueba> tblListadoPruebas;
	@FXML
	private TableColumn<OTPrueba, Date> fechaCol;
	@FXML
	private TableColumn<OTPrueba, String> cursoCol;
	@FXML
	private TableColumn<OTPrueba, String> nameCol;
	@FXML
	private TableColumn<OTPrueba, String> asignaturaCol;
	@FXML
	private TableColumn<OTPrueba, String> profesorCol;
	@FXML
	private TableColumn<OTPrueba, String> formaCol;
	@FXML
	private TableColumn<OTPrueba, Integer> nroPreguntasCol;
	@FXML
	private TableColumn<OTPrueba, Integer> formasCol;
	@FXML
	private TableColumn<OTPrueba, Integer> alternativasCol;
	
	public PruebasView() {
	}

	
	@Override
	public void onDataArrived(List<IEntity> list) {
		super.onDataArrived(list);
		
	}
	
	
}
