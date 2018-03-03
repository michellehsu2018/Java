package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Dictionary {
	public final static String DICTIONARY = "NewDictionary.txt";
	List<Word> wordList = new ArrayList<>();
	Map<String, Word> singleMap = new HashMap<>();
	Map<String, List<Word>> multiMap = new HashMap<>();

	public static void main(String[] args) {
		Dictionary d = new Dictionary();
		d.loadWordList();
		d.loadSingleMap();
		d.loadMultiMap();
		
		Scanner input = new Scanner(System.in);
		System.out.println("Enter search word");
		String searchWord = input.nextLine();
		
		System.out.println("------------WordList Search-----------");
		d.searchWordList(searchWord);
		System.out.println("*******************************");
		System.out.println("------------SingleMap Search-----------");
		d.searchSingleMap(searchWord);
		System.out.println("*******************************");
		System.out.println("------------MultiMap Search-----------");
		d.searchMultiMap(searchWord);
		System.out.println("*******************************");
		input.close();
	}
	
	/**loadWordList() reads the txt file. For each line, it invokes 
	 * getWord() method that returns a Word object. This object is then
	 * added to the arrayList wordList
	 */
	void loadWordList() {
		String wordString = null;
		try {
			Scanner input = new Scanner(new File(DICTIONARY));
			while (input.hasNextLine()) {
				wordString = input.nextLine();
				if (wordString.length() == 0) continue; //blank line
				wordList.add(getWord(wordString));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** getWord() takes a wordString and splits it on "(". The first
	 * element after split is the word, and rest are elements of its meaning. 
	 * So it uses first element to initialize 'word' of Word, and rest to 
	 * initialize 'meaning' of Word. As '(' may occur anywhere in the 
	 * 'meaning', the split string is put back together by putting
	 * '(' in front of each piece.  
	 * @param wordString
	 * @return
	 */
	Word getWord(String wordString) {
		String[] splits = wordString.split("\\(");  //split on (
		String word = null;
		StringBuilder wordMeaningString = new StringBuilder();
		if (splits[0].length() >0) 
			word = splits[0].trim();  //get the first string as it is the word
		for (int i = 1; i < splits.length; i++) {
			wordMeaningString.append("(" + splits[i]); //put back rest of the string together
		}
		return new Word(word, wordMeaningString.toString());
	}

	/** loadSingleMap() takes each word from
	 * wordList and loads it into singleMap with key being
	 * the Word's word in lowercase, and its value being the whole 
	 * Word object.
	 */
	void loadSingleMap() {
		//write your code here
			for(int i =0; i< wordList.size(); i++){
				Word w = new Word(wordList.get(i).word.toLowerCase(), wordList.get(i).meaning);
				singleMap.put(wordList.get(i).word.toLowerCase(), w);	
			}	
	}

	/**loadMultiMap() takes each word from wordList and loads it 
	 * into multiMap with key being the Word's word in lowercase, and 
	 * its value being a list of all its meaning found in the 
	 * dictionary
	 */
	void loadMultiMap() {
		//write your code here
		for(int i =0; i< wordList.size(); i++){
			if(multiMap.containsKey(wordList.get(i).word.toLowerCase())) {						
					multiMap.get(wordList.get(i).word.toLowerCase()).add(wordList.get(i));
			}else {
				List<Word> current = new ArrayList<>();
				//whenever you find a new word, create a new arraylist for the word
				current.add(wordList.get(i));//add the word to the arraylist created for the word
				multiMap.put(wordList.get(i).word.toLowerCase(), current);//put the arraylist to the map
			}
		}			
	}
	
	/** searchWordList() takes a searchWord String and and searches for it in wordList.
	 * If found, it prints all its meanings. Else it prints 'Sorry! <word> not found!'
	 * @param searchWord
	 */
	void searchWordList(String searchWord) {
		//write your code here
		for(int i = 0; i < wordList.size(); i++) {
			if(wordList.get(i).word.toLowerCase().equals(searchWord)) {
				System.out.println(wordList.get(i).word+": "+wordList.get(i).meaning);
			}
		}
	}
	/** searchSingleMap() takes a searchWord String and searches for it in singleMap.
	 * If found, it prints its meaning. Else it prints 'Sorry! <word> not found!'. 
	 * Note that key is lowercase in the map.
	 * @param searchWord
	 */
	void searchSingleMap(String searchWord) {
		//write your code here
		Word find = null;
			if(singleMap.containsKey(searchWord.toLowerCase())) {
				find = singleMap.get(searchWord.toLowerCase());
				System.out.println(find.word+": "+find.meaning);
			}else {
				System.out.println("Sorry! "+searchWord+" not found!");
			}
	}
	/** searchMultiMap() takes a searchWord String and searches for it in multiMap. 
	 * If found, it prints all its meanings. Else it prints 'Sorry! <word> not found!'
	 * Note that key is lower case in the map.
	 * @param searchWord
	 */
	void searchMultiMap(String searchWord) {
		//write your code here
		
		if(multiMap.containsKey(searchWord.toLowerCase())) {
			List<Word> findlist = new ArrayList<>();
			findlist = multiMap.get(searchWord.toLowerCase());
			//multiMap send back the arrayList which contains the word to findlist
			for(int i = 0; i< findlist.size(); i++) {
				System.out.println(findlist.get(i).word+": "+findlist.get(i).meaning);
			}
		}else {
			System.out.println("Sorry! "+searchWord+" not found!");
		}
//		Boolean flag = false;
//		for(Map.Entry<String, List<Word>> word: multiMap.entrySet()) {
//			if(word.getKey().equals(searchWord.toLowerCase())) {
//				for(int i = 0; i< word.getValue().size();i++) {			
//				 System.out.println(word.getValue().get(i).word+": "+word.getValue().get(i).meaning);
//				}
//				flag = true;
//			}
//		}
//		if(!flag) {
//			System.out.println("Sorry! "+searchWord+" not found!");
//		}
		
	}
}
