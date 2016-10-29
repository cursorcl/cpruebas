package kernel.osgi.api.space.types;

public enum EModuleType {

    ADMINISTRACION(1, "Administraci�n"), REPORTES(2, "Reportes");

    private int id;
    private String name;

    private EModuleType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
