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
            File file = new File("all_participants.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            if (file.length() == 0) {     // if the file is empty, make the headers
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
        return rows;    // seq 1.1.1.10
    }

    /**
     * Loads participants from a CSV file and store them into the system,
     * creates participants objects and store them into the system
     * @param fileName the path of the file
     */
    public static void loadParticipants(String fileName) {

        if (!isCSV(fileName)) {     // seq 1.1.1.1
            System.out.println("Invalid file type.");   // first check if its a CSV
            return; // seq 1.1.1.4
        }

        if (!isFileExistent(fileName)) {    // 1.1.1.5
            System.out.println("File not found: " + fileName);  // seq 3
            return; // seq 1.1.1.7
        }

        List<String[]> rows = readFile(fileName);   // 1.1.1.9
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
            Personality personality = new Personality(personalityScore);    // seq 1.1.1.11
            personality.setType(personalityType); // seq 1.1.1.12
            Role role = Role.valueOf(preferredRole.toUpperCase());  // seq 1.1.1.3
            Interest interest = new Interest(preferredGame, role, skillLevel);  // seq 1.1.1.14
            Participant participant = new Participant(name, ID, email, interest, personality);  // seq 1.1.1.15
            participants.add(participant);
            // seq 1.1.1.16 -> seq 1.1.1.17 -> seq 1.1.1.18
            GamingClubSystem.getInstance().addParticipant(participant); // add to the system
            line++;
        }
        logger.info("Successfully loaded " + participants.size() + " participants!");    // seq 1.1.1.19
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
