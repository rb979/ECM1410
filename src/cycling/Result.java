package cycling;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Result keeps track of results within a certain stage
 * @author Ryan Butler
 */


public class Result implements java.io.Serializable {
    private int raceId;
    private int stageId;
    private int riderId;
    private int resultId;
    private static ArrayList<Integer> resultIds = new ArrayList<>();
    
    private LocalTime resultElapsedTime;
    private ArrayList<LocalTime> checkpoints = new ArrayList<LocalTime>();

    public Result(int raceId, int stageId, int riderId, LocalTime... checkpoints) {
        this.raceId = raceId;
        this.stageId = stageId;
        this.riderId = riderId;

        for (LocalTime check : checkpoints) {
            this.checkpoints.add(check);
        }

        this.resultElapsedTime = calculateElapsedTime();
        this.resultId = generateResultId();
    }

    public LocalTime getResultElapsedTime() {
        return resultElapsedTime;
    }

    private LocalTime calculateElapsedTime() {
        LocalTime startTime = checkpoints.get(0);
        LocalTime finishTime = checkpoints.get(checkpoints.size() - 1);
        return LocalTime.ofSecondOfDay(ChronoUnit.SECONDS.between(startTime, finishTime));
    }

    private int generateResultId() {
        return resultIds.isEmpty() ? 0 : resultIds.get(resultIds.size() - 1) + 1;
    }

    public int getRaceId() {
        return raceId;
    }

    public int getResultId() {
        return resultId;
    }

    public int getRiderId() {
        return riderId;
    }

    public ArrayList<LocalTime> getCheckpoints() {
        return checkpoints;
    }

    public int getStageId() {
        return stageId;
    }
    
    public static List<Result> getStageResults(int stageId, List<Result> allResults) {
    List<Result> stageResults = new ArrayList<>();
        for (Result result : allResults) {
            if (result.getStageId() == stageId) {
                stageResults.add(result);
            }
        }
        return stageResults;
    }

}

