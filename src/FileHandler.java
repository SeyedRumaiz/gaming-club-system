import java.io.File;
import java.util.List;

public final class FileHandler {            // not gonna inherit

    public static boolean validateFileExistence(String fileName) {
        return new File(fileName).exists();
    }

    public static boolean validateFileFormat(String fileFormat) {
        return false;
    }

    public static boolean validateMissingFields(String fileName) {
        return false;
    }

    public static boolean saveTeams(List<Participant> participants) {
        return false;
    }

    public static List<Participant> loadTeams(String fileName) {
        return null;
    }
}
