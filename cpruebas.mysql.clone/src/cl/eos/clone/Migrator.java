package cl.eos.clone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Migrator {

	// Define tus credenciales aquí
	private static final String MYSQL_USER = "root";
	private static final String MYSQL_PASSWORD = "admin";

	/**
	 * Construye la lista base de argumentos para el comando de MySQL, incluyendo
	 * las credenciales.
	 *
	 * @param command El comando a ejecutar (e.g., "mysql", "mysqldump").
	 * @return Una lista de String con los argumentos base.
	 */
	private static List<String> buildBaseCommand(String command) {
		List<String> baseArgs = new ArrayList<>();
		baseArgs.add(command);
		baseArgs.add("--user=" + MYSQL_USER);
		baseArgs.add("--password=" + MYSQL_PASSWORD);
		return baseArgs;
	}

	/**
	 * Ejecuta un comando externo y captura su salida.
	 *
	 * @param command La lista de argumentos del comando a ejecutar.
	 * @return La lista de líneas de salida del comando.
	 * @throws IOException          Si ocurre un error de E/S.
	 * @throws InterruptedException Si el proceso es interrumpido.
	 */
	private static List<String> executeCommand(List<String> command) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(true);

		Process process = processBuilder.start();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			List<String> output = reader.lines().collect(Collectors.toList());
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				System.err.println("El comando falló con código de salida: " + exitCode);
				System.err.println("Salida del error: " + String.join("\n", output));
				throw new IOException("El comando falló con código de salida: " + exitCode);
			}
			return output;
		}
	}

	/**
	 * Crea una nueva base de datos y la clona a partir de una base de datos de
	 * origen.
	 */
	public static int createClient(String name) {
		try {
			// 1. Comando para crear la base de datos
			List<String> createDbArgs = buildBaseCommand("mysql");
			createDbArgs.add("--ssl-mode=DISABLED");
			createDbArgs.add("--execute=create database " + name);
			executeCommand(createDbArgs);

			// 2. Proceso para el mysqldump (fuente)
			createDbArgs = buildBaseCommand("mysqldump");
			createDbArgs.add("--no-data");
			createDbArgs.add("cpruebas_base");
			ProcessBuilder dumpProcessBuilder = new ProcessBuilder(createDbArgs);
			Process dumpProcess = dumpProcessBuilder.start();

			// 3. Proceso para el mysql (destino)
			createDbArgs = buildBaseCommand("mysql");
			createDbArgs.add("--ssl-mode=DISABLED");
			createDbArgs.add(name);
			ProcessBuilder cloneProcessBuilder = new ProcessBuilder(createDbArgs);
			Process cloneProcess = cloneProcessBuilder.start();

			// 4. Conecta la salida del primer proceso con la entrada del segundo en un hilo
			StreamGobbler gobbler = new StreamGobbler(dumpProcess.getInputStream(), cloneProcess.getOutputStream());
			gobbler.start();

			// 5. Espera a que ambos procesos y el hilo de transferencia terminen
			int dumpExitCode = dumpProcess.waitFor();
			gobbler.join(); // Espera a que el hilo termine su trabajo
			int cloneExitCode = cloneProcess.waitFor();

			return (dumpExitCode == 0 && cloneExitCode == 0) ? 0 : 1;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return 1;
		} catch (Throwable th) {
			th.printStackTrace();
			return 2;
		}
	}

	/**
	 * Elimina una base de datos. Corresponde a la lógica de delete.bat.
	 *
	 * @param name El nombre de la base de datos a eliminar.
	 * @return El código de salida del proceso.
	 */
	public static int deleteClient(String name) {
		try {
			List<String> args = buildBaseCommand("mysql");
			args.add("--ssl-mode=DISABLED");
			args.add("--execute=drop database " + name);
			executeCommand(args);
			return 0;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return 1;
		} catch (Throwable th) {
			th.printStackTrace();
			return 2;
		}
	}

	/**
	 * Verifica si una base de datos existe. Corresponde a la lógica de exists.bat.
	 *
	 * @param name El nombre de la base de datos a verificar.
	 * @return true si la base de datos existe, false en caso contrario.
	 */
	public static boolean exists(String name) {
		try {
			List<String> args = buildBaseCommand("mysql");
			args.add("--ssl-mode=DISABLED");
			args.add("-se");
			args.add("use " + name);
			List<String> output = executeCommand(args);
			return output.isEmpty();
		} catch (IOException | InterruptedException e) {
			return false;
		} catch (Throwable th) {
			th.printStackTrace();
			return false;
		}
	}

	/**
	 * Obtiene la lista de bases de datos del proyecto. Corresponde a la lógica de
	 * databases.bat.
	 *
	 * @return Lista de String con los nombres de bases de datos.
	 */
	public static List<String> databases() {
		try {
			List<String> args = buildBaseCommand("mysql");
			args.add("--ssl-mode=DISABLED");
			args.add("-se");
			args.add("\"show databases like 'cpr_%%'\"");
			return executeCommand(args);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}