import java.time.LocalDate;
import java.util.Scanner;

/**
 * Driver class for team formation and survey processes to begin
 */
public class TeamMateApp {
    public static void main(String[] args) {
        GamingClubSystem gamingClubSystem = new GamingClubSystem();
        LoginManager loginManager = new LoginManager();
        Survey survey = new Survey(1, LocalDate.of(2025, 5, 5), LocalDate.now());
        gamingClubSystem.setSurvey(survey);
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nWelcome to Gaming Club System!");

        while (true) {
            int choice = loginManager.login(scanner);

            switch (choice) {
                case 1 -> {
                    String identity = loginManager.requestIdentity(scanner, gamingClubSystem);
                    if (identity.equals("Organizer")) {
                        boolean loggedIn = loginManager.authenticateOrganizer(scanner, gamingClubSystem);
                        if (loggedIn) {
                            System.out.print("Successfully logged in!");
                            System.out.println();
                            gamingClubSystem.displayOrganizerMenu(scanner);
                        } else {
                            System.out.println("Incorrect password.");
                        }
                    } else {
                        gamingClubSystem.displayParticipantMenu(scanner);      // anything else other tan an Organizer
                    }
                }
                case 2 -> {
                    System.out.print("Goodbye.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
