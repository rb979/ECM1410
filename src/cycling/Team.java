package cycling;

/**
 * A class used to create an instance of a team.
 * This class manages the creation, reset, and removal of teams.
 * Teams are identified by their unique ID and can be retrieved by name.
 * 
 * Authors: Ryan Butler and Hugo Blanco
 */
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;

public class Team {
    // Static variables to generate unique IDs for each team and store teams by ID or name
    private static final AtomicInteger idGenerator = new AtomicInteger();
    static final Map<Integer, Team> teamsById = new HashMap<>();
    private static final Map<String, Integer> teamIdsByName = new HashMap<>();

    // Instance variables
    private final int id; // Unique ID of the team
    private final String name; // Name of the team
    private final String description; // Description of the team

    /**
     * Private constructor to create a new instance of a team.
     * 
     * @param name        The name of the team.
     * @param description The description of the team.
     */
    private Team(String name, String description) {
        this.id = idGenerator.incrementAndGet(); // Assign a unique ID to the team
        this.name = name;
        this.description = description;
    }

    /**
     * Reset all team data, including clearing team maps and resetting the ID generator.
     */
    public static void resetTeams() {
        teamsById.clear(); // Clear the map of teams by their ID
        teamIdsByName.clear(); // Clear the map of team IDs by their name
        idGenerator.set(0); // Reset the ID generator to start from 0 (or 1, depending on your ID numbering scheme)
    }

    /**
     * Create a new team with the given name and description.
     * 
     * @param name        The name of the team.
     * @param description The description of the team.
     * @return The ID of the newly created team.
     * @throws IllegalNameException   If the team name already exists in the platform.
     * @throws InvalidNameException   If the team name is null, empty, longer than 30 characters, or contains white spaces.
     */
    public static int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        validateTeamName(name); // Validate the team name
        if (teamIdsByName.containsKey(name)) {
            throw new IllegalNameException("Team name already exists in the platform: " + name);
        }
        Team newTeam = new Team(name, description); // Create a new team
        teamsById.put(newTeam.id, newTeam); // Store the team by its ID
        teamIdsByName.put(name, newTeam.id); // Store the team ID by its name
        return newTeam.id; // Return the ID of the newly created team
    }

    /**
     * Validate the team name.
     * 
     * @param name The name of the team to validate.
     * @throws InvalidNameException If the team name is null, empty, longer than 30 characters, or contains white spaces.
     */
    private static void validateTeamName(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Team name cannot be null or empty.");
        }
        if (name.length() > 30) {
            throw new InvalidNameException("Team name cannot have more than 30 characters.");
        }
        if (name.contains(" ")) {
            throw new InvalidNameException("Team name cannot contain white spaces.");
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Remove the team with the given ID from the system.
     * 
     * @param teamId The ID of the team to remove.
     * @throws IDNotRecognisedException If the ID does not match any team in the system.
     */
    public static void removeTeam(int teamId) throws IDNotRecognisedException {
        Team teamToRemove = teamsById.get(teamId); // Get the team to remove by its ID
        if (teamToRemove == null) {
            throw new IDNotRecognisedException("The ID does not match any team in the system: " + teamId);
        }

        teamsById.remove(teamId); // Remove the team from the map of teams by its ID
        teamIdsByName.remove(teamToRemove.getName()); // Remove the team from the map of team IDs by its name
    }

    /**
     * Get an array of IDs of all teams in the system.
     * 
     * @return An array of IDs of all teams in the system.
     */
    public static int[] getTeams() {
        if (teamsById.isEmpty()) {
            return new int[0]; // Return an empty array if there are no teams
        }

        Set<Integer> teamIds = teamsById.keySet(); // Get the set of all team IDs
        return teamIds.stream().mapToInt(Integer::intValue).toArray(); // Convert the set of team IDs to an array
    }


}
