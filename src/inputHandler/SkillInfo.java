package inputHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class SkillInfo {
	private HashMap<Integer,ArrayList<String>> userSkills = new HashMap<Integer,ArrayList<String>>();
	private HashMap<String,Integer> skillDistribution = new HashMap<String,Integer>();
	
	public SkillInfo(HashMap<Integer,ArrayList<String>> userSkills, HashMap<String,Integer> skillDistribution){
		this.userSkills=userSkills;
		this.skillDistribution=skillDistribution;
	}
	
	public HashMap<Integer,ArrayList<String>> getUserSkills(){
		return userSkills;
	}
	
	public HashMap<String,Integer> getSkillDistribution(){
		return skillDistribution;
	}

}
