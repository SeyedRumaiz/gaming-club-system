import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GamingClubSystem {
    private List<Participant> participants;
    private List<Team> teams;
    private Organizer organizer;
    private PersonalityClassifier personalityClassifier;
    private short teamSize;
    private String password = "admin";
    private Survey survey;

    public GamingClubSystem() {
        participants = new ArrayList<>();
        teams = new ArrayList<>();
        organizer = null;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
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

    public PersonalityClassifier getPersonalityClassifier() {
        return personalityClassifier;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public void initiateSurvey() throws Exception {
        survey = new Survey();
        survey.getController().startSurvey();
    }

    public void addOrganizer(String name, String email) {
        this.organizer = new Organizer(name, email);
    }
}
