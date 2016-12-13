package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.PruebasModel;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SNivelEvaluacion;
import cl.eos.persistence.models.SProfesor;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.STipoCurso;
import cl.eos.persistence.models.STipoPrueba;

public class PruebasController extends AController {

    public PruebasController() {

    }

    @Override
    public void initialize() {
        model = new PruebasModel();
        model.findAll(SPrueba.class, this);
        model.findAll(STipoPrueba.class, this);
        model.findAll(SProfesor.class, this);
        model.findAll(STipoCurso.class, this);
        model.findAll(SAsignatura.class, this);
        model.findAll(SNivelEvaluacion.class, this);
        model.findAll(SEvaluacionPrueba.class, this);
    }

}
