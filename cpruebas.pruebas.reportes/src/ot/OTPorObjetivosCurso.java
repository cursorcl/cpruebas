package ot;

import cl.eos.persistence.models.Objetivo;

/**
 * Clase que da soporte al reporte por objetivos de un solo curso.
 * 
 * @author curso
 */
public class OTPorObjetivosCurso {
    
    private Objetivo objetivo;
    private String preguntas = "";
    private String ejesAsociados ="";
    private String habilidadesAsociadas ="";
    private Float porcentajes = 0f;
    private int nroPreguntas = 0;

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public String getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(String preguntas) {
        this.preguntas = preguntas;
    }

    public String getEjesAsociados() {
        return ejesAsociados;
    }

    public void setEjesAsociados(String ejesAsociados) {
        this.ejesAsociados = ejesAsociados;
    }

    public String getHabilidadesAsociadas() {
        return habilidadesAsociadas;
    }

    public void setHabilidadesAsociadas(String habilidadesAsociadas) {
        this.habilidadesAsociadas = habilidadesAsociadas;
    }

    public Float getPorcentajes() {
        return porcentajes;
    }

    public void setPorcentajes(Float porcentajes) {
        this.porcentajes = porcentajes;
    }
    

    public int getNroPreguntas() {
        return nroPreguntas;
    }

    public void setNroPreguntas(int nroPreguntas) {
        this.nroPreguntas = nroPreguntas;
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
        OTPorObjetivosCurso other = (OTPorObjetivosCurso) obj;
        if (objetivo == null) {
            if (other.objetivo != null)
                return false;
        } else if (!objetivo.equals(other.objetivo))
            return false;
        return true;
    }

    public static class Builder {
        private Objetivo objetivo;
        private String preguntas = "";
        private String ejesAsociados = "";
        private String habilidadesAsociadas = "";
        private Float porcentajes = 0f;
        private int nroPreguntas = 0;
        
        public Builder objetivo(Objetivo objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public Builder preguntas(String preguntas) {
            this.preguntas = preguntas;
            return this;
        }

        public Builder ejesAsociados(String ejesAsociados) {
            this.ejesAsociados = ejesAsociados;
            return this;
        }

        public Builder habilidadesAsociadas(String habilidadesAsociadas) {
            this.habilidadesAsociadas = habilidadesAsociadas;
            return this;
        }

        public Builder porcentajes(Float porcentajes) {
            this.porcentajes = porcentajes;
            return this;
        }

        public Builder nroPreguntas(int nroPreguntas)
        {
            this.nroPreguntas = nroPreguntas;
            return this;
        }
        
        public OTPorObjetivosCurso build() {
            OTPorObjetivosCurso oTPorObjetivosCurso = new OTPorObjetivosCurso();
            oTPorObjetivosCurso.objetivo = objetivo;
            oTPorObjetivosCurso.preguntas = preguntas;
            oTPorObjetivosCurso.ejesAsociados = ejesAsociados;
            oTPorObjetivosCurso.habilidadesAsociadas = habilidadesAsociadas;
            oTPorObjetivosCurso.porcentajes = porcentajes;
            oTPorObjetivosCurso.nroPreguntas = nroPreguntas;
            return oTPorObjetivosCurso;
        }
    }
}
