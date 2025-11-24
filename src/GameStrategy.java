import java.util.ArrayList;
import java.util.List;

public class GameStrategy implements MatchingStrategy {

    private short maxPerGame;

    public GameStrategy(short maxPerGame) {
        this.maxPerGame = maxPerGame;
    }

    @Override
    public List<Team> formTeams(List<Team> teams, short size) {
        Team currentTeam =  new Team(size);
        return teams;
    }

    private void applyBalancing(List<Team> teams) {
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);

            for (int j = 0; j < team.getNoOfParticipants(); j++) {
                Participant participant = team.getParticipants().get(j);
                String game = participant.getInterest().getGame();

                int count = 0;
                for (int k = j+1; k < team.getNoOfParticipants(); k++) {
                    if (game.equals(team.getParticipants().get(k).getInterest().getGame())) {
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
