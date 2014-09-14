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



  public SimpleIntegerProperty getNumero() {
    return numero;
  }



  public void setNumero(SimpleIntegerProperty numero) {
    this.numero = numero;
  }



  public SimpleStringProperty getRespuesta() {
    return respuesta;
  }


  public void setRespuesta(SimpleStringProperty respuesta) {
    this.respuesta = respuesta;
  }


  public SimpleBooleanProperty getVerdaderoFalso() {
    return verdaderoFalso;
  }


  public void setVerdaderoFalso(SimpleBooleanProperty verdaderoFalso) {
    this.verdaderoFalso = verdaderoFalso;
  }


  public SimpleBooleanProperty getMental() {
    return mental;
  }


  public void setMental(SimpleBooleanProperty mental) {
    this.mental = mental;
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
