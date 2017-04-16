package colegio.util;

import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;

public class OTColegioCurso {

	private R_Colegio colegio;
	private R_Curso curso;
	public OTColegioCurso(R_Colegio colegio, R_Curso curso) {
		super();
		this.colegio = colegio;
		this.curso = curso;
	}

	public R_Colegio getColegio() {
		return colegio;
	}

	public void setColegio(R_Colegio colegio) {
		this.colegio = colegio;
	}

	public R_Curso getCurso() {
		return curso;
	}

	public void setCurso(R_Curso curso) {
		this.curso = curso;
	}

	@Override
	public String toString() {
		return colegio  + "-" + curso;
	}

	public static class Builder {
		private R_Colegio colegio;
		private R_Curso curso;

		public Builder colegio(R_Colegio colegio) {
			this.colegio = colegio;
			return this;
		}

		public Builder curso(R_Curso curso) {
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
