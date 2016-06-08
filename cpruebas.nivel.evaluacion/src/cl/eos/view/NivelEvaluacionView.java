package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.RangoEvaluacion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

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
	private Label lblError;
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

	private List<Object> listaNiveles;
	
	private Object entityFirstElement;

	public NivelEvaluacionView() {
		setTitle("Nivel Evaluación");
	}

	@FXML
	public void initialize() {
		cmbNiveles.setOnAction(this);
		mnuGrabar.setOnAction(this);
		//txtHasta1.setOnKeyReleased();
		txtHasta2.setOnAction(this);
		txtHasta3.setOnAction(this);
		txtHasta4.setOnAction(this);
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			entityFirstElement = list.get(0);

			if (entityFirstElement instanceof NivelEvaluacion) {
				listaNiveles = list;
				for (Object otElemento : list) {
					cmbNiveles.getItems().addAll((NivelEvaluacion) otElemento);
				}
				cmbNiveles.setValue((NivelEvaluacion) entityFirstElement);
				select((NivelEvaluacion) entityFirstElement);
			}

		}
	}

	private void setDatosNiveles(NivelEvaluacion entity) {
		List<RangoEvaluacion> rangos = new ArrayList<>(entity.getRangos());
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
		if (nivel != null) {
			removeAllStyles();
			if (validaDatos()) {
				if (lblError != null) {
					lblError.setText(" ");
				}
				for (Object object : listaNiveles) {
					if (object instanceof NivelEvaluacion) {
						if (nivel.equals((NivelEvaluacion) object)) {
							NivelEvaluacion nivelActualizar = (NivelEvaluacion) object;
							List<RangoEvaluacion> rangos = new ArrayList<>(nivelActualizar
									.getRangos());

							if (nivelActualizar.getNroRangos() == 2) {
								RangoEvaluacion rango = rangos.get(0);
								rango.setMinimo(Float.valueOf(txtDesde1
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta1
										.getText()));

								rango = rangos.get(1);
								rango.setMinimo(Float.valueOf(txtDesde2
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta2
										.getText()));

							} else if (nivelActualizar.getNroRangos() == 3) {
								RangoEvaluacion rango = rangos.get(0);
								rango.setMinimo(Float.valueOf(txtDesde1
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta1
										.getText()));

								rango = rangos.get(1);
								rango.setMinimo(Float.valueOf(txtDesde2
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta2
										.getText()));

								rango = rangos.get(2);
								rango.setMinimo(Float.valueOf(txtDesde3
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta3
										.getText()));

							} else if (nivelActualizar.getNroRangos() == 4) {
								RangoEvaluacion rango = rangos.get(0);
								rango.setMinimo(Float.valueOf(txtDesde1
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta1
										.getText()));

								rango = rangos.get(1);
								rango.setMinimo(Float.valueOf(txtDesde2
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta2
										.getText()));

								rango = rangos.get(2);
								rango.setMinimo(Float.valueOf(txtDesde3
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta3
										.getText()));

								rango = rangos.get(3);
								rango.setMinimo(Float.valueOf(txtDesde4
										.getText()));
								rango.setMaximo(Float.valueOf(txtHasta4
										.getText()));
							}
							save(nivelActualizar);
							limpiarControles();
							break;
						}
					}
				}
			} else {
				lblError.getStyleClass().add("bad");
				lblError.setText("Corregir campos destacados en color rojo.");
			}

		}
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtDesde1);
		removeAllStyle(txtDesde2);
		removeAllStyle(txtDesde3);
		removeAllStyle(txtDesde4);
		removeAllStyle(txtHasta1);
		removeAllStyle(txtHasta2);
		removeAllStyle(txtHasta3);
		removeAllStyle(txtHasta4);
	}

	private boolean validaDatos() {
		boolean valida = true;
		Float sumaNivel = (float) 0;
		IEntity entitySel = getSelectedEntity();
		if (entitySel instanceof NivelEvaluacion) {
//			if (((NivelEvaluacion) entitySel).getNroRangos() == 2) {
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde1.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde2.getText());
//
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta1.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta2.getText());
//				if (sumaNivel > 100) {
//					txtDesde1.getStyleClass().add("bad");
//					txtDesde2.getStyleClass().add("bad");
//
//					txtHasta1.getStyleClass().add("bad");
//					txtHasta2.getStyleClass().add("bad");
//					valida = false;
//				}
//			}
//
//			else if (((NivelEvaluacion) entitySel).getNroRangos() == 3) {
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde1.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde2.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde3.getText());
//
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta1.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta2.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta3.getText());
//				if (sumaNivel > 100) {
//					txtDesde1.getStyleClass().add("bad");
//					txtDesde2.getStyleClass().add("bad");
//					txtDesde3.getStyleClass().add("bad");
//
//					txtHasta1.getStyleClass().add("bad");
//					txtHasta2.getStyleClass().add("bad");
//					txtHasta3.getStyleClass().add("bad");
//					valida = false;
//				}
//			}
//
//			else if (((NivelEvaluacion) entitySel).getNroRangos() == 4) {
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde1.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde2.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde3.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtDesde4.getText());
//
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta1.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta2.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta3.getText());
//				sumaNivel = sumaNivel + Float.valueOf(txtHasta4.getText());
//				if (sumaNivel > 100) {
//					txtDesde1.getStyleClass().add("bad");
//					txtDesde2.getStyleClass().add("bad");
//					txtDesde3.getStyleClass().add("bad");
//					txtDesde4.getStyleClass().add("bad");
//
//					txtHasta1.getStyleClass().add("bad");
//					txtHasta2.getStyleClass().add("bad");
//					txtHasta3.getStyleClass().add("bad");
//					txtHasta4.getStyleClass().add("bad");
//					valida = false;
//				}
//			}
		}
		return true;

	}

	private void limpiarControles() {
		cmbNiveles.setValue((NivelEvaluacion) entityFirstElement);
		select((NivelEvaluacion) entityFirstElement);
		txtDesde1.clear();
		txtDesde2.clear();
		txtDesde3.clear();
		txtDesde4.clear();
		txtHasta1.clear();
		txtHasta2.clear();
		txtHasta3.clear();
		txtHasta4.clear();
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
	}
}
