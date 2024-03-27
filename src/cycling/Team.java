package cycling;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to create an instance of a team.
 * This class represents teams that consist of riders.
 * It provides methods to manage teams and their riders.
 * 
 * @author Ryan Butler
 * 
 */
public class Team {
    // Fields
    private String name; // Name of the team
    private String description; // Description of the team
    private static int nextTeamId = 0; // Static counter for generating team IDs
    private int id; // Unique identifier for the team
    private List<Rider> riders; // List of riders belonging to the team

    // List to store all team instances
    public static List<Team> teams = new ArrayList<>();

    /**
     * Constructor to create a new Team instance.
     * 
     * @param name        The name of the team
     * @param description The description of the team
     */
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = nextTeamId; // Assign unique team ID
        nextTeamId++;
        this.riders = new ArrayList<>(); // Initialize list of riders
        teams.add(this); // Add team to the list of all teams
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    /**
     * Method to add a new rider to the team.
     * 
     * @param name        The name of the rider
     * @param yearOfBirth The year of birth of the rider
     * @return The ID of the newly added rider
     */
    public int addRider(String name, int yearOfBirth) {
        // Create a new rider instance and add it to the team
        Rider newRider = new Rider(this.id, name, yearOfBirth);
        riders.add(newRider);
        return newRider.getId();
    }

    /**
     * Method to remove a rider from the team.
     * 
     * @param rider The rider to be removed
     */
    public void removeRider(Rider rider) {
        riders.remove(rider);
    }

    /**
     * Method to retrieve the list of riders belonging to the team.
     * 
     * @return The list of riders
     */
    public List<Rider> getRiders() {
        return riders;
    }
    
    /**
     * Method to create a new team and add it to the list of teams.
     * 
     * @param name        The name of the team
     * @param description The description of the team
     * @return The ID of the newly created team
     * @throws IllegalNameException If the team name is already used
     * @throws InvalidNameException If the team name is invalid
     */
    public static int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        // Check if the team name is already used
        for (Team t : teams) {
            if (name.equals(t.getName())) {
                throw new IllegalNameException("Name already used");
            }
        }
        // Create a new team and add it to the list of teams
        Team newTeam = new Team(name, description);
        teams.add(newTeam);
        return newTeam.getId();
    }
    
    /**
     * Method to remove a team by ID.
     * 
     * @param teamId The ID of the team to be removed
     * @throws IDNotRecognisedException If the team ID is not recognized
     */
    public static void removeTeam(int teamId) throws IDNotRecognisedException {
        // Iterate through teams to find and remove the team with the specified ID
        for (Team t : teams) {
            if (teamId == t.getId()) {
                teams.remove(t);
                return;
            }
        }
        // Throw exception if team ID not recognized
        throw new IDNotRecognisedException("ID Not Recognised");
    }
    
    /**
     * Method to get IDs of all teams.
     * 
     * @return Array of team IDs
     */
    public static int[] getTeams() {
        int[] teamIds = new int[teams.size()];
        int i = 0;
        for (Team t : teams) {
            teamIds[i] = t.getId();
            i++;
        }
        return teamIds;
    }
   
    /**
     * Method to get IDs of riders belonging to a specific team.
     * 
     * @param teamId The ID of the team
     * @return Array of rider IDs belonging to the specified team
     * @throws IDNotRecognisedException If the team ID is not recognized
     */
    public static int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
        // Iterate through teams to find the specified team
        for (Team t : teams) {
            if (t.getId() == teamId) {
                List<Rider> riders = t.getRiders();
                // If no riders found for the team, return null
                if (riders.size() == 0) {
                    return null;
                }
                // Otherwise, return an array of rider IDs
                int[] riderIds = new int[riders.size()];
                int i = 0;
                for (Rider r : riders) {
                    riderIds[i] = r.getId();
                    i++;
                }
                return riderIds;
            }
        }
        // Throw exception if team ID not recognized
        throw new IDNotRecognisedException("ID Not Recognised");
    }
}
