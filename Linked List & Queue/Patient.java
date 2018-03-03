package lab8;

public class Patient {
	static int count;
	long startTime;
	long endTime;
	int id;
	Patient(){
		id = count++;
		startTime = System.currentTimeMillis();
		//record the start time of waiting when the patient enter the queue
	}
}
