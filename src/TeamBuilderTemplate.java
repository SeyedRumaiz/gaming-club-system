import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Template pattern class to handle the algorithm when building a team
 */
public abstract class TeamBuilderTemplate {

    public final List<Team> buildTeams(List<Participant> participants, List<Team> teams) {
        applyGameConstraint();
        System.out.println("1");
        applyRoleConstraint();
        System.out.println("2");
        applyPersonalityMix();
        System.out.println("3");
        applySkillBalance();
        System.out.println("4");
        randomizeTeams(teams);
        return teams;
    }

    // Must be implemented
    protected abstract Map<String, List<Participant>> applyGameConstraint();
    protected abstract Map<Role, List<Participant>> applyRoleConstraint();
    protected abstract Map<String, List<Participant>> applyPersonalityMix();
    protected abstract List<Participant> applySkillBalance();

    /**
     * To randomize the teams for fairness
     * @param teams the teams being randomized
     */
    protected void randomizeTeams(List<Team> teams) {
        Collections.shuffle(teams);
    }

    protected Team mergeBucketsIntoTeam(Team team, Map<String, List<Participant>> gameBuckets,
                                        Map<Role, List<Participant>> roleBuckets,
                                        Map<String, List<Participant>> personalityBuckets,
                                        List<Participant> skillSorted) {
        // Add participants from game buckets
        for (List<Participant> participants : gameBuckets.values()) {
            Collections.shuffle(participants);
            for (Participant p : participants) {
                if (!team.isFull()) team.addParticipant(p);
            }
        }

        // Add participants from role buckets, skipping duplicates
        for (List<Participant> participants : roleBuckets.values()) {
            Collections.shuffle(participants);
            for (Participant p : participants) {
                if (!team.isFull() && !team.getParticipants().contains(p)) team.addParticipant(p);
            }
        }

        // Add participants from personality buckets
        for (List<Participant> participants : personalityBuckets.values()) {
            Collections.shuffle(participants);
            for (Participant p : participants) {
                if (!team.isFull() && !team.getParticipants().contains(p)) team.addParticipant(p);
            }
        }

        // Add participants based on skill balance
        for (Participant p : skillSorted) {
            if (!team.isFull() && !team.getParticipants().contains(p)) team.addParticipant(p);
        }
        return team;
    }
}
