package cycling;

/**
 * A class used to create an instance of a rider.
 * These riders will belong to an instance of a team class.
 * 
 * Authors: Ryan Butler and Hugo Blanco
 */
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class Rider {
    // Static variables to generate unique IDs for each rider and store riders by ID or team ID
    private static final AtomicInteger idGenerator = new AtomicInteger();
    static final Map<Integer, Rider> ridersById = new HashMap<>();
    private static final Map<Integer, List<Integer>> ridersByTeamId = new HashMap<>();

    // Instance variables
    private final int id; // Unique ID of the rider
    private final int teamId; // ID of the team to which the rider belongs
    private final String name; // Name of the rider
    private final int yearOfBirth; // Year of birth of the rider

    /**
     * Private constructor to create a new instance of a rider.
     * 
     * @param teamId      The ID of the team to which the rider belongs.
     * @param name        The name of the rider.
     * @param yearOfBirth The year of birth of the rider.
     */
    private Rider(int teamId, String name, int yearOfBirth) {
        this.id = idGenerator.incrementAndGet(); // Assign a unique ID to the rider
        this.teamId = teamId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    /**
     * Reset all rider data, including clearing rider maps and resetting the ID generator.
     */
    public static void resetRiders() {
        ridersById.clear(); // Clear the map of riders
        ridersByTeamId.clear(); // Clear the map of riders by team ID
        idGenerator.set(0); // Reset the ID generator
    }

    /**
     * Create a new rider with the given team ID, name, and year of birth.
     * 
     * @param teamID      The ID of the team to which the rider belongs.
     * @param name        The name of the rider.
     * @param yearOfBirth The year of birth of the rider.
     * @return The ID of the newly created rider.
     * @throws IDNotRecognisedException If the team ID does not match any team in the system.
     * @throws IllegalArgumentException If the name is null, empty, or the year of birth is invalid.
     */
    public static int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        if (!Team.teamsById.containsKey(teamID)) {
            throw new IDNotRecognisedException("Team ID does not match any team in the system.");
        }
        if (name == null || name.trim().isEmpty() || yearOfBirth < 1900) {
            throw new IllegalArgumentException("Invalid name or year of birth.");
        }

        Rider newRider = new Rider(teamID, name, yearOfBirth); // Create a new rider
        ridersById.put(newRider.id, newRider); // Store the rider by its ID
        ridersByTeamId.computeIfAbsent(teamID, k -> new ArrayList<>()).add(newRider.id); // Store the rider ID by its team ID

        return newRider.id; // Return the ID of the newly created rider
    }

    /**
     * Remove the rider with the given ID from the system.
     * 
     * @param riderId The ID of the rider to remove.
     * @throws IDNotRecognisedException If the rider ID does not match any rider in the system.
     */
    public static void removeRider(int riderId) throws IDNotRecognisedException {
        if (!ridersById.containsKey(riderId)) {
            throw new IDNotRecognisedException("Rider ID does not match any rider in the system.");
        }

        Rider riderToRemove = ridersById.remove(riderId); // Remove the rider from the map of riders
        List<Integer> teamRiders = ridersByTeamId.get(riderToRemove.teamId); // Get the list of riders belonging to the rider's team
        if (teamRiders != null) {
            teamRiders.remove(Integer.valueOf(riderId)); // Remove the rider ID from the list of team riders
            if (teamRiders.isEmpty()) {
                ridersByTeamId.remove(riderToRemove.teamId); // Remove the team from the map if it has no riders
            }
        }

        // Additional logic to remove rider's results from races would be required here
    }

    /**
     * Get an array of rider IDs belonging to the team with the given team ID.
     * 
     * @param teamId The ID of the team.
     * @return An array of rider IDs belonging to the team.
     * @throws IDNotRecognisedException If the team ID does not match any team in the system.
     */
    public static int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
        if (!Team.teamsById.containsKey(teamId)) {
            throw new IDNotRecognisedException("Team ID does not match any team in the system.");
        }

        List<Integer> teamRiders = ridersByTeamId.getOrDefault(teamId, new ArrayList<>()); // Get the list of riders belonging to the team
        return teamRiders.stream().mapToInt(i -> i).toArray(); // Convert the list of rider IDs to an array
    }

    /**
     * Check if a rider with the given ID exists in the system.
     * 
     * @param riderId The ID of the rider to check.
     * @return True if the rider exists, otherwise false.
     */
    public static boolean verifyRiderExists(int riderId) {
        return ridersById.containsKey(riderId); // Check if the rider ID exists in the map of riders
    }

}
