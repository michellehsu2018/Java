package lab4;


public class DonorBank {
	University cmu = new University();
	
	public static void main(String[] args) {
		DonorBank db = new DonorBank();
		db.getDonations();
		db.printDonations();
	}

	void printDonations() {
		System.out.printf("Section A money donations: $%,15.2f%n", StudentA.studentADonations);
		System.out.printf("Section B money donations: $%,15.2f%n", StudentB.studentBDonations);
		System.out.printf("Section C money donations: $%,15.2f%n", StudentC.studentCDonations);
		System.out.println("-----------------------------------------------------------");
		System.out.printf("Total money donations:     $%,15.2f%n", Student.totalMoneyDonations);
		System.out.println("-----------------------------------------------------------");
		System.out.printf("Section C time donations: \t%,5d hours%n", Student.totalTimeDonations);
	}
	
	/**getDonations() invokes cmu's loadRosterStrings() method and loadStudentObjects() method. 
	 * It then invokes donate() method on each student in cmu's studentObjects array
	 */
	void getDonations() {
		
		//write your code here
		cmu.loadRosterStrings();
		cmu.loadStudentObjects();

	
	}
	

}


