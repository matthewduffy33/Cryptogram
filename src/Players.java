import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Players {
	

	private ArrayList<Player> allPlayers;
	private ArrayList<String> playersFailedReading;
	private String playersFile="Players.txt";
	
	Players(){
		allPlayers= new ArrayList<Player>();
		playersFailedReading= new ArrayList<String>();
		String username;
		int totalGuesses;
		int correctGuesses;
		int cryptogramsPlayed;
		int cryptogramsCompleted;
		String fullLine = "";
	
		Scanner reader = null;
		try {
			reader = new Scanner(new File(playersFile));
			while (reader.hasNextLine()) {
				try {
					fullLine="";
					username= reader.next();
					fullLine=fullLine + username+" ";
					totalGuesses = reader.nextInt();
					fullLine=fullLine +  totalGuesses+" ";
			        correctGuesses = reader.nextInt();
			        fullLine=fullLine +  correctGuesses+" ";
			        cryptogramsPlayed = reader.nextInt();
			        fullLine=fullLine +  cryptogramsPlayed+" ";
			        cryptogramsCompleted = reader.nextInt();
			        fullLine=fullLine +  cryptogramsCompleted;
			        allPlayers.add(new Player(username, totalGuesses, correctGuesses, cryptogramsPlayed, cryptogramsCompleted));
				}catch(NoSuchElementException e){
					playersFailedReading.add(fullLine);
				}	
		      }
				
		}catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	
	void addPlayer(Player p) {
		allPlayers.add(p);
	}
	
	Player removePlayers(String p) {
		if(!allPlayers.isEmpty()) {
			for (Player player:allPlayers) {
				if (player.getUsername().equals(p)) {
					allPlayers.remove(allPlayers.indexOf(player));

				}
			}
		}
		return null;
	}
	
	
	void savePlayers() {
		try {
			PrintWriter writer = new PrintWriter(playersFile);
		
			for (Player player:allPlayers) {
				writer.println(player.getUsername() + " " + player.getTotalGuesses() + " "+ player.getCorrectGuesses() + " " + player.getCryptogramPlayed() + " " + player.getCryptogramCompleted());
			}
			if(!playersFailedReading.isEmpty()) {
				for(String player:playersFailedReading) {
					writer.println(player);
				}
			}
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving player stats.");
		}
	
	}
	
	
	Player findPlayers(String p) {
		if(!allPlayers.isEmpty()) {
			for (Player player:allPlayers) {
				if (player.getUsername().equals(p)) {
					return player;
				}
			}
		}
		return null;
	}
	
	boolean corruptedPlayers(String p) {
		if(!playersFailedReading.isEmpty()) {
			for(String player:playersFailedReading) {
				if(player.contains(p)) {
					playersFailedReading.remove(player);
					
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	Boolean sorter() {
		if(!allPlayers.isEmpty()) {
			for(Player player:allPlayers) {
				if(player.getCryptogramCompleted()>0) {
					quicksort(0,allPlayers.size()-1);
					return true;
				}
			}
		}
		return false;
		
	}
	
	void quicksort(int first, int last) {
		if (first < last) {
			int partition=partitioning(first, last);
			quicksort(first,partition-1);
			quicksort(partition+1, last);
		}
	}
	
	int partitioning(int first, int last) {
		Player pivot = allPlayers.get(last);
		double pivotValue;
		if(pivot.getCryptogramCompleted()==0) {
			pivotValue=0;
		}else {
			pivotValue = ((double)pivot.getCryptogramCompleted()/pivot.getCryptogramPlayed())*100;
		}
		
		int lo = first;
		int hi = last;
		double current;
		while( lo < hi ) { 
			if(pivot.getCryptogramCompleted()==0) {
				current=0;
			}else {
				current= ((double)allPlayers.get(lo).getCryptogramCompleted()/allPlayers.get(lo).getCryptogramPlayed())*100;
			}
			 while (current >= pivotValue && lo < hi) {
				 lo++;
				 if(pivot.getCryptogramCompleted()==0) {
						current=0;
					}else {
						current=((double)allPlayers.get(lo).getCryptogramCompleted()/allPlayers.get(lo).getCryptogramPlayed())*100;
					}
			 }
			 if(pivot.getCryptogramCompleted()==0) {
					current=0;
			 }else {
					current=((double)allPlayers.get(hi).getCryptogramCompleted()/allPlayers.get(hi).getCryptogramPlayed())*100;
			 }
			 while (pivotValue >= current && lo < hi ) {
				hi--;
				if(pivot.getCryptogramCompleted()==0) {
					current=0;
				}else {
					current=((double)allPlayers.get(hi).getCryptogramCompleted()/allPlayers.get(hi).getCryptogramPlayed())*100;
				}
			 }
			 if( lo < hi ) {
				 Player temp = allPlayers.get(lo);
				 allPlayers.set(lo,allPlayers.get(hi));
				 Collections.swap(allPlayers, lo, hi);
				 allPlayers.set(hi, temp);
			 }
		 }
		allPlayers.set(last, allPlayers.get(hi));
		allPlayers.set(hi, pivot);
		
		 return(hi);
		
	}
	
	
	
	ArrayList<Double> getAllPlayersAccuracies() {
		ArrayList<Double> playerAccuracies= new ArrayList<Double>();
		if(!allPlayers.isEmpty()) {
			for (Player player:allPlayers) {
				playerAccuracies.add(player.getAccuracy());
			}
			return playerAccuracies;
		}else {
			return null;
		}
		
	}
	
	ArrayList<Integer> getAllPlayersCryptogramsPlayed() {
		ArrayList<Integer> playerPlayed= new ArrayList<Integer>();
		if(!allPlayers.isEmpty()) {
			for (Player player:allPlayers) {
				playerPlayed.add(player.getCryptogramPlayed());
			}
			return playerPlayed;
		}else {
			return null;
		}
		
	}
	
	ArrayList<Integer> getAllPlayersCompletedCryptos() {
		ArrayList<Integer> playerCompleted= new ArrayList<Integer>();
		if(!allPlayers.isEmpty()) {
			for (Player player:allPlayers) {
				playerCompleted.add(player.getCryptogramCompleted());
			}
			return playerCompleted;
		}else {
			return null;
		}
		
	}
	
	ArrayList<String> getAllPlayersName() {
		ArrayList<String> playerNames= new ArrayList<String>();
		if(!allPlayers.isEmpty()) {
			for (Player player:allPlayers) {
				playerNames.add(player.getUsername());
			}
			return playerNames;
		}else {
			return null;
		}
		
	}
}
