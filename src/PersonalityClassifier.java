public class PersonalityClassifier {
    private short leaderMin;
    private short balancedMin;
    private short thinkerMin;

    public PersonalityClassifier() {
        this.leaderMin = 90;
        this.balancedMin = 70;
        this.thinkerMin = 50;
    }

    public Personality classify(short[] score) {
        short totalScore = 0;
        for (short value : score) {
            totalScore += value;
        }

        Personality personality = new Personality(totalScore);
        if (totalScore >= leaderMin) {
            personality.setType("Leader");
        } else if (totalScore >= balancedMin) {
            personality.setType("Balanced");
        } else if (totalScore >= thinkerMin) {
            personality.setType("Thinker");
        } else {
            personality.setType("Unknown");
        }
        return personality;
    }
}
