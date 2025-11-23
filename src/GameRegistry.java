import java.util.HashSet;
import java.util.Set;

public class GameRegistry {
    private static final Set<String> allowedGames = new HashSet<>();        // reference won't be changed

    static {
        allowedGames.add("Chess");
        allowedGames.add("FIFA");
        allowedGames.add("CS:GO");
        allowedGames.add("Valorant");
        allowedGames.add("DOTA 2");
    }

    public static boolean containsGame(String game) {
        return allowedGames.contains(game);
    }
}
