package cycling;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cycling.Race.racesById;

public class Stage {
    private static final AtomicInteger idGenerator = new AtomicInteger();
    static final Map<Integer, Stage> stagesById = new HashMap<>();
    private static final Map<Integer, List<Integer>> stageIdsByName = new HashMap<>();

    private static List<Checkpoint> checkpoints = new ArrayList<>();
    static Map<Integer, Map<Integer, LocalTime[]>> stageResults = new HashMap<>();





    private final int id;
    private final int raceId;
    private final String name;
    private final String description;
    private final double length;
    private final LocalDateTime startTime;
    private final StageType type;
    private StageState state;

    private Stage(int raceId, String name, String description, double length, LocalDateTime startTime, StageType type) {
        this.id = idGenerator.incrementAndGet();
        this.raceId = raceId;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        this.state = StageState.PREPARATION; // Default state
    }



    public static void resetStages() {
        stagesById.clear(); // Clears the map of stages by their ID

        stageIdsByName.clear(); // Clears the map of stage IDs by their name
        checkpoints.clear(); // Clears the list of checkpoints
        stageResults.clear(); // Clears the map of stage results
        idGenerator.set(0); // Resets the ID generator
    }

    // Methods to add, get, and remove stages will be implemented here.

    // Getters
    public int getId() {
        return id;
    }

    public int getRaceId() {
        return raceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLength() {
        return length;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public StageType getType() {
        return type;
    }



    public static int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type)
            throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
        // Validate race ID
        if (!racesById.containsKey(raceId)) {
            throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
        }

        // Validate stage name
        validateStageName(stageName);

        // Validate stage length
        if (length < 5) {
            throw new InvalidLengthException("Stage length must be at least 5km.");
        }

        // Ensure the startTime is not null
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null.");
        }

        // Create and add the stage
        Stage newStage = new Stage(raceId, stageName, description, length, startTime, type);
        stagesById.put(newStage.id, newStage);



