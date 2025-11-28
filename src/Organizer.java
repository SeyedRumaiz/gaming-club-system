import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
     * @return the team size defined
     */
    public short defineTeamSize(Scanner scanner) {
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
                    return teamSize;
                }
                GamingClubSystem.getInstance().setTeamSize(teamSize);         // when the tea size is valid
                Team.setSize(teamSize);
                return teamSize;
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
                        uploadFile(path);
                    }
                    case 2 -> {
                        if (!GamingClubSystem.getInstance().getParticipants().isEmpty()) {
                            initiateFormation(defineTeamSize(scanner));
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

    /**
     * To upload participants into the system
     * @param path the path where the participants are stored
     * @return a list of participants loaded
     */
    public List<Participant> uploadFile(String path) {
        List<Participant> participants = FileHandler.loadParticipants(path);
        return participants;
    }

    /**
     * Begins the team formation process forming teams which are saved to
     * another file
     * @param teamSize the number of participants required for a team
     */
    public void initiateFormation(short teamSize) throws Exception {        // If exporting fails, shows false

        GamingClubSystem system = GamingClubSystem.getInstance();
        List<Participant> participants = system.getParticipants();
        int totalTeams = (int) Math.ceil((double) participants.size() / teamSize);
        double targetAverage = 5;

        ExecutorService executor = Executors.newFixedThreadPool(totalTeams);    // each team gets a thread
        List<Future<Team>> futures = new ArrayList<>();

        for (int i = 0; i < totalTeams; i++) {
            List<Participant> chunk = participants.subList(i * teamSize,
                    Math.min(participants.size(), (i + 1) * teamSize));
            futures.add(executor.submit(new TeamWorker(2, 3, targetAverage, i+1, chunk)));
        }

        List<Team> formedTeams = new ArrayList<>();
        for (Future<Team> future : futures) {
            formedTeams.add(future.get());
        }

        executor.shutdown();
        FileHandler.exportToCSV(formedTeams, "resources/formed_teams.csv");
        Logger.getInstance().info("Teams formed concurrently.");
    }
}
