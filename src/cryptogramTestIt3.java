import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class cryptogramTestIt3 {
	
	private Game testerGame;
	private Game testerGameNum;
    private PrintStream stdout = System.out;
    private cryptogramTest test;
    private ArrayList<String> ogFile;

    @Before
    public void setUp() { 
    	test = new cryptogramTest();
    	test.setUp();
    	testerGame = test.testerGame;
    	testerGameNum = test.testerGameNum; 
    	
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
						return;
					}
					ogFile.add(fullLine);
				
		      }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    }
    
    /*6. As a player I want to be able to show the solution so I can
     *  see the answer to a cryptogram I cant solve
     */
    
    /*
     *  Scenario: player shows the solution
o Given a cryptogram is being played and hasnt been completed by the player
o When the player selects to show the solution
o Then the correct mapping is applied and the solution displayed to the player

     */
    @Test
    public void showSolutionLetter() {
    	Player playerBeforeSolution = testerGame.getCurrentPlayer();
    	
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(out));
	      
    	testerGame.showSolution();
    	
        System.setOut(stdout);
        String output = out.toString();
        Assert.assertTrue(output.contains("\n\nSolution:"));
    	
    	
    	assertEquals(playerBeforeSolution.getCryptogramCompleted(),testerGame.getCurrentPlayer().getCryptogramCompleted());
    	assertEquals(playerBeforeSolution.getCorrectGuesses(),testerGame.getCurrentPlayer().getCorrectGuesses());
    	assertEquals(playerBeforeSolution.getTotalGuesses(),testerGame.getCurrentPlayer().getTotalGuesses());
    	
    	assertEquals(testerGame.getPlayerGameMapping(),testerGame.getCurrentCrypto().getOriginal());
    }
    
    @Test
    public void showSolutionNum() {
    	Player playerBeforeSolution = testerGameNum.getCurrentPlayer();
    	
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(out));
	      
	    testerGameNum.showSolution();
    	
        System.setOut(stdout);
        String output = out.toString();
        Assert.assertTrue(output.contains("\n\nSolution:"));
    	
    	
    	assertEquals(playerBeforeSolution.getCryptogramCompleted(),testerGameNum.getCurrentPlayer().getCryptogramCompleted());
    	assertEquals(playerBeforeSolution.getCorrectGuesses(),testerGameNum.getCurrentPlayer().getCorrectGuesses());
    	assertEquals(playerBeforeSolution.getTotalGuesses(),testerGameNum.getCurrentPlayer().getTotalGuesses());
    	
    	
  		String numMappingOriginal;
  		numMappingOriginal = testerGameNum.getPlayerGameMapping().replace("    ", "*");
  		numMappingOriginal=numMappingOriginal.replace("`", "");
  		numMappingOriginal=numMappingOriginal.replace(" ", "");
  		numMappingOriginal=numMappingOriginal.replace("*", " ");
    	
    	assertTrue(testerGameNum.getCurrentCrypto().getOriginal().equals(numMappingOriginal));
    }
    
    
    

    // Scenario player views the frequencies of letters in the cryptogram

    @Test
    public void frequencyLetter() {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(out));
	    
	    testerGame.viewFrequencies();
	    
		DecimalFormat df = new DecimalFormat("#.##");
        double freqFirst = 0;
        double freqLast = 0;
        String enPh = testerGame.getCurrentCrypto().getPhrase();
        for (char letter : enPh.toCharArray()) {
        	if (letter =='A')
        	{
        		freqFirst++;
        	}
        	if (letter=='Z') {
        		freqLast++;
        	}

        }
        freqFirst = (freqFirst/enPh.replace(" ", "").length()) * 100;
        freqFirst = Double.parseDouble(df.format(freqFirst));
        
        freqLast = (freqLast/enPh.replace(" ", "").length()) * 100;
        freqLast = Double.parseDouble(df.format(freqLast));
        
        System.setOut(stdout);
        String output = out.toString();
        String[] splitOutput = output.split("\n");
        
        //test if A frequency is as expected
        assertTrue(splitOutput[1].contains("A: 8.2% \t \t \t A: "+freqFirst+"%\r"));
        //test if Z frequency is as expected
        assertTrue(splitOutput[splitOutput.length-1].contains("Z: 0.074% \t \t \t Z: "+freqLast+"%\r"));
        
        double totalFreq = 0.0;
        for (String line: splitOutput) {
        	line = line.replace("\t", "");
        	if (line.contains("%")){
        	line =	line.replace(" ", "");
        	line = line.substring(line.indexOf("%")+3, line.length()-2);
        	totalFreq += Double.valueOf(line);
        	}

        }
        
        //99.5 and 100.5 range given to account for any rounding inconsistencies
        assertTrue(totalFreq >= 99.5 && totalFreq <= 100.5);
        
	    
    }
    
    @Test
    public void frequencyNum() {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(out));
	    
	    testerGameNum.viewFrequencies();
	    
		DecimalFormat df = new DecimalFormat("#.##");
        double freqFirst = 0;
        double freqLast = 0;
        String enPh = testerGameNum.getCurrentCrypto().getPhrase().replaceAll("    ",  " ");
        double enPhLength=0;
        for (String letter : enPh.split(" ")) {
        	if (letter.equals("1"))
        	{
        		freqFirst++;
        	}
        	if (letter.equals("26")) {
        		freqLast++;
        	}
        	enPhLength++;

        }
        freqFirst = (freqFirst/enPhLength) * 100;
        freqFirst = Double.parseDouble(df.format(freqFirst));
        
        freqLast = (freqLast/enPhLength) * 100;
        freqLast = Double.parseDouble(df.format(freqLast));
        
        System.setOut(stdout);
        String output = out.toString();
        String[] splitOutput = output.split("\n");
        
        //test if A frequency is as expected
        assertTrue(splitOutput[1].contains("A: 8.2% \t \t \t 1: "+freqFirst+"%\r"));
        //test if Z frequency is as expected
        assertTrue(splitOutput[splitOutput.length-1].contains("Z: 0.074% \t \t \t 26: "+freqLast+"%\r"));
        
        double totalFreq = 0.0;
        for (String line: splitOutput) {
        	line = line.replace("\t", "");
        	if (line.contains("%")){
        	line =	line.replace(" ", "");
        	line = line.substring(line.indexOf("%")+3, line.length()-2);
        	if (line.contains(":")) {
        		line = line.replace(":","");
        	}
        	totalFreq += Double.valueOf(line);
        	}

        }
        
        //99.5 and 100.5 range given to account for any rounding inconsistencies
        assertTrue(totalFreq >= 99.5 && totalFreq <= 100.5);

    }

    /*13. As a player I want to be able to see the top 10 scores for
    number of successfully completed cryptograms*/
    
    @Test
    public void fullPlayerTop10() {

    	try {
			PrintWriter writer = new PrintWriter("Players.txt");
			writer.println("A" + " " + 6 + " "+ 1 + " " + 10 + " " + 1);
			writer.println("B" + " " + 7 + " "+ 2 + " " + 10 + " " + 2);
			writer.println("C" + " " + 8 + " "+ 3 + " " + 10 + " " + 3);
			writer.println("D" + " " + 9 + " "+ 4 + " " + 10 + " " + 4);
			writer.println("E" + " " + 10 + " "+ 5 + " " + 10 + " " + 5);
			writer.println("F" + " " + 11 + " "+ 6 + " " + 10 + " " + 6);
			writer.println("G" + " " + 12 + " "+ 7 + " " + 10 + " " + 7);
			writer.println("H" + " " + 13 + " "+ 8 + " " + 10 + " " + 8);
			writer.println("I" + " " + 14 + " "+ 9 + " " + 10 + " " + 9);
			writer.println("J" + " " + 15 + " "+ 10 + " " + 10 + " " + 10);
			
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving player stats.");
		}
    	
    	Game gameTest = new Game("FULLPLAYERTESTER", "Letter");
    	
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(out));
	    

    	gameTest.getLeaderboard();

    	
        System.setOut(stdout);
        String output = out.toString();
        Assert.assertTrue(output.contains("--Leaderboard--"));
        String[] splitOutput = output.split("\n");
        //highest player at top?
        Assert.assertTrue(splitOutput[2].contains("1.\tJ\t\t100.0%\t\t\t\t\t10\t\t\t10\t\t\t66.67%\r"));
        //blank space at bottom as no players to fill?
        Assert.assertTrue(splitOutput[splitOutput.length-3].contains("10.\tA\t\t10.0%\t\t\t\t\t10\t\t\t1\t\t\t16.67%\r"));
    }
  
    
    //there are blank spaces where there is no player to fill that position for top 10
    @Test
    public void halfPlayerTop10() {

    	try {
			PrintWriter writer = new PrintWriter("Players.txt");
			writer.println("A" + " " + 6 + " "+ 1 + " " + 10 + " " + 5);
			writer.println("B" + " " + 7 + " "+ 2 + " " + 10 + " " + 6);
			writer.println("C" + " " + 8 + " "+ 3 + " " + 10 + " " + 7);
			writer.println("D" + " " + 9 + " "+ 4 + " " + 10 + " " + 8);
			writer.println("E" + " " + 10 + " "+ 5 + " " + 10 + " " + 9);
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving player stats.");
		}
    	
    	Game gameTest = new Game("HALFPLAYERTESTER", "Letter");
    	
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(out));
	    

    	gameTest.getLeaderboard();

    	
        System.setOut(stdout);
        String output = out.toString();
        Assert.assertTrue(output.contains("--Leaderboard--"));
        String[] splitOutput = output.split("\n");
        //highest player at top?
        Assert.assertTrue(splitOutput[2].contains("1.\tE\t\t90.0%\t\t\t\t\t10\t\t\t9\t\t\t50.0%\r"));
        //lowest player at middle space? (before the blank space begins)
        Assert.assertTrue(splitOutput[(splitOutput.length-1)/2].contains("5.\tA\t\t50.0%\t\t\t\t\t10\t\t\t5\t\t\t16.67%\r"));
        //blank space at bottom as no players to fill?
        Assert.assertTrue(splitOutput[splitOutput.length-3].contains("10."));
    }
