package cl.eos.external.files;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cl.eos.external.files.utils.KeyEvaluation;
import cl.eos.external.files.utils.KeyExams;
import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.util.Utils;

/**
 * Esta clase es para recibir información de un archivo Excel y evaluarlo.
 * 
 * @author eosorio
 *
 */
public class EvaluadorPruebasDataBridge {

	private static Logger log = Logger.getLogger(EvaluadorPruebasDataBridge.class.getName());
	protected IController controller;

	protected Profesor profesor = null;

	protected Map<Long, Colegio> colegios = new HashMap<>();
	protected Map<Long, Curso> cursos = new HashMap<>();
	protected Map<KeyExams, Prueba> pruebas = new HashMap<>();
	protected Map<KeyEvaluation, EvaluacionPrueba> evaluaciones = new HashMap<>();

	protected Map<Long, Asignatura> asignaturas = new HashMap<>();
	protected Map<Long, TipoCurso> tiposCurso = new HashMap<>();

	public EvaluadorPruebasDataBridge() {
		this.controller = new PruebasExternasController();
	}

	public void init() {

	}

	public Asignatura getAsignatura(Long id) {
		Asignatura asignatura = asignaturas.get(id);
		if (asignatura == null) {
			asignatura = (Asignatura) controller.findSynchroById(Asignatura.class, id);
			asignaturas.put(id, asignatura);
		}
		return asignatura;

	}

	public TipoCurso getTipoCurso(Long id) {
		TipoCurso tipoCurso = tiposCurso.get(id);
		if (tipoCurso == null) {
			tipoCurso = (TipoCurso) controller.findSynchroById(TipoCurso.class, id);
			tiposCurso.put(id, tipoCurso);
		}
		return tipoCurso;

	}

	/**
	 * Colegio por defecto
	 * 
	 * @return
	 */
	public Colegio getColegio(Long idColegio) {
		Colegio colegio = colegios.get(idColegio);
		if (colegio == null) {
			colegio = (Colegio) controller.findSynchroById(Colegio.class, idColegio);
			colegios.put(idColegio, colegio);
		}
		return colegio;
	}

	/**
	 * Profesor por defecto.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Profesor getProfesor() {
		if (profesor == null) {
			List<Profesor> profesores = (List<Profesor>) controller.findSynchro("Profesor.findAll", null);
			if (!(profesores == null || profesores.isEmpty()))
				profesor = profesores.get(0);

		}
		return profesor;
	}

	public Curso getCurso(Long id) {

		Curso curso = cursos.get(id);
		if (curso == null) {
			curso = (Curso) controller.findSynchroById(Curso.class, id);
			cursos.put(id, curso);
		}
		return curso;

	}

	@SuppressWarnings("unchecked")
	public Prueba getPrueba(Asignatura asignatura, Curso curso) {

		Prueba prueba = pruebas.get(new KeyExams(asignatura, curso));
		if (prueba == null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("idAsignatura", asignatura.getId());
			parameters.put("idTipoCurso", curso.getTipoCurso().getId());
			List<Prueba> lPruebas = (List<Prueba>) controller.findSynchro("Prueba.findPruebaByAsignaturaCurso",
					parameters);
			prueba = lPruebas == null || lPruebas.isEmpty() ? null : lPruebas.get(0);
		}
		return prueba;
	}

	/**
	 * Obtiene las evaluación prueba para la combinación Prueba Curso sino existe,
	 * se crea..
	 * 
	 * @param prueba La prueba a la que se le busca la evaluación.
	 * @param curso  El curso al que se le buscan la evaluación.
	 * @return Evaluación asociada a la prueba.
	 */
	@SuppressWarnings("unchecked")
	public EvaluacionPrueba getEvaluacionPrueba(Prueba prueba, Curso curso) {

		KeyEvaluation key = new KeyEvaluation(prueba, curso);
		EvaluacionPrueba evalPrueba = evaluaciones.get(key);

		if (evalPrueba != null)
			return evalPrueba;

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("pruebaId", prueba.getId());
		parameters.put("cursoId", curso.getId());
		List<EvaluacionPrueba> evaluacionPrueba = (List<EvaluacionPrueba>) controller
				.findSynchro("EvaluacionPrueba.findByPruebaCurso", parameters);

		if (evaluacionPrueba == null || evaluacionPrueba.isEmpty()) {

			// No la tiene, por tanto creamos una evaluación.
			evalPrueba = new EvaluacionPrueba();
			evalPrueba.setName(prueba.getAsignatura().getName() + " " + curso.getName());
			evalPrueba.setColegio(curso.getColegio());
			evalPrueba.setCurso(curso);
			evalPrueba.setPrueba(prueba);
			evalPrueba.setProfesor(getProfesor());
			evalPrueba.setFecha(LocalDate.now().toEpochDay());

			message(String.format("Creando nueva evaluación para el curso %s prueba %d.", curso.getName(),
					prueba.getId()));
			evalPrueba = (EvaluacionPrueba) controller.save(evalPrueba);
			log.info("Creada evaluación:" + evalPrueba.getId() + " " + evalPrueba.getName());
		} else {
			evalPrueba = evaluacionPrueba.get(0);
			evalPrueba.getPruebasRendidas().isEmpty();
		}
		evaluaciones.put(key, evalPrueba);
		return evalPrueba;
	}

