import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Represents an organizer in the system who can perform team-related operations
 */
public class Organizer extends User {

    public Organizer(String name) {
        super(name);
    }

    public short defineTeamSize(Scanner scanner) {
        while (true) {
            System.out.print("Enter team size (-1 to exit): ");
            String line = scanner.nextLine();

            if (line.isEmpty()) {
                System.out.println("Team size cannot be empty.");
                continue;
            }
            try {
                short teamSize = Short.parseShort(line);

                int totalParticipants = Participant.getTotalParticipants();
                if (teamSize > totalParticipants) {
                    System.out.println("Team size cannot be greater than the total number of participants.");
                    continue;
                }
                if (teamSize <= 0) {
                    return 0;
                }
                GamingClubSystem.getInstance().setTeamSize(teamSize);         // when the tea size is valid
                Team.setSize(teamSize);
                return teamSize;
            } catch (NumberFormatException e) {
                System.out.println("Team size must be an integer.");
            }
        }
    }

    public void handleMenu(Scanner scanner) throws IOException {
        while (true) {
            System.out.println("""
                    1: Upload CSV
                    2: Initiate team formation
                    3: Exit""");
            System.out.print("Enter your choice: ");
            try {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> {
                        System.out.print("Enter file path: ");
                        String path = scanner.nextLine();
                        uploadFile(path);
                    }
                    case 2 -> {
                        initiateFormation(defineTeamSize(scanner));
                    }
                    case 3 -> {
                        return;
                    }
                    case 4 -> {
                        System.out.println("Invalid choice.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice.");
                scanner.nextLine();
            }
        }
    }

    public List<Participant> uploadFile(String path) throws IOException {
        List<Participant> participants = FileHandler.loadParticipants(path);
        return participants;
    }

    public boolean initiateFormation(short teamSize) throws IOException {        // If exporting fails, shows false

        GamingClubSystem system = GamingClubSystem.getInstance();

        system.setTeamSize(teamSize);

        int totalParticipants = system.getParticipants().size();
        int totalTeams = (int) Math.ceil((double) totalParticipants / teamSize);

        for (int i = 0; i < totalTeams; i++) {
            system.addTeam(new Team(i+1));
        }

        TeamBuilder builder = new TeamBuilder();
        List<Team> formedTeams = builder.buildTeams(system.getParticipants(), system.getTeamSize());
        System.out.println(system.getParticipants().get(0));
        FileHandler.exportToCSV(formedTeams, "resources/formed_teams.csv");
        return true;
    }


//    public List<Participant> uploadFile(String path) {
//        try {
//            List<Participant> participants = FileHandler.loadParticipants(path);
//            boolean saved = FileHandler.saveTeams(participants);
//            Logger.getInstance().info("Uploaded " + participants.size() + " participants.");
//            return participants;
//        } catch (Exception e) {
//            Logger.getInstance().error("Upload failed: " + e.getMessage());
//            return null;
//        }
//    }
}
