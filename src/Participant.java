public class Participant extends Person {
    private final String ID;
    private Interest interest;
    private Personality personality;
    private static int totalParticipants;

    public Participant(String name, String email, String ID) {
        super(name, email);
        this.ID = ID;
        totalParticipants++;
    }

    public Interest getInterest() {
        return this.interest;
    }

    public Personality getPersonality() {
        return this.personality;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    public void setPersonality(Personality personality) {
        this.personality = personality;
    }

    public String getID() {
        return this.ID;
    }

    public static int getTotalParticipants() {
        return totalParticipants;
    }
}
