package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

/**
 * Contiene las respuestas esperadas de una prueba
 * 
 * @author curso_000
 */
@Entity(name = "respuestasesperadasprueba")
@NamedQueries({
    @NamedQuery(name = "RespuestasEsperadasPrueba.findAll", query = "SELECT e FROM respuestasesperadasprueba e"),
    @NamedQuery(name = "RespuestasEsperadasPrueba.findByPrueba", query = "SELECT e FROM respuestasesperadasprueba e WHERE e.prueba.id = :pruebaId order by e.numero"),
    @NamedQuery(name = "RespuestasEsperadasPrueba.deleteByPrueba", query = "DELETE FROM respuestasesperadasprueba e WHERE e.prueba.id = :pruebaId")})
public class RespuestasEsperadasPrueba extends AEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(fetch = FetchType.LAZY)
  private Prueba prueba;

  private Integer numero;
  private String respuesta = "N";
  private Boolean verdaderoFalso = Boolean.FALSE;
  private Boolean mental = Boolean.FALSE;
  private Habilidad habilidad;
  private EjeTematico ejeTematico;
  private Objetivo objetivo;
  private Boolean anulada = Boolean.FALSE;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  
	/**
	 * Se crea para el manejo de multiusuarios
	 */
	@Version 
	protected int version;
	
	
	public final int getVersion() {
		return version;
	}

	public final void setVersion(int version) {
		this.version = version;
	}

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

public Boolean getAnulada() {
	return anulada;
}

public Boolean isAnulada() {
	return anulada;
}

public void setAnulada(Boolean anulada) {
	this.anulada = anulada;
}

public Objetivo getObjetivo() {
	return objetivo;
}

public void setObjetivo(Objetivo objetivo) {
	this.objetivo = objetivo;
}

}
