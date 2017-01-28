package cl.eos.restful.tables;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;
import cl.eos.restful.EntityUtils;
import cl.eos.util.Utils;

public class R_PruebaRendida extends AEntity {
    private static final long serialVersionUID = 1L;
    @SerializedName(value = "id", alternate = { "ID" }) Long id;
    @SerializedName(value = "omitidas", alternate = { "OMITIDAS" }) Integer omitidas;
    @SerializedName(value = "nota", alternate = { "NOTA" }) float nota;
    @SerializedName(value = "respuestas", alternate = { "RESPUESTAS" }) String respuestas;
    @SerializedName(value = "forma", alternate = { "FORMA" }) Integer forma;
    @SerializedName(value = "malas", alternate = { "MALAS" }) Integer malas;
    @SerializedName(value = "buenas", alternate = { "BUENAS" }) Integer buenas;
    @SerializedName(value = "name", alternate = { "NAME" }) String name;
    @SerializedName(value = "rango_id", alternate = { "RANGO_ID" }) Long rango_id;
    @SerializedName(value = "alumno_id", alternate = { "ALUMNO_ID" }) Long alumno_id;
    @SerializedName(value = "tipoalumno_id", alternate = { "TIPOALUMNO_ID" }) Long tipoalumno_id;
    @SerializedName(value = "evaluacionprueba_id", alternate = { "EVALUACIONPRUEBA_ID" }) Long evaluacionprueba_id;
    /**
     * @return the id
     */
    public final Long getId() {
        return id;
    }
    /**
     * @param id
     *            the id to set
     */
    public final void setId(Long id) {
        this.id = id;
    }
    /**
     * @return the omitidas
     */
    public final Integer getOmitidas() {
        return omitidas;
    }
    /**
     * @param omitidas
     *            the omitidas to set
     */
    public final void setOmitidas(Integer omitidas) {
        this.omitidas = omitidas;
    }
    /**
     * @return the nota
     */
    public final float getNota() {
        return nota;
    }
    /**
     * @param nota
     *            the nota to set
     */
    public final void setNota(float nota) {
        this.nota = nota;
    }
    /**
     * @return the respuestas
     */
    public final String getRespuestas() {
        return respuestas;
    }
    /**
     * @param respuestas
     *            the respuestas to set
     */
    public final void setRespuestas(String respuestas) {
        this.respuestas = respuestas;
    }
    /**
     * @return the forma
     */
    public final Integer getForma() {
        return forma;
    }
    /**
     * @param forma
     *            the forma to set
     */
    public final void setForma(Integer forma) {
        this.forma = forma;
    }
    /**
     * @return the malas
     */
    public final Integer getMalas() {
        return malas;
    }
    /**
     * @param malas
     *            the malas to set
     */
    public final void setMalas(Integer malas) {
        this.malas = malas;
    }
    /**
     * @return the buenas
     */
    public final Integer getBuenas() {
        return buenas;
    }
    /**
     * @param buenas
     *            the buenas to set
     */
    public final void setBuenas(Integer buenas) {
        this.buenas = buenas;
    }
    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }
    /**
     * @param name
     *            the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }
    /**
     * @return the rango_id
     */
    public final Long getRango_id() {
        return rango_id;
    }
    /**
     * @param rango_id
     *            the rango_id to set
     */
    public final void setRango_id(Long rango_id) {
        this.rango_id = rango_id;
    }
    /**
     * @return the alumno_id
     */
    public final Long getAlumno_id() {
        return alumno_id;
    }
    /**
     * @param alumno_id
     *            the alumno_id to set
     */
    public final void setAlumno_id(Long alumno_id) {
        this.alumno_id = alumno_id;
    }
    /**
     * @return the evaluacionprueba_id
     */
    public final Long getEvaluacionprueba_id() {
        return evaluacionprueba_id;
    }
    /**
     * @param evaluacionprueba_id
     *            the evaluacionprueba_id to set
     */
    public final void setEvaluacionprueba_id(Long evaluacionprueba_id) {
        this.evaluacionprueba_id = evaluacionprueba_id;
    }
    /**
     * @return the tipoalumno_id
     */
    public final Long getTipoalumno_id() {
        return tipoalumno_id;
    }
    /**
     * @param tipoalumno_id
     *            the tipoalumno_id to set
     */
    public final void setTipoalumno_id(Long tipoalumno_id) {
        this.tipoalumno_id = tipoalumno_id;
    }
    
    
    public Float getPbuenas() {
      Float totalRespuestas = (float) (this.malas + this.buenas + this.omitidas);
      float valor = ((float) this.buenas) / totalRespuestas * 100f;
      return Utils.redondeo2Decimales(valor);
    }

    public Integer getPuntaje() {
      return (Integer) Utils.getPuntaje(nota);
    }

    public Float getPpuntaje() {
      float valor = ((float) (Utils.getPuntaje(nota) / Utils.MAX_PUNTAJE)) * 100f;
      return Utils.redondeo2Decimales(valor);
    }

    
    /**
     * Este metodo recalcula la nota y el rango de la prueba rendida. Esto se hace cuando se ha realizado una anulación
     * de alguna pregunta.
     */
    public void reEvaluate(R_Prueba prueba, List<R_RespuestasEsperadasPrueba> respuestasEsperadas, List<R_RangoEvaluacion> rangos) {
        StringBuilder strResps = new StringBuilder(this.getRespuestas());
        int buenas = 0;
        int malas = 0;
        int omitidas = 0;
        int anuladas = 0;
        int nroRespuestas = prueba.getNropreguntas();
        int nroLast = Math.abs(strResps.length() - nroRespuestas);
        if (nroLast > 0) {
            for (int n = 0; n < nroLast; n++) {
                strResps.append("O");
            }
        }
        for (int n = 0; n < nroRespuestas; n++) {
            String letter = strResps.substring(n, n + 1);
            R_RespuestasEsperadasPrueba rEsperada = respuestasEsperadas.get(n);
            if (rEsperada.getAnulada()) {
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
                } else if (rEsperada.getVerdaderofalso()) {
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
        int nroPreguntas = prueba.getNropreguntas() - anuladas;
        float nota = Utils.getNota(nroPreguntas, prueba.getExigencia(), buenas, prueba.getPuntajebase());
        this.setRespuestas(strResps.toString());
        this.setBuenas(buenas);
        this.setMalas(malas);
        this.setOmitidas(omitidas);
        this.setNota(nota);
        float porcentaje = ((float) this.getBuenas()) / nroPreguntas * 100f;
        R_RangoEvaluacion rango = EntityUtils.getRango(porcentaje, rangos);
        this.setRango_id(rango.getId());
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alumno_id == null) ? 0 : alumno_id.hashCode());
        result = prime * result + ((evaluacionprueba_id == null) ? 0 : evaluacionprueba_id.hashCode());
        return result;
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        R_PruebaRendida other = (R_PruebaRendida) obj;
        if (alumno_id == null) {
            if (other.alumno_id != null) return false;
        } else if (!alumno_id.equals(other.alumno_id)) return false;
        if (evaluacionprueba_id == null) {
            if (other.evaluacionprueba_id != null) return false;
        } else if (!evaluacionprueba_id.equals(other.evaluacionprueba_id)) return false;
        return true;
    }
    
    public static class Builder {
        private Long id;
        private Integer omitidas;
        private float nota;
        private String respuestas;
        private Integer forma;
        private Integer malas;
        private Integer buenas;
        private String name;
        private Long rango_id;
        private Long alumno_id;
        private Long tipoalumno_id;
        private Long evaluacionprueba_id;
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder omitidas(Integer omitidas) {
            this.omitidas = omitidas;
            return this;
        }
        public Builder nota(float nota) {
            this.nota = nota;
            return this;
        }
        public Builder respuestas(String respuestas) {
            this.respuestas = respuestas;
            return this;
        }
        public Builder forma(Integer forma) {
            this.forma = forma;
            return this;
        }
        public Builder malas(Integer malas) {
            this.malas = malas;
            return this;
        }
        public Builder buenas(Integer buenas) {
            this.buenas = buenas;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder rango_id(Long rango_id) {
            this.rango_id = rango_id;
            return this;
        }
        public Builder alumno_id(Long alumno_id) {
            this.alumno_id = alumno_id;
            return this;
        }
        public Builder tipoalumno_id(Long tipoalumno_id) {
            this.tipoalumno_id = tipoalumno_id;
            return this;
        }
        public Builder evaluacionprueba_id(Long evaluacionprueba_id) {
            this.evaluacionprueba_id = evaluacionprueba_id;
            return this;
        }
        public R_PruebaRendida build() {
            return new R_PruebaRendida(this);
        }
    }
    private R_PruebaRendida(Builder builder) {
        this.id = builder.id;
        this.omitidas = builder.omitidas;
        this.nota = builder.nota;
        this.respuestas = builder.respuestas;
        this.forma = builder.forma;
        this.malas = builder.malas;
        this.buenas = builder.buenas;
        this.name = builder.name;
        this.rango_id = builder.rango_id;
        this.alumno_id = builder.alumno_id;
        this.tipoalumno_id = builder.tipoalumno_id;
        this.evaluacionprueba_id = builder.evaluacionprueba_id;
    }
}