	private void message(String string) {
		// TODO Auto-generated method stub

	}

	/**
	 * Realiza la evaluación de las respuestas de un alumno.
	 * 
	 * Tiene una pequeña diferencia con la evaluación original
	 * 
	 * <li>La original para las preguntas V y F considera B=V y D=F.</li>
	 * <li>La actual para las preguntas V y F considera A=V y B=F.</li>
	 * 
	 * @param prueba     La prueba que tiene las respuestas esperadas.
	 * @param alumno     Alumno a ser evaluado.
	 * @param respuestas Respuestas del a
	 * @return
	 */
	public PruebaRendida generarPruebaRendida(Prueba prueba, Alumno alumno, String respuestas) {

		PruebaRendida pRendida = null;
		String rut = alumno.getRut();
		final StringBuilder sbRut = new StringBuilder(rut).insert(rut.length() - 1, '-');
		rut = sbRut.toString();

		final StringBuilder strResps = new StringBuilder(respuestas);

		int buenas = 0;
		int malas = 0;
		int omitidas = 0;
		int anuladas = 0;

		for (int n = 0; n < prueba.getNroPreguntas(); n++) {
			String letter = strResps.substring(n, n + 1);
			final RespuestasEsperadasPrueba rEsperada = prueba.getRespuestas().get(n);

			if (rEsperada.isAnulada()) {
				rEsperada.setRespuesta("*");
				strResps.replace(n, n + 1, "*");
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
					// Ya que se hizo VF con AB.
					// Se hace cambio porque en el excel viene así.
					if ("A".equalsIgnoreCase(letter)) {
						strResps.replace(n, n + 1, "V");
						letter = "V";
					} else if ("B".equalsIgnoreCase(letter)) {
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

			final int nroPreguntas = prueba.getNroPreguntas() - anuladas;
			final float nota = Utils.getNota(nroPreguntas, prueba.getExigencia(), buenas, prueba.getPuntajeBase());
			pRendida = new PruebaRendida();
			pRendida.setAlumno(alumno);
			pRendida.setBuenas(buenas);
			pRendida.setMalas(malas);
			pRendida.setOmitidas(omitidas);
			pRendida.setNota(nota);
			pRendida.setRespuestas(strResps.toString());
			final float porcentaje = (float) pRendida.getBuenas() / nroPreguntas * 100f;
			final RangoEvaluacion rango = prueba.getNivelEvaluacion().getRango(porcentaje);
			pRendida.setRango(rango);

			pRendida.setRespuestas(strResps.toString());
		}
		return pRendida;
	}

	/**
	 * Graba una prueba rendida, se preocupa de verificar si esta ya existe.
	 * 
	 * De existir la elimina y luego graba.
	 * 
	 * @param pRendida Prueba a grabar.
	 * @return La prueba con su identidicador.
	 */
	@SuppressWarnings("unchecked")
	public PruebaRendida savePruebaRendida(PruebaRendida pRendida) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idAlumno", pRendida.getAlumno().getId());
		parameters.put("idEvaluacion", pRendida.getEvaluacionPrueba().getId());
		List<PruebaRendida> pRendidas = (List<PruebaRendida>) controller
				.findSynchro("PruebaRendida.findAlumnoEvaluacion", parameters);
		if (pRendidas != null && !pRendidas.isEmpty()) {
			PruebaRendida oldPRendida = pRendidas.get(0);
			oldPRendida.setBuenas(pRendida.getBuenas());
			oldPRendida.setMalas(pRendida.getMalas());
			oldPRendida.setNota(pRendida.getNota());
			oldPRendida.setOmitidas(pRendida.getOmitidas());
			oldPRendida.setRango(pRendida.getRango());
			oldPRendida.setRespuestas(pRendida.getRespuestas());

			return (PruebaRendida) save(oldPRendida);
		}
		return (PruebaRendida) save(pRendida);
	}

	public IEntity save(IEntity entity) {

		return controller.save(entity);
	}
}
