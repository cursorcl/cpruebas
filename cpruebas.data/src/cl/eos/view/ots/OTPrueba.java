package cl.eos.view.ots;

import java.time.LocalDate;
import java.util.List;

import cl.eos.imp.model.Prueba.Estado;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Formas;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
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
    private final SimpleObjectProperty<R_NivelEvaluacion> nivelEvaluacion = new SimpleObjectProperty<R_NivelEvaluacion>();
    private final SimpleObjectProperty<R_Profesor> profesor = new SimpleObjectProperty<R_Profesor>();
    private final SimpleObjectProperty<List<R_Formas>> formas = new SimpleObjectProperty<List<R_Formas>>();
    private final SimpleObjectProperty<List<R_RespuestasEsperadasPrueba>> respuestas = new SimpleObjectProperty<List<R_RespuestasEsperadasPrueba>>();
    private final SimpleObjectProperty<List<R_EvaluacionPrueba>> evaluaciones = new SimpleObjectProperty<List<R_EvaluacionPrueba>>();
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
        this.prueba = prueba;
        id.set(prueba.getId());
        fecha.set(prueba.getFecha());
        name.set(prueba.getName());
        responses.set(prueba.getResponses());
        tipoPrueba.set(prueba.getTipoPrueba());
        curso.set(prueba.getCurso());
        asignatura.set(prueba.getAsignatura());
        nivelEvaluacion.set(prueba.getNivelEvaluacion());
        profesor.set(prueba.getProfesor());
        formas.set(prueba.getFormas());
        respuestas.set(prueba.getRespuestas());
        evaluaciones.set(prueba.getEvaluaciones());

        nroPreguntas.set(prueba.getNroPreguntas());
        nroFormas.set(prueba.getNroFormas());
        alternativas.set(prueba.getAlternativas());
        puntajeBase.set(prueba.getPuntajeBase());
        exigencia.set(prueba.getExigencia());
        fechaLocal.set(LocalDate.ofEpochDay(fecha.longValue()));
        estado.set(prueba.getEstado());
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

    public final SimpleObjectProperty<List<R_EvaluacionPrueba>> evaluacionesProperty() {
        return evaluaciones;
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

    public final SimpleObjectProperty<List<R_Formas>> formasProperty() {
        return formas;
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

    public final List<R_EvaluacionPrueba> getEvaluaciones() {
        return evaluacionesProperty().get();
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

    public final List<R_Formas> getFormas() {
        return formasProperty().get();
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final R_NivelEvaluacion getNivelEvaluacion() {
        return nivelEvaluacionProperty().get();
    }

    public final int getNroFormas() {
        return nroFormasProperty().get();
    }

    public final int getNroPreguntas() {
        return nroPreguntasProperty().get();
    }

    public final R_Profesor getProfesor() {
        return profesorProperty().get();
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

    public final List<R_RespuestasEsperadasPrueba> getRespuestas() {
        return respuestasProperty().get();
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

    public final SimpleObjectProperty<R_NivelEvaluacion> nivelEvaluacionProperty() {
        return nivelEvaluacion;
    }

    public final SimpleIntegerProperty nroFormasProperty() {
        return nroFormas;
    }

    public final SimpleIntegerProperty nroPreguntasProperty() {
        return nroPreguntas;
    }

    public final SimpleObjectProperty<R_Profesor> profesorProperty() {
        return profesor;
    }

    public final SimpleIntegerProperty puntajeBaseProperty() {
        return puntajeBase;
    }

    public final SimpleStringProperty responsesProperty() {
        return responses;
    }

    public final SimpleObjectProperty<List<R_RespuestasEsperadasPrueba>> respuestasProperty() {
        return respuestas;
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

    public final void setEvaluaciones(final List<R_EvaluacionPrueba> evaluaciones) {
        evaluacionesProperty().set(evaluaciones);
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

    public final void setFormas(final List<R_Formas> formas) {
        formasProperty().set(formas);
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setNivelEvaluacion(final R_NivelEvaluacion nivelEvaluacion) {
        nivelEvaluacionProperty().set(nivelEvaluacion);
    }

    public final void setNroFormas(final int nroFormas) {
        nroFormasProperty().set(nroFormas);
    }

    public final void setNroPreguntas(final int nroPreguntas) {
        nroPreguntasProperty().set(nroPreguntas);
    }

    public final void setProfesor(final R_Profesor profesor) {
        profesorProperty().set(profesor);
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

    public final void setRespuestas(
            final List<R_RespuestasEsperadasPrueba> respuestas) {
        respuestasProperty().set(respuestas);
    }

    public final void setTipoPrueba(final R_TipoPrueba tipoPrueba) {
        tipoPruebaProperty().set(tipoPrueba);
    }

    public final SimpleObjectProperty<R_TipoPrueba> tipoPruebaProperty() {
        return tipoPrueba;
    }

}
