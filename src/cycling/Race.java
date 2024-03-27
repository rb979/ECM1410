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
import java.util.Iterator;

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

    /**
     *  Retrieves and formats race details including name, description, ID, and number of stages
     * 
     * @return The details of the race.
     */
    public static String viewRaceDetails(int raceId) throws IDNotRecognisedException {
        // Retrieves and formats race details including name, description, ID, and number of stages
        StringBuilder details = new StringBuilder();
        details.append("Race Details:\n");
        details.append("Name: ").append(name).append("\n");
        details.append("Description: ").append(description).append("\n");
        details.append("Race ID: ").append(raceId).append("\n");
        details.append("Number of Stages: ").append(stages.size()).append("\n");

        if (!stages.isEmpty()) {
            details.append("Stages:\n");
            for (Stage stage : stages) {
                details.append("- Stage ").append(stage.getName()).append(": ").append(stage.getLength()).append(" km\n");
            }
        } else {
            details.append("No stages available for this race.\n");
        }

        return details.toString();
    }

    public static int getNumberOfStages(int raceid) {
        int count = 0;
        for(Stage stage : stages ) {
            if (stage.getRaceId() == raceid){
                count++;

            }

        }

        return count;
    }

    public static void removeStageById(int raceId) {
        Iterator<Stage> iterator = stages.iterator();
        while (iterator.hasNext()) {
            Stage id = iterator.next();
            if (id.getId() == raceId) {
                iterator.remove();
                return; // Exit the method after removing the race
            }
        }


    }
}
