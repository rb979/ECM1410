package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import static cycling.Race.races;
import static cycling.RaceResultsManager.getStageResults;
import static cycling.Team.teams;

/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortal interface.
 * 
 * @author Ryan Butler Hugo Blanco
 * @version 2.0
 *
 */
public class BadCyclingPortalImpl implements CyclingPortal {

	private List<Race> races;
	private List<Team> teams;
	private List<Stage> stages;
	private List<Rider> riders;
	
	@Override
	public int[] getRaceIds() {
		// TODO Auto-generated method stub
		List<Integer> raceIdsList = Race.getAllRaceIds();
		int[] raceIdsArray = new int[raceIdsList.size()];
		for (int i = 0; i < raceIdsList.size(); i++) {
			raceIdsArray[i] = raceIdsList.get(i);
		}
		return raceIdsArray;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		// TODO Auto-generated method stub
		Race race = new Race(name, description);
		return race.getId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		return Race.viewRaceDetails(raceId);
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		Race.removeRaceById(raceId);

	}
	

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
        	return Race.getNumberOfStages(raceId);
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		Stage stage = new Stage(raceId, stageName, description, length, startTime, type);
		Race.addStage(stage);
		return stage.getId();

	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		return Race.getRaceStages(raceId);
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		return Race.getStageLength(stageId);
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		Race.removeStageById(stageId);
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
            Double length) {
	    if (type == CheckpointType.SPRINT) {
	        throw new IllegalArgumentException("Checkpoint type cannot be SPRINT for categorized climb");
	    }
	    Checkpoint checkpoint = new Checkpoint(stageId, location, type, averageGradient, length);
	    Checkpoint.addCategorizedClimbToStage(stageId, location, type, averageGradient, length);
	    return checkpoint.getId();
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) {
	    Checkpoint checkpoint = new Checkpoint(stageId, location, CheckpointType.SPRINT, 0.0, 0.0);
	    Checkpoint.addIntermediateSprintToStage(stageId, location);
	    return checkpoint.getId();
	}
	
	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
	    for (int checkpoint : Stage.getStageCheckpoints()) {
	        if (checkpoint == checkpointId) {
	            Stage.removeCheckpoint(checkpoint);
	            return; 
	        }
	    }
	    throw new IDNotRecognisedException("Checkpoint ID not recognized: " + checkpointId);
	}
	
	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		return Stage.getStageCheckpoints(stageId);
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
	    return Team.createTeam(name, description);
	}
	
	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
        	Team.removeTeam(teamId);
    	}
	
	@Override
	public int[] getTeams() {
	    return Team.getTeams();
	}
	
	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
	    return Team.getTeamRiders(teamId);
	}
	
	@Override
	public int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
	    return Rider.createRider(teamID, name, yearOfBirth);
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		Rider.removeRider(riderId);
	}
	
	@Override
	    public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
            throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
            InvalidStageStateException {
	        
	    
    
   
	}



	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
	  
	}
	
	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
	  
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
	   
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
	  
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
	    
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eraseCyclingPortal(List<Race> races, List<Team> teams, List<Stage> stages, List<Rider> riders) {
		races.clear();
		teams.clear();
		stages.clear();
		riders.clear();
	}

	@Override
    	public void saveCyclingPortal(String filename) throws IOException {
	        try (FileOutputStream fileOut = new FileOutputStream(filename);
	             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
	
	            // Serialize the MiniCyclingPortal object
	            out.writeObject(this);
	            System.out.println("MiniCyclingPortal saved successfully to file: " + filename);
	        } catch (IOException e) {
	            System.err.println("Error saving MiniCyclingPortal: " + e.getMessage());
	            throw e;
	        }
	    }

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
	    try (FileInputStream fileIn = new FileInputStream(filename);
	         ObjectInputStream in = new ObjectInputStream(fileIn)) {
	
	        // Deserialize the contents from the file
	        MiniCyclingPortal deserializedPortal = (MiniCyclingPortal) in.readObject();
	
	        // Replace the contents of the current MiniCyclingPortal with the deserialized contents
	        this.races = deserializedPortal.races;
	        this.teams = deserializedPortal.teams;
	        this.stages = deserializedPortal.stages;
	        this.riders = deserializedPortal.riders;
	
	        System.out.println("MiniCyclingPortal loaded successfully from file: " + filename);
	    } catch (IOException | ClassNotFoundException e) {
	        System.err.println("Error loading MiniCyclingPortal: " + e.getMessage());
	        throw e;
	    }
	}

	@Override
	public void removeRaceByName(String name) {
		Iterator<Race> iterator = races.iterator();
		while (iterator.hasNext()) {
			Race race = iterator.next();
			if (race.getName().equals(name)) {
				iterator.remove();
				return;
			}
		}
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

}
