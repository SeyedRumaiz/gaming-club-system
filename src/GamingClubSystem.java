import java.text.CollationElementIterator;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Singleton class that serves as the central system for managing the gaming club
 */
public class GamingClubSystem {
    private final List<Participant> participants;
    private final List<Team> teams;
    private Organizer organizer;
    private String password = "admin";
    private final List<Survey> surveys;       // List of surveys the system has
    private String username = "admin";
    private static GamingClubSystem instance;
    private int idCounter;

    private GamingClubSystem() {
        participants = Collections.synchronizedList(new ArrayList<>());
        teams = new ArrayList<>();
        surveys = new ArrayList<>();
        idCounter = 0;
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
    public void initiateSurvey() throws ExecutionException, InterruptedException {
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
    public synchronized void addTeam(Team team) {       // many threads can access
        teams.add(team);
    }

    public List<Team> getTeams() {
        return this.teams;
    }

    public int getIdCounter() {
        return this.idCounter;
    }
    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    /**
     * To get the centered average of all participants for
     * better balance between teams in terms of their skill level
     * @return the average skill rating of all participants
     */
    public double getAllParticipantsAverage() {
        double total = 0;
        for (Participant participant : participants) {
            double skill = participant.getInterest().getSkillLevel();
            total += skill;
        }
        return total / participants.size();
    }

    public synchronized String generateId() {
        idCounter++;
        return "P" + String.format("%03d", idCounter);  // format the ID
    }

    /**
     * To add the organizer into the system
     * @param organizer the organizer added
     */
    public void addOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    /**
     * To begin the process of forming teams and get exported
     * to a file for the Organizer to view
     */
    public void initiateFormation() {
        try {
            MatchingStrategy builder = new BalancedStrategy();
            List<Team> formedTeams = builder.buildTeams(participants, Team.getSize());

            FileHandler.exportToCSV(formedTeams, "resources/formed_teams.csv");
            Logger.getInstance().info("Teams formed successfully.");
        } catch (ExecutionException | InterruptedException e) {
            Logger.getInstance().error("Error: " + e.getMessage());
        }
    }
}
