package algorithm;

import java.util.HashMap;

import inputHandler.Network;
import inputHandler.PairValues;
import inputHandler.SkillInfo;

public class TeamFormationAlgorithm {
	//inputFields
	private Network network;
	private SkillInfo skillInfo;
	private HashMap<Integer,HashMap<Integer,PairValues>> pairInfo;
	
	//algorithmFields
	private String rarestSkill = new String();
	
	public TeamFormationAlgorithm(Network network, SkillInfo skillInfo, HashMap<Integer,HashMap<Integer,PairValues>> pairInfo){
		this.network=network;
		this.skillInfo=skillInfo;
		this.pairInfo=pairInfo;
	}

	public void start(){
		findRarestSkill();
		algorithm();
	}
	
	public void findRarestSkill(){
		
	}
	
	public void algorithm(){
		
	}
	
	public void /*not actually void, we'll see*/ getMostCompatible(/*place arguments here*/){
		
	}
	
	public void /*not actually void, we'll see*/ handleTie(/*place arguments here*/){
		
	}
	
	public boolean checkComponent(/*place arguments here*/){
		return true;//check when
	}
	
}
