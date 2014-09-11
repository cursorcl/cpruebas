package cl.eos.persistence.models;

import javax.persistence.Entity;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "pruebarendida")
public class PruebaRendida implements IEntity {

	private Alumno alumno;
	private Prueba prueba;
	private String respuestas;
	private Integer buenas;
	private Integer malas;
	private Integer omitidas; 
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Prueba getPrueba() {
		return prueba;
	}

	public void setPrueba(Prueba prueba) {
		this.prueba = prueba;
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
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
