import java.util.Arrays;

public class Team {
    private final int ID;           // Each team is unique and the id cannot be changed
    private static short size;       // Not gonna have alot of team members so short, wont change so final
    private String name;
    private final Participant[] participants;       // teams cannot be changed

    public Team(int ID, String name) {
        this.ID = ID;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "ID=" + ID +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", participants=" + Arrays.toString(participants) +
                '}';
    }
}
