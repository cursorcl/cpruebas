package cl.eos.persistence.models;

import java.util.Collection;

import cl.eos.persistence.AEntity;

public class SNivelEvaluacion extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer nroRangos;
    private Collection<SRangoEvaluacion> rangos;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getNroRangos() {
        return nroRangos;
    }

    public SRangoEvaluacion getRango(float porcentaje) {
        SRangoEvaluacion result = null;
        int n = 0;

        for (final SRangoEvaluacion rango : rangos) {
            if (n == 0) {
                if (porcentaje < rango.getMaximo()) {
                    result = rango;
                    break;
                }
            } else if (n == nroRangos - 1) {
                if (porcentaje >= rango.getMinimo()) {
                    result = rango;
                    break;
                }
            } else {
                if (porcentaje >= rango.getMinimo() && porcentaje < rango.getMaximo()) {
                    result = rango;
                    break;
                }
            }
            n++;
        }
        return result;
    }

    public Collection<SRangoEvaluacion> getRangos() {
        return rangos;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNroRangos(Integer nroRangos) {
        this.nroRangos = nroRangos;
    }

    public void setRangos(Collection<SRangoEvaluacion> rangos) {
        this.rangos = rangos;
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
        private Integer nroRangos;
        private Collection<SRangoEvaluacion> rangos;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nroRangos(Integer nroRangos) {
            this.nroRangos = nroRangos;
            return this;
        }

        public Builder rangos(Collection<SRangoEvaluacion> rangos) {
            this.rangos = rangos;
            return this;
        }

        public SNivelEvaluacion build() {
            SNivelEvaluacion sNivelEvaluacion = new SNivelEvaluacion();
            sNivelEvaluacion.id = id;
            sNivelEvaluacion.name = name;
            sNivelEvaluacion.nroRangos = nroRangos;
            sNivelEvaluacion.rangos = rangos;
            return sNivelEvaluacion;
        }
    }
}
