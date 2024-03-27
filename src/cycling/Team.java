package cycling;

/**
 * A class used to create an instance of a team.
 * 
 * @author Ryan Butler and Hugo Blanco
 * 
 */

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;

public class Team {
    private static final AtomicInteger idGenerator = new AtomicInteger();
    static final Map<Integer, Team> teamsById = new HashMap<>();
    private static final Map<String, Integer> teamIdsByName = new HashMap<>();

    private final int id;
    private final String name;
    private final String description;

    private Team(String name, String description) {
        this.id = idGenerator.incrementAndGet();
        this.name = name;
        this.description = description;
    }

    public static void resetTeams() {
        teamsById.clear(); // Clear the map of teams by their ID
        teamIdsByName.clear(); // Clear the map of team IDs by their name
        idGenerator.set(0); // Reset the ID generator to start from 0 (or 1, depending on your ID numbering scheme)
    }

    public static int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        validateTeamName(name);
        if (teamIdsByName.containsKey(name)) {
            throw new IllegalNameException("Team name already exists in the platform: " + name);
        }
        Team newTeam = new Team(name, description);
        teamsById.put(newTeam.id, newTeam);
        teamIdsByName.put(name, newTeam.id);
        return newTeam.id;
    }

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

    public static void removeTeam(int teamId) throws IDNotRecognisedException {
        Team teamToRemove = teamsById.get(teamId);
        if (teamToRemove == null) {
            throw new IDNotRecognisedException("The ID does not match any team in the system: " + teamId);
        }

        // Remove the team from both maps
        teamsById.remove(teamId);
        teamIdsByName.remove(teamToRemove.getName());
    }
    public static int[] getTeams() {
        if (teamsById.isEmpty()) {
            return new int[0]; // Return an empty array if there are no teams
        }

        Set<Integer> teamIds = teamsById.keySet();
        return teamIds.stream().mapToInt(Integer::intValue).toArray();
    }

    // Additional methods as needed...
}
