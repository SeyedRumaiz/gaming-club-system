/**
 * Class to classify personality based on the score
 */
public class PersonalityClassifier {
    private final short leaderMin;
    private final short balancedMin;
    private final short thinkerMin;
    private static PersonalityClassifier instance;

    private PersonalityClassifier() {
        this.leaderMin = 90;
        this.balancedMin = 70;
        this.thinkerMin = 50;
    }

    public static PersonalityClassifier getInstance() {
        if (instance == null) {
            instance = new PersonalityClassifier();
        }
        return instance;
    }

    public void classify(Personality personality) {
        if (personality.getScore() >= leaderMin) {
            personality.setType("Leader");
        } else if (personality.getScore() >= balancedMin) {
            personality.setType("Balanced");
        } else if (personality.getScore() >= thinkerMin) {
            personality.setType("Thinker");
        } else {
            personality.setType("Unknown");
        }
    }
}
