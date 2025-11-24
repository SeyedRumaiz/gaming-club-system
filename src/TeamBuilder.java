import java.util.List;

public class TeamBuilder {
    private MatchingStrategy matchingStrategy;

    public TeamBuilder(MatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public void setMatchingStrategy(MatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public List<Team> performMatching(List<Team> teams) {
        return matchingStrategy.formTeams(teams, GamingClubSystem.getInstance().getTeamSize());
    }
}
