package cl.eos.persistence.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "formas")
@NamedQueries({ @NamedQuery(name = "Formas.findAll", query = "SELECT e FROM formas e"),
                @NamedQuery(name = "Formas.findByPrueba", query = "SELECT e FROM formas e WHERE e.prueba.id = :pruebaId"),
                @NamedQuery(name = "Formas.deleteByPrueba", query = "DELETE FROM formas e WHERE e.prueba.id = :pruebaId")})

public class Formas implements IEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100)
	private String name;

	private Integer forma;
	/**
	 * Indica el orden en que se imprimieron las preguntas.
	 */
	private String orden;

	@ManyToOne(fetch = FetchType.LAZY)
	private Prueba prueba;

	public Prueba getPrueba() {
    return prueba;
  }

  public void setPrueba(Prueba prueba) {
    this.prueba = prueba;
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
		return true;
	}

	public Integer getForma() {
		return forma;
	}

	public void setForma(Integer forma) {
		this.forma = forma;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

}
