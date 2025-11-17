public class Personality {
    private double score;
    private String type;

    public Personality(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void classify() {
        if (score >= 90) {
            type = "Leader";
        } else if (score >= 70) {
            type = "Balanced";
        } else if (score >= 50) {
            type = "Thinker";
        } else {
            type = "None";
        }
    }
}
