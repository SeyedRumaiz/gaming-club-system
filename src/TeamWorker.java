import java.util.concurrent.*;

/**
 * Class to support the concurrency for team formation
 */
public class TeamWorker implements Callable<Team> {
    private final BalancedTeamBuilder builder;

    public TeamWorker(BalancedTeamBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Team call() {
        return builder.buildSingleTeam();
    }
}