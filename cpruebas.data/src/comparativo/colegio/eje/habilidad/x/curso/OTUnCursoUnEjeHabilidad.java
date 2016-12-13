package comparativo.colegio.eje.habilidad.x.curso;

import java.util.Arrays;
import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SEvaluacionEjeTematico;
import cl.eos.persistence.models.SHabilidad;

/**
 * Representa una entrade de {@link SEjeTematico} o {@link SHabilidad} asociado a
 * el número de preguntas en una prueba junto a la cantidad de alumnos que
 * rindieron la prueba y las buenas por alumno para dicho {@link SEjeTematico} o
 * {@link SHabilidad}.
 *
 * @author curso
 */
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

    /**
     * Realiza el calculo de cuantos alumnos hay por rango en un curso.
     *
     * @param evalEjeHab
     *            Los porcentajes de rangos para poder realizar el analisis
     */
    public int[] calculateAlumnosXRango(List<SEvaluacionEjeTematico> evalEjeHab) {
        alumnosPorRango = new int[evalEjeHab.size()];
        Arrays.fill(alumnosPorRango, 0);
        for (int n = 0; n < nroAlumnos; n++) {
            final float porcentaje = 100f * buenasPorAlumno[n] / nroPreguntas;
            for (int idx = 0; idx < evalEjeHab.size(); idx++) {
                final SEvaluacionEjeTematico eval = evalEjeHab.get(idx);
                if (eval.isInside(porcentaje)) {
                    alumnosPorRango[idx] = alumnosPorRango[idx] + 1;
                }
            }
        }
        return alumnosPorRango;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTUnCursoUnEjeHabilidad other = (OTUnCursoUnEjeHabilidad) obj;
        if (ejeHabilidad == null) {
            if (other.ejeHabilidad != null)
                return false;
        } else if (!ejeHabilidad.equals(other.ejeHabilidad))
            return false;
        return true;
    }

    public float[] getBuenasPorAlumno() {
        return buenasPorAlumno;
    }

    public IEntity getEjeHabilidad() {
        return ejeHabilidad;
    }

    public int getNroAlumnos() {
        return nroAlumnos;
    }

    public float getNroPreguntas() {
        return nroPreguntas;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (ejeHabilidad == null ? 0 : ejeHabilidad.hashCode());
        return result;
    }

    public void setBuenasPorAlumno(float[] buenasPorAlumno) {
        this.buenasPorAlumno = buenasPorAlumno;
    }

    public void setEjeHabilidad(IEntity ejeHabilidad) {
        this.ejeHabilidad = ejeHabilidad;
    }

    public void setNroAlumnos(int nroAlumnos) {
        this.nroAlumnos = nroAlumnos;
        buenasPorAlumno = new float[this.nroAlumnos];
        Arrays.fill(buenasPorAlumno, 0);
    }

    public void setNroPreguntas(float nroPreguntas) {
        this.nroPreguntas = nroPreguntas;
    }

}
