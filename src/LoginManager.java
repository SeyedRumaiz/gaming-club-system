public class LoginManager {
    public boolean confirmPassword(String password, GamingClubSystem system) {
        return password.equals(system.getPassword());
    }
}
