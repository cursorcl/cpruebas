package cl.eos.view.editablecells;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.view.ots.OTPruebaRendida;

public class EditingCellRespuestasEvaluar extends
		TableCell<OTPruebaRendida, String> {

	private TextField textField;
	private Prueba prueba = null;
	private int maxLength = 0;

	public EditingCellRespuestasEvaluar(Prueba prueba) {
		super();
		this.prueba = prueba;
		maxLength = prueba.getRespuestas().size();
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (textField == null) {
			createTextField();
		}
		setText(null);
		setGraphic(textField);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		textField.selectAll();
		textField.requestFocus();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText((String) getItem());
		setGraphic(null);
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setGraphic(textField);
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			} else {
				setText(getString());
				setGraphic(null);
				setContentDisplay(ContentDisplay.TEXT_ONLY);
			}
		}
	}

	private void createTextField() {
		textField = new TextField(getString());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
				if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.TAB) {
					commitEdit(textField.getText());
				}
			}
		});
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov,
					final String oldValue, final String newValue) {
				if (newValue.length() > maxLength) {
					textField.setText(oldValue);
				} else {
					boolean validValue = false;
					int len = newValue.length();
					if (len > 0) {
						String s = newValue.substring(len - 1, len);
						RespuestasEsperadasPrueba resp = prueba.getRespuestas()
								.get(len - 1);
						if (resp.getVerdaderoFalso()) {
							validValue = "VF*".contains(s.toUpperCase());
						} else if (resp.getMental()) {
							validValue = "BM*".contains(s.toUpperCase());
						} else {
							String sValido = "ABCDE".substring(0,
									prueba.getAlternativas())
									+ "*";
							validValue = sValido.contains(s.toUpperCase());
						}
						if (!validValue) {
							textField.setText(oldValue);
						}
					}

				}
			}
		});

	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}
}
