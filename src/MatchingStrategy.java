import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Defines the base structure for a team algorithm
 */
public interface MatchingStrategy {
    /**
     * To build the required teams in parallel for the Organizer
     * @param participants the participants used in building the teams
     * @param teamSize the number of participants per team
     * @return a list of built teams
     * @throws InterruptedException if a thread is interrupted
     * @throws ExecutionException if execution fails
     */
    List<Team> buildTeams(List<Participant> participants, short teamSize)
            throws InterruptedException, ExecutionException;
}
