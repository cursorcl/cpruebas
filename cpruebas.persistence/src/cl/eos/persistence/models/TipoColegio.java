package cl.eos.persistence.models;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "tipocolegio")
@NamedQueries({ @NamedQuery(name = "TipoColegio.findAll", query = "SELECT e FROM tipocolegio e order by e.name")})

public class TipoColegio implements IEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "tipoColegio", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Collection<Colegio> colegios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Colegio> getColegios() {
		return colegios;
	}

	public void setColegios(Collection<Colegio> colegios) {
		this.colegios = colegios;
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TipoColegio other = (TipoColegio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
	
	
}
