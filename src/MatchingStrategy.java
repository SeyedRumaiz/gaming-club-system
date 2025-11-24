import java.util.List;

public interface MatchingStrategy {
    List<Team> formTeams(List<Team> teams, short size);
}
