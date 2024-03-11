package cycling;

/**
 * A class used to create an instance of a rider.
 * These riders will belong to an instance of a team class.
 * 
 * @author Ryan Butler
 * 
 */

public class Rider {
    int teamID;
    String name;
    int yearOfBirth;
    int riderId;
    static int nextRiderId = 0;

    public Rider(int teamID, String name, int yearOfBirth) {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.riderId = nextRiderId;
        nextRiderId++;
    }

    public int getTeamID() {
        return teamID;
    }

    public int getId() {
        return riderId;
    }

}
