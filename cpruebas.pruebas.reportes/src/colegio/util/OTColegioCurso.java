package colegio.util;

import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;

public class OTColegioCurso {

	private Colegio colegio;
	private Curso curso;

	public OTColegioCurso(Colegio colegio, Curso curso) {
		super();
		this.colegio = colegio;
		this.curso = curso;
	}

	public Colegio getColegio() {
		return colegio;
	}

	public void setColegio(Colegio colegio) {
		this.colegio = colegio;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@Override
	public String toString() {
		return colegio.getName() + "-" + curso.toString();
	}

	public static class Builder {
		private Colegio colegio;
		private Curso curso;

		public Builder colegio(Colegio colegio) {
			this.colegio = colegio;
			return this;
		}

		public Builder curso(Curso curso) {
			this.curso = curso;
			return this;
		}

		public OTColegioCurso build() {
			return new OTColegioCurso(this);
		}
	}

	private OTColegioCurso(Builder builder) {
		this.colegio = builder.colegio;
		this.curso = builder.curso;
	}
}
