package cycling; 

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortal interface.
 * 
 * @author Ryan Butler Hugo Blanco
 * @version 2.0
 *
 */
public class BadCyclingPortalImpl implements CyclingPortal {

	@Override
	public int[] getRaceIds() {
		// TODO Auto-generated method stub
		return new int[] {};
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
	    InvalidNameException.checkName(name);
	    for (Team t : teams) {
	        if (name.equals(t.getName())) {
	            throw new IllegalNameException("Name already used");
	        }
	    }
	    Team newTeam = new Team(name, description);
	    teams.add(newTeam);
	    return newTeam.getId(); 
	}
	
	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
	    Iterator<Team> iterator = teams.iterator();
	    boolean check = false;
	    while (iterator.hasNext()) {
	        Team t = iterator.next();
	        if (teamId == t.getId()) {
	            iterator.remove();
	            check = true;
	            break;
	        }
	    }
	    if (!check) {
	        throw new IDNotRecognisedException("ID Not Recognised");
	    }
	}
	
	@Override
	public int[] getTeams() {
	    int[] teamIds = new int[teams.size()];
	    int i = 0;
	    for (Team t : teams) {
	        teamIds[i] = t.getId();
	        i++;
	    }
	    return teamIds;
	}
	
	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
	    for (Team t : teams) {
	        if (t.getId() == teamId) {
	            List<Rider> riders = t.getRiders();
	            if (riders.size() == 0) {
	                return null;
	            }
	            int[] riderIds = new int[riders.size()];
	            int i = 0;
	            for (Rider r : riders) {
	                riderIds[i] = r.getId();
	                i++;
	            }
	            return riderIds;
	        }
	    }
	    throw new IDNotRecognisedException("ID Not Recognised");
	}
	
	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		try{
			if(name == null){throw new IllegalArgumentException("Name can't be empty");}
			if(yearOfBirth <1900){throw new IllegalArgumentException("Invalid Year of Birth");}
			for (Team t:teams){
				if(teamID == t.getId()){
					t.addRider(teamID,name,yearOfBirth);
				}
			} throw new IDNotRecognisedException("ID Not Recognised");
		}catch (IDNotRecognisedException|IllegalArgumentException e){
		}
		return 0;
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
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

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
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
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		// TODO Auto-generated method stub

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
