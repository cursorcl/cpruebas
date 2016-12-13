package cl.eos.view.ots;

import java.time.LocalDate;
import java.util.List;

import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SFormas;
import cl.eos.persistence.models.SNivelEvaluacion;
import cl.eos.persistence.models.SProfesor;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SPrueba.Estado;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.models.STipoCurso;
import cl.eos.persistence.models.STipoPrueba;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OTPrueba {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleLongProperty fecha = new SimpleLongProperty();

    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty responses = new SimpleStringProperty();

    private final SimpleObjectProperty<STipoPrueba> tipoPrueba = new SimpleObjectProperty<STipoPrueba>();
    private final SimpleObjectProperty<STipoCurso> curso = new SimpleObjectProperty<STipoCurso>();
    private final SimpleObjectProperty<SAsignatura> asignatura = new SimpleObjectProperty<SAsignatura>();
    private final SimpleObjectProperty<SNivelEvaluacion> nivelEvaluacion = new SimpleObjectProperty<SNivelEvaluacion>();
    private final SimpleObjectProperty<SProfesor> profesor = new SimpleObjectProperty<SProfesor>();
    private final SimpleObjectProperty<List<SFormas>> formas = new SimpleObjectProperty<List<SFormas>>();
    private final SimpleObjectProperty<List<SRespuestasEsperadasPrueba>> respuestas = new SimpleObjectProperty<List<SRespuestasEsperadasPrueba>>();
    private final SimpleObjectProperty<List<SEvaluacionPrueba>> evaluaciones = new SimpleObjectProperty<List<SEvaluacionPrueba>>();
    private final SimpleObjectProperty<LocalDate> fechaLocal = new SimpleObjectProperty<LocalDate>();
    private final SimpleObjectProperty<Estado> estado = new SimpleObjectProperty<Estado>();

    private final SimpleIntegerProperty nroPreguntas = new SimpleIntegerProperty();
    private final SimpleIntegerProperty nroFormas = new SimpleIntegerProperty();
    private final SimpleIntegerProperty alternativas = new SimpleIntegerProperty();
    private final SimpleIntegerProperty puntajeBase = new SimpleIntegerProperty();
    private final SimpleIntegerProperty exigencia = new SimpleIntegerProperty();

    private SPrueba prueba;

    public OTPrueba() {

    }

    public OTPrueba(SPrueba prueba) {
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

    public final SimpleObjectProperty<SAsignatura> asignaturaProperty() {
        return asignatura;
    }

    public final SimpleObjectProperty<STipoCurso> cursoProperty() {
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

    public final SimpleObjectProperty<List<SEvaluacionPrueba>> evaluacionesProperty() {
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

    public final SimpleObjectProperty<List<SFormas>> formasProperty() {
        return formas;
    }

    public final int getAlternativas() {
        return alternativasProperty().get();
    }

    public final cl.eos.persistence.models.SAsignatura getAsignatura() {
        return asignaturaProperty().get();
    }

    public final cl.eos.persistence.models.STipoCurso getCurso() {
        return cursoProperty().get();
    }

    public final cl.eos.persistence.models.SPrueba.Estado getEstado() {
        return estadoProperty().get();
    }

    public final java.util.List<cl.eos.persistence.models.SEvaluacionPrueba> getEvaluaciones() {
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

    public final java.util.List<cl.eos.persistence.models.SFormas> getFormas() {
        return formasProperty().get();
    }

    public final long getId() {
        return idProperty().get();
    }

    public final java.lang.String getName() {
        return nameProperty().get();
    }

    public final cl.eos.persistence.models.SNivelEvaluacion getNivelEvaluacion() {
        return nivelEvaluacionProperty().get();
    }

    public final int getNroFormas() {
        return nroFormasProperty().get();
    }

    public final int getNroPreguntas() {
        return nroPreguntasProperty().get();
    }

    public final cl.eos.persistence.models.SProfesor getProfesor() {
        return profesorProperty().get();
    }

    public SPrueba getPrueba() {
        return prueba;
    }

    public final int getPuntajeBase() {
        return puntajeBaseProperty().get();
    }

    public final java.lang.String getResponses() {
        return responsesProperty().get();
    }

    public final java.util.List<cl.eos.persistence.models.SRespuestasEsperadasPrueba> getRespuestas() {
        return respuestasProperty().get();
    }

    public final cl.eos.persistence.models.STipoPrueba getTipoPrueba() {
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

    public final SimpleObjectProperty<SNivelEvaluacion> nivelEvaluacionProperty() {
        return nivelEvaluacion;
    }

    public final SimpleIntegerProperty nroFormasProperty() {
        return nroFormas;
    }

    public final SimpleIntegerProperty nroPreguntasProperty() {
        return nroPreguntas;
    }

    public final SimpleObjectProperty<SProfesor> profesorProperty() {
        return profesor;
    }

    public final SimpleIntegerProperty puntajeBaseProperty() {
        return puntajeBase;
    }

    public final SimpleStringProperty responsesProperty() {
        return responses;
    }

    public final SimpleObjectProperty<List<SRespuestasEsperadasPrueba>> respuestasProperty() {
        return respuestas;
    }

    public final void setAlternativas(final int alternativas) {
        alternativasProperty().set(alternativas);
    }

    public final void setAsignatura(final cl.eos.persistence.models.SAsignatura asignatura) {
        asignaturaProperty().set(asignatura);
    }

    public final void setCurso(final cl.eos.persistence.models.STipoCurso curso) {
        cursoProperty().set(curso);
    }

    public final void setEstado(final cl.eos.persistence.models.SPrueba.Estado estado) {
        estadoProperty().set(estado);
    }

    public final void setEvaluaciones(final java.util.List<cl.eos.persistence.models.SEvaluacionPrueba> evaluaciones) {
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

    public final void setFormas(final java.util.List<cl.eos.persistence.models.SFormas> formas) {
        formasProperty().set(formas);
    }

    public final void setId(final long id) {
        idProperty().set(id);
    }

    public final void setName(final java.lang.String name) {
        nameProperty().set(name);
    }

    public final void setNivelEvaluacion(final cl.eos.persistence.models.SNivelEvaluacion nivelEvaluacion) {
        nivelEvaluacionProperty().set(nivelEvaluacion);
    }

    public final void setNroFormas(final int nroFormas) {
        nroFormasProperty().set(nroFormas);
    }

    public final void setNroPreguntas(final int nroPreguntas) {
        nroPreguntasProperty().set(nroPreguntas);
    }

    public final void setProfesor(final cl.eos.persistence.models.SProfesor profesor) {
        profesorProperty().set(profesor);
    }

    public void setPrueba(SPrueba prueba) {
        this.prueba = prueba;
    }

    public final void setPuntajeBase(final int puntajeBase) {
        puntajeBaseProperty().set(puntajeBase);
    }

    public final void setResponses(final java.lang.String responses) {
        responsesProperty().set(responses);
    }

    public final void setRespuestas(
            final java.util.List<cl.eos.persistence.models.SRespuestasEsperadasPrueba> respuestas) {
        respuestasProperty().set(respuestas);
    }

    public final void setTipoPrueba(final cl.eos.persistence.models.STipoPrueba tipoPrueba) {
        tipoPruebaProperty().set(tipoPrueba);
    }

    public final SimpleObjectProperty<STipoPrueba> tipoPruebaProperty() {
        return tipoPrueba;
    }

}
