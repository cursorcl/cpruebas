package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SEvaluacionEjeTematico extends AEntity {

    private static final long serialVersionUID = -7756547051562574561L;
    private Long id;
    private String name;
    private Float nroRangoMin = 0F;
    private Float nroRangoMax = 0F;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SEvaluacionEjeTematico other = (SEvaluacionEjeTematico) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Float getNroRangoMax() {
        return nroRangoMax;
    }

    public Float getNroRangoMin() {
        return nroRangoMin;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    public boolean isInside(Float porcentaje) {
        return porcentaje != null && porcentaje >= getNroRangoMin() && porcentaje <= getNroRangoMax();
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNroRangoMax(Float nroRangoMax) {
        this.nroRangoMax = nroRangoMax;
    }

    public void setNroRangoMin(Float nroRangoMin) {
        this.nroRangoMin = nroRangoMin;
    }

    @Override
    public boolean validate() {
        return false;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Float nroRangoMin;
        private Float nroRangoMax;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nroRangoMin(Float nroRangoMin) {
            this.nroRangoMin = nroRangoMin;
            return this;
        }

        public Builder nroRangoMax(Float nroRangoMax) {
            this.nroRangoMax = nroRangoMax;
            return this;
        }

        public SEvaluacionEjeTematico build() {
            SEvaluacionEjeTematico sEvaluacionEjeTematico = new SEvaluacionEjeTematico();
            sEvaluacionEjeTematico.id = id;
            sEvaluacionEjeTematico.name = name;
            sEvaluacionEjeTematico.nroRangoMin = nroRangoMin;
            sEvaluacionEjeTematico.nroRangoMax = nroRangoMax;
            return sEvaluacionEjeTematico;
        }
    }
}
