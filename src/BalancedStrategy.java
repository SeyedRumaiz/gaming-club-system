import java.util.*;
import java.util.concurrent.*;

/**
 * Builds teams using a balanced, multi-criteria matching algorithm
 */
public class BalancedStrategy implements MatchingStrategy {

    @Override
    public List<Team> buildTeams(List<Participant> participants, short teamSize)
            throws InterruptedException, ExecutionException {

        Logger logger = Logger.getInstance();
        logger.info("Starting team formation: " + participants.size() + " participants");

        // Shuffle and categorize to prevent bias
        GamingClubSystem gamingClubSystem = GamingClubSystem.getInstance();
        Collections.shuffle(participants);
        double targetAverage = gamingClubSystem.getAllParticipantsAverage();

        // Create synchronized lists for thread safety
        List<Participant> leaders = Collections.synchronizedList(new ArrayList<>());
        List<Participant> thinkers = Collections.synchronizedList(new ArrayList<>());
        List<Participant> balanced = Collections.synchronizedList(new ArrayList<>());

        categorizeParticipants(participants, leaders, thinkers, balanced);

        // Concurrent team formation
        int totalTeams = (int) Math.ceil((double) participants.size() / teamSize);
        ExecutorService executor = Executors.newFixedThreadPool(
                Math.min(totalTeams, Runtime.getRuntime().availableProcessors())
        );

        List<Future<Team>> futures = new ArrayList<>();

        // Submit team building tasks
        for (int teamId = 1; teamId <= totalTeams; teamId++) {
            BalancedTeamWorker worker = new BalancedTeamWorker(
                    teamId, teamSize, targetAverage, leaders, thinkers, balanced, this
            );
            futures.add(executor.submit(worker));
        }

        // Collect results
        List<Team> teams = new ArrayList<>();
        for (Future<Team> future : futures) {
            Team team = future.get();
            teams.add(team);
            gamingClubSystem.addTeam(team);
        }

        executor.shutdown();

        // Handle leftovers
        handleLeftoverParticipants(teams, leaders, thinkers, balanced, teamSize);

        // Sort and return
        teams.sort(Comparator.comparingInt(Team::getID));

        logger.info("Finished team formation: " + teams.size() + " teams");

        return teams;
    }

    /**
     * Categorize participants by personality
     */
    public void categorizeParticipants(List<Participant> allParticipants,
                                       List<Participant> leaders,
                                       List<Participant> thinkers,
                                       List<Participant> balanced) {
        for (Participant p : allParticipants) {
            String personality = p.getPersonality().getType();
            if ("Leader".equals(personality)) {
                leaders.add(p);
            } else if ("Thinker".equals(personality)) {
                thinkers.add(p);
            } else {
                balanced.add(p);
            }
        }
    }

    /**
     * Add one leader to the team if available
     */
    public void addLeader(List<Participant> selected, short teamSize,
                          List<Participant> leaders) {
        if (selected.size() < teamSize) {
            synchronized (leaders) {
                if (!leaders.isEmpty()) {
                    selected.add(leaders.remove(0));
                }
            }
        }
    }

    /**
     * Add 1-2 thinkers to the team based on random selection
     */
    public void addThinkers(List<Participant> selected, short teamSize,
                            List<Participant> thinkers, Random random) {
        int thinkersNeeded = Math.min(
                1 + random.nextInt(teamSize / 2),
                teamSize - selected.size()
        );

        synchronized (thinkers) {
            for (int i = 0; i < thinkersNeeded && !thinkers.isEmpty(); i++) {
                if (selected.size() < teamSize) {
                    selected.add(thinkers.remove(0));
                }
            }
        }
    }

    /**
     * Fill remaining spots with balanced participants
     */
    public void fillWithBalanced(List<Participant> selected, short teamSize,
                                 List<Participant> balanced, double targetAverage) {
        int needed = teamSize - selected.size();

        for (int i = 0; i < needed; i++) {
            List<Participant> candidates = getCandidates(
                    Math.min(5, needed - i), balanced
            );
            if (candidates.isEmpty()) break;

            Participant best = findBestForSkill(candidates, selected, targetAverage);
            if (best != null) {
                selected.add(best);
                candidates.remove(best);
            }

            synchronized (balanced) {
                balanced.addAll(candidates);
            }
        }
    }

    /**
     * Get a batch of candidates
     */
    private List<Participant> getCandidates(int count, List<Participant> balanced) {
        List<Participant> candidates = new ArrayList<>();
        synchronized (balanced) {
            for (int i = 0; i < count && !balanced.isEmpty(); i++) {
                candidates.add(balanced.remove(0));
            }
        }
        return candidates;
    }

    /**
     * Find best participant for skill balance
     */
    private Participant findBestForSkill(List<Participant> candidates,
                                         List<Participant> team,
                                         double targetAverage) {
        if (candidates.isEmpty()) return null;

        double currentAvg = calculateAverageSkill(team);
        Participant best = null;
        double bestScore = Double.MAX_VALUE;

        for (Participant candidate : candidates) {
            double newAvg = (currentAvg * team.size() + candidate.getInterest().getSkillLevel())
                    / (team.size() + 1);
            double score = Math.abs(newAvg - targetAverage);

            if (score < bestScore) {
                bestScore = score;
                best = candidate;
            }
        }
        return best;
    }

    /**
     * Calculate team average skill
     */
    private double calculateAverageSkill(List<Participant> team) {
        if (team.isEmpty()) return 0;
        double sum = 0;
        for (Participant p : team) {
            sum += p.getInterest().getSkillLevel();
        }
        return sum / team.size();
    }

    /**
     * Handle leftover participants after team formation
     */
    private void handleLeftoverParticipants(List<Team> teams,
                                            List<Participant> leaders,
                                            List<Participant> thinkers,
                                            List<Participant> balanced,
                                            int teamSize) {
        List<Participant> leftovers = new ArrayList<>();
        synchronized (leaders) { leftovers.addAll(leaders); }
        synchronized (thinkers) { leftovers.addAll(thinkers); }
        synchronized (balanced) { leftovers.addAll(balanced); }

        if (!leftovers.isEmpty()) {
            for (Participant p : leftovers) {
                teams.sort(Comparator.comparingInt(t -> t.getParticipants().size()));
                for (Team team : teams) {
                    if (team.getParticipants().size() < teamSize) {
                        team.addParticipant(p);
                        break;
                    }
                }
            }
        }
    }
}