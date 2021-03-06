package cl.eos.view.editablecells;

import cl.eos.view.RegistroDefinePrueba;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class EditingCellRespuesta extends TableCell<RegistroDefinePrueba, String> {

    private String validValues = "abcde";
    private final String validBooleanValues = "vf";

    private TextField textField;

    public EditingCellRespuesta() {
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(null);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(getWidth() - getGraphicTextGap() * 2);
        textField.setOnKeyTyped(event -> {
            final RegistroDefinePrueba registro = getTableView().getItems().get(getTableRow().getIndex());

            if (!registro.getMental().booleanValue()) {
                final String ch = event.getCharacter();
                String caracteresValidos = validValues;
                if (registro.getVerdaderoFalso().booleanValue()) {
                    caracteresValidos = validBooleanValues.toUpperCase();
                }
                if (caracteresValidos.indexOf(ch.toUpperCase()) == -1) {
                    event.consume();
                }
                if (textField.getText().length() > 0) {
                    textField.setText(ch);
                    event.consume();
                }
            } else {
                event.consume();
            }
        });
        textField.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
            if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            }
        });

    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

    public String getValidValues() {
        return validValues;
    }

    public void setValidValues(String validValues) {
        this.validValues = validValues;
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
}
