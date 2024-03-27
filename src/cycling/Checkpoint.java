package cycling;

/**
 * A class used to create an instance of a team.
 * 
 * @author Ryan Butler and Hugo Blanco
 * 
 */

import java.util.concurrent.atomic.AtomicInteger;

public class Checkpoint {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int id;
    private final double location;
    private final CheckpointType type;
    private final double averageGradient;
    private final double length;

    public Checkpoint(double location, CheckpointType type, double averageGradient, double length) {
        this.id = idGenerator.incrementAndGet();
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
    }

    // Getters
    public int getId() {
        return id;
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

    public double getLength() {
        return length;
    }
}
