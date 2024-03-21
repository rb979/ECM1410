package cycling;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to create an instance of a team.
 * 
 * @author Ryan Butler
 * 
 */

public class Team {
    private String name;
    private String description;
    private static int nextTeamId = 0;
    private int id;
    private List<Rider> riders;

    private static List<Team> teams = new ArrayList<>(); // Static list to store teams

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = nextTeamId;
        nextTeamId++;
        this.riders = new ArrayList<>();
        teams.add(this); // Add the created team to the list
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void addRider(String name, int yearOB) {
        riders.add(new Rider(this.id, name, yearOB));
    }

    public void removeRider(Rider rider) {
        riders.remove(rider);
    }

    // Static method to get all teams
    public static List<Team> getAllTeams() {
        return teams;
    }
}
