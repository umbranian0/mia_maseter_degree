package Mia_01_exercicion;

public class TotalCounter {
	private long totalCounter;
	public TotalCounter() {
		// TODO Auto-generated constructor stub
		this.totalCounter=0;
	}
	
	//methods
	public synchronized void increment(long i) {
		setTotalCounter(getTotalCounter()+i);
	}

	public synchronized long getTotalCounter() {
		return totalCounter;
	}

	public void setTotalCounter(long totalCounter) {
		this.totalCounter = totalCounter;
	}
	
}	
