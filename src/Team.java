import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team consisting of participants in an event
 */
public class Team {
    private final int ID;           // Each team is unique and the id cannot be changed
    private static short size;       // Will not have many participants, and won't change
    private final List<Participant> participants;       // teams cannot be changed

    public Team(int ID) {
        this.ID = ID;
        participants = new ArrayList<>();
    }

    public int getID() {
        return this.ID;
    }

    public static short getSize() {
        return size;    // seq 2.3
    }

    public static void setSize(short size) {
        Team.size = size;
    }

    /**
     * To add a participant to a team if it isn't full
     * @param participant the participant being added
     */
    public void addParticipant(Participant participant) {
        if (isFull()) {
            return;
        }
        participants.add(participant);
    }

    /**
     * To check if the team is full
     * @return true if the team is full
     */
    public boolean isFull() {
        return participants.size() == size;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * To get the average skill rating for a team
     * @return the average skill rating
     */
    public double getAverageSkill() {
        double sum = 0;
        for (Participant participant : participants) {
            if (participant != null) {
                sum += participant.getInterest().getSkillLevel();
            }
        }
        return sum / participants.size();
    }

    public String getIds() {
        String ids = "";
        for (Participant participant : participants) {
            ids += participant.getID() + " ";
        }
        return "( " + ids + ")";
    }

    public String getRoles() {
        String roles = "";
        for (Participant participant : participants) {
            roles += participant.getInterest().getRole() + " ";
        }
        return "( " + roles + ")";
    }

    public String getGames() {
        String games = "";
        for (Participant participant : participants) {
            games += participant.getInterest().getGame() + " ";
        }
        return "( " + games + ")";
    }

    public String getPersonalities() {
        String personalities = "";
        for (Participant participant : participants) {
            personalities += participant.getPersonality().getType() + " ";
        }
        return "( " + personalities + ")";
    }

    @Override
    public String toString() {
        return ID +  "," + getIds() + "," +
                getRoles() + "," + getGames() + "," + getPersonalities() + "," + getAverageSkill();
    }
}
