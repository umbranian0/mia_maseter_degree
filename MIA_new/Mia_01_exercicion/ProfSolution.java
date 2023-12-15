package Mia_01_exercicion;

import java.util.ArrayList;
import java.util.List;

public class ProfSolution {

	private static final int N = 5;
	private static final long MAX_LIMIT = 100000;

	public static void main(String[] args) throws InterruptedException {
		class SumPrimeNumbers extends Thread {
			private long minLimit;
			private long maxLimit;
			private long partialSum;

			public SumPrimeNumbers(long minLimit, long maxLimit) {
				this.minLimit = minLimit;
				this.maxLimit = maxLimit;
			}

			public long getPartialSum() {
				return partialSum;
			}

			boolean isPrime(long n) {
				if (n <= 1)
					return false;

				for (long i = 2; i < n; i++)
					if (n % i == 0)
						return false;

				return true;
			}

			@Override
			public void run() {
				System.out
						.println("Calculating the sum of prime numbers between " + minLimit + " and " + maxLimit + ".");
				partialSum = 0;
				for (long n = minLimit; n < maxLimit; n++)
					if (isPrime(n))
						partialSum += n;
			}
		}

		long step = MAX_LIMIT / N;
		List<SumPrimeNumbers> allThreads = new ArrayList<>();

		for (long i = 0; i <= N; i++) {
			SumPrimeNumbers thread = new SumPrimeNumbers((i * step) + 1, (i + 1) * step);
			allThreads.add(thread);
			thread.start();
		}

		long sum = 0;
		for (SumPrimeNumbers t : allThreads) {
			t.join();
			sum += t.getPartialSum();
		}

		System.out.println("Sum of prime numbers between 1 and " + MAX_LIMIT + ": " + sum);
	}

}
