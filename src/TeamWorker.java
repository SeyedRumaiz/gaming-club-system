import java.util.*;
import java.util.concurrent.*;

/**
 * Class to support the concurrency for team formation
 */
public class TeamWorker implements Callable<Team> {
    private BalancedTeamBuilder builder;

    public TeamWorker(BalancedTeamBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Team call() throws Exception {
        Team team = builder.getTeam();   // temporary id

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Run all 4 concurrently
        Future<Map<String, List<Participant>>> gameFuture = executor.submit(() -> builder.applyGameConstraint());   // call inside the thread
        Future<Map<Role, List<Participant>>> roleFuture = executor.submit(() -> builder.applyRoleConstraint());
        Future<Map<String, List<Participant>>> personalityFuture = executor.submit(() -> builder.applyPersonalityMix());
        Future<List<Participant>> skillFuture = executor.submit(() -> builder.applySkillBalance());

        // Wait for the results
        Map<String, List<Participant>> gameBuckets = gameFuture.get();
        Map<Role, List<Participant>> roleBuckets = roleFuture.get();
        Map<String, List<Participant>> personalityBuckets = personalityFuture.get();
        List<Participant> skillSorted = skillFuture.get();

        executor.shutdown();    // stop accepting more tasks
        System.out.println("I am here");

        // Merge into the final team
        builder.mergeBucketsIntoTeam(team, gameBuckets, roleBuckets, personalityBuckets, skillSorted);
        System.out.println("Builder merged into team.");
        return team;
    }
}