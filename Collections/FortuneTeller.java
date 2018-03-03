package lab6;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class FortuneTeller {
	List<Student> students = new ArrayList<>();
	List<Company> companies = new ArrayList<>();
	static final String FILE_NAME = "Fortunes.csv";
	
	public static void main(String[] args) {
		FortuneTeller fortuneTeller = new FortuneTeller();
		fortuneTeller.loadStudentsList();
		fortuneTeller.loadCompaniesList();
		fortuneTeller.printReport();
	}
	
	void printReport() {
	
		Collections.sort(companies);
		System.out.println("*** No. of students hired by Fortune10 Best Employers ***"); 
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Rank. Company\t\t\t\tHired Count");
		System.out.println("--------------------------------------------------------------------");
		for (Company c : companies) {
			System.out.printf("%3d. %-30s: %10d%n", c.rank, c.name, c.hiredCount);
		}
		System.out.println("--------------------------------------------------------------------");
		System.out.printf("Total%43d%n", Company.overallHiredCount);
		System.out.println("====================================================================");

/** Write the appropriate Collections.sort() statement here to sort companies on hiredCount*/
		
		Collections.sort(companies, new CompanyHiredCountComparator());
		System.out.println("*** Fortune10 Best Employers by Hired Count ***"); 
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Rank. Company\t\t\t\tHired Count");
		System.out.println("--------------------------------------------------------------------");
		for (Company c : companies) {
			System.out.printf("%3d. %-30s: %10d%n", c.rank, c.name, c.hiredCount);
		}
		System.out.println("--------------------------------------------------------------------");
		System.out.printf("Total%43d%n", Company.overallHiredCount);
		System.out.println("====================================================================");
		System.out.println("*** Students and Employers ***");
		System.out.println("--------------------------------------------------------------------");
		
/** Write the appropriate Collections.sort() statement here to sort students on AndrewID*/
		Collections.sort(students);
		int i = 1;
		System.out.printf("#.   %-10s %-20s %-25s %-20s%n", "AndrewID", "First name", "Last name", "Employer");
		System.out.println("--------------------------------------------------------------------");
		for (Student s: students) {
			System.out.printf("%3d. %-10s %-20s %-25s %-20s%n", i++, s.andrewID, s.firstName, s.lastName, s.companyName);
		}
		System.out.println("====================================================================");

	}
	
	/** loadStudentsList() reads the data from FILE_NAME 
	 * and loads students arrayList with it. 
	 */
	void loadStudentsList() {
		//write your code here
		String[] rosterStrings;
		Scanner fileContent = null;
		StringBuilder rosterData = new StringBuilder();
		try {
			fileContent = new Scanner (new File(FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileContent.hasNextLine()) {
			rosterData.append(fileContent.nextLine() + "\n");
		}
		rosterStrings = rosterData.toString().split("\n"); 
		String[] temp  =  new String [7];
		for(int i=0; i<rosterStrings.length;i++){
			temp =rosterStrings[i].toString().split(",");
			students.add(new Student(temp[0].charAt(0),temp[1],temp[2],temp[3],temp[4],Integer.parseInt(temp[5]), temp[6])); 
		}
	}
	
	class StudentCompanyComparator implements Comparator <Student>{
		@Override
		public int compare(Student s1, Student s2) {
			return (s1.companyName.compareTo(s2.companyName));
		}
	} 
	
	class CompanyHiredCountComparator implements Comparator <Company>{
		@Override
		public int compare(Company c1, Company c2) {
			return (c2.hiredCount-c1.hiredCount);
		}
	} 
	

	void loadCompaniesList() {
		Collections.sort(students, new StudentCompanyComparator());
		Iterator<Student> iter =students.iterator();
		Student s = iter.next();
		int count=1;
		while (iter.hasNext()) {
			Student snext = iter.next();
			if(s.companyName.equals(snext.companyName)) {
				count++;
			}
			else {
				Company c = new Company(s.companyRank,s.companyName);
				c.hiredCount = count;
				companies.add(c);
				s = snext;
				count=1;
			}
		}
		Company c = new Company(s.companyRank, s.companyName);
		c.hiredCount = count;
		companies.add(c);
		
		for(int i=0; i<companies.size();i++) {
			Company.overallHiredCount+=companies.get(i).hiredCount;
		}
		
	}
	

}
