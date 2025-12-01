import java.util.ArrayList;
import java.util.List;
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
    private List<SurveyResponse> responses;

    public Survey() {
        personalityQuestions = new String[5];
        interestQuestions = new String[3];
        responses = new ArrayList<>();
        initController();
        initializePersonalityQuestions();
        initializeInterestQuestions();
    }

    /**
     * To initialize all the personality questions
     */
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

    /**
     * To initialize all the interest questions
     */
    public void initializeInterestQuestions() {
        String[] questions = {
                "Preferred role",
                "Preferred game",
                "Skill level (1–10)"
        };
        System.arraycopy(questions, 0, interestQuestions, 0, interestQuestions.length);
    }

    public String[] getPersonalityQuestions() {
        return this.personalityQuestions;
    }

    public String[] getInterestQuestions() {
        return this.interestQuestions;
    }

    public SurveyController getController() {
        return this.controller;
    }

    public List<SurveyResponse> getResponses() {
        return this.responses;
    }

    private void initController() {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        controller = new SurveyController(this, scanner, executorService);
    }

    public SurveyResponse addResponse(String ID, String name, String email,
                                      short skillLevel,
                                      Role preferredRole, String preferredGame,
                                      short[] ratings) {
        // seq 7.3.1
        SurveyResponse response = new SurveyResponse(ID, name, email, skillLevel, preferredRole, preferredGame, ratings);
        responses.add(response);
        return response;    // seq 7.3.2
    }
}
