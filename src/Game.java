import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class Game {

	private String playerGameMapping;
	private Player currentPlayer;
	private Cryptogram currentCrypto;
	private Players playerCollection;
	private String cryptType;
	private Scanner sca;
	private String cryptFile ="cryptogram.txt";
	
	
	Game(String p, String cType) {

		loadPlayer(p);
		setCryptType(cType);
		if (getCryptType().equals("Number")) {
			currentCrypto = new NumberCryptogram();
			currentPlayer.incrementGames();
			generateCryptogram();
		} 
		else if (getCryptType().equals("Letter")){
			currentCrypto = new LetterCryptogram();
			currentPlayer.incrementGames();
			generateCryptogram();
		}
		else {
			return;
		}
		
	}

	Game(String p) {
		loadPlayer(p);
	}
	
	Cryptogram getCurrentCrypto() {
		return currentCrypto;
	}
	
	Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	
	void setCurrentCrypto(Cryptogram currentCrypto) {
		this.currentCrypto = currentCrypto;
	}
	
	public String getCryptType() {
		return cryptType;
	}

	public void setCryptType(String cryptType) {
		this.cryptType = cryptType;
	}

	public String getPlayerGameMapping() {
		return playerGameMapping;
	}
	
	
	void setPlayerGameMapping(String playerGameMapping) {
		this.playerGameMapping=playerGameMapping;
	}
	
	void playGame() {
		boolean exiting = false;
		String user_input;
		sca = new Scanner(System.in);
		while (!exiting) {
			System.out.println(currentCrypto.getPhrase());
			System.out.println(playerGameMapping + "\r\n");
			System.out.println(

					"Welcome to your cryptogram. What would you like to do to?:"
					+ "\r\n ENTER: Enter a letter to map to a cryptogram value"
					+ "\r\n UNDO: Undo a letter mapped to a cryptogram value"
					+ "\r\n FREQUENCY: See the frequencies of all the letters in the cryptogram"
					+ "\r\n HINT: Get a hint for a letter"
					+ "\r\n SHOW: Show the solution to the cryptogram"
					+ "\r\n SAVE: Save your cryptogram progress to play it at another time"
					+ "\r\n QUIT: Stop playing the cryptogram\r\n"


			);
			user_input = sca.nextLine();
			switch (user_input.toUpperCase()) {
			case "ENTER":
				exiting=this.enterLetter();
				break;
			case "UNDO":
				this.undoLetter();
				break;
			case "FREQUENCY":
				this.viewFrequencies();
				break;
			case "HINT":
				this.getHint();
				if(!playerGameMapping.contains("?")) {  //checks if game has been complete
					String completeCrypto=getPlayerGameMapping();
				   	 if(this.getCryptType().equals("Number")) {
				   		 completeCrypto=completeCrypto.replace("    ", "*");
				   		 completeCrypto=completeCrypto.replace("`", "");
				   		 completeCrypto=completeCrypto.replace(" ", "");
				   		 completeCrypto=completeCrypto.replace("*", " ");
				   	 }
				   	if(currentCrypto.getOriginal().equals(completeCrypto)) {
				   		System.out.println(currentCrypto.getPhrase());
						System.out.println(playerGameMapping + "\r\n");
				   		System.out.println("Cryptogram has been complete");
				   		exiting= true;
				   	}
				}
				
				break;
			case "SHOW":
				this.showSolution();
				exiting = true;
				break;
			case "SAVE":
				if(this.saveGame()) {
					exiting = true;
				}
				break;
			case "QUIT":
				exiting = true;
				break;
			default:
				System.out.println("\r\nNot an option");
			}
			System.out.println("");
		}
		
		System.out.println("\r\nQuiting the game\r\n");
		playerCollection.savePlayers();

	}
	
	
	void generateCryptogram() {
		setPlayerGameMapping("");
		for (int i = 0; i < currentCrypto.getPhrase().length(); i++) {
			if ( (currentCrypto.getPhrase().charAt(i) >= 'A' && currentCrypto.getPhrase().charAt(i) <= 'Z') 
					|| (currentCrypto.getPhrase().charAt(i)>= '0' && currentCrypto.getPhrase().charAt(i)<= '9'))
			{
				setPlayerGameMapping(getPlayerGameMapping() + '?');
			} else {
				setPlayerGameMapping(getPlayerGameMapping() + currentCrypto.getPhrase().charAt(i));
			}
		}
	}

	void loadPlayer(String p) {
		playerCollection= new Players();
		if(playerCollection.findPlayers(p)==null) {
			if(playerCollection.corruptedPlayers(p)) {
				System.out.println("\nSorry: your account has been corrupted. The data is lost so new player data is being created.");
			}else {
				System.out.println("\nNo Player Data Found. New Player Being Created.");
			}
			currentPlayer= new Player(p);
			playerCollection.addPlayer(currentPlayer);
			playerCollection.savePlayers();


			System.out.println("New Player Data Added\n");

		}else {
			currentPlayer=playerCollection.findPlayers(p);
			
		}
	}
	
	void printPlayerStats() {
		System.out.println("Player Name: "+ currentPlayer.getUsername());
		System.out.println("Accuracy: "+ currentPlayer.getAccuracy());
		System.out.println("Cryptograms Played: "+ currentPlayer.getCryptogramPlayed());
		System.out.println("Cryptograms Completed: "+ currentPlayer.getCryptogramCompleted()+"\n");
	}
	
	boolean enterLetter() {
        Character letterGuess = 'a';
        Character valueChar = 'a';
        String[] splitCrypto = currentCrypto.getPhrase().split(" ");
        String[] splitPlayerSol= getPlayerGameMapping().split(" ");
        int valueInt = 0;
        boolean correct =false;
       
        

        if(this.getCryptType().equals("Number")) {
        	Boolean valid =false;
        	 while(!valid) {
        		 try {
			         System.out.println("Enter the cryptogram value you're placing your guess at: ");
			         valueInt = sca.nextInt();
			         sca.nextLine();
			         for (int i=0; (i<splitCrypto.length)&&(!valid); i++) {
			        	 if(splitCrypto[i] !="" && Integer.parseInt(splitCrypto[i])==valueInt) {
			        		 if(!splitPlayerSol[i].contains("?")) {
			        			 System.out.println("You already have a mapping for this value. Do you want to replace it? (Answer with Yes): ");
			        			 String answer = sca.nextLine();
			        			 answer=answer.toUpperCase();
			        			 if(!answer.equals("Y")&& !answer.equals("YES")) {
			        				 System.out.print("Stopping Enter");
			        				 return false;
			        			 }
			        		 }
			        		 valid=true;
			        	 }
			         }
			         if(!valid) {
			        	 System.out.println("Value not in cryptogram: try again");
			           }
        		 } catch(InputMismatchException e) {
        			 System.out.println("Not valid input type");
        			 sca.nextLine();
        		 }
        	 }
        }else {
        	    while(!(currentCrypto.getPhrase().contains(valueChar.toString()))) { 
		            System.out.println("Enter the cryptogram value you're placing your guess at: ");
		            valueChar = sca.next().charAt(0);
		            sca.nextLine();
		            valueChar=Character.toUpperCase(valueChar);
		            if(!(currentCrypto.getPhrase().contains(valueChar.toString()))) {
		            	System.out.println("Value not in cryptogram: try again");
		            }
        	    }
        	    
        	    if(getPlayerGameMapping().charAt(currentCrypto.getPhrase().indexOf(valueChar))!='?') {
        	    	System.out.println("You already have a mapping for this value. Do you want to replace it? (Answer with Yes): ");
       			 	String answer = sca.nextLine();
       			 	answer=answer.toUpperCase();
       			 	if(!answer.equals("Y")&& !answer.equals("YES")) {
       			 		System.out.println("Stopping Enter");
       			 		return false;
       			 	}
        	    }
        } 

        while(letterGuess < 'A' || letterGuess > 'Z') { 
            System.out.println("Enter the plain letter you're guessing: ");
            letterGuess = sca.next().charAt(0);
            sca.nextLine();
            letterGuess=Character.toUpperCase(letterGuess);
            if(letterGuess < 'A' || letterGuess > 'Z') {
                System.out.println("Error: please enter a character between A and Z");
            }
        }
        
        if(getPlayerGameMapping().contains(letterGuess.toString())) {
            System.out.println("Error: " + letterGuess + " is already entered into the cryptogram, please try again");
            return false;
        }
 

        correct = addLetter(valueInt, valueChar, letterGuess);
        
        currentPlayer.incrementTotalGuesses();
        if (correct) {
        	currentPlayer.incrementCorrectGuesses();
        }
        currentPlayer.updateAccuracy();
        
        if(!getPlayerGameMapping().contains("?")) {
        	return finishedCrypto();
        }
        
        return false;
       
    }

	
	boolean addLetter(int valueInt, char valueChar, char letterGuess) {
		String[] splitCrypto = currentCrypto.getPhrase().split(" ");
        String[] splitPlayerSol= getPlayerGameMapping().split(" ");
        boolean correct =false;
        
        
        
		if(this.getCryptType().equals("Number")) {
        	String temp ="";
        	for (int i=0; i<splitPlayerSol.length; i++) {
	        	 if(splitCrypto[i] !="" && Integer.parseInt(splitCrypto[i])==valueInt) {
	        		 temp=temp+letterGuess;
	        		 if(valueInt>9) {
	        			 temp= temp + "`";
	        		 }
	        		 temp=temp+" ";
	        		 if(currentCrypto.getPlainLetter(Integer.parseInt(splitCrypto[i]))==letterGuess) {
	        			 correct=true;
	        		 }
	        	 }else {
	        		 temp=temp+splitPlayerSol[i]+" ";
	        	 }
	         }
        	setPlayerGameMapping(temp.trim());

        } else {
        	for (int i = 0; i < currentCrypto.getPhrase().length(); i++){
        		if (currentCrypto.getPhrase().charAt(i)==valueChar) {
        			setPlayerGameMapping(getPlayerGameMapping().substring(0, i) + letterGuess + getPlayerGameMapping().substring(i+1));
        			if(currentCrypto.getPlainLetter(currentCrypto.getPhrase().charAt(i))==letterGuess) {
	        			 correct=true;
	        		 }
        		}
        	}
            
        }
		
		return correct;
		
	}
	
	
	
	boolean finishedCrypto() {
		String completeCrypto=getPlayerGameMapping();
	   	 if(this.getCryptType().equals("Number")) {
	   		 completeCrypto=completeCrypto.replace("    ", "*");
	   		 completeCrypto=completeCrypto.replace("`", "");
	   		 completeCrypto=completeCrypto.replace(" ", "");
	   		 completeCrypto=completeCrypto.replace("*", " ");
	   	 }
	   	if(currentCrypto.getOriginal().equals(completeCrypto)) {
	   		System.out.println("Congratulations the cryptogram has been complete");
	   		currentPlayer.incrementCompleteGames();
	   		return true;
	   	}else {
	   		System.out.println("Sorry - Cryptogram solved incorrectly");
	   		return false;
	   	}
	}
	
	
	
	
	void undoLetter() {

            String input;
            char letter='a';
            Boolean valid=true;
            do {
                System.out.println("Enter a valid letter to remove from your mapping");
                input = sca.nextLine();
                letter = input.charAt(0);
                letter = Character.toUpperCase(letter);
            } while (! (letter >= 'A' && letter <= 'Z'));

               
                try {
                	letter = input.charAt(0);
                    letter = Character.toUpperCase(letter);
                } catch(IndexOutOfBoundsException e) {
                	valid=false;
                }
                
            while (! (letter >= 'A' && letter <= 'Z') || !(input.substring(1).equals("") || !valid));
   
            if (!(playerGameMapping.contains("" + letter))) {
            	System.out.println("The letter, " + letter + ", is not in your mapping");
            	return;
            }
            
            removeLetter(letter);

	}
	
	void removeLetter(char letter) {
		if(cryptType=="Number") {
       	 	String ph = currentCrypto.getPhrase().replaceAll("    ",  " _ ");
            String[] tokeniser = ph.split(" ");
            String[] mappingTokeniser = playerGameMapping.replaceAll("    ",  " _ ").split(" ");
            int cryptvalue = 0;
            
            for(int i=0; i<tokeniser.length;i++) {
            	if(!mappingTokeniser[i].equals("_") && mappingTokeniser[i].charAt(0)==letter) {
            		 cryptvalue=Integer.parseInt(tokeniser[i]);
            		break;
            	}
            }
           
            if(cryptvalue>=10) {
           	 while (playerGameMapping.indexOf(letter)>=0) {
           		 int i = playerGameMapping.indexOf(letter);
           		 playerGameMapping=playerGameMapping.substring(0,i)+"??"+playerGameMapping.substring(i+2);
           		 
           	 }
           	 return;
            }
       }
		setPlayerGameMapping(playerGameMapping.replace(letter, '?'));
		return;
	}
	
	

	
	public void getHint() {
		
		Random rand = new Random();
		int hintNum = 0;
		char hintLetter = 0;
		char hintValue = 0;
		String[] mappingTokeniser = playerGameMapping.split(" ");
		String[] phraseTokeniser = currentCrypto.getPhrase().split(" ");
		
		
		
		if(cryptType=="Number") {
			ArrayList<Integer> notRightValues = new ArrayList<Integer>();
			
			
			for(int i=0; i<mappingTokeniser.length; i++) {
				if(!phraseTokeniser[i].equals("")) {
					int currentNum =Integer.parseInt(phraseTokeniser[i]);
					if((mappingTokeniser[i].equals("?") || mappingTokeniser[i].equals("??")||!currentCrypto.getPlainLetter(currentNum).equals(mappingTokeniser[i].charAt(0)))&& !notRightValues.contains(currentNum)) {
						notRightValues.add(currentNum);
					}
				}
				
			}
			
			// Get the next unmapped value in the cryptogram
			hintNum = notRightValues.get(rand.nextInt(notRightValues.size()));
			hintValue = currentCrypto.getPlainLetter(hintNum);
			
		}else {
			ArrayList<Character> notRightValues = new ArrayList<Character>();
			for(int i=0; i<playerGameMapping.length();i++) {
				Character currentChar =currentCrypto.getPhrase().charAt(i);
				if(!currentChar.equals(' ')&&(playerGameMapping.charAt(i)=='?'||!currentCrypto.getPlainLetter(currentChar).equals(playerGameMapping.charAt(i))) && !notRightValues.contains(currentChar)) {
					notRightValues.add(currentChar);
				}
			}
			
			// Get the next unmapped value in the cryptogram
			hintLetter = notRightValues.get(rand.nextInt(notRightValues.size()));
			hintValue = currentCrypto.getPlainLetter(hintLetter);
		}
		
		
		
		// Check if the player has already mapped this value
		if (playerGameMapping.indexOf(hintValue) >= 0) {
			
			// If so, remove the incorrect mapping and display a message to the player
			removeLetter(hintValue);
			
			
			System.out.println("You have already used the letter " + hintValue + ". The correct mapping is being entered for you.");
		}
		
		
		if(cryptType.equals("Letter")) {
			if(playerGameMapping.charAt(currentCrypto.getPhrase().indexOf(hintLetter))!='?') {
				System.out.println("You already have an incorrect letter at "+ hintLetter + ". The correct mapping is being entered for you.");
				
			}
		}else {
			for(int i=0; i<mappingTokeniser.length; i++) {
				if(!phraseTokeniser[i].equals("")&& Integer.parseInt(phraseTokeniser[i])==hintNum) {
					if(!mappingTokeniser[i].equals("?") && !mappingTokeniser[i].equals("??")) {
						System.out.println("You already have an incorrect letter at "+ hintNum + ". The correct mapping is being entered for you.");
						break;
					}else {
						break;
					}
				}
			}
			
		}
		
		
		
		// Add the correct mapping to the player's mapping and display the hint to the player
		
		addLetter(hintNum,hintLetter,hintValue);
		
		
		if(cryptType.equals("Letter")) {
			System.out.println("Hint: The letter " +hintValue + " maps to " + hintLetter + ".");
		}else {
			System.out.println("Hint: The letter " +hintValue + " maps to " + hintNum + ".");
		}
		
		
	}
	
	

	void viewFrequencies() {
		// Hard code frequency of English letters in text
		double englishFr[] = {8.2, 1.5, 2.8, 4.3, 13, 2.2, 2, 6.1, 7, 0.15, 0.77, 4, 2.4, 6.7, 7.5, 1.9, 0.095, 6, 6.3, 9.1, 2.8, 0.98, 2.4, 0.15, 2, 0.074};
		String alph = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		double enPhNum[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		String enPh = null;
		int position = 0;
		int length = 0;
		try {
			if(this.cryptType.equals("Letter")) {
				enPh = currentCrypto.getPhrase().replaceAll(" ", "");
				length = enPh.length();
				
				for(int i = 0; i < enPh.length(); i++) {
					// Set position to the position of character i of enPh in the alphabet, e.g B = 1
					position = alph.indexOf(enPh.charAt(i));
					// Increment counter at that position, keep track of number of each letter
					enPhNum[position]++;
					
				}
			}
			else {
				enPh = currentCrypto.getPhrase().replaceAll("    ",  " ");
				StringTokenizer tokenizer = new StringTokenizer(enPh, " ");
				
				while(tokenizer.hasMoreTokens()) {
					String item = tokenizer.nextToken();
					// Set position to integer value of enciphered value -1, e.g 22 corresponds to enPhNum[21]
					position = Integer.parseInt(item)-1;
					// Increment counter at that position, keep track of number of each letter
					enPhNum[position]++;
					length++;
				}
				
			}
		}
		catch(NullPointerException e) {
			System.out.println("Error, there is no original phrase.");
			System.out.println("Cannot find the frequencies of letters with no original phrase.");
			return;
		}
		
		
		DecimalFormat df = new DecimalFormat("#.##");
		double curr = 0;
		for(int i = 0; i < 26; i++) {
			curr = enPhNum[i];
			curr = (curr/length) * 100;
			enPhNum[i] = Double.parseDouble(df.format(curr));
		}
		System.out.println("English Language Frequencies \t Frequency In the Enciphered Phrase");
		if(this.cryptType.equals("Letter")) {
			for(int i = 0; i < 26; i++) {
				System.out.println(alph.charAt(i) + ": " + englishFr[i] + "% \t \t \t " + alph.charAt(i) + ": " + enPhNum[i] + "%");
			}
		}
		else {
			for(int i = 0; i < 26; i++) {
				System.out.println(alph.charAt(i) + ": " + englishFr[i] + "% \t \t \t " + (i+1) + ": " + enPhNum[i] + "%");
			}
		}
	} 
	
	boolean saveGame() {
		String currUser = currentPlayer.getUsername().toUpperCase();
		String typeOf = this.getCryptType();
        String ogPh = currentCrypto.getOriginal();
        String enPh = currentCrypto.getPhrase();
        String pMap = getPlayerGameMapping();
        PrintWriter writer;
        boolean savingOg = false;
        String ogSave="";
        
		if(loadGame(currUser)) { // checks if loadGame returns true - they have a saved cryptogram
			
			ogSave=(currentPlayer.getUsername() +" "+ cryptType +" "+ (currentCrypto.getOriginal().replace(" ", "_")) + " " + (currentCrypto.getPhrase().replace(" ", "_")) + " " +(playerGameMapping.replace(" ", "_")));
				
			
			
			String saveChoice;
			this.setCryptType(typeOf);
	        currentCrypto.setPhrase(ogPh, enPh);
	        setPlayerGameMapping(pMap);
	        
			
	        
			sca = new Scanner(System.in);
			boolean choice = false;
			while(!choice) {
				System.out.println(

					"You already have a cryptogram saved to file, would you like to:"
					+ "\r\n OVERWRITE: Overwrite your saved cryptogram by saving your current cryptogram game"
					+ "\r\n RETURN: Return to your current game and keep your original saved cryptogram");
					
				saveChoice = sca.nextLine();
				switch (saveChoice.toUpperCase()) {
				case "OVERWRITE":
					choice = true;
					savingOg= false;
					break;
				case "RETURN":
					choice = true;
					savingOg= true;
					break;
				default:
					System.out.println("\r\nNot an option");
				}
			}
		}
			
		// Read the existing file data and store it in a list
		List<String> fileData = new ArrayList<>();
		String fullLine="";
		String[] splitCryptoDetails;
		try {
		    // Read the existing file data and store it in a list
		    Scanner reader = new Scanner(new File(cryptFile));
		    while (reader.hasNextLine()) {
	        	
	        	fullLine=reader.nextLine();
	        	splitCryptoDetails= fullLine.split(" ");
	        	if(!splitCryptoDetails[0].equals(currUser)) {
	
	        		fileData.add(fullLine);
	        	}
	            
	        }
		    reader.close();
		        
		} catch(NoSuchElementException e){
			if(!fullLine.equals("")) {
				fileData.add(fullLine);
			}
				
				
		} catch (FileNotFoundException e) {
			System.out.println("Error with finding cryptogram file.");
			return false;
		}
		    
		if(savingOg) {
		    fileData.add(ogSave);
		}else {
		    // Append the new data for the current user to the list
			fileData.add(currUser +" "+ typeOf +" "+ ogPh.replace(" ", "_")+" "+enPh.replace(" ", "_")+" "+pMap.replace(" ", "_"));
		}
		    
		       
		// Write the updated data back to the file
		    
		try {
			 writer = new PrintWriter(cryptFile);
					
			 for (String line:fileData) {
				writer.println(line);
			}
			writer.close();
			
			
	    } catch (IOException e) {
		    System.out.println("Error writing data to file");
		    return false;
		}
		
		if(savingOg) {
			return false;
		}else {
			System.out.println("Cryptogram game data saved");
			return true;
			
		}
			
			
		
		
			 
	}
	

	
	boolean loadGame(String currUser) {
		List<String> fileData = new ArrayList<>();
		String fullLine = null;
		String username="";
		String type = null;
		String ogPhrase = null;
		String enPhrase = null;
		String playerMapping = null;
		Boolean corrupt =false;
		String[] splitCryptoDetails = null;
		
		try {
	        // Read the existing file data and store it in a list
	        Scanner reader = new Scanner(new File(cryptFile));
	        while (reader.hasNextLine()) {
	        	
	        	fullLine=reader.nextLine();
	        	splitCryptoDetails= fullLine.split(" ");
	        	if(splitCryptoDetails[0].equals(currUser)) {
	        		
	        		if(splitCryptoDetails.length!=5) {
	        			System.out.println("Sorry your save data has been corrupted and cannot be loaded.");
	    				corrupt=true;
	        		}else {
	        			username=splitCryptoDetails[0];
		        		type=splitCryptoDetails[1];
		        		ogPhrase=splitCryptoDetails[2].replace("_", " ");
		        		enPhrase=splitCryptoDetails[3].replace("_", " ");
		        		playerMapping=splitCryptoDetails[4].replace("_", " ");
	        		}
	        		
	        		
	        	}else {
	        		fileData.add(fullLine);
	        	}
	        	
	        	
	        	
	            
	        }
	        reader.close();
	        
		} catch(NoSuchElementException e){
			if(splitCryptoDetails[0].equals(currUser)) {
				System.out.println("Sorry your save data has been corrupted and cannot be loaded.");
				corrupt=true;
			}else if (!splitCryptoDetails[0].equals("")){
				fileData.add(fullLine);
			}
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Error with finding cryptogram file.");
			return false;
		}
	       
		 if (!corrupt && !username.equals("")) {
		    	 // Set user game data from file
		        this.cryptType = type;
		        if (this.cryptType.equals("Number")) {
		        	currentCrypto = new NumberCryptogram(ogPhrase, enPhrase);
		        } 
		        else {
		        	currentCrypto = new LetterCryptogram(ogPhrase, enPhrase);
		        }
		        this.setPlayerGameMapping(playerMapping);

		        
		 }
		     
		 try { 
		   // Write the updated data back to the file
		   PrintWriter fw = new PrintWriter(cryptFile);
		   if(!fileData.isEmpty()) {
			   for (String line : fileData) {
				   fw.println(line);
			   }
		   }
		   fw.close();
		   
		 } catch (IOException e) {
		      System.out.println("An error occurred in rewriting cryptogram file.");
		      return false;
		 }
	    
		 if(corrupt|| username.equals("")) {
			 return false;
		 }else {
			 return true;
		 }
		
	}



	public void getLeaderboard() {
		if(playerCollection.sorter()) {
			ArrayList<String> usernames=playerCollection.getAllPlayersName();
			ArrayList<Double> completedAccuracies=playerCollection.getAllPlayersAccuracies();
			ArrayList<Integer> completedPlayed=playerCollection.getAllPlayersCryptogramsPlayed();
			ArrayList<Integer> completedCryptos=playerCollection.getAllPlayersCompletedCryptos();
			System.out.println("--Leaderboard--");
			System.out.println("\tUsername  \tCryptogram Completion Proportion \tCryptograms Played \tCryptogramsCompleted \tGuess Accuracy");
			for(int i=0;i<10;i++) {
				if (usernames.size()>i && completedCryptos.get(i)>0) {
					double proportion =(double)completedCryptos.get(i)/completedPlayed.get(i)*100;
					proportion= (double)Math.round(proportion*100)/100;
					System.out.println((i+1)+".\t"+usernames.get(i)+ "\t\t"+proportion+"%\t\t\t\t\t"+completedPlayed.get(i)+"\t\t\t"+completedCryptos.get(i)+"\t\t\t"+completedAccuracies.get(i)+"%");
				}else {
					System.out.println((i+1)+".");
					
				}
			}
			System.out.println("\n");
		}else {
			System.out.println("Apologies- there are no player stats to show\n\n");
		}
	}
	

	void showSolution() {
        String original = currentCrypto.getOriginal();
        try {
            if(this.cryptType.equals("Letter")) {
                playerGameMapping=original;
            }
            else {
                original = original.replaceAll(" ",  "");
                String ph = currentCrypto.getPhrase().replaceAll("    ",  " _ ");
                StringTokenizer tokenizer = new StringTokenizer(ph, " ");
                String newPh = "";
                int numbers[] = new int[100];
                int c = 0;   // keep track of counter for numbers array
                int i = 0;   // keep track of characters in original phrase
                
                while(tokenizer.hasMoreTokens()) {
                    String item = tokenizer.nextToken();
                    //  
                    if(!item.equals("_")) {
                        numbers[c] = Integer.parseInt(item);
                        if(numbers[c] > 9) {
                            newPh = newPh.concat(original.charAt(i) + "` ");
                        }
                        else {
                             newPh = newPh.concat(original.charAt(i) + " ");
                        }
                        i++;
                    }
                    else {
                        newPh = newPh.concat("   ");
                    }
                    c++;
                }
        
                playerGameMapping=newPh;
                
             }
            System.out.println("\n\nSolution:");
            System.out.println(currentCrypto.getPhrase());
			System.out.println(playerGameMapping + "\r\n");
            
        }
        catch(NullPointerException e) {
            System.out.println("Error there is no original phrase so can't print solution.");
            return;
        }
        
    }
	

}