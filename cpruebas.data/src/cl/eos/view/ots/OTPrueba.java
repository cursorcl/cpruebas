package cl.eos.view.ots;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Formas;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.Prueba.Estado;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.persistence.models.TipoPrueba;

public class OTPrueba {


  private SimpleLongProperty id = new SimpleLongProperty();
  private SimpleLongProperty fecha = new SimpleLongProperty();

  private SimpleStringProperty name = new SimpleStringProperty();
  private SimpleStringProperty responses = new SimpleStringProperty();

  private SimpleObjectProperty<TipoPrueba> tipoPrueba = new SimpleObjectProperty<TipoPrueba>();
  private SimpleObjectProperty<TipoCurso> curso = new SimpleObjectProperty<TipoCurso>();
  private SimpleObjectProperty<Asignatura> asignatura = new SimpleObjectProperty<Asignatura>();
  private SimpleObjectProperty<NivelEvaluacion> nivelEvaluacion =
      new SimpleObjectProperty<NivelEvaluacion>();
  private SimpleObjectProperty<Profesor> profesor = new SimpleObjectProperty<Profesor>();
  private SimpleObjectProperty<List<Formas>> formas = new SimpleObjectProperty<List<Formas>>();
  private SimpleObjectProperty<List<RespuestasEsperadasPrueba>> respuestas =
      new SimpleObjectProperty<List<RespuestasEsperadasPrueba>>();
  private SimpleObjectProperty<List<EvaluacionPrueba>> evaluaciones =
      new SimpleObjectProperty<List<EvaluacionPrueba>>();
  private SimpleObjectProperty<LocalDate> fechaLocal = new SimpleObjectProperty<LocalDate>();
  private SimpleObjectProperty<Estado> estado = new SimpleObjectProperty<Estado>();

  private SimpleIntegerProperty nroPreguntas = new SimpleIntegerProperty();
  private SimpleIntegerProperty nroFormas = new SimpleIntegerProperty();
  private SimpleIntegerProperty alternativas = new SimpleIntegerProperty();
  private SimpleIntegerProperty puntajeBase = new SimpleIntegerProperty();
  private SimpleIntegerProperty exigencia = new SimpleIntegerProperty();

  private Prueba prueba;


  public OTPrueba(Prueba prueba) {
    this.prueba = prueba;
    this.id.set(prueba.getId());
    this.fecha.set(prueba.getFecha());
    this.name.set(prueba.getName());
    this.responses.set(prueba.getResponses());
    this.tipoPrueba.set(prueba.getTipoPrueba());
    this.curso.set(prueba.getCurso());
    this.asignatura.set(prueba.getAsignatura());
    this.nivelEvaluacion.set(prueba.getNivelEvaluacion());
    this.profesor.set(prueba.getProfesor());
    this.formas.set(prueba.getFormas());
    this.respuestas.set(prueba.getRespuestas());
    this.evaluaciones.set(prueba.getEvaluaciones());

    this.nroPreguntas.set(prueba.getNroPreguntas());
    this.nroFormas.set(prueba.getNroFormas());
    this.alternativas.set(prueba.getAlternativas());
    this.puntajeBase.set(prueba.getPuntajeBase());
    this.exigencia.set(prueba.getExigencia());
    this.fechaLocal.set(LocalDate.ofEpochDay(this.fecha.longValue()));
    this.estado.set(prueba.getEstado());
  }

  public OTPrueba() {

  }

  public final SimpleLongProperty idProperty() {
    return this.id;
  }

  public final long getId() {
    return this.idProperty().get();
  }

  public final void setId(final long id) {
    this.idProperty().set(id);
  }

  public final SimpleLongProperty fechaProperty() {
    return this.fecha;
  }

  public final long getFecha() {
    return this.fechaProperty().get();
  }

  public final void setFecha(final long fecha) {
    this.fechaProperty().set(fecha);
    this.fechaLocal.set(LocalDate.ofEpochDay(this.fechaProperty().longValue()));
  }

  public final SimpleStringProperty nameProperty() {
    return this.name;
  }

  public final java.lang.String getName() {
    return this.nameProperty().get();
  }

  public final void setName(final java.lang.String name) {
    this.nameProperty().set(name);
  }

  public final SimpleStringProperty responsesProperty() {
    return this.responses;
  }

  public final java.lang.String getResponses() {
    return this.responsesProperty().get();
  }

  public final void setResponses(final java.lang.String responses) {
    this.responsesProperty().set(responses);
  }

  public final SimpleObjectProperty<TipoPrueba> tipoPruebaProperty() {
    return this.tipoPrueba;
  }

  public final cl.eos.persistence.models.TipoPrueba getTipoPrueba() {
    return this.tipoPruebaProperty().get();
  }

  public final void setTipoPrueba(final cl.eos.persistence.models.TipoPrueba tipoPrueba) {
    this.tipoPruebaProperty().set(tipoPrueba);
  }

  public final SimpleObjectProperty<TipoCurso> cursoProperty() {
    return this.curso;
  }

  public final cl.eos.persistence.models.TipoCurso getCurso() {
    return this.cursoProperty().get();
  }

