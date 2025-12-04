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
        GamingClubSystem gamingClubSystem = GamingClubSystem.getInstance();

        // Continue from the left off ID
        String participantId = gamingClubSystem.generateId();

        // Adding basic details
        String name;
        String email;
        while (true) {
            System.out.print("Enter your name: ");  // seq 1.1.5.2
            name = scanner.nextLine();   // seq 2

            System.out.print("Enter your email: "); // seq 2.1
            email = scanner.nextLine();  // seq 3

            if (Validation.validateEmail(email)) { // seq 3.1
                break;
            }
        }
        String[] interestQuestions = survey.getInterestQuestions();
        // Preferred role
        Role preferredRole = null;
        while (preferredRole == null) {
            System.out.println("--- Interest questions ---");   // seq 3.2
            Role.displayAllRoles();     // seq 3.3
            String preferredRoleQuestion = interestQuestions[0];
            System.out.print(preferredRoleQuestion);     // seq 3.4

            preferredRole = parseRole(scanner.nextLine());  // seq 4
            if (Validation.validateRole(preferredRole)) {
                break;
            }
        }

        // Preferred game
        String preferredGame;
        while (true) {
            GameRegistry.displayAllowedGames();
            String preferredGameQuestion = interestQuestions[1];
            System.out.print(preferredGameQuestion);
            preferredGame = scanner.nextLine().trim();

            if (Validation.validateGame(preferredGame)) {
                break;
            }
        }

        String skillLevel;

        // Skill level
        while (true) {
            String skillLevelQuestion = interestQuestions[2];
            System.out.print(skillLevelQuestion);
            skillLevel = scanner.nextLine().trim();
            if (Validation.validateSkillLevel(skillLevel)) {
                break;
            }
        }

        short skill = Short.parseShort(skillLevel);     // get the valid skill level

        short[] ratings = getPersonalityInfo(scanner);    // get the personality scores

        // Create a response for the participant
        SurveyResponse response = survey.addResponse(participantId, name, email, skill,
                preferredRole, preferredGame, ratings);                                 // seq 7.3

        // Filter participants who don't meet the assumed threshold
        if (response.getTotalRating() < 50) {
            System.out.println("Unfortunately you are not eligible to participate.");
            gamingClubSystem.setIdCounter(gamingClubSystem.getIdCounter() - 1); // reset one more back
            return;
        }

        // Submit this response to the worker, seq 7.4
        SurveyWorker worker = new SurveyWorker(response); // sharing the executor

        Future<Boolean> future = executor.submit(worker);

        boolean futureValid = Validation.validateFuture(future);    // Check if survey processing is fine

        if (!futureValid) {
            return;
        }

        // Finally log the information
        Logger.getInstance().info("Successfully added participant with ID:" + participantId);
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
