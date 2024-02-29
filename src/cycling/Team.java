package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
    }

    public String getName() {
        return name;
    }

    public ArrayList<Rider> getRiders() {
        return riders;
    }

    public int getId() {
        return id;
    }
    public void addRider(int teamId, String name, int yearOB){
        riders.add(new Rider(teamId,name,yearOB));
    }
    public void removeRider(Rider astolfo){
        riders.remove(astolfo);
    }
}
