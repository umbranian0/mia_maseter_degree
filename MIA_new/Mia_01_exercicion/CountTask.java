package Mia_01_exercicion;

public class CountTask implements Runnable {
	// attributes
	private int countStart;
	private int countLimit;
	private int sum;

	public CountTask(int countLimit, int countStart) {
		this.countStart = countStart;
		this.countLimit = countLimit;
		this.sum = 0;
	}

	// Methods
	@Override
	public void run() {
		for (int i = this.countStart; i <= this.countLimit; i++) {
			// check if prime
			if (isPrimeNumber(i)) {
				this.sum += i;
				//System.out.println("is prime : " + isPrimeNumber(i) + "sum : " + getSum());
			}
		}

	}

	// function to get if is Prime
	public boolean isPrimeNumber(int num) {
		if (num < 2) {
			return false;
		}
		for (int j = 2; j < Math.sqrt(num); j++) {
			if (num % j == 0)
				return false;
		}
		return true;
	}

	// Assessors

	public int getSum() {
		return sum;
	}

}
