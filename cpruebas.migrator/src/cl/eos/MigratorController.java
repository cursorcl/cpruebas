package cl.eos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/cpruebas?" + "user=root&password=admin");
			connection_multi = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/multi_cpruebas?" + "user=root&password=admin");
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: " + e.getMessage());
		}
		try {
			metadata = connection.getMetaData();
		} catch (SQLException e) {
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
		btnMigrar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					migrar();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	void initializeMulti() {
		ObservableList<String> oList = FXCollections.observableArrayList(tablas);
		lstMulti.setItems(oList);
		lstMulti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	}

	void initializeCPruebas() {

		ObservableList<String> oList = FXCollections.observableArrayList(tablas);
		lstCpruebas.setItems(oList);
	}

	protected void migrar() throws SQLException {
		float total = tablas.length + 1;
		// Primero creo valores de TipoColegio
		int n = 1;

		ObservableList<String> lst = lstMulti.getItems();
		for (String table : lst) {
			lstMulti.getSelectionModel().select(n - 1);
			n = n + 1;
			float prog = ((float) n) / total;
			pIndicator.setProgress(prog);
			progress.setProgress(prog);
		}
		procesar(lstMulti.getItems());
	}

	public void procesar(ObservableList<String> tables) throws SQLException {

		ResultSet rs = null;
		for (String actualTable : tables) {
			rs = metadata.getColumns(null, null, actualTable, null);

			String sqlI = "insert into %s (%s) values (%s)";
			StringBuffer values = new StringBuffer();
			StringBuffer params = new StringBuffer();
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

			String sqlInsert = String.format(sqlI, actualTable, values.toString(), params.toString());

			PreparedStatement pInsertStat = connection_multi.prepareStatement("insert into tipoalumno (0, 'TODOS', 1)");
			pInsertStat.execute();
			pInsertStat = connection_multi.prepareStatement("insert into tipoalumno (1, 'PIE', 1)");
			pInsertStat.execute();
			pInsertStat = connection_multi.prepareStatement("insert into tipoalumno (2, 'NO PIE', 1)");
			pInsertStat.execute();

			String sqlSelect = "select * from " + actualTable.toLowerCase();
			PreparedStatement pStat = connection.prepareStatement(sqlSelect);
			ResultSet res = pStat.executeQuery();
			while (res.next()) {
				pInsertStat = connection_multi.prepareStatement(sqlInsert);
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
