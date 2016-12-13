package cl.eos.persistence.models;

import java.util.List;

import javax.persistence.PreUpdate;

import cl.eos.persistence.AEntity;
import cl.eos.util.Utils;

public class SPruebaRendida extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private SAlumno alumno;
    private String respuestas;
    private Integer buenas;
    private Integer malas;
    private Integer omitidas;
    private Float nota;
    public boolean eliminada;
    private SEvaluacionPrueba evaluacionPrueba;
    private SRangoEvaluacion rango;

    /**
     * Corresponde a la forma asociada a la prueba del alumno.
     */
    private Integer forma;

    public SPruebaRendida() {
        buenas = new Integer(0);
        malas = new Integer(0);
        omitidas = new Integer(0);
        forma = new Integer(1);
        nota = new Float(0);
        respuestas = "";
        alumno = null;
    }

    @PreUpdate
    public void detachFromEvaluacion() {
        if (eliminada) {
            evaluacionPrueba.getPruebasRendidas().remove(this);
            evaluacionPrueba = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SPruebaRendida other = (SPruebaRendida) obj;
        if (alumno == null) {
            if (other.alumno != null)
                return false;
        } else if (!alumno.equals(other.alumno))
            return false;
        if (evaluacionPrueba == null) {
            if (other.evaluacionPrueba != null)
                return false;
        } else if (!evaluacionPrueba.equals(other.evaluacionPrueba))
            return false;
        return true;
    }

    public SAlumno getAlumno() {
        return alumno;
    }

    public Integer getBuenas() {
        return buenas;
    }

    public SColegio getColegio() {
        return alumno.getCurso().getColegio();
    }

    public String getCurso() {
        return alumno.getCurso().getName();
    }

    public SEvaluacionPrueba getEvaluacionPrueba() {
        return evaluacionPrueba;
    }

    public Integer getForma() {
        return forma;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Integer getMalas() {
        return malas;
    }

    public String getMaterno() {
        return alumno.getMaterno();
    }

    @Override
    public String getName() {
        return name;
    }

    public String getNombre() {
        return alumno.getName();
    }

    public Float getNota() {
        return Utils.redondeo2Decimales(nota);
    }

    public SCurso getObjCurso() {
        return alumno.getCurso();
    }

    public STipoCurso getTipoCurso()
    {
        return alumno.getCurso().getTipoCurso();
    }
    
    public Integer getOmitidas() {
        return omitidas;
    }

    public String getPaterno() {
        return alumno.getPaterno();
    }

    public Float getPbuenas() {
        final Float totalRespuestas = (float) (malas + buenas + omitidas);
        final float valor = (float) buenas / totalRespuestas * 100f;
        return Utils.redondeo2Decimales(valor);
    }

    public Float getPpuntaje() {
        final float valor = Utils.getPuntaje(getNota().floatValue()) / Utils.MAX_PUNTAJE * 100f;
        return Utils.redondeo2Decimales(valor);
    }

    public Float getPpuntajes() {
        final float valor = Utils.getPuntaje(getNota().floatValue()) / Utils.MAX_PUNTAJE * 100f;
        return Utils.redondeo2Decimales(valor);

    }

    public Integer getPuntaje() {
        return Utils.getPuntaje(getNota().floatValue());
    }

    public SRangoEvaluacion getRango() {
        return rango;
    }

    public String getRespuestas() {
        return respuestas;
    }

    public String getRut() {
        return alumno.getRut();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (alumno == null ? 0 : alumno.hashCode());
        result = prime * result + (evaluacionPrueba == null ? 0 : evaluacionPrueba.hashCode());
        return result;
    }

    /**
     * Este metodo recalcula la nota y el rango de la prueba rendida. Esto se
     * hace cuando se ha realizado una anulación de alguna pregunta.
     */
    public void reEvaluate() {
        final SPrueba prueba = getEvaluacionPrueba().getPrueba();
        final List<SRespuestasEsperadasPrueba> respuestasEsperadas = prueba.getRespuestas();
        final StringBuilder strResps = new StringBuilder(getRespuestas());

        int buenas = 0;
        int malas = 0;
        int omitidas = 0;
        int anuladas = 0;
        final int nroRespuestas = prueba.getNroPreguntas();
        final int nroLast = Math.abs(strResps.length() - nroRespuestas);
        if (nroLast > 0) {
            for (int n = 0; n < nroLast; n++) {
                strResps.append("O");
            }
        }

        for (int n = 0; n < nroRespuestas; n++) {
            String letter = strResps.substring(n, n + 1);
            final SRespuestasEsperadasPrueba rEsperada = respuestasEsperadas.get(n);

            if (rEsperada.isAnulada()) {
                rEsperada.setRespuesta("*");
                strResps.replace(n, n + 1, "*");
                anuladas++;
                continue;
            }

            if ("O".equalsIgnoreCase(letter)) {
                omitidas++;
            } else if ("M".equalsIgnoreCase(letter)) {
                malas++;
            } else {
                if (rEsperada.getMental()) {
                    if ("B".equalsIgnoreCase(letter)) {
                        strResps.replace(n, n + 1, "+");
                        buenas++;
                    } else if ("D".equalsIgnoreCase(letter)) {
                        strResps.replace(n, n + 1, "-");
                        malas++;
                    } else {
                        malas++;
                    }
                } else if (rEsperada.getVerdaderoFalso()) {
                    if ("B".equalsIgnoreCase(letter)) {
                        strResps.replace(n, n + 1, "V");
                        letter = "V";
                    } else if ("D".equalsIgnoreCase(letter)) {
                        strResps.replace(n, n + 1, "F");
                        letter = "F";
                    }

                    if (rEsperada.getRespuesta().equalsIgnoreCase(letter)) {
                        buenas++;
                    } else {
                        malas++;
                    }
                } else {
                    if (rEsperada.getRespuesta().equalsIgnoreCase(letter)) {
                        buenas++;
                    } else {
                        malas++;
                    }
                }
            }
        }
        final int nroPreguntas = prueba.getNroPreguntas() - anuladas;
        final float nota = Utils.getNota(nroPreguntas, prueba.getExigencia(), buenas, prueba.getPuntajeBase());
        setRespuestas(strResps.toString());
        setBuenas(buenas);
        setMalas(malas);
        setOmitidas(omitidas);
        setNota(nota);
        final float porcentaje = (float) getBuenas() / nroPreguntas * 100f;
        final SRangoEvaluacion rango = prueba.getNivelEvaluacion().getRango(porcentaje);
        setRango(rango);
    }

    public void setAlumno(SAlumno alumno) {
        this.alumno = alumno;
    }

    public void setBuenas(Integer buenas) {
        this.buenas = buenas;
    }

    public void setEvaluacionPrueba(SEvaluacionPrueba evaluacionPrueba) {
        this.evaluacionPrueba = evaluacionPrueba;
    }

    public void setForma(Integer forma) {
        this.forma = forma;
    }

    @Override
    public void setId(Long id) {
        this.id = id;

    }

    public void setMalas(Integer malas) {
        this.malas = malas;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public void setOmitidas(Integer omitidas) {
        this.omitidas = omitidas;
    }

    public void setRango(SRangoEvaluacion rango) {
        this.rango = rango;
    }

    public void setRespuestas(String respuestas) {
        this.respuestas = respuestas;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
