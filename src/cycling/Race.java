package cycling;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class Race {
    private final String name;
    private final String description;
    private final int id;
    private static int idCounter = 0;
    private static final List<Integer> allRaceIds = new ArrayList<>();
    private static List<Stage> stages = new ArrayList<>(); // Add a list to store stages of the race




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

    public static void addStage(Stage stage) {
        stages.add(stage); // Add the stage to the list of stages
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

    public static int[] getRaceStages(int raceId) {
        List<Integer> raceStages = new ArrayList<>();
        for (Stage stage : stages) {
            if (stage.getRaceId() == raceId) {
                raceStages.add(stage.getId());
            }
        }
        // Convert List<Integer> to int[]
        int[] stagesArray = new int[raceStages.size()];
        for (int i = 0; i < raceStages.size(); i++) {
            stagesArray[i] = raceStages.get(i);
        }
        return stagesArray;
    }

    public static double getStageLength(int stageId) throws IDNotRecognisedException{
        for (Stage stage : stages) {
            if (stage.getId() == stageId) {
                return stage.getLength();
            }
        }
        // If no stage with the given ID is found, throw an exception
        throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
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






























