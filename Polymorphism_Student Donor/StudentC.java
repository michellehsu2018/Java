package lab4;

import java.util.Random;

public class StudentC extends Student implements Serviceable{

	static double studentCDonations;	
	static final int MAX_MONEY_DONATION=10;
	static int timeDonation;

	StudentC(char section, String lastName, String firstName, String middleInitial, String andrewID, int age, double income) {
		super(section, lastName, firstName, middleInitial, andrewID, age, income);
	}
	@Override
	Student donate() {
		donation = MAX_MONEY_DONATION;
		StudentC.studentCDonations += donation;
		Student.totalMoneyDonations +=donation;
		return this;
	}
	@Override
	public void serve() {
		Random timeDonation=new Random();
		Student.totalTimeDonations+=timeDonation.nextInt((MAX_SERVICE_HOURS+1));
	}
	
} 
