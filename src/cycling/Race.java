package cycling;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class Race {
    private static String name = null;
    private static String description = null;
    private final int id;
    private static int idCounter = 0;
    private static final List<Integer> allRaceIds = new ArrayList<>();
    private static List<Stage> stages = new ArrayList<>(); // Add a list to store stages of the race

    static List<Race> races = new ArrayList<>();



    public Race (String name, String description ) {
        this.name = name;
        this.description = description;
        this.id = ++idCounter;
        allRaceIds.add(this.id);
        races.add(this);


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

    public static String viewRaceDetails(int raceId) throws IDNotRecognisedException {
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

    public static void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
        boolean stageFound = false;
        for (Stage stage : stages) {
            if (stage.getId() == stageId) {
                stage.setState(String.valueOf(StageState.CONCLUDED)); // Update the state using the StageState enum
                stageFound = true;
                break;
            }
        }
        if (!stageFound) {
            throw new IDNotRecognisedException("Stage ID not recognized: " + stageId);
        }
    }



    public static LocalDateTime getStageStartTime(int stageId) throws IDNotRecognisedException {
        for (Stage stage : stages) {
            if (stage.getId() == stageId) {
                return stage.getStartTime();
            }
        }
        // If no stage with the given ID is found, throw an exception
        throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
    }

    public static Stage getStageById(int stageId) throws IDNotRecognisedException {
        for (Stage stage : stages) {
            if (stage.getId() == stageId) {
                return stage;
            }
        }
        throw new IDNotRecognisedException("Stage with ID " + stageId + " not found.");
    }




    }






























