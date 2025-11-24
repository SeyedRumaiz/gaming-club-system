import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        participants.add(participant);
    }

    public int getNoOfParticipants() {
        return participants.size();
    }

    public boolean isFull() {
        return getNoOfParticipants() == size;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public int getAverageSkill() {
        int sum = 0;
        for (Participant participant : participants) {
            if (participant != null) {
                sum += participant.getInterest().getSkillLevel();
            }
        }
        return sum / participants.size();
    }

    @Override
    public String toString() {
        return "Team [" +
                "ID = " + ID +
                ", size = " + size +
                ", name = " +
                ", participants = " + participants +
                ']';
    }
}
