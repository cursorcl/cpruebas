package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

@Entity(name = "tipoalumno")
@NamedQueries({ @NamedQuery(name = "TipoAlumno.findAll", query = "SELECT e FROM tipoalumno e order by e.name") })
public class TipoAlumno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	/**
	 * Se crea para el manejo de multiusuarios
	 */
	@Version
	protected int version;

	public TipoAlumno() {

	}

	public TipoAlumno(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

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

}
