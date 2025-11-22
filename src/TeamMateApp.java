import java.time.LocalDate;
import java.util.Scanner;

/**
 * Driver class for team formation and survey processes to begin
 */
public class TeamMateApp {
    public static void main(String[] args) {
        GamingClubSystem gamingClubSystem = new GamingClubSystem();
        Survey survey = new Survey(1, LocalDate.of(2025, 5, 5), LocalDate.now());
        gamingClubSystem.setSurvey(survey);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Gaming Club System!");
        System.out.println("1: Login");
        System.out.println("2: Exit");
        System.out.print("Please enter your choice : ");
        int choice = scanner.nextInt();
        outer: while (true) {
            switch (choice) {
                case 1 -> {
                    System.out.print("Are you the Organizer? (Y/N): ");
                    String response = scanner.next();

                    if (response.equals("Y")) {
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();

                    } else {

                    }
                }

                case 2 -> {
                    System.out.println("Goodbye.");
                    break outer;
                }
            }
        }
    }
}
