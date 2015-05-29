package cl.eos.view;

import java.util.Arrays;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Curso;


/**
 * Contiene la cantidad de alumnos de un curso en cada rango de evaluacion para un eje o habilidad.
 * @author eosorio
 *
 */
public class OTCursoRangos {
	private Curso curso;
	private int[] cantidadAlumnosEnRangos;
	
	public OTCursoRangos() {
		
	}
	
	
	public OTCursoRangos(Curso curso,
			int cantidadRangos) {
		super();
		this.curso = curso;
		this.cantidadAlumnosEnRangos = new int[cantidadRangos];
		Arrays.fill(cantidadAlumnosEnRangos, 0);
	}	
	
	public OTCursoRangos(Curso curso,
			int[] cantidadAlumnosEnRangos) {
		super();
		this.curso = curso;
		this.cantidadAlumnosEnRangos = cantidadAlumnosEnRangos;
	}

	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public int[] getCantidadAlumnosEnRangos() {
		return cantidadAlumnosEnRangos;
	}
	public void setCantidadAlumnosEnRangos(int[] cantidadAlumnosEnRangos) {
		this.cantidadAlumnosEnRangos = cantidadAlumnosEnRangos;
	}

	
	
}
