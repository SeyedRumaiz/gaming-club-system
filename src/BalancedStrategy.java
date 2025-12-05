import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A concrete implementation that forms balanced teams
 */
public class BalancedStrategy implements MatchingStrategy {

    @Override
    public List<Team> buildTeams(List<Participant> participants, short teamSize) throws InterruptedException, ExecutionException {
        // Setup logger
        Logger logger = Logger.getInstance();
        logger.info("Starting team formation for " + participants.size() + " participants");    // seq 4

        // Shuffle and categorization setup
        GamingClubSystem gamingClubSystem = GamingClubSystem.getInstance();
        Collections.shuffle(participants);  // shuffle to prevent bias
        double targetAverage = gamingClubSystem.getAllParticipantsAverage();    // seq 5

        // Creating thread safe lists to prevent race conditions (since multiple threads remove at same time)
        List<Participant> leaders = Collections.synchronizedList(new ArrayList<>());
        List<Participant> thinkers = Collections.synchronizedList(new ArrayList<>());
        List<Participant> balanced = Collections.synchronizedList(new ArrayList<>());

        // Categorise/splitting each participant into their personality types
        categorizeParticipants(participants, leaders, thinkers, balanced);  // seq 5.1.1

        // Thread pool setup
        int totalTeams = (int) Math.ceil((double) participants.size() / teamSize);
        // Prevent too many threads flooding the CPU so runtime used
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(totalTeams, Runtime.getRuntime().availableProcessors()));

        // Submitting the team building workers
        List<Future<Team>> futures = new ArrayList<>();

        // One worker per team
        for (int teamId = 1; teamId <= totalTeams; teamId++) {
            BalancedTeamWorker worker = new BalancedTeamWorker( // seq 5.1.2
                    teamId, teamSize, targetAverage, leaders, thinkers, balanced, this
            );
            futures.add(executor.submit(worker));
        }

        // Collect future results
        List<Team> teams = new ArrayList<>();
        for (Future<Team> future : futures) {
            Team team = future.get();   // block until each worker finishes
            teams.add(team);
            gamingClubSystem.addTeam(team); // seq 10.2
        }
        executor.shutdown();

        // Handle leftover participants after threads finish
        handleLeftOverParticipants(teams, leaders, thinkers, balanced, teamSize);   // seq 10.3

