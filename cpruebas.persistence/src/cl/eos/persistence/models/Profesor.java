package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

@Entity(name = "profesor")
@NamedQueries({ @NamedQuery(name = "Profesor.findAll", query = "SELECT e FROM profesor e order by e.name") })
public class Profesor extends AEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	
	private String rut;
	private String name;
	private String paterno;
	private String materno;

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
		return true;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getPaterno() {
		return paterno;
	}

	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}

	public String getMaterno() {
		return materno;
	}

	public void setMaterno(String materno) {
		this.materno = materno;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s",name, paterno, materno).trim();
	}


}
