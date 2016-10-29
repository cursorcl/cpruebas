package ot;

import java.util.ArrayList;
import java.util.List;

import cl.eos.persistence.models.Objetivo;

/**
 * Representa un registro del reporte final.
 *
 * Por cada objetivo, se tiene las preguntas, ejes y habilidades asociados.
 *
 * Contiene columnas (cursos, colegios, etc.) de acuerdo a lo que corresponde al
 * reporte.
 *
 * @author colegio
 */
public class ItemTablaObjetivo {

    public static class Builder {
        private Objetivo objetivo;
        private String preguntas = "";
        private String ejesAsociados = "";
        private String habilidades = "";
        private List<ItemObjetivo> items = new ArrayList<>();

        public ItemTablaObjetivo build() {
            return new ItemTablaObjetivo(this);
        }

        public Builder ejesAsociados(String ejesAsociados) {
            this.ejesAsociados = ejesAsociados;
            return this;
        }

        public Builder habilidades(String habilidades) {
            this.habilidades = habilidades;
            return this;
        }

        public Builder items(List<ItemObjetivo> items) {
            this.items = items;
            return this;
        }

        public Builder objetivo(Objetivo objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public Builder preguntas(String preguntas) {
            this.preguntas = preguntas;
            return this;
        }
    }

    private Objetivo objetivo;
    private String preguntas = "";
    private String ejesAsociados = "";
    private String habilidades = "";

    /**
     * Contiene los valores de todos los cursos.
     */
    List<ItemObjetivo> items = new ArrayList<>();

    private ItemTablaObjetivo(Builder builder) {
        objetivo = builder.objetivo;
        preguntas = builder.preguntas;
        ejesAsociados = builder.ejesAsociados;
        habilidades = builder.habilidades;
        items = builder.items;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ItemTablaObjetivo other = (ItemTablaObjetivo) obj;
        if (objetivo == null) {
            if (other.objetivo != null)
                return false;
        } else if (!objetivo.equals(other.objetivo))
            return false;
        return true;
    }

    public String getEjesAsociados() {
        return ejesAsociados;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public List<ItemObjetivo> getItems() {
        return items;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public String getPreguntas() {
        return preguntas;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (objetivo == null ? 0 : objetivo.hashCode());
        return result;
    }

    public void setEjesAsociados(String ejesAsociados) {
        this.ejesAsociados = ejesAsociados;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public void setItems(List<ItemObjetivo> items) {
        this.items = items;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public void setPreguntas(String preguntas) {
        this.preguntas = preguntas;
    }
}
