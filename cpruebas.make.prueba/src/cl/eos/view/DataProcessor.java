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

    public static void process(List<Object> list, DefinirPrueba defPrueba) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            // if (entity instanceof Prueba) {
            // ObservableList<OTPrueba> pruebas =
            // FXCollections.observableArrayList();
            // for (Object lEntity : list) {
            // pruebas.add(new OTPrueba((Prueba) lEntity));
            // }
            // tblListadoPruebas.setItems(pruebas);
            // }
            DataProcessor.processTipoPrueba(entity, defPrueba, list);
            DataProcessor.processProfesor(entity, defPrueba, list);
            DataProcessor.processTipoCurso(entity, defPrueba, list);
            DataProcessor.processAsignatura(entity, defPrueba, list);
            DataProcessor.processNivelEvaluacion(entity, defPrueba, list);
            DataProcessor.processHabilidad(entity, defPrueba, list);
            DataProcessor.processEjeTematico(entity, defPrueba, list);
            DataProcessor.processObjetivo(entity, defPrueba, list);
        }
    }

    private static void processAsignatura(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof Asignatura) {
            final ObservableList<Asignatura> asignaturas = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                asignaturas.add((Asignatura) lEntity);
            }
            defPrueba.cmbAsignatura.setItems(asignaturas);
            if (list != null && !list.isEmpty())
                defPrueba.cmbAsignatura.getSelectionModel().select(0);
        }
    }

    private static void processEjeTematico(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof EjeTematico) {
            final ObservableList<EjeTematico> ejesTematicos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                ejesTematicos.add((EjeTematico) lEntity);
            }
            defPrueba.cmbEjesTematicos.setItems(ejesTematicos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbEjesTematicos.getSelectionModel().select(0);
        }
    }

    private static void processHabilidad(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof Habilidad) {
            final ObservableList<Habilidad> habilidades = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                habilidades.add((Habilidad) lEntity);
            }
            defPrueba.cmbHabilidades.setItems(habilidades);
            if (list != null && !list.isEmpty())
                defPrueba.cmbHabilidades.getSelectionModel().select(0);
        }
    }

    private static void processNivelEvaluacion(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof NivelEvaluacion) {
            final ObservableList<NivelEvaluacion> nivelEvaluacion = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                nivelEvaluacion.add((NivelEvaluacion) lEntity);
            }
            defPrueba.cmbNivelEvaluacion.setItems(nivelEvaluacion);
            if (list != null && !list.isEmpty())
                defPrueba.cmbNivelEvaluacion.getSelectionModel().select(0);
        }
    }

    private static void processObjetivo(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof Objetivo) {
            final ObservableList<Objetivo> objetivos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                objetivos.add((Objetivo) lEntity);
            }
            defPrueba.cmbObjetivos.setItems(objetivos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbObjetivos.getSelectionModel().select(0);
        }
    }

    private static void processProfesor(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof Profesor) {
            final ObservableList<Profesor> profesores = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                profesores.add((Profesor) lEntity);
            }
            defPrueba.cmbProfesor.setItems(profesores);
            if (list != null && !list.isEmpty())
                defPrueba.cmbProfesor.getSelectionModel().select(0);
        }
    }

    private static void processTipoCurso(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof TipoCurso) {
            final ObservableList<TipoCurso> cursos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                cursos.add((TipoCurso) lEntity);
            }
            defPrueba.cmbCurso.setItems(cursos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbCurso.getSelectionModel().select(0);
        }
    }

    private static void processTipoPrueba(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof TipoPrueba) {

            final ObservableList<TipoPrueba> tipoPruebas = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                tipoPruebas.add((TipoPrueba) lEntity);
            }
            defPrueba.cmbTipoPrueba.setItems(tipoPruebas);
            if (list != null && !list.isEmpty())
                defPrueba.cmbTipoPrueba.getSelectionModel().select(0);
        }

    }

    public static void save() {

    }
}
