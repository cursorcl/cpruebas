package cl.eos.view.ots;

public class OTRespuestasPrueba {

    private Integer nroPregunta;
    private String respuesta;
    private Boolean verdaderoFalso;
    private Boolean Calculo;

    public OTRespuestasPrueba() {
    }

    public Boolean getCalculo() {
        return Calculo;
    }

    public Integer getNroPregunta() {
        return nroPregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public Boolean getVerdaderoFalso() {
        return verdaderoFalso;
    }

    public void setCalculo(Boolean calculo) {
        Calculo = calculo;
    }

    public void setNroPregunta(Integer nroPregunta) {
        this.nroPregunta = nroPregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public void setVerdaderoFalso(Boolean verdaderoFalso) {
        this.verdaderoFalso = verdaderoFalso;
    }

}
