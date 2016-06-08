package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

@Entity(name = "rangoevaluacion")
@NamedQueries({ @NamedQuery(name = "RangoEvaluacion.findAll", query = "SELECT e FROM rangoevaluacion e") })
public class RangoEvaluacion extends AEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String abreviacion;
	private Float minimo;
	private Float maximo;
	
	@ManyToOne
    private NivelEvaluacion nivelEvaluacion;

	/**
	 * Se crea para el manejo de multiusuarios
	 */
	@Version
	protected int version;

	@Override
	public final int getVersion() {
		return version;
	}

	@Override
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

	public String getAbreviacion() {
		return abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}

	public Float getMinimo() {
		return minimo;
	}

	public void setMinimo(Float minimo) {
		this.minimo = minimo;
	}

	public Float getMaximo() {
		return maximo;
	}

	public void setMaximo(Float maximo) {
		this.maximo = maximo;
	}

	public NivelEvaluacion getNivelEvaluacion() {
		return nivelEvaluacion;
	}

	public void setNivelEvaluacion(NivelEvaluacion nivelEvaluacion) {
		this.nivelEvaluacion = nivelEvaluacion;
	}

	@Override
	public String toString() {
		return name;
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
		RangoEvaluacion other = (RangoEvaluacion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
