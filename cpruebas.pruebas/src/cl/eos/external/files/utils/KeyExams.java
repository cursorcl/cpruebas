package cl.eos.external.files.utils;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Curso;

public class KeyExams {
	public Asignatura asignatura;
	public Curso curso;

	public KeyExams(Asignatura asignatura, Curso curso) {
		super();
		this.asignatura = asignatura;
		this.curso = curso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asignatura == null) ? 0 : asignatura.hashCode());
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
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
		KeyExams other = (KeyExams) obj;
		if (asignatura == null) {
			if (other.asignatura != null)
				return false;
		} else if (!asignatura.getId().equals(other.asignatura.getId()))
			return false;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.getId().equals(other.curso.getId()))
			return false;
		return true;
	}

}