package lab9;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class DynamicDiner implements Runnable {
	Queue<Guest> guestQ = new LinkedList<>();
	DynamicKitchen kitchen = new DynamicKitchen(guestQ);
	static long startTime, endTime; //startTime captured by Diner. endTime captured by Kitchen
	static boolean dinerOpen = true; 
	static int maxQLength; 
	static int guestsEntered;

	public static void main(String[] args) {
		DynamicDiner diner = new DynamicDiner();
		startTime = System.currentTimeMillis();
		diner.openDiner();
	}
	
	
	/** run() runs while dinerOpen is true. 
	 * In an interval from 1 to 30 ms, it randomly creates either a new Individual or 
	 * a Group object and offers it to guestQ. As a guest is added, guestsEntered is updated, 
	 * maxQLength is compared with current guestQ size, and updated if need be. 
	 * It then checks if the current system time is equal to or 
	 * more than 1000 ms since startTime. If yes, then it sets dinerOpen to false
	 * Note than all the above is done in a synchronized block to ensure atomicity.
	 */
	@Override
	public void run() {
		//write your code here
		
		Guest g;
		
		while (dinerOpen) {
		 Random r = new Random();
		 int n = r.nextInt(2);
		 int i =r.nextInt(30)+1;
			try {
				if (n == 0) {
					g = new Group();
				} else {
					g = new Individual();
				}
				synchronized (guestQ) {
					guestQ.offer(g);
					guestsEntered++;
					if (guestQ.size() > maxQLength) {
						maxQLength = guestQ.size();
					}
				}
				Thread.sleep(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if((System.currentTimeMillis() - startTime) > 1000){
				dinerOpen = false;
			}
		}
		
	}

	/** openDiner() creates threads for Diner and Kitchen and starts them off
	 * It then waits for them to join. */
	void openDiner() {
		//write your code here
		Thread t1 = new Thread(this);
		Thread t2 = new Thread(this.kitchen);
		t1.start();
		t2.start();
		try {
			t1.join();		
			t2.join();		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
