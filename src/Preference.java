public class Preference {
    private String role;
    private String game;

    public Preference(String role, String game) {
        this.role = role;
        this.game = game;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "Preference [" +
                "role = " + role +
                ", game = " + game +
                ']';
    }
}
