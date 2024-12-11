import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final String logFileName;
    private static BufferedWriter writer;

    static {

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        logFileName = "system_logs_" + timestamp + ".txt";

        try {

            File logFile = new File(logFileName);
            writer = new BufferedWriter(new FileWriter(logFile, true));
            //writer = new BufferedWriter(new FileWriter(logFileName, true));
            System.out.println("Log file created at: " + logFile.getAbsolutePath());

        } catch (IOException e) {

            System.err.println("Failed to initialize logger: " + e.getMessage());
        }

    }

    public static synchronized void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date());
        String logMessage = message + " at " + "[" + timestamp + "] ";

        // Insert to create console log and the log file same time
        System.out.println(logMessage);

        try {
            if (writer != null) {
                writer.write(logMessage);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    public static void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close log file: " + e.getMessage());
        }
    }
}
