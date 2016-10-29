package cl.eos.ot;

import cl.eos.util.Utils;

public class OTResultado {
    private String nombre;
    private double logrado;

    public Double getLogrado() {
        return Utils.redondeo2Decimales(logrado);
    }

    public String getNombre() {
        return nombre;
    }

    public void setLogrado(double logrado) {
        this.logrado = logrado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