        // Sort and return
        teams.sort(Comparator.comparingInt(Team::getID));
        return teams;   // seq 10.4
    }

    /**
     * Splits all participants into 3 different categories
     * @param participants the original list of participants
     * @param leaders synchronized list to store leaders
     * @param thinkers synchronized list to store thinkers
     * @param balanced synchronized list to store balanced
     */
    private void categorizeParticipants(List<Participant> participants,
                                        List<Participant> leaders,
                                        List<Participant> thinkers,
                                        List<Participant> balanced) {
        for (Participant p : participants) {
            String personality = p.getPersonality().getType();

            if (personality.equals("Leader")) {
                leaders.add(p);
            } else if (personality.equals("Thinker")) {
                thinkers.add(p);
            } else {
                balanced.add(p);
            }
        }
    }

    /**
     * Tries to add one leader to the current team
     * @param selected the partially built team
     * @param teamSize the maximum allowed team size
     * @param leaders shared synchronized list of leaders
     */
    public void addLeader(List<Participant> selected, short teamSize,
                          List<Participant> leaders) {
        if (selected.size() < teamSize) {   // only add if the team still has space
            synchronized (leaders) {    // locked the list so only one thread can access
                if (!leaders.isEmpty()) {
                    selected.add(leaders.remove(0));    // ensure no unsued leaders remain
                }
            }
        }
    }

    /**
     * Adds upto teamSize/2 thinkers to the team
     * @param selected the current team members
     * @param teamSize the maximum allowed team size
     * @param thinkers shared synchronized list of thinkers
     */
    public void addThinkers(List<Participant> selected, short teamSize,
                            List<Participant> thinkers, Random random) {

        int thinkersNeeded = Math.min(1 + random.nextInt(teamSize / 2), teamSize - selected.size());;

        synchronized (thinkers) {
            for (int i = 0; i < thinkersNeeded && !thinkers.isEmpty(); i++) {
                if (selected.size() < teamSize) {
                    selected.add(thinkers.remove(0));
                }
            }
        }
    }

    /**
     * Fills remaining spots with balanced members
     * @param selected current team members
     * @param teamSize the maximum allowed team size
     * @param balanced shared synchronized list of balanced participants
     * @param targetAverage average skill level of all participants
     */
    public void fillWithBalanced(List<Participant> selected, short teamSize,
                                 List<Participant> balanced, double targetAverage) {

        // Get how many needed to fill
        int needed = teamSize - selected.size();

        // Take a few from the balanced list and pick the one that makes team skill closer to target
        for (int i = 0; i< needed; i++) {
            List<Participant> candidates = getCandidates(
                    Math.min(5, needed-i), balanced
            );
            if (candidates.isEmpty()) {
                break;
            }
            Participant best = findBestForSkill(candidates, selected, targetAverage);   // seq 8.1
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
     * Temporarily removes count number of participants from the balanced list
     * to get a sample to choose from for the best fit
     * @param count the number of candidates to pull
     * @param balanced shared synchronized list of balanced participants
     * @return a list of temporarily removed participants to evaluate
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
     * Choose participant who still keeps the team closest
     * to the overall target skill average
     * @param candidates the sample of participants to evaluate
     * @param team the current team members
     * @param targetAverage the average skill rating for all participants
     * @return the participant whose addition best maintains balance
     */
    private Participant findBestForSkill(List<Participant> candidates,
                                         List<Participant> team,
                                         double targetAverage) {
        if (candidates.isEmpty()) {
            return null;
        }

        double currentAverage = calculateAverageSkill(team);    // seq 9
        Participant best = null;
        double bestScore = Double.MAX_VALUE;

        for (Participant candidate : candidates) {

            // Compute new average team skill
            double newAvg = (currentAverage * team.size() + candidate.getInterest().getSkillLevel()) /
                    (team.size() + 1);

            // Measure how close it is to the target average
            double score = Math.abs(newAvg - targetAverage);

            // Pick candidate with the smallest score
            if (score < bestScore) {
                bestScore = score;
                best = candidate;
            }
        }
        return best;    // seq 10.1
    }

    /**
     * Computes the average skill for a team
     * @param team the team being evaluated
     * @return the team's average skill value
     */
    private double calculateAverageSkill(List<Participant> team) {
        if (team.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Participant p : team) {
            sum += p.getInterest().getSkillLevel();
        }
        return sum / team.size();   // seq 10
    }

    /**
     * To fill the remaining unused people to teams after being built
     * (those not used by worker threads) to the smallest possible team
     * @param teams the list of all formed teams
     * @param leaders leftover leaders
     * @param thinkers leftover thinkers
     * @param balanced leftover balanced
     * @param teamSize maximum allowed team size
     */
    private void handleLeftOverParticipants(List<Team> teams,
                                            List<Participant> leaders,
                                            List<Participant> thinkers,
                                            List<Participant> balanced,
                                            short teamSize) {

        List<Participant> leftOvers = new ArrayList<>();

        synchronized (leaders) {
            leftOvers.addAll(leaders);
        }
        synchronized (thinkers) {
            leftOvers.addAll(thinkers);
        }
        synchronized (balanced) {
            leftOvers.addAll(balanced);
        }

        if (!leftOvers.isEmpty()) {
            for (Participant p : leftOvers) {

                // Sort teams by current size in ascending order
                teams.sort(Comparator.comparingInt(t -> t.getParticipants().size()));

                // Pull each leftover into the smallest available team for fairness
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
