package cycling;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;
import static cycling.Team.teams;
/**
 * A class used to create an instance of a rider.
 * These riders will belong to an instance of a team class.
 * 
 * @author Ryan Butler
 * 
 */

public class Rider {
    private int teamID;
    private String name;
    private int yearOfBirth;
    private int riderId;
    private static int nextRiderId = 1; 
    private Map<Integer, LocalTime[]> resultsByStage; 
    
    public Rider(int teamID, String name, int yearOfBirth) {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.riderId = nextRiderId++;
        this.resultsByStage = new HashMap<>();
    }

    public int getTeamID() {
        return teamID;
    }

    public int getId() {
        return riderId;
    }

    public static int getNextRiderId() {
        return nextRiderId;
    }

    public void registerResultsForStage(int stageId, LocalTime[] checkpoints) {
        resultsByStage.put(stageId, checkpoints);
    }

    public boolean hasResultsForStage(int stageId) {
        return resultsByStage.containsKey(stageId);
    }

    public Map<Integer, LocalTime[]> getAllResults() {
        return resultsByStage;
    }

     public static int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        if (yearOfBirth < 1900) {
            throw new IllegalArgumentException("Invalid Year of Birth");
        }
        try {
            for (Team t : teams) {
                if (teamID == t.getId()) {
                    t.addRider(name, yearOfBirth);
                    return Rider.getNextRiderId();
                }
            }
            throw new IDNotRecognisedException("Team ID Not Recognised");
        } catch (IDNotRecognisedException e) {
            throw e;
        }
    }

    public static void removeRider(int riderId) throws IDNotRecognisedException {
        try{
            for(Team t:teams){
                for(Rider r: t.getRiders()){
                    if(r.getId()==riderId){
                        t.removeRider(r);
                    }
                }
            }throw new IDNotRecognisedException("ID Not Recognised");
        }catch(IDNotRecognisedException e){
            System.out.println(e);
        }
    }
}
