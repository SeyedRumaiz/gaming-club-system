import java.util.*;

public class BalancedTeamBuilder extends TeamBuilder {

    public BalancedTeamBuilder(int GAME_CAP, int MIN_ROLES, double TARGET_AVERAGE,
                               Team team, List<Participant> chunk) {
        super(GAME_CAP, MIN_ROLES, TARGET_AVERAGE, chunk, team);
    }

    @Override
    protected Map<String, List<Participant>> applyGameConstraint(List<Participant> pool) {
        Map<String, List<Participant>> gameBuckets = new HashMap<>();   // for parallel computation

        for (Participant p : pool) {
            String game = p.getInterest().getGame();
            if (!gameBuckets.containsKey(game)) {
                gameBuckets.put(game, new ArrayList<>());
            }
            List<Participant> list = gameBuckets.get(game);

            if (list.size() >= getGAME_CAP()) {
                game = "OVERFLOW";  // temporary
                if (!gameBuckets.containsKey(game)) {
                    gameBuckets.put(game, new ArrayList<>());
                }
            }
            gameBuckets.get(game).add(p);
        }

        // Randomize each bucket
        for (List<Participant> list : gameBuckets.values()) {
            Collections.shuffle(list);
        }
        return gameBuckets;
    }

    @Override
    protected Map<Role, List<Participant>> applyRoleConstraint(List<Participant> pool) {
        Map<Role, List<Participant>> roleBuckets = new HashMap<>();

        for (Participant p : pool) {
            Role role = p.getInterest().getRole();
            if (!roleBuckets.containsKey(role)) {
                roleBuckets.put(role, new ArrayList<>());
            }
            roleBuckets.get(role).add(p);   // add the participant with their role
        }

        // Randomize participants in each role
        for (List<Participant> list : roleBuckets.values()) {
            Collections.shuffle(list);
        }

        Set<Role> addedRoles = new HashSet<>();
        List<Role> allRoles =  new ArrayList<>(roleBuckets.keySet());
        Collections.shuffle(allRoles);  // random order for roles

        for (Role role : allRoles) {
            List<Participant> roleList = roleBuckets.get(role);
            for (Participant p : roleList) {
                if (!getTEAM().getParticipants().contains(p) && !getTEAM().isFull()) {
                    getTEAM().addParticipant(p);
                    addedRoles.add(role);
                }
                if (addedRoles.size() >= getMIN_ROLES()) {
                    break;      // break when minimum is there
                }
            }
            if (addedRoles.size() >= getMIN_ROLES()) {
                break;
            }
        }
        return roleBuckets;
    }

    @Override
    protected Map<String, List<Participant>> applyPersonalityMix(List<Participant> pool) {
        Map<String, List<Participant>> personalityBuckets = new HashMap<>();
        personalityBuckets.put("Leader", new ArrayList<>());
        personalityBuckets.put("Thinker", new ArrayList<>());
        personalityBuckets.put("Balanced", new ArrayList<>());
        for (Participant p : pool) {
            personalityBuckets.get(p.getPersonality().getType()).add(p);
        }

        // Randomize each list of participants
        for (List<Participant> list : personalityBuckets.values()) {
            Collections.shuffle(list);
        }

        return personalityBuckets;
    }

    @Override
    protected List<Participant> applySkillBalance(List<Participant> pool) {
        List<Participant> skillSorted = new ArrayList<>(pool);
        skillSorted.sort(Comparator.comparingDouble(p -> Math.abs(p.getInterest().getSkillLevel() - getTARGET_AVERAGE())));

        // Randomly shuffle participants with the same skill level
        for (int i = 0; i < skillSorted.size()-1; i++) {
            if (Math.abs(skillSorted.get(i).getInterest().getSkillLevel() - getTARGET_AVERAGE()) ==
            Math.abs(skillSorted.get(i+1).getInterest().getSkillLevel() - getTARGET_AVERAGE())) {
                Collections.swap(skillSorted, i, i+1);
            }
        }

        return skillSorted;
    }
}
