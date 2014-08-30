package cl.eos.interfaces.entity;

public interface IEntity {

	Long getId();

	void setId(Long id);

	String getName();

	void setName(String name);

	boolean validate();
}
