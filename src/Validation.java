import java.util.Arrays;

/**
 * To validate the participants inputs
 */
public class Validation {
    public static boolean validateRole(Role role) {
        if (!Arrays.asList(Role.values()).contains(role)) {
            System.out.println("[V] Please enter a valid role.");
            return false;
        } else if (role == null) {
            System.out.println("[V] Please enter a role.");
            return false;
        }
        return true;
    }

    public static boolean validateSkillLevel(short skillLevel) {
        if (!(skillLevel >= 1 && skillLevel <= 10)) {
            System.out.println("Please enter a skill level between 1 and 10.");
            return false;
        }
        return true;
    }

    public static boolean validatePersonalityRatings(short[] ratings) {
        for (short rating : ratings) {
            if (rating < 4 || rating > 20) {
                System.out.println("Please enter a valid personality rating between 1 and 5.");
                return false;
            }
        }
        return true;
    }

    public static boolean validateGame(String game) {
        if (!GameRegistry.containsGame(game)) {
            System.out.println("Please enter a valid game name.");
            return false;
        }
        return true;
    }
}
