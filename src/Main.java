import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import algorithm.TeamFormationAlgorithm;
import inputHandler.InputManager;
import inputHandler.SkillInfo;

public class Main {

	public static void main(String[] args){
		
		
		/********** PARAMETERS **********/
		int numOfSkills=5;
		String dataset="epinions";
		// 1 : no_negative_paths, 2 : more_positive_paths, 3 : one_positive_path, 4 : no_negative_edge 
		int compatibility_mode=1;
		String task="";
		/********** ********** **********/
		
		System.out.print("Started at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		
		String resultPath;
		String pairValuesPath;
		
		if(compatibility_mode==1){
			pairValuesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/no_negative_paths.txt";
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_no_negative_paths.txt";

		}
		else if(compatibility_mode==2){
			pairValuesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/more_positive_paths.txt";
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_more_positive_paths.txt";
		}
		else if(compatibility_mode==3){
			pairValuesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/one_positive_path.txt";
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_one_positive_path.txt";
		}
		else{
			pairValuesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/distances.txt";
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_no_negative_edge.txt";
		}
			
		String networkPath="data/"+dataset+"/network.txt"; 
		String userPath="data/"+dataset+"/users.txt";
		String skillPath="data/"+dataset+"/skills.txt";
		
		String result;
			
		if(compatibility_mode<4){
			result=runCompatibilityAlgorithm(numOfSkills,pairValuesPath, networkPath, userPath, skillPath);
			
		}
		else{
			result=runNoNegativeAlgorithm(numOfSkills, pairValuesPath, networkPath, userPath, skillPath);
		}
		
		System.out.println(result);
		
//		FileWriter writer = new FileWriter(resultPath);
//		writer.initAppendWriter();
//		writer.appendLine(result);
		
		System.out.print("Finished at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
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
