package cl.eos.view;

import cl.eos.restful.tables.R_PruebaRendida;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

public class LetraRespuesta extends TableCell<R_PruebaRendida, String> {

    private String responses;

    public LetraRespuesta() {
        setAlignment(Pos.CENTER);
    }

    private String comparaRespuestas(char[] ccar, int index) {
        String cResultado = null;
        final char[] cResponses = responses.toUpperCase().toCharArray();

        if (ccar[0] == 'O') {
            cResultado = "O";
        } else if (ccar[0] == cResponses[index] || ccar[0] == '+') {
            cResultado = "B";
        } else if (ccar[0] != cResponses[index] || ccar[0] == '-') {
            cResultado = "M";
        }
        return cResultado;
    }

    public String getResponses() {
        return responses;
    }

    public void setResponses(String responses) {
        this.responses = responses;
    }

    @Override
    protected void updateItem(String respuesta, boolean empty) {
        super.updateItem(respuesta, empty);
        if (respuesta == null) {
            setText(null);
        } else {
            final int index = getTableView().getColumns().indexOf(getTableColumn()) - 4;
            if (index < respuesta.length()) {
                final String caracter = respuesta.toUpperCase().substring(index, index + 1);
                final char[] ccar = caracter.toCharArray();
                final String cResp = comparaRespuestas(ccar, index);
                setText(cResp);
            } else {
                setText("O");
            }
        }
    }

}
