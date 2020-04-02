package cl.eos.external.files.utils;

import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.Curso;

public class KeyEvaluation {
	public Prueba prueba;
	public Curso curso;

	public KeyEvaluation(Prueba prueba, Curso curso) {
		super();
		this.prueba = prueba;
		this.curso = curso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prueba == null) ? 0 : prueba.hashCode());
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
		KeyEvaluation other = (KeyEvaluation) obj;
		if (prueba == null) {
			if (other.prueba != null)
				return false;
		} else if (!prueba.getId().equals(other.prueba.getId()))
			return false;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.getId().equals(other.curso.getId()))
			return false;
		return true;
	}

}