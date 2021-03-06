package cl.eos.view.ots;

import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.util.Utils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTPruebaRendida {

    private PruebaRendida pruebaRendida;
    private final SimpleIntegerProperty buenas = new SimpleIntegerProperty();
    private final SimpleIntegerProperty omitidas = new SimpleIntegerProperty();
    private final SimpleIntegerProperty malas = new SimpleIntegerProperty();
    private final SimpleStringProperty respuestas = new SimpleStringProperty();
    private final SimpleFloatProperty nota = new SimpleFloatProperty();
    private final SimpleIntegerProperty puntaje = new SimpleIntegerProperty();
    private final SimpleBooleanProperty rindioPrueba = new SimpleBooleanProperty();
    private final SimpleObjectProperty<RangoEvaluacion> nivel = new SimpleObjectProperty<RangoEvaluacion>();

    public OTPruebaRendida(PruebaRendida pruebaRendida) {
        this.pruebaRendida = pruebaRendida;
        buenas.set(pruebaRendida.getBuenas());
        omitidas.set(pruebaRendida.getOmitidas());
        malas.set(pruebaRendida.getMalas());
        respuestas.set(pruebaRendida.getRespuestas());
        nota.set(Utils.redondeo2Decimales(pruebaRendida.getNota()));
        puntaje.set(Utils.getPuntaje(nota.floatValue()));
        nivel.set(pruebaRendida.getRango());
        rindioPrueba.set(true);
    }

    public SimpleIntegerProperty buenasProperty() {
        return buenas;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTPruebaRendida other = (OTPruebaRendida) obj;
        if (pruebaRendida == null) {
            if (other.pruebaRendida != null)
                return false;
        } else if (!pruebaRendida.equals(other.pruebaRendida))
            return false;
        return true;
    }

    public Integer getBuenas() {
        return buenas.getValue();
    }

    public Integer getMalas() {
        return malas.getValue();
    }

    public String getMaterno() {
        return pruebaRendida.getAlumno().getMaterno();
    }

    public RangoEvaluacion getNivel() {
        return nivel.get();
    }

    public String getNombres() {
        return pruebaRendida.getAlumno().getName();
    }

    public Float getNota() {
        return nota.floatValue();
    }

    public Integer getOmitidas() {
        return omitidas.getValue();
    }

    public String getPaterno() {
        return pruebaRendida.getAlumno().getPaterno();
    }

    public PruebaRendida getPruebaRendida() {
        return pruebaRendida;
    }

    public Integer getPuntaje() {
        return puntaje.getValue();
    }

    public String getRespuestas() {
        return respuestas.get();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (pruebaRendida == null ? 0 : pruebaRendida.hashCode());
        return result;
    }

    public final boolean isRindioPrueba() {
        return rindioPruebaProperty().get();
    }

    public SimpleIntegerProperty malasProperty() {
        return malas;
    }

    public SimpleObjectProperty<RangoEvaluacion> nivelProperty() {
        return nivel;
    }

    public SimpleFloatProperty notaProperty() {
        return nota;
    }

    public SimpleIntegerProperty omitidasProperty() {
        return omitidas;
    }

    public SimpleIntegerProperty puntajeProperty() {
        return puntaje;
    }

    public SimpleStringProperty respuestasProperty() {
        return respuestas;
    }

    public final SimpleBooleanProperty rindioPruebaProperty() {
        return rindioPrueba;
    }

    public void setBuenas(Integer buenas) {
        this.buenas.set(buenas);
        pruebaRendida.setBuenas(buenas);
    }

    public void setMalas(Integer malas) {
        this.malas.set(malas);
        pruebaRendida.setMalas(malas);
    }

    public void setNivel(RangoEvaluacion nivel) {
        this.nivel.set(nivel);
        pruebaRendida.setRango(nivel);
    }

    public void setNota(Float nota) {
        this.nota.set(Utils.redondeo2Decimales(nota));
        pruebaRendida.setNota(nota);
        setPuntaje(Utils.getPuntaje(this.nota.floatValue()));
    }

    public void setOmitidas(Integer omitidas) {
        this.omitidas.set(omitidas);
        pruebaRendida.setOmitidas(omitidas);
    }

    public void setPruebaRendida(PruebaRendida pruebaRendida) {
        this.pruebaRendida = pruebaRendida;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje.set(puntaje);
    }

    public void setRespuestas(String respuestas) {
        this.respuestas.set(respuestas);
        pruebaRendida.setRespuestas(respuestas);
    }

    public final void setRindioPrueba(final boolean rindioPrueba) {
        rindioPruebaProperty().set(rindioPrueba);
    }

}
