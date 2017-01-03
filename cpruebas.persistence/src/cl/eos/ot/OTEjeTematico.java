package cl.eos.ot;

import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_TipoPrueba;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTEjeTematico {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleObjectProperty<R_TipoPrueba> tipoprueba = new SimpleObjectProperty<R_TipoPrueba>();
    private final SimpleObjectProperty<R_Asignatura> asignatura = new SimpleObjectProperty<R_Asignatura>();
    private R_Ejetematico ejeTematico;

    public OTEjeTematico() {
        // TODO Auto-generated constructor stub
    }
    public OTEjeTematico(R_Ejetematico ejeTematico) {
        this.ejeTematico = ejeTematico;
        id.set(ejeTematico.getId());
        name.set(ejeTematico.getName());
        tipoprueba.set(null);
        asignatura.set(null);
    }
    
    public OTEjeTematico(R_Ejetematico ejeTematico, R_TipoPrueba p_tipoPrueba, R_Asignatura p_asignatura) {
        this.ejeTematico = ejeTematico;
        id.set(ejeTematico.getId());
        name.set(ejeTematico.getName());
        tipoprueba.set(p_tipoPrueba);
        asignatura.set(p_asignatura);
    }

    public final SimpleObjectProperty<R_Asignatura> asignaturaProperty() {
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

    public final R_Asignatura getAsignatura() {
        return asignaturaProperty().get();
    }

    public R_Ejetematico getEjeTematico() {
        return ejeTematico;
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final R_TipoPrueba getTipoprueba() {
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

    public final void setAsignatura(final R_Asignatura asignatura) {
        asignaturaProperty().set(asignatura);
    }

    public void setEjeTematico(R_Ejetematico ejeTematico) {
        this.ejeTematico = ejeTematico;
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setTipoprueba(final R_TipoPrueba tipoprueba) {
        tipopruebaProperty().set(tipoprueba);
    }

    public final SimpleObjectProperty<R_TipoPrueba> tipopruebaProperty() {
        return tipoprueba;
    }

}
