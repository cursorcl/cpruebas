package cl.eos.ot;

public class OTResumenGeneral {

    // NumberFormat formatter = null;
    private String name;
    private Float nota;
    private Float pbuenas;
    private Float ppuntaje;
    private Integer puntaje;

    public OTResumenGeneral(String name, Float nota, Float pBuenas, Float pPuntaje, Integer puntaje) {
        super();
        this.name = name;
        this.nota = nota;
        pbuenas = pBuenas;
        ppuntaje = pPuntaje;
        this.puntaje = puntaje;
        // formatter = new DecimalFormat("#0" + sep + "00");
    }

    public String getName() {
        return name;
    }

    public Float getNota() {
        return nota;
    }

    public float getPbuenas() {
        // return formatter.format(pbuenas * 100f);
        return pbuenas;
    }

    public float getPpuntaje() {
        // return formatter.format(ppuntaje*100f);
        return ppuntaje;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public void setPbuenas(Float pBuenas) {
        pbuenas = pBuenas;
    }

    public void setPpuntaje(Float pPuntaje) {
        ppuntaje = pPuntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

}
