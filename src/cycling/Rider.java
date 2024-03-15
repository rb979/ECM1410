package cycling;

import java.util.HashMap;
import java.util.Map;

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
    private static int nextRiderId = 0;
    private Map<Integer, LocalTime[]> resultsByStage; // Maps stage IDs to arrays of checkpoint times

    public Rider(int teamID, String name, int yearOfBirth) {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.riderId = nextRiderId;
        nextRiderId++;
        this.resultsByStage = new HashMap<>();
    }

    public int getTeamID() {
        return teamID;
    }

    public int getId() {
        return riderId;
    }

    // Method to register results for a specific stage
    public void registerResultsForStage(int stageId, LocalTime[] checkpoints) {
        resultsByStage.put(stageId, checkpoints);
    }

    // Method to check if results are already registered for a stage
    public boolean hasResultsForStage(int stageId) {
        return resultsByStage.containsKey(stageId);
    }

    // Getter for all results
    public Map<Integer, LocalTime[]> getAllResults() {
        return resultsByStage;
    }
}
