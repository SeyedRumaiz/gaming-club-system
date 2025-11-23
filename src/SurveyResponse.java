public class SurveyResponse {
    private final String ID;
    private String name;
    private String email;
    private short skillLevel;
    private Role preferredRole;
    private String preferredGame;
    private short[] personalityRatings;

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
}
