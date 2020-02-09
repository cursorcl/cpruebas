package cl.eos.clone;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.URL;

public class Migrator {

	public static int createClient(String name) {
		try {

			List<String> largs = new ArrayList<String>();

			String exec = Migrator.class.getResource("/res/clone.bat").getFile();
			File directory = new File(Migrator.class.getResource("/res/").getFile());

			largs.add(exec);
			largs.add(name);
			ProcessBuilder mySQLPorcessBuilder = new ProcessBuilder(largs);

			mySQLPorcessBuilder.directory(directory);
			mySQLPorcessBuilder.redirectErrorStream(true);
			final Process procObject = mySQLPorcessBuilder.start();

			String cmdOutput = null;
			final BufferedReader cmdStreamReader = new BufferedReader(
					new InputStreamReader(procObject.getInputStream()));

			while ((cmdOutput = cmdStreamReader.readLine()) != null) {
				System.out.println(cmdOutput);
			}
			return procObject.waitFor();

		} catch (final IOException e) {
			e.printStackTrace();
			return 1;
		} catch (final Throwable th) {
			th.printStackTrace();
			return 2;
		}

	}

	public static int deleteClient(String name) {
		try {

			List<String> largs = new ArrayList<String>();

			String exec = Migrator.class.getResource("/res/d.bat").getFile();
			File directory = new File(Migrator.class.getResource("/res/").getFile());

			largs.add(exec);
			largs.add(name);
			ProcessBuilder mySQLPorcessBuilder = new ProcessBuilder(largs);

			mySQLPorcessBuilder.directory(directory);
			mySQLPorcessBuilder.redirectErrorStream(true);
			final Process procObject = mySQLPorcessBuilder.start();

			String cmdOutput = null;
			final BufferedReader cmdStreamReader = new BufferedReader(
					new InputStreamReader(procObject.getInputStream()));

			while ((cmdOutput = cmdStreamReader.readLine()) != null) {
				System.out.println(cmdOutput);
			}
			return procObject.waitFor();

		} catch (final IOException e) {
			e.printStackTrace();
			return 1;
		} catch (final Throwable th) {
			th.printStackTrace();
			return 2;
		}

	}

	public static boolean exists(String name) {
		try {

			List<String> largs = new ArrayList<String>();

			String exec = Migrator.class.getResource("/res/exists.bat").getFile();
			File directory = new File(Migrator.class.getResource("/res/").getFile());

			largs.add(exec);
			largs.add(name);
			ProcessBuilder mySQLPorcessBuilder = new ProcessBuilder(largs);

			mySQLPorcessBuilder.directory(directory);
			mySQLPorcessBuilder.redirectErrorStream(true);
			final Process procObject = mySQLPorcessBuilder.start();

			final BufferedReader cmdStreamReader = new BufferedReader(
					new InputStreamReader(procObject.getInputStream()));

			int nRows = 0;
			while (cmdStreamReader.readLine() != null) {
				nRows++;
			}
			int res = procObject.waitFor();
			return res == 0 && nRows == 0;

		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		} catch (final Throwable th) {
			th.printStackTrace();
			return false;
		}

	}

	/**
	 * Obtiene la lista de bases de datos del proyecto.
	 * 
	 * Corresponden al proyecto las bases de datos cpr_....
	 * 
	 * @return Lista de String con los nombres de bases de datos.
	 */
	public static List<String> databases()
	{
		List<String> databases =  new ArrayList<>();
		System.out.println("databases");
		
		try {
			List<String> largs = new ArrayList<String>();
			
			
			String exec = Migrator.class.getResource("/res/databases.bat").getFile();
			System.out.println(System.getProperty("user.dir") + " " + exec);
			File directory = new File(Migrator.class.getResource("/res/").getFile() + "/");
			try {
				File fexec = Paths.get(Migrator.class.getResource("/res/databases.bat").toURI()).toFile();
				System.out.println(fexec.getAbsolutePath());
				exec = fexec.getAbsolutePath();
				directory = fexec.getParentFile();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			largs.add(exec);
			
			ProcessBuilder mySQLPorcessBuilder = new ProcessBuilder(largs);

			mySQLPorcessBuilder.directory(directory);
			mySQLPorcessBuilder.redirectErrorStream(true);

			final Process procObject = mySQLPorcessBuilder.start();
			String cmdOutput = null;
			final BufferedReader cmdStreamReader = new BufferedReader(
					new InputStreamReader(procObject.getInputStream()));

			while ((cmdOutput = cmdStreamReader.readLine()) != null) {
				databases.add(cmdOutput);
			}
			procObject.waitFor();

		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databases;
	}
	public static void main(String[] args) {
			try {
				
				List<String> largs = new ArrayList<String>();
				String exec = Migrator.class.getResource("/res/databases.bat").getFile();
				File directory = new File(Migrator.class.getResource("/res/").getFile());
				largs.add(exec);
				
				ProcessBuilder mySQLPorcessBuilder = new ProcessBuilder(largs);

				mySQLPorcessBuilder.directory(directory);
				mySQLPorcessBuilder.redirectErrorStream(true);

				final Process procObject = mySQLPorcessBuilder.start();
				String cmdOutput = null;
				final BufferedReader cmdStreamReader = new BufferedReader(
						new InputStreamReader(procObject.getInputStream()));

				while ((cmdOutput = cmdStreamReader.readLine()) != null) {
					System.out.println(cmdOutput);
				}
				procObject.waitFor();

			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
