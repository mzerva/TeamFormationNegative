package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import inputHandler.Network;
import inputHandler.PairValues;
import inputHandler.SkillInfo;

public class TeamFormationAlgorithm {
	//inputFields
	private Network network;
	private SkillInfo skillInfo;
	private HashMap<Integer,HashMap<Integer,PairValues>> pairInfo;
	
	//algorithmFields
	private ArrayList<String> task = new ArrayList<String>();
	private String rarestSkill = new String();
	private HashMap<Integer,StarTeam> teams = new HashMap<Integer,StarTeam>();
	
	public TeamFormationAlgorithm(Network network, SkillInfo skillInfo, HashMap<Integer,HashMap<Integer,PairValues>> pairInfo){
		this.network=network;
		this.skillInfo=skillInfo;
		this.pairInfo=pairInfo;
	}

	public void start(){
		produceTask(5);
		findRarestSkill();
		algorithm();
	}
	
	public void produceTask(int numOfTasks){
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			task.add(skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size())));
		}
		for(int i=0;i<task.size();i++){
			System.out.println(task.get(i));
		}
	}
	
	public void findRarestSkill(){
		rarestSkill=task.get(0);
		int min=skillInfo.getSkillUsers().get(rarestSkill).size();
		for(int i=1;i<task.size();i++){
			if(skillInfo.getSkillUsers().get(task.get(i)).size()<min){
				min=skillInfo.getSkillUsers().get(task.get(i)).size();
				rarestSkill=task.get(i);
			}
		}
	}
	
	public StarTeam algorithm(){
		for(int i=0;i<skillInfo.getSkillUsers().get(rarestSkill).size();i++){
			int rareUser = skillInfo.getSkillUsers().get(rarestSkill).get(i);
			//create star team
			StarTeam star = new StarTeam();
			star.addMember(rareUser);
			
			//cover rareUser's skill
			ArrayList<String> taskSkills = getUserTaskSkills(rareUser);
			for(int k=0;k<taskSkills.size();k++){
				if(!star.getCoveredSkills().contains(taskSkills.get(i))){
					star.addSkill(taskSkills.get(i));
				}	
			}
			
			for(int j=0;j<task.size();j++){
				if(!star.getCoveredSkills().contains(task.get(j))){
					ArrayList<Integer> users = new ArrayList<Integer>();
					for(int m=0;m<skillInfo.getSkillUsers().get(task.get(j)).size();m++){
						users.add(skillInfo.getSkillUsers().get(task.get(j)).get(m));
					}
					
					//choose most compatible user
					int user=getMostCompatibleNoNegative(users,star);
					//int user=getMostCompatibleMorePositive(users,star);
					//int user=getMostCompatibleOnePositive(users,star);
					//int user=getMostCompatibleNoNegativeEdge(users,star);
					
					if(user!=-1){
						//add to team
						star.addMember(user);
						
						//add his skills in team
						ArrayList<String> skillsCovered = getUserTaskSkills(user);
						for(int k=0;k<skillsCovered.size();k++){
							if(!star.getCoveredSkills().contains(skillsCovered.get(i))){
								star.addSkill(skillsCovered.get(i));
							}	
						}
					}
					else{
						StarTeam noTeam = new StarTeam();
						teams.put(rareUser, noTeam);
						break;
					}
				}
			}
			if(!teams.containsKey(rareUser)){
				if(star.getCoveredSkills().size()==task.size()){
					teams.put(rareUser, star);
				}
				else{
					StarTeam noTeam = new StarTeam();
					teams.put(rareUser, noTeam);
				}
			}
		}
		return getBestTeam();
	}
	
	public StarTeam getBestTeam(){
		for(Integer starNode : teams.keySet()){
			return teams.get(starNode);
		}
		return null;
	}
	
	public ArrayList<String> getUserTaskSkills(int user){
		ArrayList<String> taskSkills = new ArrayList<String>();
		for(int i=0;i<skillInfo.getUserSkills().get(user).size();i++){
			if(task.contains(skillInfo.getUserSkills().get(user).get(i))){
				taskSkills.add(skillInfo.getUserSkills().get(user).get(i));
			}
		}
		return taskSkills;
	}
	
	public int getMostCompatibleNoNegative(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> teamMembers = team.getTeam();
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		ArrayList<Integer> compatible = new ArrayList<Integer>();
		for(int i=0;i<candidates.size();i++){
			for(int j=0;j<teamMembers.size();j++){
				//(3,4) and (4,3)
				if(pairInfo.get(candidates.get(i)).get(teamMembers.get(j)).getNegative()>0){
					notCompatible.add(candidates.get(i));
				}
			}
			if(!notCompatible.contains(candidates.get(i))){
				compatible.add(candidates.get(i));
			}
		}
		if(compatible.size()<=0){
			return -1;
		}
		else if(compatible.size()>1){
			return handleTies(compatible);
		}
		else{
			return compatible.get(0);
		}
	}
	
	public int getMostCompatibleMorePositive(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> teamMembers = team.getTeam();
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		ArrayList<Integer> compatible = new ArrayList<Integer>();
		for(int i=0;i<candidates.size();i++){
			for(int j=0;j<teamMembers.size();j++){
				//(3,4) and (4,3)
				if(pairInfo.get(candidates.get(i)).get(teamMembers.get(j)).getNegative()>=pairInfo.get(candidates.get(i)).get(teamMembers.get(j)).getPositive()){
					notCompatible.add(candidates.get(i));
				}
			}
			if(!notCompatible.contains(candidates.get(i))){
				compatible.add(candidates.get(i));
			}
		}
		if(compatible.size()<=0){
			return -1;
		}
		else if(compatible.size()>1){
			return handleTies(compatible);
		}
		else{
			return compatible.get(0);
		}	
	}
	
	public int getMostCompatibleOnePositive(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> teamMembers = team.getTeam();
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		ArrayList<Integer> compatible = new ArrayList<Integer>();
		for(int i=0;i<candidates.size();i++){
			for(int j=0;j<teamMembers.size();j++){
				//(3,4) and (4,3)
				if(pairInfo.get(candidates.get(i)).get(teamMembers.get(j)).getPositive()==0){
					notCompatible.add(candidates.get(i));
				}
			}
			if(!notCompatible.contains(candidates.get(i))){
				compatible.add(candidates.get(i));
			}
		}
		if(compatible.size()<=0){
			return -1;
		}
		else if(compatible.size()>1){
			return handleTies(compatible);
		}
		else{
			return compatible.get(0);
		}	
	}
	
	public int getMostCompatibleNoNegativeEdge(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> teamMembers = team.getTeam();
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		ArrayList<Integer> compatible = new ArrayList<Integer>();
		for(int i=0;i<candidates.size();i++){
			for(int j=0;j<teamMembers.size();j++){
				String edge1 = candidates.get(i)+","+teamMembers.get(j);
				String edge2 = teamMembers.get(j)+","+candidates.get(i);
				if(network.getEdges().containsKey(edge1)){
					if(network.getEdges().get(edge1)==-1){
						notCompatible.add(candidates.get(i));
					}
					else{
						compatible.add(candidates.get(i));
					}
				}
				else if(network.getEdges().containsKey(edge2)){
					if(network.getEdges().get(edge2)==-1){
						notCompatible.add(candidates.get(i));
					}
					else{
						compatible.add(candidates.get(i));
					}
				}
				else{
					compatible.add(candidates.get(i));
				}	
			}
		}
		if(compatible.size()<=0){
			return -1;
		}
		else if(compatible.size()>1){
			return handleTies(compatible);
		}
		else{
			return compatible.get(0);
		}
	}
	
	public int  handleTies(ArrayList<Integer> compatible){
		int min=compatible.get(0);
		for(int i=1;i<compatible.size();i++){
			if(compatible.get(i)<min){
				min=compatible.get(i);
			}
		}
		return min;
	}
	
	public boolean checkComponent(/*place arguments here*/){
		return true;//check when
	}
	
}
