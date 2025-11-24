import java.util.Scanner;

/**
 * Driver class for team formation and survey processes to begin
 */
public class TeamMateApp {
    public static void main(String[] args) {
        GamingClubSystem gamingClubSystem = new GamingClubSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Gaming Club System!");

        while (true) {
            System.out.println("1: Login");
            System.out.println("2: Exit");
            System.out.print("Please enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Are you the Organizer? (Y - Yes, N - No): ");
                    String answer = scanner.next().toUpperCase();
                    if (answer.equals("Y")) {
                        System.out.print("Enter password: ");
                        scanner.nextLine();
                        String password = scanner.nextLine();
                        boolean confirmed = gamingClubSystem.getPassword().equals(password);
                        if (confirmed) {
                            System.out.print("Successfully logged in!");
                            System.out.println();
                            System.out.print("Enter your name: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter your email: ");
                            String email = scanner.nextLine();

                            gamingClubSystem.addOrganizer(name, email);
                            while (true) {
                                System.out.println("""
                                        1: Upload CSV
                                        2: Initiate team formation
                                        3: Exit""");

                                System.out.print("Enter your choice: ");
                                int option = scanner.nextInt();
                                scanner.nextLine();
                                if (option == 1) {
                                    System.out.print("Enter file path: ");
                                    String path = scanner.nextLine();
                                    gamingClubSystem.initiateUpload(path);
                                } else if (option == 2) {
                                    gamingClubSystem.getOrganizer().initiateFormation();
                                } else {
                                    break;
                                }
                            }
                    } else {
                            System.out.println("Incorrect password!");
                        }
                    } else {
                        System.out.print("Complete survey? (Y - Yes, N - No): ");
                        boolean proceeding = scanner.next().equalsIgnoreCase("Y");
                        scanner.nextLine();
                        if (proceeding) {
                            try {
                                gamingClubSystem.initiateSurvey();
                                return;
                            } catch (Exception e) {
                                System.out.println("Something went wrong: " + e.getMessage());
                            }
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
