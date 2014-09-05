package cl.eos.persistence.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "colegio")
public class Colegio implements IEntity {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	
	@Column(name="image", columnDefinition="blob")
	private byte[] image;
	
	private String direccion;
	

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

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	
}
