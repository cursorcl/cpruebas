package cl.eos.detection;

import com.google.common.base.Splitter;

public class OTResultadoScanner {

    private String rut;
    private Integer forma;
    private String respuestas;

    public OTResultadoScanner() {
    }

    public OTResultadoScanner(String rut, Integer forma, String respuestas) {
        super();
        this.rut = rut;
        this.forma = forma;
        this.respuestas = respuestas;
    }

    public Integer getForma() {
        return forma;
    }

    public String getRespuestas() {
        return respuestas;
    }

    public String getRut() {
        return rut;
    }

    public void setForma(Integer forma) {
        this.forma = forma;
    }

    public void setRespuestas(String respuestas) {
        this.respuestas = respuestas;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    @Override
    public String toString() {

        return "OTResultadoScanner [rut=" + rut + ", forma=" + forma + ", respuestas="
                + Splitter.fixedLength(5).split(respuestas) + "]";
    }

}
