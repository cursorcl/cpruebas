package cl.eos.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Clase que permite registrar la definición de un registro de una prueba.
 * 
 * @author curso
 */
public class RegistroAnularPreguntas {

	private SimpleIntegerProperty numero;
	private SimpleStringProperty respuesta;
	private SimpleBooleanProperty verdaderoFalso;
	private SimpleBooleanProperty mental;
	private SimpleBooleanProperty anulada;

	public RegistroAnularPreguntas() {
		this(0, "", false, false, false);
	}

	public RegistroAnularPreguntas(Integer numero, String respuesta,
			Boolean verdaderoFalso, Boolean mental, Boolean anulada) {
		super();
		this.numero = new SimpleIntegerProperty(numero);
		this.respuesta = new SimpleStringProperty(respuesta);
		this.verdaderoFalso = new SimpleBooleanProperty(
				verdaderoFalso.booleanValue());
		this.mental = new SimpleBooleanProperty(mental.booleanValue());
		this.anulada = new SimpleBooleanProperty(anulada.booleanValue());
		
		
		this.mental.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue.equals(Boolean.TRUE)) {
					RegistroAnularPreguntas.this.verdaderoFalso.set(Boolean.FALSE);
					RegistroAnularPreguntas.this.respuesta.set(" ");
				}
			}
		});

		this.verdaderoFalso.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue.equals(Boolean.TRUE)) {
					RegistroAnularPreguntas.this.mental.set(Boolean.FALSE);
					if(!"VF".contains(getRespuesta().toUpperCase()))
					{
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

	public Boolean getAnulada() {
		return anulada.getValue();
	}

	public void setAnulada(Boolean anulada) {
		this.anulada.set(anulada);
	}

	public SimpleBooleanProperty anuladaProperty() {
		return anulada;
	}

}
