import java.util.List;

public interface MatchingStrategy {
    List<Team> formTeams(List<Participant> participants, short size);
}
