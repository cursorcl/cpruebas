package cl.eos.ot;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;

public class OTAlumno {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleStringProperty rut = new SimpleStringProperty();
	private SimpleStringProperty name = new SimpleStringProperty();
	private SimpleStringProperty paterno = new SimpleStringProperty();
	private SimpleStringProperty materno = new SimpleStringProperty();
	private SimpleStringProperty direccion = new SimpleStringProperty();
	private SimpleObjectProperty<Colegio> colegio = new SimpleObjectProperty<Colegio>();
	private SimpleObjectProperty<Curso> curso = new SimpleObjectProperty<Curso>();
	private Alumno alumno;

	public OTAlumno(Alumno alumno) {
		this.alumno = alumno;
		this.id.set(alumno.getId());
		this.rut.set(alumno.getRut());
		this.name.set(alumno.getName());
		this.paterno.set(alumno.getPaterno());
		this.materno.set(alumno.getMaterno());
		this.direccion.set(alumno.getDireccion());
		this.colegio.set(alumno.getColegio());
		this.curso.set(alumno.getCurso());

	}

	public OTAlumno() {
		// TODO Auto-generated constructor stub
	}

	public final SimpleLongProperty idProperty() {
		return this.id;
	}

	public final long getId() {
		return this.idProperty().get();
	}

	public final void setId(final long id) {
		this.idProperty().set(id);
	}

	public final SimpleStringProperty rutProperty() {
		return this.rut;
	}

	public final java.lang.String getRut() {
		return this.rutProperty().get();
	}

	public final void setRut(final java.lang.String rut) {
		this.rutProperty().set(rut);
	}

	public final SimpleStringProperty nameProperty() {
		return this.name;
	}

	public final java.lang.String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final java.lang.String name) {
		this.nameProperty().set(name);
	}

	public final SimpleStringProperty paternoProperty() {
		return this.paterno;
	}

	public final java.lang.String getPaterno() {
		return this.paternoProperty().get();
	}

	public final void setPaterno(final java.lang.String paterno) {
		this.paternoProperty().set(paterno);
	}

	public final SimpleStringProperty maternoProperty() {
		return this.materno;
	}

	public final java.lang.String getMaterno() {
		return this.maternoProperty().get();
	}

	public final void setMaterno(final java.lang.String materno) {
		this.maternoProperty().set(materno);
	}

	public final SimpleStringProperty direccionProperty() {
		return this.direccion;
	}

	public final java.lang.String getDireccion() {
		return this.direccionProperty().get();
	}

	public final void setDireccion(final java.lang.String direccion) {
		this.direccionProperty().set(direccion);
	}

	public final SimpleObjectProperty<Colegio> colegioProperty() {
		return this.colegio;
	}

	public final cl.eos.persistence.models.Colegio getColegio() {
		return this.colegioProperty().get();
	}

	public final void setColegio(final cl.eos.persistence.models.Colegio colegio) {
		this.colegioProperty().set(colegio);
	}

	public final SimpleObjectProperty<Curso> cursoProperty() {
		return this.curso;
	}

	public final cl.eos.persistence.models.Curso getCurso() {
		return this.cursoProperty().get();
	}

	public final void setCurso(final cl.eos.persistence.models.Curso curso) {
		this.cursoProperty().set(curso);
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OTAlumno other = (OTAlumno) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
