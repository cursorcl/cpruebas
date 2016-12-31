package cl.eos.controller;

import java.util.List;

import cl.eos.imp.controller.AController;
import cl.eos.interfaces.entity.IEntity;
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

    /* (non-Javadoc)
     * @see cl.eos.imp.controller.AController#onError(java.lang.String)
     */
    @Override
    public void onError(String error) {
        // TODO Auto-generated method stub
        super.onError(error);
    }

    /* (non-Javadoc)
     * @see cl.eos.imp.controller.AController#onFindAllFinished(java.util.List)
     */
    @Override
    public void onFindAllFinished(List<Object> list) {
        // TODO Auto-generated method stub
        super.onFindAllFinished(list);
    }

    /* (non-Javadoc)
     * @see cl.eos.imp.controller.AController#onFindFinished(java.util.List)
     */
    @Override
    public void onFindFinished(List<Object> list) {
        // TODO Auto-generated method stub
        super.onFindFinished(list);
    }

    /* (non-Javadoc)
     * @see cl.eos.imp.controller.AController#onFound(cl.eos.interfaces.entity.IEntity)
     */
    @Override
    public void onFound(IEntity entity) {
        // TODO Auto-generated method stub
        super.onFound(entity);
    }

}
