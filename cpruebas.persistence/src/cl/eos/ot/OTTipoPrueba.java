package cl.eos.ot;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import cl.eos.persistence.models.TipoPrueba;

public class OTTipoPrueba {

	private SimpleLongProperty id  = new SimpleLongProperty();
	private SimpleStringProperty name = new SimpleStringProperty();

	private TipoPrueba tipoPrueba;

	public OTTipoPrueba(TipoPrueba tipoPrueba) {
		this.tipoPrueba = tipoPrueba;
		this.id.set(tipoPrueba.getId());
		this.name.set(tipoPrueba.getName());
	}

	public OTTipoPrueba() {
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

	public TipoPrueba getTipoPrueba() {
		return tipoPrueba;
	}

	public void setTipoPrueba(TipoPrueba tipoPrueba) {
		this.tipoPrueba = tipoPrueba;
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
		OTTipoPrueba other = (OTTipoPrueba) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}  else if (id.get() != other.id.get())
			return false;
		return true;
	}

}
