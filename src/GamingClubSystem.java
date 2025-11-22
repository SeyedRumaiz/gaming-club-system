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

    public void displayOrganizerMenu(Scanner scanner) {
        System.out.println("""
                1: Upload CSV
                2: Initiate team formation
                3: Exit""");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                organizer.uploadFile();
            } case 2 -> {
                organizer.initiateFormation();
            } case 3 -> {

            } default -> {
                System.out.println("Invalid choice. Try again");
            }
        }
    }

    public void initiateSurvey(Scanner scanner) {
        survey.getController().startSurvey(scanner);
    }

    public void addOrganizer(String name, String email) {
        this.organizer = new Organizer(name, email);
    }

    public void displayParticipantMenu(Scanner scanner) {
        System.out.println("""
                1: Complete Survey
                2: Exit""");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                survey.getController().startSurvey(scanner);
            } case 2 -> {

            } default -> {
                System.out.println("Invalid choice. Try again");
            }
        }
    }
}
