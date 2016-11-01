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
 * @author eosorio
 */
public class XItemTablaObjetivo {

    /**
     * El objetivo para el que se asocian los elementos.
     */
    private Objetivo objetivo;

    /**
     * Contiene los valores de todos los cursos.
     */
    List<XItemObjetivo> items = new ArrayList<>();


    private XItemTablaObjetivo(Builder builder) {
        this.objetivo = builder.objetivo;
        this.items = builder.items;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final XItemTablaObjetivo other = (XItemTablaObjetivo) obj;
        if (objetivo == null) {
            if (other.objetivo != null)
                return false;
        } else if (!objetivo.equals(other.objetivo))
            return false;
        return true;
    }

    public List<XItemObjetivo> getItems() {
        return items;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (objetivo == null ? 0 : objetivo.hashCode());
        return result;
    }

    public void setItems(List<XItemObjetivo> items) {
        this.items = items;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public static class Builder {
        private Objetivo objetivo;
        private List<XItemObjetivo> items = new ArrayList<>();

        public Builder objetivo(Objetivo objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public Builder items(List<XItemObjetivo> items) {
            this.items = items;
            return this;
        }

        public XItemTablaObjetivo build() {
            return new XItemTablaObjetivo(this);
        }
    }

}
