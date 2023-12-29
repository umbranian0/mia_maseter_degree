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
import java.util.concurrent.ScheduledExecutorService;
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
	 *  2 - Fault Tolerance daemon Thread Verify every 1 second -->
	 * ThreadProducer.isDown ? ThreadConsumer.isDown ?
	 *
	 *  2.1 - Verify if any ThreadProducer.isDown 2.1.1- If ThreadProducer don't
	 * produce values for 10 or more seconds
	 * 
	 *  2.2 - Verify if any ThreadConsumer.isDown 2.2.1- If ThreadConsumer don't
	 * consume values for 30 or more seconds 
	 *  2.3 - Case Producer or Consumer
	 * down 2.3.1 - Instance new Thread to replace the down Thread
	 * 
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
	public static final int NUMBER_MONITOR_THREAD_POOLS = 1;
	// restart timmers
	public final static int PRODUCER_TIMMER = 10;
	public final static int CONSUMER_TIMMER = 30;
	public static final String[][] TYPE_PRODUCERS = { { "CPU" }, { "RAM" }, { "DISK_SPACE" } };

	public static final int NUMBER_PRODUCERS = 3;
	public static final int NUMBER_CONSUMERS = 5;

	// Queue for the producers and consuemrs
	public static BlockingQueue<OutputSpec_v2> queue = new ArrayBlockingQueue<OutputSpec_v2>(100);

	// Daemon thread
	public static MonitoringThread monitorinDeamonThread;
	// checking for any down thread
	// ARRAY TO store all my executors and will be checked by a deamon thread //
	public static CopyOnWriteArrayList<ExecutorService> executorsArrayList = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<Thread> consumerThreadArrayList = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<Thread> producerThreadArrayList = new CopyOnWriteArrayList<>();
	// executors

	public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static ExecutorService monitorExecutor;
	// Cached thread pools, 1 for each, producers and consumers
	public static ExecutorService producerExecutor;
	public static ExecutorService consumerExecutor;

	public static void main(String[] args) throws InterruptedException {
		// Variable to dynamic start / stop program
		boolean isRunning = true;

		GUI.addAlert("Starting Program by default" + isRunning);
		if (isRunning) {
			startThreadPools(isRunning);
		} else {
			stopProgram(isRunning);
		}

	}

	/*
	 * USE Case 2 - Monitoring threads that are stopped Replacing the stopped
	 * Threads by new ones
	 */
	public synchronized static void monitoringDeamonThread(boolean isRunning, ExecutorService executor,
			CopyOnWriteArrayList<Thread> threadArrayList) {

		monitorinDeamonThread = new MonitoringThread(executor, isRunning, threadArrayList, GUI);
		monitorinDeamonThread.setDaemon(true);
		try {
			monitorExecutor.execute(monitorinDeamonThread);
			monitorinDeamonThread.start();
			Thread.yield();
			System.out.println("Starting monitoring thread : " + monitorinDeamonThread.isAlive());
			monitorinDeamonThread.join();

		} catch (InterruptedException e) {
			monitorinDeamonThread.notify();
			e.printStackTrace();
		}
		threadArrayList.add(monitorinDeamonThread);

	}
