package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SRangoEvaluacion extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String abreviacion;
    private Float minimo;
    private Float maximo;
    private SNivelEvaluacion nivelEvaluacion;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SRangoEvaluacion other = (SRangoEvaluacion) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Float getMaximo() {
        return maximo;
    }

    public Float getMinimo() {
        return minimo;
    }

    @Override
    public String getName() {
        return name;
    }

    public SNivelEvaluacion getNivelEvaluacion() {
        return nivelEvaluacion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    /**
     * Indica si el valor esta dentro del rango.
     * 
     * @param porcentaje
     *            Porcentaje de logro
     * @return Verdadero si esta dentro.
     */
    public boolean isInside(float porcentaje) {
        boolean res = false;
        if (porcentaje > getMinimo() && porcentaje <= getMaximo()) {
            res = true;
        } else if (getMinimo() == 0 && porcentaje == 0) {
            res = true;
        }
        return res;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setMaximo(Float maximo) {
        this.maximo = maximo;
    }

    public void setMinimo(Float minimo) {
        this.minimo = minimo;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNivelEvaluacion(SNivelEvaluacion nivelEvaluacion) {
        this.nivelEvaluacion = nivelEvaluacion;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean validate() {
        return false;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String abreviacion;
        private Float minimo;
        private Float maximo;
        private SNivelEvaluacion nivelEvaluacion;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder abreviacion(String abreviacion) {
            this.abreviacion = abreviacion;
            return this;
        }

        public Builder minimo(Float minimo) {
            this.minimo = minimo;
            return this;
        }

        public Builder maximo(Float maximo) {
            this.maximo = maximo;
            return this;
        }

        public Builder nivelEvaluacion(SNivelEvaluacion nivelEvaluacion) {
            this.nivelEvaluacion = nivelEvaluacion;
            return this;
        }

        public SRangoEvaluacion build() {
            SRangoEvaluacion sRangoEvaluacion = new SRangoEvaluacion();
            sRangoEvaluacion.id = id;
            sRangoEvaluacion.name = name;
            sRangoEvaluacion.abreviacion = abreviacion;
            sRangoEvaluacion.minimo = minimo;
            sRangoEvaluacion.maximo = maximo;
            sRangoEvaluacion.nivelEvaluacion = nivelEvaluacion;
            return sRangoEvaluacion;
        }
    }
}
