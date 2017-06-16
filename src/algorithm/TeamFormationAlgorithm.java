package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import inputHandler.Network;
import inputHandler.SkillInfo;

public class TeamFormationAlgorithm {
	//inputFields
	private Network network;
	private SkillInfo skillInfo;

	//algorithmFields
	private ArrayList<String> initialTask = new ArrayList<String>();
	private ArrayList<String> task = new ArrayList<String>();
	private String rarestSkill = new String();
	private HashMap<Integer,StarTeam> teams = new HashMap<Integer,StarTeam>();
	
	private HashMap<Integer,ArrayList<Integer>> compatibleList = new HashMap<Integer,ArrayList<Integer>>();
	private HashMap<String,Integer> compatibleDistances = new HashMap<String,Integer>();
	
	public TeamFormationAlgorithm(Network network, SkillInfo skillInfo, HashMap<Integer,ArrayList<Integer>> compatibleList, HashMap<String,Integer> compatibleDistances/*, HashMap<Integer,HashMap<Integer,PairValues>> pairInfo*/){
		this.network=network;
		this.skillInfo=skillInfo;
		this.compatibleList=compatibleList;
		this.compatibleDistances=compatibleDistances;
		//this.pairInfo=pairInfo;
	}

	public void start(){
		produceTask(3);
		rankSkills();
		algorithm();
	}
	
