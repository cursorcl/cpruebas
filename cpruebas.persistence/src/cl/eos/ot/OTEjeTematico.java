package cl.eos.ot;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.TipoPrueba;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTEjeTematico {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleObjectProperty<TipoPrueba> tipoprueba = new SimpleObjectProperty<TipoPrueba>();
    private final SimpleObjectProperty<Asignatura> asignatura = new SimpleObjectProperty<Asignatura>();
    private EjeTematico ejeTematico;

    public OTEjeTematico() {
        // TODO Auto-generated constructor stub
    }

    public OTEjeTematico(EjeTematico ejeTematico) {
        this.ejeTematico = ejeTematico;
        id.set(ejeTematico.getId());
        name.set(ejeTematico.getName());
        tipoprueba.set(ejeTematico.getTipoprueba());
        asignatura.set(ejeTematico.getAsignatura());
    }

    public final SimpleObjectProperty<Asignatura> asignaturaProperty() {
        return asignatura;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTEjeTematico other = (OTEjeTematico) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (id.get() != other.id.get())
            return false;
        return true;
    }

    public final cl.eos.persistence.models.Asignatura getAsignatura() {
        return asignaturaProperty().get();
    }

    public EjeTematico getEjeTematico() {
        return ejeTematico;
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final cl.eos.persistence.models.TipoPrueba getTipoprueba() {
        return tipopruebaProperty().get();
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

    public final void setAsignatura(final cl.eos.persistence.models.Asignatura asignatura) {
        asignaturaProperty().set(asignatura);
    }

    public void setEjeTematico(EjeTematico ejeTematico) {
        this.ejeTematico = ejeTematico;
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setTipoprueba(final cl.eos.persistence.models.TipoPrueba tipoprueba) {
        tipopruebaProperty().set(tipoprueba);
    }

    public final SimpleObjectProperty<TipoPrueba> tipopruebaProperty() {
        return tipoprueba;
    }

}
