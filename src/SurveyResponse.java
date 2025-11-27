/**
 * Represents all data collected from a single participant's survey response
 */
public class SurveyResponse {
    private final String ID;
    private final String name;
    private final String email;
    private final short skillLevel;
    private final Role preferredRole;
    private final String preferredGame;
    private final short[] personalityRatings;

    public SurveyResponse(String ID, String name, String email, short skillLevel, Role preferredRole, String preferredGame, short[] personalityRatings) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.skillLevel = skillLevel;
        this.preferredRole = preferredRole;
        this.preferredGame = preferredGame;
        this.personalityRatings = personalityRatings;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public short getSkillLevel() {
        return skillLevel;
    }

    public Role getPreferredRole() {
        return preferredRole;
    }

    public String getPreferredGame() {
        return preferredGame;
    }

    public short[] getPersonalityRatings() {
        return personalityRatings;
    }

    public short getTotalRating() {
        short total = 0;
        for (short rating : personalityRatings) {
            total += rating;
        }
        return total;
    }
}
