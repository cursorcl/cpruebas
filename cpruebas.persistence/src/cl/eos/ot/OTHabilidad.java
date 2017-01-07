package cl.eos.ot;

import cl.eos.restful.tables.R_Habilidad;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTHabilidad {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty descripcion = new SimpleStringProperty();
    private R_Habilidad habilidad;

    public OTHabilidad(R_Habilidad habilidad) {
        this.habilidad = habilidad;
        id.set(habilidad.getId());
        name.set(habilidad.getName());
        descripcion.set(habilidad.getDescripcion());
    }

    public final SimpleStringProperty descripcionProperty() {
        return descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTHabilidad other = (OTHabilidad) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (id.get() != other.id.get())
            return false;
        return true;
    }

    public final java.lang.String getDescripcion() {
        return descripcionProperty().get();
    }

    public R_Habilidad getHabilidad() {
        return habilidad;
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

    public final void setDescripcion(final java.lang.String descripcion) {
        descripcionProperty().set(descripcion);
    }

    public void setHabilidad(R_Habilidad habilidad) {
        this.habilidad = habilidad;
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

}
