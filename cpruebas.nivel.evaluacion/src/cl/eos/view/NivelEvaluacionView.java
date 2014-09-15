package cl.eos.view;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.RangoEvaluacion;

public class NivelEvaluacionView extends AFormView implements
		EventHandler<ActionEvent> {

	@FXML
	private ComboBox<NivelEvaluacion> cmbNiveles;
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
	@FXML
	private MenuItem mnuGrabar;
	private List<? extends IEntity> listaNiveles;

	public NivelEvaluacionView() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	public void initialize() {
		cmbNiveles.setOnAction(this);
		mnuGrabar.setOnAction(this);
		txtHasta1.setOnAction(this);
		txtHasta2.setOnAction(this);
		txtHasta3.setOnAction(this);
		txtHasta4.setOnAction(this);
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);

			if (entity instanceof NivelEvaluacion) {
				listaNiveles = (List<? extends IEntity>) list;
				for (Object otElemento : list) {
					cmbNiveles.getItems().addAll((NivelEvaluacion) otElemento);
				}
				cmbNiveles.setValue((NivelEvaluacion) entity);
				select((NivelEvaluacion) entity);
			}

		}
	}

	private void setDatosNiveles(NivelEvaluacion entity) {
		List<RangoEvaluacion> rangos = entity.getRangos();
		if (entity.getNroRangos() == 2) {
			RangoEvaluacion rango = rangos.get(0);
			label1.setText(rango.getName());
			txtDesde1.setText(String.valueOf(rango.getMinimo()));
			txtHasta1.setText(String.valueOf(rango.getMaximo()));

			rango = rangos.get(1);
			label2.setText(rango.getName());
			txtDesde2.setText(String.valueOf(rango.getMinimo()));
			txtHasta2.setText(String.valueOf(rango.getMaximo()));

			label3.setText(null);
			txtDesde3.setText(null);
			txtHasta3.setText(null);
			label3.setDisable(true);
			txtDesde3.setVisible(false);
			txtHasta3.setVisible(false);

			label4.setText(null);
			txtDesde4.setText(null);
			txtHasta4.setText(null);
			label4.setDisable(true);
			txtDesde4.setVisible(false);
			txtHasta4.setVisible(false);
		} else if (entity.getNroRangos() == 3) {
			RangoEvaluacion rango = rangos.get(0);
			label1.setText(rango.getName());
			txtDesde1.setText(String.valueOf(rango.getMinimo()));
			txtHasta1.setText(String.valueOf(rango.getMaximo()));

			rango = rangos.get(1);
			label2.setText(rango.getName());
			txtDesde2.setText(String.valueOf(rango.getMinimo()));
			txtHasta2.setText(String.valueOf(rango.getMaximo()));

			rango = rangos.get(2);
			label3.setText(rango.getName());
			txtDesde3.setText(String.valueOf(rango.getMinimo()));
			txtHasta3.setText(String.valueOf(rango.getMaximo()));
			label3.setDisable(false);
			txtDesde3.setVisible(true);
			txtHasta3.setVisible(true);

			label4.setText(null);
			txtDesde4.setText(null);
			txtHasta4.setText(null);
			label4.setDisable(true);
			txtDesde4.setVisible(false);
			txtHasta4.setVisible(false);

		} else if (entity.getNroRangos() == 4) {
			RangoEvaluacion rango = rangos.get(0);
			label1.setText(rango.getName());
			txtDesde1.setText(String.valueOf(rango.getMinimo()));
			txtHasta1.setText(String.valueOf(rango.getMaximo()));

			rango = rangos.get(1);
			label2.setText(rango.getName());
			txtDesde2.setText(String.valueOf(rango.getMinimo()));
			txtHasta2.setText(String.valueOf(rango.getMaximo()));

			rango = rangos.get(2);
			label3.setText(rango.getName());
			txtDesde3.setText(String.valueOf(rango.getMinimo()));
			txtHasta3.setText(String.valueOf(rango.getMaximo()));
			label3.setDisable(false);
			txtDesde3.setVisible(true);
			txtHasta3.setVisible(true);

			rango = rangos.get(3);
			label4.setText(rango.getName());
			txtDesde4.setText(String.valueOf(rango.getMinimo()));
			txtHasta4.setText(String.valueOf(rango.getMaximo()));
			label4.setDisable(false);
			txtDesde4.setVisible(true);
			txtHasta4.setVisible(true);
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuGrabar) {
			handleGrabar();
		} else if (source == cmbNiveles) {
			handleSeleccionar();
		} else if (source == txtHasta1) {
			handlerActualizar1();
		}
	}

	private void handlerActualizar1() {
		String valor = txtHasta1.getText();
		Float valorFloat = Float.valueOf(valor) + 1;
		txtDesde2.setText(String.valueOf(valorFloat));

	}

	private void handleSeleccionar() {
		NivelEvaluacion nivel = cmbNiveles.getValue();
		select(nivel);
		setDatosNiveles(nivel);
	}

	private void handleGrabar() {
		NivelEvaluacion nivel = cmbNiveles.getValue();
		for (Object object : listaNiveles) {
			if (object instanceof NivelEvaluacion) {
				if (nivel.equals((NivelEvaluacion) object)) {
					NivelEvaluacion nivelActualizar = (NivelEvaluacion) object;
					List<RangoEvaluacion> rangos = nivelActualizar.getRangos();

					if (nivelActualizar.getNroRangos() == 2) {
						RangoEvaluacion rango = rangos.get(0);
						rango.setMinimo(Float.valueOf(txtDesde1.getText()));
						rango.setMaximo(Float.valueOf(txtHasta1.getText()));

						rango = rangos.get(1);
						rango.setMinimo(Float.valueOf(txtDesde2.getText()));
						rango.setMaximo(Float.valueOf(txtHasta2.getText()));

					} else if (nivelActualizar.getNroRangos() == 3) {
						RangoEvaluacion rango = rangos.get(0);
						rango.setMinimo(Float.valueOf(txtDesde1.getText()));
						rango.setMaximo(Float.valueOf(txtHasta1.getText()));

						rango = rangos.get(1);
						rango.setMinimo(Float.valueOf(txtDesde2.getText()));
						rango.setMaximo(Float.valueOf(txtHasta2.getText()));

						rango = rangos.get(2);
						rango.setMinimo(Float.valueOf(txtDesde3.getText()));
						rango.setMaximo(Float.valueOf(txtHasta3.getText()));

					} else if (nivelActualizar.getNroRangos() == 4) {
						RangoEvaluacion rango = rangos.get(0);
						rango.setMinimo(Float.valueOf(txtDesde1.getText()));
						rango.setMaximo(Float.valueOf(txtHasta1.getText()));

						rango = rangos.get(1);
						rango.setMinimo(Float.valueOf(txtDesde2.getText()));
						rango.setMaximo(Float.valueOf(txtHasta2.getText()));

						rango = rangos.get(2);
						rango.setMinimo(Float.valueOf(txtDesde3.getText()));
						rango.setMaximo(Float.valueOf(txtHasta3.getText()));

						rango = rangos.get(3);
						rango.setMinimo(Float.valueOf(txtDesde4.getText()));
						rango.setMaximo(Float.valueOf(txtHasta4.getText()));
					}
					save(nivelActualizar);
					break;
				}
			}
		}
	}
}
