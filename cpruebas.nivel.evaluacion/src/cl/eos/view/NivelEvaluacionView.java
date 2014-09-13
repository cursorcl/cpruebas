package cl.eos.view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.NivelEvaluacion;

public class NivelEvaluacionView extends AFormView {

	@FXML
	private RadioButton rbtn1;
	@FXML
	private RadioButton rbtn2;
	@FXML
	private RadioButton rbtn3;
	@FXML
	private Label label1;
	@FXML
	private Label label2;
	@FXML
	private Label label3;
	@FXML
	private Label label4;
	@FXML
	private TextField txtDesde1;
	@FXML
	private TextField txtDesde2;
	@FXML
	private TextField txtDesde3;
	@FXML
	private TextField txtDesde4;
	@FXML
	private TextField txtHasta1;
	@FXML
	private TextField txtHasta2;
	@FXML
	private TextField txtHasta3;
	@FXML
	private TextField txtHasta4;

	public NivelEvaluacionView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof NivelEvaluacion) {

			}
		}
	}
}
