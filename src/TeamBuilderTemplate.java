import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public abstract class TeamBuilderTemplate {
    public final List<Team> buildTeams(List<Participant> participants, List<Team> teams) {
        applyHardConstraints();
        applySoftConstraints();
        randomizeTeams(teams);
        return finalizeTeams(participants, teams);
    }

    // Must be implemented
    protected abstract void applyHardConstraints();
    protected abstract void applySoftConstraints();
    protected abstract List<Team> finalizeTeams(List<Participant> participants, List<Team> teams);

//    // Optional methods
//    protected List<Team> initializeTeams(List<Participant> participants, int teamSize) {
//        int totalTeams = (int) Math.ceil((double) participants.size() / teamSize);
//        List<Team> teams = new ArrayList<>(totalTeams);
//        for (int i = 0; i < totalTeams; i++) {
//            teams.add(new Team(i+1));
//        }
//        return teams;
//    }

    /**
     * To randomize the teams for fairness
     * @param teams the teams being randomized
     */
    protected void randomizeTeams(List<Team> teams) {
        Collections.shuffle(teams);
    }
}
