package cl.eos.ot;

import java.util.Collection;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Ciclo;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.TipoCurso;

public class OTCurso {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleStringProperty name = new SimpleStringProperty();
	private SimpleObjectProperty<Ciclo> ciclo = new SimpleObjectProperty<Ciclo>();
	private SimpleObjectProperty<Colegio> colegio = new SimpleObjectProperty<Colegio>();

	private SimpleObjectProperty<Collection<Alumno>> alumnos = new SimpleObjectProperty<Collection<Alumno>>();
	private SimpleObjectProperty<TipoCurso> tipoCurso = new SimpleObjectProperty<TipoCurso>();

	private Curso curso;

	public OTCurso(Curso curso) {
		this.curso = curso;
		this.id.set(curso.getId());
		this.name.set(curso.getName());
		this.ciclo.set(curso.getCiclo());
		this.colegio.set(curso.getColegio());
		this.alumnos.set(curso.getAlumnos());
		this.tipoCurso.set(curso.getTipoCurso());
	}

	public OTCurso() {
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

	public final SimpleStringProperty nameProperty() {
		return this.name;
	}

	public final java.lang.String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final java.lang.String name) {
		this.nameProperty().set(name);
	}

	public final SimpleObjectProperty<Ciclo> cicloProperty() {
		return this.ciclo;
	}

	public final cl.eos.persistence.models.Ciclo getCiclo() {
		return this.cicloProperty().get();
	}

	public final void setCiclo(final cl.eos.persistence.models.Ciclo ciclo) {
		this.cicloProperty().set(ciclo);
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

	public final SimpleObjectProperty<Collection<Alumno>> alumnosProperty() {
		return this.alumnos;
	}

	public final java.util.Collection<cl.eos.persistence.models.Alumno> getAlumnos() {
		return this.alumnosProperty().get();
	}

	public final void setAlumnos(
			final java.util.Collection<cl.eos.persistence.models.Alumno> alumnos) {
		this.alumnosProperty().set(alumnos);
	}

	public final SimpleObjectProperty<TipoCurso> tipoCursoProperty() {
		return this.tipoCurso;
	}

	public final cl.eos.persistence.models.TipoCurso getTipoCurso() {
		return this.tipoCursoProperty().get();
	}

	public final void setTipoCurso(
			final cl.eos.persistence.models.TipoCurso tipoCurso) {
		this.tipoCursoProperty().set(tipoCurso);
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
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
		OTCurso other = (OTCurso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}  else if (id.get() != other.id.get())
			return false;
		return true;
	}

}
