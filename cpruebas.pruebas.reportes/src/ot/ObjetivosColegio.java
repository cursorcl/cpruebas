package ot;

import java.util.ArrayList;
import java.util.List;

import cl.eos.persistence.models.Colegio;

/**
 * Contiene el resultado de cada uno de los items que conforman el colegio.
 * @author colegio
 */
public class ObjetivosColegio implements IResultado{
    
    Colegio colegio;
    List<ObjetivosCurso> objetivosCurso = new ArrayList<>();

    public List<ObjetivosCurso> getObjetivosCurso() {
        return objetivosCurso;
    }

    public void setObjetivosCurso(List<ObjetivosCurso> objetivosCurso) {
        this.objetivosCurso = objetivosCurso;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((colegio == null) ? 0 : colegio.hashCode());
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
        ObjetivosColegio other = (ObjetivosColegio) obj;
        if (colegio == null) {
            if (other.colegio != null)
                return false;
        } else if (!colegio.equals(other.colegio))
            return false;
        return true;
    }



    public static class Builder {
        private List<ObjetivosCurso> objetivosCurso;
        private Colegio colegio;

        public Builder objetivosCurso(List<ObjetivosCurso> objetivosCurso) {
            this.objetivosCurso = objetivosCurso;
            return this;
        }

        public Builder Colegio(Colegio colegio) {
            this.colegio = colegio;
            return this;
        }

        public ObjetivosColegio build() {
            ObjetivosColegio objColegio = new ObjetivosColegio();
            objColegio.objetivosCurso = objetivosCurso;
            objColegio.colegio = colegio;
            return objColegio;
        }
    }



    @Override
    public List<TitledItemObjetivo> getResultados() {
        List<TitledItemObjetivo> result = new ArrayList<>();
        objetivosCurso.forEach(o -> result.addAll(o.getResultados()));
        return result;
    }
    

}
