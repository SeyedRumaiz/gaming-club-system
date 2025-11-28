import java.util.*;
import java.util.concurrent.*;

public class TeamWorker implements Callable<Team> {
    private final int GAME_CAP;
    private final int MIN_ROLES;
    private final double TARGET_AVERAGE;
    private final int teamId;
    private final List<Participant> chunk;

    public TeamWorker(int GAME_CAP, int MIN_ROLES, double TARGET_AVERAGE, int teamId, List<Participant> chunk) {
        this.GAME_CAP = GAME_CAP;
        this.MIN_ROLES = MIN_ROLES;
        this.TARGET_AVERAGE = TARGET_AVERAGE;
        this.teamId = teamId;
        this.chunk = chunk;
    }

    @Override
    public Team call() {
        Team team = new Team(teamId);
        BalancedTeamBuilder builder = new BalancedTeamBuilder(GAME_CAP, MIN_ROLES, TARGET_AVERAGE, team, chunk);
        return builder.buildSingleTeam();
    }
}