package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.CursosModel;
import cl.eos.restful.tables.R_Ciclo;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_TipoCurso;

public class CursosContoller extends AController {

    public CursosContoller() {

    }

    @Override
    public void initialize() {
        model = new CursosModel();
        model.findAll(R_Curso.class, this);
        model.findAll(R_Ciclo.class, this);
        model.findAll(R_Colegio.class, this);
        model.findAll(R_TipoCurso.class, this);
    }
}
