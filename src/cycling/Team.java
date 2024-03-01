package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Team {
  string name ;
  string description;
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

  public string getName() {
    return name;
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
