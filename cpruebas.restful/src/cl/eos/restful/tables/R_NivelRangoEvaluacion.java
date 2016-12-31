package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

public class R_NivelRangoEvaluacion {
    @SerializedName(value = "nivelEvaluacionId", alternate = {"NIVELEVALUACIONID"})
    Long nivelEvaluacionId;
    @SerializedName(value = "rangoId", alternate = {"RANGOID"})
    Long rangoId;

    public R_NivelRangoEvaluacion() {
    }

    /**
     * @return the nivelEvaluacionId
     */
    public final Long getNivelEvaluacionId() {
        return nivelEvaluacionId;
    }

    /**
     * @param nivelEvaluacionId the nivelEvaluacionId to set
     */
    public final void setNivelEvaluacionId(Long nivelEvaluacionId) {
        this.nivelEvaluacionId = nivelEvaluacionId;
    }

    /**
     * @return the rangoId
     */
    public final Long getRangoId() {
        return rangoId;
    }

    /**
     * @param rangoId the rangoId to set
     */
    public final void setRangoId(Long rangoId) {
        this.rangoId = rangoId;
    }

    public static class Builder {
        private Long nivelEvaluacionId;
        private Long rangoId;

        public Builder nivelEvaluacionId(Long nivelEvaluacionId) {
            this.nivelEvaluacionId = nivelEvaluacionId;
            return this;
        }

        public Builder rangoId(Long rangoId) {
            this.rangoId = rangoId;
            return this;
        }

        public R_NivelRangoEvaluacion build() {
            R_NivelRangoEvaluacion r_NivelRangoEvaluacion = new R_NivelRangoEvaluacion();
            r_NivelRangoEvaluacion.nivelEvaluacionId = nivelEvaluacionId;
            r_NivelRangoEvaluacion.rangoId = rangoId;
            return r_NivelRangoEvaluacion;
        }
    }
}
