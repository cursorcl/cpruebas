package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cl.eos.persistence.AEntity;

@Entity(name = "ciclo")
@Table(name = "ciclo", schema = "cpruebas_comun")

@NamedQueries({ @NamedQuery(name = "Ciclo.findAll", query = "SELECT e FROM ciclo e order by e.name") })
public class Ciclo extends AEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setName(String name) {
		this.name = name;

	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean validate() {
		return false;
	}

}
