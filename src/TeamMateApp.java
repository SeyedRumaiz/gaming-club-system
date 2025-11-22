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

        System.out.println("Welcome to Gaming Club System!");
        System.out.println("1: Login");
        System.out.println("2: Exit");
        System.out.print("Please enter your choice: ");

        while (true) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Are you the Organizer? (Y/N): ");
                    String answer = scanner.next().toUpperCase();
                    if (answer.equals("Y")) {
                        System.out.print("Enter password: ");
                        scanner.nextLine();
                        String password = scanner.nextLine();
                        boolean confirmed = loginManager.confirmPassword(password, gamingClubSystem);
                        if (confirmed) {
                            System.out.print("Successfully logged in!");
                            System.out.println();
                            System.out.print("Enter your name: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter your email: ");
                            String email = scanner.nextLine();

                            gamingClubSystem.addOrganizer(name, email);

                    } else {
                            System.out.println("Incorrect password!");
                        }
                    } else {
                        System.out.print("Do you wish to proceed with the survey? (Y/N): ");
                        boolean proceeding = scanner.next().equalsIgnoreCase("Y");
                        scanner.nextLine();
                        if (proceeding) {
                            gamingClubSystem.initiateSurvey(scanner);
                        }
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
