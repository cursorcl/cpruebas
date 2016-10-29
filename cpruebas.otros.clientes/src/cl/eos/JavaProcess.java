package cl.eos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class JavaProcess {

    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            ProcessBuilder mySQLPorcessBuilder = new ProcessBuilder("mysqldump",
                    "--defaults-extra-file=credentials.cnf", "--no-data", "--add-drop-database", "multi_cpruebas");

            mySQLPorcessBuilder.redirectErrorStream(true);
            final Process procObject = mySQLPorcessBuilder.start();

            String cmdOutput = null;
            final BufferedReader cmdStreamReader = new BufferedReader(
                    new InputStreamReader(procObject.getInputStream()));
            cmdOutput = null;
            final StringBuffer buffer = new StringBuffer();

            while ((cmdOutput = cmdStreamReader.readLine()) != null) {
                buffer.append(cmdOutput);
                buffer.append("\n");
            }

            FileWriter fichero = null;
            PrintWriter pw = null;
            try {
                fichero = new FileWriter("d:/tmp/prueba.sql");
                pw = new PrintWriter(fichero);
                pw.print(buffer.toString());

            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fichero)
                        fichero.close();
                } catch (final Exception e2) {
                    e2.printStackTrace();
                }
            }
            mySQLPorcessBuilder = new ProcessBuilder("mysql", "--defaults-extra-file=credentials.cnf",
                    " CREATE DATABASE  IF NOT EXISTS `ejemplo`;");
            mySQLPorcessBuilder.redirectErrorStream(true);
            Process process = mySQLPorcessBuilder.start();

            mySQLPorcessBuilder = new ProcessBuilder("mysql", "--defaults-extra-file=credentials.cnf", "ejemplo");
            mySQLPorcessBuilder.redirectErrorStream(true);
            process = mySQLPorcessBuilder.start();
            final OutputStream stdin = process.getOutputStream();
            process.getErrorStream();
            final InputStream stdout = process.getInputStream();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
            writer.write(buffer.toString());

            cmdOutput = null;
            while ((cmdOutput = reader.readLine()) != null) {
                System.out.println(cmdOutput);
            }

        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final Throwable th) {
            th.printStackTrace();
        }

    }
}
