/**
 * Dec 20, 2012 - ayachan
 */
package cl.sisdef.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * Simple utility to allow logging with a single line output. No threads.
 *
 * From
 * http://stackoverflow.com/questions/194765/how-do-i-get-java-logging-output
 * -to-appear-on-a-single-line
 *
 * @author ayachan
 */
public class UtilLineLogging extends Formatter {
    /**
     * Utility class to force message flushing (instead all messages appear at
     * the end).
     * 
     * @author ayachan
     */
    static class FlushStreamHandler extends StreamHandler {
        public FlushStreamHandler(OutputStream out, Formatter formatter) {
            super(out, formatter);
        }

        @Override
        public synchronized void publish(LogRecord record) {
            super.publish(record);
            flush();
        }
    }

    static final String FILE_HANDLER_PATTERN = "%s%s%s_%%u.log";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    static final String DT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    // final DateFormat DFMT = new SimpleDateFormat(DT_FORMAT);

    /**
     * Set this single line formatter as the logging formatter.
     * 
     * @param level
     */
    static public void initialize(Level level) {
        UtilLineLogging.initialize(level, null);
    }

    static public void initialize(Level level, String logFilePrefix) {
        // locate log root
        Logger root = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        while (root.getParent() != null)
            root = root.getParent();
        // now I'm on the root logger in the name space
        // ... set the default report level
        if (level != null)
            root.setLevel(level);
        // ... remove all other handlers
        final Handler[] handlers = root.getHandlers();
        for (final Handler handler : handlers) {
            System.out.printf("removing log root handler %s\n", handler);
            root.removeHandler(handler);
        }

        if (logFilePrefix != null) {
            // add file handler
            try {
                final String fileHandlerPattern = String.format(UtilLineLogging.FILE_HANDLER_PATTERN,
                        System.getProperty("user.home"), System.getProperty("file.separator"), logFilePrefix);

                Handler fileHandler;
                fileHandler = new FileHandler(fileHandlerPattern, 10 * 1024 * 1024, 10);
                fileHandler.setFormatter(new UtilLineLogging());
                fileHandler.setLevel(level != null ? level : Level.INFO);
                root.addHandler(fileHandler);
            } catch (final SecurityException e) {
                e.printStackTrace(System.err);
            } catch (final IOException e) {
                e.printStackTrace(System.err);
            }
        }

        try {
            // add single line formatter handler
            final Handler handler = new FlushStreamHandler(System.out, new UtilLineLogging());
            if (level != null)
                handler.setLevel(level);
            root.addHandler(handler);
        } catch (final SecurityException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public String format(LogRecord record) {
        final StringBuilder sb = new StringBuilder();

        final DateFormat DFMT = new SimpleDateFormat(UtilLineLogging.DT_FORMAT);
        sb.append(DFMT.format(new Date(record.getMillis())));
        sb.append(" [");
        sb.append(record.getThreadID());
        sb.append("] ");
        sb.append(record.getLevel().getLocalizedName());
        sb.append(": ");
        sb.append(record.getSourceClassName());
        sb.append(" | ");
        sb.append(formatMessage(record));
        sb.append(UtilLineLogging.LINE_SEPARATOR);

        if (record.getThrown() != null) {
            try {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (final Exception ex) {
                // ignore
            }
        }

        return sb.toString();
    }
}
