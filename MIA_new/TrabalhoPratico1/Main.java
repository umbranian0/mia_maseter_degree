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
	 * down 2.3.1 - Instance new Thread to replace the down Thread TODO
	 * 
	 * 3 - The system should be configured to end the system safely There must be a
	 * button or a string command line to stop the program all threads must be
	 * finished before system shut down
	 * 
	 * 
	 */
	public static final ResourceMonitorGUI GUI = new ResourceMonitorGUI();

	public static final int NUMBER_CACHED_THREAD_POOLS_CONSUMERS = 1;
	public static final int NUMBER_CACHED_THREAD_POOLS_PRODUCERS = 1;
	public static final String[][] TYPE_PRODUCERS = { { "CPU" }, { "RAM" }, { "DISK_SPACE" } };

	public static final int NUMBER_PRODUCERS = 3;
	public static final int NUMBER_CONSUMERS = 10;

	// Variable to dynamic start / stop program
	public static boolean isRunning = true;

	// Queue for the producers and consuemrs
	public static BlockingQueue<OutputSpec_v2> queue = new ArrayBlockingQueue<OutputSpec_v2>(100);

	// checking for any down thread
	// ARRAY TO store all my executors and will be checked by a deamon thread //
	public static CopyOnWriteArrayList<ExecutorService> executorsArrayList = new CopyOnWriteArrayList<>();
	public static MonitoringThread monitoringThread;
	// Cached thread pools, 1 for each, producers and consumers
	public static ExecutorService producerExecutor;
	public static ExecutorService consumerExecutor;

	public static void main(String[] args) throws InterruptedException {
		GUI.addAlert("Starting Program by default"+isRunning);
		
		if (isRunning) {
			startThreadPools();
			// use case 2 - Instance Producer / Consumer Threads that are down
			if (monitoringThread != null ) {
				monitoringDeamonThread();
			}
		}

	}

	/*
	 * USE Case 2 - Monitoring threads that are stopped Replacing the stopped
	 * Threads by new ones
	 */
	public static void monitoringDeamonThread() {
		monitoringThread = new MonitoringThread(executorsArrayList, GUI);
		monitoringThread.setDaemon(true);
		monitoringThread.run();
	}

	/*
	 * USE CASE 3 - Safety when closing the program Stop program, stoppiung all
	 * executors and services removing all the executors from the array This is the
	 * safety method that closes the program
	 */
	public static void stopProgram() {
		// terminate Daemon Thread
		if (monitoringThread != null && monitoringThread.deamonThreadIsAlive()) {
			monitoringThread.terminateThread();
		}
		// terminate program
		isRunning = false;
		// stop thread pools
		if (!executorsArrayList.isEmpty()) {
			for (ExecutorService executorService : executorsArrayList) {
				executorService.shutdown();
				try {
					if (!executorService.awaitTermination(60, TimeUnit.MILLISECONDS))
						executorService.shutdown();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					executorService.shutdown();
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
				executorsArrayList.remove(executorService);

				System.out.println(
						"removing " + executorService.toString() + " terminated? " + executorService.isShutdown());
			}
			System.out.println("conusmer" + consumerExecutor.toString());
			System.out.println("producer" + producerExecutor.toString());
		}

		System.out.println("stop program");
	}

	/*
	 * USE CASE - 1 Private method to start thread pools with the logic of pools
	 * creation and executros
	 * 
	 */
	private synchronized static void startThreadPools() {
		// Starting 2 thread pools, 1 for Producers , 1 for Consumers
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
			// instance Consumers in Consumer Thread Pool
			// dynamic based on global variable

			for (int i = 1; i <= NUMBER_CONSUMERS; i++) {
				consumerExecutor.execute(new Consumer(i, queue, isRunning, GUI));
				
			}
			// instance 3 producers
			// dynamic based on global variable
			for (int j = 1; j <= NUMBER_PRODUCERS; j++) {
				// dynamic solution
				// producerExecutor.execute(new Producer(j, queue, isRunning,
				// GUI,TYPE_PRODUCERS[j].toString()));

				// case solution
				// create executors process for each type of output
				if(isRunning)
				switch (j) {
				case 1: {
					producerExecutor.execute(new Producer(j, queue, isRunning, GUI, "CPU"));
					break;
				}
				case 2: {
					producerExecutor.execute(new Producer(j, queue, isRunning, GUI, "RAM"));

					break;
				}
				case 3: {
					producerExecutor.execute(new Producer(j, queue, isRunning, GUI, "DISK_SPACE"));

					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + j);
				}

			}
		}

	}

}
