package cycling;

/**
 * A class used to create an instance of a checkpoint.
 * Each checkpoint represents a specific location along a cycling route.
 * It stores information such as location, type, average gradient, and length.
 * 
 * Authors: Ryan Butler and Hugo Blanco
 */
import java.util.concurrent.atomic.AtomicInteger;

public class Checkpoint {
    // Static variable to generate unique IDs for each checkpoint
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    // Instance variables
    private final int id;
    private final double location; // Location along the route
    private final CheckpointType type; // Type of checkpoint (e.g., start, finish, intermediate)
    private final double averageGradient; // Average gradient at the checkpoint
    private final double length; // Length of the route segment associated with the checkpoint

    /**
     * Constructor to initialize a new checkpoint instance.
     * 
     * @param location         The location along the route where the checkpoint is located.
     * @param type             The type of the checkpoint (e.g., start, finish, intermediate).
     * @param averageGradient  The average gradient at the checkpoint.
     * @param length           The length of the route segment associated with the checkpoint.
     */
    public Checkpoint(double location, CheckpointType type, double averageGradient, double length) {
        // Assign a unique ID to the checkpoint
        this.id = idGenerator.incrementAndGet();
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
    }

    // Getters for instance variables

    /**
     * Get the ID of the checkpoint.
     * 
     * @return The ID of the checkpoint.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the location of the checkpoint along the route.
     * 
     * @return The location of the checkpoint.
     */
    public double getLocation() {
        return location;
    }

    /**
     * Get the type of the checkpoint.
     * 
     * @return The type of the checkpoint.
     */
    public CheckpointType getType() {
        return type;
    }

    /**
     * Get the average gradient at the checkpoint.
     * 
     * @return The average gradient at the checkpoint.
     */
    public double getAverageGradient() {
        return averageGradient;
    }

    /**
     * Get the length of the route segment associated with the checkpoint.
     * 
     * @return The length of the route segment associated with the checkpoint.
     */
    public double getLength() {
        return length;
    }
}
