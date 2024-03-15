package cycling;

import java.time.LocalDateTime;

public class Stage {
    private static int stageIdCounter = 0;
    private final int stageId;
    private final int raceId;
    private final String name;
    private final String description;
    private final double length;
    private final LocalDateTime startTime;
    private final StageType type;

    public Stage(int raceId, String name, String description, double length, LocalDateTime startTime, StageType type) {
        this.stageId = ++stageIdCounter;
        this.raceId = raceId;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
    }

    public int getStageId() {
        return stageId;
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
}
