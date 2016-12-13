package cl.eos.view.ots.resumenxalumno.eje.habilidad;

import cl.eos.persistence.models.SHabilidad;

/**
 * Para una prueba, contiene una habilidad y la cantidad de preguntas que hay de
 * dicha habilidad.
 *
 * @author curso_000
 *
 */
public class OTHabilidadX {
    private SHabilidad hab;
    private int cantidadPreguntas;

    public OTHabilidadX(SHabilidad hab) {
        super();
        this.hab = hab;
    }

    public int getCantidadPreguntas() {
        return cantidadPreguntas;
    }

    public SHabilidad getEje() {
        return hab;
    }

    public boolean itIsTheSame(SHabilidad hab) {
        return this.hab.equals(hab);
    }

    public void setCantidadPreguntas(int cantidadPreguntas) {
        this.cantidadPreguntas = cantidadPreguntas;
    }

    public void setEje(SHabilidad hab) {
        this.hab = hab;
    }

}
