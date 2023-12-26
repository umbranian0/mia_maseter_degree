package TrabalhoPratico1;

import java.io.InterruptedIOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class Main {
	/*
	 * Requirments
	 *
	 * 1 - Base program : publisher <-> consumer 1.1- 3 producers , each 100
	 * milliseconds DONE 1- CPU Load 2- Free RAM 3- Free Disk Space
	 * 
	 * 1.2 - N consumers , N == Config Variable 1.2.1- Consume Producer Data
	 * 1.2.1.2- Alarm - CPU over > than 80% 1.2.1.3- Alarm - Free RAM less < than
	 * 10% 1.2.1.4- Alarm - Storage less < than 20% 1.3- Case Alarm == true 1.3.1
	 * Alert to GUI Interface about the Alarm
	 * 
	 * TODO 2 - Fault Tolerance daemon Thread Verify every 1 second -->
	 * ThreadProducer.isDown ? ThreadConsumer.isDown ?
	 *
	 * TODO 2.1 - Verify if any ThreadProducer.isDown 2.1.1- If ThreadProducer don't
	 * produce values for 10 or more seconds
	 * 
	 * TODO 2.2 - Verify if any ThreadConsumer.isDown 2.2.1- If ThreadConsumer don't
	 * consume values for 30 or more seconds TODO 2.3 - Case Producer or Consumer
	 * down 2.3.1 - Instance new Thread to replace the down Thread TODO 3 - The
	 * system should be configured to end the system safely There must be a button
	 * or a string command line to stop the program all threads must be finished
	 * before system shut down
	 * 
	 * 
	 */
	public static final ResourceMonitorGUI GUI = new ResourceMonitorGUI();

	public static final int NUMBER_CACHED_THREAD_POOLS_CONSUMERS = 1;
	public static final int NUMBER_CACHED_THREAD_POOLS_PRODUCERS = 1;

	public static final int NUMBER_PRODUCERS = 3;
	public static final int NUMBER_CONSUMERS = 5;

	// Variable to dynamic start / stop program
	public static boolean isRunning = true;

	// Queue for the producers and consuemrs
	public static BlockingQueue queue = new ArrayBlockingQueue<OutputSpec>(100);

	// checking for any down thread
	// ARRAY TO store all my executors and will be checked by a deamon thread
	public static CopyOnWriteArrayList<ExecutorService> executorsArrayList = new CopyOnWriteArrayList<>();

	// Cached thread pools, 1 for each, producers and consumers
	public static ExecutorService producerExecutor;
	public static ExecutorService consumerExecutor;

	public static void main(String[] args) {
		GUI.addAlert("Starting Program = True by default");
		if(!isRunning)
			isRunning = true;

		while (isRunning) {
			if (isRunning)
				startThreadPools();
			else if (!isRunning) {
				stopProgram();
			} else {
				continue;
			}
		}
	}

	private static void startThreadPools() {
		System.out.println("start thread pools");
		
		if (executorsArrayList.isEmpty()) {
			for (int x = 0; x < NUMBER_CACHED_THREAD_POOLS_PRODUCERS; x++) {
				producerExecutor = Executors.newCachedThreadPool();
				executorsArrayList.add(producerExecutor);
			}
			for (int x = 0; x < NUMBER_CACHED_THREAD_POOLS_CONSUMERS; x++) {
				consumerExecutor = Executors.newCachedThreadPool();
				executorsArrayList.add(consumerExecutor);
			}
		}

		while (isRunning) {
			// construction 3 producers
			for (int i = 1; i <= NUMBER_PRODUCERS; i++) {
				producerExecutor.execute(new Producer(i, queue, isRunning, GUI));
			}
			for (int i = 0; i <= NUMBER_CONSUMERS; i++) {
				consumerExecutor.execute(new Consumer(i, queue, isRunning, GUI));
			}
		}

	}

	public static synchronized void stopProgram() {
		System.out.println("stop program");
		// stop thread pools
		if (!executorsArrayList.isEmpty()) {
			for (ExecutorService executorService : executorsArrayList) {

				try {
					executorService.shutdown();
					executorService.awaitTermination(1, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				executorsArrayList.remove(executorService);
			}

		}
		isRunning = false;
	}

}