/*Scenario: no player stats have been stored
o Given no player stats have been stored
o When the player selects to see the top 10 players
o Then an error message is shown
     */
    @Test
    public void noPlayerTop10() {
    	
    	try {
			PrintWriter writer = new PrintWriter("Players.txt");
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving player stats.");
		}
    	Game gameTest = new Game("a", "Letter");
    	
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(out));
	      
    	gameTest.getLeaderboard();
    	
        System.setOut(stdout);
        String output = out.toString();
        Assert.assertTrue(output.contains("Apologies- there are no player stats to show"));

    }

    /*14. As a player I want to be able to get a hint for a letter, 
     * so I can solve the cryptogram
     */
    
    
  //There are still cryptogram values to be mapped
    @Test
    public void hintWhenMappableLetter() {
    	
    	Player playerBeforeHint =testerGame.getCurrentPlayer();
	      
    	String mapBeforeHint = testerGame.getPlayerGameMapping();
    	String enPh = testerGame.getCurrentCrypto().getPhrase();
    	testerGame.getHint();
    	String mapAfterHint = testerGame.getPlayerGameMapping();
    	
    	boolean found = false;
    	int indexOfHint = 0;


 	//loop to find what index the hint was inserted
    	while (!found && indexOfHint<(mapAfterHint.length())) {
    		if (mapAfterHint.charAt(indexOfHint)!=mapBeforeHint.charAt(indexOfHint)) {
    			found = true;
    			break;
    		} else {
    			indexOfHint++;
    		}
    	}

    	//test if hint was placed (found should be true)
    	Assert.assertTrue(found);
    	//test if the correct letter was placed at the hint index found
    	Assert.assertTrue(testerGame.getCurrentCrypto().getPlainLetter(enPh.charAt(indexOfHint)) == mapAfterHint.charAt(indexOfHint));
    
    	//test if player stats have not been changed
      	assertEquals(playerBeforeHint.getCryptogramCompleted(),testerGame.getCurrentPlayer().getCryptogramCompleted());
    	assertEquals(playerBeforeHint.getCorrectGuesses(),testerGame.getCurrentPlayer().getCorrectGuesses());
    	assertEquals(playerBeforeHint.getTotalGuesses(),testerGame.getCurrentPlayer().getTotalGuesses());
    	
    }
    
    @Test
    public void hintWhenMappableNum() {
    	
    	Player playerBeforeHint =testerGameNum.getCurrentPlayer();
  
	    
    	String mapBeforeHint = testerGameNum.getPlayerGameMapping();
    	
    	testerGameNum.getHint();
    	String mapAfterHint = testerGameNum.getPlayerGameMapping();
    	
    	boolean found = false;
    	int indexOfHint = 0;
    	
    	
    	while (!found && indexOfHint<(mapAfterHint.length())) {
    		if (mapAfterHint.charAt(indexOfHint)!=mapBeforeHint.charAt(indexOfHint)) {
    			found = true;
    			break;
    		} else {
    			indexOfHint++;
    		}
    	}


        Assert.assertTrue(found);
        
    	assertEquals(playerBeforeHint.getCryptogramCompleted(),testerGameNum.getCurrentPlayer().getCryptogramCompleted());
    	assertEquals(playerBeforeHint.getCorrectGuesses(),testerGameNum.getCurrentPlayer().getCorrectGuesses());
    	assertEquals(playerBeforeHint.getTotalGuesses(),testerGameNum.getCurrentPlayer().getTotalGuesses());
    	
        
        
        //test for number
    	
    	String[] mapAfterHintSplit = testerGameNum.getPlayerGameMapping().split(" ");
    	String[] enPhSplit = testerGameNum.getCurrentCrypto().getPhrase().split(" ");

        for (int i=0;i<mapAfterHintSplit.length;i++) {
        	if (enPhSplit[i]!="" &&
        			testerGameNum.getCurrentCrypto().getPlainLetter(Integer.parseInt(enPhSplit[i]))==mapAfterHint.charAt(indexOfHint)) {
        		return;
        	}
        }
        //if we reach here, then the hint has not been mapped in the correct place
        Assert.fail();
        

    }
    
    //Player has mapped all values but there can be replacements
    
    @Test
    public void hintWhenFullMapLetter() {
    	
    	Player playerBeforeHint =testerGame.getCurrentPlayer();
    	
    	
    	
    	String enPh = testerGame.getCurrentCrypto().getPhrase();

    	String mapBeforeHint;
    	String mapAfterHint;

		testerGame.setPlayerGameMapping(testerGame.getPlayerGameMapping().replace('?', 'z'));
		mapBeforeHint = testerGame.getPlayerGameMapping();
		testerGame.getHint();
		mapAfterHint = testerGame.getPlayerGameMapping();

    	boolean found = false;
    	int indexOfHint = 0;

    	
    	while (!found && indexOfHint<(mapAfterHint.length())) {
    		if (mapAfterHint.charAt(indexOfHint)!=mapBeforeHint.charAt(indexOfHint)) {
    			found = true;
    			break;
    		} else {
    			indexOfHint++;
    		}
    	}
    	
        Assert.assertTrue(found);
    	Assert.assertTrue(testerGame.getCurrentCrypto().getPlainLetter(enPh.charAt(indexOfHint)) 
    			== mapAfterHint.charAt(indexOfHint));

    	
      	assertEquals(playerBeforeHint.getCryptogramCompleted(),testerGame.getCurrentPlayer().getCryptogramCompleted());
    	assertEquals(playerBeforeHint.getCorrectGuesses(),testerGame.getCurrentPlayer().getCorrectGuesses());
    	assertEquals(playerBeforeHint.getTotalGuesses(),testerGame.getCurrentPlayer().getTotalGuesses());
    	
    }
    
    @Test
    public void hintWhenFullMapNum() {
    	
    	Player playerBeforeHint =testerGameNum.getCurrentPlayer();

    	
    	String mapBeforeHint;
    	String mapAfterHint;
    	
    	//this code is used to generate a full player number mapping - adding in ` when we have a double digit
    	String mapChar;
    	String fullPlayerMap = "";
    	String[] enPhSplit = testerGameNum.getCurrentCrypto().getPhrase().split(" ");
        for (int i=0;i<enPhSplit.length;i++) {
    		mapChar = "";
        	if (enPhSplit[i]!="") {
        		mapChar += "z";
	        	if(Integer.valueOf(enPhSplit[i]) > 9) {
	        		mapChar += "`";
	        	}
	        	mapChar += " ";
	        	fullPlayerMap+=mapChar;
        	}
        }
        
        testerGameNum.setPlayerGameMapping(fullPlayerMap);
        
		mapBeforeHint = testerGameNum.getPlayerGameMapping();
		testerGameNum.getHint();
		mapAfterHint = testerGameNum.getPlayerGameMapping();

    	boolean found = false;
    	int indexOfHint = 0;


    	while (!found && indexOfHint<(mapAfterHint.length())) {
    		if (mapAfterHint.charAt(indexOfHint)!=mapBeforeHint.charAt(indexOfHint)) {
    			found = true;
    			break;
    		} else {
    			indexOfHint++;
    		}
    	}
    	

        Assert.assertTrue(found);
        
     	assertEquals(playerBeforeHint.getCryptogramCompleted(),testerGameNum.getCurrentPlayer().getCryptogramCompleted());
    	assertEquals(playerBeforeHint.getCorrectGuesses(),testerGameNum.getCurrentPlayer().getCorrectGuesses());
    	assertEquals(playerBeforeHint.getTotalGuesses(),testerGameNum.getCurrentPlayer().getTotalGuesses());
    	
        
    	String[] mapAfterHintSplit = testerGameNum.getPlayerGameMapping().split(" ");
        for (int i=0;i<mapAfterHintSplit.length;i++) {
        	if (enPhSplit[i]!="" &&
        			testerGameNum.getCurrentCrypto().getPlainLetter(Integer.parseInt(enPhSplit[i]))==mapAfterHint.charAt(indexOfHint)) {
        		return;
        	}
        }
        
        Assert.fail();
        
 

    }

    @After
    public void rewritingFile() {
    	try {
			PrintWriter writer = new PrintWriter("Players.txt");
		
			for (String player:ogFile) {
				writer.println(player);
			}
		    writer.close();
		} catch (IOException e) {
		      System.out.println("An error occurred in saving player stats.");
		}
    }
        

    
    
    
    
    
    
}
	

    