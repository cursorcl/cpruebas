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
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

@Entity(name = "tipocolegio")
@NamedQueries({ @NamedQuery(name = "TipoColegio.findAll", query = "SELECT e FROM tipocolegio e order by e.name")})

public class TipoColegio extends AEntity {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "tipoColegio", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Collection<Colegio> colegios;

	
	/**
	 * Se crea para el manejo de multiusuarios
	 */
	@Version 
	protected int version;
	
	@Override
	public final int getVersion() {
		return version;
	}

	@Override
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
	public String toString() {
		return name;
	}

	
}
