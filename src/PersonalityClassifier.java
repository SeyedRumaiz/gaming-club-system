public class PersonalityClassifier {
    private static short leaderMin = 90;
    private static  short balancedMin = 70;
    private static short thinkerMin = 50;

    public static Personality classify(short score) {

        Personality personality = new Personality(score);
        if (score >= leaderMin) {
            personality.setType("Leader");
        } else if (score >= balancedMin) {
            personality.setType("Balanced");
        } else if (score >= thinkerMin) {
            personality.setType("Thinker");
        } else {
            personality.setType("Unknown");
        }
        return personality;
    }
}
