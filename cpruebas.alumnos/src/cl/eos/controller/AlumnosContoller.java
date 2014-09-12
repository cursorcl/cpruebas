package cl.eos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.eos.imp.controller.AController;
import cl.eos.model.AlumnosModel;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;

public class AlumnosContoller extends AController {

	public AlumnosContoller() {
		
	}

	@Override
	public void initialize() {
		model = new AlumnosModel();
		model.findAll(Alumno.class, this);
		model.findAll(Colegio.class, this);
		model.findAll(Curso.class, this);	
		

	}

	@Override
	public void onFindAllFinished(List<Object> list) {
		super.onFindAllFinished(list);
	}
	
	public void buscarPorDireccion(String direccion)
	{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dato", direccion);
		model.find("Alumno.preguntita", param);
	}
	
}
