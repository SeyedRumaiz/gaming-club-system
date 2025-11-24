import java.util.*;

/**
 * Class to form teams based on Role diversity
 */
public class RoleStrategy implements MatchingStrategy {

    private int maxRolePerTeam;

    public RoleStrategy(int maxRolePerTeam) {
        this.maxRolePerTeam = maxRolePerTeam;
    }

    @Override
    public List<Team> formTeams(List<Team> teams, short size) {
        Map<Role, List<Participant>> participantsPerRole = getParticipantsByRole(teams);
        List<Team> newTeams = new ArrayList<>(teams.size());
        return null;
    }

    // To group participants by role
    private Map<Role, List<Participant>> getParticipantsByRole(List<Team> teams) {
        // Get participants by role
        Map<Role, List<Participant>> participantsByRole = new HashMap<>();

        Set<Role> roles = new HashSet<>(Arrays.asList(Role.values()));

        for (Role role : roles) {
            List<Participant> participants = new ArrayList<>();
            for (Team team : teams) {
                for (Participant participant : team.getParticipants()) {
                    if (participant.getInterest().getRole() == role) {
                        participants.add(participant);
                    }
                }
            }
            participantsByRole.put(role, participants);
        }
        return participantsByRole;
    }
}
