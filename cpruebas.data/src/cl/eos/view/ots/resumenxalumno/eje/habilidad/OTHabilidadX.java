package cl.eos.view.ots.resumenxalumno.eje.habilidad;

import cl.eos.persistence.models.Habilidad;

/**
 * Para una prueba, contiene una habilidad y la cantidad de preguntas que hay de
 * dicha habilidad.
 *
 * @author curso_000
 *
 */
public class OTHabilidadX {
    private Habilidad hab;
    private int cantidadPreguntas;

    public OTHabilidadX(Habilidad hab) {
        super();
        this.hab = hab;
    }

    public int getCantidadPreguntas() {
        return cantidadPreguntas;
    }

    public Habilidad getEje() {
        return hab;
    }

    public boolean itIsTheSame(Habilidad hab) {
        return this.hab.equals(hab);
    }

    public void setCantidadPreguntas(int cantidadPreguntas) {
        this.cantidadPreguntas = cantidadPreguntas;
    }

    public void setEje(Habilidad hab) {
        this.hab = hab;
    }

}
