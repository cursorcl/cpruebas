package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "habilidad")
public class Habilidad  implements IEntity  {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	private String name;
	private String descripcion;
	
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
		this.name=name;
		
	}
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
