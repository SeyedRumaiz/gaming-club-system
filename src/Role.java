import java.util.Arrays;

/**
 * Represents the fixed roles available in both gaming and sports context
 */
public enum Role {
    STRATEGIST, ATTACKER, DEFENDER, SUPPORTER, COORDINATOR;

    public static void displayAllRoles() {
        System.out.println("Available Roles: " + Arrays.toString(Role.values()));
    }
}
