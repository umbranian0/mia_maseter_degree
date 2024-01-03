package concurrency_sync_locks;

import java.util.concurrent.locks.ReentrantLock;

public class Incrementor {

	// atributes
	private int count;
	// instance of ReentrantLock to be used in fucntion incrementLock
	private ReentrantLock lock = new ReentrantLock();

	public Incrementor() {

	}

	public Incrementor(int count) {
		super();
		this.count = count;
	}

	// Creating ReentrantLock
	public void incrementLock() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void submitLock() {
		System.out.println("Locked: " + lock.isLocked());
		System.out.println("Held by me: " + lock.isHeldByCurrentThread());
		boolean locked = lock.tryLock();
		System.out.println("Lock acquired: " + locked);
	}


	// increment method
	public void increment() {
		this.count++;
	}

	// increment method sync
	// here is used the Lock or intrinsic Lock to order manage syncronization
	synchronized void incrementSync() {
		count = count + 1;
	}

	// acessors
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public void setLock(ReentrantLock lock) {
		this.lock = lock;
	}
	@Override
	public String toString() {
		return "Incrementor [count=" + count + "]";
	}

}
