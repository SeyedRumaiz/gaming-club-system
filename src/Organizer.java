import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Organizer extends Person {

    public Organizer(String name, String email) {
        super(name, email);
    }

    public List<Participant> uploadFile(String path) throws IOException {
        List<Participant> participants = FileHandler.loadParticipants(path);
        FileHandler.saveTeams(participants);
        return participants;
    }

    public boolean initiateFormation(short teamSize) throws IOException {        // If exporting fails, shows false

        GamingClubSystem system = GamingClubSystem.getInstance();

        system.setTeamSize(teamSize);

        int totalParticipants = system.getParticipants().size();
        int totalTeams = (int) Math.ceil((double) totalParticipants / teamSize);

        for (int i = 0; i < totalTeams; i++) {
            system.addTeam(new Team(i+1));
        }

        TeamBuilder builder = new TeamBuilder(new PersonalityStrategy());
        List<Team> filledTeams = builder.performMatching(system.getTeams());
        system.getTeams().clear();
        system.getTeams().addAll(filledTeams);
        FileHandler.exportToCSV(system.getTeams(), "resources/formed_teams.csv");
        return true;
    }
}
