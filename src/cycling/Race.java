package cycling;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class Race {

    private final String name;
    private final String description;
    private final int id;
    private static int idCounter = 0;
    private static List<Integer> allRaceIds = new ArrayList<>();



    public Race (String name, String description ) {
        this.name = name;
        this.description = description;
        this.id = ++idCounter;
        allRaceIds.add(this.id);


    }

    public int getId() {
        return id;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public static List<Integer> getAllRaceIds(){
        return new ArrayList<>(allRaceIds);
    }

    public static void removeRaceById(int raceId) {
        Iterator<Integer> iterator = allRaceIds.iterator();
        while (iterator.hasNext()) {
            Integer id = iterator.next();
            if (id == raceId) {
                iterator.remove();
                return; // Exit the method after removing the race
            }
        }

    }






}
