package threads_example;

public class ExampleRunnable implements Runnable {

	/*
	 * employs a Runnable object, is more general, because the Runnable object can
	 * subclass a class other than Thread.
	 */
	private boolean status;
	private int countRuns;

	//constructors
	public ExampleRunnable(boolean status) {
		super();
		this.status = status;
		countRuns = 0;
	}

	public ExampleRunnable() {
		super();
		this.status = true;
		countRuns = 0;
	}

	
	// method Run that executed in the Thread
	public void run() {
		// while status is not false, will run the theread
		while(isStatus()) {
			System.out.println(toString());
			incrementCounter();
		}
		

	}
	private void incrementCounter() {
		if(countRuns <=5)
		countRuns++;
		else
			setStatus(false);
	}
	public void main(String args[]) {
		(new Thread(new ExampleRunnable())).start();
	}
	
	//getters and setters
	public boolean isStatus() {
		return status;
	}

	public int getCountRuns() {
		return countRuns;
	}

	public void setCountRuns(int countRuns) {
		this.countRuns = countRuns;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}



	@Override
	public String toString() {
		return "ExampleRunnable [status=" + status + ", isStatus()=" + isStatus() 
				+ ", getCountRuns()=" + getCountRuns()				+ "]";
	}
}
