package ot;

import cl.eos.restful.tables.R_Objetivo;

/**
 * Contiene el resultado de un alumno, clasificando la cantidad de buenas que
 * tiene por objetivo.
 * 
 * @author cursor
 */
public class XItemObjetivo {

    R_Objetivo objetivo = null;
    int buenas = 0;
    int nroPreguntas = 0;
    String preguntas = "";
    String ejesAsociados = "";
    String habilidades = "";

    public void add(XItemObjetivo item) {
        nroPreguntas += item.nroPreguntas;
        buenas += item.buenas;

        if (item.getEjesAsociados() != null) {
            String[] ejes = item.getEjesAsociados().split("\n");
            for (String eje : ejes) {
                if (!ejesAsociados.contains(eje)) {
                    ejesAsociados = ejesAsociados + (ejesAsociados == "" ? "" : "\n") + eje;
                }
            }
        }

        if (item.getHabilidades() != null) {
            String[] arr_habilidades = item.getHabilidades().split("\n");
            for (String habilidad : arr_habilidades) {
                if (!habilidades.contains(habilidad)) {
                    habilidades = habilidades + (habilidades == "" ? "" : "\n") + habilidad;
                }
            }
        }

        if (item.getPreguntas() != null) {
            String[] arr_preguntas = item.getPreguntas().split("-");
            for (String pregunta : arr_preguntas) {
                if (!preguntas.contains(pregunta)) {
                    preguntas = preguntas + (preguntas == "" ? "" : "-") + pregunta;
                }
            }
        }

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
        final XItemObjetivo other = (XItemObjetivo) obj;
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

    public R_Objetivo getObjetivo() {
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

    public void setObjetivo(R_Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public void substract(XItemObjetivo item) {
        nroPreguntas -= item.nroPreguntas;
        buenas -= item.buenas;
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

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return objetivo.getName();
    }



    public static class Builder {
        private R_Objetivo objetivo;
        int buenas = 0;
        int nroPreguntas = 0;
        String preguntas = "";
        String ejesAsociados = "";
        String habilidades = "";

        public Builder objetivo(R_Objetivo objetivo) {
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

        public Builder preguntas(String preguntas) {
            this.preguntas = preguntas;
            return this;
        }

        public Builder ejesAsociados(String ejesAsociados) {
            this.ejesAsociados = ejesAsociados;
            return this;
        }

        public Builder habilidades(String habilidades) {
            this.habilidades = habilidades;
            return this;
        }

        public XItemObjetivo build() {
            XItemObjetivo xItemObjetivo = new XItemObjetivo();
            xItemObjetivo.objetivo = objetivo;
            xItemObjetivo.buenas = buenas;
            xItemObjetivo.nroPreguntas = nroPreguntas;
            xItemObjetivo.preguntas = preguntas;
            xItemObjetivo.ejesAsociados = ejesAsociados;
            xItemObjetivo.habilidades = habilidades;
            return xItemObjetivo;
        }
    }
}
