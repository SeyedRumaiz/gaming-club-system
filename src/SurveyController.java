import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SurveyController implements Callable<Boolean> {
    private Survey survey;
    private Scanner scanner;
    private PersonalityClassifier classifier;
    private ExecutorService executor;

    public SurveyController(Survey survey, Scanner scanner, PersonalityClassifier classifier, ExecutorService executor) {
        this.survey = survey;
        this.scanner = scanner;
        this.classifier = classifier;
        this.executor = executor;
    }

    public void startSurvey() throws Exception {
        call();
    }

    @Override
    public Boolean call() throws Exception {
        System.out.println("--- Welcome to the Survey! ---");
        System.out.println("This should take a couple of minutes...");

        String participantId = "P" + (Participant.getTotalParticipants() + 1);      // Continue from the left off ID

        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        short[] personalityScores = completePersonalityQuestions(scanner);

        Personality personality = new PersonalityClassifier().classify(personalityScores);
        Interest interest = completeInterestQuestions(scanner);

        SurveyResponse response = new SurveyResponse(
                participantId,
                name,
                email,
                interest.getSkillLevel(),
                interest.getRole(),
                interest.getGame(),
                personalityScores
        );

        SurveyWorker worker = new SurveyWorker(response, executor);
        Future<Boolean> future = executor.submit(worker);

        boolean valid;
        try {
            valid = future.get();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        if (!valid) {
            System.out.println("Validation failed.");
            return false;
        }

        Participant participant = new Participant(participantId, name, email);
        participant.setInterest(interest);
        participant.setPersonality(personality);
        System.out.println("Your personality score is: " + personality.getScore());
        System.out.println("You are a: " + personality.getType());

        System.out.println("Thank you for completing the survey.");
        return true;
    }

    private short[] completePersonalityQuestions(Scanner scanner) {
        System.out.println("-- Personality questions --");
        System.out.println("Please rate each statement from 1 (Strongly Disagree) to 5 (Strongly Agree):");
        String[] personalityQuestions = survey.getPersonalityQuestions();
        short[] ratings = new short[personalityQuestions.length];
        try {
            for (int i = 0; i < personalityQuestions.length; i++) {
                System.out.print("\" " + personalityQuestions[i] + "\" " + ": ");
                short rating = Short.parseShort(scanner.nextLine());
                if (rating < 1 || rating > 5)
                    throw new InvalidPersonalityRatingException("Rating must be between 1 and 5.");
                ratings[i] = (short) ((short) 4 * rating);
            }
        } catch (InvalidPersonalityRatingException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return ratings;
    }

    private Interest completeInterestQuestions(Scanner scanner) {
        System.out.println("-- Interest questions --");
        System.out.println("Available roles: " + Arrays.toString(Role.values()));
        System.out.println("What is your preferred role?: ");

        Role role = Role.valueOf(scanner.nextLine());

        System.out.println("What is your preferred game?: ");

        String game = scanner.nextLine();
        System.out.println("What is your skill level?: ");

        short level = Short.parseShort(scanner.nextLine());

        return new Interest(game, role, level);
    }
}
