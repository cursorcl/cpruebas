package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "colegio")
@NamedQueries({ @NamedQuery(name = "Colegio.findAll", query = "SELECT e FROM colegio e order by e.name") })
public class Colegio implements IEntity {

	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	private String direccion;
	
	@ManyToOne
	private TipoColegio tipoColegio;

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

	public TipoColegio getTipoColegio() {
		return tipoColegio;
	}

	public void setTipoColegio(TipoColegio tipo) {
		this.tipoColegio = tipo;
	}

	@Override
	public boolean validate() {
		return true;
	}

	//
	// public byte[] getImage() {
	// return image;
	// }
	//
	// public void setImage(byte[] image) {
	// this.image = image;
	// }

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return name;
	}

}
