import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that serves as the central system for managing the gaming club
 */
public class GamingClubSystem {
    private List<Participant> participants;
    private List<Team> teams;
    private Organizer organizer;
    private short teamSize;
    private String password = "admin";
    private List<Survey> surveys;
    private static GamingClubSystem instance;
    private String username = "admin";

    private GamingClubSystem() {
        participants = new ArrayList<>();
        teams = new ArrayList<>();
        surveys = new ArrayList<>();
    }

    public static synchronized GamingClubSystem getInstance() {
        if (instance == null) {
            instance = new GamingClubSystem();
        }
        return instance;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public short getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(short teamSize) {
        this.teamSize = teamSize;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void initiateSurvey() throws Exception {
        Survey survey = new Survey();
        surveys.add(survey);
        survey.getController().startSurvey();
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void addOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }
}
