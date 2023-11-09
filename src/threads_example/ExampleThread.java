package threads_example;

public class ExampleThread extends Thread {
	private boolean status;
	private static int countRuns;

	/*
	 * The Thread class defines a number of methods useful for thread management.
	 * These include static methods, which provide information about, or affect the
	 * status of, the thread invoking the method. The other methods are invoked from
	 * other threads involved in managing the thread and Thread object. We'll
	 * examine some of these methods in the following sections.
	 */
	// constructors
	public ExampleThread(boolean status) {
		super();
		this.status = status;
		countRuns = 0;
	}

	public ExampleThread() {
		super();
		this.status = true;
		countRuns = 0;
	}

	public void main(String args[]) {
		(new ExampleThread()).start();
	}

	// subclass Thread with a own method Run()
	public void run() {
		
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
	
	// getters and setters
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getCountRuns() {
		return countRuns;
	}

	public static void setCountRuns(int countRuns) {
		ExampleThread.countRuns = countRuns;
	}

	@Override
	public String toString() {
		return "ExampleThread [status=" + status + ", isStatus()=" + isStatus() + ", countRuns()=" + getCountRuns()
				+ "]";
	}

}
