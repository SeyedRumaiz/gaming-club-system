import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Driver class for team formation and survey processes to begin
 */
public class TeamMateApp {
    public static void main(String[] args) {
        GamingClubSystem gamingClubSystem = GamingClubSystem.getInstance(); // seq 1.1/1.2
        Scanner scanner = new Scanner(System.in);
        Logger logger = Logger.getInstance();       // seq 1.3/1.4
        logger.info("System started");  // seq 1.5
        System.out.println("Welcome to Gaming Club System!");   // seq 1.6

        while (true) {
            try {
                System.out.println("1: Login");  // seq 1.7
                System.out.println("2: Exit");
                System.out.print("Please enter your choice: ");
                int choice = scanner.nextInt();  // seq 2
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        System.out.print("Are you the Organizer? (Y - Yes, N - No): ");  // seq 2.1
                        String answer = scanner.nextLine().toUpperCase().trim(); // seq 3
                        if (answer.equals("Y")) {
                            System.out.print("Enter your username: ");   // seq 3.1
                            String username = scanner.nextLine();    // seq 4
                            System.out.print("Enter password: ");    // seq 4.1
                            String password = scanner.nextLine();    // seq 5
                            // seq 5.1 -> 5.2 -> 5.3 -> 5.4
                            boolean confirmed = gamingClubSystem.getPassword().equals(password) &&
                                    gamingClubSystem.getUsername().equals(username);
                            if (confirmed) {
                                System.out.print("Successfully logged in!"); // seq 5.5
                                System.out.println();
                                Organizer organizer = new Organizer(username);   // seq 5.6
                                gamingClubSystem.addOrganizer(organizer);    // seq 5.7

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
                                            System.out.print("Enter file name with path: ");  // seq 1
                                            String fileName = scanner.nextLine();   // seq 2
                                            organizer.uploadFile(fileName); // seq 2.1
                                        }
                                        case 2 -> {
                                            if (!gamingClubSystem.getParticipants().isEmpty()) {  // if the organizer has uploaded the file
                                                System.out.print("Enter team size: ");
                                                String size = scanner.nextLine();

                                                if (Validation.validateTeamSize(size)) {
                                                    short teamSize = Short.parseShort(size);
                                                    Team.setSize(teamSize);
                                                    organizer.initiateTeamFormation();
                                                }
                                            } else {    // first get the team size then form teams
                                                System.out.println("Please load participants first.");
                                            }
                                        }
                                        case 3 -> {
                                            return;
                                        }
                                        case 4 -> System.out.println("Invalid choice.");
                                    }
                                }
                            } else {
                                System.out.println("Incorrect username or password!");   // seq 5.9
                            }
                        } else {
                            while (true) {
                            System.out.print("Complete survey? (Y - Yes, N - No): "); // seq 5.10
                            boolean proceeding = scanner.next().equalsIgnoreCase("Y");   // seq 6 (Login), seq 1 (complete survey)
                            scanner.nextLine();
                            if (proceeding) {
                                gamingClubSystem.initiateSurvey();   // seq 6.1 (Login), // seq 1.1 (Complete survey)
                                // exit survey after completion, seq 6.2
                            } else {
                                System.exit(0); // seq 6.3
                                }
                            }
                        }
                    }
                    case 2 -> {
                        System.out.print("Goodbye.");    // 6.4
                        System.exit(0);  // 6.5
                    }
                    default -> System.out.println("Invalid choice!");   // seq 6.6
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
