package cycling;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;

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
}
