import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class LetterCryptogram extends Cryptogram<Character> {

	private Map<Character,Character> cryptogramAlphabet;//left-hand side character is the og value; right-hand side character is the enciphered value
	
	public LetterCryptogram() {																																																																																																																																								
		super();
		setPhrase(); //get a phrase from our phrase bank
		

		cryptogramAlphabet = new HashMap<Character,Character>(); //init the cryptogram alphabet to a letter one
		cryptogramAlphabet = makeCryptogramAlpha(getCryptogramAlphabet());

		for (int i=0; i<phrase.length(); i++) {
			char itChar = phrase.charAt(i);
			if (itChar >= 'A' && itChar<='Z') {
				phrase=phrase.substring(0,i)+getCryptogramAlphabet().get(itChar)+phrase.substring(i+1);

			}
		}
	}
	
	
	public LetterCryptogram(String ph, String enPh) {
		super();
		setPhrase(ph, enPh);  //set the phrase to a specific phrase word
		
		cryptogramAlphabet = new HashMap<Character,Character>(); //init the cryptogram alphabet to a letter one
		
		for(int i = 0; i < ph.length(); i++) {
			if(ph.charAt(i) != (' ')) {
				cryptogramAlphabet.put(ph.charAt(i), enPh.charAt(i));
			}
		}

	}
	
	
	//method to create the letter cryptogram. takes in a cryptogram, assumed to be empty, and returns
	//the cryptogram alphabet 
	public Map<Character,Character> makeCryptogramAlpha(Map<Character,Character> cryptomap) {
		Random rand = new Random(); //random generator
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //string storing all alpha characters
		String encryptAlpha = alpha; //copy of the alphabet 
		int encrypt;
		
		for (int i = 0; i<alpha.length();i++) {
			if(i==24 && encryptAlpha.charAt(1)=='z') {                   //stops the very rare case of an infinite loop of z trying to assign z to itself
				cryptomap.put(alpha.charAt(24),encryptAlpha.charAt(1));
				cryptomap.put(alpha.charAt(25),encryptAlpha.charAt(0));
			}else {
				do {
					encrypt = rand.nextInt(encryptAlpha.length());	 //get some random character from the encryptAlpha that is not our current iteration character
				} while (alpha.charAt(i) == encryptAlpha.charAt(encrypt) ); 
				cryptomap.put(alpha.charAt(i),encryptAlpha.charAt(encrypt));    //map the encrypted value to the letter
				encryptAlpha = encryptAlpha.replace(Character.toString(encryptAlpha.charAt(encrypt)), ""); 		//remove the encrypted value from our encryptAlpha so the value is not mapped again
			}
		}
		
		return cryptomap;

	}
	
	@Override
	public Character getPlainLetter(Character cryptoLetter) {
		//gets what the decrypted letter is for a given letter on the crypto alphabet
		for (Entry<Character, Character> entry : cryptogramAlphabet.entrySet()) {
			if (entry.getValue().equals(cryptoLetter)) {
				return entry.getKey();
			}
		} 
		return null;
	}
	
	
	
	void printScramble() {
		System.out.println(Collections.singletonList(getCryptogramAlphabet()));
	}



	public Map<Character,Character> getCryptogramAlphabet() {
		return cryptogramAlphabet;
	}


}
