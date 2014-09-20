package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.interfaces.entity.IEntity;

/**
 * Contiene las respuestas esperadas de una prueba
 * 
 * @author curso_000
 */
@Entity(name = "respuestasesperadasprueba")
@NamedQueries({
    @NamedQuery(name = "RespuestasEsperadasPrueba.findAll", query = "SELECT e FROM respuestasesperadasprueba e"),
    @NamedQuery(name = "RespuestasEsperadasPrueba.findByPrueba", query = "SELECT e FROM respuestasesperadasprueba e WHERE e.prueba.id = :pruebaId"),
    @NamedQuery(name = "RespuestasEsperadasPrueba.deleteByPrueba", query = "DELETE FROM respuestasesperadasprueba e WHERE e.prueba.id = :pruebaId")})
public class RespuestasEsperadasPrueba implements IEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(fetch = FetchType.LAZY)
  private Prueba prueba;

  private Integer numero;
  private String respuesta;
  private Boolean verdaderoFalso;
  private Boolean mental;
  private Habilidad habilidad;
  private EjeTematico ejeTematico;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean validate() {
    return false;
  }

  public Prueba getPrueba() {
    return prueba;
  }

  public void setPrueba(Prueba prueba) {
    this.prueba = prueba;
  }

  public Integer getNumero() {
    return numero;
  }

  public void setNumero(Integer numero) {
    this.numero = numero;
  }

  public String getRespuesta() {
    return respuesta;
  }

  public void setRespuesta(String respuesta) {
    this.respuesta = respuesta;
  }

  public Boolean getVerdaderoFalso() {
    return verdaderoFalso;
  }

  public void setVerdaderoFalso(Boolean verdaderoFalso) {
    this.verdaderoFalso = verdaderoFalso;
  }

  public Boolean getMental() {
    return mental;
  }

  public void setMental(Boolean mental) {
    this.mental = mental;
  }

  public Habilidad getHabilidad() {
    return habilidad;
  }

  public void setHabilidad(Habilidad habilidad) {
    this.habilidad = habilidad;
  }

  public EjeTematico getEjeTematico() {
    return ejeTematico;
  }

  public void setEjeTematico(EjeTematico ejeTematico) {
    this.ejeTematico = ejeTematico;
  }

}
