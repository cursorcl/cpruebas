package cl.eos.view.ots;

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
  private SimpleObjectProperty<RangoEvaluacion> nivel = new SimpleObjectProperty<RangoEvaluacion>();

  public OTPruebaRendida(PruebaRendida pruebaRendida) {
    this.pruebaRendida = pruebaRendida;
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
  public SimpleObjectProperty<RangoEvaluacion> nivelProperty()
  {
    return nivel;
  }
  public void setNivel(RangoEvaluacion nivel) {
    this.nivel.set(nivel);
  }

  public PruebaRendida getPruebaRendida() {
    return pruebaRendida;
  }

  public void setPruebaRendida(PruebaRendida pruebaRendida) {
    this.pruebaRendida = pruebaRendida;
  }



  public Float getNota() {
    return pruebaRendida.getNota() != null ? pruebaRendida.getNota() : 0F;
  }

  public SimpleFloatProperty notaProperty() {
    return nota;
  }

  public void setNota(Float nota) {
    this.nota.set(nota);
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

}
