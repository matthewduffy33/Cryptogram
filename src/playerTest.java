

import org.junit.*;

public class playerTest {
	
	private Player testPlayer;
	
    @Before
    public void setUp() { 
    	testPlayer = new Player("a");
    }

	
    @Test 
    public void getUsername() {
		Assert.assertEquals(testPlayer.getUsername(), "a");
		
	}
	
    @Test 
    public void upTotalGuess() {
    	int totalBefore = testPlayer.getTotalGuesses();
    	testPlayer.incrementTotalGuesses();
		Assert.assertEquals(totalBefore+1, testPlayer.getTotalGuesses());
		
	}
    
    @Test 
    public void upCorrectGuess() {
    	int correctBefore = testPlayer.getCorrectGuesses();
    	testPlayer.incrementCorrectGuesses();
		Assert.assertEquals(correctBefore+1, testPlayer.getCorrectGuesses());
		
	}
    
    @Test 
    public void upComplete() {
    	int completeBefore = testPlayer.getCryptogramCompleted();
    	testPlayer.incrementCompleteGames();
		Assert.assertEquals(completeBefore+1, testPlayer.getCryptogramCompleted());
		
	}

    @Test 
    public void upPlayed() {
    	int playedBefore = testPlayer.getCryptogramPlayed();
    	testPlayer.incrementGames();
		Assert.assertEquals(playedBefore+1, testPlayer.getCryptogramPlayed());
		
	}
    
    
    
    @Test 
    public void upAccuracyWhenNoGuess() {
    	testPlayer.updateAccuracy();
		Assert.assertTrue(0 == testPlayer.getAccuracy());
		
	}




}
