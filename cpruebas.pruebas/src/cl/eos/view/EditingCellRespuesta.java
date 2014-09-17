package cl.eos.view;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EditingCellRespuesta extends
		TableCell<RegistroDefinePrueba, String> {

	private String validValues = "abcdevf";
	public String getValidValues() {
		return validValues;
	}

	public void setValidValues(String validValues) {
		this.validValues = validValues;
	}

	private TextField textField;

	public EditingCellRespuesta() {

	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (textField == null) {
			createTextField();
		}
		setText(null);
		setGraphic(textField);
		textField.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText((String) getItem());
		setGraphic(null);
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
				setText(null);
				setGraphic(textField);
			} else {
				setText(getString());
				setGraphic(null);
			}
		}
	}

	private void createTextField() {
		textField = new TextField(getString());
		
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String ch = event.getCharacter();
				
				if (validValues.indexOf(ch.toUpperCase()) == -1 ) {
					event.consume();
				}
				if(textField.getText().length() > 0)
				{
					event.consume();
				}
			}
		});
		
		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				int index = 0;
				if (textField.getText() != null
						&& !textField.getText().isEmpty()) {
					index = validValues.indexOf(textField.getText()
							.toLowerCase());
				}
				if (t.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
				if (index != -1) {
					if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.TAB) {
						commitEdit(textField.getText());
					}
				}
			}
		});
	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}
}