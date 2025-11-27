import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Handles the input and output operations required for a single participant
 */
public class SurveyController {
    private Survey survey;
    private Scanner scanner;
    private ExecutorService executor;

    public SurveyController(Survey survey, Scanner scanner, ExecutorService executor) {
        this.survey = survey;
        this.scanner = scanner;
        this.executor = executor;
    }

    public void startSurvey() throws Exception {
        System.out.println("--- Welcome to the Survey! ---");
        System.out.println("This should take a couple of minutes...");

        String participantId = "P" + (Participant.getTotalParticipants() + 1);      // Continue from the left off ID

        while (true) {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            Role preferredRole = null;
            while (preferredRole == null) {
                System.out.println("--- Interest questions ---");
                System.out.println("Available roles: " + Arrays.toString(Role.values()));
                System.out.print("What is your preferred role?: ");

                preferredRole = parseRole(scanner.nextLine());
                if (preferredRole == null) {
                    System.out.println("[SC] Please enter a valid role.");
                }
            }

            String game;
            while (true) {
                System.out.println("Available Games: " + GameRegistry.getAllowedGames());
                System.out.print("What is your preferred game?: ");
                game = scanner.nextLine();

                if (!Validation.validateGame(game)) {
                    break;
                }
            }

            short skillLevel;

            while (true) {
                System.out.print("What is your skill level?: ");
                try {
                    skillLevel = Short.parseShort(scanner.nextLine());
                    if (!Validation.validateSkillLevel(skillLevel)) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid skill level. Please enter a valid number.");
                }
            }

            Interest interest = new Interest(game, preferredRole, skillLevel);

            short[] personalityScores = getPersonalityInfo(scanner);

            SurveyResponse response = new SurveyResponse(
                    participantId,
                    name,
                    email,
                    skillLevel,
                    preferredRole,
                    game,
                    personalityScores
            );

            short totalRating = response.getTotalRating();

            Personality personality = new Personality(totalRating);

            PersonalityClassifier.getInstance().classify(personality);
            System.out.println("Your score is: " + personality.getScore());
            System.out.println("You are a: " + personality.getType());

            SurveyWorker worker = new SurveyWorker(response, executor);
            Future<Boolean> future = executor.submit(worker);

            boolean valid = future.get();

            if (!valid) {
                System.out.println("Survey validation failed.");
                return;
            }

            Participant participant = new Participant(name, email, participantId, interest, personality);

            // Finally add them to the system
            GamingClubSystem.getInstance().addParticipant(participant);
            FileHandler.saveParticipant(participant);
            Logger.getInstance().info("Successfully added participant with ID:" + participant.getID());
            break;
        }
        System.out.println("Thank you for completing the survey.");
    }

    private Role parseRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
        return null;
        }
    }

    private short[] getPersonalityInfo(Scanner scanner) {
        System.out.println("-- Personality questions --");
        System.out.println("Please rate each statement from 1 (Strongly Disagree) to 5 (Strongly Agree):");
        String[] personalityQuestions = survey.getPersonalityQuestions();
        short[] ratings = new short[personalityQuestions.length];
        try {
            for (int i = 0; i < personalityQuestions.length; i++) {
                System.out.print("\"" + personalityQuestions[i] + "\" " + ": ");
                short rating = Short.parseShort(scanner.nextLine());
                if (rating < 1 || rating > 5)
                    throw new InvalidPersonalityRatingException("Rating must be between 1 and 5.");
                ratings[i] = (short) ((short) 4 * rating);
            }
        } catch (InvalidPersonalityRatingException e) {
            Logger.getInstance().error("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
        return ratings;
    }
}
