import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file handling operations related to participants and teams
 */
public final class FileHandler {            // Will not be inherited

    private static final Logger logger = Logger.getInstance();

    /**
     * Checks if a file exists at the given file name path
     * @param fileName the path to the file
     * @return true if the file exists, false otherwise
     */
    public static boolean isFileExistent(String fileName) {
        return new File(fileName).exists();
    }

    /**
     * Checks whether the file has a CSV extension
     * @param fileName the file name
     * @return true if the file is a CSV, false otherwise
     */
    public static boolean isCSV(String fileName) {
        return fileName.toLowerCase().endsWith(".csv");
    }

    /**
     * Saves a single participant to a single file, synchronized to ensure
     * thread safety for many participants at the same time
     * @param participant the participant to save
     */
    public static synchronized void saveParticipant(Participant participant) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("all_participants.csv", true));
            if (GamingClubSystem.getInstance().getParticipants().size() == 1) {     // if this is the first participant, add the headers
                writer.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType\n");
            }
            writer.write(participant.getDetails());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            logger.error("Error while saving participant: " + e.getMessage());
        }
    }

    /**
     * Reads the content of a CSV file
     * @param fileName the path of the file
     * @return a list of string arrays;
     */
    public static List<String[]> readFile(String fileName) {
        List<String[]> rows = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            br.readLine();      // skip header portion
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                rows.add(fields);
            }
        } catch (IOException e) {
            logger.error("Error while reading file " + e.getMessage());
        }
        return rows;
    }

    /**
     * Loads participants from a CSV file and store them into the system,
     * creates participants objects and store them into the system
     * @param fileName the path of the file
     * @return a list of participant objects
     */
    public static List<Participant> loadParticipants(String fileName) {

        if (!isCSV(fileName)) {
            System.out.println("Invalid file type.");
            return null;
        }

        if (!isFileExistent(fileName)) {
            logger.error("File not found: " + fileName);
            return null;
        }

        List<String[]> rows = readFile(fileName);
        List<Participant> participants = new ArrayList<>();

        int line = 1;       // skip the header

        // Parse the data
        for (String[] row : rows) {

            String ID = row[0];
            String name = row[1];
            String email = row[2];
            String preferredGame = row[3];
            short skillLevel = Short.parseShort(row[4]);
            String preferredRole = row[5];
            short personalityScore = Short.parseShort(row[6]);
            String personalityType = row[7];

            // Create participant
            Personality personality = new Personality(personalityScore);
            personality.setType(personalityType);
            Role role = Role.valueOf(preferredRole.toUpperCase());
            Interest interest = new Interest(preferredGame, role, skillLevel);
            Participant participant = new Participant(name, ID, email, interest, personality);
            participants.add(participant);
            GamingClubSystem.getInstance().addParticipant(participant);
            line++;
        }
        logger.info("Loaded " + participants.size() + " participants.");
        return participants;
    }

    /**
     * Exports a list of teams to a CSV file, each occupying one row
     * @param teams the list of teams to export
     * @param filePath the path to be saved
     */
    public static void exportToCSV(List<Team> teams, String filePath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write("TeamID,ParticipantIds,Roles,Games,Personalities,AverageSkill\n");
            System.out.println(teams);

            // writing each team row by row
            for (Team team : teams) {
                bw.write(team.toString() + "\n");
            }
            bw.close();
        } catch (IOException e) {
            logger.error("Error while writing file " + e.getMessage());
        }
        logger.info("Teams have been successfully exported to: " + filePath);
    }
}
