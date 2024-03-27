package cycling;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cycling.Team.teams;

/**
 * A class used to create an instance of a rider.
 * These riders will belong to an instance of a team class.
 * This class represents individual cyclists participating in races.
 * It contains methods to manage rider data and results.
 * 
 * @author Ryan Butler and Hugo Blanco
 *
 */
public class Rider {
    // Fields
    private int teamID; // Team ID to which the rider belongs
    private String name; // Rider's name
    private int yearOfBirth; // Rider's year of birth
    private int riderId; // Rider's unique identifier
    private static int nextRiderId = 1; // Static counter to generate unique rider IDs
    private Map<Integer, LocalTime[]> resultsByStage = new HashMap<>(); // Map to store results by stage
    public static List<Rider> riders = new ArrayList<>(); // List to store all rider instances

    /**
     * Constructor to create a new Rider instance.
     * 
     * @param teamID      The ID of the team to which the rider belongs
     * @param name        The name of the rider
     * @param yearOfBirth The year of birth of the rider
     */
    public Rider(int teamID, String name, int yearOfBirth) {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.riderId = nextRiderId++; // Assign unique rider ID
        riders.add(this); // Add rider to the list of all riders
    }

    // Getter methods
    public static int getNextRiderId() {
        return nextRiderId;
    }

    public int getTeamID() {
        return teamID;
    }

    public int getId() {
        return riderId;
    }

    /**
     * Method to create a new rider and add it to the specified team.
     * 
     * @param teamID      The ID of the team to which the rider belongs
     * @param name        The name of the rider
     * @param yearOfBirth The year of birth of the rider
     * @return The ID of the newly created rider
     * @throws IDNotRecognisedException   If the team ID is not recognized
     * @throws IllegalArgumentException If name is empty or year of birth is invalid
     */
    public static int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        // Validation checks
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        if (yearOfBirth < 1900) {
            throw new IllegalArgumentException("Invalid Year of Birth");
        }
        try {
            // Find the team by ID and add the rider
            for (Team t : teams) {
                if (teamID == t.getId()) {
                    t.addRider(name, yearOfBirth);
                    return Rider.getNextRiderId();
                }
            }
            // Throw exception if team ID not recognized
            throw new IDNotRecognisedException("Team ID Not Recognised");
        } catch (IDNotRecognisedException e) {
            throw e;
        }
    }

    /**
     * Method to remove a rider by ID.
     * 
     * @param riderId The ID of the rider to be removed
     * @throws IDNotRecognisedException If the rider ID is not recognized
     */
    public static void removeRider(int riderId) throws IDNotRecognisedException {
        try {
            // Find and remove the rider from the team
            for (Team t : teams) {
                for (Rider r : t.getRiders()) {
                    if (r.getId() == riderId) {
                        t.removeRider(r);
                    }
                }
            }
            // Throw exception if rider ID not recognized
            throw new IDNotRecognisedException("ID Not Recognised");
        } catch (IDNotRecognisedException e) {
            System.out.println(e);
        }
    }

    /**
     * Method to set results for a particular stage for the rider.
     * 
     * @param stageId     The ID of the stage
     * @param checkpoints Array of LocalTime objects representing checkpoints
     */
    public void setResultsForStage(int stageId, LocalTime[] checkpoints) {
        resultsByStage.put(stageId, checkpoints);
    }

    /**
     * Method to check if the rider has results for a particular stage.
     * 
     * @param stageId The ID of the stage
     * @return True if the rider has results for the stage, otherwise false
     */
    public boolean hasResultsForStage(int stageId) {
        return resultsByStage.containsKey(stageId);
    }

    /**
     * Method to get results for a particular stage for the rider.
     * 
     * @param stageId The ID of the stage
     * @return Array of LocalTime objects representing checkpoints
     */
    public LocalTime[] getResultsForStage(int stageId) {
        return resultsByStage.get(stageId);
    }

    /**
     * Method to retrieve a rider by ID.
     * 
     * @param riderId The ID of the rider to be retrieved
     * @return The Rider object with the specified ID, or null if not found
     */
    public static Rider retrieveRiderById(int riderId) {
        for (Rider rider : riders) {
            if (rider.getId() == riderId) {
                return rider;
            }
        }
        return null;
    }

    /**
     * Method to remove results for a particular stage for the rider.
     * 
     * @param stageId The ID of the stage
     */
    public void removeResultsForStage(int stageId) {
        resultsByStage.remove(stageId);
    }

    /**
     * Method to get IDs of all participating riders for a particular stage.
     * 
     * @param stageId The ID of the stage
     * @return List of Integer representing IDs of participating riders
     */
    public static List<Integer> getAllParticipatingRiderIds(int stageId) {
        List<Integer> participatingRiderIds = new ArrayList<>();
        for (Rider rider : riders) {
            if (rider.hasResultsForStage(stageId)) {
                participatingRiderIds.add(rider.getId());
            }
        }
        return participatingRiderIds;
    }

    /**
     * Method to sort rider IDs by finish time for a particular stage.
     * 
     * @param allRiderIds List of Integer representing IDs of all participating riders
     * @param stageId     The ID of the stage
     */
    public static void sortRiderIdsByFinishTime(List<Integer> allRiderIds, int stageId) {
        allRiderIds.sort((riderId1, riderId2) -> {
            Rider rider1 = retrieveRiderById(riderId1);
            Rider rider2 = retrieveRiderById(riderId2);
            LocalTime[] times1 = rider1.getResultsForStage(stageId);
            LocalTime[] times2 = rider2.getResultsForStage(stageId);
            if (times1 != null && times2 != null && times1.length > 0 && times2.length > 0) {
                return times1[times1.length - 1].compareTo(times2[times2.length - 1]);
            }
            return 0;
        });
    }
}
