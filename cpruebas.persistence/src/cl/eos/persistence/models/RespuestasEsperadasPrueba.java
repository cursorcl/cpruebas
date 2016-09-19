package cl.eos.persistence.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import cl.eos.persistence.AEntity;

/**
 * Contiene las respuestas esperadas de una prueba
 * 
 * @author curso_000
 */
@Entity(name = "respuestasesperadasprueba")
@Cache(type = CacheType.NONE, size = 64000, // Use 64,000 as the initial cache size.
        expiry = 360000, // 6 minutes
        coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS // if cache coordination is used, only send invalidation messages.
)
@NamedQueries({
        @NamedQuery(name = "RespuestasEsperadasPrueba.findAll", query = "SELECT e FROM respuestasesperadasprueba e"),
        @NamedQuery(name = "RespuestasEsperadasPrueba.findByPrueba", query = "SELECT e FROM respuestasesperadasprueba e WHERE e.prueba.id = :pruebaId order by e.numero"),
        @NamedQuery(name = "RespuestasEsperadasPrueba.deleteByPrueba", query = "DELETE FROM respuestasesperadasprueba e WHERE e.prueba.id = :pruebaId") })
public class RespuestasEsperadasPrueba extends AEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Prueba prueba;

    private Integer numero;
    private String respuesta = "N";
    private Boolean verdaderoFalso = Boolean.FALSE;
    private Boolean mental = Boolean.FALSE;
    private Habilidad habilidad;
    private EjeTematico ejeTematico;
    private Objetivo objetivo;
    private Boolean anulada = Boolean.FALSE;

    @OneToMany(mappedBy = "respuesta", cascade = CascadeType.ALL)
    private List<Imagenes> imagenes;

    @OneToMany(mappedBy = "respuesta", cascade = CascadeType.ALL)
    private List<Alternativas> alternativas;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    /**
     * Se crea para el manejo de multiusuarios
     */
    @Version
    protected int version;

    public final int getVersion() {
        return version;
    }

    public final void setVersion(int version) {
        this.version = version;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean validate() {
        return false;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Boolean getVerdaderoFalso() {
        return verdaderoFalso;
    }

    public void setVerdaderoFalso(Boolean verdaderoFalso) {
        this.verdaderoFalso = verdaderoFalso;
    }

    public Boolean getMental() {
        return mental;
    }

    public void setMental(Boolean mental) {
        this.mental = mental;
    }

    public Habilidad getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }

    public EjeTematico getEjeTematico() {
        return ejeTematico;
    }

    public void setEjeTematico(EjeTematico ejeTematico) {
        this.ejeTematico = ejeTematico;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public Boolean isAnulada() {
        return anulada;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
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

    public List<Imagenes> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagenes> imagenes) {
        this.imagenes = imagenes;
    }

    public List<Alternativas> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativas> alternativas) {
        this.alternativas = alternativas;
    }

    public static class Builder {
        private Prueba prueba;
        private Integer numero;
        private String respuesta;
        private Boolean verdaderoFalso;
        private Boolean mental;
        private Habilidad habilidad;
        private EjeTematico ejeTematico;
        private Objetivo objetivo;
        private Boolean anulada;
        private List<Imagenes> imagenes;
        private List<Alternativas> alternativas;
        private Long id;
        private String name;
        private int version;

        public Builder prueba(Prueba prueba) {
            this.prueba = prueba;
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

        public Builder habilidad(Habilidad habilidad) {
            this.habilidad = habilidad;
            return this;
        }

        public Builder ejeTematico(EjeTematico ejeTematico) {
            this.ejeTematico = ejeTematico;
            return this;
        }

        public Builder objetivo(Objetivo objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public Builder anulada(Boolean anulada) {
            this.anulada = anulada;
            return this;
        }

        public Builder imagenes(List<Imagenes> imagenes) {
            this.imagenes = imagenes;
            return this;
        }

        public Builder alternativas(List<Alternativas> alternativas) {
            this.alternativas = alternativas;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(int version) {
            this.version = version;
            return this;
        }

        public RespuestasEsperadasPrueba build() {
            RespuestasEsperadasPrueba respuestasEsperadasPrueba = new RespuestasEsperadasPrueba();
            respuestasEsperadasPrueba.prueba = prueba;
            respuestasEsperadasPrueba.numero = numero;
            respuestasEsperadasPrueba.respuesta = respuesta;
            respuestasEsperadasPrueba.verdaderoFalso = verdaderoFalso;
            respuestasEsperadasPrueba.mental = mental;
            respuestasEsperadasPrueba.habilidad = habilidad;
            respuestasEsperadasPrueba.ejeTematico = ejeTematico;
            respuestasEsperadasPrueba.objetivo = objetivo;
            respuestasEsperadasPrueba.anulada = anulada;
            respuestasEsperadasPrueba.imagenes = imagenes;
            respuestasEsperadasPrueba.alternativas = alternativas;
            respuestasEsperadasPrueba.id = id;
            respuestasEsperadasPrueba.name = name;
            respuestasEsperadasPrueba.version = version;
            return respuestasEsperadasPrueba;
        }
    }
}
