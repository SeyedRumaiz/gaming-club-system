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

    /**
     * To display a short introduction
     */
    private void displayShortIntro() {
        System.out.println("--- Welcome to the Survey! ---");
        System.out.println("This should take a couple of minutes...");
    }

    /**
     *  Method to begin the whole survey process and take all inputs from the
     *  participant
     */
    public void startSurvey() {
        displayShortIntro();    // seq 1.1.4.1
        GamingClubSystem gamingClubSystem = GamingClubSystem.getInstance();   // seq 1.1.4.2, seq 1.1.4.3

        // Continue from the left off ID
        String participantId = gamingClubSystem.generateId();   // seq 1.1.4.4

        // Adding basic details
        String name;
        String email;
        while (true) {
            System.out.print("Enter your name: ");
            name = scanner.nextLine();

            if (Validation.validateName(name)) {
                break;
            }
        }

        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();

            if (Validation.validateEmail(email)) {
                break;
            }
        }

        String[] interestQuestions = survey.getInterestQuestions();
        // Preferred role
        Role preferredRole = null;
        while (preferredRole == null) {
            System.out.println("--- Interest questions ---");
            Role.displayAllRoles();     // seq 1.1.4.6
            String preferredRoleQuestion = interestQuestions[0];
            System.out.print(preferredRoleQuestion);

            preferredRole = parseRole(scanner.nextLine());  // seq 1.1.4.7
            if (Validation.validateRole(preferredRole)) {
                break;
            }
        }

        // Preferred game
        String preferredGame;
        while (true) {
            GameRegistry.displayAllowedGames(); // seq 1.1.4.9
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

        short[] ratings = getPersonalityInfo(scanner);    // get the personality scores, seq 1.1.4.10

        // Create a response for the participant
        SurveyResponse response = survey.addResponse(participantId, name, email, skill,
                preferredRole, preferredGame, ratings);

        // Filter participants who don't meet the assumed threshold
        if (response.getTotalRating() < 50) {
            System.out.println("Unfortunately you are not eligible to participate.");
            gamingClubSystem.setIdCounter(gamingClubSystem.getIdCounter() - 1); // reset one more back
            return;
        }

        // Submit this response to the worker
        SurveyWorker worker = new SurveyWorker(response); // sharing the executor, seq 1.1.4.17

        Future<Boolean> future = executor.submit(worker);

        boolean futureValid = Validation.validateFuture(future);    // Check if survey processing is fine

        if (!futureValid) {
            return;
        }

        // Finally log the information, seq 1.1.4.18
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
            return Role.valueOf(role.toUpperCase().trim()); // seq 1.1.4.8
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
        String[] personalityQuestions = survey.getPersonalityQuestions();   // seq 2
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
        return ratings; // seq 1.1.4.11
    }
}
