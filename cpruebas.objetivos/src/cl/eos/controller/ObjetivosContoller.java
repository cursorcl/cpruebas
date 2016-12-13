package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ObjetivosModel;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SObjetivo;
import cl.eos.persistence.models.STipoCurso;

public class ObjetivosContoller extends AController {

    public ObjetivosContoller() {

    }

    @Override
    public void initialize() {
        model = new ObjetivosModel();
        model.findAll(SObjetivo.class, this);
        model.findAll(STipoCurso.class, this);
        model.findAll(SAsignatura.class, this);
    }
}
