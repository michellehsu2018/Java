package lab8;


public class Doctor implements Runnable{
	int consultationTime;
	int pcount = 0;
	Doctor(int consultationTime){
		this.consultationTime = consultationTime;
	}
	@Override 
	public void run() {
		Patient p;
		while (pcount < ClinicManager.maxPatientCount) {
			synchronized (Clinic.patientQ) {
				p = Clinic.patientQ.poll();
			}
			if (p != null) {
				try {
					p.endTime = System.currentTimeMillis();
					ClinicManager.patientWaitTime += p.endTime - p.startTime;
					Thread.sleep(consultationTime);
					System.out.printf("Consulting patient %d%n", (p.id+1));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pcount++;
			} 
		} 
	} 
	
}
