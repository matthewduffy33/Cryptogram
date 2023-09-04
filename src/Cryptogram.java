import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public abstract class Cryptogram<E> {
	
	protected String phrase;
	protected String originalPhrase;
	protected Map cryptogramAlphabet;
	

	public Cryptogram(){
		phrase="";
	}

	public void setPhrase() {
		Random rand = new Random();
		Scanner reader;
		try {
			reader = new Scanner(new File("Quotes.txt"));
			for (int i=0; i<=rand.nextInt(15);i++){
				try {
					phrase = reader.nextLine();
				}catch(NoSuchElementException e){
					System.out.println("Unable to read whole phrases");
				}
			}
			reader.close();
		}catch (FileNotFoundException e) {
			System.out.println("Phrase file not found");
			System.out.println("Exiting...");
			System.exit(0);
		}
		originalPhrase=phrase;
		
	}
	
	
	public void setPhrase(String myPhrase, String encipheredPhrase) {
		originalPhrase = myPhrase;
		phrase = encipheredPhrase;
	}
	
	public String getPhrase() {
		return phrase;
	}

	public String getOriginal() {
		return originalPhrase;
	}
	
	public abstract Character getPlainLetter(E cryptoValue);
	
	//int getFrequencies(); 

}
