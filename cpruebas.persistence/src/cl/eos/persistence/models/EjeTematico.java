package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

@Entity(name = "ejetematico")
@NamedQueries({ @NamedQuery(name = "EjeTematico.findAll", query = "SELECT e FROM ejetematico e order by e.name"),
                @NamedQuery(name = "EjeTematico.findByAsigntura", query = "Select e FROM ejetematico e WHERE e.asignatura.id = :idAsignatura")})
public class EjeTematico extends AEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private TipoPrueba tipoprueba;
	private Asignatura asignatura;

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

	public TipoPrueba getTipoprueba() {
		return tipoprueba;
	}

	public void setTipoprueba(TipoPrueba tipoprueba) {
		this.tipoprueba = tipoprueba;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	@Override
	public String toString() {
		return name;
	}


}
