/**
 * Represents an organizer in the system who can perform team-related operations
 */
public class Organizer extends User {

    public Organizer(String name) {
        super(name);
    }

    /**
     * To allow the Organizer to upload a file into the system
     * @param fileName path of the file being uploaded
     */
    public void uploadFile(String fileName) {
        FileHandler.loadParticipants(fileName);
    }

    /**
     * To allow the Organizer to initiate ethe team formation process
     */
    public void initiateTeamFormation() {
        GamingClubSystem.getInstance().initiateFormation();
    }
}
