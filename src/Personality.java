/**
 * Represents a participant's personality profile
 */
public class Personality {
    private final double score;
    private String type;

    public Personality(double score) {
        this.score = score;
    }

    public double getSCORE() {
        return score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
