import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SurveyWorker implements Callable<Boolean> {
    private final SurveyResponse response;

    public SurveyWorker(SurveyResponse response) {
        this.response = response;
    }

    @Override
    public Boolean call() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Callable<Boolean>> tasks = new ArrayList<>();

        // Validate role
        tasks.add(() -> Validation.validateRole(response.getPreferredRole()));

        // Validate skill level
        tasks.add(() -> Validation.validateSkillLevel(response.getSkillLevel()));

        // Validate game
        tasks.add(() -> Validation.validateGame(response.getPreferredGame()));

        //Validate personality scores
        tasks.add(() -> Validation.validatePersonalityRatings(response.getPersonalityRatings()));

        // Run all validations in parallel
        List<Future<Boolean>> futures = executor.invokeAll(tasks);

        for (Future<Boolean> future : futures) {
            if (!future.get()) {
                return false;       // whole survey fails
            }
        }
        return true;
    }
}
