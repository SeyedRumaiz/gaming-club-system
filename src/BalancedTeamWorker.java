import java.util.*;
import java.util.concurrent.Callable;

/**
 * Worker class that handles concurrency in team building
 */
public class BalancedTeamWorker implements Callable<Team> {
    private final int teamId;
    private final short teamSize;
    private final double targetAverage;
    private final List<Participant> leaders;
    private final List<Participant> thinkers;
    private final List<Participant> balanced;
    private final BalancedStrategy strategy;
    private final Random random = new Random();

    public BalancedTeamWorker(int teamId, short teamSize, double targetAverage,
                              List<Participant> leaders, List<Participant> thinkers,
                              List<Participant> balanced, BalancedStrategy strategy) {
        this.teamId = teamId;
        this.teamSize = teamSize;
        this.targetAverage = targetAverage;
        this.leaders = leaders;
        this.thinkers = thinkers;
        this.balanced = balanced;
        this.strategy = strategy;
    }

    @Override
    public Team call() {
        // Create team
        Team team = new Team(teamId);   // seq 5.1.2.1
        List<Participant> selected = new ArrayList<>();

        try {
            // Add Leader
            strategy.addLeader(selected, teamSize, leaders);    // seq 6

            // Add Thinkers
            strategy.addThinkers(selected, teamSize, thinkers, random); // seq 7

            // Fill with Balanced
            strategy.fillWithBalanced(selected, teamSize, balanced, targetAverage); // seq 8

            // Add to team
            for (Participant participant : selected) {
                team.addParticipant(participant);   // seq 11
            }

        } catch (Exception e) {
            Logger.getInstance().error("Error in team worker " + teamId + ": " + e.getMessage());
        }
        return team;
    }
}