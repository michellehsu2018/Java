package lab6;

public class Company implements Comparable<Company>{
	static int overallHiredCount;
	int rank;
	String name;
	int hiredCount;
	Company(int rank, String name){
		this.rank = rank;
		this.name = name;
	}
	@Override
	public int compareTo(Company c) {
		Integer r1 = Integer.valueOf(this.rank);
		Integer r2 = Integer.valueOf(c.rank);
		return (r1.compareTo(r2));
	}
}
