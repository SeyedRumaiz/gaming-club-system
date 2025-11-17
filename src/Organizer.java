import java.io.File;


public class Organizer {

    private static Organizer organizer;

    private Organizer() {

    }

    public static Organizer getInstance() {
        if (organizer == null) {
            organizer = new Organizer();
        }
        return organizer;
    }

    public boolean uploadFile(File file) {
        return false;
    }

    public void defineTeamSize() {

    }

    public boolean initiateFormation() {        // If exporting fails, shows false
        return false;
    }
}
