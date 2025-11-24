import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class OrganizerController {
    private Organizer organizer;

    public OrganizerController(Organizer organizer) {
        this.organizer = organizer;
    }

    public List<Participant> uploadFile(String path) {
        try {
            List<Participant> participants = FileHandler.loadParticipants(path);
            boolean saved = FileHandler.saveTeams(participants);
            System.out.println("Upload successful. " + participants.size() + " participants loaded.");
            return participants;
        } catch (Exception e) {
            System.out.println("Upload failed: " + e.getMessage());
            return null;
        }
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
                Team.setSize(teamSize);         // when the tea size is valid
                GamingClubSystem.getInstance().setTeamSize(teamSize);
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
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.print("Enter file path: ");
                String path = scanner.nextLine();
                organizer.uploadFile(path);
                } case 2 -> {
                    organizer.initiateFormation(defineTeamSize(scanner));
                } case 3 -> {
                    return;
                } case 4 -> {
                    System.out.println("Invalid choice.");
                }
            }
        }
    }
}
