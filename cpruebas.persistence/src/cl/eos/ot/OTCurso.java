package cl.eos.ot;

import cl.eos.restful.tables.R_Ciclo;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_TipoCurso;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTCurso {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleObjectProperty<R_Ciclo> ciclo = new SimpleObjectProperty<R_Ciclo>();
    private final SimpleObjectProperty<R_Colegio> colegio = new SimpleObjectProperty<R_Colegio>();

    private final SimpleObjectProperty<R_TipoCurso> tipoCurso = new SimpleObjectProperty<R_TipoCurso>();

    private R_Curso curso;

    public OTCurso() {
    }

    public OTCurso(R_Curso p_curso) {
        this(p_curso, null, null, null);
    }

    public OTCurso(R_Curso p_curso, R_Ciclo p_ciclo, R_Colegio p_colegio, R_TipoCurso p_tipoCurso) {
        this.curso = p_curso;
        id.set(p_curso.getId());
        name.set(p_curso.getName());
        ciclo.set(p_ciclo);
        colegio.set(p_colegio);
        tipoCurso.set(p_tipoCurso);
    }

    public final SimpleObjectProperty<R_Ciclo> cicloProperty() {
        return ciclo;
    }

    public final SimpleObjectProperty<R_Colegio> colegioProperty() {
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

    public final R_Ciclo getCiclo() {
        return cicloProperty().get();
    }

    public final R_Colegio getColegio() {
        return colegioProperty().get();
    }

    public R_Curso getCurso() {
        return curso;
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final R_TipoCurso getTipoCurso() {
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

    public final void setCiclo(final R_Ciclo ciclo) {
        cicloProperty().set(ciclo);
    }

    public final void setColegio(final R_Colegio colegio) {
        colegioProperty().set(colegio);
    }

    public void setCurso(R_Curso curso) {
        this.curso = curso;
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setTipoCurso(final R_TipoCurso tipoCurso) {
        tipoCursoProperty().set(tipoCurso);
    }

    public final SimpleObjectProperty<R_TipoCurso> tipoCursoProperty() {
        return tipoCurso;
    }

}
