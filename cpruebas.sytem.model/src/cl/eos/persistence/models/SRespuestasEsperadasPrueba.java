package cl.eos.persistence.models;

import java.util.List;

import cl.eos.persistence.AEntity;

/**
 * Contiene las respuestas esperadas de una prueba
 *
 * @author curso_000
 */
public class SRespuestasEsperadasPrueba extends AEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    private Integer numero;
    private String respuesta = "N";
    private Boolean verdaderoFalso = Boolean.FALSE;
    private Boolean mental = Boolean.FALSE;
    private Boolean anulada = Boolean.FALSE;

    private SHabilidad habilidad;
    private SEjeTematico ejeTematico;
    private SObjetivo objetivo;
    private SPrueba prueba;
    private List<SImagenes> imagenes;
    private List<SAlternativas> alternativas;

    public List<SAlternativas> getAlternativas() {
        return alternativas;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public SEjeTematico getEjeTematico() {
        return ejeTematico;
    }

    public SHabilidad getHabilidad() {
        return habilidad;
    }

    @Override
    public Long getId() {
        return id;
    }

    public List<SImagenes> getImagenes() {
        return imagenes;
    }

    public Boolean getMental() {
        return mental;
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getNumero() {
        return numero;
    }

    public SObjetivo getObjetivo() {
        return objetivo;
    }

    public SPrueba getPrueba() {
        return prueba;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public Boolean getVerdaderoFalso() {
        return verdaderoFalso;
    }

    public Boolean isAnulada() {
        return anulada;
    }

    public void setAlternativas(List<SAlternativas> alternativas) {
        this.alternativas = alternativas;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    public void setEjeTematico(SEjeTematico ejeTematico) {
        this.ejeTematico = ejeTematico;
    }

    public void setHabilidad(SHabilidad habilidad) {
        this.habilidad = habilidad;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setImagenes(List<SImagenes> imagenes) {
        this.imagenes = imagenes;
    }

    public void setMental(Boolean mental) {
        this.mental = mental;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setObjetivo(SObjetivo objetivo) {
        this.objetivo = objetivo;
    }

    public void setPrueba(SPrueba prueba) {
        this.prueba = prueba;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public void setVerdaderoFalso(Boolean verdaderoFalso) {
        this.verdaderoFalso = verdaderoFalso;
    }

    @Override
    public boolean validate() {
        return false;
    }

    /**
     * Validará si value es un valore correcto para la respuesta.
     *
     * @param value
     * @return
     */
    public boolean validate(String value) {
        boolean result = false;
        if (!isAnulada()) {
            if ("+".equals(value) || getRespuesta().equalsIgnoreCase(value)) {
                result = true;
            }
        }
        return result;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Integer numero;
        private String respuesta;
        private Boolean verdaderoFalso;
        private Boolean mental;
        private Boolean anulada;
        private SHabilidad habilidad;
        private SEjeTematico ejeTematico;
        private SObjetivo objetivo;
        private SPrueba prueba;
        private List<SImagenes> imagenes;
        private List<SAlternativas> alternativas;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder numero(Integer numero) {
            this.numero = numero;
            return this;
        }

        public Builder respuesta(String respuesta) {
            this.respuesta = respuesta;
            return this;
        }

        public Builder verdaderoFalso(Boolean verdaderoFalso) {
            this.verdaderoFalso = verdaderoFalso;
            return this;
        }

        public Builder mental(Boolean mental) {
            this.mental = mental;
            return this;
        }

        public Builder anulada(Boolean anulada) {
            this.anulada = anulada;
            return this;
        }

        public Builder habilidad(SHabilidad habilidad) {
            this.habilidad = habilidad;
            return this;
        }

        public Builder ejeTematico(SEjeTematico ejeTematico) {
            this.ejeTematico = ejeTematico;
            return this;
        }

        public Builder objetivo(SObjetivo objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public Builder prueba(SPrueba prueba) {
            this.prueba = prueba;
            return this;
        }

        public Builder imagenes(List<SImagenes> imagenes) {
            this.imagenes = imagenes;
            return this;
        }

        public Builder alternativas(List<SAlternativas> alternativas) {
            this.alternativas = alternativas;
            return this;
        }

        public SRespuestasEsperadasPrueba build() {
            SRespuestasEsperadasPrueba sRespuestasEsperadasPrueba = new SRespuestasEsperadasPrueba();
            sRespuestasEsperadasPrueba.id = id;
            sRespuestasEsperadasPrueba.name = name;
            sRespuestasEsperadasPrueba.numero = numero;
            sRespuestasEsperadasPrueba.respuesta = respuesta;
            sRespuestasEsperadasPrueba.verdaderoFalso = verdaderoFalso;
            sRespuestasEsperadasPrueba.mental = mental;
            sRespuestasEsperadasPrueba.anulada = anulada;
            sRespuestasEsperadasPrueba.habilidad = habilidad;
            sRespuestasEsperadasPrueba.ejeTematico = ejeTematico;
            sRespuestasEsperadasPrueba.objetivo = objetivo;
            sRespuestasEsperadasPrueba.prueba = prueba;
            sRespuestasEsperadasPrueba.imagenes = imagenes;
            sRespuestasEsperadasPrueba.alternativas = alternativas;
            return sRespuestasEsperadasPrueba;
        }
    }
}
