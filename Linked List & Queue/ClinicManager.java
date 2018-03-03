package lab8;

import java.util.Scanner;

public class ClinicManager {
	
	static int maxPatientCount = 0;		//to be input by the user
	static long patientWaitTime = 0;	//sum of time spent by all patients in the clinic 
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter max patient count");
		maxPatientCount = input.nextInt();
		System.out.println("Enter patient's max arrival gap (ms)");
		Clinic clinic = new Clinic(input.nextInt());
		
		System.out.println("Enter Doctor's consultation time per patient (ms)");
		Doctor doc = new Doctor(input.nextInt());
		
		input.close();
		
		Thread t1 = new Thread(clinic);
		Thread t2 = new Thread(doc);
		t1.start();
		t2.start();
		
		try {
			t1.join();		//wait for t1 to complete and join main thread
			t2.join();		//wait for t2 to complete and join main thread
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long clinicCloseTime = System.currentTimeMillis();	
		System.out.printf("Final queue length: %d%n", Clinic.patientQ.size());
		System.out.printf("Total patients consulted: %d%n", clinic.patientCount);
		System.out.printf("Average patient wait time: %,.1fms%n", (double)patientWaitTime / clinic.patientCount);
		System.out.printf("Clinic run time: %dms%n", clinicCloseTime - clinic.clinicOpenTime);	
	}
}
