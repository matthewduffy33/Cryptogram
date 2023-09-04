
public class Player {

	private String username;
	private Double accuracy;
	private int totalGuesses;
	private int correctGuesses;
	private int cryptogramsPlayed;
	private int cryptogramsCompleted;

	Player(String u){
		username=u;
		totalGuesses=0;
		correctGuesses=0;
		cryptogramsPlayed=0;
		cryptogramsCompleted=0;
	}
	Player(String u, int totGu, int corGu, int cryptPlay, int cryptComp){
		username=u;
		totalGuesses=totGu;
		correctGuesses=corGu;
		cryptogramsPlayed=cryptPlay;
		cryptogramsCompleted=cryptComp;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getTotalGuesses() {
		return totalGuesses;
	}
	
	public void incrementTotalGuesses() {
		totalGuesses++;
	}
	
	public int getCorrectGuesses() {
		return correctGuesses;
	}
	
	public int getCryptogramPlayed() {
		return cryptogramsPlayed;
	}
	
	public int getCryptogramCompleted() {
		return cryptogramsCompleted;
	}
	
	public void incrementCorrectGuesses() {
		correctGuesses++;
	}
	
	public void incrementGames() {
		cryptogramsPlayed++;
	}
	
	public void incrementCompleteGames() {
		cryptogramsCompleted++;
	}
	
	public void updateAccuracy() {
		if (totalGuesses == 0) {
			accuracy = 0.00;
		} else {
			accuracy = ((double)correctGuesses / totalGuesses)*100.00;
		}
	}
	
	public double getAccuracy() {
		updateAccuracy();
		return (double) Math.round(accuracy*100)/100;
	}


}
