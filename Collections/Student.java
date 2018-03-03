package lab6;

public class Student implements Comparable<Student> {
	char section;
	String firstName, lastName, middleinitial;
	String andrewID;
	int companyRank;
	String companyName;
	public Student(char section,String firstName,String lastName, String middleinitial, String andrewID,int companyRank,String companyName ){
		this.section = section;
		this.firstName =firstName;
		this.lastName = lastName;
		this.middleinitial = middleinitial;
		this.andrewID = andrewID;
		this.companyRank = companyRank;
		this.companyName = companyName;
	}
	@Override
	public int compareTo(Student s) {
		return (this.andrewID.compareTo(s.andrewID));
	}

}
