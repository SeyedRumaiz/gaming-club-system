import java.util.List;

public class TeamBuilder {
    private MatchingStrategy matchingStrategy;

    public TeamBuilder() {

    }

    public void setMatchingStrategy(MatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public List<Team> performMatching(List<Participant> participants) {
        List<Team> teams = GamingClubSystem.getInstance().getTeams();
        return null;
    }
}
