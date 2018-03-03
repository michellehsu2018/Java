package lab9;

public class Group extends Guest{
	static int groupCount;
	@Override
	public void placeOrder() {
		drink = 4;
		food = 4;
		guestCount++;
		groupCount++;
	}
	

}
