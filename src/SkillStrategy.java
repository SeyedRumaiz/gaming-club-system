import java.util.List;

public class SkillStrategy implements MatchingStrategy {
    @Override
    public List<Team> formTeams(List<Participant> participants, short size) {

        List<Team> currentTeams = GamingClubSystem.getInstance().getTeams();

        for (int i = 0; i < currentTeams.size(); i++) {
            Team team = currentTeams.get(i);
            int avgSkill = team.getAverageSkill();
        }
        return currentTeams;
    }
}
