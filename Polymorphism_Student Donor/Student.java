package lab4;

public abstract class Student {
	char section;
	String lastName;
	String firstName;
	String middleInitial;
	String andrewID;
	int age;
	double income;
	double donation;

	static double totalMoneyDonations;
	static int totalTimeDonations;

	Student(char section, String lastName, String firstName, String middleInitial, String andrewID, int age, double income) {
		this.section = section;
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.andrewID = andrewID;
		this.age = age;
		this.income = income;

	}
	abstract Student donate(); 

}
