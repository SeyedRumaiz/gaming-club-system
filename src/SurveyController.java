import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Handles the input and output operations required for a single participant
 */
public class SurveyController {
    private final Survey survey;
    private final Scanner scanner;
    private final ExecutorService executor;

    public SurveyController(Survey survey, Scanner scanner, ExecutorService executor) {
        this.survey = survey;
        this.scanner = scanner;
        this.executor = executor;
    }

    private void displayShortIntro() {
        System.out.println("--- Welcome to the Survey! ---");
        System.out.println("This should take a couple of minutes...");
    }

    /**
     *  Method to begin the whole survey process and take all inputs from the
     *  participant
     */
    public void startSurvey() {
        displayShortIntro();    // seq 1.1.5.1
        String participantId = "P" + (Participant.getTotalParticipants() + 1);      // Continue from the left off ID

        // Adding basic details
        while (true) {
            System.out.print("Enter your name: ");  // seq 1.1.5.2
            String name = scanner.nextLine();   // seq 2

            System.out.print("Enter your email: "); // seq 2.1
            String email = scanner.nextLine();  // seq 3

            if (!Validation.validateEmail(email)) { // seq 3.1
                System.out.println("Invalid email.");
                continue;
            }

            // Preferred role
            Role preferredRole = null;
            while (preferredRole == null) {
                System.out.println("--- Interest questions ---");   // seq 3.2
                Role.displayAllRoles();     // seq 3.3
                System.out.print("What is your preferred role?: ");     // seq 3.4

                preferredRole = parseRole(scanner.nextLine());  // seq 4
                if (preferredRole == null) {
                    System.out.println("Please enter a valid role.");
                } else if (Validation.validateRole(preferredRole)) {
                    break;
                }
            }

            // Preferred game
            String game;
            do {
                System.out.println("Available Games: " + GameRegistry.getAllowedGames());
                System.out.print("What is your preferred game?: ");
                game = scanner.nextLine().trim();

            } while (!Validation.validateGame(game));

            String skillLevel;

            // Skill level
            do {
                System.out.print("What is your skill level?: ");
                skillLevel = scanner.nextLine().trim();
            } while (!Validation.validateSkillLevel(skillLevel));

            short skill = Short.parseShort(skillLevel);     // get the valid skill level

            short[] personalityScores = getPersonalityInfo(scanner);    // get the personality scores

            // Create a response for the participant
            SurveyResponse response = survey.addResponse(participantId, name, email, skill,
                    preferredRole, game, personalityScores);

            // Submit this response to the worker
            SurveyWorker worker = new SurveyWorker(response, executor); // sharing the executor

            if (response.getTotalRating() < 50) {
                System.out.println("Unfortunately you are not eligible to participate.");
                return;
            }

            Future<Boolean> future = executor.submit(worker);

            boolean futureValid = Validation.validateFuture(future);    //C heck if survey processing is fine

            if (!futureValid) {
                return;
            }

            // Finally log the information
            Logger.getInstance().info("Successfully added participant with ID:" + participantId);
            break;
        }
        System.out.println("Thank you for completing the survey.");
    }

    /**
     * To parse the string role input into a Role object
     * @param role the string being parsed
     * @return the Role object
     */
    private Role parseRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
        return null;
        }
    }

    /**
     *  To get the personality ratings/scores from the participant
     *  as an array
     * @param scanner to read the user's score
     * @return an array of personality scores
     */
    private short[] getPersonalityInfo(Scanner scanner) {
        System.out.println("-- Personality questions --");
        System.out.println("Please rate each statement from 1 (Strongly Disagree) to 5 (Strongly Agree):");
        String[] personalityQuestions = survey.getPersonalityQuestions();
        short[] ratings = new short[personalityQuestions.length];

        for (int i = 0; i < personalityQuestions.length; i++) {
            System.out.print("\"" + personalityQuestions[i] + "\" " + ": ");
            String rating = scanner.nextLine().trim();
            if (Validation.validateRating(rating)) {
                short personalityScore = Short.parseShort(rating);
                ratings[i] = (short) (personalityScore * 4);
            } else {
                i -= 1;
            }
        }
        return ratings;
    }
}
