package cl.eos.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Objetivo;

/**
 * Clase que permite registrar la definición de un registro de una prueba.
 * 
 * @author curso
 */
public class RegistroDefinePrueba {

	private SimpleIntegerProperty numero;
	private SimpleStringProperty respuesta;
	private SimpleBooleanProperty verdaderoFalso;
	private SimpleBooleanProperty mental;
	private ObjectProperty<Habilidad> habilidad;
	private ObjectProperty<EjeTematico> ejeTematico;
	private ObjectProperty<Objetivo> objetivo;
	private SimpleStringProperty bad;

	public RegistroDefinePrueba() {
		this(0, "", false, false);
	}

	public RegistroDefinePrueba(Integer numero, String respuesta, Boolean verdaderoFalso, Boolean mental) {
		super();
		this.numero = new SimpleIntegerProperty(numero);
		this.respuesta = new SimpleStringProperty(respuesta);
		this.verdaderoFalso = new SimpleBooleanProperty(verdaderoFalso.booleanValue());
		this.mental = new SimpleBooleanProperty(mental.booleanValue());
		this.habilidad = new SimpleObjectProperty<>();
		this.ejeTematico = new SimpleObjectProperty<>();
		this.objetivo = new SimpleObjectProperty<>();
		
		this.mental.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue.equals(Boolean.TRUE)) {
					RegistroDefinePrueba.this.verdaderoFalso.set(Boolean.FALSE);
					RegistroDefinePrueba.this.respuesta.set(" ");
				}
			}
		});

		this.verdaderoFalso.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue.equals(Boolean.TRUE)) {
					RegistroDefinePrueba.this.mental.set(Boolean.FALSE);
					if (!"VF".contains(getRespuesta().toUpperCase())) {
						setRespuesta("");
					}
				}
			}
		});

	}

	public Integer getNumero() {
		return numero.getValue();
	}

	public void setNumero(Integer numero) {
		this.numero.set(numero);
	}

	public String getRespuesta() {
		return respuesta.getValue();
	}

	public SimpleStringProperty respuestaProperty() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta.set(respuesta);
	}

	public Boolean getVerdaderoFalso() {
		return verdaderoFalso.getValue();
	}

	public void setVerdaderoFalso(Boolean verdaderoFalso) {
		this.verdaderoFalso.set(verdaderoFalso);
	}

	public SimpleBooleanProperty verdaderoFalsoProperty() {
		return verdaderoFalso;
	}

	public Boolean getMental() {
		return mental.getValue();
	}

	public void setMental(Boolean mental) {
		this.mental.set(mental);
		if (mental) {
			setVerdaderoFalso(false);
		}
	}

	public SimpleBooleanProperty mentalProperty() {
		return mental;
	}

	public Habilidad getHabilidad() {
		return habilidad.getValue();
	}

	public ObjectProperty<Habilidad> habilidadProperty() {
		return habilidad;
	}

	public void setHabilidad(Habilidad habilidad) {
		this.habilidad.set(habilidad);
	}

	public ObjectProperty<EjeTematico> ejeTematicoProperty() {
		return ejeTematico;
	}

	public EjeTematico getEjeTematico() {
		return ejeTematico.getValue();
	}

	public void setEjeTematico(EjeTematico ejeTematico) {
		this.ejeTematico.set(ejeTematico);
	}

	public String getBad() {
		return bad.getValue();
	}

	public SimpleStringProperty badProperty() {
		return bad;
	}

	public void setBad(String bad) {
		this.bad.set(bad);
	}

	public final ObjectProperty<Objetivo> objetivoProperty() {
		return this.objetivo;
	}

	public final cl.eos.persistence.models.Objetivo getObjetivo() {
		return this.objetivoProperty().get();
	}

	public final void setObjetivo(final cl.eos.persistence.models.Objetivo objetivo) {
		this.objetivoProperty().set(objetivo);
	}

}
