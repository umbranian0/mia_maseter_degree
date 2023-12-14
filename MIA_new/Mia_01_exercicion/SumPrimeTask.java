package Mia_01_exercicion;

public class SumPrimeTask {
	private int totalThreads;
	// attributes
	private int limit;
	private int segmentSize;
	private int start;
	private int end;
	private int totalSum;
	private long endTime;
	private long startTime;

	// constructor
	public SumPrimeTask(int limit) {
		this.limit = limit;
		this.start = 2; // can start with 0, but we start with our First Prime = 2
		this.end = segmentSize;
		this.totalSum = 0;
		this.calculateThreadNumberDynamic();
		this.calcSegmentSize();
		this.main();
	}
	
	// constructor
	public SumPrimeTask(int limit, int threads) {
		this.limit = limit;
		this.start = 2; // can start with 0, but we start with our First Prime = 2
		this.end = segmentSize;
		this.totalSum = 0;
		this.totalThreads = threads;
		this.calcSegmentSize();
		this.main();
	}

	private void main() {

		Thread[] threads = new Thread[totalThreads];
		CountTask[] tasks = new CountTask[totalThreads];

		startTime = System.currentTimeMillis();
		initializeThreads(threads, tasks);
		joinThreads(threads, tasks);
		endTime = System.currentTimeMillis();
	}

	private void initializeThreads(Thread[] threads, CountTask[] tasks) {
		for (int i = 0; i < totalThreads; i++) {
			if (i == totalThreads - 1) {
				// Last thread takes care of remaining numbers
				this.end = limit;
			}
			tasks[i] = new CountTask(end, start);
			threads[i] = new Thread(tasks[i]);
			threads[i].start();
			start = end + 1;
			end += segmentSize;
			
		}
	}

	private void joinThreads(Thread[] threads, CountTask[] tasks) {
		for (int i = 0; i < totalThreads; i++) {
			try {
				threads[i].join();
				this.totalSum += tasks[i].getSum();
				// System.out.println("sum value" + tasks[i].getSum());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int calculateThreadNumberDynamic() {

		if (getLimit() >= 10000 && getLimit() <= 1000000) {
			setNumThreads(10);					 
		}else {
			setNumThreads(5);
		}

		return this.getNumThreads();
	}

	private int calcSegmentSize() {
		setSegmentSize(getLimit() / getNumThreads());
		
		return getSegmentSize();
	}
	
	// Assessors

	public int getLimit() {
		return limit;
	}

	public int getSegmentSize() {
		return segmentSize;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getNumThreads() {
		return totalThreads;
	}

	// dynamic set threads
	public void setNumThreads(int threads) {
		this.totalThreads = threads;
	}
	
	public void setSegmentSize(int segmentSize) {
		this.segmentSize = segmentSize;
	}

	public int getTotalSum() {
		return totalSum;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	@Override
	public String toString() {
		return "SumPrimeTask [limit=" + limit + ", segmentSize=" + segmentSize + ", start=" + start + ", end=" + end
				+ ", totalSum=" + totalSum + ", endTimeTimmer=" + endTime + ", startTimeTimmer=" + startTime + "]";
	}

}
