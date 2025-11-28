import java.util.*;

public class BalancedTeamBuilder extends TeamBuilderTemplate {
    private final int GAME_CAP;
    private final int MIN_ROLES;
    private final double TARGET_AVERAGE;
    private final Team team;
    private final List<Participant> chunk;

    public BalancedTeamBuilder(int GAME_CAP, int MIN_ROLES, double TARGET_AVERAGE, Team team, List<Participant> chunk) {
        this.GAME_CAP = GAME_CAP;
        this.MIN_ROLES = MIN_ROLES;
        this.TARGET_AVERAGE = TARGET_AVERAGE;
        this.team = team;
        this.chunk = chunk;
    }

    /**
     *
     */
    @Override
    protected void applyHardConstraints() {
        // Game cap
        Map<String, Integer> gameCounts = new HashMap<>();
        for (Participant p : chunk) {
            if (team.isFull()) {    // do not consider teams that are full
                break;
            }
            String preferredGame = p.getInterest().getGame();
            int count = gameCounts.getOrDefault(preferredGame, 0);  // starts from 0

            if (count < GAME_CAP) {
                team.addParticipant(p); // if the count is low, then add this participant to the team
                gameCounts.put(preferredGame, count+1);
            }
        }

        // Role
        Set<Role> roles = new HashSet<>();
        for (Participant p : chunk) {
            if (team.isFull()) {
                break;
            }
            Role role = p.getInterest().getRole();
            if (!roles.contains(role)) {
                roles.add(role);
                team.addParticipant(p);
                if (roles.size() >= MIN_ROLES) {    // terminate if there are minimum amount of distinct roles
                    break;
                }
            }
        }
    }

    /**
     *
     */
    @Override
    protected void applySoftConstraints() {
        // Personality mix
        Participant leader = null;  // there should be ideally 1 leader
        List<Participant> thinkers = new ArrayList<>();
        List<Participant> balanced = new ArrayList<>();

        for (Participant p : chunk) {
            switch (p.getPersonality().getType()) {
                case "Leader" -> leader = p;
                case "Thinker" -> thinkers.add(p);
                case "Balanced" -> balanced.add(p);
            }
        }

        if (leader !=null && !team.isFull()) {
            team.addParticipant(leader);
        }

        for (int i = 0; i < thinkers.size() && i < 2 && team.isFull(); i++) {
            team.addParticipant(thinkers.get(i));
        }

        for (Participant p : balanced) {    // add remaining balanced participants
            if (!team.isFull()) {
                team.addParticipant(p);
            }
        }

        // Balance skill by closeness to the target average, participants closer to the average appear first
        chunk.sort((p1, p2) -> Double.compare(Math.abs(p1.getInterest().getSkillLevel() - TARGET_AVERAGE),
                Math.abs(p2.getInterest().getSkillLevel() - TARGET_AVERAGE)));

        // Add participants not already in the team
        for (Participant p : chunk) {
            if (!team.isFull() && !team.getParticipants().contains(p)) {
                team.addParticipant(p);
            }
        }
    }

    /**
     * @param participants
     * @param teams
     * @return
     */
    @Override
    protected List<Team> finalizeTeams(List<Participant> participants, List<Team> teams) {
        return List.of();
    }

    public Team buildSingleTeam() {
        List<Team> teams = new ArrayList<>();
        teams.add(team);
        buildTeams(chunk, teams);

        List<Team> result = buildTeams(chunk, teams);
        return teams.get(0);
    }
}
