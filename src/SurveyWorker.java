import java.util.concurrent.*;

/**
 * A worker class responsible for processing a single participant's response
 */
public class SurveyWorker implements Callable<Boolean> {
    private final SurveyResponse response;

    /**
     * Creates a worker task responsible for a single survey's response
     * @param response the response received and to be processed
     */
    public SurveyWorker(SurveyResponse response) {
        this.response = response;
    }

    @Override
    public Boolean call() {
        Logger logger = Logger.getInstance();
        try {
            // Classify
            short totalRating = response.getTotalRating();
            Personality personality = new Personality(totalRating);
            PersonalityClassifier.classify(personality);

            // Create participant from the response received
            Interest interest = new Interest(response.getPreferredGame(), response.getPreferredRole(),
                    response.getSkillLevel());

            Participant participant = new Participant(response.getName(),
                    response.getID(), response.getEmail(), interest, personality, response);

            FileHandler.saveParticipant(participant);   // save the participant to the file
            GamingClubSystem.getInstance().addParticipant(participant); // add the participant to the system

            logger.info("Processed and added participant: " + participant.getID());
            return true;
        } catch (Exception e) {
            logger.error("Error processing response " + response.getID() + ": " + e.getMessage());
            return false;
        }
    }
}
