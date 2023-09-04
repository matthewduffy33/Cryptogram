import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		Game game = null;
		boolean choiceMade = false;
		boolean exiting =false;
		String cryptChoice = null;
		Scanner sca = new Scanner(System.in);
		String player = null;
		
		do{
			System.out.println("Enter your name: ");
			player = sca.nextLine();
			player = player.toUpperCase();
			if(!player.matches("^[A-Z0-9]*$")||player.equals("")) {
				System.out.println("Name must be alphanumeric and at least one character long.");
			}
		}while(!player.matches("^[a-zA-Z0-9]*$")||player.equals("")); 
		
		while (!exiting) {
			System.out.println(

					"Welcome to the game, "+ player + ". What would like to do?:"
					+ "\r\n NUMBER: Play a cryptogram that maps letters to numbers."
					+ "\r\n LETTER: Play a cryptogram that maps letters to letters."
					+ "\r\n STATS: See your cryptogram stats."
					+ "\r\n LOAD: Load your cryptogram progress from file."
					+ "\r\n LEADERBOARD: See the scores of the best players.."
					+ "\r\n RELOGIN: Login as a different name."
					+ "\r\n EXIT: Exit the software. \r\n"

			);
			cryptChoice = sca.nextLine();

			switch (cryptChoice.toUpperCase()) {
				case "NUMBER":
					game = new Game(player, "Number");
					choiceMade=true;
					break;
				case "LETTER":
					game = new Game(player, "Letter");
					choiceMade=true;
					break;
				case "STATISTICS","STATS":
					game = new Game(player);
					game.printPlayerStats();
					break;
				case "LOAD":
					game = new Game(player);
					if (game.loadGame(player)) {
						System.out.println("Game Loaded");
						choiceMade=true;
					}else {
						System.out.println("No games to load for you");
					
					}
					break;
				case "LEADERBOARD":
					game = new Game(player);
					game.getLeaderboard();
					break;
				case "RELOGIN":
					do{
						System.out.println("Enter your name: ");
						player = sca.nextLine();
						player = player.toUpperCase();
						if(!player.matches("^[a-zA-Z0-9]*$")||player.equals("")) {
							System.out.println("Name must be alphanumeric and at least one character long.");
						}
					}while(!player.matches("^[a-zA-Z0-9]*$")||player.equals("")); 
					break;
				case "EXIT":
					exiting=true;
					break;
				default:
					System.out.println("\r\nNot an option");
			}
			
			
			if(choiceMade) {
				game.playGame();
				choiceMade=false;
			}
		}
		
		
		sca.close();
		System.out.println("\r\nExiting...\r\n");
	}


}

