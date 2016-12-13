package cl.eos.ot;

import cl.eos.persistence.models.SProfesor;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTProfesor {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty rut = new SimpleStringProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty paterno = new SimpleStringProperty();
    private final SimpleStringProperty materno = new SimpleStringProperty();

    private SProfesor profesor;

    public OTProfesor() {
        // TODO Auto-generated constructor stub
    }

    public OTProfesor(SProfesor profesor) {
        this.profesor = profesor;
        id.set(profesor.getId());
        rut.set(profesor.getRut());
        name.set(profesor.getName());
        paterno.set(profesor.getPaterno());
        materno.set(profesor.getMaterno());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTProfesor other = (OTProfesor) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (id.get() != other.id.get())
            return false;
        return true;
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getMaterno() {
        return maternoProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final java.lang.String getPaterno() {
        return paternoProperty().get();
    }

    public SProfesor getProfesor() {
        return profesor;
    }

    public final java.lang.String getRut() {
        return rutProperty().get();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    public final SimpleLongProperty idProperty() {
        return id;
    }

    public final SimpleStringProperty maternoProperty() {
        return materno;
    }

    public final SimpleStringProperty nameProperty() {
        return name;
    }

    public final SimpleStringProperty paternoProperty() {
        return paterno;
    }

    public final SimpleStringProperty rutProperty() {
        return rut;
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setMaterno(final java.lang.String materno) {
        maternoProperty().set(materno);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setPaterno(final java.lang.String paterno) {
        paternoProperty().set(paterno);
    }

    public void setProfesor(SProfesor profesor) {
        this.profesor = profesor;
    }

    public final void setRut(final java.lang.String rut) {
        rutProperty().set(rut);
    }

}
