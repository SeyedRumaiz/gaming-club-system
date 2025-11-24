import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileHandler {            // not gonna inherit

    public static boolean isFileExistent(String fileName) {
        return new File(fileName).exists();
    }

    public static boolean isCSV(String fileName) {
        return fileName.toLowerCase().endsWith(".csv");
    }

    public static boolean saveTeams(List<Participant> participants) {
        return false;
    }

    private void saveParticipants(List<Participant> participants) {

    }

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
            System.out.println("Error reading file: " + e.getMessage());
        }
        return rows;
    }

    public static List<Participant> loadParticipants(String fileName) throws IOException {

        if (!isFileExistent(fileName)) {
            throw new IOException("File not found: " + fileName);
        }

        if (!isCSV(fileName)) {
            throw new IOException("Invalid file type.");
        }

        List<String[]> rows = readFile(fileName);
        List<Participant> participants = new ArrayList<>();

        int line = 1;

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
            Participant participant = new Participant(name, email, ID);
            participant.setPersonality(personality);
            participant.setInterest(interest);
            participants.add(participant);
            GamingClubSystem.getInstance().addParticipant(participant);
            line++;
        }
        System.out.println(participants.size());
        System.out.println(GamingClubSystem.getInstance().getParticipants());
        return participants;
    }

    public static void exportToCSV(List<Team> teams, String filePath) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write("TeamID,ParticipantName,Role,Skill,Game,Personality\n");
            System.out.println(teams);

            // writing each team row by row
            for (Team team : teams) {
                for (Participant participant : team.getParticipants()) {
                    bw.write(team.getID() + "," +
                            participant.getName() + "," +
                            participant.getInterest().getRole() + "," +
                            participant.getInterest().getSkillLevel() + "," +
                            participant.getInterest().getGame() + "," +
                            participant.getPersonality().getType() + "\n");
                }
            }
            bw.close();
        } catch (IOException e) {
            System.err.println("[FileHandler] Error writing to file: " + e.getMessage());
        }
        System.out.println("Teams have been successfully exported to: " + filePath);
    }
}
