package cycling;

import java.util.ArrayList;
import java.util.List;

public class RaceResultsManager {

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
