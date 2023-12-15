package Mia_01_exercicion;

public class CountTask implements Runnable {
	// attributes
	private long countStart;
	private long countLimit;
	private long sum;

	public CountTask(long countLimit, long countStart) {
		this.countStart = countStart;
		this.countLimit = countLimit;
		this.sum = 0;
	}

	// Methods
	@Override
	public void run() {
		for (long i = this.countStart; i <= this.countLimit; i++) {
			// check if prime
			if (isPrimeNumber(i)) {
				this.sum += i;
				//System.out.prlongln("is prime : " + isPrimeNumber(i) + "sum : " + getSum());
			}
		}

	}

	// function to get if is Prime
	public boolean isPrimeNumber(long num) {
		if (num < 2) {
			return false;
		}
		for (long j = 2; j < num; j++) {
			if (num % j == 0)
				return false;
		}
		
		return true ;
	}

	// Assessors

	public long getSum() {
		return sum;
	}
}
