package cl.eos.ot;

import cl.eos.util.Utils;

public class OTRespuestasPorcentaje {

    private String titulo;
    private Float porcentaje;

    public Float getPorcentaje() {
        return Utils.redondeo2Decimales(porcentaje);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
