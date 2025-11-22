import java.util.Scanner;

public class LoginManager {

    public int login(Scanner scanner) {
        System.out.println("1: Login");
        System.out.println("2: Exit");
        System.out.print("Please enter your choice : ");

        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public boolean authenticateOrganizer(Scanner scanner, GamingClubSystem system) {
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        return password.equals(system.getPassword());
    }

    public String requestIdentity(Scanner scanner, GamingClubSystem system) {
        System.out.print("Are you the Organizer? (Y/N): ");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("Y")) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            Organizer organizer = new Organizer(name, email);
            system.setOrganizer(organizer);
            return "Organizer";
        } else {
            return "None";
        }
    }
}
