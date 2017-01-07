package comparativo.colegio.eje.habilidad.x.curso;

import cl.eos.restful.tables.R_Curso;

/**
 * Contiene la cantidad de alumnos de un curso en cada rango de evaluacion para
 * un eje o habilidad.
 *
 * @author eosorio
 *
 */
public class OTCursoRangos {
    private R_Curso curso;
    private int[] nroAlumnosXEjeHab;

    public OTCursoRangos() {
    }

    public OTCursoRangos(R_Curso curso, int[] nroAlumnosXEjeHab) {
        super();
        this.curso = curso;
        this.nroAlumnosXEjeHab = nroAlumnosXEjeHab;
    }

    public R_Curso getCurso() {
        return curso;
    }

    public int[] getNroAlumnosXEjeHab() {
        return nroAlumnosXEjeHab;
    }

    public void setCurso(R_Curso curso) {
        this.curso = curso;
    }

    public void setNroAlumnosXEjeHab(int[] nroAlumnosXEjeHab) {
        this.nroAlumnosXEjeHab = nroAlumnosXEjeHab;
    }
}
