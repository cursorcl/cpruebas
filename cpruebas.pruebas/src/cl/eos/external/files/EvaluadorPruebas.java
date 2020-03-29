package cl.eos.external.files;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.eos.interfaces.controller.IController;
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
import cl.eos.util.Utils;
import cl.eos.view.ots.OTPruebaRendida;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta clase es para recibir información de un archivo Excel y evaluarlo.
 * 
 * @author eosorio
 *
 */
public class EvaluadorPruebas {

	protected IController controller;
	protected Colegio colegio = null;
	protected Curso curso = null;
	protected Prueba prueba = null;
	protected Profesor profesor = null;
	protected Asignatura asignatura = null;

	public EvaluadorPruebas(IController controller) {
		this.controller = controller;
	}

	public void init() {

	}

	public Asignatura getAsignatura(Long id) {
		return (Asignatura) controller.findSynchroById(Asignatura.class, id);
	}

	public Colegio getColegio(Long id) {
		return (Colegio) controller.findSynchroById(Colegio.class, id);
	}

	public Curso getCurso(Long id) {
		return (Curso) controller.findSynchroById(Curso.class, id);
	}

	public Profesor getProfesor(Long id) {
		return (Profesor) controller.findSynchroById(Profesor.class, id);
	}

	@SuppressWarnings("unchecked")
	public Prueba getPrueba(Asignatura asignatura, Curso curso) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Prueba> pruebas = (List<Prueba>) controller.findSynchro("findByAsignaturaCurso", parameters);
		return pruebas == null || pruebas.isEmpty() ? null : pruebas.get(0);
	}

	public EvaluacionPrueba getEvaluation() {
		EvaluacionPrueba evalPrueba = new EvaluacionPrueba();
		evalPrueba.setColegio(colegio);
		evalPrueba.setCurso(curso);
		evalPrueba.setFecha(Instant.now().toEpochMilli());
		evalPrueba.setPrueba(prueba);
		evalPrueba.setProfesor(profesor);
		return evalPrueba;
	}

	/**
	 * Genero la lista de Pruebas Rendidas para una evaluación.
	 * 
	 * @param prueba
	 * @param curso
	 * @return
	 */
	public ObservableList<OTPruebaRendida> getPruebasRendida(Prueba prueba, Curso curso) {
		EvaluacionPrueba evalPrueba = null;

		final List<EvaluacionPrueba> listEvaluaciones = prueba.getEvaluaciones();
		if (listEvaluaciones != null && !listEvaluaciones.isEmpty()) {
			for (final EvaluacionPrueba evaluacion : listEvaluaciones) {

				if ((evaluacion.getColegio() != null && evaluacion.getColegio().equals(colegio))
						&& (evaluacion.getCurso() != null && evaluacion.getCurso().equals(curso))) {
					evalPrueba = evaluacion;
					evalPrueba.getPruebasRendidas().isEmpty();
					break;
				}
			}
		}
		final ObservableList<OTPruebaRendida> oList = FXCollections.observableArrayList();
		if (evalPrueba == null) {
			// Tengo que crear la evaluacion Prueba.
			evalPrueba = getEvaluation();
		}

		if (curso.getAlumnos() != null && !curso.getAlumnos().isEmpty()) {
			for (final Alumno alumno : curso.getAlumnos()) {
				PruebaRendida pRendida = new PruebaRendida();
				pRendida.setAlumno(alumno);
				pRendida.setEvaluacionPrueba(evalPrueba);
				if (evalPrueba.getPruebasRendidas().contains(pRendida)) {
					final int idx = evalPrueba.getPruebasRendidas().indexOf(pRendida);
					pRendida = evalPrueba.getPruebasRendidas().get(idx);
				}
				oList.add(new OTPruebaRendida(pRendida));
			}
		}
		return oList;
	}

	
	
	
	/**
	 * Graba el resultado de la prueba de todo el curso..
	 * @param evalPrueba
	 * @param pruebasRendidas
	 */
	protected void save(EvaluacionPrueba evalPrueba, List<OTPruebaRendida> pRendidas) {
		
            if (!prueba.getEvaluaciones().contains(evalPrueba)) {
                final String s = String.format("%s %s %s %s", evalPrueba.getAsignatura(), evalPrueba.getColegio(),
                        evalPrueba.getCurso(), evalPrueba.getFechaLocal().toString());
                evalPrueba.setPrueba(prueba);
                evalPrueba.setName(s);
            }
            if(evalPrueba.getId() == null)
            	evalPrueba = (EvaluacionPrueba) controller.save(evalPrueba);
            
            final List<PruebaRendida> lstPruebasRendidas = new ArrayList<>();
            for (final OTPruebaRendida otPRendida : pRendidas) {
                if (otPRendida.isRindioPrueba() && otPRendida.getRespuestas() != null
                        && !otPRendida.getRespuestas().trim().isEmpty()) {
                    PruebaRendida pRendida = otPRendida.getPruebaRendida();
                    pRendida.setEvaluacionPrueba(evalPrueba);
                    if (pRendida.getId() != null) {
                        pRendida = (PruebaRendida) controller.save(pRendida);
                    }
                    lstPruebasRendidas.add(pRendida);
                } else {
                    if (otPRendida.getPruebaRendida() != null) {
                    	controller.delete(otPRendida.getPruebaRendida());
                    }
                }

        }
    }


protected void evaluar(String respsAlumno, OTPruebaRendida otRendida, List<RespuestasEsperadasPrueba> respuestas) {

	final int nroPreguntas = respuestas.size();

	otRendida.setOmitidas(0);
	final int nroLast = Math.abs(respsAlumno.length() - nroPreguntas);
	if (nroLast > 0) {
		final char[] c = new char[nroLast];
		Arrays.fill(c, 'O');
		final StringBuilder sBuilder = new StringBuilder(respsAlumno);
		sBuilder.append(c);
		respsAlumno = sBuilder.toString();
	}
	otRendida.setBuenas(0);
	otRendida.setMalas(0);
	int anuladas = 0;
	final StringBuilder strResps = new StringBuilder(respsAlumno);
	for (int n = 0; n < nroPreguntas; n++) {
		final RespuestasEsperadasPrueba resp = respuestas.get(n);
		if (resp.isAnulada()) {
			resp.setRespuesta("*");
			strResps.replace(n, n + 1, "*");
			anuladas++;
			continue;
		}
		final String userResp = respsAlumno.substring(n, n + 1);
		String validResp = resp.getRespuesta();
		if (resp.getMental()) {
			validResp = "+";
		}
		if (userResp.toUpperCase().equals("O")) {
			otRendida.setOmitidas(otRendida.getOmitidas() + 1);
		} else if (userResp.toUpperCase().equals(validResp.toUpperCase())) {
			otRendida.setBuenas(otRendida.getBuenas() + 1);
		} else {
			otRendida.setMalas(otRendida.getMalas() + 1);
		}
	}
	final float porcDificultad = prueba.getExigencia() == null ? 60f : prueba.getExigencia();
	final float notaMinima = 1.0f;
	otRendida.setNota(Utils.getNota(nroPreguntas - anuladas, porcDificultad, otRendida.getBuenas(), notaMinima));

	final float total = nroPreguntas - anuladas;
	final float porcentaje = (float) otRendida.getBuenas() / total * 100f;

	final RangoEvaluacion rango = prueba.getNivelEvaluacion().getRango(porcentaje);
	otRendida.setNivel(rango);
}

}
