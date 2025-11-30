import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Singleton class that serves as the central system for managing the gaming club
 */
public class GamingClubSystem {
    private final List<Participant> participants;
    private final List<Team> teams;
    private Organizer organizer;
    private short teamSize;
    private String password = "admin";
    private final List<Survey> surveys;       // List of surveys the system has
    private String username = "admin";
    private static GamingClubSystem instance;

    private GamingClubSystem() {
        participants = new ArrayList<>();
        teams = new ArrayList<>();
        surveys = new ArrayList<>();
    }

    /**
     * Method to use a single instance for the GamingClubSystem
     * @return Single gaming club instance
     */
    public static synchronized GamingClubSystem getInstance() {
        if (instance == null) {
            instance = new GamingClubSystem();
        }
        return instance;
    }

    public List<Participant> getParticipants() {
        return this.participants;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public short getTeamSize() {
        return this.teamSize;
    }

    public void setTeamSize(short teamSize) {
        this.teamSize = teamSize;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {  // password can be changed
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * To initiate the survey for participants
     */
    public void initiateSurvey() {
        Survey survey = new Survey();   // seq 1.1.1
        surveys.add(survey);        // seq 1.1.2
        // seq 1.1.3 -> 1.1.4 -> 1.1.5
        survey.getController().startSurvey();
    }

    /**
     * To add a participant into the system for team formation
     * Synchronized to add many participants at the same time
     * @param participant the participant being added
     */
    public synchronized void addParticipant(Participant participant) {
        participants.add(participant);
    }

    /**
     * To add a team into the system
     * @param team the team being added
     */
    public void addTeam(Team team) {
        teams.add(team);
    }

    /**
     * To add the organizer into the system
     * @param organizer the organizer added
     */
    public void addOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public void initiateFormation() throws InterruptedException, ExecutionException {
        int totalTeams = (int) Math.ceil((double) participants.size() / teamSize);
        double targetAverage = 5;

        // For large datasets, runtime is used
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(totalTeams, Runtime.getRuntime().availableProcessors()));
        // each team runs its own thread
        List<Future<Team>> futureTeams = new ArrayList<>(totalTeams);

        Collections.shuffle(participants);      // Shuffle to ensure randomness
        // Submit teamworkers
        for (int i = 0; i< totalTeams; i++) {       // split participants into chunks for each team
            List<Participant> chunk = participants.subList(
                    i * teamSize,     // starting index
                    Math.min(participants.size(), (i+1) * teamSize));

            BalancedTeamBuilder builder = new BalancedTeamBuilder(
                    3, 4, targetAverage, new Team(i+1), chunk);

            TeamWorker worker = new TeamWorker(builder);    // wrap builder
            futureTeams.add(executor.submit(worker));       // submit the worker to the executor
        }

        // Collect formed teams
        List<Team> formedTeams = new ArrayList<>(totalTeams);
        for (Future<Team> futureTeam : futureTeams) {
            Team team = futureTeam.get();
            formedTeams.add(team);  // each future returns build team
            addTeam(team);
        }

        executor.shutdown();

        FileHandler.exportToCSV(formedTeams, "resources/formed_teams.csv");
        Logger.getInstance().info("Teams formed successfully.");
    }
}
