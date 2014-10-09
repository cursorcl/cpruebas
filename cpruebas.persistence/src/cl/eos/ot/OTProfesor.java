package cl.eos.ot;

import cl.eos.persistence.models.Profesor;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTProfesor {

	private SimpleLongProperty id  = new SimpleLongProperty();
	private SimpleStringProperty rut = new SimpleStringProperty();
	private SimpleStringProperty name = new SimpleStringProperty();
	private SimpleStringProperty paterno = new SimpleStringProperty();
	private SimpleStringProperty materno = new SimpleStringProperty();
	
	private Profesor profesor;
	
	public OTProfesor(Profesor profesor) {
		this.profesor = profesor;
		this.id.set(profesor.getId());
		this.rut.set(profesor.getRut());
		this.name.set(profesor.getName());
		this.paterno.set(profesor.getPaterno());
		this.materno.set(profesor.getMaterno());
	}
	
	public OTProfesor() {
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

	public final SimpleStringProperty rutProperty() {
		return this.rut;
	}

	public final java.lang.String getRut() {
		return this.rutProperty().get();
	}

	public final void setRut(final java.lang.String rut) {
		this.rutProperty().set(rut);
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

	public final SimpleStringProperty paternoProperty() {
		return this.paterno;
	}

	public final java.lang.String getPaterno() {
		return this.paternoProperty().get();
	}

	public final void setPaterno(final java.lang.String paterno) {
		this.paternoProperty().set(paterno);
	}

	public final SimpleStringProperty maternoProperty() {
		return this.materno;
	}

	public final java.lang.String getMaterno() {
		return this.maternoProperty().get();
	}

	public final void setMaterno(final java.lang.String materno) {
		this.maternoProperty().set(materno);
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
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
		OTProfesor other = (OTProfesor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}  else if (id.get() != other.id.get())
			return false;
		return true;
	}
	
}
