package cl.eos.view;

import java.util.List;

import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.restful.tables.R_TipoPrueba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataProcessor {

    public static void process(List<Object> list, DefinirPrueba defPrueba) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
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
        if (entity instanceof R_Asignatura) {
            final ObservableList<R_Asignatura> asignaturas = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                asignaturas.add((R_Asignatura) lEntity);
            }
            defPrueba.cmbAsignatura.setItems(asignaturas);
            if (list != null && !list.isEmpty())
                defPrueba.cmbAsignatura.getSelectionModel().select(0);
        }
    }

    private static void processEjeTematico(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof R_Ejetematico) {
            final ObservableList<R_Ejetematico> ejesTematicos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                ejesTematicos.add((R_Ejetematico) lEntity);
            }
            defPrueba.cmbEjesTematicos.setItems(ejesTematicos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbEjesTematicos.getSelectionModel().select(0);
        }
    }

    private static void processHabilidad(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof R_Habilidad) {
            final ObservableList<R_Habilidad> habilidades = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                habilidades.add((R_Habilidad) lEntity);
            }
            defPrueba.cmbHabilidades.setItems(habilidades);
            if (list != null && !list.isEmpty())
                defPrueba.cmbHabilidades.getSelectionModel().select(0);
        }
    }

    private static void processNivelEvaluacion(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof R_NivelEvaluacion) {
            final ObservableList<R_NivelEvaluacion> nivelEvaluacion = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                nivelEvaluacion.add((R_NivelEvaluacion) lEntity);
            }
            defPrueba.cmbNivelEvaluacion.setItems(nivelEvaluacion);
            if (list != null && !list.isEmpty())
                defPrueba.cmbNivelEvaluacion.getSelectionModel().select(0);
        }
    }

    private static void processObjetivo(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof R_Objetivo) {
            final ObservableList<R_Objetivo> objetivos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                objetivos.add((R_Objetivo) lEntity);
            }
            defPrueba.cmbObjetivos.setItems(objetivos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbObjetivos.getSelectionModel().select(0);
        }
    }

    private static void processProfesor(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof R_Profesor) {
            final ObservableList<R_Profesor> profesores = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                profesores.add((R_Profesor) lEntity);
            }
            defPrueba.cmbProfesor.setItems(profesores);
            if (list != null && !list.isEmpty())
                defPrueba.cmbProfesor.getSelectionModel().select(0);
        }
    }

    private static void processTipoCurso(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof R_TipoCurso) {
            final ObservableList<R_TipoCurso> cursos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                cursos.add((R_TipoCurso) lEntity);
            }
            defPrueba.cmbCurso.setItems(cursos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbCurso.getSelectionModel().select(0);
        }
    }

    private static void processTipoPrueba(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof R_TipoPrueba) {

            final ObservableList<R_TipoPrueba> tipoPruebas = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                tipoPruebas.add((R_TipoPrueba) lEntity);
            }
            defPrueba.cmbTipoPrueba.setItems(tipoPruebas);
            if (list != null && !list.isEmpty())
                defPrueba.cmbTipoPrueba.getSelectionModel().select(0);
        }

    }

    public static void save() {

    }
}
