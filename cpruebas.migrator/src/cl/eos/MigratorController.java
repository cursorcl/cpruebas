package cl.eos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;

public class MigratorController {

    static Connection connection = null;
    static DatabaseMetaData metadata = null;
    static Connection connection_multi;

    static {
        try {
            MigratorController.connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/cpruebas?" + "user=root&password=admin");
            MigratorController.connection_multi = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/multi_cpruebas?" + "user=root&password=admin");
        } catch (final SQLException e) {
            System.err.println("There was an error getting the connection: " + e.getMessage());
        }
        try {
            MigratorController.metadata = MigratorController.connection.getMetaData();
        } catch (final SQLException e) {
            System.err.println("There was an error getting the metadata: " + e.getMessage());
        }
    }

    @FXML
    ListView<String> lstCpruebas;
    @FXML
    ListView<String> lstMulti;
    @FXML
    Button btnMigrar;
    @FXML
    ProgressBar progress;
    @FXML
    ProgressIndicator pIndicator;

    String[] tablas = { "Alumno", "Asignatura", "Curso", "Profesor", "Colegio", "EjeTematico", "Habilidad", "Prueba",
            "TipoPrueba", "Ciclo", "PruebaRendida", "NivelEvaluacion", "RangoEvaluacion", "TipoCurso",
            "EvaluacionPrueba", "RespuestasEsperadasPrueba", "Formas", "EvaluacionEjeTematico" };

    public MigratorController() {
        super();
    }

    @FXML
    public void initialize() {

        initializeMulti();
        initializeCPruebas();
        btnMigrar.setOnAction(event -> {
            try {
                migrar();
            } catch (final SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    void initializeCPruebas() {

        final ObservableList<String> oList = FXCollections.observableArrayList(tablas);
        lstCpruebas.setItems(oList);
    }

    void initializeMulti() {
        final ObservableList<String> oList = FXCollections.observableArrayList(tablas);
        lstMulti.setItems(oList);
        lstMulti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    protected void migrar() throws SQLException {
        final float total = tablas.length + 1;
        // Primero creo valores de TipoColegio
        int n = 1;

        final ObservableList<String> lst = lstMulti.getItems();
        for (final String table : lst) {
            lstMulti.getSelectionModel().select(n - 1);
            n = n + 1;
            final float prog = n / total;
            pIndicator.setProgress(prog);
            progress.setProgress(prog);
        }
        procesar(lstMulti.getItems());
    }

    public void procesar(ObservableList<String> tables) throws SQLException {

        ResultSet rs = null;
        for (final String actualTable : tables) {
            rs = MigratorController.metadata.getColumns(null, null, actualTable, null);

            final String sqlI = "insert into %s (%s) values (%s)";
            final StringBuffer values = new StringBuffer();
            final StringBuffer params = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                if (values.length() > 0)
                    values.append(", ");
                values.append(rs.getString("COLUMN_NAME"));

                if (params.length() > 0)
                    params.append(", ");
                params.append("?");
                count++;
            }

            if (actualTable.equalsIgnoreCase("alumno")) {
                values.append(", " + "TIPOALUMNO_ID");
                params.append(", ?");

            }
            values.append(", " + "VERSION");
            params.append(", ?");

            final String sqlInsert = String.format(sqlI, actualTable, values.toString(), params.toString());

            PreparedStatement pInsertStat = MigratorController.connection_multi
                    .prepareStatement("insert into tipoalumno (0, 'TODOS', 1)");
            pInsertStat.execute();
            pInsertStat = MigratorController.connection_multi.prepareStatement("insert into tipoalumno (1, 'PIE', 1)");
            pInsertStat.execute();
            pInsertStat = MigratorController.connection_multi
                    .prepareStatement("insert into tipoalumno (2, 'NO PIE', 1)");
            pInsertStat.execute();

            final String sqlSelect = "select * from " + actualTable.toLowerCase();
            final PreparedStatement pStat = MigratorController.connection.prepareStatement(sqlSelect);
            final ResultSet res = pStat.executeQuery();
            while (res.next()) {
                pInsertStat = MigratorController.connection_multi.prepareStatement(sqlInsert);
                int n = 0;
                for (n = 1; n <= count; n++) {
                    pInsertStat.setObject(n, res.getObject(n));
                }
                if (actualTable.equalsIgnoreCase("alumno")) {
                    pInsertStat.setInt(n++, 2);
                }
                pInsertStat.setInt(n++, 1);
                pInsertStat.execute();
            }

        }

    }

}
