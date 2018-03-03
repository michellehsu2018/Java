package lab9;

public class Individual extends Guest{
	static int individualCount;
	@Override
	public void placeOrder() {
		drink = 1;
		food = 1;
		guestCount++;
		individualCount++;
		
	}
}
