package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.EjesTematicosModel;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.TipoPrueba;

public class EjesTematicosContoller extends AController {

    public EjesTematicosContoller() {

    }

    @Override
    public void initialize() {
        model = new EjesTematicosModel();
        model.findAll(EjeTematico.class, this);
        model.findAll(TipoPrueba.class, this);
        model.findAll(Asignatura.class, this);
    }
}
