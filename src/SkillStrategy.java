import java.util.ArrayList;
import java.util.List;

public class SkillStrategy implements MatchingStrategy {
    @Override
    public List<Team> formTeams(List<Team> teams, short size) {
        List<Team> currentTeams = GamingClubSystem.getInstance().getTeams();

        List<Participant> participants = new ArrayList<>();

        return null;
    }
}
