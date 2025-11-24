import java.util.Arrays;

public class Team {
    private final int ID;           // Each team is unique and the id cannot be changed
    private static short size;       // Not gonna have alot of team members so short, wont change so final
    private final Participant[] participants;       // teams cannot be changed

    public Team(int ID) {
        this.ID = ID;
        participants = new Participant[size];
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
        for (int i = 0; i < participants.length; i++) {
            if (participants[i] == null) {
                participants[i] = participant;
            }
        }
    }

    public short getNoOfParticipants() {
        short size = 0;
        for (int i = 0; i < participants.length; i++) {
            if (participants[i] != null) {
                size++;
            }
        }
        return size;
    }

    public boolean isFull() {
        return getNoOfParticipants() == size;
    }

    public Participant[] getParticipants() {
        return participants;
    }

    public int getAverageSkill() {
        int sum = 0;
        for (Participant participant : participants) {
            if (participant != null) {
                sum += participant.getInterest().getSkillLevel();
            }
        }
        return sum / participants.length;
    }

    @Override
    public String toString() {
        return "Team [" +
                "ID = " + ID +
                ", size = " + size +
                ", name = " +
                ", participants = " + Arrays.toString(participants) +
                ']';
    }
}
