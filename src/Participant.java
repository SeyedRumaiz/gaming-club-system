/**
 * To represent an individual participant
 */
public class Participant extends User {
    private final String ID;
    private String email;
    private final Interest interest;
    private final Personality personality;
    private static int totalParticipants;
    private SurveyResponse surveyResponse;

    public Participant(String name, String ID, String email, Interest interest, Personality personality, SurveyResponse surveyResponse) {
        super(name);
        this.ID = ID;
        this.email = email;
        this.interest = interest;
        this.personality = personality;
        this.surveyResponse = surveyResponse;
        totalParticipants++;
    }

    public Participant(String name, String ID, String email, Interest interest, Personality personality) {
        super(name);
        this.ID = ID;
        this.email = email;
        this.interest = interest;
        this.personality = personality;
        totalParticipants++;
    }

    public Personality getPersonality() {
        return this.personality;
    }

    public String getID() {
        return this.ID;
    }

    public static int getTotalParticipants() {
        return totalParticipants;
    }

    public Interest getInterest() {
        return this.interest;
    }

    public SurveyResponse getResponse() {
        return this.surveyResponse;
    }

    /**
     * Method to store the participant details to a file
     * @return a formatted string to store in a CSV file
     */
    public String getDetails() {
        return ID + "," + getName() + "," + email + "," + interest.getGame() + "," +
                + interest.getSkillLevel() + "," + interest.getRole().name().substring(0, 1).toUpperCase()
                + interest.getRole().name().substring(1).toLowerCase() +
                "," + (int) personality.getScore() + "," + personality.getType();
    }

    @Override
    public String toString() {
        return ID + "," + getInterest().getGame() + "," +
                getInterest().getRole().name().substring(0, 1).toUpperCase()
                + interest.getRole().name().substring(1).toLowerCase() + "," +
                getPersonality().getType() + ",";
    }
}
