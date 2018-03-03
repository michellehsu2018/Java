package lab4;

public class StudentA extends Student {

	static double studentADonations;
	static final double PERCENT_DONATION  = 0.01;
	
	StudentA(char section, String lastName, String firstName, String middleInitial, String andrewID, int age, double income) {
		super(section, lastName, firstName, middleInitial, andrewID, age, income);
	}
	
	@Override
	Student donate() {
		donation = income * PERCENT_DONATION;  		
		Student.totalMoneyDonations += donation;
		StudentA.studentADonations += donation;
		return this;
	}

}
