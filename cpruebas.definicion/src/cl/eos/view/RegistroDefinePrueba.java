package cl.eos.view;

import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SHabilidad;
import cl.eos.persistence.models.SObjetivo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;

/**
 * Clase que permite registrar la definición de un registro de una prueba.
 *
 * @author curso
 */
public class RegistroDefinePrueba {

    private final SimpleIntegerProperty numero;
    private final SimpleStringProperty respuesta;
    private final SimpleBooleanProperty verdaderoFalso;
    private final SimpleBooleanProperty mental;
    private final ObjectProperty<SHabilidad> habilidad;
    private final ObjectProperty<SEjeTematico> ejeTematico;
    private final ObjectProperty<SObjetivo> objetivo;
    private SimpleStringProperty bad;

    public RegistroDefinePrueba() {
        this(0, "", false, false);
    }

    public RegistroDefinePrueba(Integer numero, String respuesta, Boolean verdaderoFalso, Boolean mental) {
        super();
        this.numero = new SimpleIntegerProperty(numero);
        this.respuesta = new SimpleStringProperty(respuesta);
        this.verdaderoFalso = new SimpleBooleanProperty(verdaderoFalso.booleanValue());
        this.mental = new SimpleBooleanProperty(mental.booleanValue());
        habilidad = new SimpleObjectProperty<>();
        ejeTematico = new SimpleObjectProperty<>();
        objetivo = new SimpleObjectProperty<>();

        this.mental.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue.equals(Boolean.TRUE)) {
                RegistroDefinePrueba.this.verdaderoFalso.set(Boolean.FALSE);
                RegistroDefinePrueba.this.respuesta.set(" ");
            }
        });

        this.verdaderoFalso.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue.equals(Boolean.TRUE)) {
                RegistroDefinePrueba.this.mental.set(Boolean.FALSE);
                if (!"VF".contains(getRespuesta().toUpperCase())) {
                    setRespuesta("");
                }
            }
        });

    }

    public SimpleStringProperty badProperty() {
        return bad;
    }

    public ObjectProperty<SEjeTematico> ejeTematicoProperty() {
        return ejeTematico;
    }

    public String getBad() {
        return bad.getValue();
    }

    public SEjeTematico getEjeTematico() {
        return ejeTematico.getValue();
    }

    public SHabilidad getHabilidad() {
        return habilidad.getValue();
    }

    public Boolean getMental() {
        return mental.getValue();
    }

    public Integer getNumero() {
        return numero.getValue();
    }

    public final cl.eos.persistence.models.SObjetivo getObjetivo() {
        return objetivoProperty().get();
    }

    public String getRespuesta() {
        return respuesta.getValue();
    }

    public Boolean getVerdaderoFalso() {
        return verdaderoFalso.getValue();
    }

    public ObjectProperty<SHabilidad> habilidadProperty() {
        return habilidad;
    }

    public SimpleBooleanProperty mentalProperty() {
        return mental;
    }

    public final ObjectProperty<SObjetivo> objetivoProperty() {
        return objetivo;
    }

    public SimpleStringProperty respuestaProperty() {
        return respuesta;
    }

    public void setBad(String bad) {
        this.bad.set(bad);
    }

    public void setEjeTematico(SEjeTematico ejeTematico) {
        this.ejeTematico.set(ejeTematico);
    }

    public void setHabilidad(SHabilidad habilidad) {
        this.habilidad.set(habilidad);
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

    public final void setObjetivo(final cl.eos.persistence.models.SObjetivo objetivo) {
        objetivoProperty().set(objetivo);
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
