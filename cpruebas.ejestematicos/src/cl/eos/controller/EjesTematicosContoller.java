package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.EjesTematicosModel;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.STipoPrueba;

public class EjesTematicosContoller extends AController {

    public EjesTematicosContoller() {

    }

    @Override
    public void initialize() {
        model = new EjesTematicosModel();
        model.findAll(SEjeTematico.class, this);
        model.findAll(STipoPrueba.class, this);
        model.findAll(SAsignatura.class, this);
    }
}
