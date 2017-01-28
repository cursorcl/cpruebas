package cl.eos.ot;

import cl.eos.restful.tables.R_TipoPrueba;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTTipoPrueba {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();

    private R_TipoPrueba tipoPrueba;

    public OTTipoPrueba() {
    }

    public OTTipoPrueba(R_TipoPrueba tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
        id.set(tipoPrueba.getId());
        name.set(tipoPrueba.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTTipoPrueba other = (OTTipoPrueba) obj;
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

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public R_TipoPrueba getTipoPrueba() {
        return tipoPrueba;
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

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public void setTipoPrueba(R_TipoPrueba tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }

}
