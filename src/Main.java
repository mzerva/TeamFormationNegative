import java.util.ArrayList;
import java.util.Random;

import algorithm.TeamFormationAlgorithm;
import inputHandler.InputManager;
import inputHandler.SkillInfo;

public class Main {

	public static void main(String[] args){
		
		//Epinions
		/*
		//String pairValuesPath="data\\epinions\\more_positive_paths.txt";
		//String pairValuesPath="data\\epinions\\one_positive_path.txt";
		String pairValuesPath="data\\epinions\\no_negative_paths.txt";
		String networkPath="data\\epinions\\network.txt"; 
		String userPath="data\\epinions\\users.txt";
		String skillPath="data\\epinions\\skills.txt";*/
		
		//SlashDot
		//String pairValuesPath="data\\slashdot\\more_positive_paths.txt";
		//String pairValuesPath="data\\slashdot\\one_positive_path.txt";
		String pairValuesPath="data\\slashdot\\no_negative_paths.txt";
		String networkPath="data\\slashdot\\network.txt"; 
		String userPath="data\\slashdot\\users.txt";
		String skillPath="data\\slashdot\\skills.txt";
				
		InputManager manager = new InputManager(pairValuesPath,networkPath,userPath,skillPath);
		manager.retrieveNetwork();
		manager.retrieveSkillInfo();
		
		ArrayList<String> initialTask = produceTask(3,manager.getSkillInfo());
		
		manager.retrievePairInfo(initialTask);
		
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(initialTask,manager.getNetwork(), manager.getSkillInfo(),manager.getPairInfo(),manager.getCompatibleDistances());
		algorithm.start();
	}
	
	public static ArrayList<String> produceTask(int numOfTasks, SkillInfo skillInfo){
		ArrayList<String> initialTask = new ArrayList<String>();
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			String temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
			initialTask.add(temp);
		}
		return initialTask;
	}
}
