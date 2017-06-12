import algorithm.TeamFormationAlgorithm;
import inputHandler.InputManager;

public class Main {

	public static void main(String[] args){
		
		//Epinions
		String pairValuesPath="data\\?";
		String networkPath="data\\epinions\\network.txt"; 
		String userPath="data\\epinions\\users.txt";
		String skillPath="data\\epinions\\skills.txt";
		
		//SlashDot
		/*String pairValuesPath="data\\?";
		String networkPath="data\\slashdot\\network.txt"; 
		String userPath="data\\slashdot\\users.txt";
		String skillPath="data\\slashdot\\skills.txt";*/
				
		InputManager manager = new InputManager(pairValuesPath,networkPath,userPath,skillPath);
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(manager.getNetwork(), manager.getSkillInfo(),manager.getPairInfo());
		algorithm.start();
	}
}
