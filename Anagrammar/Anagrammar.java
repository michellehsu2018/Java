package lab2;

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
	
	//DO NOT change main method
	public static void main(String[] args) {
		Anagrammar ag = new Anagrammar();
		ag.getInputs();
		ag.loadWords();
		ag.findAnagrams();
		ag.printResult();
	}
	
	 void loadWords() {
		Scanner fileInput = null;
		StringBuilder fileContent = new StringBuilder();
		try {
			fileInput = new Scanner (new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileInput.hasNextLine()) fileContent.append(fileInput.nextLine() + "\n");
		words = fileContent.toString().split("\n");
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
	void findAnagrams() {
		//write your code here
		int count =0;
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
					foundword.append(count+". "+words[i]+"\n");
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
		
		if(isInDictionary) {
			if(hasAnagrams) {
				if(count==0) {
					System.out.println("Sorry! No aragram found!");
				}
				System.out.println(count+" anagram(s) found");
			}else {
				System.out.println("Sorry! No aragram found!");
			}
		}else {
			if(hasAnagrams) {
				System.out.println(clue+" not found in the dictionary");
				System.out.println(count+" anagram(s) found");
			}else {
				System.out.println(clue+" not found in the dictionary");
				System.out.println("Sorry! No aragram found!");
			}
		}
		anagramArray = foundword.toString().split("\n");

	}

	void printResult() {
		//write your code here
		for(String s:anagramArray) {
			System.out.println(s);
		}

	
		
	}
}
