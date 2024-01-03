package concurrency_sync_locks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

public class Main {

	/*
	 * Use case: multi-thread to increment integer acessible for multiple threads
	 */
	public static void main(String[] args) {
		// instance a thread pool
		ExecutorService executor = Executors.newFixedThreadPool(5);

		testSynchronized(executor);

		testReentrantLock(executor);

		testStampedLock(executor);

		testReadWriteLock(executor);

		testOptimisticRead(executor);
		/*
		 * output
		 * 
		 * Optimistic Lock Valid: true Write Lock acquired Optimistic Lock Valid: false
		 * Write done Optimistic Lock Valid: false
		 */
		ExecutorService executor2 = Executors.newFixedThreadPool(20);
		testSemaphores(executor2);
		/*
		 * output 
		 * Semaphore acquired 
		 * Semaphore acquired 
		 * Semaphore acquired 
		 * Semaphore acquired 
		 * Semaphore acquired 
		 * Could not acquire semaphore 
		 * Could not acquire semaphore 
		 * Could not acquire semaphore 
		 * Could not acquire semaphore 
		 * Could not acquire semaphore
		 */
		stop(executor);
	}

	private static void testSemaphores(ExecutorService executor) {

		Semaphore semaphore = new Semaphore(1);

		Runnable longRunningTask = () -> {
			boolean permit = false;
			try {
				permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
				if (permit) {
					System.out.println("Semaphore acquired");
					Thread.sleep(5);
				} else {
					System.out.println("Could not acquire semaphore");
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				if (permit) {
					semaphore.release();
				}
			}
		};

		IntStream.range(0, 5).forEach(i -> executor.submit(longRunningTask));

		stop(executor);
	}

	// test optimistic locking
	private static void testOptimisticRead(ExecutorService executor) {
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.tryOptimisticRead();
			try {
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
				Thread.sleep(1);
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
				Thread.sleep(2);
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} finally {
				lock.unlock(stamp);
			}
		});

		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				System.out.println("Write Lock acquired");
				Thread.sleep(2);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} finally {
				lock.unlock(stamp);
				System.out.println("Write done");
			}
		});

		stop(executor);
	}

	private static void testReadWriteLock(ExecutorService executor) {
		Map<String, String> map = new HashMap<>();
		ReadWriteLock lock = new ReentrantReadWriteLock();

		executor.submit(() -> {
			lock.writeLock().lock();
			try {

				Thread.sleep(1);
				map.put("foo", "bar");
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} finally {
				lock.writeLock().unlock();
			}
		});

		Runnable readTask = () -> {
			lock.readLock().lock();
			try {
				System.out.println(map.get("foo"));
				Thread.sleep(1);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} finally {
				lock.readLock().unlock();
			}
		};

		executor.submit(readTask);
		executor.submit(readTask);

		stop(executor);
	}

	private static void testStampedLock(ExecutorService executor) {
		Map<String, String> map = new HashMap<>();
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				Thread.sleep(1);
				map.put("foo", "bar");
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} finally {
				lock.unlockWrite(stamp);
			}
		});

		Runnable readTask = () -> {
			long stamp = lock.readLock();
			try {
				Thread.sleep(1);
				System.out.println(map.get("foo"));
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} finally {
				lock.unlockRead(stamp);
			}
		};

		executor.submit(readTask);
		executor.submit(readTask);

		stop(executor);
	}

	private static void testReentrantLock(ExecutorService executor) {
		// using ReentractLock example
		// it is implemented in Incrementos Class
		Incrementor i1 = new Incrementor();

		executor.submit(() -> {
			i1.incrementLock();
		});

		executor.submit(() -> {
			i1.submitLock();

		});

	}

	private static void testSynchronized(ExecutorService executor) {
		// For simplicity the code samples of this tutorial make use of
		// the two helper methods sleep(seconds) and stop(executor) as defined here.
		Incrementor i1 = new Incrementor();
		Incrementor i2 = new Incrementor();

		// iterate stream and increment method
		IntStream.range(0, 10000).forEach(i -> executor.submit(i1::increment));

		// iterate stream and increment Sync method
		IntStream.range(0, 10000).forEach(i -> executor.submit(i2::incrementSync));

		System.out.println(i1.getCount()); // 9965
		System.out.println(i2.getCount()); // 10000
	}

	private static void stop(ExecutorService executor) {
		// TODO Auto-generated method stub
		System.out.println("stopping executor service");
	}

}
