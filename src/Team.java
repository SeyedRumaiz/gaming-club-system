import java.util.Arrays;

public class Team {
    private final int ID;
    private final short size;
    private String name;
    private final Participant[] participants;       // teams cannot be changed

    public Team(int ID, short size, String name) {
        this.ID = ID;
        this.size = size;
        this.name = name;
        participants = new Participant[size];
    }

    public int getID() {
        return ID;
    }

    public short getSize() {
        return size;
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
