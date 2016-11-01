package ot.objetivos;

import java.util.ArrayList;
import java.util.List;

import ot.IResultado;
import ot.TitledItemObjetivo;

/**
 * Contiene el resultado de cada uno de los items que conforman el colegio.
 * 
 * @author colegio
 */
public class ObjetivosComuna implements IResultado {

    public static class Builder {
        private List<ObjetivosColegio> objetivosColegio;
        private String comuna;

        public ObjetivosComuna build() {
            final ObjetivosComuna objColegio = new ObjetivosComuna();
            objColegio.objetivosColegio = objetivosColegio;
            objColegio.comuna = comuna;
            return objColegio;
        }

        public Builder comuna(String comuna) {
            this.comuna = comuna;
            return this;
        }

        public Builder objetivosComuna(List<ObjetivosColegio> objetivosColegio) {
            this.objetivosColegio = objetivosColegio;
            return this;
        }
    }

    String comuna;

    List<ObjetivosColegio> objetivosColegio = new ArrayList<>();

    public ObjetivosComuna() {
        comuna = "--";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ObjetivosComuna other = (ObjetivosComuna) obj;
        if (comuna == null) {
            if (other.comuna != null)
                return false;
        } else if (!comuna.equals(other.comuna))
            return false;
        return true;
    }

    public String getComuna() {
        return comuna;
    }

    public List<ObjetivosColegio> getObjetivosColegio() {
        return objetivosColegio;
    }

    @Override
    public List<TitledItemObjetivo> getResultados() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (comuna == null ? 0 : comuna.hashCode());
        return result;
    }

    public void setColegio(String comuna) {
        this.comuna = comuna;
    }

    public void setObjetivosColegio(List<ObjetivosColegio> objetivosColegio) {
        this.objetivosColegio = objetivosColegio;
    }

}
