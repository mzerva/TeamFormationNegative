import algorithm.TeamFormationAlgorithm;
import inputHandler.InputManager;

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
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(manager.getNetwork(), manager.getSkillInfo(),manager.getPairInfo(),manager.getCompatibleDistances());
		algorithm.start();
	}
}
