package cycling;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalTime;

public class Stage {
    StageState stageState;
    StageType stageType;
    String stageName;
    String stageDescription;
    double stageLength;
    LocalDateTime stageStartTime;
    static int nextStageID = 0;
    int id;
    ArrayList<Segment> segments;
    ArrayList<Results> results;

    public Stage(String stageName, String stageDescription, double stageLength, LocalDateTime stageStartTime, StageType
            stageType) {
        this.stageState = StageState.UNDER_PREPARATION;
        this.stageType = stageType;
        this.stageName = stageName;
        this.stageDescription = stageDescription;
        this.stageLength = stageLength;
        this.stageStartTime = stageStartTime;
        this.id = nextStageID;
        this.segments = new ArrayList<Segment>();
        nextStageID++;
        this.results = new ArrayList<Results>();
    }

    public String getStageName() {return stageName;}

    public double getStageLength() {return stageLength;}

    public int getId() {
        return id;
    }

    public static int getCurId() {
        return nextStageID-1;
    }

    public StageState getStageState() {
        return stageState;
    }

    public ArrayList<Segment> getSegments() {return segments;}

    public StageType getStageType() {return stageType;}

    public ArrayList<Results> getResults() {return results;}

    public void addResults(int riderID, LocalTime[] checkPoints){
        results.add(new Results(riderID, checkPoints));
    }

    public void addSegment(double loc, double segmentLength, SegmentType type, double avgGradient){
        segments.add(new Segment (loc,segmentLength,type,avgGradient));
    }
    public void addSegment(double loc){
        segments.add(new Segment(loc,0,SegmentType.SPRINT,0));
    }
    public void removeSegment(Segment seg){
        segments.remove(seg);
    }

    public void setStageState(StageState stageState) {
        this.stageState = stageState;
    }
}