        return newStage.id;
    }

    private static void validateStageName(String name) throws IllegalNameException, InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Stage name cannot be null or empty.");
        }
        if (name.length() > 30) {
            throw new InvalidNameException("Stage name cannot have more than 30 characters.");
        }
        if (name.contains(" ")) {
            throw new InvalidNameException("Stage name cannot contain white spaces.");
        }
        if (stageIdsByName.containsKey(name)) {
            throw new IllegalNameException("Stage name already exists: " + name);
        }
    }

    public static int[] getRaceStages(int raceId)  {
        // Check if the race exists (assuming racesById is a map of all races)

        // Stream over the stagesById map, filter stages by the given raceId,
        // and collect the IDs into a list
        List<Integer> filteredStageIds = stagesById.entrySet().stream()
                .filter(entry -> entry.getValue().getRaceId() == raceId)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Check if any stages were found for the race
        if (filteredStageIds.isEmpty()) {
            // Return an empty array if no stages are associated with the race ID
            return new int[0];
        }

        // Convert the List<Integer> to int[]
        return filteredStageIds.stream().mapToInt(i -> i).toArray();
    }

    public static double getStageLength(int stageId) throws IDNotRecognisedException {
        // Check if the stage exists
        Stage stage = stagesById.get(stageId);
        if (stage == null) {
            throw new IDNotRecognisedException("The ID does not match any stage in the system: " + stageId);
        }

        // Return the length of the stage
        return stage.getLength();
    }


    public static int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
                                                 Double length) throws IDNotRecognisedException, InvalidLocationException,
            InvalidStageStateException, InvalidStageTypeException {
        // Validate stage exists
        Stage stage = stagesById.get(stageId);
        if (stage == null) {
            throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
        }

        
        if (stage.type == StageType.TT) {
            throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoint.");
        }

        // Validate location within stage bounds
        if (location <= 0 || location > stage.length) {
            throw new InvalidLocationException("Location is out of bounds of the stage length.");
        }

       
        if (stage.isWaitingForResults()) {
            throw new InvalidStageStateException("The stage is waiting for results.");
        }

        // Create and add the checkpoint
        Checkpoint newCheckpoint = new Checkpoint(location, type, averageGradient, length);
        checkpoints.add(newCheckpoint);

        return newCheckpoint.getId();
    }

    private boolean isWaitingForResults() {
       
        return false;
    }

    public static int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
            InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {

        // Validate the stage exists
        Stage stage = stagesById.get(stageId);
        if (stage == null) {
            throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
        }

       
        if (stage.type == StageType.TT) {
            throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoint.");
        }

        // Validate location within stage bounds
        if (location <= 0 || location > stage.length) {
            throw new InvalidLocationException("Location is out of bounds of the stage length.");
        }

        
        if (stage.isWaitingForResults()) {
            throw new InvalidStageStateException("The stage is waiting for results.");
        }

        // Create and add the sprint checkpoint
       
        Checkpoint sprintCheckpoint = new Checkpoint(location, CheckpointType.SPRINT, 0.0, 0.0);
        stage.checkpoints.add(sprintCheckpoint);

        return sprintCheckpoint.getId();
    }

    public static void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
        boolean checkpointFound = false;

        for (Stage stage : stagesById.values()) {
           
            if (stage.isWaitingForResults()) {
                throw new InvalidStageStateException("The stage is 'waiting for results' and cannot be modified.");
            }

            // Iterate through checkpoints to find the one to remove
            for (Iterator<Checkpoint> iterator = stage.checkpoints.iterator(); iterator.hasNext();) {
                Checkpoint checkpoint = iterator.next();
                if (checkpoint.getId() == checkpointId) {
                    iterator.remove(); // Remove the checkpoint
                    checkpointFound = true;
                    break; // Break from the for-loop after removing the checkpoint
                }
            }

            if (checkpointFound) {
                break; // Break from the outer loop if checkpoint has been found and removed
            }
        }

        if (!checkpointFound) {
            throw new IDNotRecognisedException("No checkpoint found with the ID: " + checkpointId);
        }

      
    }

    public static void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
        Stage stage = stagesById.get(stageId);
        if (stage == null) {
            throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
        }

        if (stage.state == StageState.WAITING_FOR_RESULTS) {
            throw new InvalidStageStateException("The stage is already 'waiting for results'.");
        }

        // Change the stage's state
        stage.state = StageState.WAITING_FOR_RESULTS;
    }

    public static int[] getStageCheckpoints(int stageId) {
        Stage stage = stagesById.get(stageId);

        // Assuming checkpoints are added in no particular order, sort them by location
        List<Checkpoint> sortedCheckpoints = stage.checkpoints.stream()
                .sorted(Comparator.comparingDouble(Checkpoint::getLocation))
                .collect(Collectors.toList());

        // Convert sorted list of Checkpoint objects to an array of checkpoint IDs
        return sortedCheckpoints.stream()
                .mapToInt(Checkpoint::getId)
                .toArray();
    }
    public StageState getState() {
        return this.state;
    }

    public static int getCheckpointCount(int stageId) throws IDNotRecognisedException {
        Stage stage = stagesById.get(stageId); 
        if (stage == null) {
            throw new IDNotRecognisedException("Stage ID does not match any stage in the system.");
        }
        return stage.checkpoints.size();
    }

    public static void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpointTimes)
            throws IDNotRecognisedException, DuplicatedResultException,
            InvalidCheckpointTimesException, InvalidStageStateException {

        // Check if the stage exists
        Stage stage = stagesById.get(stageId);
        if (stage == null) {
            throw new IDNotRecognisedException("Stage ID not recognised.");
        }

        
        if (!Rider.verifyRiderExists(riderId)) {
            throw new IDNotRecognisedException("Rider ID not recognised.");
        }

        // Check if the stage is waiting for results
        if (stage.getState() != StageState.WAITING_FOR_RESULTS) {
            throw new InvalidStageStateException("Results can only be added to a stage while it is 'waiting for results'.");
        }

        // Ensure no duplicated results
        Map<Integer, LocalTime[]> resultsForStage = stageResults.computeIfAbsent(stageId, k -> new HashMap<>());
        if (resultsForStage.containsKey(riderId)) {
            throw new DuplicatedResultException("The rider has already a result for this stage.");
        }

        // Validate checkpoint times length
        int expectedLength = stage.getCheckpointCount(stageId) + 2;
        if (checkpointTimes.length != expectedLength) {
            throw new InvalidCheckpointTimesException("Invalid number of checkpoint times provided.");
        }

        // Record the results
        resultsForStage.put(riderId, checkpointTimes);
        stageResults.put(stageId, resultsForStage);
    }

    public static Map<Integer, LocalTime> getFinishTimes(int stageId) {
        Map<Integer, LocalTime> finishTimes = new HashMap<>();

        Map<Integer, LocalTime[]> resultsForStage = stageResults.get(stageId);
        if (resultsForStage != null) {
            for (Map.Entry<Integer, LocalTime[]> entry : resultsForStage.entrySet()) {

                LocalTime finishTime = entry.getValue()[entry.getValue().length - 1];
                finishTimes.put(entry.getKey(), finishTime);
            }
        }

        return finishTimes;
    }

    public static LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // validate the existence of the stage and the rider
        if (!stageResults.containsKey(stageId)) {
            throw new IDNotRecognisedException("Stage ID does not match any stage in the system.");
        }
        Map<Integer, LocalTime[]> resultsForStage = stageResults.get(stageId);
        if (!resultsForStage.containsKey(riderId)) {
            throw new IDNotRecognisedException("Rider ID does not match any rider in this stage.");
        }

        // Fetch all finish times for the stage
        Map<Integer, LocalTime> finishTimes = getFinishTimes(stageId);

        // Create a list of finish times and sort it
        List<LocalTime> sortedFinishTimes = new ArrayList<>(finishTimes.values());
        Collections.sort(sortedFinishTimes);

        LocalTime riderFinishTime = finishTimes.get(riderId);
        LocalTime adjustedTime = riderFinishTime; // Start with the actual finish time

        
        for (LocalTime time : sortedFinishTimes) {
            if (riderFinishTime.minusSeconds(1).isBefore(time) || riderFinishTime.equals(time)) {
                adjustedTime = time;
                break;
            }
        }

        return adjustedTime;
    }

    public static void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // Check if the stage exists
        if (!stageResults.containsKey(stageId)) {
            throw new IDNotRecognisedException("Stage ID does not match any stage in the system.");
        }

        Map<Integer, LocalTime[]> resultsForStage = stageResults.get(stageId);

        // Check if the rider has results in this stage
        if (!resultsForStage.containsKey(riderId)) {
            throw new IDNotRecognisedException("Rider ID does not match any rider in this stage.");
        }

        // Remove the rider's results from this stage
        resultsForStage.remove(riderId);

        if (resultsForStage.isEmpty()) {
            stageResults.remove(stageId);
        }
    }

    public static int[] getRidersRankInStage(int stageId) {
        // Check if the stage exists


        // Fetch the stage results
        Map<Integer, LocalTime[]> resultsForStage = stageResults.get(stageId);
        if (resultsForStage == null || resultsForStage.isEmpty()) {
            return new int[0]; // Return an empty array if there are no results
        }

        // Convert results to finish times
        Map<Integer, LocalTime> finishTimes = resultsForStage.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[entry.getValue().length - 1]));

        // Sort riders by their finish times
        List<Map.Entry<Integer, LocalTime>> sortedEntries = new ArrayList<>(finishTimes.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());

        // Extract rider IDs from the sorted list
        int[] sortedRiderIds = sortedEntries.stream().mapToInt(Map.Entry::getKey).toArray();

        return sortedRiderIds;
    }

    public static LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
        // Verify stage existence
        if (!stagesById.containsKey(stageId)) {
            throw new IDNotRecognisedException("The ID does not match any stage in the system: " + stageId);
        }

        
        Map<Integer, LocalTime[]> resultsForStage = stageResults.get(stageId);
        if (resultsForStage == null || resultsForStage.isEmpty()) {
            return new LocalTime[0]; // Return an empty array if there are no results
        }

        // Get finish times for all riders
        List<LocalTime> finishTimes = new ArrayList<>();
        for (LocalTime[] times : resultsForStage.values()) {
            finishTimes.add(times[times.length - 1]); 
        }

        // Sort the finish times
        Collections.sort(finishTimes);

       
        for (int i = 0; i < finishTimes.size() - 1; i++) {
            LocalTime current = finishTimes.get(i);
            LocalTime next = finishTimes.get(i + 1);
            if (next.minusSeconds(1).isBefore(current)) {
                finishTimes.set(i + 1, current); // Adjust next rider's time to current rider's time
            }
        }


        return finishTimes.toArray(new LocalTime[0]);
    }


    public static int[] getSprintRankingsAtCheckpoint(int stageId, int checkpointId)  {
        int[] checkpointIds = getStageCheckpoints(stageId); 
        // Find the index of the checkpoint in the ordered array
        int checkpointIndex = Arrays.binarySearch(checkpointIds, checkpointId) + 1 ;


        // Create a map to store rider ID and their time at the specified checkpoint
        Map<Integer, LocalTime> riderTimesAtCheckpoint = new HashMap<>();

        // Populate the map with rider times at the sprint checkpoint
        stageResults.getOrDefault(stageId, new HashMap<>()).forEach((riderId, times) -> {
            if (checkpointIndex < times.length) {
                LocalTime time = times[checkpointIndex];
                if (time != null) { // Ensure there's a time recorded at the checkpoint
                    riderTimesAtCheckpoint.put(riderId, time);
                }
            }
        });

        // Sort the map entries by time to get rankings
        return riderTimesAtCheckpoint.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .mapToInt(Map.Entry::getKey)
                .toArray();
    }


    public static int[] getRidersPointsInStage(int stageId) {

        Stage stage = stagesById.get(stageId);
        int[] riderIds = getRidersRankInStage(stageId); 
        Map<Integer, Integer> riderPoints = new HashMap<>();

        // Points for intermediate sprints
        int[] sprintPoints = {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        // Assign sprint points for each checkpoint of type SPRINT
        for (Checkpoint checkpoint : stage.checkpoints) {
            if (checkpoint.getType() == CheckpointType.SPRINT) {
                int[] sprintRankings = getSprintRankingsAtCheckpoint(stageId, checkpoint.getId());
                for (int rank = 0; rank < sprintRankings.length && rank < sprintPoints.length; rank++) {
                    riderPoints.merge(sprintRankings[rank], sprintPoints[rank], Integer::sum);
                }
            }
        }

        // Points allocation based on the stage type
        int[] finishLinePoints;
        switch (stage.getType()) {
            case FLAT -> finishLinePoints = new int[]{50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2};
            case MEDIUM_MOUNTAIN -> finishLinePoints = new int[]{30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
            case HIGH_MOUNTAIN, TT -> finishLinePoints = new int[]{20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
            default -> throw new IllegalArgumentException("Unsupported stage type");
        }

        // Add finish line points based on rider finish order
        for (int i = 0; i < riderIds.length && i < finishLinePoints.length; i++) {
            riderPoints.merge(riderIds[i], finishLinePoints[i], Integer::sum);
        }

        // Prepare final points array to return, matching the order of riderIds
        int[] totalPoints = new int[riderIds.length];
        for (int i = 0; i < riderIds.length; i++) {
            totalPoints[i] = riderPoints.getOrDefault(riderIds[i], 0);
        }

        return totalPoints;
    }


    public static int[] getRidersMountainPointsInStage(int stageId) {
        // Verify the stage exists
        // This map will hold the total mountain points for each rider
        Map<Integer, Integer> riderMountainPoints = new HashMap<>();

        // Iterate over each checkpoint to find categorized climbs and assign points

        for (Checkpoint checkpoint : checkpoints) {
            int []checkpointids = Stage.getStageCheckpoints(stageId);
            boolean containsCheckpoint = Arrays.stream(checkpointids).anyMatch(id -> id == checkpoint.getId());

            if (containsCheckpoint && checkpoint.getType() != CheckpointType.SPRINT) {
                int[] points;
                switch (checkpoint.getType()) {
                    case C1:
                        points = new int[]{10, 8, 6, 4, 2, 1};
                        break;
                    case C2:
                        points = new int[]{5, 3, 2, 1};
                        break;
                    case C3:
                        points = new int[]{2, 1};
                        break;
                    case C4:
                        points = new int[]{1};
                        break;
                    case HC:
                        points = new int[]{20, 15, 12, 10, 8, 6, 4, 2};
                        break;
                    default:
                        continue; // Skip if the checkpoint type is not a categorized climb
                }

                
                
                int[] riderOrder = getSprintRankingsAtCheckpoint(stageId,checkpoint.getId());

                // Assign points to riders based on their order at this checkpoint
                for (int i = 0; i < riderOrder.length && i < points.length; i++) {
                    int riderId = riderOrder[i];
                    riderMountainPoints.put(riderId, riderMountainPoints.getOrDefault(riderId, 0) + points[i]);
                }
            }
        }

        List<Map.Entry<Integer, Integer>> sortedByPointsDesc = riderMountainPoints.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        
        int[] sortedRiderIds = sortedByPointsDesc.stream().mapToInt(Map.Entry::getKey).toArray();

       
        int[] sortedPoints = sortedByPointsDesc.stream().mapToInt(Map.Entry::getValue).toArray();

        
        return sortedPoints;
    }


    public static int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
        if (!racesById.containsKey(raceId)) {
            throw new IDNotRecognisedException("Race ID does not match any race in the system.");
        }

        int[] stageIds = Stage.getRaceStages(raceId); // Retrieve all stage IDs for the given race

        // Initialize a map to keep track of each rider's total time across all stages
        Map<Integer, Duration> totalTimesForRiders = new HashMap<>();

        // For each stage, sum up the adjusted elapsed time for each rider
        for (int stageId : stageIds) {
           
            int[] riderIds = Stage.getRidersInStage(stageId); 

            for (int riderId : riderIds) {
                LocalTime time = Stage.getRiderAdjustedElapsedTimeInStage(stageId, riderId); // Fetch the adjusted time
                Duration duration = Duration.between(LocalTime.MIDNIGHT, time); // Convert LocalTime to Duration

                totalTimesForRiders.merge(riderId, duration, Duration::plus); // Aggregate the times
            }
        }

        // Sort the entries of the map by the total duration, ascending
        List<Map.Entry<Integer, Duration>> sortedEntries = totalTimesForRiders.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        // Extract the rider IDs from the sorted list to form the final ranking
        int[] ranking = sortedEntries.stream().mapToInt(Map.Entry::getKey).toArray();

        return ranking;
    }

    
    private static int[] getRidersInStage(int stageId)  {
        

        // Check if there are results for the stage
        if (!stageResults.containsKey(stageId) || stageResults.get(stageId).isEmpty()) {
            return new int[0]; // Return an empty array if there are no results for the stage
        }

        // Fetch the map of results for the specific stage
        Map<Integer, LocalTime[]> resultsForStage = stageResults.get(stageId);

        // Extract rider IDs from the results map
        int[] riderIds = resultsForStage.keySet().stream().mapToInt(Integer::intValue).toArray();

        return riderIds;
    }


    public static LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
        if (!racesById.containsKey(raceId)) {
            throw new IDNotRecognisedException("Race ID does not match any race in the system.");
        }

        int[] stageIds = Stage.getRaceStages(raceId); // Method to get all stages for the race

        Map<Integer, Duration> totalTimesForRiders = new HashMap<>();

        for (int stageId : stageIds) {
            int[] riderIds = Stage.getRidersInStage(stageId); 

            for (int riderId : riderIds) {
                LocalTime time = Stage.getRiderAdjustedElapsedTimeInStage(stageId, riderId); // Fetch adjusted time
                Duration duration = Duration.between(LocalTime.MIDNIGHT, time); // Convert to Duration

                totalTimesForRiders.merge(riderId, duration, Duration::plus); // Aggregate times
            }
        }

        // Convert aggregated durations back to LocalTime and sort
        List<LocalTime> sortedTimes = totalTimesForRiders.values().stream()
                .map(duration -> LocalTime.MIDNIGHT.plus(duration)) // Convert Duration back to LocalTime
                .sorted()
                .collect(Collectors.toList());

        // Convert list to array
        LocalTime[] timesArray = new LocalTime[sortedTimes.size()];
        sortedTimes.toArray(timesArray);

        return timesArray;
    }

    public static int[] getRidersPointsInRace(int raceId)  {
        // First, ensure the race exists


        // This map aggregates total points per rider
        Map<Integer, Integer> totalPointsPerRider = new HashMap<>();

        // Retrieve all stage IDs for the given race
        int[] stageIds = Stage.getRaceStages(raceId);
        if (stageIds.length == 0) {
            System.out.println("No stages found for the race ID: " + raceId);
            return new int[0]; // Return an empty array early if there are no stages
        }

        for (int stageId : stageIds) {
            
            int[] riderIds = Stage.getRidersRankInStage(stageId); 
            int[] stagePoints = Stage.getRidersPointsInStage(stageId); 

            if (riderIds.length != stagePoints.length) {
                System.out.println("Mismatch between rider IDs and points length for stage: " + stageId);
                continue; // Skip this stage due to mismatched data
            }

            for (int i = 0; i < riderIds.length; i++) {
                int riderId = riderIds[i];
                int points = stagePoints[i];
                totalPointsPerRider.merge(riderId, points, Integer::sum);
            }
        }

        // Convert map values to a list, sort it, and then convert to array
        List<Integer> sortedPoints = new ArrayList<>(totalPointsPerRider.values());
        sortedPoints.sort(Comparator.reverseOrder());

        return sortedPoints.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void removeRaceByName(String name) throws NameNotRecognisedException {
        // Find the race ID by name
        Integer raceIdToRemove = null;
        for (Race race : racesById.values()) {
            if (race.getName().equals(name)) {
                raceIdToRemove = race.getId();
                break;
            }
        }

        if (raceIdToRemove == null) {
            throw new NameNotRecognisedException("Race name not recognised: " + name);
        }

        // Remove the race by ID from racesById map
        racesById.remove(raceIdToRemove);

        // Now, find and remove all stages associated with this race
        List<Integer> stagesToRemove = new ArrayList<>();
        for (Map.Entry<Integer, Stage> entry : stagesById.entrySet()) {
            if (entry.getValue().getRaceId() == raceIdToRemove) {
                stagesToRemove.add(entry.getKey());
            }
        }

        // Remove stages from stagesById
        for (Integer stageId : stagesToRemove) {
            stagesById.remove(stageId);

            // Also remove any checkpoints and results associated with this stage
            checkpoints.clear();
            stageResults.remove(stageId);
        }


    }


    public static int[] getRidersMountainPointsInRace(int raceId)  {


        // Aggregate mountain points for riders across all stages
        Map<Integer, Integer> riderMountainPoints = new HashMap<>();
        for (int stageId : getRaceStages(raceId)) {
            int[] stageMountainPoints = getRidersMountainPointsInStage(stageId); // This returns points in finish order

           
            int[] ridersInStage = getRidersInStage(stageId);
            for (int i = 0; i < ridersInStage.length; i++) {
                riderMountainPoints.merge(ridersInStage[i], stageMountainPoints[i], Integer::sum);
            }
        }

        // Sort rider mountain points in descending order
        int[] pointsInGCOrder = riderMountainPoints.values().stream()
                .sorted(Comparator.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray();

        return pointsInGCOrder;
    }


    public static int[] getRidersMountainPointClassificationRank (int raceId) throws Exception {
        Map<Integer, Integer> overallRiderMountainPoints = new HashMap<>();

        for (Stage stage : getRaceStages2(raceId)) {
            int stageId = stage.getId();

            // Verify the stage exists
            if (!stagesById.containsKey(stageId)) {
                throw new IDNotRecognisedException("Stage ID does not match any stage in the system: " + stageId);
            }

            for (Checkpoint checkpoint : checkpoints) {
                int[] checkpointids = stage.getStageCheckpoints(stageId); // Assume this method exists in Stage class
                boolean containsCheckpoint = Arrays.stream(checkpointids).anyMatch(id -> id == checkpoint.getId());

                if (containsCheckpoint && checkpoint.getType() != CheckpointType.SPRINT) {
                    int[] points;
                    switch (checkpoint.getType()) {
                        case C1:
                            points = new int[]{10, 8, 6, 4, 2, 1};
                            break;
                        case C2:
                            points = new int[]{5, 3, 2, 1};
                            break;
                        case C3:
                            points = new int[]{2, 1};
                            break;
                        case C4:
                            points = new int[]{1};
                            break;
                        case HC:
                            points = new int[]{20, 15, 12, 10, 8, 6, 4, 2};
                            break;
                        default:
                            continue; // Skip if the checkpoint type is not a categorized climb
                    }

                    int[] riderOrder = getSprintRankingsAtCheckpoint(stageId, checkpoint.getId());
                    for (int i = 0; i < riderOrder.length && i < points.length; i++) {
                        int riderId = riderOrder[i];
                        overallRiderMountainPoints.merge(riderId, points[i], Integer::sum);
                    }
                }
            }
        }

        // Sort the riders by mountain points in descending order
        int[] sortedRiders = overallRiderMountainPoints.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .mapToInt(Map.Entry::getKey)
                .toArray();

        return sortedRiders;
    }


    public static List<Stage> getRaceStages2(int raceId) throws IDNotRecognisedException {
        // Check if the race ID exists
        if (!racesById.containsKey(raceId)) {
            throw new IDNotRecognisedException("The ID does not match any race in the system: " + raceId);
        }

        // Iterate over all stages to find those belonging to the specified race
        List<Stage> stagesForRace = new ArrayList<>();
        for (Stage stage : stagesById.values()) {
            if (stage.getRaceId() == raceId) {
                stagesForRace.add(stage);
            }
        }

        return stagesForRace;
    }

    public static int[] getRidersPointClassificationRank (int raceId) throws Exception {
        Map<Integer, Integer> riderPoints = new HashMap<>();

        // Iterate over all stages in the race
        for (Stage stage : getRaceStages2(raceId)) {
            int stageId = stage.getId();

            // Verify the stage exists
            if (!stagesById.containsKey(stageId)) {
                throw new IDNotRecognisedException("Stage ID does not match any stage in the system: " + stageId);
            }

            // Get rider IDs for the current stage
            int[] riderIds = getRidersRankInStage(stageId);

            // Calculate points for the current stage and update riderPoints
            calculateStagePoints(riderPoints, stage, riderIds);
        }

        // Extract rider IDs sorted by total points
        int[] sortedRiders = riderPoints.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .mapToInt(Map.Entry::getKey)
                .toArray();

        return sortedRiders;
    }

    static void calculateStagePoints(Map<Integer, Integer> riderPoints, Stage stage, int[] riderIds)  {
        // Points for intermediate sprints
        int[] sprintPoints = {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        // Assign sprint points for each checkpoint of type SPRINT
        for (Checkpoint checkpoint : stage.checkpoints) {
            if (checkpoint.getType() == CheckpointType.SPRINT) {
                int[] sprintRankings = getSprintRankingsAtCheckpoint(stage.getId(), checkpoint.getId());
                for (int rank = 0; rank < sprintRankings.length && rank < sprintPoints.length; rank++) {
                    riderPoints.merge(sprintRankings[rank], sprintPoints[rank], Integer::sum);
                }
            }
        }

        // Points allocation based on the stage type
        int[] finishLinePoints;
        switch (stage.getType()) {
            case FLAT:
                finishLinePoints = new int[]{50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2};
                break;
            case MEDIUM_MOUNTAIN:
                finishLinePoints = new int[]{30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
                break;
            case HIGH_MOUNTAIN:
            case TT:
                finishLinePoints = new int[]{20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
                break;
            default:
                throw new IllegalArgumentException("Unsupported stage type");
        }

        // Add finish line points based on rider finish order
        for (int i = 0; i < riderIds.length && i < finishLinePoints.length; i++) {
            riderPoints.merge(riderIds[i], finishLinePoints[i], Integer::sum);
        }
    }

    public static String viewRaceDetails(int raceId) throws IDNotRecognisedException {
        Race race = racesById.get(raceId);
        if (race == null) {
            throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
        }

        // Retrieves and formats race details including name, description, ID, and number of stages
        StringBuilder details = new StringBuilder();
        details.append("Race Details:\n");
        details.append("Name: ").append(race.getName()).append("\n");
        details.append("Description: ").append(race.getDescription()).append("\n");
        details.append("Race ID: ").append(raceId).append("\n");
        details.append("Number of Stages: ").append(stagesById.size()).append("\n");

        if (!stagesById.isEmpty()) {
            details.append("Stages:\n");
            for (Map.Entry<Integer, Stage> entry : stagesById.entrySet()) {
                Stage stage = entry.getValue();
                details.append("- Stage ").append(stage.getName()).append(": ").append(stage.getLength()).append(" km\n");
            }
        } else {
            details.append("No stages available for this race.\n");
        }

        return details.toString();
    }

    public static int getNumberOfStages(int raceId) {
        int count = 0;
        for (Stage stage : stagesById.values()) {
            if (stage.getRaceId() == raceId) {
                count++;
            }
        }
        return count;
    }

    public static void removeStageById(int stageId) {
        Iterator<Map.Entry<Integer, Stage>> iterator = stagesById.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Stage> entry = iterator.next();
            if (entry.getKey() == stageId) {
                iterator.remove();
                return; // Exit the method after removing the stage
            }
        }
    }



   
}
