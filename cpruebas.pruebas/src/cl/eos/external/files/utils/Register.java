package cl.eos.external.files.utils;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Register {

	public SimpleLongProperty idColegio = new SimpleLongProperty();
	public SimpleLongProperty idCurso = new SimpleLongProperty();
	public SimpleLongProperty idAsignatura = new SimpleLongProperty();
	public SimpleStringProperty rut = new SimpleStringProperty();
	public SimpleStringProperty respuestas = new SimpleStringProperty();
	public SimpleFloatProperty nota = new SimpleFloatProperty(0f);

	@Override
	public String toString() {
		return String.format("%d/%d/%d/%s/%s/%%3.1f", idColegio.get(), idCurso.get(), idAsignatura.get(), rut.get(), respuestas.get(), nota.get());
	}

	public final SimpleLongProperty idColegioProperty() {
		return this.idColegio;
	}

	public final long getIdColegio() {
		return this.idColegioProperty().get();
	}

	public final void setIdColegio(final long idColegio) {
		this.idColegioProperty().set(idColegio);
	}

	public final SimpleLongProperty idCursoProperty() {
		return this.idCurso;
	}

	public final long getIdCurso() {
		return this.idCursoProperty().get();
	}

	public final void setIdCurso(final long idCurso) {
		this.idCursoProperty().set(idCurso);
	}

	public final SimpleLongProperty idAsignaturaProperty() {
		return this.idAsignatura;
	}

	public final long getIdAsignatura() {
		return this.idAsignaturaProperty().get();
	}

	public final void setIdAsignatura(final long idAsignatura) {
		this.idAsignaturaProperty().set(idAsignatura);
	}

	public final SimpleStringProperty rutProperty() {
		return this.rut;
	}

	public final String getRut() {
		return this.rutProperty().get();
	}

	public final void setRut(final String rut) {
		this.rutProperty().set(rut);
	}

	public final SimpleStringProperty respuestasProperty() {
		return this.respuestas;
	}

	public final String getRespuestas() {
		return this.respuestasProperty().get();
	}

	public final void setRespuestas(final String respuestas) {
		this.respuestasProperty().set(respuestas);
	}

	public final SimpleFloatProperty notaProperty() {
		return this.nota;
	}

	public final float getNota() {
		return this.notaProperty().get();
	}

	public final void setNota(final float nota) {
		this.notaProperty().set(nota);
	}

}