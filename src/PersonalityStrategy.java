import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonalityStrategy implements MatchingStrategy{
    @Override
    public List<Team> formTeams(List<Team> teams, short size) {

        // Get participants
        List<Participant> participants = GamingClubSystem.getInstance().getParticipants();

        List<Participant> leaders = new ArrayList<>();
        List<Participant> thinkers = new ArrayList<>();
        List<Participant> balanced = new ArrayList<>();

        // Grouping by types
            for (Participant participant : GamingClubSystem.getInstance().getParticipants()) {
                if (participant.getPersonality().getType().equals("Leader")) {
                    leaders.add(participant);
                } else if (participant.getPersonality().getType().equals("Thinker")) {
                    thinkers.add(participant);
                } else {
                    balanced.add(participant);
                }
            }

        // Create empty teams
        int totalParticipants = participants.size();
        int noOfTeams = (int) Math.ceil(totalParticipants / (double) size);

        List<Team> newTeams = new ArrayList<>();
        for (int i = 0; i < noOfTeams; i++) {
            newTeams.add(new Team(i+1));
        }

        // Distribute leaders
        for (int i = 0; i < leaders.size(); i++) {
            Team team = newTeams.get(i % noOfTeams);
            team.addParticipant(leaders.get(i));
        }

        // Distribute thinkers
        for (int i = 0; i < thinkers.size(); i++) {
            Team team = newTeams.get(i % noOfTeams);
            int totalThinkers = getTotalThinkers(team);     // total thinkers for this team

            if (totalThinkers < 2) {
                team.addParticipant(thinkers.get(i));
            }
        }

        // Fill remaining with balanced
        for (Participant participant : balanced) {
            boolean added = false;
            for (Team team : newTeams) {
                if (team.getParticipants().size() < size) {
                    team.addParticipant(participant);
                    added = true;
                    break;
                }
            }
            if (!added) {
                System.out.println("Could not add: " + participant.getName());
            }
        }

        return newTeams;
    }

    private int getTotalThinkers(Team team) {
        int count = 0;
        for (Participant participant : team.getParticipants()) {
            if (participant.getPersonality().getType().equals("Thinker")) {
                count++;
            }
        }
        return count;
    }
}
