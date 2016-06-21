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
            Process procObject = mySQLPorcessBuilder.start();

            String cmdOutput = null;
            BufferedReader cmdStreamReader = new BufferedReader(new InputStreamReader(procObject.getInputStream()));
            cmdOutput = null;
            StringBuffer buffer = new StringBuffer();

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

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fichero)
                        fichero.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            mySQLPorcessBuilder = new ProcessBuilder("mysql", "--defaults-extra-file=credentials.cnf", " CREATE DATABASE  IF NOT EXISTS `ejemplo`;");
            mySQLPorcessBuilder.redirectErrorStream(true);
            Process process = mySQLPorcessBuilder.start();
            
            mySQLPorcessBuilder = new ProcessBuilder("mysql", "--defaults-extra-file=credentials.cnf", "ejemplo");
            mySQLPorcessBuilder.redirectErrorStream(true);
            process = mySQLPorcessBuilder.start();
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
            writer.write(buffer.toString());


            cmdOutput = null;
            while ((cmdOutput = reader.readLine()) != null) {
                System.out.println(cmdOutput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }

    }
}
