package cl.eos.ot;

public class OTRespuestaPreguntas {

    private String name;
    private Integer buenas;
    private Integer malas;
    private Integer omitidas;

    public Integer getBuenas() {
        return buenas;
    }

    public Integer getMalas() {
        return malas;
    }

    public String getName() {
        return name;
    }

    public Integer getOmitidas() {
        return omitidas;
    }

    public void setBuenas(Integer buenas) {
        this.buenas = buenas;
    }

    public void setMalas(Integer malas) {
        this.malas = malas;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOmitidas(Integer omitidas) {
        this.omitidas = omitidas;
    }

}
