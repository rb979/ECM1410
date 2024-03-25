package cycling;

import java.time.LocalTime;
import java.util.*;

public class Rider {
    private int id; // Changed from public int getId;
    private int teamID;
    private String name;
    private int yearOfBirth;
    private static int nextRiderId = 1;
    public static Map<Integer, LocalTime[]> stageResults;

    private static Map<Integer, Integer> individualPoint;

    public static List<Rider> riders = new ArrayList<>();

    public Rider(int teamID, String name, int yearOfBirth) {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.id = nextRiderId++; // Increment nextRiderId after assigning it to id
        this.stageResults = new HashMap<>();
        riders.add(this);
    }

    public static int getNextRiderId() {
        return nextRiderId;
    }

    public int getTeamID() {
        return teamID;
    }

    public int getId() {
        return id;
    }

    public boolean hasResultsForStage(int stageId) {
        return stageResults.containsKey(stageId);
    }

    // Renamed from getRiderId
    public void addResults(int stageId, LocalTime[] checkpointTimes) {
        stageResults.put(stageId, checkpointTimes);
    }

    public LocalTime[] getResultsForStage(int stageId) {
        return stageResults.get(stageId);
    }

    public static Rider retrieveRiderById(int riderId) {
        for (Rider rider : riders) {
            if (rider.getId() == riderId) {
                return rider;
            }
        }
        return null;
    }


    public void removeResultsForStage(int stageId) {
        // Implementation depends on how results are stored for each rider
        // For example, if results are stored in a map where stageId is the key:
        stageResults.remove(stageId);

    }
    public static List<Integer> getAllParticipatingRiderIds(int stageId) {
        List<Integer> participatingRiderIds = new ArrayList<>();

        // Iterate through all riders
        for (Rider rider : Rider.riders) {
            // Check if the rider has results for the specified stage
            if (rider.hasResultsForStage(stageId)) {
                // If so, add the rider's ID to the list
                participatingRiderIds.add(rider.getId());
            }
        }

        return participatingRiderIds;
    }

    public static void sortRiderIdsByFinishTime(List<Integer> allRiderIds, int stageId) {
        // Sort the list of all rider IDs based on finish time
        Collections.sort(allRiderIds, Comparator.comparingInt(riderId -> {
            // Retrieve the rider by ID
            Rider rider = Rider.retrieveRiderById(riderId);

            // Get the checkpoint times for the rider in the stage
            LocalTime[] checkpointTimes = rider.getResultsForStage(stageId);

            // Return the last checkpoint time (finishing time) if available
            return (checkpointTimes != null && checkpointTimes.length > 0) ? checkpointTimes[checkpointTimes.length - 1].toSecondOfDay() : Integer.MAX_VALUE;
        }));
    }




    public static void addpoints(int stageId, int points){
        individualPoint.put(stageId, points + individualPoint.get(stageId));
    }


    public static void individpoints (int stageId, int[] rankedIds, int[] points){
        for (int i = 0; i < 15; i++){
            Rider rider = retrieveRiderById(rankedIds[i]);
            rider.addpoints(stageId,points[i]);

        }
    }


    public static Map<Rider, LocalTime[]> getAllCheckpointTimesForStage(int stageId) {
        Map<Rider, LocalTime[]> checkpointTimesMap = new HashMap<>();

        for (Rider rider : riders) {
            // Check if the rider has results for the specified stage
            if (rider.hasResultsForStage(stageId)) {
                // If so, add the rider along with their checkpoint times to the map
                checkpointTimesMap.put(rider, rider.getResultsForStage(stageId));
            }
        }

        return checkpointTimesMap;






}

    public static Map<Integer, List<LocalTime[]>> mapRiderCheckpointTimes(List<Integer> riderIds, List<LocalTime[]> allRidersResults, int[] checkpointIds) {
        Map<Integer, List<LocalTime[]>> riderCheckpointTimesMap = new HashMap<>();

        // Iterate over each rider
        for (int riderId : riderIds) {
            List<LocalTime[]> riderCheckpointTimes = new ArrayList<>();

            // Iterate over each checkpoint to find the position of the rider's checkpoint times
            for (int checkpointId : checkpointIds) {
                int position = Arrays.asList(allRidersResults).indexOf(Stage.getCheckpointById(checkpointId));
                if (position != -1) {
                    // Add the rider's checkpoint times starting from the current position
                    riderCheckpointTimes.addAll(allRidersResults.subList(position, allRidersResults.size()));
                }
            }

            // Map the rider ID to their checkpoint times
            riderCheckpointTimesMap.put(riderId, riderCheckpointTimes);
        }

        return riderCheckpointTimesMap;

    
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








}}





