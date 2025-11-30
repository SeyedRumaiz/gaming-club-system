import java.util.*;
import java.util.concurrent.*;

/**
 * Template pattern class to handle the algorithm when building a team
 */
public abstract class TeamBuilder {
    private final int GAME_CAP;
    private final int MIN_ROLES;
    private final double TARGET_AVERAGE;
    private final List<Participant> POOL;  // participants for this team
    private final Team TEAM;                 // team to fill
    private final ExecutorService EXECUTOR;

    /**
     *
     */
    public TeamBuilder(int GAME_CAP, int MIN_ROLES, double TARGET_AVERAGE, List<Participant> POOL, Team TEAM) {
        this.GAME_CAP = GAME_CAP;
        this.MIN_ROLES = MIN_ROLES;
        this.TARGET_AVERAGE = TARGET_AVERAGE;
        this.POOL = POOL;
        this.TEAM = TEAM;
        this.EXECUTOR = Executors.newFixedThreadPool(4);
    }

    public final Team buildSingleTeam() throws InterruptedException, ExecutionException {
            Future<Map<String, List<Participant>>> gameFuture = EXECUTOR.submit(() -> applyGameConstraint(POOL));
            Future<Map<Role, List<Participant>>> roleFuture = EXECUTOR.submit(() -> applyRoleConstraint(POOL));
            Future<Map<String, List<Participant>>> personalityFuture = EXECUTOR.submit(() -> applyPersonalityMix(POOL));
            Future<List<Participant>> skillFuture = EXECUTOR.submit(() -> applySkillBalance(POOL));

            // Wait for all futures to complete
            Map<String, List<Participant>> gameBucket = gameFuture.get();
            Map<Role, List<Participant>> roleBucket = roleFuture.get();
            Map<String, List<Participant>> personalityBucket = personalityFuture.get();
            List<Participant> skillGroup = skillFuture.get();

            EXECUTOR.shutdown();

            // Merge the results into the final team
            mergeBucketsIntoTeam(TEAM, gameBucket, roleBucket, personalityBucket, skillGroup);
            return TEAM;
    }

    // Must be implemented

    /**
     *
     * @return
     */
    protected abstract Map<String, List<Participant>> applyGameConstraint(List<Participant> pool);

    /**
     *
     * @return
     */
    protected abstract Map<Role, List<Participant>> applyRoleConstraint(List<Participant> pool);

    /**
     *
     * @return
     */
    protected abstract Map<String, List<Participant>> applyPersonalityMix(List<Participant> pool);

    /**
     *
     * @return
     */
    protected abstract List<Participant> applySkillBalance(List<Participant> pool);


    protected void mergeBucketsIntoTeam(Team team,
                                        Map<String, List<Participant>> gameBuckets,
                                        Map<Role, List<Participant>> roleBuckets,
                                        Map<String, List<Participant>> personalityBuckets,
                                        List<Participant> skillSorted) {

        Random rand = new Random();
        int teamSize = team.getSize();

        // Add from gameBuckets
        for (Map.Entry<String, List<Participant>> entry : gameBuckets.entrySet()) {
            String game = entry.getKey();
            List<Participant> bucket = entry.getValue();
            int addedForGame = 0;
            for (Participant p : bucket) {
                if (team.isFull()) break;
                if (addedForGame >= GAME_CAP) break;
                if (!team.getParticipants().contains(p)) {
                    team.addParticipant(p);
                    addedForGame++;
                }
            }
            if (team.isFull()) break;
        }

        // Ensure role variety
        Set<Role> rolesPresent = new HashSet<>();
        for (Participant p : team.getParticipants()) rolesPresent.add(p.getInterest().getRole());

        // iterate roles randomly and add one from each until MIN_ROLES reached
        List<Role> roles = new ArrayList<>(roleBuckets.keySet());
        Collections.shuffle(roles);
        for (Role r : roles) {
            if (team.isFull()) break;
            if (rolesPresent.size() >= MIN_ROLES) break;
            List<Participant> list = roleBuckets.get(r);
            if (list == null || list.isEmpty()) continue;
            // pick a random candidate with role r not already in team
            Participant pick = null;
            for (Participant cand : list) {
                if (!team.getParticipants().contains(cand)) {
                    pick = cand;
                    break;
                }
            }
            if (pick != null) {
                team.addParticipant(pick);
                rolesPresent.add(r);
            }
        }

        // Ensure 1 Leader, 1-2 Thinkers, rest Balanced
        List<Participant> leaders = personalityBuckets.getOrDefault("Leader", new ArrayList<>());
        List<Participant> thinkers = personalityBuckets.getOrDefault("Thinker", new ArrayList<>());
        List<Participant> balanced = personalityBuckets.getOrDefault("Balanced", new ArrayList<>());

        // add one leader if missing
        boolean hasLeader = false;
        for (Participant p : team.getParticipants()) {
            if (p.getPersonality().getType().equals("Leader")) { hasLeader = true; break; }
        }
        if (!hasLeader && !leaders.isEmpty() && !team.isFull()) {
            Participant pick = pickFirstNotInTeam(leaders, team);
            if (pick != null) team.addParticipant(pick);
        }

        // add up to 2 thinkers
        int thinkersToAdd = 2;
        for (Participant p : team.getParticipants()) if (p.getPersonality().getType().equals("Thinker")) thinkersToAdd--;
        while (thinkersToAdd > 0 && !team.isFull() && !thinkers.isEmpty()) {
            Participant pick = pickFirstNotInTeam(thinkers, team);
            if (pick == null) break;
            team.addParticipant(pick);
            thinkersToAdd--;
        }

        // fill remaining slots with Balanced
        while (!team.isFull() && !balanced.isEmpty()) {
            Participant pick = pickFirstNotInTeam(balanced, team);
            if (pick == null) break;
            team.addParticipant(pick);
        }

        // Fill with skillSorted until team is full
        for (Participant p : skillSorted) {
            if (team.isFull()) break;
            if (!team.getParticipants().contains(p)) team.addParticipant(p);
        }

        // Final small randomization (shuffle team participants order)
        Collections.shuffle(team.getParticipants(), rand);
    }

    // helper
    private Participant pickFirstNotInTeam(List<Participant> list, Team team) {
        for (Participant p : list) if (!team.getParticipants().contains(p)) return p;
        return null;
    }


    // Getters for subclass
    public int getGAME_CAP() {
        return this.GAME_CAP;
    }

    public int getMIN_ROLES() {
        return this.MIN_ROLES;
    }

    public double getTARGET_AVERAGE() {
        return this.TARGET_AVERAGE;
    }

    public List<Participant> getPOOL() {
        return this.POOL;
    }

    public Team getTEAM() {
        return this.TEAM;
    }
}
