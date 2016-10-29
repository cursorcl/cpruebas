package cl.eos.ot;

import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoColegio;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTColegio {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty direccion = new SimpleStringProperty();
    private final ObjectProperty<TipoColegio> tipo = new SimpleObjectProperty<TipoColegio>();

    private Colegio colegio;

    public OTColegio() {
        // TODO Auto-generated constructor stub
    }

    public OTColegio(Colegio colegio) {
        this.colegio = colegio;
        id.set(colegio.getId());
        name.set(colegio.getName());
        direccion.set(colegio.getDireccion());
        tipo.set(colegio.getTipoColegio());

    }

    public final SimpleStringProperty direccionProperty() {
        return direccion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTColegio other = (OTColegio) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (id.get() != other.id.get())
            return false;
        return true;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public final java.lang.String getDireccion() {
        return direccionProperty().get();
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final TipoColegio getTipo() {
        return tipoProperty().getValue();
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

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    public final void setDireccion(final java.lang.String direccion) {
        direccionProperty().set(direccion);
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setTipo(final TipoColegio tipoColegio) {
        tipoProperty().set(tipoColegio);
    }

    public final ObjectProperty<TipoColegio> tipoProperty() {
        return tipo;
    }

}
