package cycling;

import java.time.LocalDateTime;
import java.util.*;

public class Stage {
    private String state;

    private static int idCounter = 0;
    private final int id;
    private final int raceId;
    private final String name;
    private final String description;
    private final double length;
    private final LocalDateTime startTime;
    private final StageType type;
    private List<Checkpoint> checkpoints; 
    
    public Stage(int raceId, String name, String description, double length, LocalDateTime startTime, StageType type) {
        this.id = ++idCounter;
        this.raceId = raceId;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        this.checkpoints = new ArrayList<>(); 
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

    public void addCheckpoint(int stageId, Checkpoint checkpoint) {
        checkpoints.add(checkpoint); 
    }

    public static void removeCheckpoint(int checkpointId) {
        Iterator<Checkpoint> iterator = checkpoints.iterator();
        while (iterator.hasNext()) {
            Checkpoint checkpoint = iterator.next();
            if (checkpoint.getId() == checkpointId) {
                iterator.remove();
                return; 
            }
        }
    }

    public int[] getStageCheckpoints() {
        checkpoints.sort(Comparator.comparingDouble(Checkpoint::getLocation));
        
        int[] checkpointIds = new int[checkpoints.size()];
        for (int i = 0; i < checkpoints.size(); i++) {
            checkpointIds[i] = checkpoints.get(i).getId();
        }
        return checkpointIds;
    }
}
