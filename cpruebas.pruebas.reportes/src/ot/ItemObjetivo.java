package ot;

import cl.eos.persistence.models.Objetivo;

/**
 * Contiene el resultado de un alumno, clasificando la cantidad de buenas que tiene por objetivo. 
 * @author cursor
 */
public class ItemObjetivo {

    Objetivo objetivo = null;
    int buenas = 0;
    int nroPreguntas = 0;

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public int getBuenas() {
        return buenas;
    }

    public void setBuenas(int buenas) {
        this.buenas = buenas;
    }
    
    public void addBuena()
    {
        addBuenas(1);
    }
    public void addBuenas(int nroBuenas)
    {
        this.buenas += nroBuenas;
    }

    public int getNroPreguntas() {
        return nroPreguntas;
    }

    public void setNroPreguntas(int nroPreguntas) {
        this.nroPreguntas = nroPreguntas;
    }
    
    public void addPregunta()
    {
        addPreguntas(1);
    }
    public void addPreguntas(int nroPreguntas)
    {
        this.nroPreguntas += nroPreguntas;
    }

    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((objetivo == null) ? 0 : objetivo.hashCode());
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
        ItemObjetivo other = (ItemObjetivo) obj;
        if (objetivo == null) {
            if (other.objetivo != null)
                return false;
        } else if (!objetivo.equals(other.objetivo))
            return false;
        return true;
    }
    
    public void add(ItemObjetivo item)
    {
        nroPreguntas += item.nroPreguntas;
        buenas += item.buenas;
    }
    public void substract(ItemObjetivo item)
    {
        nroPreguntas -= item.nroPreguntas;
        buenas -= item.buenas;
    }

    public float getPorcentajeAprobacion()
    {
        if(getNroPreguntas() == 0)
            return 0f;
        
        return (float)getBuenas() / (float)getNroPreguntas() * 100f;
    }
    

    public static class Builder {
        private Objetivo objetivo;
        private int buenas;
        private int nroPreguntas;

        public Builder objetivo(Objetivo objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public Builder buenas(int buenas) {
            this.buenas = buenas;
            return this;
        }

        public Builder nroPreguntas(int nroPreguntas) {
            this.nroPreguntas = nroPreguntas;
            return this;
        }

        public ItemObjetivo build() {
            ItemObjetivo objxAlumno = new ItemObjetivo();
            objxAlumno.objetivo = objetivo;
            objxAlumno.buenas = buenas;
            objxAlumno.nroPreguntas = nroPreguntas;
            return objxAlumno;
        }
    }
}
