import java.util.ArrayList;
import java.util.List;

public class GamingClubSystem {
    private List<Participant> participants =  new ArrayList<>();
    private List<Team> teams = new ArrayList<>();
    private Organizer organizer;
    private short teamSize;
    private String password = "admin";
    private List<Survey> surveys = new ArrayList<>();
    private FileHandler fileHandler = new FileHandler();
    private GameRegistry gameRegistry = new GameRegistry();
    private static GamingClubSystem instance;

    private GamingClubSystem() {

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

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public GameRegistry getGameRegistry() {
        return gameRegistry;
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

    public void initiateUpload(String path) {
        organizer.uploadFile(path);
    }
}
