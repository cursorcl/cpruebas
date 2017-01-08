package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ObjetivosModel;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_TipoCurso;

public class ObjetivosContoller extends AController {

    public ObjetivosContoller() {

    }

    @Override
    public void initialize() {
        model = new ObjetivosModel();
        model.findAll(R_Objetivo.class, this);
        model.findAll(R_TipoCurso.class, this);
        model.findAll(R_Asignatura.class, this);
    }
}
