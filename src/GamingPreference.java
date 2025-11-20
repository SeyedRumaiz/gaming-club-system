public class GamingPreference {
    private final String game;     // assumed that game and skill level don't change
    private final short skillLevel;

    public GamingPreference(String game, short skillLevel) {
        this.game = game;
        this.skillLevel = skillLevel;
    }

    public String getGame() {
        return game;
    }

    public short getSkillLevel() {
        return skillLevel;
    }

    @Override
    public String toString() {
        return "GamingPreference{" +
                "gamePreferred='" + game + '\'' +
                ", skillLevel=" + skillLevel +
                '}';
    }
}
