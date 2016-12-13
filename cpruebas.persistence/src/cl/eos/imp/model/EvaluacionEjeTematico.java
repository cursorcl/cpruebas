package cl.eos.imp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "evaluacionejetematico")
@NamedQueries({ @NamedQuery(name = "SEvaluacionEjeTematico.findAll", query = "SELECT e FROM evaluacionejetematico e") })
public class EvaluacionEjeTematico extends AEntity {

    /**
     * Numero serial.
     */
    private static final long serialVersionUID = -7756547051562574561L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        final EvaluacionEjeTematico other = (EvaluacionEjeTematico) obj;
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

}
