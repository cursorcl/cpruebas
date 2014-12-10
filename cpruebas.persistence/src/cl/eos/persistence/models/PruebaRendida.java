package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.util.Utils;

@Entity(name = "pruebarendida")
@NamedQueries({ @NamedQuery(name = "PruebaRendida.findAll", query = "SELECT e FROM pruebarendida e")})
public class PruebaRendida implements IEntity {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Alumno alumno;
  /**
   * Las respuestas del alumno.
   */
  private String respuestas;
  private Integer buenas;
  private Integer malas;
  private Integer omitidas;
  private Float nota;

  @ManyToOne
  private EvaluacionPrueba evaluacionPrueba;

  private RangoEvaluacion rango;

  /**
   * Corresponde a la forma asociada a la prueba del alumno.
   */
  private Integer forma;

  public PruebaRendida() {
    buenas = new Integer(0);
    malas = new Integer(0);
    omitidas = new Integer(0);
    forma = new Integer(1);
    nota = new Float(0);
    respuestas = "";
    alumno = null;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;

  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  public Alumno getAlumno() {
    return alumno;
  }

  public void setAlumno(Alumno alumno) {
    this.alumno = alumno;
  }

  public String getRespuestas() {
    return respuestas;
  }

  public void setRespuestas(String respuestas) {
    this.respuestas = respuestas;
  }

  public Integer getBuenas() {
    return buenas;
  }

  public void setBuenas(Integer buenas) {
    this.buenas = buenas;
  }

  public Integer getMalas() {
    return malas;
  }

  public void setMalas(Integer malas) {
    this.malas = malas;
  }

  public Integer getOmitidas() {
    return omitidas;
  }

  public void setOmitidas(Integer omitidas) {
    this.omitidas = omitidas;
  }

  @Override
  public boolean validate() {
    return true;
  }

  public Float getNota() {
    return Utils.redondeo2Decimales(nota);
  }

  public void setNota(Float nota) {
    this.nota = nota;
  }

  public Integer getForma() {
    return forma;
  }

  public void setForma(Integer forma) {
    this.forma = forma;
  }

  public String getRut() {
    return alumno.getRut();
  }

  public String getPaterno() {
    return alumno.getPaterno();
  }

  public String getMaterno() {
    return alumno.getMaterno();
  }

  public String getNombre() {
    return alumno.getName();
  }

  public String getCurso() {
    return alumno.getCurso().getName();
  }

  public Float getPbuenas() {
    Float totalRespuestas = (float) (this.malas + this.buenas + this.omitidas);
    float valor = ((float) this.buenas) / totalRespuestas * 100f;
    return Utils.redondeo2Decimales(valor);
  }

  public Integer getPuntaje() {
    return (Integer) Utils.getPuntaje(getNota().floatValue());
  }

  public Float getPpuntaje() { 
    float valor = ((float) (Utils.getPuntaje(getNota().floatValue()) / Utils.MAX_PUNTAJE))*100f;
    return Utils.redondeo2Decimales(valor);
  }

  public Float getPpuntajes() {   
    float valor = ((float) (Utils.getPuntaje(getNota().floatValue()) / Utils.MAX_PUNTAJE))*100f;
    return Utils.redondeo2Decimales(valor);

  }

  public EvaluacionPrueba getEvaluacionPrueba() {
    return evaluacionPrueba;
  }

  public void setEvaluacionPrueba(EvaluacionPrueba evaluacionPrueba) {
    this.evaluacionPrueba = evaluacionPrueba;
  }

  public RangoEvaluacion getRango() {
    return rango;
  }

  public void setRango(RangoEvaluacion rango) {
    this.rango = rango;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((alumno == null) ? 0 : alumno.hashCode());
    result = prime * result + ((evaluacionPrueba == null) ? 0 : evaluacionPrueba.hashCode());
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
    PruebaRendida other = (PruebaRendida) obj;
    if (alumno == null) {
      if (other.alumno != null)
        return false;
    } else if (!alumno.equals(other.alumno))
      return false;
    if (evaluacionPrueba == null) {
      if (other.evaluacionPrueba != null)
        return false;
    } else if (!evaluacionPrueba.equals(other.evaluacionPrueba))
      return false;
    return true;
  }


}
