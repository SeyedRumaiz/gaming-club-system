import java.util.List;

public class RoleStrategy implements MatchingStrategy {

    private int maxRolePerTeam;

    public RoleStrategy(int maxRolePerTeam) {
        this.maxRolePerTeam = maxRolePerTeam;
    }

    @Override
    public List<Team> formTeams(List<Participant> participants, short size) {
        return null;
    }
}
