package cl.eos.clone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class StreamGobbler extends Thread {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    StreamGobbler(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Es crucial cerrar el outputStream para señalar el fin del flujo
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}