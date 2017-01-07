package cl.eos.view.editablecells;

import java.util.Collections;
import java.util.List;

import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.view.ots.OTPruebaRendida;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class EditingCellRespuestasEvaluar extends TableCell<OTPruebaRendida, String> {

    private TextField textField;
    private R_Prueba prueba = null;
    private final int maxLength;
    private List<R_RespuestasEsperadasPrueba> listaRespuestas = null;

    public EditingCellRespuestasEvaluar(R_Prueba prueba, List<R_RespuestasEsperadasPrueba> respEsperadas) {
        super();
        this.prueba = prueba;
        listaRespuestas = respEsperadas;
        Collections.sort(listaRespuestas, Comparadores.compararRespuestasEsperadas());

        maxLength = listaRespuestas.size();
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
                        final R_RespuestasEsperadasPrueba resp = listaRespuestas.get(len - 1);
                        if (resp.getVerdaderofalso()) {
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
