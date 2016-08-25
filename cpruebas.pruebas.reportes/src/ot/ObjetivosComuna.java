package ot;

import java.util.ArrayList;
import java.util.List;

/**
 * Contiene el resultado de cada uno de los items que conforman el colegio.
 * @author colegio
 */
public class ObjetivosComuna implements IResultado{

    String comuna;
    List<ObjetivosColegio> objetivosColegio = new ArrayList<>();

    public ObjetivosComuna() {
        comuna = "--";
    }
    
    public List<ObjetivosColegio> getObjetivosColegio() {
        return objetivosColegio;
    }

    public void setObjetivosColegio(List<ObjetivosColegio> objetivosColegio) {
        this.objetivosColegio = objetivosColegio;
    }

    public String getComuna() {
        return comuna;
    }

    public void setColegio(String comuna) {
        this.comuna = comuna;
    }

    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((comuna == null) ? 0 : comuna.hashCode());
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
        ObjetivosComuna other = (ObjetivosComuna) obj;
        if (comuna == null) {
            if (other.comuna != null)
                return false;
        } else if (!comuna.equals(other.comuna))
            return false;
        return true;
    }



    public static class Builder {
        private List<ObjetivosColegio> objetivosColegio;
        private String comuna;

        public Builder objetivosComuna(List<ObjetivosColegio> objetivosColegio) {
            this.objetivosColegio = objetivosColegio;
            return this;
        }

        public Builder comuna(String comuna) {
            this.comuna =  comuna;
            return this;
        }

        public ObjetivosComuna build() {
            ObjetivosComuna objColegio = new ObjetivosComuna();
            objColegio.objetivosColegio = objetivosColegio;
            objColegio.comuna = comuna;
            return objColegio;
        }
    }



    @Override
    public List<TitledItemObjetivo> getResultados() {
        // TODO Auto-generated method stub
        return null;
    }
    

}
