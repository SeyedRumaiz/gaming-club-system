import java.util.Scanner;

public class Organizer extends Person {

    public Organizer(String name, String email) {
        super(name, email);
    }

    public boolean uploadFile() {
        System.out.print("Enter file name: ");
        return false;
    }

    public void defineTeamSize() {
        System.out.print("Enter team size: ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        if (line.isEmpty()) {
            System.out.println("Team size cannot be empty.");
            return;
        }
        try {
            short teamSize = Short.parseShort(line);

            int totalParticipants = Participant.getTotalParticipants();
            if (teamSize > totalParticipants) {
                System.out.println("Team size cannot be greater than the total number of participants.");
                return;
            }
            Team.setSize(teamSize);
        } catch (NumberFormatException e) {
            System.out.println("Team size must be an integer.");
        }
    }

    public boolean initiateFormation() {        // If exporting fails, shows false
        defineTeamSize();
        return true;
    }
}
