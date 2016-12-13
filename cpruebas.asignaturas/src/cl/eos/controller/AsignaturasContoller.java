package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.AsignaturasModel;
import cl.eos.persistence.models.SAsignatura;

public class AsignaturasContoller extends AController {

    public AsignaturasContoller() {

    }

    @Override
    public void initialize() {
        model = new AsignaturasModel();
        model.findAll(SAsignatura.class, this);
    }
}
