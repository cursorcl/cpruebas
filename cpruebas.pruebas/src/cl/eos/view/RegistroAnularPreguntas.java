package cl.eos.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;

/**
 * Clase que permite registrar la definición de un registro de una prueba.
 *
 * @author colegio
 */
public class RegistroAnularPreguntas {

    private final SimpleIntegerProperty numero;
    private final SimpleStringProperty respuesta;
    private final SimpleBooleanProperty verdaderoFalso;
    private final SimpleBooleanProperty mental;
    private final SimpleBooleanProperty anulada;

    public RegistroAnularPreguntas() {
        this(0, "", false, false, false);
    }

    public RegistroAnularPreguntas(Integer numero, String respuesta, Boolean verdaderoFalso, Boolean mental,
            Boolean anulada) {
        super();
        this.numero = new SimpleIntegerProperty(numero);
        this.respuesta = new SimpleStringProperty(respuesta);
        this.verdaderoFalso = new SimpleBooleanProperty(verdaderoFalso.booleanValue());
        this.mental = new SimpleBooleanProperty(mental.booleanValue());
        this.anulada = new SimpleBooleanProperty(anulada.booleanValue());

        this.mental.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue.equals(Boolean.TRUE)) {
                RegistroAnularPreguntas.this.verdaderoFalso.set(Boolean.FALSE);
                RegistroAnularPreguntas.this.respuesta.set(" ");
            }
        });

        this.verdaderoFalso.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue.equals(Boolean.TRUE)) {
                RegistroAnularPreguntas.this.mental.set(Boolean.FALSE);
                if (!"VF".contains(getRespuesta().toUpperCase())) {
                    setRespuesta("");
                }
            }
        });

    }

    public SimpleBooleanProperty anuladaProperty() {
        return anulada;
    }

    public Boolean getAnulada() {
        return anulada.getValue();
    }

    public Boolean getMental() {
        return mental.getValue();
    }

    public Integer getNumero() {
        return numero.getValue();
    }

    public String getRespuesta() {
        return respuesta.getValue();
    }

    public Boolean getVerdaderoFalso() {
        return verdaderoFalso.getValue();
    }

    public SimpleBooleanProperty mentalProperty() {
        return mental;
    }

    public SimpleStringProperty respuestaProperty() {
        return respuesta;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada.set(anulada);
    }

    public void setMental(Boolean mental) {
        this.mental.set(mental);
        if (mental) {
            setVerdaderoFalso(false);
        }
    }

    public void setNumero(Integer numero) {
        this.numero.set(numero);
    }

    public void setRespuesta(String respuesta) {
        this.respuesta.set(respuesta);
    }

    public void setVerdaderoFalso(Boolean verdaderoFalso) {
        this.verdaderoFalso.set(verdaderoFalso);
    }

    public SimpleBooleanProperty verdaderoFalsoProperty() {
        return verdaderoFalso;
    }

}
