package cl.eos.ot;

public enum EEvaluados {

	TOTAL_EVA(1, "Total Alumnos evaluados"), 
	TOTAL_INF(2, "Total Alumnos informados"), 
	VALIDACION(3, "% Validación muestra");

	private int id;
	private String name;

	private EEvaluados(int id, String name) {
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
