package cl.eos.view;

import java.util.List;

import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SHabilidad;
import cl.eos.persistence.models.SNivelEvaluacion;
import cl.eos.persistence.models.SObjetivo;
import cl.eos.persistence.models.SProfesor;
import cl.eos.persistence.models.STipoCurso;
import cl.eos.persistence.models.STipoPrueba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataProcessor {

    public static void process(List<Object> list, DefinirPrueba defPrueba) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            // if (entity instanceof SPrueba) {
            // ObservableList<OTPrueba> pruebas =
            // FXCollections.observableArrayList();
            // for (Object lEntity : list) {
            // pruebas.add(new OTPrueba((SPrueba) lEntity));
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
        if (entity instanceof SAsignatura) {
            final ObservableList<SAsignatura> asignaturas = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                asignaturas.add((SAsignatura) lEntity);
            }
            defPrueba.cmbAsignatura.setItems(asignaturas);
            if (list != null && !list.isEmpty())
                defPrueba.cmbAsignatura.getSelectionModel().select(0);
        }
    }

    private static void processEjeTematico(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof SEjeTematico) {
            final ObservableList<SEjeTematico> ejesTematicos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                ejesTematicos.add((SEjeTematico) lEntity);
            }
            defPrueba.cmbEjesTematicos.setItems(ejesTematicos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbEjesTematicos.getSelectionModel().select(0);
        }
    }

    private static void processHabilidad(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof SHabilidad) {
            final ObservableList<SHabilidad> habilidades = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                habilidades.add((SHabilidad) lEntity);
            }
            defPrueba.cmbHabilidades.setItems(habilidades);
            if (list != null && !list.isEmpty())
                defPrueba.cmbHabilidades.getSelectionModel().select(0);
        }
    }

    private static void processNivelEvaluacion(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof SNivelEvaluacion) {
            final ObservableList<SNivelEvaluacion> nivelEvaluacion = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                nivelEvaluacion.add((SNivelEvaluacion) lEntity);
            }
            defPrueba.cmbNivelEvaluacion.setItems(nivelEvaluacion);
            if (list != null && !list.isEmpty())
                defPrueba.cmbNivelEvaluacion.getSelectionModel().select(0);
        }
    }

    private static void processObjetivo(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof SObjetivo) {
            final ObservableList<SObjetivo> objetivos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                objetivos.add((SObjetivo) lEntity);
            }
            defPrueba.cmbObjetivos.setItems(objetivos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbObjetivos.getSelectionModel().select(0);
        }
    }

    private static void processProfesor(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof SProfesor) {
            final ObservableList<SProfesor> profesores = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                profesores.add((SProfesor) lEntity);
            }
            defPrueba.cmbProfesor.setItems(profesores);
            if (list != null && !list.isEmpty())
                defPrueba.cmbProfesor.getSelectionModel().select(0);
        }
    }

    private static void processTipoCurso(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof STipoCurso) {
            final ObservableList<STipoCurso> cursos = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                cursos.add((STipoCurso) lEntity);
            }
            defPrueba.cmbCurso.setItems(cursos);
            if (list != null && !list.isEmpty())
                defPrueba.cmbCurso.getSelectionModel().select(0);
        }
    }

    private static void processTipoPrueba(Object entity, DefinirPrueba defPrueba, List<Object> list) {
        if (entity instanceof STipoPrueba) {

            final ObservableList<STipoPrueba> tipoPruebas = FXCollections.observableArrayList();
            for (final Object lEntity : list) {
                tipoPruebas.add((STipoPrueba) lEntity);
            }
            defPrueba.cmbTipoPrueba.setItems(tipoPruebas);
            if (list != null && !list.isEmpty())
                defPrueba.cmbTipoPrueba.getSelectionModel().select(0);
        }

    }

    public static void save() {

    }
}