  public final void setCurso(final cl.eos.persistence.models.TipoCurso curso) {
    this.cursoProperty().set(curso);
  }

  public final SimpleObjectProperty<Asignatura> asignaturaProperty() {
    return this.asignatura;
  }

  public final cl.eos.persistence.models.Asignatura getAsignatura() {
    return this.asignaturaProperty().get();
  }

  public final void setAsignatura(final cl.eos.persistence.models.Asignatura asignatura) {
    this.asignaturaProperty().set(asignatura);
  }

  public final SimpleObjectProperty<NivelEvaluacion> nivelEvaluacionProperty() {
    return this.nivelEvaluacion;
  }

  public final cl.eos.persistence.models.NivelEvaluacion getNivelEvaluacion() {
    return this.nivelEvaluacionProperty().get();
  }

  public final void setNivelEvaluacion(
      final cl.eos.persistence.models.NivelEvaluacion nivelEvaluacion) {
    this.nivelEvaluacionProperty().set(nivelEvaluacion);
  }

  public final SimpleObjectProperty<Profesor> profesorProperty() {
    return this.profesor;
  }

  public final cl.eos.persistence.models.Profesor getProfesor() {
    return this.profesorProperty().get();
  }

  public final void setProfesor(final cl.eos.persistence.models.Profesor profesor) {
    this.profesorProperty().set(profesor);
  }

  public final SimpleObjectProperty<List<Formas>> formasProperty() {
    return this.formas;
  }

  public final java.util.List<cl.eos.persistence.models.Formas> getFormas() {
    return this.formasProperty().get();
  }

  public final void setFormas(final java.util.List<cl.eos.persistence.models.Formas> formas) {
    this.formasProperty().set(formas);
  }

  public final SimpleObjectProperty<List<RespuestasEsperadasPrueba>> respuestasProperty() {
    return this.respuestas;
  }

  public final java.util.List<cl.eos.persistence.models.RespuestasEsperadasPrueba> getRespuestas() {
    return this.respuestasProperty().get();
  }

  public final void setRespuestas(
      final java.util.List<cl.eos.persistence.models.RespuestasEsperadasPrueba> respuestas) {
    this.respuestasProperty().set(respuestas);
  }

  public final SimpleObjectProperty<List<EvaluacionPrueba>> evaluacionesProperty() {
    return this.evaluaciones;
  }

  public final java.util.List<cl.eos.persistence.models.EvaluacionPrueba> getEvaluaciones() {
    return this.evaluacionesProperty().get();
  }

  public final void setEvaluaciones(
      final java.util.List<cl.eos.persistence.models.EvaluacionPrueba> evaluaciones) {
    this.evaluacionesProperty().set(evaluaciones);
  }

  public final SimpleIntegerProperty nroPreguntasProperty() {
    return this.nroPreguntas;
  }

  public final int getNroPreguntas() {
    return this.nroPreguntasProperty().get();
  }

  public final void setNroPreguntas(final int nroPreguntas) {
    this.nroPreguntasProperty().set(nroPreguntas);
  }

  public final SimpleIntegerProperty nroFormasProperty() {
    return this.nroFormas;
  }

  public final int getNroFormas() {
    return this.nroFormasProperty().get();
  }

  public final void setNroFormas(final int nroFormas) {
    this.nroFormasProperty().set(nroFormas);
  }

  public final SimpleIntegerProperty alternativasProperty() {
    return this.alternativas;
  }

  public final int getAlternativas() {
    return this.alternativasProperty().get();
  }

  public final void setAlternativas(final int alternativas) {
    this.alternativasProperty().set(alternativas);
  }

  public final SimpleIntegerProperty puntajeBaseProperty() {
    return this.puntajeBase;
  }

  public final int getPuntajeBase() {
    return this.puntajeBaseProperty().get();
  }

  public final void setPuntajeBase(final int puntajeBase) {
    this.puntajeBaseProperty().set(puntajeBase);
  }

  public final SimpleIntegerProperty exigenciaProperty() {
    return this.exigencia;
  }

  public final int getExigencia() {
    return this.exigenciaProperty().get();
  }

  public final void setExigencia(final int exigencia) {
    this.exigenciaProperty().set(exigencia);
  }

  public final SimpleObjectProperty<LocalDate> fechaLocalProperty() {
    return this.fechaLocal;
  }

  public final java.time.LocalDate getFechaLocal() {
    return this.fechaLocalProperty().get();
  }

  public final void setFechaLocal(final java.time.LocalDate fechaLocal) {
    this.fechaLocalProperty().set(fechaLocal);
  }

  public final SimpleObjectProperty<Estado> estadoProperty() {
    return this.estado;
  }

  public final cl.eos.persistence.models.Prueba.Estado getEstado() {
    return this.estadoProperty().get();
  }

  public final void setEstado(final cl.eos.persistence.models.Prueba.Estado estado) {
    this.estadoProperty().set(estado);
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OTPrueba other = (OTPrueba) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (id.get() != other.id.get())
      return false;
    return true;
  }

  public Prueba getPrueba() {
    return prueba;
  }

  public void setPrueba(Prueba prueba) {
    this.prueba = prueba;
  }



}
