public class Participant extends Person {
    private final String ID;
    private Preference interest;
    private Personality personality;
    private static int totalParticipants;

    public Participant(String name, String email, String ID) {
        super(name, email);
        this.ID = ID;
        totalParticipants++;
    }

    public String getID() {
        return ID;
    }

    public static int getTotalParticipants() {
        return totalParticipants;
    }
}
