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

    public static List<Team> teams = new ArrayList<>();

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = nextTeamId;
        nextTeamId++;
        this.riders = new ArrayList<>();
        teams.add(this);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int addRider(String name, int yearOfBirth) {
        Rider newRider = new Rider(this.id, name, yearOfBirth);
        riders.add(newRider);
        return newRider.getId();
    }

    public void removeRider(Rider rider) {
        riders.remove(rider);
    }

    public List<Rider> getRiders() {
        return riders;
    }
    
    public static int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        for (Team t : teams) {
            if (name.equals(t.getName())) {
                throw new IllegalNameException("Name already used");
            }
        }
        Team newTeam = new Team(name, description);
        teams.add(newTeam);
        return newTeam.getId();
    }
    
    public static void removeTeam(int teamId) throws IDNotRecognisedException {
	    Iterator<Team> iterator = teams.iterator();
	    boolean check = false;
	    while (iterator.hasNext()) {
	        Team t = iterator.next();
	        if (teamId == t.getId()) {
	            iterator.remove();
	            check = true;
	            break;
	        }
	    }
	    if (!check) {
	        throw new IDNotRecognisedException("ID Not Recognised");
	    }
	}
    
    public static int[] getTeams() {
	    int[] teamIds = new int[teams.size()];
	    int i = 0;
	    for (Team t : teams) {
	        teamIds[i] = t.getId();
	        i++;
	    }
	    return teamIds;
	}
   
    public static int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
        for (Team t : teams) {
            if (t.getId() == teamId) {
                List<Rider> riders = t.getRiders();
                if (riders.size() == 0) {
                    return null;
                }
                int[] riderIds = new int[riders.size()];
                int i = 0;
                for (Rider r : riders) {
                    riderIds[i] = r.getId();
                    i++;
                }
                return riderIds;
            }
        }
        throw new IDNotRecognisedException("ID Not Recognised");
    }
}
