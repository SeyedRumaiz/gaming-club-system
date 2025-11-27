import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents a survey taken by a participant
 */
public class Survey {
    private final String[] personalityQuestions;
    private final String[] interestQuestions;
    private SurveyController controller;
    private SurveyResponse response;

    public Survey() {
        personalityQuestions = new String[5];
        interestQuestions = new String[3];
        initController();
        initializePersonalityQuestions();
        initializeInterestQuestions();
    }

    public void initializePersonalityQuestions() {
        String[] questions = {
                "I enjoy taking the lead and guiding others during group activities.",
                "I prefer analyzing situations and coming up with strategic solutions.",
                "I work well with others and enjoy collaborative teamwork.",
                "I am calm under pressure and can help maintain team morale.",
                "I like making quick decisions and adapting in dynamic situations."
        };
        System.arraycopy(questions, 0, personalityQuestions, 0, personalityQuestions.length);
    }

    public void initializeInterestQuestions() {
        String[] questions = {
                "Preferred role",
                "Preferred game",
                "Skill level (1–10)"
        };
        System.arraycopy(questions, 0, interestQuestions, 0, interestQuestions.length);
    }

    public String[] getPersonalityQuestions() {
        return personalityQuestions;
    }

    public String[] getInterestQuestions() {
        return interestQuestions;
    }

    public SurveyController getController() {
        return controller;
    }

    private void initController() {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        controller = new SurveyController(this, scanner, executorService);
    }
}
