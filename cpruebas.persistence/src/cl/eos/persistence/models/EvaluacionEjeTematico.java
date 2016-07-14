package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import cl.eos.persistence.AEntity;

@Entity(name = "evaluacionejetematico")
@Cache(
        type=CacheType.NONE,
        size=64000,  // Use 64,000 as the initial cache size.
        expiry=360000,  // 6 minutes
        coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS  // if cache coordination is used, only send invalidation messages.
      )
@NamedQueries({ @NamedQuery(name = "EvaluacionEjeTematico.findAll", query = "SELECT e FROM evaluacionejetematico e") })
public class EvaluacionEjeTematico extends AEntity {
	
	/**
	 * Numero serial.
	 */
	private static final long serialVersionUID = -7756547051562574561L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Float nroRangoMin = 0F;
	private Float nroRangoMax = 0F;
	
	/**
	 * Se crea para el manejo de multiusuarios
	 */
	@Version 
	protected int version;
	
	
	public final int getVersion() {
		return version;
	}

	public final void setVersion(int version) {
		this.version = version;
	}
	
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		EvaluacionEjeTematico other = (EvaluacionEjeTematico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
