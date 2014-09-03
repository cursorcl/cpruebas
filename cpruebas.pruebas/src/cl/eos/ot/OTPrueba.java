package cl.eos.ot;

import java.util.Date;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.TipoPrueba;

public class OTPrueba {
	private Long id;

	private String name;

	private TipoPrueba tipoPrueba;
	private Curso curso;
	private Asignatura asignatura;

	private Date fecha;
	private Integer nroPreguntas;
	private Integer formas;
	private Integer alternativas;

	private String responses;
}
