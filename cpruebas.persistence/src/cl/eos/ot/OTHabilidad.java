package cl.eos.ot;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import cl.eos.persistence.models.Habilidad;

public class OTHabilidad {

	private SimpleLongProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty descripcion;
	private Habilidad habilidad;

	public OTHabilidad(Habilidad habilidad) {
		this.habilidad = habilidad;
		this.id.set(habilidad.getId());
		this.name.set(habilidad.getName());
		this.descripcion.set(habilidad.getDescripcion());
	}

	public final SimpleLongProperty idProperty() {
		return this.id;
	}

	public final long getId() {
		return this.idProperty().get();
	}

	public final void setId(final long id) {
		this.idProperty().set(id);
	}

	public final SimpleStringProperty nameProperty() {
		return this.name;
	}

	public final java.lang.String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final java.lang.String name) {
		this.nameProperty().set(name);
	}

	public final SimpleStringProperty descripcionProperty() {
		return this.descripcion;
	}

	public final java.lang.String getDescripcion() {
		return this.descripcionProperty().get();
	}

	public final void setDescripcion(final java.lang.String descripcion) {
		this.descripcionProperty().set(descripcion);
	}

	public Habilidad getHabilidad() {
		return habilidad;
	}

	public void setHabilidad(Habilidad habilidad) {
		this.habilidad = habilidad;
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
		OTHabilidad other = (OTHabilidad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (id.get() != other.id.get())
			return false;
		return true;
	}

}
