import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Driver class for team formation and survey processes to begin
 */
public class TeamMateApp {
    public static void main(String[] args) throws Exception {
        GamingClubSystem gamingClubSystem = GamingClubSystem.getInstance();
        Scanner scanner = new Scanner(System.in);
        Logger logger = Logger.getInstance();
        FileHandler.loadParticipants("all_participants.csv");
        System.out.println("Welcome to Gaming Club System!");
        logger.info("System started");

       while (true) {
           try {
               System.out.println("1: Login");
               System.out.println("2: Exit");
               System.out.print("Please enter your choice: ");
               int choice = scanner.nextInt();
               scanner.nextLine();
               switch (choice) {
                   case 1 -> {
                       System.out.print("Are you the Organizer? (Y - Yes, N - No): ");
                       String answer = scanner.nextLine().toUpperCase().trim();
                       if (answer.equals("Y")) {
                           System.out.print("Enter your username: ");
                           String username = scanner.nextLine();
                           System.out.print("Enter password: ");
                           String password = scanner.nextLine();
                           boolean confirmed = gamingClubSystem.getPassword().equals(password) &&
                                   gamingClubSystem.getUsername().equals(username);
                           if (confirmed) {
                               System.out.print("Successfully logged in!");
                               System.out.println();
                               Organizer organizer = new Organizer(username);
                               gamingClubSystem.addOrganizer(organizer);
                               organizer.handleMenu(scanner);
                           } else {
                               System.out.println("Incorrect username or password!");
                           }
                       } else {
                           System.out.print("Complete survey? (Y - Yes, N - No): ");
                           boolean proceeding = scanner.next().equalsIgnoreCase("Y");
                           scanner.nextLine();
                           if (proceeding) {
                               try {
                                   gamingClubSystem.initiateSurvey();
                               } catch (Exception e) {
                                   logger.error("Something went wrong: " + e.getMessage());
                               }
                           }
                       }
                   }
                   case 2 -> {
                       System.out.print("Goodbye.");
                       System.exit(0);
                   }
                   default -> logger.error("Incorrect choice!");
               }
           } catch (InputMismatchException e) {
               System.out.println("Please enter a number.");
               scanner.nextLine();      // clear the incorrect input
           }
       }
    }
}
