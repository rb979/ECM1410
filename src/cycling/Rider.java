package cycling;

/**
 * A class used to create an instance of a rider.
 * These riders will belong to an instance of a team class.
 *
 * @author Ryan Butler and Hugo Blanco
 *
 */

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class Rider {
    private static final AtomicInteger idGenerator = new AtomicInteger();
    static final Map<Integer, Rider> ridersById = new HashMap<>();
    private static final Map<Integer, List<Integer>> ridersByTeamId = new HashMap<>();

    private final int id;
    private final int teamId;
    private final String name;
    private final int yearOfBirth;

    private Rider(int teamId, String name, int yearOfBirth) {
        this.id = idGenerator.incrementAndGet();
        this.teamId = teamId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public static void resetRiders() {
        ridersById.clear(); // Clears the map of riders
        ridersByTeamId.clear(); // Clears the map of riders by team ID
        idGenerator.set(0); // Resets the ID generator
    }

    public static int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        if (!Team.teamsById.containsKey(teamID)) {
            throw new IDNotRecognisedException("Team ID does not match any team in the system.");
        }
        if (name == null || name.trim().isEmpty() || yearOfBirth < 1900) {
            throw new IllegalArgumentException("Invalid name or year of birth.");
        }

        Rider newRider = new Rider(teamID, name, yearOfBirth);
        ridersById.put(newRider.id, newRider);
        ridersByTeamId.computeIfAbsent(teamID, k -> new ArrayList<>()).add(newRider.id);

        return newRider.id;
    }

    public static void removeRider(int riderId) throws IDNotRecognisedException {
        if (!ridersById.containsKey(riderId)) {
            throw new IDNotRecognisedException("Rider ID does not match any rider in the system.");
        }

        Rider riderToRemove = ridersById.remove(riderId);
        List<Integer> teamRiders = ridersByTeamId.get(riderToRemove.teamId);
        if (teamRiders != null) {
            teamRiders.remove(Integer.valueOf(riderId));
            if (teamRiders.isEmpty()) {
                ridersByTeamId.remove(riderToRemove.teamId);
            }
        }

        // Additional logic to remove rider's results from races would be required here
    }

    public static int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
        if (!Team.teamsById.containsKey(teamId)) {
            throw new IDNotRecognisedException("Team ID does not match any team in the system.");
        }

        List<Integer> teamRiders = ridersByTeamId.getOrDefault(teamId, new ArrayList<>());
        return teamRiders.stream().mapToInt(i -> i).toArray();
    }

    public static boolean verifyRiderExists(int riderId) {
        return ridersById.containsKey(riderId);
    }

    // Additional getters and methods as needed...
}
