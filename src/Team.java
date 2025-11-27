import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team consisting of participants in an event
 */
public class Team {
    private final int ID;           // Each team is unique and the id cannot be changed
    private static short size;       // Not gonna have alot of team members so short, wont change so final
    private final List<Participant> participants;       // teams cannot be changed

    public Team(int ID) {
        this.ID = ID;
        participants = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public short getSize() {
        return size;
    }

    public static void setSize(short size) {
        Team.size = size;
    }

    public void addParticipant(Participant participant) {
        if (isFull()) {
            return;
        }
        participants.add(participant);
    }

    public boolean isFull() {
        return participants.size() == size;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

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

    public String getGame() {
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

    public int countGame(String game) {
        int count = 0;
        for (Participant participant : participants) {
            if (participant.getInterest().getGame().equals(game)) {
                count++;
            }
        }
        return count;
    }

    public boolean hasRole(Role role) {
        for (Participant participant : participants) {
            if (participant.getInterest().getRole() == role) {
                return true;
            }
        }
        return false;
    }

    public int countPersonality(String personality) {
        int count = 0;
        for (Participant participant : participants) {
            if (participant.getPersonality().getType().equals(personality)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return ID +  "," + getIds() + "," +
                getRoles() + "," + getGame() + "," + getPersonalities() + "," + getAverageSkill();
    }
}
