/**
 * Represents an individual participant's role, gaming or sports interest
 */
public class Interest {
    private final String game;
    private final Role role;
    private final short skillLevel;

    public Interest(String game, Role role, short skillLevel) {
        this.game = game;
        this.role = role;
        this.skillLevel = skillLevel;
    }

    // Getters
    public String getGame() {
        return game;
    }

    public Role getRole() {
        return role;
    }

    public short getSkillLevel() {
        return skillLevel;
    }
}
