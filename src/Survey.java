import java.time.LocalDate;

public class Survey {
    private final int ID;
    private final LocalDate creationDate;
    private final LocalDate expirationDate;
    private final String[] personalityQuestions;
    private final String[] interestQuestions;
    private SurveyController controller;

    public Survey(int ID, LocalDate creationDate, LocalDate expirationDate) {
        this.ID = ID;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        personalityQuestions = new String[5];
        interestQuestions = new String[3];
        initializePersonalityQuestions();
        initializeInterestQuestions();
        controller = new SurveyController();
        controller.setSurvey(this);
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

    public int getID() {
        return ID;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
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
}
