package lab8;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;



public class Clinic implements Runnable{
	static Queue<Patient> patientQ = new LinkedList<>();
	int patientCount;
	long clinicOpenTime;
	int maxPatientArrivalGap;
	Clinic(int maxPatientArrivalGap){
		this.maxPatientArrivalGap = maxPatientArrivalGap;
	}
	
	@Override
	public void run() {
		clinicOpenTime = System.currentTimeMillis();//record the time when the clinic is opened
		Random r = new Random();
		while (patientCount < ClinicManager.maxPatientCount)
			try {
				synchronized (Clinic.patientQ) {//synchronized the patientQ object(the common resource shared by two threads) 
					patientQ.offer(new Patient()); //add the patient into the queue
					patientCount++;
				}
				Thread.sleep(r.nextInt(maxPatientArrivalGap));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 
	}

}
