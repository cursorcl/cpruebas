package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.AlumnosModel;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_TipoAlumno;

public class AlumnosContoller extends AController {

    @Override
    public void initialize() {
        model = new AlumnosModel();
        
        model.findAll(R_Colegio.class, this);
        model.findAll(R_Curso.class, this);
        model.findAll(R_TipoAlumno.class, this);
    }

}
