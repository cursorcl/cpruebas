package cl.eos.view;

import cl.eos.persistence.models.Curso;


/**
 * Contiene la cantidad de alumnos de un curso en cada rango de evaluacion para un eje o habilidad.
 * @author eosorio
 *
 */
public class OTCursoRangos {
	private Curso curso;
	private int cantidadPreguntas;
	private int cantidadBuenas;
	public OTCursoRangos() {
		
	}
	
	
	public OTCursoRangos(Curso curso) {
		super();
		this.curso = curso;
	}	
	

	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}


	public int getCantidadPreguntas() {
		return cantidadPreguntas;
	}


	public void setCantidadPreguntas(int cantidadPreguntas) {
		this.cantidadPreguntas = cantidadPreguntas;
	}


	public int getCantidadBuenas() {
		return cantidadBuenas;
	}


	public void setCantidadBuenas(int cantidadBuenas) {
		this.cantidadBuenas = cantidadBuenas;
	}
	
	
}
