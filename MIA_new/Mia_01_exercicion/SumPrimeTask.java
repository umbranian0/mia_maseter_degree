package Mia_01_exercicion;

public class SumPrimeTask {
	private int totalThreads;
	// attributes
	private long limit;
	private long segmentSize;
	private long start;
	private long end;
	private long totalSum;
	private long endTime;
	private long startTime;
	private TotalCounter count = new TotalCounter();;

	// constructor
	public SumPrimeTask(long limit) {
		this.limit = limit;
		this.start = 2; // can start with 0, but we start with our First Prime = 2
		this.end = segmentSize;
		this.totalSum = 0;
		this.calculateThreadNumberDynamic();
		this.calcSegmentSize();
		this.run();
	}
	
	// constructor
	public SumPrimeTask(long limit, int threads) {
		this.limit = limit;
		this.start = 2; // can start with 0, but we start with our First Prime = 2
		this.end = segmentSize;
		this.totalSum = 0;
		this.totalThreads = threads;
		this.calcSegmentSize();
		this.run();
	}

	private void run() {

		Thread[] threads = new Thread[totalThreads];
		CountTask[] tasks = new CountTask[totalThreads];
		startTime = System.currentTimeMillis();
		
		initializeThreads(threads, tasks);
		joinThreads(threads, tasks);
		
		endTime = System.currentTimeMillis();
	}
	//we start the thereads individually
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
	//we join threads so we can wait for all threads to End to Join 
	//join will wait for the end of the thread
	private void joinThreads(Thread[] threads, CountTask[] tasks) {
		for (int i = 0; i < totalThreads; i++) {
			try {
				threads[i].join();
				this.totalSum += tasks[i].getSum();
				//partilha de counter por todas as threads
				this.count.increment( tasks[i].getSum());	
				// System.out.println("sum value" + tasks[i].getSum());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int calculateThreadNumberDynamic() {

		if (getLimit() >= 10000 && getLimit() <= 1000000) {
			setNumThreads(20);					 
		}else {
			setNumThreads(10);
		}

		return this.getNumThreads();
	}

	private long calcSegmentSize() {
		setSegmentSize(getLimit() / getNumThreads());
		
		return getSegmentSize();
	}
	
	// Assessors

	public long getLimit() {
		return limit;
	}

	public long getSegmentSize() {
		return segmentSize;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public int getNumThreads() {
		return totalThreads;
	}

	// dynamic set threads
	public void setNumThreads(int threads) {
		this.totalThreads = threads;
	}
	
	public void setSegmentSize(long l) {
		this.segmentSize = l;
	}

	public long getTotalSum() {
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
