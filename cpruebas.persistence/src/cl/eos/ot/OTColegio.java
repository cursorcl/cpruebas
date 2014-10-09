package cl.eos.ot;

import cl.eos.persistence.models.Colegio;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTColegio {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleStringProperty name = new SimpleStringProperty();
	private SimpleStringProperty direccion = new SimpleStringProperty();

	private Colegio colegio;

	public OTColegio(Colegio colegio) {
		this.colegio = colegio;
		this.id.set(colegio.getId());
		this.name.set(colegio.getName());
		this.direccion.set(colegio.getDireccion());

	}

	public OTColegio() {
		// TODO Auto-generated constructor stub
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

	public final SimpleStringProperty direccionProperty() {
		return this.direccion;
	}

	public final java.lang.String getDireccion() {
		return this.direccionProperty().get();
	}

	public final void setDireccion(final java.lang.String direccion) {
		this.direccionProperty().set(direccion);
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
		OTColegio other = (OTColegio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}  else if (id.get() != other.id.get())
			return false;
		return true;
	}

}
