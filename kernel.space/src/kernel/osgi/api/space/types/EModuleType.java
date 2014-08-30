package kernel.osgi.api.space.types;

public enum EModuleType {

	ADMINISTRACION(1, "Administración"), REPORTES(2, "Reportes");

	private int id;
	private String name;

	private EModuleType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
