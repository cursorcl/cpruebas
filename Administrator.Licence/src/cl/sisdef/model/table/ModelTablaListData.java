package cl.sisdef.model.table;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import cl.sisdef.model.Register;

/**
 * Clase que implementa el modelo de la tabla que contiene el listado de las
 * licencias generadas.
 * 
 * @author rparedes
 *
 */
public class ModelTablaListData extends AbstractTableModel {
    /**
     * Serial.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Titulos de las columnas de la tabla.
     */
    private final String[] header = { "Fecha", "Serial", "Licencia" };
    /**
     * Valores de la tabla.
     */
    private final List<Register> valores = new LinkedList<Register>();

    /**
     * Metodo que ingresa valores a la tabla.
     *
     * @param calculo
     *            El calculo a ser ingresado.
     */
    public void addData(final Register calculo) {
        if (calculo != null) {
            valores.add(calculo);
            fireTableDataChanged();
        }
    }

    /**
     * Metodo que agrega o actualiza un elemento del modelo.
     * 
     * @param object
     */
    public void addElement(final Register object) {
        if (object != null) {

            // if ( this.valores.contains( object ) )
            // {
            // updateElement( object );
            // }
            // else
            // {
            valores.add(object);
            // }

            fireTableDataChanged();
        }
    }

    /**
     * Limpia el contenido del modelo.
     */
    public final void clearItems() {
        valores.clear();
        fireTableDataChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(final int column) {
        Class<?> resultado = null;
        if (column == 0) {
            resultado = Register.class;
        }
        if (column == 1) {
            resultado = Register.class;
        }
        if (column == 2) {
            resultado = Register.class;
        }
        return resultado;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return header.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(final int colValue) {
        return header[colValue];
    }

    /**
     * Metodo que devuelve los datos de la tabla.
     *
     * @return Los valores de la tabla.
     */
    public List<Register> getData() {
        return valores;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return valores.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(final int rowValue, final int colValue) {
        Object resultado = null;
        if (colValue == 0) {
            resultado = valores.get(rowValue);
        }
        if (colValue == 1) {
            resultado = valores.get(rowValue);
        }
        if (colValue == 2) {
            resultado = valores.get(rowValue);
        }
        return resultado;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(final int fila, final int column) {
        return false;
    }

    /**
     * Metodo que indica si el modelo esta vacio o no.
     *
     * @return Boolean con el valor de vacio o no.
     */
    public boolean isVacio() {
        return valores.isEmpty();
    }

    /**
     * Metodo que borra un elemento de los valores de la tabla.
     */
    public void removeData(final Register object) {
        final int index = valores.indexOf(object);
        if (index != -1) {
            valores.remove(index);
            fireTableDataChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAt(final Object value, final int fila, final int column) {
    }

    /**
     * Metodo que actualiza un elemento del modelo.
     * 
     * @param object
     */
    public void updateElement(final Register object) {
        final int index = valores.indexOf(object);
        if (index != -1) {
            valores.set(index, object);
            fireTableDataChanged();
        }
    }
}
