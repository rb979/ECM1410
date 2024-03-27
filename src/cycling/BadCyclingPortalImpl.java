package cycling;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.io.*;


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
		Race.concludeStagePreparation(stageId);
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
		// Initialize an empty array to store checkpoint times and elapsed time
		LocalTime[] resultTimes = new LocalTime[0];

		// Find the stage by ID
		Stage stage = null;
		for (Stage s : Race.stages) {
			if (s.getId() == stageId) {
				stage = s;
				break;
			}
		}

		// Throw exception if stage not found
		if (stage == null) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found.");
		}

		// Retrieve the rider
		Rider rider = retrieveRiderById(riderId);

		// Throw exception if rider not found
		if (rider == null) throw new IDNotRecognisedException("Rider with ID " + riderId + " not found.");

		LocalTime[] checkpointTimes = rider.getResultsForStage(stageId);
		// Calculate total elapsed time
		LocalTime startTime = checkpointTimes[0];
		LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
		Duration totalElapsedTime = Duration.between(startTime, finishTime);

		List<LocalTime> resultList = new ArrayList<>();
		for (LocalTime checkpointTime : checkpointTimes) {
			resultList.add(checkpointTime);
		}

		resultList.add(startTime.plus(totalElapsedTime));  // Add total elapsed time to the start time

		return resultList.toArray(new LocalTime[0]);





	}
	
	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId)
			throws IDNotRecognisedException {
		Stage stage = Race.getStageById(stageId); // Retrieve the stage by ID

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found.");
		}

		Rider rider = retrieveRiderById(riderId); // Retrieve the rider by ID

		// Check if the rider exists
		if (rider == null) {
			throw new IDNotRecognisedException("Rider with ID " + riderId + " not found.");
		}

		// Get the results for the specified stage and rider
		LocalTime[] checkpointTimes = rider.getResultsForStage(stageId);

		// If no results are found, return null
		if (checkpointTimes == null || checkpointTimes.length == 0) {
			return null;
		}

		// Iterate through all checkpoints for the rider and focus on the last one
		LocalTime lastCheckpointTime = checkpointTimes[checkpointTimes.length - 1];

		// Initialize adjusted time with the rider's original time
		LocalTime adjustedTime = lastCheckpointTime;

		// Iterate through all riders to compare finishing times
		for (Rider r : Rider.riders) {
			if (r.getId() != riderId) {
				// Get the finishing time of the other rider
				LocalTime[] otherTimes = r.getResultsForStage(stageId);
				if (otherTimes != null && otherTimes.length > 0) {
					// Compare finishing times
					for (LocalTime time : otherTimes) {
						Duration difference = Duration.between(time, lastCheckpointTime).abs();
						if (difference.getSeconds() < 1) {
							// If the difference is less than 1 second, update adjusted time to the faster rider's time
							adjustedTime = time;
						}
					}
				}
			}
		}

		return adjustedTime;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// Retrieve the stage by ID
		Stage stage = Race.getStageById(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found.");
		}

		// Retrieve the rider by ID
		Rider rider = retrieveRiderById(riderId);

		// Check if the rider exists
		if (rider == null) {
			throw new IDNotRecognisedException("Rider with ID " + riderId + " not found.");
		}

		// Remove the stage results from the rider
		rider.removeResultsForStage(stageId);


	}


	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// Get the list of all participating rider IDs in the stage
		List<Integer> participatingRiderIds = getAllParticipatingRiderIds(stageId);

		// Sort the participating rider IDs based on finish time
		sortRiderIdsByFinishTime(participatingRiderIds, stageId);

		// Convert the list of rider IDs to an array
		int[] rankedRiderIds = new int[participatingRiderIds.size()];
		for (int i = 0; i < rankedRiderIds.length; i++) {
			rankedRiderIds[i] = participatingRiderIds.get(i);
		}

		return rankedRiderIds;

	}


	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {

		// Retrieve participating rider IDs for the stage
		List<Integer> riderIds = Rider.getAllParticipatingRiderIds(stageId);

		// Sort rider IDs by finish time
		Rider.sortRiderIdsByFinishTime(riderIds, stageId);

		// Initialize a list to store adjusted elapsed times
		List<LocalTime> adjustedTimes = new ArrayList<>();

		// Iterate over rider IDs and calculate adjusted elapsed time
		for (Integer riderId : riderIds) {
			// Get adjusted elapsed time for the rider
			LocalTime adjustedTime = getRiderAdjustedElapsedTimeInStage(stageId, riderId);

			// Add adjusted time to the list
			adjustedTimes.add(adjustedTime);
		}

		// Convert list to array
		LocalTime[] adjustedTimesArray = adjustedTimes.toArray(new LocalTime[0]);

		return adjustedTimesArray;


	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {

			// Retrieve the ranked list of rider IDs based on finish time
			int[] rankedRiderIds = getRidersRankInStage(stageId);

			// Get stage type and initialize points array accordingly
			Stage stage = Race.getStageById(stageId);
			int[] points;
			switch (stage.getType()) {
				case FLAT:
					points = new int[]{30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
					break;
				case MEDIUM_MOUNTAIN:
					points = new int[]{30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
					break;
				case HIGH_MOUNTAIN:
					points = new int[]{30, 25, 22, 19, 17, 15, 13, 11, 14, 7, 6, 5, 4, 3, 2};
					break;
				case TT:
					points = new int[]{30, 25, 22, 19, 1, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
					break;
				default:
					throw new IllegalArgumentException("Unsupported stage type");
			}

			// Initialize an array to store points for each rider
			int[] riderPoints = new int[rankedRiderIds.length];

			// Create a map to store rider IDs along with their corresponding points
			Map<Integer, Integer> riderPointsMap = new HashMap<>();


			// Assign points to riders based on their rank
			for (int i = 0; i < rankedRiderIds.length; i++) {
				int rank = i + 1;
				if (rank <= points.length) {
					int riderId = rankedRiderIds[i];
					int pointsEarned = points[rank - 1]; // Subtract 1 to adjust for zero-based indexing
					riderPoints[i] = pointsEarned;
					riderPointsMap.put(riderId, pointsEarned);
				} else {
					// Assign zero points to remaining riders
					riderPoints[i] = 0;
					riderPointsMap.put(rankedRiderIds[i], 0);
				}
			}
		int[] sprintPoints = new int[]{30, 25, 22, 19, 1, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
		// Add intermediate sprint points to the riders' points based on their rank
		for (int i = 0; i < rankedRiderIds.length; i++) {
			int rank = i + 1;
			if (rank <= sprintPoints.length) {
				riderPoints[i] += sprintPoints[rank - 1]; // Subtract 1 to adjust for zero-based indexing
			}
		}




			return riderPoints;
		}


	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// Retrieve the stage
		Stage stage = Race.getStageById(stageId);

		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognized: " + stageId);
		}

		// Retrieve all participating rider IDs for the stage
		List<Integer> participatingRiderIds = getAllParticipatingRiderIds(stageId);

		// Create a map to store each rider's mountain points
		Map<Integer, Integer> mountainPointsMap = new HashMap<>();

		// Get the checkpoint IDs for the stage
		int[] checkpointIds = stage.getStageCheckpoints(stageId);




		// Iterate over each checkpoint
		for (int checkpointId : checkpointIds) {
			// Retrieve the checkpoint
			Checkpoint checkpoint = getCheckpointById(checkpointId);
			int checkpointIndex = 0;

			for (int i = 0; i < checkpointIds.length; i++) {
				if(checkpointId == checkpointIds[i]){
					  checkpointIndex = i;

				}
			}

			// Check if the checkpoint is a mountain checkpoint
			if (checkpoint.getType() != CheckpointType.SPRINT) {
				// Retrieve the checkpoint times for all riders
				List<LocalTime[]> checkpointTimesList = new ArrayList<>();
				for (int riderId : participatingRiderIds) {
					LocalTime[] checkpointTimes = retrieveRiderById(riderId).getResultsForStage(stageId);
					checkpointTimesList.add(checkpointTimes);
				}

				// Sort the checkpoint times list based on the finish time of the current checkpoint
				// Sort the checkpoint times for the rider based on the first checkpoint time

				// Retrieve the checkpoint times for a specific checkpoint for all riders
				LocalTime[] specificCheckpointTimes = new LocalTime[participatingRiderIds.size()];
				int riderIndex = 0;
				for (int riderId : participatingRiderIds) {
					LocalTime[] riderCheckpointTimes = retrieveRiderById(riderId).getResultsForStage(stageId);
					specificCheckpointTimes[riderIndex++] = riderCheckpointTimes[checkpointIndex]; // Assuming checkpointIndex is the index of the specific checkpoint
				}

				// Sort the specific checkpoint times array based on the time for the specific checkpoint
				Arrays.sort(specificCheckpointTimes);


				// Assign points based on the sorted order
				int[] points;
				switch (checkpoint.getType()) {
					case C1:
						points = new int[]{10, 8, 6, 4, 2, 1};
						break;
					case C2:
						points = new int[]{5, 3, 2, 1};
						break;
					case C3:
						points = new int[]{2, 1};
						break;
					case C4:
						points = new int[]{1};
						break;
					case HC:
						points = new int[]{20, 15, 12, 10, 8, 6, 4, 2};
						break;
					default:
						throw new IllegalArgumentException("Unsupported stage type");
				}

				// Assign points based on the order of finish for each rider at this checkpoint
				// Assign points based on the order of finish for each rider at this checkpoint
				// Assign points based on the order of finish for each rider at this checkpoint
				for (int i = 0; i < participatingRiderIds.size(); i++) {
					int riderId = participatingRiderIds.get(i);
					int pointsIndex = i; // Ensure we don't go out of bounds of the points array
					int mountainPoints = mountainPointsMap.getOrDefault(riderId, 0);

					if (!Arrays.asList(specificCheckpointTimes).isEmpty()) {
						LocalTime fastestTime = specificCheckpointTimes[0]; // Fastest time at this checkpoint
						if (fastestTime != null && specificCheckpointTimes[i] != null && specificCheckpointTimes[i].equals(fastestTime)) {
							mountainPoints += points[pointsIndex];
						}
					}
					mountainPointsMap.put(riderId, mountainPoints);
				}


			}

			}

		// Sort the mountain points map by the values (mountain points)
		List<Map.Entry<Integer, Integer>> sortedMountainPoints = new ArrayList<>(mountainPointsMap.entrySet());
		sortedMountainPoints.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

		// Extract the mountain points into an array
		int[] mountainPointsArray = new int[participatingRiderIds.size()];
		int index = 0;
		for (Map.Entry<Integer, Integer> entry : sortedMountainPoints) {
			mountainPointsArray[index++] = entry.getValue();
		}

		return mountainPointsArray;

	}

	@Override
	public void eraseCyclingPortal() {
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
			BadCyclingPortalImpl deserializedPortal = (BadCyclingPortalImpl) in.readObject();

			// Replace the contents of the current BadCyclingPortalImpl with the deserialized contents
			this.races = deserializedPortal.races;
			this.teams = deserializedPortal.teams;
			this.stages = deserializedPortal.stages;
			this.riders = deserializedPortal.riders;

			System.out.println("BadCyclingPortalImpl loaded successfully from file: " + filename);
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error loading BadCyclingPortalImpl: " + e.getMessage());
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
		List<Stage> raceStages = new ArrayList<>();
		for (Stage stage : stages) {
			if (stage.getRaceId() == raceId) {
				raceStages.add(stage);
			}
		}
		if (raceStages.isEmpty()) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " has no stages.");
		}
		List<LocalTime> generalClassificationTimes = new ArrayList<>();
		for (Stage stage : raceStages) {
			List<Integer> riderIds = Rider.getAllParticipatingRiderIds(stage.getId());
			Rider.sortRiderIdsByFinishTime(riderIds, stage.getId());
			for (Integer riderId : riderIds) {
				Rider rider = Rider.retrieveRiderById(riderId);
				LocalTime[] riderResults = rider.getResultsForStage(stage.getId());
				if (riderResults != null && riderResults.length > 0) {
					generalClassificationTimes.add(riderResults[riderResults.length - 1]);
				}
			}
		}

		// Sort the general classification times list
		generalClassificationTimes.sort(LocalTime::compareTo);

		// Convert list to array
		return generalClassificationTimes.toArray(new LocalTime[0]);
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
	    List<Stage> raceStages = new ArrayList<>();
	    for (Stage stage : stages) {
	        if (stage.getRaceId() == raceId) {
	            raceStages.add(stage);
	        }
	    }
	    if (raceStages.isEmpty()) {
	        throw new IDNotRecognisedException("Race with ID " + raceId + " has no stages.");
	    }
	    Map<Integer, Integer> riderPointsMap = new HashMap<>();
	    for (Stage stage : raceStages) {
	        int[] stageRiderPoints = getRidersPointsInStage(stage.getId());
	        for (int i = 0; i < stageRiderPoints.length; i++) {
	            int riderId = stageRiderPoints[i];
	            int points = riderPointsMap.getOrDefault(riderId, 0);
	            points += stageRiderPoints.length - i; 
	            riderPointsMap.put(riderId, points);
	        }
	    }
	    int[] riderPointsArray = new int[riderPointsMap.size()];
	    int index = 0;
	    for (int points : riderPointsMap.values()) {
	        riderPointsArray[index++] = points;
	    }
	
	    return riderPointsArray;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		List<Stage> raceStages = new ArrayList<>();
		for (Stage stage : stages) {
			if (stage.getRaceId() == raceId) {
				raceStages.add(stage);
			}
		}
		if (raceStages.isEmpty()) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " has no stages.");
		}
		Map<Integer, Integer> mountainPointsMap = new HashMap<>();
		for (Stage stage : raceStages) {
			int[] stageMountainPoints = getRidersMountainPointsInStage(stage.getId());
			for (int i = 0; i < stageMountainPoints.length; i++) {
				int riderId = stageMountainPoints[i];
				int points = mountainPointsMap.getOrDefault(riderId, 0);
				points += stageMountainPoints[i];
				mountainPointsMap.put(riderId, points);
			}
		}
		int[] mountainPointsArray = new int[mountainPointsMap.size()];
		int index = 0;
		for (int points : mountainPointsMap.values()) {
			mountainPointsArray[index++] = points;
		}
		return mountainPointsArray;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
	    List<Integer> riderIds = new ArrayList<>(); 
	    Map<Integer, Integer> totalPointsMap = new HashMap<>(); 
	    for (Stage stage : stages) {
	        if (stage.getRaceId() == raceId) {
	            int[] stageRiderPoints = getRidersPointsInStage(stage.getId());
	            for (int i = 0; i < stageRiderPoints.length; i++) {
	                int riderId = stageRiderPoints[i];
	                int points = totalPointsMap.getOrDefault(riderId, 0);
	                points += stageRiderPoints[i];
	                totalPointsMap.put(riderId, points);
	                if (!riderIds.contains(riderId)) {
	                    riderIds.add(riderId);
	                }
	            }
	        }
	    }
	    riderIds.sort((riderId1, riderId2) -> {
	        int points1 = totalPointsMap.getOrDefault(riderId1, 0);
	        int points2 = totalPointsMap.getOrDefault(riderId2, 0);
	        // Sort in descending order
	        return Integer.compare(points2, points1);
	    });
	    int[] rankedRiderIds = riderIds.stream().mapToInt(Integer::intValue).toArray();
	    return rankedRiderIds;
	}
	
	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
	    List<Integer> riderIds = new ArrayList<>(); 
	    Map<Integer, Integer> totalMountainPointsMap = new HashMap<>(); 
	    for (Stage stage : stages) {
	        if (stage.getRaceId() == raceId && stage.getType() != StageType.FLAT && stage.getType() != StageType.TT) {
	            int[] stageRiderMountainPoints = getRidersMountainPointsInStage(stage.getId());
	            for (int i = 0; i < stageRiderMountainPoints.length; i++) {
	                int riderId = stageRiderMountainPoints[i];
	                int points = totalMountainPointsMap.getOrDefault(riderId, 0);
	                points += stageRiderMountainPoints[i];
	                totalMountainPointsMap.put(riderId, points);
	                if (!riderIds.contains(riderId)) {
	                    riderIds.add(riderId);
	                }
	            }
	        }
	    }

	    riderIds.sort((riderId1, riderId2) -> {
	        int points1 = totalMountainPointsMap.getOrDefault(riderId1, 0);
	        int points2 = totalMountainPointsMap.getOrDefault(riderId2, 0);
	        return Integer.compare(points2, points1);
	    });
	    int[] rankedRiderIds = riderIds.stream().mapToInt(Integer::intValue).toArray();
	    return rankedRiderIds;
	}

}
