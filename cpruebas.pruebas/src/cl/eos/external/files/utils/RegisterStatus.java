package cl.eos.external.files.utils;

public enum RegisterStatus {

	BUENO(0, "Correcta"), 
	RUT_INCORRECTO(1, "Rut incorrecto."), 
	NO_EXISTE_COLEGIO(2, "No existe el colegio."),
	NO_EXISTE_ALUMNO(3, "No existe el alumno."), 
	NO_EXISTE_ASIGNATURA(4, "No existe la asignatura."),
	NO_EXISTE_CURSO(5, "No existe el curso");
	
	private int id;
	private String description;
	
	private RegisterStatus(int id, String description)
	{
		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}
	
	

}
