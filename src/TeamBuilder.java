import java.util.*;

/**
 * To build teams using a matching algorithm
 */
public class TeamBuilder {

    private static int GAME_CAP = 2;        // number of participants who can play the same game

    /**
     * Method to form teams based on a list of participant objects using a matching criteria
     * @param participants the list of participant objects
     * @return a list of built teams using a matching criteria
     */
    public List<Team> buildTeams(List<Participant> participants, short teamSize) {

        int totalTeams = (int) Math.ceil(participants.size() / (double) teamSize);
        List<Team> teams = createTeams(totalTeams);

        // 1. Group by skill level
        Map<Integer, List<Participant>> skillGroups = new HashMap<>();

        for (Participant p : participants) {
            int skill = p.getInterest().getSkillLevel();

            if (!skillGroups.containsKey(skill)) {
                skillGroups.put(skill, new ArrayList<>());
            }
            skillGroups.get(skill).add(p);
        }

        // 2. Shuffle groups (multiple valid combinations)
        for (Integer s : skillGroups.keySet()) {
            Collections.shuffle(skillGroups.get(s));
        }

        // 3. Sort keys manually (bubble sort)
        List<Integer> skillLevels = new ArrayList<>(skillGroups.keySet());
        for (int i = 0; i < skillLevels.size(); i++) {
            for (int j = i + 1; j < skillLevels.size(); j++) {
                if (skillLevels.get(j) > skillLevels.get(i)) {
                    int temp = skillLevels.get(i);
                    skillLevels.set(i, skillLevels.get(j));
                    skillLevels.set(j, temp);
                }
            }
        }

        // 4. Build final sorted list
        List<Participant> sorted = new ArrayList<>();
        for (int i = 0; i < skillLevels.size(); i++) {
            List<Participant> g = skillGroups.get(skillLevels.get(i));
            for (int j = 0; j < g.size(); j++) {
                sorted.add(g.get(j));
            }
        }

        // 5. Snake distribution
        int left = 0;
        int right = sorted.size() - 1;
        int index = 0;

        while (left <= right) {

            teams.get(index % totalTeams).addParticipant(sorted.get(left));
            left++;

            if (left > right) break;

            teams.get(index % totalTeams).addParticipant(sorted.get(right));
            right--;

            index++;
        }

        return teams;
    }


    /**
     * To check if the game count is greater than the game cap
     * @param team the team containing participants with their preferred games
     * @param game the game to be added via a participant
     * @return true if the count is less than the game cap
     */
    private boolean canAddGame(Team team, String game) {
        int count = 0;
        for (Participant participant : team.getParticipants()) {
            if (participant.getInterest().getGame().equals(game)) {
                count++;
            }
        }
        return count < GAME_CAP;
    }

    /**
     * Method to create empty list of teams
     * @param totalTeams the total number of teams possible
     * @return a list of empty teams
     */
    private List<Team> createTeams(int totalTeams) {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < totalTeams; i++) {
            teams.add(new Team(i+1));
        }
        return teams;
    }

    /**
     * Method to sort participants in descending order by skill level
     * @param participants the participants to be sorted
     * @return a list of participants with their skill levels in descending order
     */
    private List<Participant> sortParticipants(List<Participant> participants) {
        Map<Short, List<Participant>> skillMap = new HashMap<>();   // For each skill level and its list of participants

        for (Participant p : participants) {
            short skillLevel = p.getInterest().getSkillLevel(); // get each participant's skill level

            if (!skillMap.containsKey(skillLevel)) {
                skillMap.put(skillLevel, new ArrayList<>());        // add the skill level if its not in the map
            }
            skillMap.get(skillLevel).add(p);        // Normally add if skill level exists/doesn't exist
        }

        // Get all skill levels
        List<Short> skillLevels = new ArrayList<>();
        skillLevels.addAll(skillMap.keySet());

        // Sort skill by descending
        skillLevels.sort(Collections.reverseOrder());

        // Insert participants to a list
        List<Participant> sorted = new ArrayList<>();

        for (Short skillLevel : skillLevels) {
            List<Participant> group = skillMap.get(skillLevel); // add each group according to their skill level
            sorted.addAll(group);
        }
        return sorted;
    }

    /**
     * Method to distribute the skill levels
     * @param teams
     * @param participants
     */
    private void distributeSkill(List<Team> teams, List<Participant> participants, int numberOfTeams) {
        int teamIndex = 0;
        int start = 0;
        int end = participants.size() - 1;

        while (start <= end) {       // converging to the middle
            if (start == end) {     // If the number of teams is even
                teams.get(teamIndex % numberOfTeams).addParticipant(participants.get(start));
                        // modulus to cycle through the teams
                break;
            }
            teams.get(teamIndex % numberOfTeams).addParticipant(participants.get(start));
            teams.get(teamIndex % numberOfTeams).addParticipant(participants.get(end));

            start++;
            end--;
            teamIndex++;
        }
    }
}
