package cl.eos.ot;

import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.util.Utils;

public class OTPreguntasEjes {

    private R_Ejetematico ejeTematico;
    private Integer buenas = 0;
    private Integer total = 0;

    public OTPreguntasEjes() {

    }

    public OTPreguntasEjes(R_Ejetematico ejeTematico) {
        this.ejeTematico = ejeTematico;
    }

    public String getName()
    {
      return this.ejeTematico == null ? "" :  this.ejeTematico.getName();
    }
    
    public Integer getBuenas() {
        return buenas;
    }

    public String getDescripcion() {
        return "Respuestas correctas: " + buenas.toString() + " de " + total.toString();
    }

    public R_Ejetematico getEjeTematico() {
        return ejeTematico;
    }

    public Float getLogrado() {
        return (float) buenas / (float) total * 100f;
    }


    public Float getNologrado() {
        final Float valor = (float) total - (float) buenas;
        return (float) (valor / (float) total * 100f);
    }

    public Float getSlogrado() {
        final float valor = (float) buenas / (float) total * 100f;
        return Utils.redondeo2Decimales(valor);
    }

    public Float getSnlogrado() {
        final Float valor = (float) total - (float) buenas;
        final float pvalor = valor / (float) total * 100f;
        return Utils.redondeo2Decimales(pvalor);
    }

    public Integer getTotal() {
        return total;
    }

    public void setBuenas(Integer buenas) {
        this.buenas = buenas;
    }

    public void setEjeTematico(R_Ejetematico ejeTematico) {
      this.ejeTematico = ejeTematico;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
