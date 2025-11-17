import java.util.List;

public class GameStrategy implements MatchingStrategy {
    @Override
    public List<Participant> formTeams(List<Participant> participants, short size) {
        return participants;
    }
}
