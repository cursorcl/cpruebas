package cl.eos.view;

import java.util.List;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.persistence.models.TipoPrueba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataProcessor {

    public static void process(List<Object> list, DefinirPrueba defPrueba)
    {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
//            if (entity instanceof Prueba) {
//                ObservableList<OTPrueba> pruebas = FXCollections.observableArrayList();
//                for (Object lEntity : list) {
//                    pruebas.add(new OTPrueba((Prueba) lEntity));
//                }
//                tblListadoPruebas.setItems(pruebas);
//            }
            processTipoPrueba(entity, defPrueba, list);
            processProfesor(entity, defPrueba, list);
            processTipoCurso(entity, defPrueba, list);
            processAsignatura(entity, defPrueba, list);
            processNivelEvaluacion(entity, defPrueba, list);
            processHabilidad(entity, defPrueba, list);
            processEjeTematico(entity, defPrueba, list);
            processObjetivo(entity, defPrueba, list);
        }
    }

    private static void processObjetivo(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof Objetivo) {
            ObservableList<Objetivo> objetivos = FXCollections.observableArrayList();
            for (Object lEntity : list) {
                objetivos.add((Objetivo) lEntity);
            }
            defPrueba.cmbObjetivos.setItems(objetivos);
            if(list != null &&  !list.isEmpty())
                defPrueba.cmbObjetivos.getSelectionModel().select(0);
        }
    }

    private static void processEjeTematico(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof EjeTematico) {
            ObservableList<EjeTematico> ejesTematicos = FXCollections.observableArrayList();
            for (Object lEntity : list) {
                ejesTematicos.add((EjeTematico) lEntity);
            }
            defPrueba.cmbEjesTematicos.setItems(ejesTematicos);
            if(list != null &&  !list.isEmpty())
                defPrueba.cmbEjesTematicos.getSelectionModel().select(0);
        }
    }

    private static void processHabilidad(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof Habilidad) {
            ObservableList<Habilidad> habilidades = FXCollections.observableArrayList();
            for (Object lEntity : list) {
                habilidades.add((Habilidad) lEntity);
            }
            defPrueba.cmbHabilidades.setItems(habilidades);
            if(list != null &&  !list.isEmpty())
                defPrueba.cmbHabilidades.getSelectionModel().select(0);
        }
    }

    private static void processNivelEvaluacion(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof NivelEvaluacion) {
            ObservableList<NivelEvaluacion> nivelEvaluacion = FXCollections.observableArrayList();
            for (Object lEntity : list) {
                nivelEvaluacion.add((NivelEvaluacion) lEntity);
            }
            defPrueba.cmbNivelEvaluacion.setItems(nivelEvaluacion);
            if(list != null &&  !list.isEmpty())
                defPrueba.cmbNivelEvaluacion.getSelectionModel().select(0);
        }
    }

    private static void processAsignatura(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof Asignatura) {
            ObservableList<Asignatura> asignaturas = FXCollections.observableArrayList();
            for (Object lEntity : list) {
                asignaturas.add((Asignatura) lEntity);
            }
            defPrueba.cmbAsignatura.setItems(asignaturas);
            if(list != null &&  !list.isEmpty())
                defPrueba.cmbAsignatura.getSelectionModel().select(0);
        }
    }

    private static void processTipoCurso(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof TipoCurso) {
            ObservableList<TipoCurso> cursos = FXCollections.observableArrayList();
            for (Object lEntity : list) {
                cursos.add((TipoCurso) lEntity);
            }
            defPrueba.cmbCurso.setItems(cursos);
            if(list != null &&  !list.isEmpty())
                defPrueba.cmbCurso.getSelectionModel().select(0);
        }
    }

    private static void processProfesor(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof Profesor) {
            ObservableList<Profesor> profesores = FXCollections.observableArrayList();
            for (Object lEntity : list) {
                profesores.add((Profesor) lEntity);
            }
            defPrueba.cmbProfesor.setItems(profesores);
            if(list != null &&  !list.isEmpty())
                defPrueba.cmbProfesor.getSelectionModel().select(0);
        }
    }

    private static void processTipoPrueba(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof TipoPrueba) {
            
            ObservableList<TipoPrueba> tipoPruebas = FXCollections.observableArrayList();
            for (Object lEntity : list) {
                tipoPruebas.add((TipoPrueba) lEntity);
            }
            defPrueba.cmbTipoPrueba.setItems(tipoPruebas);
            if(list != null &&  !list.isEmpty())
                defPrueba.cmbTipoPrueba.getSelectionModel().select(0);
        }
        
    }
    
    
    public static void save()
    {
        
    }
}
