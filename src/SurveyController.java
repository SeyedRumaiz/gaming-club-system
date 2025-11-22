import java.util.Arrays;
import java.util.Scanner;

public class SurveyController {
    private Survey survey;

    public void startSurvey(Scanner scanner) {
        System.out.println("--- Welcome to the Survey! ---");
        System.out.println("This should take a couple of minutes...");

        String participantId = "P" + (Participant.getTotalParticipants() + 1);      // Continue from the left off ID

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        completePersonalityQuestions(scanner);
        completeInterestQuestions(scanner);
    }

    private void completePersonalityQuestions(Scanner scanner) {
        System.out.println("-- Personality questions --");
        System.out.println("Please rate each statement from 1 (Strongly Disagree) to 5 (Strongly Agree):");
        String[] personalityQuestions = survey.getPersonalityQuestions();
        try {
            for (String question : personalityQuestions) {
                System.out.print("\" " + question + "\" " + ": ");
                int rating = Integer.parseInt(scanner.nextLine());
                if (rating < 1 || rating > 5) throw new InvalidPersonalityRatingException("Rating must be between 1 and 5.");
            }
        } catch (InvalidPersonalityRatingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void completeInterestQuestions(Scanner scanner) {
        System.out.println("-- Interest questions --");
        System.out.println("Available roles: " + Arrays.toString(Role.values()));
        System.out.println("What is your preferred role?: ");

        Role role = Role.valueOf(scanner.nextLine());

        System.out.println("What is your preferred game?: ");

        String game = scanner.nextLine();
        System.out.println("What is your skill level?: ");

        short level = Short.parseShort(scanner.nextLine());

    }

    private void processSurveyResults() {

    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
