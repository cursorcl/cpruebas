package cl.eos.view;

import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPrueba;

public class PruebasView extends AFormView {

	@FXML
	private TableView<OTPrueba> tblListadoPruebas;
	@FXML
	private TableColumn<OTPrueba, Date> fechaCol;

	@FXML
	private TextField text;
	public PruebasView() {
		text = new TextField();
	}

	
	@Override
	public void onDataArrived(List<IEntity> list) {
		super.onDataArrived(list);
		
	}
	
	
}
