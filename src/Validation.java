import java.util.Arrays;

public class Validation {
    public static boolean validateRole(Role role) {
        return Arrays.asList(Role.values()).contains(role);
    }

    public static boolean validateSkillLevel(short skillLevel) {
        return skillLevel >= 1 && skillLevel <= 10;
    }

    public static boolean validatePersonalityRatings(short[] ratings) {
        for (short rating : ratings) {
            if (rating < 4 || rating > 20) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateGame(String game) {
        return GameRegistry.containsGame(game);
    }
}
