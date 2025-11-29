import java.util.*;

public class BalancedTeamBuilder extends TeamBuilderTemplate {

    private final int GAME_CAP;
    private final int MIN_ROLES;
    private final double TARGET_AVERAGE;
    private final List<Participant> chunk;  // participants for this team
    private final Team team;                 // team to fill

    // Buckets for parallel computation
    private Map<String, List<Participant>> gameBuckets;
    private Map<Role, List<Participant>> roleBuckets;
    private Map<String, List<Participant>> personalityBuckets;
    private List<Participant> skillSorted;

    public BalancedTeamBuilder(int GAME_CAP, int MIN_ROLES, double TARGET_AVERAGE,
                               Team team, List<Participant> chunk) {
        this.GAME_CAP = GAME_CAP;
        this.MIN_ROLES = MIN_ROLES;
        this.TARGET_AVERAGE = TARGET_AVERAGE;
        this.team = team;
        this.chunk = chunk;
    }

    @Override
    protected Map<String, List<Participant>> applyGameConstraint() {
        gameBuckets = new HashMap<>();
        for (Participant p : chunk) {
            List<Participant> bucket = gameBuckets.computeIfAbsent(p.getInterest().getGame(), k -> new ArrayList<>());

            if (bucket.size() < GAME_CAP) {
                bucket.add(p);
            }
        }
        return gameBuckets;
    }

    @Override
    protected Map<Role, List<Participant>> applyRoleConstraint() {
        roleBuckets = new HashMap<>();
        for (Participant p : chunk) {
            roleBuckets.computeIfAbsent(p.getInterest().getRole(), k -> new ArrayList<>()).add(p);

            if (roleBuckets.size() < MIN_ROLES) {
                
            }
        }
        return roleBuckets;
    }

    @Override
    protected Map<String, List<Participant>> applyPersonalityMix() {
        personalityBuckets = new HashMap<>();
        personalityBuckets.put("Leader", new ArrayList<>());
        personalityBuckets.put("Thinker", new ArrayList<>());
        personalityBuckets.put("Balanced", new ArrayList<>());
        for (Participant p : chunk) {
            personalityBuckets.get(p.getPersonality().getType()).add(p);
        }
        return personalityBuckets;
    }

    @Override
    protected List<Participant> applySkillBalance() {
        skillSorted = new ArrayList<>(chunk);
        skillSorted.sort(Comparator.comparingDouble(p -> Math.abs(p.getInterest().getSkillLevel() - TARGET_AVERAGE)));
        return skillSorted;
    }

    public Team getTeam() {
        return team;
    }
}
