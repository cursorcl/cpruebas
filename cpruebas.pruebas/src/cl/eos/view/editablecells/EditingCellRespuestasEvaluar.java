package cl.eos.view.editablecells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.view.ots.OTPruebaRendida;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class EditingCellRespuestasEvaluar extends TableCell<OTPruebaRendida, String> {

    private TextField textField;
    private SPrueba prueba = null;
    private final int maxLength;
    private List<SRespuestasEsperadasPrueba> listaRespuestas = null;

    public EditingCellRespuestasEvaluar(SPrueba prueba) {
        super();
        this.prueba = prueba;

        listaRespuestas = new ArrayList<SRespuestasEsperadasPrueba>(prueba.getRespuestas());
        Collections.sort(listaRespuestas, Comparadores.compararRespuestasEsperadas());

        maxLength = prueba.getRespuestas().size();
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
        textField.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
            if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            }
        });
        textField.textProperty().addListener(new ChangeListener<String>() {
            private boolean selfChage = false;

            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue,
                    final String newValue) {
                if (selfChage) {
                    selfChage = false;
                    return;
                }
                if (newValue.length() > maxLength) {
                    selfChage = true;
                    textField.setText(newValue.substring(0, maxLength));
                } else {
                    boolean validValue = false;
                    final int len = newValue.length();
                    if (len > 0) {

                        final String s = newValue.substring(len - 1, len);
                        final SRespuestasEsperadasPrueba resp = listaRespuestas.get(len - 1);
                        if (resp.getVerdaderoFalso()) {
                            validValue = "VFO".contains(s.toUpperCase());
                        } else if (resp.getMental()) {
                            validValue = "-+O".contains(s.toUpperCase());
                        } else {
                            final String sValido = "ABCDE".substring(0, prueba.getAlternativas()) + "O";
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
