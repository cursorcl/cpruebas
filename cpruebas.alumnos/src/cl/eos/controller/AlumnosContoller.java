package cl.eos.controller;

import java.util.HashMap;
import java.util.Map;

import cl.eos.imp.controller.AController;
import cl.eos.model.AlumnosModel;
import cl.eos.persistence.models.SAlumno;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.STipoAlumno;

public class AlumnosContoller extends AController {

    public void buscarPorDireccion(String direccion) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("dato", direccion);
        model.find("SAlumno.preguntita", param);
    }

    @Override
    public void initialize() {
        model = new AlumnosModel();
        model.findAll(SAlumno.class, this);
        model.findAll(SColegio.class, this);
        model.findAll(SCurso.class, this);
        model.findAll(SCurso.class, this);
        model.findAll(STipoAlumno.class, this);
    }

}
