package cycling;

/**
 * A class used to create an instance of a race.
 * This class provides methods for managing races, such as creating races,
 * resetting race data, and validating race names.
 * 
 * Authors: Ryan Butler and Hugo Blanco
 */
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;

public class Race {
    // Static variables to generate unique IDs for each race and store races by ID or name
    private static final AtomicInteger idGenerator = new AtomicInteger();
    static final Map<Integer, Race> racesById = new HashMap<>();
    static final Map<String, Integer> raceIdsByName = new HashMap<>();

    // Instance variables
    private final int id; // Unique ID of the race
    private final String name; // Name of the race
    private final String description; // Description of the race

    /**
     * Private constructor to create a new instance of a race.
     * 
     * @param name        The name of the race.
     * @param description The description of the race.
     */
    private Race(String name, String description) {
        this.id = idGenerator.incrementAndGet(); // Assign a unique ID to the race
        this.name = name;
        this.description = description;
    }

    /**
     * Reset all race data, including clearing race maps and resetting the ID generator.
     */
    public static void resetRaces() {
        racesById.clear(); // Clear the map of races by their ID
        raceIdsByName.clear(); // Clear the map of race IDs by their name
        idGenerator.set(0); // Reset the ID generator to start from 0 (or 1, depending on your ID numbering scheme)
    }

    /**
     * Create a new race with the given name and description.
     * 
     * @param name        The name of the race.
     * @param description The description of the race.
     * @return The ID of the newly created race.
     * @throws IllegalNameException  If the race name already exists.
     * @throws InvalidNameException If the race name is null, empty, too long, or contains white spaces.
     */
    public static int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
        validateRaceName(name); // Validate the race name
        if (raceIdsByName.containsKey(name)) {
            throw new IllegalNameException("Race name already exists: " + name);
        }
        Race newRace = new Race(name, description); // Create a new race
        racesById.put(newRace.id, newRace); // Store the race by its ID
        raceIdsByName.put(name, newRace.id); // Store the race ID by its name
        return newRace.id; // Return the ID of the newly created race
    }

    /**
     * Validate the given race name.
     * 
     * @param name The name of the race to validate.
     * @throws InvalidNameException If the race name is null, empty, too long, or contains white spaces.
     */
    private static void validateRaceName(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Race name cannot be null or empty.");
        }
        if (name.length() > 30) {
            throw new InvalidNameException("Race name cannot have more than 30 characters.");
        }
        if (name.contains(" ")) {
            throw new InvalidNameException("Race name cannot contain white spaces.");
        }
    }

    // Getters for instance variables

    /**
     * Get the ID of the race.
     * 
     * @return The ID of the race.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the race.
     * 
     * @return The name of the race.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the race.
     * 
     * @return The description of the race.
     */
    public String getDescription() {
        return description;
    }

    // Additional methods as needed for race management...
}
