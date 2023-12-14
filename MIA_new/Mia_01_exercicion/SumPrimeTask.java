package Mia_01_exercicion;

public class SumPrimeTask {
	private static final int NUM_THREADS = 4;
	
//	private int limit;
//	private int segmentSize = limit / NUM_THREADS;
//	private int start = 0;
//	private int end = segmentSize;
//	
	
	
	
	
	public static void main(String[] args,int limit) {
		// TODO Auto-generated method stub
		

		Thread[] threads = new Thread[NUM_THREADS];
		CountTask[] tasks = new CountTask[NUM_THREADS];
		
		int segmentSize = limit / NUM_THREADS;
		int start = 0;
		int end = segmentSize;

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < NUM_THREADS; i++) {
			if (i == NUM_THREADS - 1) {
				// Last thread takes care of remaining numbers
				end = limit;
			}

			tasks[i] = new CountTask(end,start);
			System.out.println("start:" + start + " end:" + end);
			threads[i] = new Thread(tasks[i]);
			threads[i].start();
			start = end + 1;
			end += segmentSize;
		}

		long sum = 0;

		for (int i = 0; i < NUM_THREADS; i++) {
			try {
				threads[i].join();
				sum += tasks[i].getSum();
				System.out.println("sum value" + tasks[i].getSum());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		long endTime = System.currentTimeMillis();

		System.out.println("Sum of prime numbers up to " + limit + ": " + sum);
		System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");

	}

}
