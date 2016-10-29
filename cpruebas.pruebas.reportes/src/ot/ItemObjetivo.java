package ot;

import cl.eos.persistence.models.Objetivo;

/**
 * Contiene el resultado de un alumno, clasificando la cantidad de buenas que
 * tiene por objetivo.
 * 
 * @author cursor
 */
public class ItemObjetivo {

    public static class Builder {
        private Objetivo objetivo;
        private int buenas;
        private int nroPreguntas;

        public Builder buenas(int buenas) {
            this.buenas = buenas;
            return this;
        }

        public ItemObjetivo build() {
            final ItemObjetivo objxAlumno = new ItemObjetivo();
            objxAlumno.objetivo = objetivo;
            objxAlumno.buenas = buenas;
            objxAlumno.nroPreguntas = nroPreguntas;
            return objxAlumno;
        }

        public Builder nroPreguntas(int nroPreguntas) {
            this.nroPreguntas = nroPreguntas;
            return this;
        }

        public Builder objetivo(Objetivo objetivo) {
            this.objetivo = objetivo;
            return this;
        }
    }

    Objetivo objetivo = null;
    int buenas = 0;

    int nroPreguntas = 0;

    public void add(ItemObjetivo item) {
        nroPreguntas += item.nroPreguntas;
        buenas += item.buenas;
    }

    public void addBuena() {
        addBuenas(1);
    }

    public void addBuenas(int nroBuenas) {
        buenas += nroBuenas;
    }

    public void addPregunta() {
        addPreguntas(1);
    }

    public void addPreguntas(int nroPreguntas) {
        this.nroPreguntas += nroPreguntas;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ItemObjetivo other = (ItemObjetivo) obj;
        if (objetivo == null) {
            if (other.objetivo != null)
                return false;
        } else if (!objetivo.equals(other.objetivo))
            return false;
        return true;
    }

    public int getBuenas() {
        return buenas;
    }

    public int getNroPreguntas() {
        return nroPreguntas;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public float getPorcentajeAprobacion() {
        if (getNroPreguntas() == 0)
            return 0f;

        return (float) getBuenas() / (float) getNroPreguntas() * 100f;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (objetivo == null ? 0 : objetivo.hashCode());
        return result;
    }

    public void setBuenas(int buenas) {
        this.buenas = buenas;
    }

    public void setNroPreguntas(int nroPreguntas) {
        this.nroPreguntas = nroPreguntas;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public void substract(ItemObjetivo item) {
        nroPreguntas -= item.nroPreguntas;
        buenas -= item.buenas;
    }
}
