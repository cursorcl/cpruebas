package cl.eos.controller;

import java.util.List;

import cl.eos.imp.controller.AController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.model.PruebasModel;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.restful.tables.R_TipoPrueba;

public class PruebasController extends AController {

    public PruebasController() {

    }

    @Override
    public void initialize() {
        model = new PruebasModel();
        model.findAll(R_TipoPrueba.class, this);
        model.findAll(R_Profesor.class, this);
        model.findAll(R_TipoCurso.class, this);
        model.findAll(R_Asignatura.class, this);
        model.findAll(R_NivelEvaluacion.class, this);
        model.findAll(R_EvaluacionPrueba.class, this);
        model.findAll(R_Prueba.class, this);
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
