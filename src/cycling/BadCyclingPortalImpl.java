package cycling;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cycling.Race.raceIdsByName;
import static cycling.Race.racesById;
import static cycling.Stage.*;


/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortal interface.
 * 
 * @author Diogo Pacheco
 * @version 2.0
 *
 */
public class BadCyclingPortalImpl implements CyclingPortal {

	@Override
	public int[] getRaceIds() {
		List<Integer> raceIdsList = new ArrayList<>(racesById.keySet());
		int[] raceIdsArray = new int[raceIdsList.size()];
		for (int i = 0; i < raceIdsList.size(); i++) {
			raceIdsArray[i] = raceIdsList.get(i);
		}
		return raceIdsArray;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
        return Race.createRace(name, description);
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		return Stage.viewRaceDetails(raceId);

	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		// Check if the raceId exists in the racesById map.
		if (!racesById.containsKey(raceId)) {
			throw new IDNotRecognisedException("The ID does not match any race in the system: " + raceId);
		}

		// Retrieve the race to get its name, as we'll need it to remove the entry from raceIdsByName map.
		Race race = racesById.get(raceId);
		String raceName = race.getName();

		// Remove the race from both maps. It's important to remove the race from all related data structures
		// to ensure the state of the system is consistent.
		racesById.remove(raceId);
		raceIdsByName.remove(raceName);

	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		return Stage.getNumberOfStages(raceId);
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
        return Stage.addStageToRace(raceId, stageName, description, length, startTime, type);
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		return Stage.getRaceStages(raceId);
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		return Stage.getStageLength(stageId);
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		Stage.removeStageById(stageId);

	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		return Stage.addCategorizedClimbToStage(stageId,location,type,averageGradient,length);
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		return addIntermediateSprintToStage(stageId,location);
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage.removeCheckpoint(checkpointId);

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage.concludeStagePreparation(stageId);

	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		return Stage.getStageCheckpoints(stageId);
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		return Team.createTeam(name,description);
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
		return Rider.getTeamRiders(teamId);
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		return Rider.createRider(teamID,name,yearOfBirth);
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		Rider.removeRider(riderId);

	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		Stage.registerRiderResultsInStage(stageId,riderId,checkpoints);

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		return getRiderResultsInStage(stageId,riderId);
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		return Stage.getRiderAdjustedElapsedTimeInStage(stageId,riderId);
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Stage.deleteRiderResultsInStage(stageId,riderId);

	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		return Stage.getRidersRankInStage(stageId);
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		return Stage.getRankedAdjustedElapsedTimesInStage(stageId);
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws Exception {
		return Stage.getRidersPointsInStage(stageId);

    }

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws Exception {
		return Stage.getRidersMountainPointsInStage(stageId);
	}

	@Override
	public void eraseCyclingPortal() {
		Race.resetRaces();
		Stage.resetStages();
		Team.resetTeams();
		Rider.resetRiders();

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

			System.out.println("MiniCyclingPortal loaded successfully from file: " + filename);
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error loading MiniCyclingPortal: " + e.getMessage());
			throw e;
		}

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		Stage.removeRaceByName(name);

	}




	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		return Stage.getGeneralClassificationTimesInRace(raceId);
	}

	@Override
	public int[] getRidersPointsInRace(int raceId)  {
		return Stage.getRidersPointsInRace(raceId);
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId)  {
		return Stage.getRidersMountainPointsInRace(raceId);
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		return Stage.getRidersGeneralClassificationRank(raceId);
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId)  throws IDNotRecognisedException {
		Map<Integer, Integer> riderPoints = new HashMap<>();

		// Iterate over all stages in the race
		for (Stage stage : getRaceStages2(raceId)) {
			int stageId = stage.getId();

			// Verify the stage exists
			if (!stagesById.containsKey(stageId)) {
				throw new IDNotRecognisedException("Stage ID does not match any stage in the system: " + stageId);
			}

			// Get rider IDs for the current stage
			int[] riderIds = getRidersRankInStage(stageId);

			// Calculate points for the current stage and update riderPoints
			calculateStagePoints(riderPoints, stage, riderIds);
		}

		// Extract rider IDs sorted by total points
		int[] sortedRiders = riderPoints.entrySet().stream()
				.sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
				.mapToInt(Map.Entry::getKey)
				.toArray();

		return sortedRiders;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		return getRidersMountainPointClassificationRank(raceId);
	}

}
