package cl.eos.view;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import cl.eos.persistence.models.PruebaRendida;

public class LetraRespuesta extends TableCell<PruebaRendida, String> {

	private String responses;

	public LetraRespuesta() {
		setAlignment(Pos.CENTER);
	}

	@Override
	protected void updateItem(String respuesta, boolean empty) {
		super.updateItem(respuesta, empty);
		if (respuesta == null) {
			setText(null);
		} else {
			int index = getTableView().getColumns().indexOf(getTableColumn()) - 4;
			String caracter = respuesta.substring(index, index + 1);
			char[] ccar = caracter.toCharArray();
			String cResp = comparaRespuestas(ccar, index);
			setText(cResp);
		}
	}

	public String getResponses() {
		return responses;
	}

	public void setResponses(String responses) {
		this.responses = responses;
	}

	private String comparaRespuestas(char[] ccar, int index) {
		String cResultado = null;
		char[] cResponses = responses.toCharArray();

		if (ccar[0] == 'O') {
			cResultado = "O";
		} else if (ccar[0] == (cResponses[index]) || ccar[0] == '+') {
			cResultado = "B";
		} else if (ccar[0] != (cResponses[index]) || ccar[0] == '-') {
			cResultado = "M";
		}
		return cResultado;
	}

}
