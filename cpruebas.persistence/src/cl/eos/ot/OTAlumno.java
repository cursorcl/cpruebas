package cl.eos.ot;

import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.TipoAlumno;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTAlumno {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty rut = new SimpleStringProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty paterno = new SimpleStringProperty();
    private final SimpleStringProperty materno = new SimpleStringProperty();
    private final SimpleStringProperty direccion = new SimpleStringProperty();
    private final SimpleObjectProperty<Colegio> colegio = new SimpleObjectProperty<Colegio>();
    private final SimpleObjectProperty<Curso> curso = new SimpleObjectProperty<Curso>();
    private final SimpleObjectProperty<TipoAlumno> tipoAlumno = new SimpleObjectProperty<TipoAlumno>();
    private Alumno alumno;

    public OTAlumno() {
        id.setValue(null);
    }

    public OTAlumno(Alumno alumno) {
        this.alumno = alumno;
        id.set(alumno.getId());
        rut.set(alumno.getRut());
        name.set(alumno.getName());
        paterno.set(alumno.getPaterno());
        materno.set(alumno.getMaterno());
        direccion.set(alumno.getDireccion());
        colegio.set(alumno.getColegio());
        curso.set(alumno.getCurso());
        tipoAlumno.set(alumno.getTipoAlumno());

    }

    public final SimpleObjectProperty<Colegio> colegioProperty() {
        return colegio;
    }

    public final SimpleObjectProperty<Curso> cursoProperty() {
        return curso;
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
        final OTAlumno other = (OTAlumno) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (id.get() != other.id.get())
            return false;
        return true;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public final cl.eos.persistence.models.Colegio getColegio() {
        return colegioProperty().get();
    }

    public final cl.eos.persistence.models.Curso getCurso() {
        return cursoProperty().get();
    }

    public final java.lang.String getDireccion() {
        return direccionProperty().get();
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

    public final java.lang.String getRut() {
        return rutProperty().get();
    }

    public final cl.eos.persistence.models.TipoAlumno getTipoAlumno() {
        return tipoAlumnoProperty().get();
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

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public final void setColegio(final cl.eos.persistence.models.Colegio colegio) {
        colegioProperty().set(colegio);
    }

    public final void setCurso(final cl.eos.persistence.models.Curso curso) {
        cursoProperty().set(curso);
    }

    public final void setDireccion(final java.lang.String direccion) {
        direccionProperty().set(direccion);
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

    public final void setRut(final java.lang.String rut) {
        rutProperty().set(rut);
    }

    public final void setTipoAlumno(final cl.eos.persistence.models.TipoAlumno tipoAlumno) {
        tipoAlumnoProperty().set(tipoAlumno);
    }

    public final SimpleObjectProperty<TipoAlumno> tipoAlumnoProperty() {
        return tipoAlumno;
    }

    @Override
    public String toString() {
        String result = "";
        if (alumno != null) {
            result = String.format("%s\t%s\t%s %s %s %s", alumno.getColegio().getName(), alumno.getCurso().getName(),
                    alumno.getRut(), alumno.getPaterno(), alumno.getMaterno(), alumno.getName());
        }
        return result;
    }

}
