package comparativo.colegio.eje.habilidad.x.curso;

import cl.eos.persistence.models.Curso;

/**
 * Contiene la cantidad de alumnos de un curso en cada rango de evaluacion para
 * un eje o habilidad.
 * 
 * @author eosorio
 *
 */
public class OTCursoRangos {
	private Curso curso;
	private int[] nroAlumnosXEjeHab;

	public OTCursoRangos() {
	}

	public OTCursoRangos(Curso curso, int[] nroAlumnosXEjeHab) {
		super();
		this.curso = curso;
		this.nroAlumnosXEjeHab = nroAlumnosXEjeHab;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public int[] getNroAlumnosXEjeHab() {
		return nroAlumnosXEjeHab;
	}

	public void setNroAlumnosXEjeHab(int[] nroAlumnosXEjeHab) {
		this.nroAlumnosXEjeHab = nroAlumnosXEjeHab;
	}
}
