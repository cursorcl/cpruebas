package cl.eos.ot;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.TipoPrueba;

public class OTEjeTematico {

	private SimpleLongProperty id;
	private SimpleStringProperty name;
	private SimpleObjectProperty<TipoPrueba> tipoprueba;
	private SimpleObjectProperty<Asignatura> asignatura;
	private EjeTematico ejeTematico;
	
	public OTEjeTematico(EjeTematico ejeTematico) {
		this.ejeTematico = ejeTematico;
		this.id.set(ejeTematico.getId());
		this.name.set(ejeTematico.getName());
		this.tipoprueba.set(ejeTematico.getTipoprueba());
		this.asignatura.set(ejeTematico.getAsignatura());
	}
	
	public OTEjeTematico() {
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

	public final SimpleObjectProperty<TipoPrueba> tipopruebaProperty() {
		return this.tipoprueba;
	}

	public final cl.eos.persistence.models.TipoPrueba getTipoprueba() {
		return this.tipopruebaProperty().get();
	}

	public final void setTipoprueba(
			final cl.eos.persistence.models.TipoPrueba tipoprueba) {
		this.tipopruebaProperty().set(tipoprueba);
	}

	public final SimpleObjectProperty<Asignatura> asignaturaProperty() {
		return this.asignatura;
	}

	public final cl.eos.persistence.models.Asignatura getAsignatura() {
		return this.asignaturaProperty().get();
	}

	public final void setAsignatura(
			final cl.eos.persistence.models.Asignatura asignatura) {
		this.asignaturaProperty().set(asignatura);
	}

	public EjeTematico getEjeTematico() {
		return ejeTematico;
	}

	public void setEjeTematico(EjeTematico ejeTematico) {
		this.ejeTematico = ejeTematico;
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
		OTEjeTematico other = (OTEjeTematico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}  else if (id.get() != other.id.get())
			return false;
		return true;
	}
	
	
}
