package comparativo.colegio.eje.habilidad.x.curso;

import cl.eos.restful.tables.R_TipoCurso;

/**
 * Contiene la cantidad de alumnos de un curso en cada rango de evaluacion para
 * un eje o habilidad.
 *
 * @author eosorio
 *
 */
public class OTTipoCursoRangos {
    private R_TipoCurso tipoCurso;
    private int[] nroAlumnosXEjeHab;

    public OTTipoCursoRangos() {
    }

    public OTTipoCursoRangos(R_TipoCurso tipoCurso, int[] nroAlumnosXEjeHab) {
        super();
        this.tipoCurso = tipoCurso;
        this.nroAlumnosXEjeHab = nroAlumnosXEjeHab;
    }

    /**
     * Suma la cantidad de alumnos desde el {@link OTTipoCursoRangos} a los
     * existentes en this.
     *
     * @param ot
     *            El {@link OTTipoCursoRangos} del que se quiere obtener los
     *            valores a sumar.
     */
    public void add(OTTipoCursoRangos ot) {
        final int[] value = ot.getNroAlumnosXEjeHab();
        for (int n = 0; n < nroAlumnosXEjeHab.length; n++) {
            nroAlumnosXEjeHab[n] += value[n];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTTipoCursoRangos other = (OTTipoCursoRangos) obj;
        if (tipoCurso == null) {
            if (other.tipoCurso != null)
                return false;
        } else if (!tipoCurso.equals(other.tipoCurso))
            return false;
        return true;
    }

    public int[] getNroAlumnosXEjeHab() {
        return nroAlumnosXEjeHab;
    }

    public R_TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (tipoCurso == null ? 0 : tipoCurso.hashCode());
        return result;
    }

    public void setNroAlumnosXEjeHab(int[] nroAlumnosXEjeHab) {
        this.nroAlumnosXEjeHab = nroAlumnosXEjeHab;
    }

    public void setTipoCurso(R_TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

}
