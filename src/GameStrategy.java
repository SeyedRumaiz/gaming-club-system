import java.util.ArrayList;
import java.util.List;

public class GameStrategy implements MatchingStrategy {

    private short maxPerGame;

    public GameStrategy(short maxPerGame) {
        this.maxPerGame = maxPerGame;
    }

    @Override
    public List<Team> formTeams(List<Participant> participants, short size) {

        List<Team> teams = new ArrayList<>();
        Team currentTeam =  new Team(size);

        // Add all participants to teams
        for (Participant participant : participants) {
            currentTeam.addParticipant(participant);

            // If maximum number of participants or participants with insufficient members
            if (currentTeam.getNoOfParticipants() == size) {
                teams.add(currentTeam);
                currentTeam = new Team(size);
            }
        }

        if (currentTeam.getNoOfParticipants() > 0) {
            teams.add(currentTeam);
        }

        return teams;
    }

    private void applyBalancing(List<Team> teams) {
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);

            for (int j = 0; j < team.getNoOfParticipants(); j++) {
                Participant participant = team.getParticipants()[j];
                String game = participant.getInterest().getGame();

                int count = 0;
                for (int k = j+1; k < team.getNoOfParticipants(); k++) {
                    if (game.equals(team.getParticipants()[k].getInterest().getGame())) {
                        count++;
                    }
                }
            }
        }
    }

    public short getMaxPerGame() {
        return maxPerGame;
    }

    public void setMaxPerGame(short maxPerGame) {
        this.maxPerGame = maxPerGame;
    }
}
