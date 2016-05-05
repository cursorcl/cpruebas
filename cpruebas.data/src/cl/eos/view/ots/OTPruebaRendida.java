package cl.eos.view.ots;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.util.Utils;

public class OTPruebaRendida {

  private PruebaRendida pruebaRendida;
  private SimpleIntegerProperty buenas = new SimpleIntegerProperty();
  private SimpleIntegerProperty omitidas = new SimpleIntegerProperty();
  private SimpleIntegerProperty malas = new SimpleIntegerProperty();
  private SimpleStringProperty respuestas = new SimpleStringProperty();
  private SimpleFloatProperty nota = new SimpleFloatProperty();
  private SimpleIntegerProperty puntaje = new SimpleIntegerProperty();
  private SimpleBooleanProperty rindioPrueba = new SimpleBooleanProperty();
  private SimpleObjectProperty<RangoEvaluacion> nivel = new SimpleObjectProperty<RangoEvaluacion>();

  public OTPruebaRendida(PruebaRendida pruebaRendida) {
    this.pruebaRendida = pruebaRendida;
    this.buenas.set(pruebaRendida.getBuenas());
    this.omitidas.set(pruebaRendida.getOmitidas());
    this.malas.set(pruebaRendida.getMalas());
    this.respuestas.set(pruebaRendida.getRespuestas());
    this.nota.set(Utils.redondeo2Decimales(pruebaRendida.getNota()));
    this.puntaje.set(Utils.getPuntaje(this.nota.floatValue()));
    this.nivel.set(pruebaRendida.getRango());
    this.rindioPrueba.set(true);
  }

  public String getPaterno() {
    return pruebaRendida.getAlumno().getPaterno();
  }

  public String getMaterno() {
    return pruebaRendida.getAlumno().getMaterno();
  }

  public String getNombres() {
    return pruebaRendida.getAlumno().getName();
  }


  public RangoEvaluacion getNivel() {
    return nivel.get();
  }

  public SimpleObjectProperty<RangoEvaluacion> nivelProperty() {
    return nivel;
  }

  public void setNivel(RangoEvaluacion nivel) {
    this.nivel.set(nivel);
    this.pruebaRendida.setRango(nivel);
  }

  public PruebaRendida getPruebaRendida() {
    return pruebaRendida;
  }

  public void setPruebaRendida(PruebaRendida pruebaRendida) {
    this.pruebaRendida = pruebaRendida;
  }



  public Float getNota() {
    return  nota.floatValue();
  }

  public SimpleFloatProperty notaProperty() {
    return nota;
  }

  public void setNota(Float nota) {
    this.nota.set(Utils.redondeo2Decimales(nota));
    this.pruebaRendida.setNota(nota);
    setPuntaje(Utils.getPuntaje(this.nota.floatValue()));
  }

  public Integer getPuntaje() {
    return puntaje.getValue();
  }

  public SimpleIntegerProperty puntajeProperty() {
    return puntaje;
  }

  public void setPuntaje(Integer puntaje) {
    this.puntaje.set(puntaje);
  }

  public String getRespuestas() {
    return respuestas.get();
  }

  public SimpleStringProperty respuestasProperty() {
    return respuestas;
  }

  public void setRespuestas(String respuestas) {
    this.respuestas.set(respuestas);
    pruebaRendida.setRespuestas(respuestas);
  }

  public void setMalas(Integer malas) {
    this.malas.set(malas);
    this.pruebaRendida.setMalas(malas);
  }

  public SimpleIntegerProperty malasProperty() {
    return this.malas;
  }

  public Integer getMalas() {
    return malas.getValue();
  }

  public void setOmitidas(Integer omitidas) {
    this.omitidas.set(omitidas);
    this.pruebaRendida.setOmitidas(omitidas);
  }

  public SimpleIntegerProperty omitidasProperty() {
    return this.omitidas;
  }

  public Integer getOmitidas() {
    return omitidas.getValue();
  }


  public void setBuenas(Integer buenas) {
    this.buenas.set(buenas);
    this.pruebaRendida.setBuenas(buenas);
  }

  public SimpleIntegerProperty buenasProperty() {
    return this.buenas;
  }

  public Integer getBuenas() {
    return buenas.getValue();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pruebaRendida == null) ? 0 : pruebaRendida.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OTPruebaRendida other = (OTPruebaRendida) obj;
    if (pruebaRendida == null) {
      if (other.pruebaRendida != null)
        return false;
    } else if (!pruebaRendida.equals(other.pruebaRendida))
      return false;
    return true;
  }

  public final SimpleBooleanProperty rindioPruebaProperty() {
    return this.rindioPrueba;
  }

  public final boolean isRindioPrueba() {
    return this.rindioPruebaProperty().get();
  }

  public final void setRindioPrueba(final boolean rindioPrueba) {
    this.rindioPruebaProperty().set(rindioPrueba);
  }

  
  
}
