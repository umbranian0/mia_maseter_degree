package Exemplo_Produtor_Consumidor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ExamplePC {
	private static final int N_PRODUCERS = 3;
	private static final int N_CONSUMERS = 3;

	public static void main(String[] args) throws InterruptedException {
		//startProgram();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void startProgram() throws InterruptedException {

		class Producer implements Runnable {
			private BlockingQueue<Integer> queue;
			private int id;

			public Producer(int id, BlockingQueue<Integer> queue) {
				this.queue = queue;
				this.id = id;
			}

			@Override
			public void run() {
				try {
					while (true) {
						int rNumber = ThreadLocalRandom.current().nextInt(-1, 101);
						System.out.println("id : " + id + " , P: " + rNumber);
						
						if (queue.offer(rNumber, 3, TimeUnit.SECONDS))
							break;
						
						if (rNumber == -1)
							break;
						queue.put(rNumber);
					}
				} catch (InterruptedException e) {
				}
			}
		}

		class Consumer implements Runnable {
			private BlockingQueue<Integer> queue;
			private int id;

			public Consumer(int id, BlockingQueue<Integer> queue) {
				this.queue = queue;
				this.id = id;
			}

			@Override
			public void run() {
				try {
					while (true) {
						Integer number = queue.poll(5, TimeUnit.SECONDS);
						System.out.println("id : " + id + " , C: " + number);

						if (number == null)
							break;
				
						if (number == -1)
							break;
					}
				} catch (InterruptedException e) {
				}
			}
		}

		BlockingQueue queue = new ArrayBlockingQueue<Integer>(5);
		ExecutorService executor = Executors.newCachedThreadPool();

		// ConcurrentHashMap sum = new ConcurrentHashMap(5);
		// Thread consumer = new Consumer(queue);
		// Thread producer = new Producer(queue);
		for (int i = 0; i < N_CONSUMERS; i++) {
			executor.execute(new Consumer(i, queue));
		}
		for (int i = 0; i < N_PRODUCERS; i++) {
			executor.execute(new Producer(i, queue));
		}

		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.MINUTES);

	}
}