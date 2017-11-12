package cl.eos.persistence.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "preguntas")
@NamedQueries({
        @NamedQuery(name = "Preguntas.findAll", query = "SELECT a FROM alternativas a order by a.respuesta.prueba.id, a.numero"),
        @NamedQuery(name = "Preguntas.findByPrueba", query = "SELECT a FROM alternativas a where a.respuesta.prueba.id = :idPrueba") })
public class Preguntas extends AEntity {

	public static class Builder {
        private Long id;
        private String name;
        private int numero;
        private RespuestasEsperadasPrueba respuesta;
        private Prueba prueba;

        public Preguntas build() {
            final Preguntas pregunta = new Preguntas();
            pregunta.id = id;
            pregunta.name = name;
            pregunta.numero = numero;
            pregunta.respuestaEsperada = respuesta;
            pregunta.prueba = prueba;
            return pregunta;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder numero(int numero) {
            this.numero = numero;
            return this;
        }

        public Builder respuesta(RespuestasEsperadasPrueba respuesta) {
            this.respuesta = respuesta;
            return this;
        }

        public Builder prueba(Prueba prueba) {
            this.prueba = prueba;
            return this;
        }

    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int numero;
    

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private RespuestasEsperadasPrueba respuestaEsperada;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Prueba prueba;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getNumero() {
        return numero;
    }

    public RespuestasEsperadasPrueba getRespuestaEsperada() {
        return respuestaEsperada;
    }


    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNumero(int numero) {
        this.numero = numero;
        name = String.format("%d", numero);
    }

    public void setRespuestaEsperada(RespuestasEsperadasPrueba respuesta) {
        this.respuestaEsperada = respuesta;
    }

    public Prueba getPrueba() {
		return prueba;
	}

	public void setPrueba(Prueba prueba) {
		this.prueba = prueba;
	}

	@Override
    public boolean validate() {
        return true;
    }
    
    
}
