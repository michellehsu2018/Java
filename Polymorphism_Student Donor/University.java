package lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class University {
	String[] rosterStrings;
	public Student[] studentObjects;
	
	public void loadRosterStrings() {
		Scanner fileContent = null;
		StringBuilder rosterData = new StringBuilder();
		try {
			fileContent = new Scanner (new File("Roster.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileContent.hasNextLine()) {
			rosterData.append(fileContent.nextLine() + "\n");
		}
		rosterStrings = rosterData.toString().split("\n"); 
	}
	
	/**loadStudentObjects() method takes rosterStrings data and creates a new Student object
	 * from data in each string. It uses 'Section' data to decide which type of 
	 * Student to create. For example, if Section is 'A', then it creates StudentA object. 
	 * It initializes instance variables of Student object from rosterString data after splitting
	 * each String on ',' as delimiter. 
	 * Hint: Use charAt(index) method to get Section name
	 */
	public void loadStudentObjects(){
		//write your code here
		studentObjects = new Student[rosterStrings.length];
		String[] temp  =  new String [7];
		for(int i=0; i<rosterStrings.length;i++){
			temp =rosterStrings[i].toString().split(",");
			if(temp[0].equals("A")) {
				studentObjects[i] = new StudentA(temp[0].charAt(0),temp[1],temp[2],temp[3],temp[4],Integer.parseInt(temp[5]), Double.parseDouble(temp[6]));
				((StudentA)studentObjects[i]).donate();
			}else if(temp[0].equals("B")) {
				studentObjects[i]= new StudentB(temp[0].charAt(0),temp[1],temp[2],temp[3],temp[4],Integer.parseInt(temp[5]), Double.parseDouble(temp[6]));
				((StudentB)studentObjects[i]).donate();
			}else {
				studentObjects[i]= new StudentC(temp[0].charAt(0),temp[1],temp[2],temp[3],temp[4], Integer.parseInt(temp[5]), Double.parseDouble(temp[6]));
				((StudentC)studentObjects[i]).donate();
				((StudentC)studentObjects[i]).serve();
			}
		}
	}
}
