package cl.eos.external.files.utils;

import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RegisterForView extends Register {

	public ObjectProperty<Colegio> colegio = new SimpleObjectProperty<Colegio>();
	public ObjectProperty<Curso> curso = new SimpleObjectProperty<Curso>();
	public ObjectProperty<Asignatura> asignatura = new SimpleObjectProperty<Asignatura>();
	public ObjectProperty<RegisterStatus> status = new SimpleObjectProperty<RegisterStatus>();
	public ObjectProperty<Alumno> alumno = new SimpleObjectProperty<Alumno>();

	public RegisterForView(Register register) {
		this.idAsignatura.set(register.idAsignatura.get());
		this.idCurso.set(register.idCurso.get());
		this.idColegio.set(register.idColegio.get());
		this.rut.set(register.rut.get());
		this.nota.set(register.nota.get());
		this.respuestas.set(register.respuestas.get());
	}

	public final ObjectProperty<Colegio> colegioProperty() {
		return this.colegio;
	}

	public final Colegio getColegio() {
		return this.colegioProperty().get();
	}

	public final void setColegio(final Colegio colegio) {
		this.colegioProperty().set(colegio);
	}

	public final ObjectProperty<Curso> cursoProperty() {
		return this.curso;
	}

	public final Curso getCurso() {
		return this.cursoProperty().get();
	}

	public final void setCurso(final Curso curso) {
		this.cursoProperty().set(curso);
	}

	public final ObjectProperty<Asignatura> asignaturaProperty() {
		return this.asignatura;
	}

	public final Asignatura getAsignatura() {
		return this.asignaturaProperty().get();
	}

	public final void setAsignatura(final Asignatura asignatura) {
		this.asignaturaProperty().set(asignatura);
	}

	public final ObjectProperty<RegisterStatus> statusProperty() {
		return this.status;
	}

	public final RegisterStatus getStatus() {
		return this.statusProperty().get();
	}

	public final void setStatus(final RegisterStatus status) {
		this.statusProperty().set(status);
	}

	public final ObjectProperty<Alumno> alumnoProperty() {
		return this.alumno;
	}

	public final Alumno getAlumno() {
		return this.alumnoProperty().get();
	}

	public final void setAlumno(final Alumno alumno) {
		this.alumnoProperty().set(alumno);
	}

	@Override
	public String toString() {
		return String.format("%d/%d/%d/%s/%s/%3.1f/%s", idColegio.get(), idCurso.get(), idAsignatura.get(), rut.get(),
				respuestas.get(), nota.get(), status.get() == null ? "OK" : status.get().toString());
	}

}
