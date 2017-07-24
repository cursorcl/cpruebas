package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.PruebasModel;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Clientes;
import cl.eos.restful.tables.R_EstadoPruebaCliente;
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
        model.setController(this);
        model.findAll(R_TipoPrueba.class, this);
        model.findAll(R_Profesor.class, this);
        model.findAll(R_TipoCurso.class, this);
        model.findAll(R_Asignatura.class, this);
        model.findAll(R_Clientes.class, this);
        model.findAll(R_EstadoPruebaCliente.class, this);
        model.findAll(R_NivelEvaluacion.class, this);
        model.findAll(R_Prueba.class, this);
        
    }

}
