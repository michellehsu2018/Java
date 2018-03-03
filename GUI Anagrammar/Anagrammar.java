package lab5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Anagrammar {
	String[] words;		//stores all words from the dictionary
	String filename, clue;	
	public boolean isInDictionary; //true if the clue word exists in dictionary
	public boolean hasAnagrams;	//true if the clue word has anagrams
	public String[] anagramArray;	//stores all anagrams of clue-word, if found
	int count;
	//DO NOT change main method
	public static void main(String[] args) {
		Anagrammar ag = new Anagrammar();
		ag.getInputs();
		ag.loadWords();
	}
	
	/**loadWords method reads the file and loads all words 
	 * into the words array */
	public void loadWords() {
		Scanner fileInput = null;
		StringBuilder fileContent = new StringBuilder();
		try {
			fileInput = new Scanner (new File("dictionary.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileInput.hasNextLine()) fileContent.append(fileInput.nextLine() + "\n");
		words = fileContent.toString().split("\n");
		//System.out.println(words.length);
	}
	
	/** getInputs method takes two inputs from the user to 
	 * initialize the member variables filename and clue */
	void getInputs() {
		//write your code here
		Scanner input = new Scanner(System.in);
		Scanner search = new Scanner(System.in);
		System.out.println("Enter file name");
		filename = input.nextLine(); //next() is everything till whitespace
		System.out.println("Enter the word");
		clue = search.nextLine(); //nextLine() is everything till next line(\n)
		input.close();
		search.close();
	}
	
	
	/** findAnagrams method traverses the words array and looks 
	 * for anagrams of the clue. While doing so, if the clue-word itself is found in  
	 * words array, it sets isInDictionary to true.
	 * If it finds any anagram of the clue, it sets hasAnagram to true. 
	 * It loads the anagrams into anagramArray */
	public void findAnagrams(String s) {
		//write your code here
		count =0;
		clue = s;
		isInDictionary = false;
		hasAnagrams = false;
		StringBuilder foundword = new StringBuilder();
		char[] charArray = clue.toCharArray();//convert string into char array
		Arrays.sort(charArray); // sort the char array into alphabetical order
		for(int i = 0; i <words.length; i++) {
			String st = words[i].toString();  //convert each string array into string
			char[] ch = st.toCharArray();
			Arrays.sort(ch);
			if(Arrays.equals(ch, charArray)) { //compare the elements in arrays==>same order and same elements
				count++;	
				hasAnagrams=true;
				if(clue.equals(words[i])) {
					count--;	
				}else {
					foundword.append(words[i]+"\n");
				}	
			}
		
		}
		for(int i = 0; i <words.length; i++) {
			if(clue.equals(words[i])) {		
				isInDictionary=true;
			}		
		}
		if(count==0) {
			if(isInDictionary) {
				hasAnagrams=false;
			}
		}
		anagramArray = foundword.toString().split("\n");
	}		
	
}
