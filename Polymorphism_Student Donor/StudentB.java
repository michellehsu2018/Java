package lab4;

import java.util.Random;

public class StudentB extends Student{

	static double studentBDonations;
	static int MAX_MONEY_DONATION=1000;
	StudentB(char section, String lastName, String firstName, String middleInitial, String andrewID, int age, double income) {
		super(section, lastName, firstName, middleInitial, andrewID, age, income);
	}
	
	@Override
	Student donate() {
		Random r=new Random();
		StudentB.studentBDonations+=r.nextInt((MAX_MONEY_DONATION+1));
		Student.totalMoneyDonations+=studentBDonations;
		return this;
	}

}
