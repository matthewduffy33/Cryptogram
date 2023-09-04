import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Map.Entry;

public class NumberCryptogram extends Cryptogram<Integer> {
	

	private Map<Character,Integer> cryptogramAlphabet;
	
	
	public NumberCryptogram() {
		super();
		setPhrase(); //get a phrase from our phrase bank
		phrase = phrase.replaceAll(" ", "   ");
		
		cryptogramAlphabet = new HashMap<Character,Integer>(); //init the cryptogram alphabet to a letter one
		cryptogramAlphabet = makeCryptogramAlpha(cryptogramAlphabet);
		
		for (int i=0; i<phrase.length(); i++) {
			char itChar = phrase.charAt(i);
			if (itChar >= 'A' && itChar<='Z') {
				phrase=phrase.substring(0,i)+cryptogramAlphabet.get(itChar)+" "+phrase.substring(i+1);
			}
		}
	}
	
	
	public NumberCryptogram(String ph, String enPh) {
		super();
		String[] splitCrypto = enPh.split(" ");
		setPhrase(ph, enPh);  //set the phrase to the one passed in from the file
		
		cryptogramAlphabet = new HashMap<Character,Integer>(); //init the cryptogram alphabet to a letter one
		int c = 0;
		
		for(int i = 0; i < ph.length(); i++) {
			if(ph.charAt(i) != (' ')) {
				cryptogramAlphabet.put(ph.charAt(i), Integer.parseInt(splitCrypto[c]));
				
			}
			else {
				c = c + 2;
			}
			c++;
		}

	}
	
	
	//method to create the number cryptogram. takes in a cryptogram, assumed to be empty, and returns
	//the cryptogram alphabet 
	public Map<Character,Integer> makeCryptogramAlpha(Map<Character,Integer> cryptomap) {
		Random rand = new Random(); //random generator
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //string storing all alpha characters
		String encryptAlpha = alpha; //copy of the alphabet 
		int encrypt;
		
		for (int i = 0; i<alpha.length();i++) {
			if(i==24 && encryptAlpha.charAt(1)=='z') {                   //stops the very rare case of an infinite loop of z trying to assign z to itself
				cryptomap.put(encryptAlpha.charAt(2),24);
				cryptomap.put(encryptAlpha.charAt(1),25);
			}else {
				do {
					encrypt = rand.nextInt(encryptAlpha.length());	 //get some random character from the encryptAlpha that is not our current iteration character
				} while (alpha.charAt(i) == encryptAlpha.charAt(encrypt) ); 
				cryptomap.put(encryptAlpha.charAt(encrypt),i+1);    //map the encrypted value to the letter
				encryptAlpha = encryptAlpha.replace(Character.toString(encryptAlpha.charAt(encrypt)), ""); 		//remove the encrypted value from our encryptAlpha so the value is not mapped again
			}
		}
		

		
		return cryptomap;

	}
	
	public Character getPlainLetter(Integer cryptoNumber) {
		//gets what the decrypted letter is for a given number on the crypto alphabet
		for (Entry<Character, Integer> entry : cryptogramAlphabet.entrySet()) {
			if (entry.getValue().equals(cryptoNumber)) {
				return entry.getKey();
			}
		} 
		return null;
	}
	
	void printScramble() {
		System.out.println(Collections.singletonList(cryptogramAlphabet));
	}
	
	public Map<Character, Integer> getCryptogramAlphabet() {
		return cryptogramAlphabet;
	}

}

