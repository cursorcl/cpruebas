package cl.eos.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;

/**
 * Clase que permite registrar la definición de un registro de una prueba. 
 * @author curso
 */
public class RegistroDefinePrueba {

  private SimpleIntegerProperty numero;
  private SimpleStringProperty respuesta;
  private SimpleBooleanProperty verdaderoFalso;
  private SimpleBooleanProperty mental;
  private SimpleListProperty<Habilidad> habilidades;
  private SimpleListProperty<EjeTematico> ejesTematicos;


  public RegistroDefinePrueba() {
    this(0, "", false, false);
  }



  public RegistroDefinePrueba(Integer numero, String respuesta, Boolean verdaderoFalso,
      Boolean mental) {
    super();
    this.numero = new SimpleIntegerProperty(numero);
    this.respuesta = new SimpleStringProperty(respuesta);
    this.verdaderoFalso = new SimpleBooleanProperty(verdaderoFalso.booleanValue());
    this.mental = new SimpleBooleanProperty(mental.booleanValue());
    this.habilidades = new SimpleListProperty<>();
    this.ejesTematicos = new SimpleListProperty<>();
  }

  public Integer getNumero() {
    return numero.getValue();
  }

  public void setNumero(Integer numero) {
    this.numero.set(numero);
  }

  public String getRespuesta() {
    return respuesta.getValue();
  }

  public void setRespuesta(String respuesta) {
    this.respuesta.set(respuesta);
  }

  public Boolean getVerdaderoFalso() {
    return verdaderoFalso.getValue();
  }

  public void setVerdaderoFalso(Boolean verdaderoFalso) {
    this.verdaderoFalso.set(verdaderoFalso);
  }

  public Boolean getMental() {
    return mental.getValue();
  }

  public void setMental(Boolean mental) {
    this.mental.set(mental);
  }



  public SimpleListProperty<Habilidad> getHabilidades() {
    return habilidades;
  }



  public void setHabilidades(SimpleListProperty<Habilidad> habilidades) {
    this.habilidades = habilidades;
  }



  public SimpleListProperty<EjeTematico> getEjesTematicos() {
    return ejesTematicos;
  }



  public void setEjesTematicos(SimpleListProperty<EjeTematico> ejesTematicos) {
    this.ejesTematicos = ejesTematicos;
  }


}
