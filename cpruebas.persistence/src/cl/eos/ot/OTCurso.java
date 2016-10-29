package cl.eos.ot;

import java.util.Collection;

import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Ciclo;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.TipoCurso;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTCurso {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleObjectProperty<Ciclo> ciclo = new SimpleObjectProperty<Ciclo>();
    private final SimpleObjectProperty<Colegio> colegio = new SimpleObjectProperty<Colegio>();

    private final SimpleObjectProperty<Collection<Alumno>> alumnos = new SimpleObjectProperty<Collection<Alumno>>();
    private final SimpleObjectProperty<TipoCurso> tipoCurso = new SimpleObjectProperty<TipoCurso>();

    private Curso curso;

    public OTCurso() {
        // TODO Auto-generated constructor stub
    }

    public OTCurso(Curso curso) {
        this.curso = curso;
        id.set(curso.getId());
        name.set(curso.getName());
        ciclo.set(curso.getCiclo());
        colegio.set(curso.getColegio());
        alumnos.set(curso.getAlumnos());
        tipoCurso.set(curso.getTipoCurso());
    }

    public final SimpleObjectProperty<Collection<Alumno>> alumnosProperty() {
        return alumnos;
    }

    public final SimpleObjectProperty<Ciclo> cicloProperty() {
        return ciclo;
    }

    public final SimpleObjectProperty<Colegio> colegioProperty() {
        return colegio;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTCurso other = (OTCurso) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (id.get() != other.id.get())
            return false;
        return true;
    }

    public final java.util.Collection<cl.eos.persistence.models.Alumno> getAlumnos() {
        return alumnosProperty().get();
    }

    public final cl.eos.persistence.models.Ciclo getCiclo() {
        return cicloProperty().get();
    }

    public final cl.eos.persistence.models.Colegio getColegio() {
        return colegioProperty().get();
    }

    public Curso getCurso() {
        return curso;
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final cl.eos.persistence.models.TipoCurso getTipoCurso() {
        return tipoCursoProperty().get();
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

    public final void setAlumnos(final java.util.Collection<cl.eos.persistence.models.Alumno> alumnos) {
        alumnosProperty().set(alumnos);
    }

    public final void setCiclo(final cl.eos.persistence.models.Ciclo ciclo) {
        cicloProperty().set(ciclo);
    }

    public final void setColegio(final cl.eos.persistence.models.Colegio colegio) {
        colegioProperty().set(colegio);
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setTipoCurso(final cl.eos.persistence.models.TipoCurso tipoCurso) {
        tipoCursoProperty().set(tipoCurso);
    }

    public final SimpleObjectProperty<TipoCurso> tipoCursoProperty() {
        return tipoCurso;
    }

}
