package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "evaluacionejetematico")
@NamedQueries({ @NamedQuery(name = "EvaluacionEjeTematico.findAll", query = "SELECT e FROM evaluacionejetematico e") })
public class EvaluacionEjeTematico implements IEntity {
	
	/**
	 * Numero serial.
	 */
	private static final long serialVersionUID = -7756547051562574561L;
	@Id
	private Long id;
	private String name;
	private Float nroRangoMin = 0F;
	private Float nroRangoMax = 0F;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean validate() {
		return false;
	}

	public Float getNroRangoMin() {
		return nroRangoMin;
	}

	public void setNroRangoMin(Float nroRangoMin) {
		this.nroRangoMin = nroRangoMin;
	}

	public Float getNroRangoMax() {
		return nroRangoMax;
	}

	public void setNroRangoMax(Float nroRangoMax) {
		this.nroRangoMax = nroRangoMax;
	}
	
	public boolean isInside(Float porcentaje)
	{
	  return porcentaje != null && porcentaje >= getNroRangoMin() && porcentaje <= getNroRangoMax();
	}
}
