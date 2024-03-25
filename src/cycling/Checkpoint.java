package cycling;

public class Checkpoint {
    private static int idCounter = 0;
    private final int id;
    private static int stageId = 0;
    private double location;
    private final CheckpointType type;
    private final double averageGradient;
    private static double length;

    public Checkpoint(int stageId, double location, CheckpointType type, double averageGradient, double length) {
        this.id = ++idCounter;
        this.stageId = stageId;
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public static int getStageId() {
        return stageId;
    }

    public double getLocation() {
        return location;
    }

    public CheckpointType getType() {
        return type;
    }

    public double getAverageGradient() {
        return averageGradient;
    }

    public static double getLength() {
        return length;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static int addCategorizedClimbToStage(int stageId, double location, CheckpointType type,
                                                 double averageGradient, double length){
        Checkpoint checkpoint = new Checkpoint(stageId, location, type, averageGradient, length);
        Stage.addCheckpoint(checkpoint);
        return checkpoint.getId();


    }

    public static int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
            InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        Checkpoint checkpoint = new Checkpoint(stageId, location, CheckpointType.SPRINT, 0.0, 0.0);
        Stage.addCheckpoint(checkpoint);

        return checkpoint.getId();
    }









}