	public void produceTask(int numOfTasks){
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			String temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
			initialTask.add(temp);
		}
		for(int i=0;i<initialTask.size()-1;i++){
			System.out.print(initialTask.get(i)+" , ");
		}
		System.out.println(initialTask.get(initialTask.size()-1));
	}
	
	public void rankSkills(){
		while(initialTask.size()>0){
			String t =findRarestSkill();
			initialTask.remove(t);
			task.add(t);
		}
		rarestSkill=task.get(0);
		/*System.out.println("RANKED:");
		for(int i=0;i<task.size();i++){
			System.out.println(task.get(i));
		}*/
	}
	
	public String findRarestSkill(){
		String rare = initialTask.get(0);
		int min=skillInfo.getSkillUsers().get(rare).size();
		for(int i=1;i<initialTask.size();i++){
			if(skillInfo.getSkillUsers().get(initialTask.get(i)).size()<min){
				min=skillInfo.getSkillUsers().get(initialTask.get(i)).size();
				rare=initialTask.get(i);
			}
		}
		return rare;
	}
	
	public void algorithm(){
		for(int i=0;i<skillInfo.getSkillUsers().get(rarestSkill).size();i++){
			int rareUser = skillInfo.getSkillUsers().get(rarestSkill).get(i);
			//create star team
			StarTeam star = new StarTeam();
			star.addMember(rareUser);
			
			//cover rareUser's skill
			ArrayList<String> taskSkills = getUserTaskSkills(rareUser);
			for(int k=0;k<taskSkills.size();k++){
				if(!star.getCoveredSkills().contains(taskSkills.get(k))){
					star.addSkill(taskSkills.get(k));
				}	
			}
			
			for(int j=1;j<task.size();j++){
				if(!star.getCoveredSkills().contains(task.get(j))){
					ArrayList<Integer> users = new ArrayList<Integer>();
					for(int m=0;m<skillInfo.getSkillUsers().get(task.get(j)).size();m++){
						users.add(skillInfo.getSkillUsers().get(task.get(j)).get(m));
					}
					
					//choose most compatible user
					int user=getMostCompatible(users,star);
					
					if(user!=-1){
						//add to team
						star.addMember(user);
						
						//add his skills in team
						ArrayList<String> skillsCovered = getUserTaskSkills(user);
						for(int k=0;k<skillsCovered.size();k++){
							if(!star.getCoveredSkills().contains(skillsCovered.get(k))){
								star.addSkill(skillsCovered.get(k));
							}	
						}
					}
					else{
						//StarTeam noTeam = new StarTeam();
						teams.put(rareUser, null);
						break;
					}
				}
			}
			if(!teams.containsKey(rareUser)){
				//if(star.getCoveredSkills().size()==task.size()){
					teams.put(rareUser, star);
				//}
				//else{
					//StarTeam noTeam = new StarTeam();
				//	teams.put(rareUser, null);
				//}
			}
		}
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for(Integer rareNode : teams.keySet()){
			if(teams.get(rareNode)==null){
				toRemove.add(rareNode);
			}
		}
		for(int i=0;i<toRemove.size();i++){
			teams.remove(toRemove.get(i));
		}
		if(teams.keySet().size()==1){
			System.out.println("Found 1 team:");
			for(Integer key : teams.keySet()){
				System.out.println("Starnode's "+key+" team: ");
				for(int i=0;i<teams.get(key).getTeam().size()-1;i++){
					System.out.print(teams.get(key).getTeam().get(i)+" , ");
				}
				System.out.println(teams.get(key).getTeam().get(teams.get(key).getTeam().size()-1));
			}
		}
		else if(teams.keySet().size()>1){
			for(Integer key : teams.keySet()){
				System.out.println("Starnode's "+key+" team: ");
				for(int i=0;i<teams.get(key).getTeam().size()-1;i++){
					System.out.print(teams.get(key).getTeam().get(i)+" , ");
				}
				System.out.println(teams.get(key).getTeam().get(teams.get(key).getTeam().size()-1));
			}
			
			System.out.println("Total teams found: "+teams.size());
			
			StarTeam best=getBestTeam();
			System.out.println("Best team:");
			for(int i=0;i<best.getTeam().size()-1;i++){
				System.out.print(best.getTeam().get(i)+"   ,   ");
			}
			System.out.println(best.getTeam().get(best.getTeam().size()-1));
			/*System.out.println("Skills covered:");
			for(int i=0;i<best.getCoveredSkills().size();i++){
				System.out.print(best.getCoveredSkills().get(i)+"   ,   ");
			}*/
		}
		else{
			System.out.println("No team found!");
		}
	}
	
	public StarTeam getBestTeam(){
		int total_max=-1;
		int node=-1;
		for(Integer starNode : teams.keySet()){
			int max=0;
			for(int i=0;i<teams.get(starNode).getTeam().size();i++){
				for(int j=0;j<teams.get(starNode).getTeam().size();j++){
					int node1= teams.get(starNode).getTeam().get(i);
					int node2= teams.get(starNode).getTeam().get(j);
					
					if(compatibleDistances.containsKey(node1+","+node2)){
						if(max<compatibleDistances.get(node1+","+node2)){
							max=compatibleDistances.get(node1+","+node2);
						}
					}
					else if(compatibleDistances.containsKey(node2+","+node1)){
						if(max<compatibleDistances.get(node2+","+node1)){
							max=compatibleDistances.get(node2+","+node1);
						}
					}
				}
			}
			System.out.println("Max distance of "+starNode+"'s team: "+max);
			if(total_max==-1){
				total_max=max;
				node=starNode;
			}
			else if(total_max>max){
				total_max=max;
				node=starNode;
			}
			else if(total_max==max){
				int tmp=handleTies(node,starNode);
				if(tmp==starNode){
					total_max=max;
					node=starNode;
				}
			}
		}
		return teams.get(node);
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
	
	public int getMostCompatible(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		//ArrayList<Integer> compatibles = new ArrayList<Integer>();
		int compatible=-1;
		int max_c=-1;
		
		for(int i=0;i<candidates.size();i++){
			int max=0;
			for(int j=0;j<team.getTeam().size();j++){
				//System.out.println("candidate: "+candidates.get(i));
				//System.out.println("team_member: "+team.getTeam().get(j));
				if(compatibleList.containsKey(candidates.get(i)) && compatibleList.containsKey(team.getTeam().get(j))){
					if(!compatibleList.get(candidates.get(i)).contains(team.getTeam().get(j)) && !compatibleList.get(team.getTeam().get(j)).contains(candidates.get(i))){
						notCompatible.add(candidates.get(i));
					}
				}
				else if(!compatibleList.containsKey(candidates.get(i)) && !compatibleList.containsKey(team.getTeam().get(j))){
					notCompatible.add(candidates.get(i));
				}
				else if(!compatibleList.containsKey(candidates.get(i))){
					if(!compatibleList.get(team.getTeam().get(j)).contains(candidates.get(i))){
						notCompatible.add(candidates.get(i));
					}
				}
				else{
					if(!compatibleList.get(candidates.get(i)).contains(team.getTeam().get(j))){
						notCompatible.add(candidates.get(i));
					}
				}
				if(compatibleDistances.containsKey(candidates.get(i)+","+team.getTeam().get(j))){
					if(max<compatibleDistances.get(candidates.get(i)+","+team.getTeam().get(j))){
						max=compatibleDistances.get(candidates.get(i)+","+team.getTeam().get(j));
					}
				}
				else if(compatibleDistances.containsKey(team.getTeam().get(j)+","+candidates.get(i))){
					if(max<compatibleDistances.get(team.getTeam().get(j)+","+candidates.get(i))){
						max=compatibleDistances.get(team.getTeam().get(j)+","+candidates.get(i));
					}
				}
			}
			if(!notCompatible.contains(candidates.get(i))){
				//compatibles.add(candidates.get(i));
				if(max_c==-1){
					max_c=max;
					compatible= candidates.get(i);
				}
				else if(max_c>max){
					max_c=max;
					compatible= candidates.get(i);
				}
				else if(max==max_c){
					int tmp=handleTies(compatible, candidates.get(i));
					if(tmp==candidates.get(i)){
						max_c=max;
						compatible=candidates.get(i);
					}
				}
			}
		}
		
		return compatible;
	}
	
	public int handleTies(int compatible1, int compatible2){
		if(compatible1<compatible2){
			return compatible1;
		}
		else{
			return compatible2;
		}
	}
	
	public boolean checkComponent(/*place arguments here*/){
		return true;//check when
	}
	
}
