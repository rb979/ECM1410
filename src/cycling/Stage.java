package cycling;

import java.time.LocalDateTime;
import java.util.*;

public class Stage {
    private static String state;

    private static int idCounter = 0;
    private final int id;
    private final int raceId;
    private final String name;
    private final String description;
    private final double length;
    private final LocalDateTime startTime;
    private final StageType type;
    static List<Checkpoint> checkpoints = new ArrayList<>();


    public Stage(int raceId, String name, String description, double length, LocalDateTime startTime, StageType type) {
        this.id = ++idCounter;
        this.raceId = raceId;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;

    }

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


    public static void addCheckpoint(Checkpoint checkpoint) {
        // Initialize the list of checkpoints if it's null
        if (checkpoints == null) {
            checkpoints = new ArrayList<>();
        }
        // Add the checkpoint to the list
        checkpoints.add(checkpoint);
    }


    public static void RemoveCheckpoint(int checkpointid) {
        Iterator<Checkpoint> iterator = checkpoints.iterator();
        while (iterator.hasNext()) {
            Checkpoint id = iterator.next();
            if (id.getId() == checkpointid) {
                iterator.remove();
                return; // Exit the method after removing the checkpoint
                }
            }
    }

    public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
        if (this.id != stageId) {
            throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
        }

        // Sort the checkpoints based on their location
        List<Checkpoint> sortedCheckpoints = new ArrayList<>(checkpoints);
        Collections.sort(sortedCheckpoints, Comparator.comparingDouble(Checkpoint::getLocation));


        // Extract checkpoint IDs into an array
        int[] checkpointIds = new int[sortedCheckpoints.size()];
        for (int i = 0; i < sortedCheckpoints.size(); i++) {
            checkpointIds[i] = sortedCheckpoints.get(i).getId();
        }
        return checkpointIds;
    }


















}
