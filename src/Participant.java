public class Participant extends Person {
    private final String ID;
    private final String preferredGame;     // assumed that game and skill level don't change
    private final short skillLevel;
    private final String personalityType;
    private short personalityScore;
    private Role rolePreferred;
    private Personality personality;
    private static int totalParticipants;

    public Participant(String name, String email, String ID, String preferredGame, short skillLevel, String personalityType, short personalityScore) {
        super(name, email);
        this.ID = ID;
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
        this.personalityType = personalityType;
        totalParticipants++;
    }

    public String getID() {
        return ID;
    }

    public static int getTotalParticipants() {
        return totalParticipants;
    }
}
