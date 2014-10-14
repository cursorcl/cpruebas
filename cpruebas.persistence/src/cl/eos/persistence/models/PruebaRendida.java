package cl.eos.persistence.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.util.Utils;

@Entity(name = "pruebarendida")
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

	@ManyToOne(fetch = FetchType.EAGER)
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
		return nota;
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
		return ((float) this.buenas) / totalRespuestas * 100f;
	}

	public Integer getPuntaje() {
		return (Integer) Utils.getPuntaje(getNota().floatValue());
	}

	public Float getPpuntaje() {
		return (float) (Utils.getPuntaje(getNota().floatValue()) / Utils.MAX_PUNTAJE);
	}

	public String getPpuntajes() {
		NumberFormat formatter = new DecimalFormat("#0,00");
		float valor = (float) (Utils.getPuntaje(getNota().floatValue()) / Utils.MAX_PUNTAJE);
		return formatter.format(valor);

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

}
