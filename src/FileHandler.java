import java.io.File;
import java.util.List;

public final class FileHandler {            // not gonna inherit

    public static boolean isFileExistent(String fileName) {
        return new File(fileName).exists();
    }

    public static boolean isCSV(String fileFormat) {
        return false;
    }

    public static boolean hasMissingFields(String fileName) {
        return false;
    }

    public static boolean saveTeams(List<Participant> participants) {
        return false;
    }

    public static boolean storeFile(String fileName) {
        return false;
    }

    public static List<Participant> loadParticipants(String fileName) {
        return null;
    }
}
