package cl.eos.ot;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cl.eos.persistence.models.Habilidad;

public class OTPreguntasHabilidad {

    NumberFormat formatter = new DecimalFormat("#0.00");
    private Habilidad habilidad;
    private Integer buenas = 0;
    private Integer total = 0;

    public OTPreguntasHabilidad() {

    }

    public OTPreguntasHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }

    public Integer getBuenas() {
        return buenas;
    }

    public String getDescripcion() {
        return "Respuestas correctas: " + buenas.toString() + " de " + total.toString();
    }

    public Habilidad getHabilidad() {
        return habilidad;
    }

    public Float getLogrado() {
        final float valor = total == 0 ? 0 : (float) buenas / (float) total * 100f;
        return valor;
    }

    public String getName() {
        return habilidad.getName();
    }

    public Float getNologrado() {
        final Float valor = total == 0 ? 0 : (float) total - (float) buenas;
        final float pvalor = total == 0 ? 0 : (float) (valor / (float) total * 100f);
        return pvalor;
    }

    public String getSlogrado() {
        final float valor = total == 0 ? 0 : (float) buenas / (float) total * 100f;
        return formatter.format(valor);
    }

    public String getSnlogrado() {
        final Float valor = total == 0 ? 0 : (float) total - (float) buenas;
        final float pvalor = total == 0 ? 0 : (float) (valor / (float) total * 100f);
        return formatter.format(pvalor);
    }

    public Integer getTotal() {
        return total;
    }

    public void setBuenas(Integer buenas) {
        this.buenas = buenas;
    }

    public void setHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
