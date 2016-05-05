package comparativo.colegio.eje.habilidad.x.curso;

import java.util.Arrays;
import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EvaluacionEjeTematico;

public class OTUnCursoUnEjeHabilidad {
	private IEntity ejeHabilidad;
	private float nroPreguntas;
	private int nroAlumnos;
	private float[] buenasPorAlumno;
	
	private int[] alumnosPorRango;

	public OTUnCursoUnEjeHabilidad(IEntity ejeHabilidad, int nroAlumnos) {
		super();
		this.ejeHabilidad = ejeHabilidad;
		this.nroAlumnos = nroAlumnos;
		buenasPorAlumno = new float[this.nroAlumnos];
		Arrays.fill(buenasPorAlumno, 0);
	}

	public IEntity getEjeHabilidad() {
		return ejeHabilidad;
	}

	public void setEjeHabilidad(IEntity ejeHabilidad) {
		this.ejeHabilidad = ejeHabilidad;
	}

	public float getNroPreguntas() {
		return nroPreguntas;
	}

	public void setNroPreguntas(float nroPreguntas) {
		this.nroPreguntas = nroPreguntas;
	}

	public int getNroAlumnos() {
		return nroAlumnos;
	}

	public void setNroAlumnos(int nroAlumnos) {
		this.nroAlumnos = nroAlumnos;
		buenasPorAlumno = new float[this.nroAlumnos];
		Arrays.fill(buenasPorAlumno, 0);		
	}

	public float[] getBuenasPorAlumno() {
		return buenasPorAlumno;
	}

	public void setBuenasPorAlumno(float[] buenasPorAlumno) {
		this.buenasPorAlumno = buenasPorAlumno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ejeHabilidad == null) ? 0 : ejeHabilidad.hashCode());
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
		OTUnCursoUnEjeHabilidad other = (OTUnCursoUnEjeHabilidad) obj;
		if (ejeHabilidad == null) {
			if (other.ejeHabilidad != null)
				return false;
		} else if (!ejeHabilidad.equals(other.ejeHabilidad))
			return false;
		return true;
	}

	
	/**
	 * Realiza el calculo de cuantos alumnos hay por rango en un curso.
	 * @param evalEjeHab Los porcentajes de rangos para poder realizar el analisis
	 */
	public int[] calculateAlumnosXRango( List<EvaluacionEjeTematico> evalEjeHab)
	{
		alumnosPorRango = new int[evalEjeHab.size()];
		Arrays.fill(alumnosPorRango, 0);
		for(int n = 0; n < nroAlumnos; n++)
		{
			float porcentaje = 100f * buenasPorAlumno[n] / nroPreguntas;
			for(int idx = 0; idx < evalEjeHab.size(); idx++)
			{
				EvaluacionEjeTematico eval = evalEjeHab.get(idx);
				if(eval.isInside(porcentaje))
				{
					alumnosPorRango[idx] = alumnosPorRango[idx] + 1; 
				}
			}
		}
		return alumnosPorRango;
	}
	
}
