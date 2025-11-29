import java.util.HashSet;
import java.util.Set;

/**
 * Maintains the set of allowed games for events in a gaming club
 */
public class GameRegistry {
    private static final Set<String> allowedGames = new HashSet<>();        // reference won't be changed

    static {
        allowedGames.add("Chess");
        allowedGames.add("FIFA");
        allowedGames.add("CS:GO");
        allowedGames.add("Valorant");
        allowedGames.add("DOTA 2");
    }

    /**
     *
     * @param game The entered game by the participant
     * @return true if the game exists
     */
    public static boolean containsGame(String game) {
        return allowedGames.contains(game);
    }

    /**
     * To get all the supported games
     * @return a set of supported games
     */
    public static Set<String> getAllowedGames() {
        return allowedGames;
    }
}
