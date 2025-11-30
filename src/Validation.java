import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * To validate the participants inputs
 */
public class Validation {
    public static boolean validateRole(Role role) {
        if (!Arrays.asList(Role.values()).contains(role)) {
            System.out.println("Please enter a valid role.");
            return false;
        } else if (role == null) {
            System.out.println("Please enter a role.");
            return false;
        }
        return true;
    }

    /**
     * To validate the skill level from the participant
     * @param skillLevel the skill level being validated
     * @return true if the skill is valid
     */
    public static boolean validateSkillLevel(String skillLevel) {
        short skill;
        try {
            skill = Short.parseShort(skillLevel);
            if (!(skill >= 1 && skill <= 10)) {
                System.out.println("Please enter a skill level between 1 and 10.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Skill level must be a number.");
            return false;
        }
        return true;
    }

    /**
     * To validate the personality rating level from the participant
     * @param rating the rating being validated
     * @return true if the rating is valid
     */
    public static boolean validateRating(String rating) {
        short personalityRating;
        try {
            personalityRating = Short.parseShort(rating);
            if (personalityRating < 1 || personalityRating > 5) {
                System.out.println("Please enter a valid rating between 1 and 5.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Rating must be an integer.");
            return false;
        }
        return true;
    }

    /**
     * To validate the preferred game from the participant
     * @param game the game being validated
     * @return true if the game is valid
     */
    public static boolean validateGame(String game) {
        if (!GameRegistry.containsGame(game)) {
            System.out.println("Please enter a valid game name.");
            return false;
        }
        return true;
    }

    /**
     * To validate the email from the participant
     * @param email the email being validated
     * @return true if the email is valid
     */
    public static boolean validateEmail(String email) {
        final Pattern EMAIL_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.matches();
    }

    /**
     * To validate the future for a single participant for concurrency
     * @param future the future being validated
     * @return true if the future is valid
     */
    public static boolean validateFuture(Future<Boolean> future) {
        try {
            boolean complete = future.get();
            if (!complete) {
                System.out.println("Survey processing failed.");
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            Logger.getInstance().error("Processing error: " + e.getMessage());
            return false;
        }
        return true;
    }
}
