public class PersonalityClassifier {
    private short leaderMin;
    private short balancedMin;
    private short thinkerMin;

    public PersonalityClassifier() {
        this.leaderMin = 90;
        this.balancedMin = 70;
        this.thinkerMin = 50;
    }

    public Personality classify(short score) {
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
