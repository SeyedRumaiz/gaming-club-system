import java.util.List;

public class RoleStrategy implements MatchingStrategy {
    @Override
    public List<Participant> formTeams(List<Participant> participants, short size) {
        return participants;
    }
}
