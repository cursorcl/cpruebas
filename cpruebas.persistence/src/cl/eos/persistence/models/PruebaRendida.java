package cl.eos.persistence.models;

import java.util.Arrays;
import java.util.List;

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
@NamedQueries({ @NamedQuery(name = "PruebaRendida.findAll", query = "SELECT e FROM pruebarendida e") })
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
		if(alumno == null)
		{
			return "";
		}
		return alumno.getRut();
	}

	public String getPaterno() {
		if(alumno == null)
		{
			return "";
		}
		return alumno.getPaterno();
	}

	public String getMaterno() {
		if(alumno == null)
		{
			return "";
		}
		return alumno.getMaterno();
	}

	public String getNombre() {
		if(alumno == null)
		{
			return "";
		}
		return alumno.getName();
	}

	public String getCurso() {
		if(alumno == null)
		{
			return "";
		}
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
		float valor = ((float) (Utils.getPuntaje(getNota().floatValue()) / Utils.MAX_PUNTAJE)) * 100f;
		return Utils.redondeo2Decimales(valor);
	}

	public Float getPpuntajes() {
		float valor = ((float) (Utils.getPuntaje(getNota().floatValue()) / Utils.MAX_PUNTAJE)) * 100f;
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
		result = prime
				* result
				+ ((evaluacionPrueba == null) ? 0 : evaluacionPrueba.hashCode());
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

	/**
	 * Este metodo recalcula la nota y el rango de la prueba rendida. Esto se
	 * hace cuando se ha realizado una anulación de alguna pregunta.
	 */
	public void reEvaluate() {
		Prueba prueba = this.getEvaluacionPrueba().getPrueba();
		List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba
				.getRespuestas();
		StringBuilder strResps = new StringBuilder(this.getRespuestas());

		int buenas = 0;
		int malas = 0;
		int omitidas = 0;
		int anuladas = 0;
		int nroRespuestas = prueba.getNroPreguntas();
		int nroLast = Math.abs(strResps.length() - nroRespuestas);
		if (nroLast > 0) {
			for(int n = 0; n < nroLast; n++)
			{
				strResps.append("O");
			}
		}

		for (int n = 0; n < nroRespuestas; n++) {
			String letter = strResps.substring(n, n + 1);
			RespuestasEsperadasPrueba rEsperada = respuestasEsperadas.get(n);

			if (rEsperada.isAnulada()) {
				rEsperada.setRespuesta("*");
				strResps.replace(n, n+1, "*");
				anuladas++;
				continue;
			}

			if ("O".equalsIgnoreCase(letter)) {
				omitidas++;
			} else if ("M".equalsIgnoreCase(letter)) {
				malas++;
			} else {
				if (rEsperada.getMental()) {
					if ("B".equalsIgnoreCase(letter)) {
						strResps.replace(n, n + 1, "+");
						buenas++;
					} else if ("D".equalsIgnoreCase(letter)) {
						strResps.replace(n, n + 1, "-");
						malas++;
					} else {
						malas++;
					}
				} else if (rEsperada.getVerdaderoFalso()) {
					if ("B".equalsIgnoreCase(letter)) {
						strResps.replace(n, n + 1, "V");
						letter = "V";
					} else if ("D".equalsIgnoreCase(letter)) {
						strResps.replace(n, n + 1, "F");
						letter = "F";
					}

					if (rEsperada.getRespuesta().equalsIgnoreCase(letter)) {
						buenas++;
					} else {
						malas++;
					}
				} else {
					if (rEsperada.getRespuesta().equalsIgnoreCase(letter)) {
						buenas++;
					} else {
						malas++;
					}
				}
			}
		}
		int nroPreguntas = prueba.getNroPreguntas() - anuladas;
		float nota = Utils.getNota(nroPreguntas, prueba.getExigencia(), buenas,
				prueba.getPuntajeBase());
		this.setRespuestas(strResps.toString());
		this.setBuenas(buenas);
		this.setMalas(malas);
		this.setOmitidas(omitidas);
		this.setNota(nota);
		float porcentaje = ((float) this.getBuenas()) / nroPreguntas * 100f;
		RangoEvaluacion rango = prueba.getNivelEvaluacion()
				.getRango(porcentaje);
		this.setRango(rango);
	}

}
