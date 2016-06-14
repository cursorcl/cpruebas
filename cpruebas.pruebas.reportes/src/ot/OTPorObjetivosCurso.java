package ot;

import cl.eos.persistence.models.Objetivo;
import javafx.collections.ObservableList;

/**
 * Clase que da soporte al reporte por objetivos de un solo curso.
 * 
 * @author curso
 */
public class OTPorObjetivosCurso {
    private ObservableList<Objetivo> objetivos;
    private ObservableList<String> preguntasAsociadas;
    private ObservableList<String> ejesAsociados;
    private ObservableList<String> habilidadesAsociadas;
    private ObservableList<Float> porcentajes;

    public ObservableList<Objetivo> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(ObservableList<Objetivo> objetivos) {
        this.objetivos = objetivos;
    }

    public ObservableList<String> getPreguntasAsociadas() {
        return preguntasAsociadas;
    }

    public void setPreguntasAsociadas(ObservableList<String> preguntasAsociadas) {
        this.preguntasAsociadas = preguntasAsociadas;
    }

    public ObservableList<String> getEjesAsociados() {
        return ejesAsociados;
    }

    public void setEjesAsociados(ObservableList<String> ejesAsociados) {
        this.ejesAsociados = ejesAsociados;
    }

    public ObservableList<String> getHabilidadesAsociadas() {
        return habilidadesAsociadas;
    }

    public void setHabilidadesAsociadas(ObservableList<String> habilidadesAsociadas) {
        this.habilidadesAsociadas = habilidadesAsociadas;
    }

    public ObservableList<Float> getPorcentajes() {
        return porcentajes;
    }

    public void setPorcentajes(ObservableList<Float> porcentajes) {
        this.porcentajes = porcentajes;
    }

}
