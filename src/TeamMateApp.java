import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Driver class for team formation and survey processes to begin
 */
public class TeamMateApp {
    public static void main(String[] args) {
        GamingClubSystem gamingClubSystem = GamingClubSystem.getInstance();
        Scanner scanner = new Scanner(System.in);
        Logger logger = Logger.getInstance();
        logger.info("System started");
        System.out.println("Welcome to Gaming Club System!");

        while (true) {
            try {
                System.out.println("1: Login");
                System.out.println("2: Exit");
                System.out.print("Please enter your choice: ");
                int choice = scanner.nextInt(); // seq 1
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        System.out.print("Are you the Organizer? (Y - Yes, Anything else - No): ");
                        String answer = scanner.nextLine().toUpperCase().trim();
                        if (answer.equals("Y")) {
                            System.out.print("Enter your username: ");
                            String username = scanner.nextLine();
                            System.out.print("Enter password: ");
                            String password = scanner.nextLine();
                            // seq 1.1 -> 1.2 -> 1.3 -> 1.4
                            boolean confirmed = gamingClubSystem.getPassword().equals(password) &&
                                    gamingClubSystem.getUsername().equals(username);
                            if (confirmed) {
                                System.out.print("Successfully logged in!"); // seq 1.5
                                System.out.println();
                                Organizer organizer = new Organizer(username);   // seq 1.6
                                gamingClubSystem.addOrganizer(organizer);    // seq 1.7

                                while (true) {
                                    System.out.println("""
                                            1: Upload participants (in CSV)
                                            2: Initiate team formation
                                            3: Exit""");
                                    System.out.print("Enter your choice: ");
                                    int option = scanner.nextInt();
                                    scanner.nextLine();
                                    switch (option) {
                                        case 1 -> {
                                            System.out.print("Enter file name with path: ");  // seq 1 (Upload participants)
                                            String fileName = scanner.nextLine();
                                            organizer.uploadFile(fileName); // seq 1.1
                                        }
                                        case 2 -> { // seq 1 (initiate team formation)

                                            // check if the organizer has uploaded the file. this seq 1.1 and 1.2
                                            if (!gamingClubSystem.getParticipants().isEmpty()) {
                                                System.out.print("Enter team size: ");
                                                String size = scanner.nextLine();

                                                if (Validation.validateTeamSize(size)) {
                                                    short teamSize = Short.parseShort(size);
                                                    Team.setSize(teamSize); // seq 1.3
                                                    organizer.initiateTeamFormation();  // seq 1.4
                                                }
                                            } else {    // first get the team size then form teams
                                                System.out.println("Please load participants first.");  // seq 1.5
                                            }
                                        }
                                        case 3 -> {
                                            return;
                                        }
                                        default -> System.out.println("Invalid choice.");
                                    }
                                }
                            } else {
                                System.out.println("Incorrect username or password!");   // seq 1.8
                            }
                        } else {
                            while (true) {
                            System.out.print("Complete survey? (Y - Yes, Anything else - No): ");
                            boolean proceeding = scanner.next().equalsIgnoreCase("Y");   // seq 1.9 (Login), seq 1 (complete survey)
                            scanner.nextLine();
                            if (proceeding) {
                                gamingClubSystem.initiateSurvey(); // seq 1.9 (Complete survey)
                            } else {
                                System.exit(0); // seq 1.10
                                }
                            }
                        }
                    }
                    case 2 -> {
                        System.out.print("Goodbye.");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.");
                scanner.nextLine();      // clear the incorrect input
            } catch (InterruptedException e) {
                logger.error("Operation was interrupted: " + e.getMessage());
            } catch (ExecutionException e) {
                logger.error("Error during execution: " + e.getMessage());
            }
        }
    }
}
