package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A class used to create an instance of a team.
 * 
 * @author Ryan Butler
 * 
 */

public class Team {
    String name;
    String description;
    static int nextTeamId = 0;
    int id;
    ArrayList<Rider> riders;

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = nextTeamId;
        nextTeamId++;
        this.riders = new ArrayList<Rider>();
        teams.add(this);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void addRider(int teamId, String name, int yearOB) {
        riders.add(new Rider(teamId, name, yearOB));
    }

    public void removeRider(Rider astolfo) {
        riders.remove(astolfo);
    }
}
