package cl.eos.view.ots.resumenxalumno.eje.habilidad;

import cl.eos.restful.tables.R_Habilidad;

/**
 * Para una prueba, contiene una habilidad y la cantidad de preguntas que hay de
 * dicha habilidad.
 *
 * @author curso_000
 *
 */
public class OTHabilidadX {
    private R_Habilidad hab;
    private int cantidadPreguntas;

    public OTHabilidadX(R_Habilidad hab) {
        super();
        this.hab = hab;
    }

    public int getCantidadPreguntas() {
        return cantidadPreguntas;
    }

    public R_Habilidad getEje() {
        return hab;
    }

    public boolean itIsTheSame(R_Habilidad hab) {
        return this.hab.equals(hab);
    }

    public void setCantidadPreguntas(int cantidadPreguntas) {
        this.cantidadPreguntas = cantidadPreguntas;
    }

    public void setEje(R_Habilidad hab) {
        this.hab = hab;
    }

}
