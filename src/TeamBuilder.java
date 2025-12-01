import java.util.*;

/**
 * Template pattern class to handle the algorithm when building a team
 */
public abstract class TeamBuilder {
    private final int GAME_CAP;
    private final int MIN_ROLES;
    private final double TARGET_AVERAGE;
    private final List<Participant> POOL;  // participants for this team
    private final Team TEAM;                 // team to fill

    /**
     *
     */
    public TeamBuilder(int GAME_CAP, int MIN_ROLES, double TARGET_AVERAGE, List<Participant> POOL, Team TEAM) {
        this.GAME_CAP = GAME_CAP;
        this.MIN_ROLES = MIN_ROLES;
        this.TARGET_AVERAGE = TARGET_AVERAGE;
        this.POOL = POOL;
        this.TEAM = TEAM;
    }

    public final Team buildSingleTeam() {
        List<Participant> preparedPool = preparePool(POOL);     // shuffle and create a pool

        // Apply grouping and distribution
        Map<String, List<Participant>> gameBuckets = groupByGame(preparedPool);
        Map<Role, List<Participant>> roleBuckets = groupByRole(preparedPool);
        Map<String, List<Participant>> personalityBuckets = groupByPersonality(preparedPool);
        List<Participant> skillBuckets = distributeSkill(preparedPool);
        mergeBucketsIntoTeam(TEAM, gameBuckets, roleBuckets, personalityBuckets, skillBuckets);

        // Return the completed team with constraints
        return TEAM;
    }

    /**
     *
     * @param pool
     * @return
     */
    protected List<Participant> preparePool(List<Participant> pool) {
        List<Participant> copy = new ArrayList<>(pool);
        Collections.shuffle(copy);
        return copy;
    }

    // Must be implemented

    /**
     *
     * @return
     */
    protected abstract Map<String, List<Participant>> groupByGame(List<Participant> pool);

    /**
     *
     * @return
     */
    protected abstract Map<Role, List<Participant>> groupByRole(List<Participant> pool);

    /**
     *
     * @return
     */
    protected abstract Map<String, List<Participant>> groupByPersonality(List<Participant> pool);

    /**
     *
     * @return
     */
    protected abstract List<Participant> distributeSkill(List<Participant> pool);


    protected void mergeBucketsIntoTeam(Team team,
                                        Map<String, List<Participant>> gameBuckets,
                                        Map<Role, List<Participant>> roleBuckets,
                                        Map<String, List<Participant>> personalityBuckets,
                                        List<Participant> skillSorted) {

        System.out.println("game buckets: " + gameBuckets);
        System.out.println("role buckets: " + roleBuckets);
        System.out.println("personal buckets: " + personalityBuckets);
        System.out.println("skill sorted: " + skillSorted);

        List<Participant> finalList = new ArrayList<>();
        Set<Participant> used = new HashSet<>();

        // 1. Add one Leader
        addFromBucket(finalList, used, personalityBuckets.get("Leader"), 1);

        // 2. Add two Thinkers max
        addFromBucket(finalList, used, personalityBuckets.get("Thinker"), 2);

        // 3. Add players from preferred game (top bucket)
        for (List<Participant> bucket : gameBuckets.values()) {
            addFromBucket(finalList, used, bucket, GAME_CAP);
        }

        // 4. Add role diversity (one from each role)
        for (Map.Entry<Role, List<Participant>> entry : roleBuckets.entrySet()) {
            addFromBucket(finalList, used, entry.getValue(), 1);
        }

        // 5. Fill remaining spots with skill-sorted list
        addFromBucket(finalList, used, skillSorted, team.getSize());

        // Final: Add to team until full
        for (Participant p : finalList) {
            if (team.isFull()) break;
            team.addParticipant(p);
        }
        System.out.println("Done newwwww");
    }

    // HELPER: add up to 'limit' items from a list
    private void addFromBucket(List<Participant> out,
                               Set<Participant> used,
                               List<Participant> source,
                               int limit)
    {
        if (source == null) return;
        for (Participant p : source) {
            if (limit <= 0) break;
            if (used.contains(p)) continue;
            out.add(p);
            used.add(p);
            limit--;
        }
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
