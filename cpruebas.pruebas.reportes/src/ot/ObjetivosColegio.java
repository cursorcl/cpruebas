package ot;

import java.util.ArrayList;
import java.util.List;

import cl.eos.persistence.models.Colegio;

/**
 * Contiene el resultado de cada uno de los items que conforman el colegio.
 * 
 * @author colegio
 */
public class ObjetivosColegio implements IResultado {

    public static class Builder {
        private List<ObjetivosCurso> objetivosCurso;
        private Colegio colegio;

        public ObjetivosColegio build() {
            final ObjetivosColegio objColegio = new ObjetivosColegio();
            objColegio.objetivosCurso = objetivosCurso;
            objColegio.colegio = colegio;
            return objColegio;
        }

        public Builder Colegio(Colegio colegio) {
            this.colegio = colegio;
            return this;
        }

        public Builder objetivosCurso(List<ObjetivosCurso> objetivosCurso) {
            this.objetivosCurso = objetivosCurso;
            return this;
        }
    }

    Colegio colegio;

    List<ObjetivosCurso> objetivosCurso = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ObjetivosColegio other = (ObjetivosColegio) obj;
        if (colegio == null) {
            if (other.colegio != null)
                return false;
        } else if (!colegio.equals(other.colegio))
            return false;
        return true;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public List<ObjetivosCurso> getObjetivosCurso() {
        return objetivosCurso;
    }

    @Override
    public List<TitledItemObjetivo> getResultados() {
        final List<TitledItemObjetivo> result = new ArrayList<>();
        objetivosCurso.forEach(o -> result.addAll(o.getResultados()));
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (colegio == null ? 0 : colegio.hashCode());
        return result;
    }

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    public void setObjetivosCurso(List<ObjetivosCurso> objetivosCurso) {
        this.objetivosCurso = objetivosCurso;
    }

}
