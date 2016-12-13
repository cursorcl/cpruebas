package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.CursosModel;
import cl.eos.persistence.models.SCiclo;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.STipoCurso;

public class CursosContoller extends AController {

    public CursosContoller() {

    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        model = new CursosModel();
        model.findAll(SCurso.class, this);
        model.findAll(SCiclo.class, this);
        model.findAll(SColegio.class, this);
        model.findAll(STipoCurso.class, this);
    }
}
