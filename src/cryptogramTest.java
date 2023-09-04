
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Map.*;

import org.junit.*;

public class cryptogramTest {
	 Game testerGame;
	Game testerGameNum;
	private InputStream stdin = System.in;
    private PrintStream stdout = System.out;
    private ArrayList<String> ogFile;
	     
	     /**
	     * Sets up the test fixture
	     * Called before every test case method.
	     */
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
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
	    	testerGame = new Game("a", "Letter");
	    	testerGameNum = new Game("a", "Number");
	    }
	    

	    
	    @Test 
	    public void allLettersCrypto() {
		    /**
		     * Scenario: Player requests letters cryptogram
		     */
	    	
	    	
	    	LetterCryptogram testerLetterCrypto = new LetterCryptogram();
	    	assertNotNull(testerLetterCrypto.getCryptogramAlphabet()); 

	    	String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    	
	    	//testing every letter is mapped
	    	for (int i = 0; i < abc.length(); i++) {
	    		 assertTrue(testerLetterCrypto.getCryptogramAlphabet().containsKey(abc.charAt(i)));
	    	}
	    }
	    
	    
	    @Test 
	    public void uniqueLettersCrypto() {
	    	//testing every value is unique
	    	LetterCryptogram testerLetterCrypto = new LetterCryptogram();
	    	assertNotNull(testerLetterCrypto.getCryptogramAlphabet()); 

	    	String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    	
	    	String valuesMapped = "";
			for (Entry<Character, Character> entry : testerLetterCrypto.getCryptogramAlphabet().entrySet()) {
				valuesMapped += entry.getValue();
			}
			char[] sortedMapValues = valuesMapped.toCharArray();
			Arrays.sort(sortedMapValues);
			assertArrayEquals(abc.toCharArray(), sortedMapValues);
			
	    }
	    	
	    
	    
	    @Test 
	    public void allNumbersCrypto() {
	    	/**
		     *
	    	
	    	Scenario: Player requests numbers cryptogram
	    	*/
	    	
	    	NumberCryptogram testerNumCrypto = new NumberCryptogram();
	    	assertNotNull(testerNumCrypto.getCryptogramAlphabet()); 
	    	String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    	
	    	//testing every letter is mapped
	    	for (int i = 0; i < abc.length(); i++) {
	    		 assertTrue(testerNumCrypto.getCryptogramAlphabet().containsKey(abc.charAt(i)));
	    	}
	    }
	    	
	    @Test 
	    public void uniqueNumbersCrypto() {
	    	NumberCryptogram testerNumCrypto = new NumberCryptogram();
	    	//testing every value is unique
	    	int numValuesMapped = 0;
			for (Entry<Character, Integer> entry : testerNumCrypto.getCryptogramAlphabet().entrySet()) {
				numValuesMapped +=entry.getValue();
			}
	    	assertTrue(numValuesMapped == 351); //Triangular Number of 26 
	    }
	    
	    
	    
	    public void noFile() {
	    	/**
		     *
	    	Scenario: Player requests a cryptogram but no phrases file exists
	    	*/
		       ByteArrayOutputStream out = new ByteArrayOutputStream();
		       System.setOut(new PrintStream(out));
		       
	    		NumberCryptogram testerNumCrypto = new NumberCryptogram("","");
	    		LetterCryptogram testerLetterCrypto = new LetterCryptogram("","");
	    		
		        System.setOut(stdout);
		        
		        String output = out.toString();
		        Assert.assertTrue(output.contains("File not found\r\nFile not found\r\n"));

	    		return;
	    }


	    @Test 
	    public void undoMapping() {
	    	
	    	/**
		    Scenario: player wants to undo a mapped letter
		    */
	    	
	    	LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
	    	
	    	char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
	    	
	    	ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\nA\nUNDO\nA\nQUIT\n").getBytes());
	    	System.setIn(in);
	    	
	       ByteArrayOutputStream out = new ByteArrayOutputStream();
	       System.setOut(new PrintStream(out));
	        
	    	testerGame.playGame();
	    	
	        System.setIn(stdin);
	        System.setOut(stdout);
	        
	        String output = out.toString();
	        Assert.assertFalse(output.contains("The letter, A, is not in your mapping"));
	        
	    }

	    
	    @Test 
	    public void undoNotMapped() {
	    	
	    	/**
		     *
		    Scenario: player selects a letter in the cryptogram which they have not mapped
		    */
	      LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
	      char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
	      ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\nA\nUNDO\nB\nQUIT\n").getBytes());
	      System.setIn(in);
	     	
	      ByteArrayOutputStream out = new ByteArrayOutputStream();
	      System.setOut(new PrintStream(out));
	        
	    	testerGame.playGame();
	    	
	        System.setIn(stdin);
	        System.setOut(stdout);
	        
	        String output = out.toString();
	        Assert.assertTrue(output.contains("The letter, B, is not in your mapping"));
	    	
	    }
	    


	    @Test 
	    public void enteringLetters() {
	    	
	    	/**
		     *
			Scenario: player enters a letter
			*/
	    	
	    	LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
	    	
	    	char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
	    	
	    	ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\nA\nQUIT\n").getBytes());
	    	System.setIn(in);
	    	
	       ByteArrayOutputStream out = new ByteArrayOutputStream();
	       System.setOut(new PrintStream(out));
	        
	    	testerGame.playGame();
	    	
	        System.setIn(stdin);
	        System.setOut(stdout);
	        
	        String output = out.toString();
	        Assert.assertFalse(output.contains("Error:"));
	        Assert.assertTrue(testerGame.getCurrentPlayer().getTotalGuesses() != 0);
	    }
	    
	    @Test 
	    public void enteringLettersNumberCrypto() {
	    	
	    	/**
		     *
			Scenario: player enters a letter
			*/
	    	Game testerGameNumber = new Game("tester", "Number");
	    	NumberCryptogram testerLetterCrypto = (NumberCryptogram) testerGameNumber.getCurrentCrypto();
	    	
	    	int firstMapLetter = Integer.parseInt(testerLetterCrypto.getPhrase().split(" ")[0]);
	    	
	    	ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\nA\nQUIT\n").getBytes());
	    	System.setIn(in);
	    	
	       ByteArrayOutputStream out = new ByteArrayOutputStream();
	       System.setOut(new PrintStream(out));
	        
	    	testerGameNumber.playGame();
	    	Players ps =new Players();
	    	Player p = ps.findPlayers("tester");
	    	
	        System.setIn(stdin);
	        System.setOut(stdout);
	        
	        String output = out.toString();
	        Assert.assertFalse(output.contains("Error:"));
	        Assert.assertTrue(p.getTotalGuesses() != 0);
	    }
	    
	    	
	    @Test 
	    public void enteringAlreadyMappedValue() {
	    	/**
		     *
			Scenario: player selects a cryptogram value which has already been mapped
	    	*/
	    	LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
	    	char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
	    	
	    	ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\nA\nENTER\n"+firstMapLetter+"\nN\nQUIT\n").getBytes());
	    	System.setIn(in);
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	       System.setOut(new PrintStream(out));
	        
    	    testerGame.playGame();
	    	
	        System.setIn(stdin);
	        System.setOut(stdout);
	        
	        String output = out.toString();
	        Assert.assertTrue(output.contains("You already have a mapping for this value. Do you want to replace it? (Answer with Yes):"));

	    }
	    
	    
	    @Test 
	    public void enteringAlreadyMappedValueNumber() {
	    	/**
		     *
			Scenario: player selects a cryptogram value which has already been mapped
	    	*/
	    	NumberCryptogram testerLetterCrypto = (NumberCryptogram) testerGameNum.getCurrentCrypto();
	    	
	    	int firstMapLetter = Integer.parseInt(testerLetterCrypto.getPhrase().split(" ")[0]);
	    	
	    	ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\nA\nENTER\n"+firstMapLetter+"\nN\nQUIT\n").getBytes());
	    	System.setIn(in);
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	       System.setOut(new PrintStream(out));
	        
    	    testerGameNum.playGame();
	    	
	        System.setIn(stdin);
	        System.setOut(stdout);
	        
	        String output = out.toString();
	        Assert.assertTrue(output.contains("You already have a mapping for this value. Do you want to replace it? (Answer with Yes):"));

	    }
	    
	    
	    @Test 
	    public void enteringAlreadyMappedLetter() {
	    	/**
		     *Scenario: player selects a plain letter which they have already mapped
	    	*/
	        
	    	LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
	    	char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
	        char secondMapLetter = testerLetterCrypto.getPhrase().charAt(1);
	        
	        ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\nA\nENTER\n"+secondMapLetter+"\nA\nQUIT\n").getBytes());
	    	System.setIn(in);
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	        System.setOut(new PrintStream(out));
	        
    	    testerGame.playGame();
	    	
	        System.setIn(stdin);
	        System.setOut(stdout);
	        
	        String output = out.toString();
	        Assert.assertTrue(output.contains("Error: A is already entered into the cryptogram, please try again"));
	    }
	    	
	    
	    @Test 
	    public void enteringAlreadyMappedNumber() {
	    	/**
		     *Scenario: player selects a plain letter which they have already mapped
	    	*/
	        
	    	NumberCryptogram testerLetterCrypto = (NumberCryptogram) testerGameNum.getCurrentCrypto();
	    	
	    	int firstMapLetter = Integer.parseInt(testerLetterCrypto.getPhrase().split(" ")[0]);
	    	int secondMapLetter = Integer.parseInt(testerLetterCrypto.getPhrase().split(" ")[1]);
	        
	        ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\nA\nENTER\n"+secondMapLetter+"\nA\nQUIT\n").getBytes());
	    	System.setIn(in);
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	        System.setOut(new PrintStream(out));
	        
    	    testerGameNum.playGame();
	    	
	        System.setIn(stdin);
	        System.setOut(stdout);
	        
	        String output = out.toString();
	        Assert.assertTrue(output.contains("Error: A is already entered into the cryptogram, please try again"));
	    }
	    
	    
	    @Test 
	    public void finishingLetter() {
				    	/**
			Scenario: player enters the last value to be mapped and successfully completes the
			cryptogram
			o Given a cryptogram has been generated and is being played
			o When the player enters the last value to be mapped and their mapping is
			correct
			o Then a success message is displayed, their stats are updated and the game is
			finished
			*/
	    	LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
	    	char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
	        String answer = testerLetterCrypto.getOriginal();
	        char answerFirstChar = answer.charAt(0);
	        testerGame.setPlayerGameMapping(answer.replace(answerFirstChar,'?'));
	       	
	        ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\n" + answerFirstChar +"\nQUIT\n").getBytes());
	    	System.setIn(in);
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		       System.setOut(new PrintStream(out));
	        
		        int cryptoComplete = testerGame.getCurrentPlayer().getCryptogramCompleted();
		   	    testerGame.playGame();
		    	
		        System.setIn(stdin);
		        System.setOut(stdout);
		        
		      String output = out.toString();
		      Assert.assertTrue(output.contains("Congratulations the cryptogram has been complete"));
		      Assert.assertEquals(testerGame.getCurrentPlayer().getCryptogramCompleted() , cryptoComplete+1);
	    }
	    
	   


	    @Test 
	    public void failingFinishingLetter() {
				/**
				Scenario: player enters the last value to be mapped and unsuccessfully completes
				the cryptogram
				o Given a cryptogram has been generated and is being played
				o When the player enters the last value to be mapped and their mapping is
				incorrect
				o Then a fail message is displayed, the player stats are updated, and the game
				continues
				*/ 

	    	   LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
	    	   char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
	          String answer = testerLetterCrypto.getOriginal();
	           char answerFirstChar = answer.charAt(0);
		        testerGame.setPlayerGameMapping(answer.replace(answerFirstChar,'?'));

		        
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
			        
			        String output = out.toString();
			        Assert.assertTrue(output.contains("Sorry - Cryptogram solved incorrectly"));
	    }
	    	
	    
	    @Test 
	    public void failingFinishingNumber() {
				/**
				Scenario: player enters the last value to be mapped and unsuccessfully completes
				the cryptogram
				o Given a cryptogram has been generated and is being played
				o When the player enters the last value to be mapped and their mapping is
				incorrect
				o Then a fail message is displayed, the player stats are updated, and the game
				continues
				*/ 

	    	NumberCryptogram testerLetterCrypto = (NumberCryptogram) testerGameNum.getCurrentCrypto();
	    	
	    	int firstMapLetter = Integer.parseInt(testerLetterCrypto.getPhrase().split(" ")[0]);
	          String answer = testerLetterCrypto.getOriginal();
	           
	           if (firstMapLetter>=10) {
	        	   testerGameNum.setPlayerGameMapping("?? " + answer.substring(2));
	           }else {
	        	   testerGameNum.setPlayerGameMapping("? " + answer.substring(1));
	           }
		        

		        
		        char notInAns = 'A';
		        
		        while(answer.contains(notInAns + "")) {
		        	notInAns++;
		        }
		        
		        
		        ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+firstMapLetter+"\n" + notInAns +"\nQUIT\n").getBytes());System.setIn(in);
		    	
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			       System.setOut(new PrintStream(out));
		        
			   	    testerGameNum.playGame();
			    	
			        System.setIn(stdin);
			        System.setOut(stdout);
			        
			        String output = out.toString();
			        Assert.assertTrue(output.contains("Sorry - Cryptogram solved incorrectly"));
	    }
	    
	    
	    @Test 
	    public void notValueInLetter() {
							    	/**
						Scenario: player enters a cryptogram value which is not used in the cryptogram
								    */
	    			LetterCryptogram testerLetterCrypto = (LetterCryptogram) testerGame.getCurrentCrypto();
	    			char firstMapLetter = testerLetterCrypto.getPhrase().charAt(0);
			        char notInMap = 'A';
			        
			        while(testerGame.getCurrentCrypto().getPhrase().contains(notInMap + "")) {
			        	notInMap++;
			        }
			        ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+notInMap+"\nENTER\n"+firstMapLetter+"\nA\nQUIT\n").getBytes());
			    	System.setIn(in);
			    	
			    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			       System.setOut(new PrintStream(out));
			        
		    	    testerGame.playGame();
			    	
			        System.setIn(stdin);
			        System.setOut(stdout);
			        
			        String output = out.toString();
			        Assert.assertTrue(output.contains("Value not in cryptogram: try again"));

	    }
	    
	    @Test 
	    public void notValueInNumber() {
							    	/**
						Scenario: player enters a cryptogram value which is not used in the cryptogram
								    */
	    			NumberCryptogram testerLetterCrypto = (NumberCryptogram) testerGameNum.getCurrentCrypto();
	    	
	    			int firstMapLetter = Integer.parseInt(testerLetterCrypto.getPhrase().split(" ")[0]);
			        int notInMap = 0;
			        
			        while(testerGame.getCurrentCrypto().getPhrase().contains(notInMap + "")) {
			        	notInMap++;
			        }
			        ByteArrayInputStream in = new ByteArrayInputStream(("ENTER\n"+ notInMap +"\nENTER\n"+firstMapLetter+"\nA\nQUIT\n").getBytes());
			    	System.setIn(in);
			    	
			    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			       System.setOut(new PrintStream(out));
			        
		    	    testerGameNum.playGame();
			    	
			        System.setIn(stdin);
			        System.setOut(stdout);
			        
			        String output = out.toString();
			        Assert.assertTrue(output.contains("Value not in cryptogram: try again"));

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
