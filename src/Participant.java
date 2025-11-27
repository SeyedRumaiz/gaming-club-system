/**
 * To represent an individual participant
 */
public class Participant extends User {
    private final String ID;
    private final String email;
    private final Interest interest;
    private Personality personality;
    private static int totalParticipants;

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

    public String getEmail() {
        return this.email;
    }

    public static int getTotalParticipants() {
        return totalParticipants;
    }

    public Interest getInterest() {
        return this.interest;
    }

    @Override
    public String toString() {
        return ID + "," + getInterest().getGame() + "," + getInterest().getRole() + "," +
                getPersonality().getType() + ",";
    }
}
