package inputHandler;

public class PairValues {
	private int positive;
	private int negative;
	private int distance;
	
	public PairValues(int positive, int negative, int distance){
		this.positive=positive;
		this.negative=negative;
		this.distance=distance;
	}
	
	public int getPositive(){
		return positive;
	}
	
	public int getNegative(){
		return negative;
	}
	
	public int getDistance(){
		return distance;
	}
}
