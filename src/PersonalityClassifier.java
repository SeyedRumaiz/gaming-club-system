/**
 * Classifies a participant's personality type based on their total personality score
 */
public class PersonalityClassifier {
    /**
     * Classifies a participant's personality based on their score
     * @param personality the personality being classified
     */
    public static void classify(Personality personality) {
        short leaderMin = 90;
        short balancedMin = 70;
        short thinkerMin = 50;
        if (personality.getScore() >= leaderMin) {
            personality.setType("Leader");
        } else if (personality.getScore() >= balancedMin) {
            personality.setType("Balanced");
        } else if (personality.getScore() >= thinkerMin) {
            personality.setType("Thinker");
        }
    }
}
