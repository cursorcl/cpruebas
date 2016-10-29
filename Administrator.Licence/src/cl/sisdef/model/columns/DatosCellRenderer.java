package cl.sisdef.model.columns;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import cl.sisdef.model.Register;

public class DatosCellRenderer extends JPanel implements TableCellRenderer {
    /**
     * String con un guion.
     */
    private static final String GUION = "-";
    /**
     * Serial de la clase.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Fuente de los textos en el panel.
     */
    private static final Font FUENTE = new Font("SansSerif", Font.PLAIN, 10);
    /**
     * Dimensiones de las etiquetas del panel.
     */
    private static final Dimension DIMENSION_LABEL = new Dimension(20, 410);
    /**
     * Dimensiones del panel.
     */
    private static final Dimension DIMENSION_PANEL = new Dimension(20, 410);
    /**
     * Label que despliega la fecha.
     */
    private final JLabel inicio = new JLabel();

    /**
     * Constructor de la clase.
     */
    public DatosCellRenderer() {
        super();
        setLayout(new GridLayout(1, 1));
        setSize(DatosCellRenderer.DIMENSION_PANEL);

        inicio.setPreferredSize(DatosCellRenderer.DIMENSION_LABEL);
        inicio.setHorizontalAlignment(SwingConstants.LEFT);
        inicio.setHorizontalTextPosition(SwingConstants.LEFT);
        inicio.setMinimumSize(DatosCellRenderer.DIMENSION_LABEL);
        inicio.setMaximumSize(DatosCellRenderer.DIMENSION_LABEL);
        inicio.setForeground(Color.BLACK);
        inicio.setFont(DatosCellRenderer.FUENTE);
        add(inicio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int rowValue, final int colValue) {
        setBackground(table.getBackground());
        setForeground(table.getForeground());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());

        }

        final Register modelo = (Register) table.getModel().getValueAt(rowValue, colValue);

        String valor = null;
        switch (colValue) {
        case 1: {
            valor = modelo.getSerial();
        }
            break;
        case 2: {
            valor = modelo.getLicence();
        }
            break;
        default:
            break;
        }

        if (valor == null) {
            inicio.setText(DatosCellRenderer.GUION);
        } else {
            inicio.setText(valor);
        }
        return this;
    }
}
