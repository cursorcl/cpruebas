package cl.eos.ot;

import cl.eos.persistence.models.SAsignatura;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTAsignatura {
    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();

    private SAsignatura asignatura;

    public OTAsignatura() {
        // TODO Auto-generated constructor stub
    }

    public OTAsignatura(SAsignatura asignatura) {
        this.asignatura = asignatura;
        id.set(asignatura.getId());
        name.set(asignatura.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTAsignatura other = (OTAsignatura) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (id.get() != other.id.get())
            return false;
        return true;
    }

    public SAsignatura getAsignatura() {
        return asignatura;
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
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

    public final SimpleStringProperty nameProperty() {
        return name;
    }

    public void setAsignatura(SAsignatura asignatura) {
        this.asignatura = asignatura;
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

}
