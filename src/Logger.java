import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton class responsible for logging system messages and errors to a file
 */
public class Logger {
    private static Logger instance;
    private final String FILENAME;
    private final DateTimeFormatter FORMATTER;

    private Logger(String filename) {
        this.FILENAME = filename;
        this.FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger("gaming_club_system_logs.txt");
        }
        return instance;
    }

    public synchronized void log(String type, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String line = String.format("%s | %s | %s", timestamp, type, message);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, true));
            writer.write(line);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void error(String message) {
        log("ERROR", message);
        System.err.println("Error: " + message);
    }

    public void info(String message) {
        log("INFO", message);
        System.out.println(message);
    }
}
