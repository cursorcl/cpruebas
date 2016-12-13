package comparativo.colegio.eje.habilidad.x.curso;

import cl.eos.persistence.models.SCurso;

/**
 * Contiene la cantidad de alumnos de un curso en cada rango de evaluacion para
 * un eje o habilidad.
 *
 * @author eosorio
 *
 */
public class OTCursoRangos {
    private SCurso curso;
    private int[] nroAlumnosXEjeHab;

    public OTCursoRangos() {
    }

    public OTCursoRangos(SCurso curso, int[] nroAlumnosXEjeHab) {
        super();
        this.curso = curso;
        this.nroAlumnosXEjeHab = nroAlumnosXEjeHab;
    }

    public SCurso getCurso() {
        return curso;
    }

    public int[] getNroAlumnosXEjeHab() {
        return nroAlumnosXEjeHab;
    }

    public void setCurso(SCurso curso) {
        this.curso = curso;
    }

    public void setNroAlumnosXEjeHab(int[] nroAlumnosXEjeHab) {
        this.nroAlumnosXEjeHab = nroAlumnosXEjeHab;
    }
}
