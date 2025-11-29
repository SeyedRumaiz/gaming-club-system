import java.util.concurrent.*;

/**
 * A worker class responsible for processing a single participant's response
 */
public class SurveyWorker implements Callable<Boolean> {
    private final SurveyResponse response;
    private final ExecutorService executor;

    /**
     * Creates a worker task responsible for a single survey's response
     * @param response the response received and to be processed
     */
    public SurveyWorker(SurveyResponse response, ExecutorService executor) {
        this.response = response;
        this.executor = executor;
    }

    @Override
    public Boolean call() {
        Logger logger = Logger.getInstance();
        try {
            // Classify
            Future<Personality> personalityFuture = executor.submit(()  -> {
                short totalRating = response.getTotalRating();
                Personality personality = new Personality(totalRating);
                PersonalityClassifier.classify(personality);
                return personality;
            });


            Future<Interest> interestFuture = executor.submit(() -> {
                return new Interest(response.getPreferredGame(), response.getPreferredRole(),
                        response.getSkillLevel());
            });

            // Create participant from the response received
            Personality personality = personalityFuture.get();
            Interest interest = interestFuture.get();

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
