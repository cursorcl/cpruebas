package cl.eos.interfaces.entity;

import java.io.Serializable;

public interface IEntity extends Serializable{

	Long getId();

	void setId(Long id);

	String getName();

	void setName(String name);

	boolean validate();
}
