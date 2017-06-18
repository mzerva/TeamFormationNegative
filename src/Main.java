import java.util.ArrayList;
import java.util.Random;

import algorithm.TeamFormationAlgorithm;
import inputHandler.InputManager;
import inputHandler.SkillInfo;

public class Main {

	public static void main(String[] args){
		
		/********** PARAMETERS **********/
		int numOfSkills=10;
		int numOfIterations=50;
		String dataset="slashdot";
		/* 1 : no_negative_paths, 2 : more_positive_paths, 3 : one_positive_path, 4 : no_negative_edge */
		int compatibility_mode=1;
		/********** ********** **********/
		
		String resultPath;
		String pairValuesPath;
		
		if(compatibility_mode==1){
			pairValuesPath="data\\"+dataset+"\\no_negative_paths.txt";
			resultPath="results\\"+dataset+"_"+numOfSkills+"_"+numOfIterations+"_no_negative_paths.txt";
			
		}
		else if(compatibility_mode==2){
			pairValuesPath="data\\"+dataset+"\\more_positive_paths.txt";
			resultPath="results\\"+dataset+"_"+numOfSkills+"_"+numOfIterations+"_more_positive_paths.txt";
		}
		else if(compatibility_mode==3){
			pairValuesPath="data\\"+dataset+"\\one_positive_path.txt";
			resultPath="results\\"+dataset+"_"+numOfSkills+"_"+numOfIterations+"_one_positive_path.txt";
		}
		else{
			pairValuesPath="data\\"+dataset+"\\distances.txt";
			resultPath="results\\"+dataset+"_"+numOfSkills+"_"+numOfIterations+"_no_negative_edge.txt";
		}
			
		String networkPath="data\\"+dataset+"\\network.txt"; 
		String userPath="data\\"+dataset+"\\users.txt";
		String skillPath="data\\"+dataset+"\\skills.txt";
		
		ArrayList<String> results = new ArrayList<String>();
		
		for(int i=0;i<numOfIterations;i++){	
			
			if(compatibility_mode<4){
				results.add(runCompatibilityAlgorithm(numOfSkills,pairValuesPath, networkPath, userPath, skillPath));
			}
			else{
				results.add(runNoNegativeAlgorithm(numOfSkills, pairValuesPath, networkPath, userPath, skillPath));
			}
			
		}
		
		if(numOfIterations>1){
			FileWriter writer = new FileWriter(resultPath);
			writer.initWriter();
			writer.writeData(results);
		}
	}
	
	
	public static String runCompatibilityAlgorithm(int numOfSkills,String pairValuesPath, String networkPath, String userPath, String skillPath){
		InputManager manager = new InputManager(pairValuesPath,networkPath,userPath,skillPath);
		manager.retrieveSkillInfo();
		
		ArrayList<String> initialTask = produceTask(numOfSkills,manager.getSkillInfo());
		
		manager.retrievePairInfo(initialTask);
		
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(initialTask,manager.getSkillInfo(),manager.getPairInfo(),manager.getCompatibleDistances());
		algorithm.start();
		
		return algorithm.getResult();
	}
	
	public static String runNoNegativeAlgorithm(int numOfSkills,String pairValuesPath, String networkPath, String userPath, String skillPath){
		InputManager manager = new InputManager(pairValuesPath,networkPath,userPath,skillPath);
		manager.retrieveNetwork();
		manager.retrieveSkillInfo();
		
		ArrayList<String> initialTask = produceTask(numOfSkills,manager.getSkillInfo());
		
		manager.retrieveDistancesInfo(initialTask);
		
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(initialTask,manager.getNetwork(), manager.getSkillInfo(),manager.getCompatibleDistances());
		algorithm.start();
		
		return algorithm.getResult();
		
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
