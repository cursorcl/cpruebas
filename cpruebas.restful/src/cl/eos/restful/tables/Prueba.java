package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class Prueba  extends AEntity{
    
    private static final long serialVersionUID = 1L;
    
    Long id;
    Integer exigencia;
    Integer alternativas;
    Long fecha;
    Integer puntajebase;
    Integer nroformas;
    String responses;
    Integer nropreguntas;
    String name;
    Long asignatura_id;
    Long curso_id;
    Long tipoprueba_id;
    Long profesor_id;
    Long nivelevaluacion_id;

    /**
     * @return the id
     */
    public final Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the exigencia
     */
    public final Integer getExigencia() {
        return exigencia;
    }

    /**
     * @param exigencia the exigencia to set
     */
    public final void setExigencia(Integer exigencia) {
        this.exigencia = exigencia;
    }

    /**
     * @return the alternativas
     */
    public final Integer getAlternativas() {
        return alternativas;
    }

    /**
     * @param alternativas the alternativas to set
     */
    public final void setAlternativas(Integer alternativas) {
        this.alternativas = alternativas;
    }

    /**
     * @return the fecha
     */
    public final Long getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public final void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the puntajebase
     */
    public final Integer getPuntajebase() {
        return puntajebase;
    }

    /**
     * @param puntajebase the puntajebase to set
     */
    public final void setPuntajebase(Integer puntajebase) {
        this.puntajebase = puntajebase;
    }

    /**
     * @return the nroformas
     */
    public final Integer getNroformas() {
        return nroformas;
    }

    /**
     * @param nroformas the nroformas to set
     */
    public final void setNroformas(Integer nroformas) {
        this.nroformas = nroformas;
    }

    /**
     * @return the responses
     */
    public final String getResponses() {
        return responses;
    }

    /**
     * @param responses the responses to set
     */
    public final void setResponses(String responses) {
        this.responses = responses;
    }

    /**
     * @return the nropreguntas
     */
    public final Integer getNropreguntas() {
        return nropreguntas;
    }

    /**
     * @param nropreguntas the nropreguntas to set
     */
    public final void setNropreguntas(Integer nropreguntas) {
        this.nropreguntas = nropreguntas;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return the asignatura_id
     */
    public final Long getAsignatura_id() {
        return asignatura_id;
    }

    /**
     * @param asignatura_id the asignatura_id to set
     */
    public final void setAsignatura_id(Long asignatura_id) {
        this.asignatura_id = asignatura_id;
    }

    /**
     * @return the curso_id
     */
    public final Long getCurso_id() {
        return curso_id;
    }

    /**
     * @param curso_id the curso_id to set
     */
    public final void setCurso_id(Long curso_id) {
        this.curso_id = curso_id;
    }

    /**
     * @return the tipoprueba_id
     */
    public final Long getTipoprueba_id() {
        return tipoprueba_id;
    }

    /**
     * @param tipoprueba_id the tipoprueba_id to set
     */
    public final void setTipoprueba_id(Long tipoprueba_id) {
        this.tipoprueba_id = tipoprueba_id;
    }

    /**
     * @return the profesor_id
     */
    public final Long getProfesor_id() {
        return profesor_id;
    }

    /**
     * @param profesor_id the profesor_id to set
     */
    public final void setProfesor_id(Long profesor_id) {
        this.profesor_id = profesor_id;
    }

    /**
     * @return the nivelevaluacion_id
     */
    public final Long getNivelevaluacion_id() {
        return nivelevaluacion_id;
    }

    /**
     * @param nivelevaluacion_id the nivelevaluacion_id to set
     */
    public final void setNivelevaluacion_id(Long nivelevaluacion_id) {
        this.nivelevaluacion_id = nivelevaluacion_id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder2 = new StringBuilder();
        builder2.append("Prueba [id=");
        builder2.append(id);
        builder2.append(", exigencia=");
        builder2.append(exigencia);
        builder2.append(", alternativas=");
        builder2.append(alternativas);
        builder2.append(", fecha=");
        builder2.append(fecha);
        builder2.append(", puntajebase=");
        builder2.append(puntajebase);
        builder2.append(", nroformas=");
        builder2.append(nroformas);
        builder2.append(", responses=");
        builder2.append(responses);
        builder2.append(", nropreguntas=");
        builder2.append(nropreguntas);
        builder2.append(", name=");
        builder2.append(name);
        builder2.append(", asignatura_id=");
        builder2.append(asignatura_id);
        builder2.append(", curso_id=");
        builder2.append(curso_id);
        builder2.append(", tipoprueba_id=");
        builder2.append(tipoprueba_id);
        builder2.append(", profesor_id=");
        builder2.append(profesor_id);
        builder2.append(", nivelevaluacion_id=");
        builder2.append(nivelevaluacion_id);
        builder2.append(", version=");
        builder2.append(version);
        builder2.append("]");
        return builder2.toString();
    }
    
    
}
