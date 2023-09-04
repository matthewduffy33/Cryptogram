import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class cryptogramTestIt2 {
	
	private Game testerGame;
	private Game testerGameNum;
	private InputStream stdin = System.in;
    private PrintStream stdout = System.out;
    private ArrayList<String> ogFile;
    private ArrayList<String> ogCryptoFile;
    private cryptogramTest test;

    @Before

    public void setUp() { 
    	
    	ogFile= new ArrayList<String>();
    	String fullLine="";
		Scanner reader;
    	try {
			reader = new Scanner(new File("Players.txt"));
	
			while (reader.hasNextLine()) {
					fullLine="";
					try {
						fullLine="" + reader.next() ;
						fullLine= fullLine+  " "+ reader.nextInt();
						fullLine=fullLine +  " "+  reader.nextInt();
				        fullLine=fullLine +  " "+   reader.nextInt();
				        fullLine=fullLine +  " "+ reader.nextInt();
				    }catch(NoSuchElementException e){
						
					}
					ogFile.add(fullLine);
				
		      }
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    	
    	ogCryptoFile= new ArrayList<String>();
    	
    	try {
			reader = new Scanner(new File("cryptogram.txt"));
	
			while (reader.hasNextLine()) {
	        	
	        	fullLine=reader.nextLine();
	        	ogCryptoFile.add(fullLine);
	        	}
	        	
	        	
	        	
	            
	        
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    	
    	
    	test = new cryptogramTest();
    	test.setUp();
    	testerGame = test.testerGame;
    	testerGameNum = test.testerGameNum;
    	
    	
		
    	
    }
	

    
   
    
    /*Scenario  cryptogram successfully completed*/
    @Test 
    public void incrementSuccessfulCrypto() {
    	int cryptoComplete = testerGame.getCurrentPlayer().getCryptogramCompleted();
    	
    	test.finishingLetter();
    	Assert.assertEquals(testerGame.getCurrentPlayer().getCryptogramCompleted() , cryptoComplete+1);
    }
    
    /*Scenario  cryptogram unsuccessfully completed*/
    @Test 
    public void incrementUnsuccessfulCrypto() {
    	int cryptoComplete = testerGame.getCurrentPlayer().getCryptogramCompleted();
    	test.failingFinishingLetter();
    	Assert.assertEquals(testerGame.getCurrentPlayer().getCryptogramCompleted() , cryptoComplete);
    }
    
    
    
    /*Scenario  new cryptogram played*/
    
    @Test 
    public void newCrypto() {
    	Player newPlayer = new Player("test2");
    	int cryptoMade = newPlayer.getCryptogramPlayed();
    	Game newGame = new Game(newPlayer.getUsername(), "Number");
    	int newCrypto = newGame.getCurrentPlayer().getCryptogramPlayed();
    	Assert.assertEquals(newCrypto,cryptoMade+1);
    }
    
    /*Scenario  cryptogram loaded*/

    @Test 
    public void loadOldCrypto() {
    	int cryptoMade = testerGame.getCurrentPlayer().getCryptogramPlayed();
    	Player currentPlayer = testerGame.getCurrentPlayer();
    	if (cryptoMade == 0) {
    	  testerGame.saveGame();
    	}
    	Game game = new Game(currentPlayer.getUsername());
    	game.loadGame(currentPlayer.getUsername());
    	Assert.assertEquals(currentPlayer.getCryptogramPlayed(), cryptoMade);
    }
    
    //Scenario- correct guess made
    @Test 
    public void correctGuessUpLetter() {
    	  LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
    	  char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
          String answer = testerLetterCrypto.getOriginal();
          char answerFirstChar = answer.charAt(0);
          int numRightGuesses = testerGame.getCurrentPlayer().getCorrectGuesses();
          int numGuesses= testerGame.getCurrentPlayer().getTotalGuesses();
	
	        ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\n" + answerFirstChar +"\nQUIT\n").getBytes());System.setIn(in);
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		       System.setOut(new PrintStream(out));
	        
		   	    testerGame.playGame();
		    	
		        System.setIn(stdin);
		        System.setOut(stdout);

		        Assert.assertEquals(numRightGuesses+1, testerGame.getCurrentPlayer().getCorrectGuesses());
		        Assert.assertEquals(numGuesses+1, testerGame.getCurrentPlayer().getTotalGuesses());
    }
    
    //Scenario- incorrect guess made
    @Test 
    public void incorrectGuessUpLetter() {
    	  LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
    	  char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
          String answer = testerLetterCrypto.getOriginal();
          int numRightGuesses = testerGame.getCurrentPlayer().getCorrectGuesses();
          int numGuesses= testerGame.getCurrentPlayer().getTotalGuesses();
	    
	        char notInAns = 'A';
	        while(answer.contains(notInAns + "")) {
	        	notInAns++;
	        }
	        
	
	        ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\n" + notInAns +"\nQUIT\n").getBytes());System.setIn(in);
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		       System.setOut(new PrintStream(out));
	        
		   	    testerGame.playGame();
		    	
		        System.setIn(stdin);
		        System.setOut(stdout);
		        
		        Assert.assertEquals(numRightGuesses, testerGame.getCurrentPlayer().getCorrectGuesses());
		        Assert.assertEquals(numGuesses+1, testerGame.getCurrentPlayer().getTotalGuesses());
    }
    
    @Test
    public void showStats() {
    	 ByteArrayInputStream in = new ByteArrayInputStream(("TEST\n"+"STATS\n"+"\nEXIT\n").getBytes());
    	 
    	 System.setIn(in);
	    	
 	    ByteArrayOutputStream out = new ByteArrayOutputStream();
 		System.setOut(new PrintStream(out));
 		      
 		 main.main(null);
 		 
 		 	
 		 System.setIn(stdin);
 		 System.setOut(stdout);
 		 
 		Players ps = new Players(); 
 		Player p = ps.findPlayers("TEST");
 		
 		String output = out.toString();
 		
 		output=(output.substring(output.indexOf("Accuracy")));
 		String accuracy=output.substring(0,output.indexOf("C")-2);
 		Assert.assertEquals(accuracy, "Accuracy: "+p.getAccuracy());
        
        output=(output.substring(output.indexOf("Cryptograms Played:")));
 		String played=output.substring(0,output.substring(1).indexOf("C")-1);
        Assert.assertEquals(played, "Cryptograms Played: "+p.getCryptogramPlayed());
        
        output=(output.substring(output.indexOf("Cryptograms Completed:")));
 		String completed=output.substring(0,output.substring(1).indexOf("W")-2);
 		Assert.assertEquals(completed, "Cryptograms Completed: "+p.getCryptogramCompleted());
    }
    
    
    @Test
    public void storeDetails() throws FileNotFoundException {
    	Game newGame = new Game("storeTest", "Letter");
    	LetterCryptogram testerLetterCrypto = (LetterCryptogram) newGame.getCurrentCrypto();
  	    char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
        String answer = testerLetterCrypto.getOriginal();
        char answerFirstChar = answer.charAt(0);

    	ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\n" + answerFirstChar +"\nQUIT\n").getBytes());System.setIn(in);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	        
		newGame.playGame();
		    	
	    System.setIn(stdin);
		System.setOut(stdout);
		String fullLine;
		String outputLine="";
		ArrayList<String> temp= new ArrayList<String>();
		Scanner reader = new Scanner(new File("Players.txt"));
		while (reader.hasNextLine()) {
				fullLine="";
				try {
					fullLine="" + reader.next() ;
					fullLine= fullLine+  " "+ reader.nextInt();
					fullLine=fullLine +  " "+  reader.nextInt();
			        fullLine=fullLine +  " "+   reader.nextInt();
			        fullLine=fullLine +  " "+ reader.nextInt();
			    }catch(NoSuchElementException e){
					
				}
				if(fullLine.contains("storeTest")) {
					outputLine=fullLine;
				}else {
					temp.add(fullLine);
				}
			
	      }
		Assert.assertEquals("storeTest 1 1 1 0", outputLine);

    }
    
    

    @Test
//Scenario  player details loaded
    public void loadDetails() throws FileNotFoundException {
    	try {
			PrintWriter writer = new PrintWriter("Players.txt");
		
			writer.println("playerLoadTest 32 12 43 22");
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving player stats.");
		}
		
		Players ps = new Players(); 
 		Player p = ps.findPlayers("playerLoadTest");
 		
		Assert.assertEquals("playerLoadTest", p.getUsername());
		Assert.assertEquals(32, p.getTotalGuesses());
		Assert.assertEquals(12, p.getCorrectGuesses());
		Assert.assertEquals(43, p.getCryptogramPlayed());
		Assert.assertEquals(22, p.getCryptogramCompleted());

    }
    
    
   //Scenario- error loading player details
    @Test
    public void loadDetailsCorruptedPlayer() {
    	try {
			PrintWriter writer = new PrintWriter("Players.txt");
		
			writer.println("CorruptedPlayer 32 12 43");
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving player stats.");
		}
    	
    	ByteArrayInputStream in = new ByteArrayInputStream(("QUIT\n").getBytes());System.setIn(in);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	        
		Game testGame = new Game("CorruptedPlayer", "Letter");

		System.setOut(stdout);
 		String output = out.toString();
 		
 		
 		Assert.assertTrue(output.contains("Sorry: your account has been corrupted. The data is lost so new player data is being created."));
    	
 		
 		Players ps = new Players(); 
 		Player p = ps.findPlayers("CorruptedPlayer");
 		Assert.assertTrue(0==p.getTotalGuesses());
 		Assert.assertTrue(0==p.getCorrectGuesses());
 		Assert.assertTrue(0==p.getCryptogramPlayed());
 		Assert.assertTrue(0==p.getCryptogramCompleted());
    }
    
//Scenario  error loading player, they dont exist
    @Test
    public void loadDetailsNotExistPlayer() {
    	
    	this.rewritingFiles();
    	
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	        
		Game testGame = new Game("loadDetailsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAr", "Letter");
		    	
	    System.setIn(stdin);
		System.setOut(stdout);
 		String output = out.toString();
 		Assert.assertTrue(output.contains("New Player Data Added"));
 		
 		Players ps = new Players(); 
 		Player p = ps.findPlayers("loadDetailsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAr");
 		Assert.assertTrue(0==p.getTotalGuesses());
 		Assert.assertTrue(0==p.getCorrectGuesses());
 		Assert.assertTrue(0==p.getCryptogramPlayed());
 		Assert.assertTrue(0==p.getCryptogramCompleted());
    	 
    }
    
    
 
    
    
    /*TO DO: add test cases for user stories
    4) save a cryptogram 
    5) load a cryptogram 
    see user stories for acceptance tests
    */
    
    //Scenario: player saves cryptogram
    @Test
    public void successfulSave() {
    	
    	
        ByteArrayInputStream in = new ByteArrayInputStream(("SAVE\nQUIT\n").getBytes());
    	System.setIn(in);
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
       
        
        Game tester = new Game("SaveTester", "Letter");
	    tester.playGame();
        System.setIn(stdin);
        System.setOut(stdout);
        
        String currUser = "SAVETESTER";
		String typeOf = tester.getCryptType();
        String ogPh = tester.getCurrentCrypto().getOriginal().replace(" ", "_");
        String enPh = tester.getCurrentCrypto().getPhrase().replace(" ", "_");
        String pMap = tester.getPlayerGameMapping().replace(" ", "_");
        String expectedOutputLine = currUser +" "+ typeOf +" "+ ogPh+" "+enPh+" "+pMap;
        
		String fullLine;
		String readLine="";
		String[] splitCryptoDetails;
		Scanner reader;
		try {
			reader = new Scanner(new File("cryptogram.txt"));

			while (reader.hasNextLine()) {
	        	
	        	fullLine=reader.nextLine();
	        	splitCryptoDetails= fullLine.split(" ");
	        	if(splitCryptoDetails[0].equals(currUser)) {
	        		readLine=fullLine;
	        	}
	        	
	        	
	        	
	            
	        }
		} catch (FileNotFoundException e) {
			Assert.fail();
		}
		
		Assert.assertTrue(readLine.contains( expectedOutputLine));    
    }
    
    //Scenario: player already has a saved cryptogram and overwrites
    @Test
    public void overwriteSave() {

          Game tester = new Game("SAVETESTER2", "Letter");
          tester.saveGame();


          String currUser = "SAVETESTER2";
  		  String typeOf = tester.getCryptType();
          String ogPh = tester.getCurrentCrypto().getOriginal().replace(" ", "_");
          String enPh = tester.getCurrentCrypto().getPhrase().replace(" ", "_");
          String pMap = tester.getPlayerGameMapping().replace(" ", "_");
          String beforeOverwrite = currUser +" "+ typeOf +" "+ ogPh+" "+enPh+" "+pMap;
        
        tester.setPlayerGameMapping(pMap.replace(pMap.charAt(0), 'A'));
        ByteArrayInputStream in = new ByteArrayInputStream(("OVERWRITE\nQUIT\n").getBytes());
    	System.setIn(in);
        tester.saveGame();
        System.setIn(stdin);
        System.setOut(stdout);
        
        
		String fullLine;
		String readLine="";
		String[] splitCryptoDetails;
		ArrayList<String> temp= new ArrayList<String>();
		Scanner reader;
		try {
			reader = new Scanner(new File("cryptogram.txt"));

			while (reader.hasNextLine()) {
	        	
	        	fullLine=reader.nextLine();
	        	splitCryptoDetails= fullLine.split(" ");
	        	if(splitCryptoDetails[0].toUpperCase().equals(currUser)) {
	        		readLine=fullLine;
	        	}
	        }
		} catch (FileNotFoundException e) {
			Assert.fail();
		}
		Assert.assertNotEquals(readLine, beforeOverwrite);    

    }
    
    //Scenario: player already has a saved cryptogram and keeps it
    @Test
    public void keepSave() {
        Game tester = new Game("SAVETESTER3", "Letter");
        tester.saveGame();


        String currUser = "SAVETESTER3";
		String typeOf = tester.getCryptType();
        String ogPh = tester.getCurrentCrypto().getOriginal().replace(" ", "_");
        String enPh = tester.getCurrentCrypto().getPhrase().replace(" ", "_");
        String pMap = tester.getPlayerGameMapping().replace(" ", "_");
        String beforeOverwrite = currUser +" "+ typeOf +" "+ ogPh+" "+enPh+" "+pMap;
      
      tester.setPlayerGameMapping(pMap.replace(pMap.charAt(0), 'A'));
      ByteArrayInputStream in = new ByteArrayInputStream(("RETURN\nQUIT\n").getBytes());
  	  System.setIn(in);
      tester.saveGame();
      System.setIn(stdin);
      System.setOut(stdout);
      
      
		String fullLine;
		String readLine="";
		String[] splitCryptoDetails;
		ArrayList<String> temp= new ArrayList<String>();
		Scanner reader;
		try {
			reader = new Scanner(new File("cryptogram.txt"));

			while (reader.hasNextLine()) {
	        	
	        	fullLine=reader.nextLine();
	        	splitCryptoDetails= fullLine.split(" ");
	        	if(splitCryptoDetails[0].toUpperCase().equals(currUser)) {
	        		readLine=fullLine;
	        	}
	        }
		} catch (FileNotFoundException e) {
			Assert.fail();
		}
		Assert.assertEquals(readLine, beforeOverwrite);    
 

    }
    
    //Scenario: player has no previously saved cryptogram game
    @Test
    public void noSave() {
    	
        ByteArrayInputStream in = new ByteArrayInputStream(("ABCTESTXYZ\nLOAD\nEXIT\n").getBytes());
    	System.setIn(in);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		
		main.main(null);
		
	    System.setIn(stdin);
		System.setOut(stdout);
 		String output = out.toString();
 		Assert.assertTrue(output.contains("No games to load for you"));
    	
    }
    //Scenario: error loading previously saved game (corrupt file)
    @Test
    public void corruptSaveLoaded() {
    	try {
			PrintWriter writer = new PrintWriter("cryptogram.txt");
			writer.println("CorruptedPlayer");
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving save files.");
		}

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	        
		Game testGame = new Game("CorruptedPlayer", "Letter");
		testGame.loadGame("CorruptedPlayer");

		System.setOut(stdout);
 		String output = out.toString();

 		Assert.assertTrue(output.contains("Sorry your save data has been corrupted and cannot be loaded."));
    }
    
    //Scenario: player loads their saved cryptogram game
    @Test 
    public void resumeCrypto() {
    	 ByteArrayInputStream in = new ByteArrayInputStream(("SAVE\nQUIT\n").getBytes());
     	System.setIn(in);
     	ByteArrayOutputStream out = new ByteArrayOutputStream();
         System.setOut(new PrintStream(out));
         
        
         
         Game tester = new Game("SaveTester2", "Letter");
 	    tester.playGame();
         System.setIn(stdin);
         System.setOut(stdout);
    	
    	Assert.assertTrue(testerGame.loadGame("SAVETESTER2"));

    }
    
    @After
    public void rewritingFiles() {
    	try {
			PrintWriter writer = new PrintWriter("Players.txt");
		
			for (String player:ogFile) {
				writer.println(player);
			}
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving player stats.");
		}
    	
    	
    	try {
			PrintWriter writer = new PrintWriter("cryptogram.txt");
		
			for (String saveFile:ogCryptoFile) {
				writer.println(saveFile);
			}
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving save files.");
		}
    }

}

