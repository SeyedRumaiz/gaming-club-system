import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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

            String name = getName(scanner).trim();

            String email = getEmail(scanner).trim();

            short[] personalityScores = getPersonalityInfo(scanner);

            short totalScore = getTotalScore(personalityScores);

            Personality personality = new Personality(totalScore);

            PersonalityClassifier.getInstance().classify(personality);

            System.out.println("Your personality score is: " + personality.getScore());
            System.out.println("You are a: " + personality.getType());

            Interest interest = getInterestInfo(scanner);

            SurveyResponse response = new SurveyResponse(
                    participantId,
                    name,
                    email,
                    interest.getSkillLevel(),
                    interest.getRole(),
                    interest.getGame(),
                    personalityScores
            );

            SurveyWorker worker = new SurveyWorker(response);
            Future<Boolean> future = executor.submit(worker);

            boolean valid;
            try {
                valid = future.get();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }

            if (!valid) {
                System.out.println("Validation failed.");
                return;
            }

            Participant participant = new Participant(participantId, name, email);
            participant.setInterest(interest);
            participant.setPersonality(personality);

            // Finally add them to the system
            GamingClubSystem.getInstance().addParticipant(participant);
            break;
        }

        System.out.println("Thank you for completing the survey.");
    }

    private boolean isValid(boolean valid) {
        return valid;
    }

    private short getTotalScore(short[] scores) {
        short totalScore = 0;
        for (short score : scores) {
            totalScore += score;
        }
        return totalScore;
    }

    private String getName(Scanner scanner) {
        System.out.print("Enter your name: ");
        return scanner.nextLine().trim();
    }

    private String getEmail(Scanner scanner) {
        System.out.print("Enter your email: ");
        return scanner.nextLine().trim();
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
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
        return ratings;
    }

    private Interest getInterestInfo(Scanner scanner) {
        System.out.println("--- Interest questions ---");
        System.out.println("Available roles: " + Arrays.toString(Role.values()));
        System.out.print("What is your preferred role?: ");

        Role role = Role.valueOf(scanner.nextLine().toUpperCase().trim());

        System.out.println("Available Games: " + GameRegistry.getAllowedGames());
        System.out.print("What is your preferred game?: ");

        String game = scanner.nextLine();
        System.out.print("What is your skill level?: ");

        short level = Short.parseShort(scanner.nextLine());

        return new Interest(game, role, level);
    }
}
