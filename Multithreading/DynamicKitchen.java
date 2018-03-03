package lab9;

import java.util.Queue;



public class DynamicKitchen implements Runnable {
	Queue<Guest> guestQ;
	int income;
	int foodStock = 175;  
	int drinkStock = 175;
	int foodRate = 4;
	int drinkRate = 2;
	boolean underStock = false;

	DynamicKitchen(Queue<Guest> guestQ) {
		this.guestQ = guestQ;
	}
	
	/** run() runs while the Kitchen's underStock is false, Diner's dinerOpen is true.
	 * It polls guestQ, invokes polled guest's placeOrder() method, 
	 * updates kitchen stock, and increments income accordingly. 
	 * Note that placeOrder() method sets the number of food and drink items ordered
	 * which is then used to reduce kitchen stock, and calculate income.
	 * It also keeps track of how many guests it has served.
	 * When its drink or food stock reaches 4 or below, it sets 
	 * the underStock flag to true. 
	 * Once done, it captures the kitchen's close time in Diner's endTime variable.
	 * Finally it invokes its printReport() method to print all output.
	 */
	@Override
	public void run() {
		//write your code here
		Guest g;
		int n = 5 + (int) (Math.random() * 15);
		while (!underStock && DynamicDiner.dinerOpen) {
			synchronized (guestQ) {
				g = guestQ.poll();
				if (g != null) {
					g.placeOrder();
						foodStock -= g.food;
						drinkStock -= g.drink;
						income += (g.food * foodRate + g.drink * drinkRate);
						if (foodStock <= 4||drinkStock<=4) {
							underStock = true;
						}
					
					try {
						Thread.sleep(n);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}

		}
		DynamicDiner.endTime= System.currentTimeMillis();
		printReport();
		
	}

	void printReport() {
		System.out.println("-------------- Guests--------------");
		System.out.printf("%-25s%5d%n", "Total guests entered:", DynamicDiner.guestsEntered);
		System.out.printf("%-25s%5d%n", "Individuals served:", Individual.individualCount);
		System.out.printf("%-25s%5d%n", "Groups served:", Group.groupCount);
		System.out.printf("%-25s%5d%n", "Total guests served:", Guest.guestCount);
		System.out.printf("%-25s%5d%n", "Guests declined service:", guestQ.size());
		System.out.println("--------- Kitchen -----------");
		System.out.printf("%-25s%5d%n%-25s%5d%n", "Drinks left:", drinkStock, "Food left:", foodStock);
		System.out.printf("%-25s%s%n", "Closing status", (underStock) ? "Under stock" : "Overstock" );
		System.out.println("-------------- Diner -------------- ");
		System.out.printf("%-25s%5d%n", "Max Q length", DynamicDiner.maxQLength);
		System.out.printf("%-25s%,d ms%n", "Diner was open for: ", DynamicDiner.endTime - DynamicDiner.startTime);
		System.out.printf("%-25s$%,5d%n", "Income:", income);
		System.out.println("-----------------------------------");
	}
}
