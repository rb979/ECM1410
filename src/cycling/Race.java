package cycling;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;

public class Race {
    private static final AtomicInteger idGenerator = new AtomicInteger();
    static final Map<Integer, Race> racesById = new HashMap<>();
    static final Map<String, Integer> raceIdsByName = new HashMap<>();

    private final int id;
    private final String name;
    private final String description;

    private Race(String name, String description) {
        this.id = idGenerator.incrementAndGet();
        this.name = name;
        this.description = description;
    }
    public static void resetRaces() {
        racesById.clear();
        raceIdsByName.clear();
        idGenerator.set(0); // Or set to 1, depending on how your ID numbering should start
    }

    public static int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
        validateRaceName(name);
        if (raceIdsByName.containsKey(name)) {
            throw new IllegalNameException("Race name already exists: " + name);
        }
        Race newRace = new Race(name, description);
        racesById.put(newRace.id, newRace);
        raceIdsByName.put(name, newRace.id);
        return newRace.id;
    }

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

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Additional methods as needed for race management...
}
