package cycling;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cycling.Team.teams;

/**
 * A class used to create an instance of a rider.
 * These riders will belong to an instance of a team class.
 *
 * @author Ryan Butler and Hugo Blanco
 *
 */

public class Rider {
    private int teamID;
    private String name;
    private int yearOfBirth;
    private int riderId;
    private static int nextRiderId = 1;
    private Map<Integer, LocalTime[]> resultsByStage = new HashMap<>();
    public static List<Rider> riders = new ArrayList<>();

    public Rider(int teamID, String name, int yearOfBirth) {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.riderId = nextRiderId++;
        riders.add(this);
    }

    public static int getNextRiderId() {
        return nextRiderId;
    }
    
    public int getTeamID() {
        return teamID;
    }

    public int getId() {
        return riderId;
    }

    public static int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        if (yearOfBirth < 1900) {
            throw new IllegalArgumentException("Invalid Year of Birth");
        }
        try {
            for (Team t : teams) {
                if (teamID == t.getId()) {
                    t.addRider(name, yearOfBirth);
                    return Rider.getNextRiderId();
                }
            }
            throw new IDNotRecognisedException("Team ID Not Recognised");
        } catch (IDNotRecognisedException e) {
            throw e;
        }
    }

    public static void removeRider(int riderId) throws IDNotRecognisedException {
        try{
            for(Team t:teams){
                for(Rider r: t.getRiders()){
                    if(r.getId()==riderId){
                        t.removeRider(r);
                    }
                }
            }throw new IDNotRecognisedException("ID Not Recognised");
        }catch(IDNotRecognisedException e){
            System.out.println(e);
        }
    }

    public void setResultsForStage(int stageId, LocalTime[] checkpoints) {
        resultsByStage.put(stageId, checkpoints);
    }

    public boolean hasResultsForStage(int stageId) {
        return resultsByStage.containsKey(stageId);
    }

    public LocalTime[] getResultsForStage(int stageId) {
        return resultsByStage.get(stageId);
    }

    public static Rider retrieveRiderById(int riderId) {
        for (Rider rider : riders) {
            if (rider.getId() == riderId) {
                return rider;
            }
        }
        return null;
    }

    public void removeResultsForStage(int stageId) {
        resultsByStage.remove(stageId);
    }

    public static List<Integer> getAllParticipatingRiderIds(int stageId) {
        List<Integer> participatingRiderIds = new ArrayList<>();
        for (Rider rider : riders) {
            if (rider.hasResultsForStage(stageId)) {
                participatingRiderIds.add(rider.getId());
            }
        }
        return participatingRiderIds;
    }

    public static void sortRiderIdsByFinishTime(List<Integer> allRiderIds, int stageId) {
        allRiderIds.sort((riderId1, riderId2) -> {
            Rider rider1 = retrieveRiderById(riderId1);
            Rider rider2 = retrieveRiderById(riderId2);
            LocalTime[] times1 = rider1.getResultsForStage(stageId);
            LocalTime[] times2 = rider2.getResultsForStage(stageId);
            if (times1 != null && times2 != null && times1.length > 0 && times2.length > 0) {
                return times1[times1.length - 1].compareTo(times2[times2.length - 1]);
            }
            return 0;
        });
    }
}