//periodic task to run depending on type of monitor thread to be started
	public static void schedulePeriodicTask(int intervalSeconds, String type, boolean isRunning) {

		// Runnable task to be executed periodically
		Runnable periodicTask = () -> {
			// Put your logic here
			System.out.println("Executing periodic task...");
			if (type.equals("C")) {
				// checkAndRestartThreads_v2(producerExecutor);
				monitoringDeamonThread(isRunning, consumerExecutor, consumerThreadArrayList);
			}
			if (type.equals("P")) {
				// checkAndRestartThreads_v2(producerExecutor);
				monitoringDeamonThread(isRunning, producerExecutor, producerThreadArrayList);
			}

			// Example: Display current time
			System.out.println("Current time: " + System.currentTimeMillis());
		};

		// Schedule the task to run periodically
		scheduler.scheduleAtFixedRate(periodicTask, 0, intervalSeconds, TimeUnit.SECONDS);
	}
	/*
	 * USE CASE 3 - Safety when closing the program Stop program, stoppiung all
	 * executors and services removing all the executors from the array This is the
	 * safety method that closes the program
	 */

	public static void stopProgram(boolean isRunning) {
		// terminate Daemon Thread
		isRunning = false;
		CopyOnWriteArrayList<Thread> threadArrayList = consumerThreadArrayList;
		threadArrayList.addAll(producerThreadArrayList);
		// close all threads
		for (int i = 0; i < threadArrayList.size(); i++) {

			try {
				Thread.yield();
				threadArrayList.get(i).join();

			} catch (InterruptedException e) {
				threadArrayList.get(i).notify();
				e.printStackTrace();
			}
			threadArrayList.remove(i);
		}
		// shutdown services executors
		for (int j = 0; j < executorsArrayList.size(); j++) {
			if (!executorsArrayList.get(j).isShutdown()) {
				executorsArrayList.get(j).shutdown();
				try {

					executorsArrayList.get(j).awaitTermination(1, TimeUnit.SECONDS);
					executorsArrayList.get(j).shutdownNow();
				} catch (InterruptedException e) {

					executorsArrayList.get(j).shutdownNow();
					e.printStackTrace();
				}
			}

		}

		System.out.println(
				"executors " + executorsArrayList.toString() + " \n thread Array" + threadArrayList.toString());

	}

	/*
	 * USE CASE - 1 Private method to start thread pools with the logic of pools
	 * creation and executros
	 * 
	 */
	private synchronized static void startThreadPools(boolean isRunning) {

		// Starting 2 thread pools, 1 for Producers , 1 for Consumers
		for (int x = 0; x < NUMBER_CACHED_THREAD_POOLS_PRODUCERS; x++) {
			producerExecutor = Executors.newCachedThreadPool();
			executorsArrayList.add(producerExecutor);
		}
		for (int y = 0; y < NUMBER_CACHED_THREAD_POOLS_CONSUMERS; y++) {
			consumerExecutor = Executors.newCachedThreadPool();
			executorsArrayList.add(consumerExecutor);
		}
		for (int k = 0; k < NUMBER_MONITOR_THREAD_POOLS; k++) {
			monitorExecutor = Executors.newSingleThreadExecutor();
			executorsArrayList.add(monitorExecutor);
		}

		while (isRunning) {

			// instance Consumers in Consumer Thread Pool
			for (int i = 1; i <= NUMBER_CONSUMERS; i++) {
				Thread c = new Thread(new Consumer(i, queue, isRunning, GUI));
				try {
					// execute task
					if (!consumerExecutor.isShutdown()) {
						consumerExecutor.execute(c);
					}
					consumerExecutor.awaitTermination(1, TimeUnit.SECONDS);
					consumerThreadArrayList.add(c);
				} catch (InterruptedException e) {
					consumerExecutor.shutdownNow();
					e.printStackTrace();
				}

			}
			// instance 3 producers
			// dynamic based on global variable
			for (int j = 1; j <= NUMBER_PRODUCERS; j++) {

				// case solution
				// create executors process for each type of output
				Thread p;
				switch (j) {
				case 1: {
					p = new Thread(new Producer(j, queue, isRunning, GUI, "CPU"));
					break;
				}
				case 2: {
					p = new Thread(new Producer(j, queue, isRunning, GUI, "RAM"));
					break;
				}
				case 3: {
					p = new Thread(new Producer(j, queue, isRunning, GUI, "DISK_SPACE"));
					;
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + j);
				}

				try {
					// execute task
					if (!producerExecutor.isShutdown()) {
						producerExecutor.execute(p);
						producerThreadArrayList.add(p);

					}
					producerExecutor.awaitTermination(1, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					producerExecutor.shutdownNow();
					e.printStackTrace();
				}

			}
			// if (executorsArrayList.isEmpty()) {
			// use case 2 - re instance threads stopped...
			if (monitorinDeamonThread == null) {
				// monitoringDeamonThread(isRunning);
				schedulePeriodicTask(CONSUMER_TIMMER, "C", isRunning);
				schedulePeriodicTask(PRODUCER_TIMMER, "P", isRunning);
			}
		}

	}
}
