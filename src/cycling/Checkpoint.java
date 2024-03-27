package cycling;

/**
 * A class used to create an instance of a checkpoint.
 * These checkpoints will belong to an instance of a stage class.
 * 
 * Authors: Ryan Butler and Hugo Blanco
 */
public class Checkpoint {
    // Fields
    private static int idCounter = 0; // Static counter to generate checkpoint IDs
    private final int id; // Unique identifier for the checkpoint
    private static int stageId = 0; // The ID of the stage to which the checkpoint belongs
    private double location; // Location of the checkpoint
    private final CheckpointType type; // Type of the checkpoint
    private final double averageGradient; // Average gradient of the checkpoint
    private static double length; // Length of the checkpoint

    /**
     * Constructor to create a new Checkpoint instance.
     * 
     * @param stageId          The ID of the stage to which the checkpoint belongs
     * @param location         The location of the checkpoint
     * @param type             The type of the checkpoint
     * @param averageGradient  The average gradient of the checkpoint
     * @param length           The length of the checkpoint
     */
    public Checkpoint(int stageId, double location, CheckpointType type, double averageGradient, double length) {
        this.id = ++idCounter; // Assign a unique ID to the checkpoint
        this.stageId = stageId;
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
    }

    // Getter methods
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

    /**
     * Method to add a categorized climb checkpoint to a stage.
     * 
     * @param stageId          The ID of the stage
     * @param location         The location of the checkpoint
     * @param type             The type of the checkpoint
     * @param averageGradient  The average gradient of the checkpoint
     * @param length           The length of the checkpoint
     * @return The ID of the added checkpoint
     * @throws IDNotRecognisedException    If the stage ID is not recognized
     * @throws InvalidLocationException    If the location is invalid
     * @throws InvalidStageStateException If the stage state is invalid
     * @throws InvalidStageTypeException  If the stage type is invalid
     */
    public static int addCategorizedClimbToStage(int stageId, double location, CheckpointType type,
                                                 double averageGradient, double length) throws IDNotRecognisedException,
            InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        // Create a new checkpoint instance
        Checkpoint checkpoint = new Checkpoint(stageId, location, type, averageGradient, length);
        // Add the checkpoint to the stage
        Stage.addCheckpoint(stageId, checkpoint);
        return checkpoint.getId();
    }

    /**
     * Method to add an intermediate sprint checkpoint to a stage.
     * 
     * @param stageId  The ID of the stage
     * @param location The location of the checkpoint
     * @return The ID of the added checkpoint
     * @throws IDNotRecognisedException    If the stage ID is not recognized
     * @throws InvalidLocationException    If the location is invalid
     * @throws InvalidStageStateException If the stage state is invalid
     * @throws InvalidStageTypeException  If the stage type is invalid
     */
    public static int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
            InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        // Create a new checkpoint instance for intermediate sprint
        Checkpoint checkpoint = new Checkpoint(stageId, location, CheckpointType.SPRINT, 0.0, 0.0);
        // Add the checkpoint to the stage
        Stage.addCheckpoint(stageId, checkpoint);
        return checkpoint.getId();
    }
}
