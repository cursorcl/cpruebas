package cl.sisdef.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Conexion a la base de datos MySQL.
 */

public class SQLiteConnector {

    /** Logger de la clase SQLiteConnector. */
    private static final Logger logger = Logger.getLogger(SQLiteConnector.class.getName());

    public static void main(final String[] args) throws SQLException {
        final SQLiteConnector mySql = new SQLiteConnector();
        System.out.println("Conexion " + (mySql.getConnection().isClosed() ? "Cerrada" : "Abierta"));
        mySql.closeConnection();
    }

    /**
     * El objeto conexi�n de la base de datos.
     */
    private Connection connection;

    /**
     * Constructor de la clase MySQLConnector.
     */
    public SQLiteConnector() {
    }

    /**
     * Metodo.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }

    /**
     * Metodo.
     * 
     * @return
     */
    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                final String url = "jdbc:sqlite:DBLicence.db";
                connection = DriverManager.getConnection(url);
            } catch (final ClassNotFoundException error) {
                System.out.println("Error al cargar driver de SQLite.");
            } catch (final SQLException except) {
                System.out.println("Error de conexión a la BD SQLite.");
                connection = null;
            }
        }
        return connection;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    public Connection reconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (final SQLException e) {
                SQLiteConnector.logger.severe("Error al desconectarse de la BD de SQLite");
            }
        }
        return getConnection();
    }
}
