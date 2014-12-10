package cl.sisdef.model.columns;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import cl.sisdef.model.Register;

public class DateCellRenderer extends JPanel implements TableCellRenderer
{
    /**
     * String con un guion.
     */
    private static final String    GUION            = "-";
    /**
     * Serial de la clase.
     */
    private static final long      serialVersionUID = 1L;
    /**
     * Fuente de los textos en el panel.
     */
    private static final Font      FUENTE           = new Font( "SansSerif", Font.PLAIN, 10 );
    /**
     * Dimensiones de las etiquetas del panel.
     */
    private static final Dimension DIMENSION_LABEL  = new Dimension( 20, 90 );
    /**
     * Dimensiones del panel.
     */
    private static final Dimension DIMENSION_PANEL  = new Dimension( 20, 100 );
    /**
     * Label que despliega la fecha.
     */
    private final JLabel           inicio           = new JLabel();

    /**
     * Constructor de la clase.
     */
    public DateCellRenderer()
    {
        super();
        setLayout( new GridLayout( 1, 1 ) );
        setSize( DateCellRenderer.DIMENSION_PANEL );

        this.inicio.setOpaque( true );
        this.inicio.setPreferredSize( DateCellRenderer.DIMENSION_LABEL );
        this.inicio.setHorizontalAlignment( SwingConstants.CENTER );
        this.inicio.setHorizontalTextPosition( SwingConstants.CENTER );
        this.inicio.setMinimumSize( DateCellRenderer.DIMENSION_LABEL );
        this.inicio.setMaximumSize( DateCellRenderer.DIMENSION_LABEL );
        this.inicio.setForeground( Color.BLACK );
        this.inicio.setFont( DateCellRenderer.FUENTE );
        add( this.inicio );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int rowValue, final int colValue )
    {
        final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

        this.inicio.setBackground( table.getBackground() );
        this.inicio.setForeground( table.getForeground() );

        if ( isSelected )
        {
            this.inicio.setBackground( table.getSelectionBackground() );
            this.inicio.setForeground( table.getSelectionForeground() );

        }
        final Register modelo = (Register) table.getModel().getValueAt( rowValue, 1 );
        final Date fecha = new Date(modelo.getDate());

        final String strFfecha = sdf.format( fecha );

        if ( fecha == null )
        {
            this.inicio.setText( GUION );
        }
        else
        {
            this.inicio.setText( strFfecha );
        }
        return this;
    }
}
