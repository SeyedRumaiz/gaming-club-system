import java.util.List;

public interface MatchingStrategy {
    List<Participant> formTeams(List<Participant> participants, short size);
}
