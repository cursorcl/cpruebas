package cl.eos.persistence.models;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "pruebarendida")
public class PruebaRendida implements IEntity {

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
	
	/**
	 * Corresponde a la forma asociada a la prueba del alumno.
	 */
	private Integer forma;

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
		Integer totalRespuestas = this.malas + this.buenas + this.omitidas;
//		DecimalFormat df = new DecimalFormat("#.##");
//		System.out.print(df.format(d))
		return (float) (((float)this.buenas / (float)totalRespuestas) * 100f);
	}

	public Integer getPuntaje() {
		return (Integer) this.buenas;
	}

	public Float getPpuntaje() {
		Integer totalRespuestas = this.malas + this.buenas + this.omitidas;
		return (float) (((float)this.buenas / (float)totalRespuestas) * 100f);
	}

}
