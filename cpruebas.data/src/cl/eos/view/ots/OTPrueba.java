package cl.eos.view.ots;

import java.time.LocalDate;

import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_Prueba.Estado;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.restful.tables.R_TipoPrueba;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTPrueba {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleLongProperty fecha = new SimpleLongProperty();

    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty responses = new SimpleStringProperty();

    private final SimpleObjectProperty<R_TipoPrueba> tipoPrueba = new SimpleObjectProperty<R_TipoPrueba>();
    private final SimpleObjectProperty<R_TipoCurso> curso = new SimpleObjectProperty<R_TipoCurso>();
    private final SimpleObjectProperty<R_Asignatura> asignatura = new SimpleObjectProperty<R_Asignatura>();
    private final SimpleObjectProperty<LocalDate> fechaLocal = new SimpleObjectProperty<LocalDate>();
    private final SimpleObjectProperty<Estado> estado = new SimpleObjectProperty<Estado>();

    private final SimpleIntegerProperty nroPreguntas = new SimpleIntegerProperty();
    private final SimpleIntegerProperty nroFormas = new SimpleIntegerProperty();
    private final SimpleIntegerProperty alternativas = new SimpleIntegerProperty();
    private final SimpleIntegerProperty puntajeBase = new SimpleIntegerProperty();
    private final SimpleIntegerProperty exigencia = new SimpleIntegerProperty();

    private R_Prueba prueba;

    public OTPrueba() {

    }

    public OTPrueba(R_Prueba prueba) {
        this(prueba, null, null);
    }

    
    public OTPrueba(R_Prueba prueba, R_Asignatura asignatura, R_TipoCurso tipoCurso) {
        this.prueba = prueba;
        id.set(prueba.getId());
        fecha.set(prueba.getFecha());
        name.set(prueba.getName());
        responses.set(prueba.getResponses());
        curso.set(tipoCurso);
        this.asignatura.set(asignatura);

        nroPreguntas.set(prueba.getNropreguntas());
        nroFormas.set(prueba.getNroformas());
        alternativas.set(prueba.getAlternativas());
        puntajeBase.set(prueba.getPuntajebase());
        exigencia.set(prueba.getExigencia());
        fechaLocal.set(LocalDate.ofEpochDay(fecha.longValue()));
        estado.set(Estado.getEstado(prueba.getEstado()));
    }

    public final SimpleIntegerProperty alternativasProperty() {
        return alternativas;
    }

    public final SimpleObjectProperty<R_Asignatura> asignaturaProperty() {
        return asignatura;
    }

    public final SimpleObjectProperty<R_TipoCurso> cursoProperty() {
        return curso;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTPrueba other = (OTPrueba) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (id.get() != other.id.get())
            return false;
        return true;
    }

    public final SimpleObjectProperty<Estado> estadoProperty() {
        return estado;
    }


    public final SimpleIntegerProperty exigenciaProperty() {
        return exigencia;
    }

    public final SimpleObjectProperty<LocalDate> fechaLocalProperty() {
        return fechaLocal;
    }

    public final SimpleLongProperty fechaProperty() {
        return fecha;
    }

    public final int getAlternativas() {
        return alternativasProperty().get();
    }

    public final R_Asignatura getAsignatura() {
        return asignaturaProperty().get();
    }

    public final R_TipoCurso getCurso() {
        return cursoProperty().get();
    }

    public final R_Prueba.Estado getEstado() {
        return estadoProperty().get();
    }

    public final int getExigencia() {
        return exigenciaProperty().get();
    }

    public final long getFecha() {
        return fechaProperty().get();
    }

    public final java.time.LocalDate getFechaLocal() {
        return fechaLocalProperty().get();
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final int getNroFormas() {
        return nroFormasProperty().get();
    }

    public final int getNroPreguntas() {
        return nroPreguntasProperty().get();
    }

    public R_Prueba getPrueba() {
        return prueba;
    }

    public final int getPuntajeBase() {
        return puntajeBaseProperty().get();
    }

    public final java.lang.String getResponses() {
        return responsesProperty().get();
    }

    public final R_TipoPrueba getTipoPrueba() {
        return tipoPruebaProperty().get();
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

    public final SimpleIntegerProperty nroFormasProperty() {
        return nroFormas;
    }

    public final SimpleIntegerProperty nroPreguntasProperty() {
        return nroPreguntas;
    }

    public final SimpleIntegerProperty puntajeBaseProperty() {
        return puntajeBase;
    }

    public final SimpleStringProperty responsesProperty() {
        return responses;
    }

    public final void setAlternativas(final int alternativas) {
        alternativasProperty().set(alternativas);
    }

    public final void setAsignatura(final R_Asignatura asignatura) {
        asignaturaProperty().set(asignatura);
    }

    public final void setCurso(final R_TipoCurso curso) {
        cursoProperty().set(curso);
    }

    public final void setEstado(final R_Prueba.Estado estado) {
        estadoProperty().set(estado);
    }

    public final void setExigencia(final int exigencia) {
        exigenciaProperty().set(exigencia);
    }

    public final void setFecha(final long fecha) {
        fechaProperty().set(fecha);
        fechaLocal.set(LocalDate.ofEpochDay(fechaProperty().longValue()));
    }

    public final void setFechaLocal(final java.time.LocalDate fechaLocal) {
        fechaLocalProperty().set(fechaLocal);
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setNroFormas(final int nroFormas) {
        nroFormasProperty().set(nroFormas);
    }

    public final void setNroPreguntas(final int nroPreguntas) {
        nroPreguntasProperty().set(nroPreguntas);
    }

    public void setPrueba(R_Prueba prueba) {
        this.prueba = prueba;
    }

    public final void setPuntajeBase(final int puntajeBase) {
        puntajeBaseProperty().set(puntajeBase);
    }

    public final void setResponses(final java.lang.String responses) {
        responsesProperty().set(responses);
    }

    public final void setTipoPrueba(final R_TipoPrueba tipoPrueba) {
        tipoPruebaProperty().set(tipoPrueba);
    }

    public final SimpleObjectProperty<R_TipoPrueba> tipoPruebaProperty() {
        return tipoPrueba;
    }

}
