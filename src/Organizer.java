import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Represents an organizer in the system who can perform team-related operations
 */
public class Organizer extends User {

    public Organizer(String name) {
        super(name);
    }
    /**
     * Allows the organizer to enter the team size for team formation
     * @param scanner to read the team size from the organizer
     */
    public void defineTeamSize(Scanner scanner) {
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
                    return;
                }
                GamingClubSystem.getInstance().setTeamSize(teamSize);         // when the tea size is valid
                Team.setSize(teamSize); // all teams have this team size
                return;
            } catch (NumberFormatException e) {
                System.out.println("Team size must be an integer.");
            }
        }
    }

    /**
     * To display and direct the organizer to their preferred menu option
     * @param scanner to read the input from the organizer
     */
    public void handleMenu(Scanner scanner) throws Exception {
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
                        FileHandler.loadParticipants(path);
                    }
                    case 2 -> {
                        if (!GamingClubSystem.getInstance().getParticipants().isEmpty()) {  // if the organizer has uploaded the file
                            defineTeamSize(scanner);        // first get the team size then form teams
                            GamingClubSystem.getInstance().initiateFormation();
                        } else {
                            System.out.println("Please load participants first.");
                        }
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
}
